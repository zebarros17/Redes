package connection;


import data.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;


public class ClientON_C implements Runnable {
    private int      id;
    private Caminhos caminhos;

    private InetAddress serverAddress;
    private int serverPort; 
     
    private DatagramSocket dSocket;


    public ClientON_C(int id, Caminhos caminhos, DatagramSocket dSocket) throws Exception {
        this.id       = id;
        this.caminhos = caminhos;

        //this.serverAddress = InetAddress.getByName("10.0.0.10");
        this.serverAddress = InetAddress.getByName("localhost");
        this.serverPort = 4100 + this.id;

        this.dSocket = dSocket;
    }


    // Receives his links
    private void receiveCaminhos() throws Exception {
        byte[] buffer          = new byte[512];
        DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, this.serverAddress, this.serverPort);
        this.dSocket.receive(dPacket);

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ObjectInputStream ois     = new ObjectInputStream(bais);
        Caminhos caminhos = (Caminhos) ois.readObject();
        this.caminhos.clear();
        this.caminhos.addAll(caminhos);
        ois.close();
        bais.close();
    }


    @Override
    public void run() {
        try {
            while(true) {
                receiveCaminhos();
                TimeUnit.SECONDS.sleep(5);
                byte[] buffer          = new String("WE ARE YO").getBytes(); 
                DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, this.serverAddress, this.serverPort);       
                dSocket.send(dPacket);
            }     
        } 
        catch (Exception e) {
            System.out.println("ClientON_C NOT YO");
            this.dSocket.close();
        }
    }        
}
