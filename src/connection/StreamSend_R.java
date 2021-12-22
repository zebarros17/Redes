package connection;

import data.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.io.IOException;
import java.io.InterruptedIOException;


public class StreamSend_R extends JFrame implements Runnable, ActionListener {
    // Variáveis unicas
    private Caminhos caminhos;
    private int      destinoID;
    
    // RTP variables
    private InetAddress    proxAddress;
    private int            proxNodoPort;
    private int            proxRTPPort;
    private DatagramSocket dSocket;

    //Video constants:
    int imagenb = 0;   //image nb of the image currently transmitted
    VideoStream video; //VideoStream object used to access video frames
    static int MJPEG_TYPE   = 26;  //RTP payload type for MJPEG video
    static int FRAME_PERIOD = 100; //Frame period of the video to stream, in ms
    static int VIDEO_LENGTH = 500; //length of the video in frames
 
    Timer     cTimer; // timer used to receive data from the UDP socket
    byte[]    cBuf;   // buffer used to store data received from the server

    //GUI:
    JLabel label;



    public StreamSend_R(DatagramSocket dSocket, Caminhos caminhos, int clientID) throws Exception {
        this.caminhos  = caminhos;
        this.destinoID = clientID;
        this.dSocket   = dSocket;
    }


    private void sendNodoDestino() throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.destinoID);
        byte[] buffer = bb.array();

        Caminho caminho   = this.caminhos.getCaminho(this.destinoID);
        this.proxNodoPort = 5000 + caminho.getProxSalto().getID();
        this.proxRTPPort  = 6000 + caminho.getProxSalto().getID();
        this.proxAddress  = InetAddress.getByName(caminho.getProxSalto().getIP4());

        // We send the links
        DatagramPacket caminhosPacket = new DatagramPacket(buffer, buffer.length, proxAddress, proxNodoPort);
        this.dSocket.send(caminhosPacket);
    }


    public void actionPerformed(ActionEvent e) {
        // Construct a DatagramPacket to receive data from the UDP socket
        DatagramPacket rcvdp = new DatagramPacket(cBuf, cBuf.length);
        try {
            //receive the DP from the socket:
            dSocket.receive(rcvdp);
            DatagramPacket senddp = new DatagramPacket(cBuf, cBuf.length, proxAddress, proxRTPPort);
            dSocket.send(senddp);
        }
        catch (InterruptedIOException iioe) {
            System.out.println("Nothing to read");
        }
        catch (IOException ioe) {
            System.out.println("Exception caught: "+ioe);
        }
    }

    @Override
    public void run() {    
        try {
            sendNodoDestino();

            cTimer = new Timer(20, this);
            cTimer.setInitialDelay(0);
            cTimer.setCoalesce(true);
            cBuf = new byte[15000]; 
            this.dSocket.setSoTimeout(5000); // setimeout to 5s
            cTimer.start();
        }
        catch (Exception e) {
            System.out.println("Can't reach " + this.destinoID);
            this.dSocket.close();
        }  
    }
}
