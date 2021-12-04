/*
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class UDPWorker implements Runnable{
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private byte[] buffer;
    

    public UDPWorker(DatagramSocket datagramSocket, DatagramPacket datagramPacket, byte[] buffer) {
        this.datagramSocket = datagramSocket;
        this.datagramPacket = datagramPacket;
        this.buffer = buffer;
    }

    public void send() {
        try {
            this.datagramPacket = new DatagramPacket(this.buffer, this.buffer.length, this.datagramPacket.getAddress(), this.datagramPacket.getPort());
            this.datagramSocket.send(datagramPacket);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("UDPServer send() NOT YO");
        }
    }
    
    
    @Override
    public void run(){ send(); }

}
*/
//Ganda cavalo que tens ai
//Pequeno ponei que nao tens
//Herois do mar nobre povo fdps