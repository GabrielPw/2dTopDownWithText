package Game.Mapa;

import Game.Shader;
import Utils.MapLoader;
import Utils.MapPath;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private Shader layerShader;
    private int mapLayerVAO, interativeTilesVAO;
    List<MapLayer> mapLayers = new ArrayList<>();

    // (mapFile     -> JsonFile containing information about position and Id of each tile in each layer)
    // (layerTextureMap -> Map<layer, texture)

    public Map(String mapFile, java.util.Map<Integer, Integer> layerTextureMap, Vector2f textureSize, float spriteGlobalScale){

        this.layerShader = new Shader("map/map.vert", "map/map.frag");

        mapLayerVAO = GL30.glGenVertexArrays();

        int firtTexture = layerTextureMap.get(MapLayer.MapLayerIndexValues.FIRST_LAYER);
        int secondTexture = layerTextureMap.get(MapLayer.MapLayerIndexValues.SECOND_LAYER);

        MapLayer firstLayer  = new MapLayer(mapLayerVAO, layerShader, MapLayer.MapLayerIndexValues.FIRST_LAYER, mapFile, firtTexture, textureSize, spriteGlobalScale);
        MapLayer secondLayer = new MapLayer(mapLayerVAO, layerShader, MapLayer.MapLayerIndexValues.SECOND_LAYER, mapFile, secondTexture, textureSize, spriteGlobalScale);

        mapLayers.add(firstLayer);
        mapLayers.add(secondLayer);

    }

    public void render(Matrix4f projection, Matrix4f view, float deltaTime){

        mapLayers.forEach(mapLayer -> {
            mapLayer.render(projection, view);
        });

        Vector2f tilePosInGrid = new Vector2f(9, 4);
        Tile tile = mapLayers.get(1).getTileList().get(tilePosInGrid);

        mapLayers.get(1).moveTile(tile);

        tile.position.x += 0.02f;
    }
}
