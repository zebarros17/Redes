package connection;

import data.*;
import java.io.*;
import java.net.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;


public class UDPClient {
    // Variáveis unicas
    private int      id;
    private Caminhos caminhos;

    // Para controlo
    private InetAddress    serverAddress; 
    private int            serverPort;  
    private int            controlPort;
    private DatagramSocket dSocketControl;

    // RTP variables
    private int            streamPort;
    private DatagramSocket RTPsocket;

    // GUI
    JFrame    f           = new JFrame("Cliente de Testes");
    JButton   setupButton = new JButton("Setup");
    JButton   playButton  = new JButton("Play");
    JButton   pauseButton = new JButton("Pause");
    JButton   tearButton  = new JButton("Teardown");
    JPanel    mainPanel   = new JPanel();
    JPanel    buttonPanel = new JPanel();
    JLabel    iconLabel   = new JLabel();
    ImageIcon icon;

    Timer     cTimer; // timer used to receive data from the UDP socket
    byte[]    cBuf;   // buffer used to store data received from the server

    // Constructor
    public UDPClient(int id) throws Exception {
        this.id       = id;
        this.caminhos = new Caminhos();
        
        this.serverAddress = InetAddress.getByName("10.0.0.10"); 
        this.serverPort    = 4001;
        
        this.controlPort    = 4000 + this.id;
        this.dSocketControl = new DatagramSocket(this.controlPort);
        

        this.streamPort = 6000 + this.id;
        this.RTPsocket  = new DatagramSocket(this.streamPort);
    }


    
    // ---- Montar janela ----
    class playButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                streamON();
                System.out.println("Play Button pressed !"); 
                cTimer.start();
            } 
            catch (Exception e1) {
                System.out.println("streamON NOT YO");
            } 
        }
    }
    
    //Handler for tear button
    class tearButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Teardown Button pressed !");  
            cTimer.stop();
            System.exit(0);
        }
    }

    class clientTimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Construct a DatagramPacket to receive data from the UDP socket
            DatagramPacket rcvdp = new DatagramPacket(cBuf, cBuf.length);
            try {
                //receive the DP from the socket:
                RTPsocket.receive(rcvdp);
                
                //create an RTPpacket object from the DP
                RTPpacket rtp_packet = new RTPpacket(rcvdp.getData(), rcvdp.getLength());
            
                //get the payload bitstream from the RTPpacket object
                int payload_length = rtp_packet.getpayload_length();
                byte [] payload    = new byte[payload_length];
                rtp_packet.getpayload(payload);
            
                //get an Image object from the payload bitstream
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image image     = toolkit.createImage(payload, 0, payload_length);
                
                // display the image as an ImageIcon object
                icon = new ImageIcon(image);
                iconLabel.setIcon(icon);
            }
            catch (InterruptedIOException iioe) {
                System.out.println("Nothing to read");
            }
            catch (IOException ioe) {
                System.out.println("Exception caught: "+ioe);
            }
        }
    }

    public void buildGUI() {
        //Frame
        f.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){System.exit(0);}});

        //Buttons
        buttonPanel.setLayout(new GridLayout(1,0));
        buttonPanel.add(setupButton);
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(tearButton);

        // handlers... (so dois)
        playButton.addActionListener(new playButtonListener());
        tearButton.addActionListener(new tearButtonListener());

        //Image display label
        iconLabel.setIcon(null);
        
        //frame layout
        mainPanel.setLayout(null);
        mainPanel.add(iconLabel);
        mainPanel.add(buttonPanel);
        iconLabel.setBounds(0,0,380,280);
        buttonPanel.setBounds(0,280,380,50);

        f.getContentPane().add(mainPanel, BorderLayout.CENTER);
        f.setSize(new Dimension(390,370));
        f.setVisible(true);
        
        cTimer = new Timer(20, new clientTimerListener());
        cTimer.setInitialDelay(0);
        cTimer.setCoalesce(true);
        cBuf = new byte[15000]; 

        try {
            RTPsocket.setSoTimeout(5000); // setimeout to 5s
        } 
        catch (SocketException e) {
            System.out.println("Cliente: erro no socket: " + e.getMessage());
        }
    }


    // Sends ON flag to server
    private void turnON() throws Exception {
        byte[] buffer          = ("CON:" + this.id).getBytes(StandardCharsets.UTF_8);
        DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, this.serverAddress, this.serverPort);
        this.dSocketControl.send(dPacket);
    }

    // Sends Stream flag to Server
    private void streamON() throws Exception {
        byte[] buffer          = ("SON:" + this.id).getBytes(StandardCharsets.UTF_8);
        DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, this.serverAddress, this.serverPort);
        this.dSocketControl.send(dPacket);
    }

    // Run the client
    public void run() throws Exception {
        turnON(); // We first tell the server who we are
        new Thread( new ClientON_CR(this.id, this.caminhos, this.dSocketControl) ).start();

        System.out.println("WE ARE GETTING READY YO");
        TimeUnit.SECONDS.sleep(5);
        System.out.println("WE ARE READY YO");
        
        buildGUI();
    }
}
