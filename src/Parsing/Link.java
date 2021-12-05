public class Link {
    private int deviceID;
    private String ip4;


    // Constructors
    public Link() {
        this.deviceID = 0;
        this.ip4 = null;
    }

    public Link(int deviceID, String ipv4) {
        this.deviceID = deviceID;
        this.ip4 = ipv4;
    }


    // GETTERS
    public int getDeviceID() {
        return this.deviceID;
    }

    public String getIP4() {
        return this.ip4;
    }


    // SETTERS
    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public void setIpv4(String ipv4) {
        this.ip4 = ipv4;
    }


    // OTHER
    public void print() {
        System.out.println("deviceID: " + this.deviceID);
        System.out.println("ip4: " + this.ip4);
    }
}
