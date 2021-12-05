import java.util.HashMap;
import java.util.ArrayList;

public class Devices {
    private HashMap<Integer, Device> devices;


    // Constructor
    public Devices() {
        this.devices = new HashMap<Integer, Device>();
    }

    public Devices(HashMap<Integer, Device> devices) {
        this.devices = new HashMap<Integer, Device>(devices);
    }

    public Devices(Devices devices) {
        this.devices = devices.getDevices();
    }

    public Devices(Device device) {
        this.devices = new HashMap<Integer, Device>();
        this.devices.put(device.getId(), new Device(device));
    }


    // GETTERS
    public HashMap<Integer, Device> getDevices() {
        return devices;
    }

    public ArrayList<Device> getDevicesList() {
        return new ArrayList<Device>(devices.values());
    }

    public Device getDevice(int id) {
        return this.devices.get(id);
    }


    // SETTERS
    public void setDevices(HashMap<Integer, Device> devices) {
        this.devices = devices;
    }

    public void putDevices(HashMap<Integer, Device> devices) {
        this.devices.putAll(devices);
    }

    public void putDevice(Device device) {
        this.devices.put(device.getId(), device);
    }


    // OTHER
    public Boolean contains(int id) {
        return this.devices.keySet().contains(id);
    }

    public Boolean contains(int id1, int id2) {
        return this.devices.keySet().contains(id1) && this.devices.keySet().contains(id2);
    }


    
    
    public void print() {
        System.out.println("DEVICES");
        for(Device device : this.devices.values()) {
            device.print();
        }
    }
}