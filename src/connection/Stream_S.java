package connection;

import data.*;

import java.net.DatagramPacket;
import java.net.InetAddress;


public class Stream_S implements Runnable{
    private Caminhos caminhos;
    private int      clientID;
    
    private InetAddress clientAddress;
    private int         clientPort;
    
    
    public Stream_S(Caminhos caminhos, int clientID, DatagramPacket dPacket) {
        this.caminhos = caminhos;
        this.clientID = clientID;
        
        this.clientAddress = dPacket.getAddress();
        this.clientPort = dPacket.getPort();
    }
    

    @Override
    public void run() {

    }
    
}
