package Parsing;
public class Link {
    private Integer node1;
    private Integer node2;
    private String ip;


    // Constructor
    public Link() {
        this.node1 = 0;
        this.node2 = 0;
        this.ip = "";
    }

    public Link(int node1, int node2, String ip) {
        this.node1 = node1;
        this.node2 = node2;
        this.ip = ip;
    }


    // GETTERS
    public int getNode1() {
        return node1;
    }

    public int getNode2() {
        return node2;
    }

    public String getIp() {
        return ip;
    }


    // SETTERS
    public void setNode1(int node1) {
        this.node1 = node1;
    }

    public void setNode2(int node2) {
        this.node2 = node2;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
