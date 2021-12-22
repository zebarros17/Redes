package connection;

import data.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;


public class UDPServer  {
    // Variáveis unicas
    private int          id;        
    private Underlay     underlay;  
    private Caminhos     caminhos;

    // RTP variables
    private InetAddress    iAddress;   
    private int            port;   
    private DatagramSocket dSocket;


    // Contrutor
    public UDPServer(String file, int id) throws Exception {
        this.id       = id;
        this.underlay = new Underlay(file);
        this.caminhos = new Caminhos();

        this.iAddress = InetAddress.getByName("10.0.0.10"); 
        this.port     = 4000 + this.id;

        this.dSocket = new DatagramSocket(this.port, this.iAddress);
    }
    

    // Run the Server
    public void run() throws Exception {
        // We turn on the server links
        this.underlay.turnONDevices(this.id);
        this.caminhos = Caminhos.calculaCaminhos(this.id, this.underlay);

        while(true) {
            // We receive a flag that tells us what we should do
            byte[] buffer           = new byte[8];
            DatagramPacket dPacket  = new DatagramPacket(buffer, buffer.length);
            this.dSocket.receive(dPacket);
            
            // We treat the flag
            String receivedData = new String(buffer, StandardCharsets.UTF_8);
            String[] arrOfStr   = receivedData.split(":", 2);
            String flag         = arrOfStr[0].trim();

            // A client wants to connect to the server
            if(flag.equals("CON")) {
                int clientID = Integer.parseInt(arrOfStr[1].trim());
                new Thread(new ClientON_S(this.underlay, this.caminhos, clientID, dPacket)).start();
            }
            // Whatever we want muahahaha
            if(flag.equals("SON")) {
                int destinoID = Integer.parseInt(arrOfStr[1].trim());
                new Thread(new StreamSend_S(this.caminhos, destinoID)).start();
            }
        }
    }
}