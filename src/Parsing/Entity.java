package Parsing;
public class Entity {
    private Integer id;
    private String name; 
    private Integer positionX;
    private Integer positionY; 


    // Constructors
    public Entity() {
        this.id = 0;
        this.name = "";
        this.positionX = 0;
        this.positionY = 0;
    }

    public Entity(int id, String name, int positionX, int positionY) {
        this.id = id;
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
    }


    // GETTERS
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }


    // SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
    
}
