package Parsing;

import java.util.HashMap;
import java.util.ArrayList;

public class Devices {
    private HashMap<Integer, Device> devices;

    // CONSTRUCTORS
    public Devices() {
        this.devices = new HashMap<Integer, Device>();
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

    public void addDevices(HashMap<Integer, Device> devices) {
        this.devices.putAll(devices);
    }

    public void addDevice(Device device) {
        this.devices.put(device.getId(), device);
    }


    // OTHER
    public Boolean contains(int id) {
        return this.devices.keySet().contains(id);
    }

    public void print() {
        System.out.println("DEVICES");
        for(Device device : this.devices.values()) {
            device.print();
        }
    }
}