package connection;

import data.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;


public class StreamSend implements Runnable{
    private Caminhos caminhos;
    private int      destinoID;
    
    private InetAddress proxAddress;
    private int         proxPort;

    private DatagramSocket dSocket;
    
    
    public StreamSend(Caminhos caminhos, int clientID) throws SocketException {
        this.caminhos  = caminhos;
        this.destinoID = clientID;

        this.dSocket = new DatagramSocket();
    }

    private void sendNodoDestino() throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(destinoID);
        byte[] buffer = bb.array();

        Caminho caminho = this.caminhos.getCaminho(destinoID);
        this.proxPort   = 5000 + caminho.getProxSalto().getID();
        this.proxAddress = InetAddress.getByName(caminho.getProxSalto().getIP4());

        // We send the links
        DatagramPacket caminhosPacket = new DatagramPacket(buffer, buffer.length, proxAddress, proxPort);
        this.dSocket.send(caminhosPacket);
    }


    @Override
    public void run() {
        try {
            sendNodoDestino();
        }
        catch (Exception e) {
            System.out.println("SendNodoDestino NOT YO");
            System.out.println("Can't reach " + destinoID);
            this.dSocket.close();
        }
    }
    
}
