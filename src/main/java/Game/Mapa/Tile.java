package Game.Mapa;

import org.joml.Vector2f;

public class Tile {

    private int ID;
    protected Vector2f positionInGrid; // col/row position
    protected Vector2f position;

    public Tile(int ID, Vector2f positionInGrid){

        this.ID = ID;
        this.positionInGrid = positionInGrid;
        this.position = new Vector2f(0.f);
    }

    public void printInfo() {

        System.out.println("Tile: ");
        System.out.println("ID: " + this.ID);
        System.out.println("Position: (" + this.positionInGrid.x + "," + this.positionInGrid.y + ")");
    }

    public Vector2f getPositionInGrid() {
        return positionInGrid;
    }

    public Vector2f getPosition() {
        return position;
    }

    public int getID() {
        return ID;
    }
}
