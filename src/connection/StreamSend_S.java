package connection;

import data.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;


public class StreamSend_S extends JFrame implements Runnable, ActionListener {
    // Variáveis unicas
    private Caminhos caminhos;
    private int      destinoID;
    
    // RTP variables
    private InetAddress    proxAddress;
    private int            proxNodoPort;
    private int            proxRTPPort;
    private DatagramSocket dSocket;

    static String VideoFileName = "movie.Mjpeg"; //video file to request to the server

    //Video constants:
    int imagenb = 0;   //image nb of the image currently transmitted
    VideoStream video; //VideoStream object used to access video frames
    static int MJPEG_TYPE   = 26;  //RTP payload type for MJPEG video
    static int FRAME_PERIOD = 100; //Frame period of the video to stream, in ms
    static int VIDEO_LENGTH = 500; //length of the video in frames

    Timer sTimer; //timer used to send the images at the video frame rate
    byte[] sBuf; //buffer used to store the images to send to the client 

    //GUI:
    JLabel label;

    
    public StreamSend_S(Caminhos caminhos, int clientID) throws SocketException {
        this.caminhos  = caminhos;
        this.destinoID = clientID;
        this.dSocket   = new DatagramSocket();
    }

    private void sendNodoDestino() throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.destinoID);
        byte[] buffer = bb.array();

        Caminho caminho   = this.caminhos.getCaminho(this.destinoID);
        this.proxNodoPort = 5000 + caminho.getProxSalto().getID();
        this.proxRTPPort  = 6000 + caminho.getProxSalto().getID();
        this.proxAddress  = InetAddress.getByName(caminho.getProxSalto().getIP4());

        // We send the links
        DatagramPacket caminhosPacket = new DatagramPacket(buffer, buffer.length, proxAddress, proxNodoPort);
        this.dSocket.send(caminhosPacket);
    }

    public void sendRTP() throws Exception {
        // init para a parte do servidor
        sTimer = new Timer(FRAME_PERIOD, this); //init Timer para servidor
        sTimer.setInitialDelay(0);
        sTimer.setCoalesce(true);
        sBuf = new byte[15000];  

        System.out.println("Servidor: socket " + this.proxAddress);
	    video = new VideoStream(VideoFileName); //init the VideoStream object:
        System.out.println("Servidor: vai enviar video da file " + VideoFileName);
        sTimer.start();
    }


    //------------------------
    //Handler for timer
    //------------------------
    public void actionPerformed(ActionEvent e) {
        // if the current image nb is less than the length of the video
        if (imagenb < VIDEO_LENGTH) {
            //update current imagenb
            imagenb++;
        
            try {
                //get next frame to send from the video, as well as its size
                int image_length = video.getnextframe(sBuf);

                //Builds an RTPpacket object containing the frame
                RTPpacket rtp_packet = new RTPpacket(MJPEG_TYPE, imagenb, imagenb*FRAME_PERIOD, sBuf, image_length);
            
                //get to total length of the full rtp packet to send
                int packet_length = rtp_packet.getlength();

                //retrieve the packet bitstream and store it in an array of bytes
                byte[] packet_bits = new byte[packet_length];
                rtp_packet.getpacket(packet_bits);

                //send the packet as a DatagramPacket over the UDP socket 
                DatagramPacket senddp = new DatagramPacket(packet_bits, packet_length, this.proxAddress, this.proxRTPPort);
                this.dSocket.send(senddp);
            }
            catch(Exception ex) {
                System.out.println("Exception caught: " + ex);
                System.exit(0);
            }
        }
        else {
            //if we have reached the end of the video file, stop the timer
            sTimer.stop();
        }
    }

    @Override
    public void run() {
        try {
            sendNodoDestino();
            sendRTP();
            System.out.println(this.proxRTPPort);
        }
        catch (Exception e) {
            System.out.println("Can't reach " + this.destinoID);
            this.dSocket.close();
        }
    }
    
}
