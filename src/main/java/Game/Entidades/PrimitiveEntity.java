package Game.Entidades;

import Buffers.EBO;
import Buffers.VBO;
import Utils.Primitives;
import Game.Shader;
import Utils.Vertex;
import Game.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

abstract class PrimitiveEntity {

    protected Vector2f position;
    protected Vector2f scale;
    protected Matrix4f model;
    protected int texture;

    protected Shader shader;

    protected int VAO;
    protected Buffers.VBO VBO;
    protected Buffers.EBO EBO;

    public PrimitiveEntity(Shader shader){

        this.shader = shader;

        position = new Vector2f(0.f, 0.f);
        scale    = new Vector2f(1.f, 1.f);
        model = new Matrix4f().identity();

        createBuffers();
    }

    public void render() {

        GL30.glBindVertexArray(VAO);
        VBO.bind();
        EBO.bind();

        GL11.glDrawElements(GL11.GL_TRIANGLES, Primitives.squareIndices.length, GL11.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
    }

    private void createBuffers(){

        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer( Primitives.squareVertices.length * 4);

        for (Vertex vertex : Primitives.squareVertices) {
            verticesBuffer.put(vertex.position.x);
            verticesBuffer.put(vertex.position.y);
            verticesBuffer.put(vertex.textureCoord.x);
            verticesBuffer.put(vertex.textureCoord.y);
        }

        verticesBuffer.flip();

        VBO = new VBO(verticesBuffer, GL30.GL_STATIC_DRAW);
        EBO = new EBO(Primitives.squareIndices, GL30.GL_STATIC_DRAW);

        GL30.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL30.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES); // Color

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        verticesBuffer.clear();
    }

    protected void update(Window window, float frameCount, Matrix4f view, Matrix4f projection){}
}
