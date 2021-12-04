import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;


public class UDPClient {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer; 
    private int port;

    // Constructor
    public UDPClient() {
        try {
            this.datagramSocket = new DatagramSocket();
            this.inetAddress = InetAddress.getByName("localhost");
            this.buffer = new byte[256];
            this.port = 5000;

        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPClient constructor NOT YO");
        }
    }
    
    
    // Read input
    private void getInput(Scanner scanner){
        this.buffer = scanner.nextLine().getBytes();
    }

    // Receives Message
    private DatagramPacket receiveNsend(){
        DatagramPacket datagramPacket = null;
        try {
            datagramPacket = new DatagramPacket(this.buffer, this.buffer.length, this.inetAddress, this.port);
            this.datagramSocket.send(datagramPacket);
            this.datagramSocket.receive(datagramPacket); 
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPClient sendNReceive() NOT YO");
        }
        return datagramPacket;
    }

    // Print Output
    private void getOutput(DatagramPacket datagramPacket) {
        String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        System.out.println("The Server Says you said: " + message);
    }


    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            getInput(scanner);
            DatagramPacket datagramPacket = receiveNsend();
            getOutput(datagramPacket);
        }
    }
}