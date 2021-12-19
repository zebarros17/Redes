package data;

import java.io.Serializable;
import java.util.ArrayList;


public class Links implements Serializable{
    private int id;
    private ArrayList<Link> links;

    
    // --- CONSTRUCTORS ---
    public Links() {
        this.id = 0;
        this.links = new ArrayList<Link>();
    }

    public Links(int id) {
        this.id = id;
        this.links = new ArrayList<Link>();
    }

    public Links(int id, Link link) {
        this.id = id;
        this.links = new ArrayList<Link>();
        this.links.add(link);
    }


    // --- GETTERS ---
    public int getID() {
        return this.id;
    }

    public ArrayList<Link> getLinks() {
        return this.links;
    }

    public ArrayList<Link> getLinksON() {
        ArrayList<Link> linksON = new ArrayList<Link>();
        for (Link link : this.links) if (link.isOn()) linksON.add(link);
        return linksON;
    }

    public Link getLink(int id) {
        for (Link link : this.links) if (id == link.getID()) return link; 
        return null;
    }


    // --- SETTERS ---
    public void setID(int id) {
        this.id = id;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public void addLinks(Links links) {
        this.links.addAll(links.getLinks());
    }

    public void addLink(Link link) {
        this.links.add(link);
    }

    public void turnON(int id) {
        for (Link link : this.links) if (link.getID() == id) link.turnON();
    }

    public void turnOFF(int id) {
        for (Link link : this.links) if (link.getID() == id) link.turnOFF();
    }


    // --- OTHER ---
    public void print() {
        System.out.println("id: " + this.id);
        for (Link link : this.links) link.print();
        System.out.println();
        System.out.println("--------------------------");
        System.out.println();
    }
    
}