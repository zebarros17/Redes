package Parsing;


public class Device {
    private Integer id;
    private String name; 

    // --- CONSTRUCTORS ---
    public Device() {
        this.id = 0;
        this.name = "";
    }

    public Device(Device device) {
        this.id = device.getId();
        this.name = device.getName();
    }

    public Device(int id, String name) {
        this.id = id;
        this.name = name;
    }


    // --- GETTERS ---
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }


    // --- SETTERS ---
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    // --- OTHER ---
    public void print() {
        System.out.println("id: " + this.id);
        System.out.println("name: " + this.name);
    }
    
}