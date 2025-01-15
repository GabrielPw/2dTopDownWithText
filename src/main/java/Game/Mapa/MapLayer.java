package Game.Mapa;

import Buffers.EBO;
import Buffers.VBO;
import Game.GUI.GlyphData;
import Game.Shader;
import Utils.MapLoader;
import Utils.MapPath;
import Utils.Primitives;
import Utils.Vertex;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

public class MapLayer {

    private final int LAYER_INDEX;
    private final int MAX_TILES_QUANTITY = 1050; // Max area of 35x30 tiles.

    private Vector2f spriteSize = new Vector2f(16.f, 16.f);
    private Map<Vector2f, Tile> tileList;
    private int atlasTexture;
    private Vector2f textureSize;

    private Matrix4f model;
    private Vector2f position;
    private Vector2f scale;
    private int VAO;
    private Buffers.VBO VBO;
    private Buffers.EBO EBO;
    private Shader shader;

    public class MapLayerIndexValues {

        public static final int FIRST_LAYER             = 0; // basemap tiles, like ground, grass, etc.
        public static final int SECOND_LAYER            = 1; // decorative tiles above first layer
        public static final int THIRD_LAYER_INTERACTIVE = 2; // layer containing interactive/collidable tiles.
    }

    public MapLayer(int VAO_, Shader shader_, int layerIndex, String mapPathFile, int atlasTexture, Vector2f textureSize, float spriteGlobalScale){

        this.LAYER_INDEX = layerIndex;
        this.tileList = MapLoader.loadMap(mapPathFile, LAYER_INDEX);
        this.atlasTexture = atlasTexture;
        this.textureSize = textureSize;

        this.shader     = shader_;
        this.position   = new Vector2f(0.f, 800.f);

        spriteGlobalScale *= 1.15f;
        this.scale      = new Vector2f(spriteGlobalScale, spriteGlobalScale);
        this.model      = new Matrix4f().identity();

        // buffer stuff
        VAO = VAO_;

        GL30.glBindVertexArray(VAO);
        this.VBO = new VBO((long) (Primitives.squareVertices.length * 4 * MAX_TILES_QUANTITY) * Float.BYTES, GL30.GL_DYNAMIC_DRAW);
        this.EBO = new EBO((long) (Primitives.squareIndices.length * MAX_TILES_QUANTITY) * Integer.BYTES, GL30.GL_DYNAMIC_DRAW);

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        setupBuffers();
    }

    public void setupBuffers(){
        this.position = position;

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer( Primitives.squareVertices.length * 4 * MAX_TILES_QUANTITY);
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(Primitives.squareIndices.length * MAX_TILES_QUANTITY);

        int letterIndex = 0;
        for (int row = 0; row < 30; row++) {
            for (int col = 0; col < 35; col++) {

                Tile tile = tileList.get(new Vector2f(col, row));
                int tileID = tile.getID() - 1;
                //if (tileID < 0) continue; // ignore empty spaces in map

                int tilesPerRow = (int) (textureSize.x / spriteSize.x);  // 25
                int tileRow = tileID / tilesPerRow;
                int tileCol = tileID % tilesPerRow;

                float spriteWidthUV = spriteSize.x / textureSize.x;  // 0.04f
                float spriteHeightUV = spriteSize.y / textureSize.y; // 0.04f

                float epsilon = 0.001f; // Ajuste fino para evitar bleeding

                float u1 = tileCol * spriteWidthUV + epsilon;
                float u2 = u1 + spriteWidthUV - 2 * epsilon;
                float v1 = tileRow * spriteHeightUV + epsilon;
                float v2 = v1 + spriteHeightUV - 2 * epsilon;

                for (Vertex squareVertex : Primitives.squareVertices) {

                    float mappedU = squareVertex.textureCoord.x == 0.0f ? u1 : u2;
                    float mappedV = squareVertex.textureCoord.y == 0.0f ? v1 : v2;

                    verticesBuffer.put(squareVertex.position.x + col);
                    verticesBuffer.put(squareVertex.position.y - row);
                    verticesBuffer.put(mappedU);
                    verticesBuffer.put(mappedV);
                }

                int baseIndex = letterIndex * 4; // O índice base de onde começa o quadrado
                for (int i = 0; i < Primitives.squareIndices.length; i++) {
                    indicesBuffer.put(Primitives.squareIndices[i] + baseIndex);
                }

                letterIndex++;
            }
        }

        verticesBuffer.flip();
        indicesBuffer.flip();

        VBO.updateData(verticesBuffer);
        EBO.updateData(indicesBuffer);
    }

    public void render(Matrix4f projection, Matrix4f view){
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.atlasTexture);

        model.identity();
        model.translate(new Vector3f(this.position, 0.f));
        model.scale(this.scale.x, this.scale.y, 1.f);

        shader.use();
        shader.addUniformMatrix4fv("projection", projection);
        shader.addUniformMatrix4fv("view", view);
        shader.addUniformMatrix4fv("model", this.model);
        shader.addUniform1f("atlasTexture", this.atlasTexture);

        GL30.glBindVertexArray(VAO);
        this.VBO.bind();
        this.EBO.bind();

        GL30.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL30.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);

        GL11.glDrawElements(GL11.GL_TRIANGLES, MAX_TILES_QUANTITY * Primitives.squareIndices.length, GL11.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
    }

    public void moveTile(Tile tile) {

        float tileColGrid = tile.getPositionInGrid().x;
        float tileRowGrid = tile.getPositionInGrid().y;
        int tileIndex = (int) (tile.positionInGrid.y * 35 + tile.getPositionInGrid().x);  // Supondo MAP_WIDTH como número de colunas
        int vertexStartOffset = tileIndex * Primitives.squareVertices.length * Float.BYTES * 4;  // Offset do tile no buffer

        // Criar novo buffer para armazenar os dados atualizados do tile
        FloatBuffer updatedVertices = BufferUtils.createFloatBuffer(Primitives.squareVertices.length * 4);

        int tileID = tile.getID() - 1;

        int tilesPerRow = (int) (textureSize.x / spriteSize.x);  // 25
        int tileRowAtlas = tileID / tilesPerRow;
        int tileColAtlas = tileID % tilesPerRow;

        float spriteWidthUV = spriteSize.x / textureSize.x;  // 0.04f
        float spriteHeightUV = spriteSize.y / textureSize.y; // 0.04f

        float epsilon = 0.001f; // Ajuste fino para evitar bleeding

        float u1 = tileColAtlas * spriteWidthUV + epsilon;
        float u2 = u1 + spriteWidthUV - 2 * epsilon;
        float v1 = tileRowAtlas * spriteHeightUV + epsilon;
        float v2 = v1 + spriteHeightUV - 2 * epsilon;

        for (Vertex squareVertex : Primitives.squareVertices) {

            float mappedU = squareVertex.textureCoord.x == 0.0f ? u1 : u2;
            float mappedV = squareVertex.textureCoord.y == 0.0f ? v1 : v2;

            updatedVertices.put(squareVertex.position.x + tileColGrid + tile.getPosition().x);
            updatedVertices.put(squareVertex.position.y - tileRowGrid + tile.getPosition().y);
            updatedVertices.put(mappedU);
            updatedVertices.put(mappedV);
        }

        updatedVertices.flip();

        // Atualizar o VBO com os novos vértices
        VBO.subData(vertexStartOffset, updatedVertices);
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Map<Vector2f, Tile> getTileList() {
        return tileList;
    }
}
