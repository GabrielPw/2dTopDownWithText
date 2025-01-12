package Utils;

import Game.Mapa.Tile;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.joml.Vector2f;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapLoader {

    public static Map<Vector2f, Tile> loadMap(String filePath, int layerIndex) {
        Map<Vector2f, Tile> tiles = new HashMap<>();

        try (FileReader reader = new FileReader(filePath)) {
            // Parse JSON
            Gson gson = new Gson();
            JsonObject mapData = gson.fromJson(reader, JsonObject.class);

            // Obtenha a camada de dados
            JsonArray layers = mapData.getAsJsonArray("layers");
            JsonObject layer = layers.get(layerIndex).getAsJsonObject();
            JsonArray tileData = layer.getAsJsonArray("data");

            System.out.println(layers.get(0).toString());

            int width = mapData.get("width").getAsInt();
            int height = mapData.get("height").getAsInt();

            System.out.println("\nMap Width: " + width);
            System.out.println("Map Height: " + height);

            System.out.println("----\n\n");

            int totalTiles = width * height;

            for (int i = 0; i < totalTiles; i++) {

                int row = i / width;
                int col = i % width;

                Vector2f position = new Vector2f(col, row);
                Tile tile = new Tile(tileData.get(i).getAsInt(), position);
                tiles.put(position, tile);
            }

            tiles.get(new Vector2f(1, 0)).printInfo();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tiles;
    }
    
}