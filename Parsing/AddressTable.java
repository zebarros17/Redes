import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;


public class AddressTable {
    private HashMap<Integer, Links> table;

    // --- CONSTRUCTOR ---
    public AddressTable() {
        this.table = new HashMap<Integer, Links>();
    }

    public AddressTable(HashMap<Integer, Links> table) {
        this.table = new HashMap<Integer, Links>(table);
    }

    public AddressTable(Links links) {
        this.table.put(links.getID(), links);
    }


    // --- GETTERS ---
    public HashMap<Integer, Links> getTable() {
        return this.table;
    }

    public ArrayList<Links> getAllLinks() {
        return new ArrayList<Links>(this.table.values());
    }

    public Links getLinks(int id) {
        return this.table.get(id);
    }

    public Set<Integer> getKeys() {
        return this.table.keySet();
    }


    // --- SETTERS ---
    public void setTable(HashMap<Integer, Links> links) {
        this.table = links;
    }

    public void addLinks(int id, Links links) {
        if (this.table.containsKey(id)) {
            this.table.get(id).addLinks(links);
        }
        else {
            this.table.put(id, links);
        }
    }


    // --- OTHER ---
    public void print() {
        System.out.println("TABLE");
        for(Links links : this.table.values()) {
            links.print();
        }
    }
}