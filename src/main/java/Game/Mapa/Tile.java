package Game.Mapa;

import org.joml.Vector2f;

public class Tile {

    private int ID;
    private Vector2f position;
    private boolean isCollidable;

    public Tile(int ID, Vector2f position){

        this.ID = ID;
        this.position = position;
        this.isCollidable = false;
    }

    public void printInfo() {

        System.out.println("Tile: ");
        System.out.println("ID: " + this.ID);
        System.out.println("Position: (" + this.position.x + "," + this.position.y + ")");
    }

    public Vector2f getPosition() {
        return position;
    }

    public int getID() {
        return ID;
    }
}
