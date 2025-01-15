package Game.Entidades;

import Game.Mapa.Tile;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.joml.Vector2f;

import java.io.FileReader;
import java.io.IOException;

public class CollisionBox {

    public float x, y, width, height;

    public CollisionBox(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(CollisionBox other) {
        return (x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y);
    }

    public static void loadCollisionBoxesFromFile(String filePath){

        try (FileReader reader = new FileReader(filePath)) {
            // Parse JSON
            Gson gson = new Gson();
            JsonObject mapData = gson.fromJson(reader, JsonObject.class);

            // Obtenha a camada de dados
            JsonArray layers = mapData.getAsJsonArray("layers");

            System.out.println(" ------ CollisionBox Class LOG------");
            System.out.println(layers);

            System.out.println(" ------ END CollisionBox Class LOG ------");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
