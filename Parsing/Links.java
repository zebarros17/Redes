import java.util.ArrayList;


public class Links {
    private int id;
    private ArrayList<Link> links;

    // --- CONSTRUCTORS ---
    public Links() {
        this.id = 0;
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

    public Link getLink(int id) {
        for (Link link : this.links) {
            if (id == link.getID()) return link; 
        }
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


    // --- OTHER ---
    public void print() {
        System.out.println("id: " + this.id);
        for(Link link : this.links) {
            link.print();
        }
        System.out.println();
    }
    
}
