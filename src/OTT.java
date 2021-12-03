import java.net.*;
import java.util.Scanner;

public class OTT {

    public static void main(String argvs[]) throws SocketException, UnknownHostException {
        System.out.println("S: INIT SERVER");
        System.out.println("C: INIT CLIENT");

        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();
        //scanner.close();

        if(action.equals("S")) {
            DatagramSocket datagramSocket = new DatagramSocket(1234);
            UDPServer server = new UDPServer(datagramSocket);
            server.receiveThenSend();
        }
        if(action.equals("C")) {
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName("localhost");
            UDPClient client = new UDPClient(datagramSocket, inetAddress);
            client.sendThenReceive();
        }
        else {
            System.out.println("INVALID. TERMINATING");
        }
        scanner.close();
    }
}