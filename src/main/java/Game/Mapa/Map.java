package Game.Mapa;

import Utils.MapPath;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private final int LAYERS = 2; // how many layers of tiles the map will have.
    List<MapLayer> mapLayers = new ArrayList<>();

    // (mapFile     -> JsonFile containing information about position and Id of each tile in each layer)
    // (layerTextureMap -> Map<layer, texture)
    public Map(String mapFile, java.util.Map<Integer, Integer> layerTextureMap, Vector2f textureSize, float spriteGlobalScale){

        int firtTexture = layerTextureMap.get(MapLayer.MapLayerIndexValues.FIRST_LAYER);
        int secondTexture = layerTextureMap.get(MapLayer.MapLayerIndexValues.SECOND_LAYER);

        MapLayer firstLayer  = new MapLayer(MapLayer.MapLayerIndexValues.FIRST_LAYER , mapFile, firtTexture, textureSize, spriteGlobalScale);
        MapLayer secondLayer = new MapLayer(MapLayer.MapLayerIndexValues.SECOND_LAYER, mapFile, secondTexture, textureSize, spriteGlobalScale);

        mapLayers.add(firstLayer);
        mapLayers.add(secondLayer);
    }

    public void render(Matrix4f projection, Matrix4f view){

        mapLayers.forEach(mapLayer -> {
            mapLayer.render(projection, view);
        });
    }
}
