import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;


public class UDPClient {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer; 
    private int port = 5000;

    // Constructor
    public UDPClient(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }
    
    
    // Read input
    private void getInput(Scanner scanner){
        this.buffer = scanner.nextLine().getBytes();
    }

    // Receives Message
    private DatagramPacket receiveNsend(){
        DatagramPacket datagramPacket = null;
        try {
            datagramPacket = new DatagramPacket(this.buffer, this.buffer.length, inetAddress, port);
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

    public static void main(String[] args) throws Exception {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        UDPClient client = new UDPClient(datagramSocket, inetAddress);
        System.out.println("Send Packets to a Server");
        client.run();
    }
}