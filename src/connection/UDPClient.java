package connection;

import data.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;


public class UDPClient {
    private int      id;
    private Caminhos caminhos;

    private InetAddress serverAddress; 
    private int         serverPort;  

    private int         clientToClientPort; 
    private int         clientToServerPort;
    
    private DatagramSocket dSocketToClient;
    private DatagramSocket dSocketToServer;


    // Constructor
    public UDPClient(int id) throws Exception {
        this.id       = id;
        this.caminhos = new Caminhos();

        this.clientToClientPort = 5000 + this.id;
        this.clientToServerPort = 4000 + this.id;
        this.dSocketToClient    = new DatagramSocket(this.clientToClientPort);
        this.dSocketToServer    = new DatagramSocket(this.clientToServerPort);

        //this.serverAddress = InetAddress.getByName("10.0.0.10"); 
        this.serverAddress = InetAddress.getByName("localhost");
        this.serverPort    = 4001;
    }


    // Sends ON flag to server
    private void turnON() throws Exception {
        byte[] buffer          = ("CON:" + this.id).getBytes("UTF-8");
        DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, this.serverAddress, this.serverPort);
        this.dSocketToServer.send(dPacket);
    }

    // Sends Stream flag to Server
    private void streamON() throws Exception {
        byte[] buffer          = ("SON:" + this.id).getBytes("UTF-8");
        DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, this.serverAddress, this.serverPort);
        this.dSocketToServer.send(dPacket);
    }

    // Run the client
    public void run() throws Exception {
        turnON(); // We first tell the server who we are
        new Thread( new ClientON_C(this.id, this.caminhos, this.dSocketToServer) ).start();

        System.out.println("WE ARE GETTING READY YO");
        TimeUnit.SECONDS.sleep(2);
        System.out.println("WE ARE READY YO");
        System.out.println();
        System.out.println();
        this.caminhos.print();

        streamON(); // Tell the server we are ready to
        //new Thread( new ClientStreamSend(this.caminhos)).start();
    }


}
