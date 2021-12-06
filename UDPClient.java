import Parsing.Links;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class UDPClient {
    private DatagramSocket datagramSocket;
    private byte[] buffer;
    private InetAddress inetAddress;
    private int port;
    private int id;

    // Constructor
    public UDPClient(int id) {
        try {
            this.datagramSocket = new DatagramSocket();
            this.buffer = new byte[256];
            this.inetAddress = InetAddress.getByName("localhost"); // CHANGE
            this.port = 5000;
            this.id = id;

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

    private DatagramPacket sendId(){
        DatagramPacket datagramPacket = null;
        try {
            this.buffer = Integer.toString(this.id).getBytes();
            datagramPacket = new DatagramPacket(this.buffer, this.buffer.length, this.inetAddress, this.port);
            this.datagramSocket.send(datagramPacket);
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

    private void receive() throws IOException, ClassNotFoundException {
        byte[] data2 = new byte[1024];
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
        this.datagramSocket.receive(packet2);
        ByteArrayInputStream bais = new ByteArrayInputStream(data2);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Links links = (Links) ois.readObject();
        System.out.println(links);
        ois.close();
    }


    public void run() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            //getInput(scanner);
            //DatagramPacket datagramPacket = receiveNsend();
            DatagramPacket datagramPacket = sendId();
            receive();
        }
    }
}
