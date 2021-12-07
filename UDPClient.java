import java.net.DatagramSocket;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import Parsing.Links;


public class UDPClient {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer; 
    private int port;
    private int id;


    // --- CONSTRUCTOR ---
    public UDPClient(int id) {
        try {
            this.datagramSocket = new DatagramSocket();
            this.buffer = new byte[256];
            this.inetAddress = InetAddress.getByName("localhost"); // CHANGE
            this.port = 5000;
            this.id = id;
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPClient constructor NOT YO");
        }
    }
    
    
    // --- CONNECTON ---
    // Sends ID to server
    private void sendID() {
        this.buffer = new byte[256];
        DatagramPacket clientID = null;
        try {
            this.buffer = ByteBuffer.allocate(Integer.BYTES).putInt(this.id).array();
            clientID = new DatagramPacket(this.buffer, this.buffer.length, this.inetAddress, this.port);
            this.datagramSocket.send(clientID);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPClient sendID() NOT YO");
        }
    }

    // Receives Links
    private void receiveLinks() {
        this.buffer = new byte[256];
        DatagramPacket datagramPacket = new DatagramPacket(this.buffer, this.buffer.length);
        Links links = new Links();
        try {
            this.datagramSocket.receive(datagramPacket);

            ByteArrayInputStream bais = new ByteArrayInputStream(this.buffer);
            ObjectInputStream ois     = new ObjectInputStream(bais);
            links = (Links) ois.readObject();
            ois.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPClient receiveLinks() NOT YO");
        }
        links.print();
    }

    // Run client
    public void run() {
        sendID();
        receiveLinks();
        while (true) {
            ;
        }
    }
}