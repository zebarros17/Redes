/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
  * Ott pode ser executado como cliente e como servidor.                            *
  * Com a Flag -s dado como argumento Ott é executado como servidor na porta 5000   *
  * Com a Flag -c dado como argumento Ott é executado como Cliente que contacta com *
  * o servidor na porta 5000                                                        *
  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class OTT {

    public static void main(String[] argvs) throws Exception {

        if(argvs[0].equals("-s")){              // caso flag seja -s executa como servidor
            System.out.println("Server");
            System.out.println("SERVER RUNNING");
            UDPServer server = new UDPServer(5000);
            server.executaServer();
        }
        else if(argvs[0].equals("-c")) {        // caso flag seja -c executa como cliente
            System.out.println("Cliente");
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName("localhost");
            UDPClient client = new UDPClient(datagramSocket, inetAddress);
            client.executaClient();
        }
        else System.out.println("Argumentos invalidos");
    }

    // --------------------------------------------- Server -----------------------------------------------
    private static class UDPServer {
        private DatagramSocket datagramSocket;
        private byte[] buffer;

        // Contructor
        public UDPServer(int port) throws Exception {
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
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("UDPServer receive() NOT YO");
            }
            return datagramPacket;
        }

        public void executaServer(){
            while (true) {
                DatagramPacket datagramPacket = receive();
                treatMessage(datagramPacket);
                new Thread(new UDPWorker(this.datagramSocket, datagramPacket, this.buffer)).start();
            }
        }
    }

    // ----------------------------------- UDPWorker -----------------------------------------------------
    private static class UDPWorker implements Runnable{
        private DatagramSocket datagramSocket;
        private DatagramPacket datagramPacket;
        private byte[] buffer;

        UDPWorker(DatagramSocket datagramSocket, DatagramPacket datagramPacket, byte[] buffer) {
            this.datagramSocket = datagramSocket;
            this.datagramPacket = datagramPacket;
            this.buffer = buffer;
        }

        @Override
        public void run(){  // envia mensagens ao client (função send())
            try {
                this.datagramPacket = new DatagramPacket(this.buffer, this.buffer.length, this.datagramPacket.getAddress(), this.datagramPacket.getPort());
                this.datagramSocket.send(datagramPacket);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("UDPServer send() NOT YO");
            }
        }
    }

    // ----------------------------------- Client -----------------------------------------------------
    private static class UDPClient {
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

        public void executaClient() {
            System.out.println("Send Packets to a Server");
            Scanner scanner = new Scanner(System.in);
            while(true) {
                getInput(scanner);
                DatagramPacket datagramPacket = receiveNsend();
                getOutput(datagramPacket);
            }
        }
    }
}

