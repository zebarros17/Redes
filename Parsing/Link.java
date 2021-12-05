public class Link {
    private int id;
    private String ip4;


    // --- CONSTRUCTORS ---
    public Link() {
        this.id = 0;
        this.ip4 = null;
    }

    public Link(int id, String ip4) {
        this.id = id;
        this.ip4 = ip4;
    }


    // --- GETTERS ---
    public int getID() {
        return this.id;
    }

    public String getIP4() {
        return this.ip4;
    }


    // --- SETTERS ---
    public void setID(int id) {
        this.id = id;
    }

    public void setIP4(String ipv4) {
        this.ip4 = ipv4;
    }


    // --- OTHER ---
    public void print() {
        System.out.println("deviceID: " + this.id);
        System.out.println("ip4: " + this.ip4);
    }
}