package data;

import java.io.Serializable;


public class Link implements Serializable{
    private int    id;
    private String ip4;
    private int    on;


    // --- CONSTRUCTORS ---
    public Link() {
        this.id  = 0;
        this.ip4 = null;
        this.on  = 0;
    }

    public Link(int id, String ip4) {
        this.id  = id;
        this.ip4 = ip4;
        this.on  = 0;
    }


    // --- GETTERS ---
    public int getID() {
        return this.id;
    }

    public String getIP4() {
        return this.ip4;
    }

    public Boolean isOn() {
        return this.on == 1;
    }


    // --- SETTERS ---
    public void setID(int id) {
        this.id = id;
    }

    public void setIP4(String ipv4) {
        this.ip4 = ipv4;
    }

    public void turnON() {
        this.on = 1;
    }

    public void turnOFF() {
        this.on = 0;
    }


    // --- OTHER ---
    public void print() {
        System.out.println("deviceID: " + this.id);
        System.out.println("ip4: " + this.ip4);
        System.out.println("on: " + this.on);
    }
}
