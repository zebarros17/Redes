import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Parsing.AddressTable;


public class UDPServer {
    private DatagramSocket datagramSocket;
    private AddressTable underlay;


    // --- CONSTRUCTOR ---
    public UDPServer() {
        try { 
            this.datagramSocket = new DatagramSocket(5000, InetAddress.getByName("localhost")); // CHANGE 
            this.underlay       = new AddressTable("underlay1.xml"); // CHANGE
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPServer NOT OK");
        }
       
    }

    public void run(){
        while (true) {
            byte[] buffer = new byte[256];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            try {
                this.datagramSocket.receive(datagramPacket);
                new Thread( new UDPWorker(this.datagramSocket, datagramPacket, this.underlay) ).start();
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("UDPServer run() NOT YO");
            }
        }
    }
}
