/*
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class UDPServer {
    private DatagramSocket datagramSocket;
    private byte[] buffer; 

    // Contructor
    public UDPServer(int port) throws Exception{
        this.datagramSocket = new DatagramSocket(port);
        this.buffer = new byte[256];  
    }


    // Treats message received
    private void treatMessage(DatagramPacket datagramPacket) {
        String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        System.out.println("Message from Client: " + message);
    }

    // Receives Message
    private DatagramPacket receive() {
        DatagramPacket datagramPacket = null;
        try {
            datagramPacket = new DatagramPacket(this.buffer, this.buffer.length);   
            this.datagramSocket.receive(datagramPacket);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPServer receive() NOT YO");
        }
        return datagramPacket;
    }

    
    public void run(){
        while(true) {
            DatagramPacket datagramPacket = receive();
            treatMessage(datagramPacket);
            new Thread( new UDPWorker(this.datagramSocket, datagramPacket, this.buffer) ).start();
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println("SERVER RUNNING");
        UDPServer server = new UDPServer(5000);
        server.run();
    }
}
*/
