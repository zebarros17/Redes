import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import Parsing.AddressTable;
import Parsing.Links;


public class UDPWorker implements Runnable{
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private AddressTable underlay;
    private InetAddress inetAddress;
    private int port;
    
    // --- CONSTRUCTOR ---
    public UDPWorker(DatagramSocket datagramSocket, DatagramPacket datagramPacket, AddressTable underlay) {
        this.datagramSocket = datagramSocket;
        this.datagramPacket = datagramPacket;
        this.underlay       = underlay;
        this.inetAddress    = this.datagramPacket.getAddress();
        this.port           = this.datagramPacket.getPort();  
    }
    

    // CONNECTIONS
    private void connect() {
        try {
            byte[] buffer = this.datagramPacket.getData();
            int clientID  = ByteBuffer.wrap(buffer).getInt();

            Links clientLinks = this.underlay.getLinks(clientID);
            clientLinks.print();

            ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
            ObjectOutputStream oos     = new ObjectOutputStream(baos);
            oos.writeObject(clientLinks);
            oos.flush();
            oos.close();
            buffer = baos.toByteArray();

            DatagramPacket linksPacket = new DatagramPacket(buffer, buffer.length, this.inetAddress, this.port);
            this.datagramSocket.send(linksPacket);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPWorker NOT YO");
        }
    }
    
    @Override
    public void run() {   
        connect();
    }
}