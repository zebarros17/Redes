package data;

import java.io.*;


public class VideoStream {
    FileInputStream fis; // video file
    int frame_nb;        // current frame nb

    // Constructor
    public VideoStream(String filename) throws Exception {
        fis      = new FileInputStream(filename);
        frame_nb = 0;
    }


    // GETTER
    public int getnextframe(byte[] frame) throws Exception {
        int length = 0;
        String length_string;
        byte[] frame_length = new byte[5];
        
        //read current frame length
        fis.read(frame_length,0,5);
        
        //transform frame_length to integer
        length_string = new String(frame_length);
        length = Integer.parseInt(length_string);
        
        return(fis.read(frame,0,length));
    }
}
