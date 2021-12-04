import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class UDPServer {
    private DatagramSocket datagramSocket;
    private byte[] buffer; 

    // Contructor
    public UDPServer(int port) throws Exception{
        this.datagramSocket = new DatagramSocket(port); 
    }


    // Treats message received
    private void treatMessage(DatagramPacket datagramPacket) {
        String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        System.out.println("Message from Client: " + message);
    }

    // Receives Message
    private DatagramPacket receive() {
        // Inicializa o packet
        DatagramPacket datagramPacket = null;
        try {
            // O packet é preenchido com informação
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
            this.buffer = new byte[256];
            DatagramPacket datagramPacket = receive();
            treatMessage(datagramPacket);
            new Thread( new UDPWorker(this.datagramSocket, datagramPacket, this.buffer) ).start();
        }
    }
}
