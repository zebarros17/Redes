import java.util.HashMap;
import java.util.ArrayList;


public class Links {
    private int deviceID;
    private HashMap<Integer, Link> links;


    // Constructors
    public Links() {
        this.deviceID = 0;
        this.links = new HashMap<Integer, Link>();
    }

    public Links(Links links) {
        this.deviceID = links.getDeviceID();
        this.links = links.getLinks();
    }

    public Links(int id, Link link) {
        this.deviceID = id;
        this.links = new HashMap<Integer, Link>();
        this.links.put(link.getDeviceID(), link);
    }


    public Links(int device, HashMap<Integer, Link> links) {
        this.deviceID = device;
        this.links = new HashMap<Integer, Link>(links);
    }

    public Links(int device) {
        this.deviceID = device;
        this.links = new HashMap<Integer, Link>();
    }


    // GETTERS
    public int getDeviceID() {
        return deviceID;
    }

    public HashMap<Integer, Link> getLinks() {
        return links;
    }

    public Link getLink(int id) {
        return this.links.get(id);
    }

    public ArrayList<Link> getLinksList() {
        return new ArrayList<Link>(links.values());
    }


    // SETTERS
    public void setDeviceID(int device) {
        this.deviceID = device;
    }

    public void setLinks(HashMap<Integer, Link> links) {
        this.links = links;
    }

    public void put(Links links) {
        this.links.putAll(links.getLinks());
    }

    public void put(Link link) {
        this.links.put(link.getDeviceID(), link);
    }


    // OTHER
    public void print() {
        System.out.println("deviceID: " + this.deviceID);
        for(Link link : this.links.values()) {
            link.print();
        }
        System.out.println();
    }
}
