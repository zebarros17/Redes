import java.io.*;
import java.net.*;

public class UDPServer {


    public class UDPServer {

        private DatagramSocket datagramSocket;
        private byte[] buffer = new byte[256];

        public UDPServer(DatagramSocket datagramSocket) {
            this.datagramSocket = datagramSocket;
        }

        public void receiveThenSend() {
            while(true) {
                try {
                    // Receives Message
                    DatagramPacket datagramPacket = new DatagramPacket(this.buffer, this.buffer.length);
                    this.datagramSocket.receive(datagramPacket);
                    InetAddress inetAddress =  datagramPacket.getAddress();
                    int port = datagramPacket.getPort();

                    // Treat de data Received
                    String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    System.out.println("Message from Client: " + message);

                    // Send Message
                    datagramPacket = new DatagramPacket(this.buffer, this.buffer.length, inetAddress, port);
                    this.datagramSocket.send(datagramPacket);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public static void main(String[] args) throws SocketException {
            DatagramSocket datagramSocket = new DatagramSocket(1234);
            UDPServer server = new UDPServer(datagramSocket);
            server.receiveThenSend();
        }
    }
}
