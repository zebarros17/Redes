package connection;

import data.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;


public class ClientON_S implements Runnable {
    private int clientID;
    private Underlay underlay;
    private Caminhos caminhos;

    private InetAddress iAddress;
    private InetAddress clientAddress;
    private int         serverPort;
    private int         clientPort;

    private DatagramSocket dSocket;
    

    public ClientON_S(Underlay underlay, Caminhos caminhos , int clientID, DatagramPacket dPacket) throws Exception {
        this.clientID = clientID;
        this.underlay = underlay;
        this.caminhos = caminhos;

        this.iAddress  = InetAddress.getByName("10.0.0.10");
        this.serverPort = 4100 + clientID;

        this.clientAddress = dPacket.getAddress();
        this.clientPort    = dPacket.getPort();

        this.dSocket = new DatagramSocket(this.serverPort, this.iAddress);
    }


    private void sendCaminhos() throws Exception {        
        Caminhos clientCaminhos = Caminhos.calculaCaminhos(this.clientID, this.underlay);

        this.caminhos.clear();
        this.caminhos.addAll(Caminhos.calculaCaminhos(1, this.underlay));

        // We write the links on
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos     = new ObjectOutputStream(baos);
        oos.writeObject(clientCaminhos);
        oos.flush();
        oos.close();
        baos.flush();
        baos.close();
        byte[] buffer = baos.toByteArray();

        // We send the links
        DatagramPacket caminhosPacket = new DatagramPacket(buffer, buffer.length, this.clientAddress, this.clientPort);
        this.dSocket.send(caminhosPacket);
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Client: " + this.clientID + " is YO");
            this.underlay.turnONDevices(this.clientID);
            this.dSocket.setSoTimeout(4000);
            while(true){
                sendCaminhos();
                byte[] buffer = new byte[15000];
                DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, this.clientAddress, this.clientPort);
                this.dSocket.receive(dPacket);
            }
        } 
        catch (Exception e) {
            System.out.println("Client: " + this.clientID + " is closed YO");
            this.dSocket.close();
            this.underlay.turnOFFDevices(this.clientID);
        }
    }

}