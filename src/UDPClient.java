import java.net.*;
import java.util.Scanner;

public class UDPClient {


    public class UDPClient {
        private DatagramSocket datagramSocket;
        private InetAddress inetAddress;
        private byte[] buffer;
        private int port = 1234;

        public UDPClient(DatagramSocket datagramSocket, InetAddress inetAddress) {
            this.datagramSocket = datagramSocket;
            this.inetAddress = inetAddress;
        }

        public void sendThenReceive() {
            Scanner scanner = new Scanner(System.in);

            while(true) {
                try {
                    // Treat Message
                    this.buffer = scanner.nextLine().getBytes();

                    // Receives and Sends
                    DatagramPacket datagramPacket = new DatagramPacket(this.buffer, this.buffer.length, inetAddress, port);
                    this.datagramSocket.send(datagramPacket);
                    this.datagramSocket.receive(datagramPacket);

                    // Prints Received Message
                    String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    System.out.println("The Server Says you said: " + message);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            scanner.close();
        }

        public static void main(String[] args) throws SocketException, UnknownHostException {
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName("localhost");
            UDPClient client = new UDPClient(datagramSocket, inetAddress);
            System.out.println("Send Packets to a Server");
            client.sendThenReceive();
        }
    }
}
