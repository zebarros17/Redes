import Parsing.Links;
import Parsing.Topology;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class UDPWorker implements Runnable{
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private Topology underlay;
    private byte[] buffer;
    

    public UDPWorker(DatagramSocket datagramSocket, DatagramPacket datagramPacket, Topology underlay, byte[] buffer) {
        this.datagramSocket = datagramSocket;
        this.datagramPacket = datagramPacket;
        this.underlay = underlay;
        this.buffer = buffer;
    }

    public void send() {
        try {
            //this.datagramPacket = new DatagramPacket(this.buffer, this.buffer.length, this.datagramPacket.getAddress(), this.datagramPacket.getPort());
            this.datagramSocket.send(datagramPacket);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPServer send() NOT YO");
        }
    }

    public void sendLinks() throws IOException {
        int clientID = Integer.parseInt(this.datagramPacket.getData().toString());
        Links links = this.underlay.getLinks(clientID);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream (baos);
        oos.writeObject(links);
        oos.close();
}
    
    
    @Override
    public void run() {   
        send(); 
    }
}