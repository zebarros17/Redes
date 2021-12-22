package connection;

import data.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class UDPRouter {
    // Variaveis Unicas
    private int      id;
    private Caminhos caminhos;

    // RTP variables
    private InetAddress serverAddress; 
    private int         serverPort;  

    private int         NodoPort; 
    private int         rtpPort; 
    private int         controlPort;
    
    private DatagramSocket dSocketStreamNodo;
    private DatagramSocket dSocketStreamRTP;
    private DatagramSocket dSocketControl;


    // Constructor
    public UDPRouter(int id) throws Exception {
        this.id       = id;
        this.caminhos = new Caminhos();

        this.rtpPort      = 6000 + this.id;
        this.NodoPort     = 5000 + this.id;
        this.controlPort  = 4000 + this.id;
        this.dSocketStreamNodo = new DatagramSocket(this.NodoPort);
        this.dSocketStreamRTP  = new DatagramSocket(this.rtpPort);
        this.dSocketControl    = new DatagramSocket(this.controlPort);

        this.serverAddress = InetAddress.getByName("10.0.0.10"); 
        this.serverPort    = 4001;
    }


    // Sends ON flag to server
    private void turnON() throws Exception {
        byte[] buffer          = ("CON:" + this.id).getBytes(StandardCharsets.UTF_8);
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

        // We listen to packets to send
        while(true) {
            byte[] buffer           = new byte[8];
            DatagramPacket dPacket  = new DatagramPacket(buffer, buffer.length);
            this.dSocketStreamNodo.receive(dPacket);      
            
            ByteBuffer wrapped = ByteBuffer.wrap(buffer);
            int destinoID = wrapped.getInt();
            System.out.println("Destino: " + destinoID);
            new Thread( new StreamSend_R(this.dSocketStreamRTP, this.caminhos, destinoID)).start();
        }
    }
}

