package Buffers;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class VBO {

    private final int ID;
    public VBO(FloatBuffer verticesBuffer, int usage) {

        this.ID = GL30.glGenBuffers();
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.ID);
        GL30.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, usage);
    }

    // construtor para caso inicie um VBO vazio.
    public VBO(long bufferSize, int usage) {

        this.ID = GL30.glGenBuffers();
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.ID);
        GL30.glBufferData(GL15.GL_ARRAY_BUFFER, bufferSize, usage);
    }

    public VBO(int usage) {
        this.ID = GL30.glGenBuffers();
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.ID);
        // Inicializa o buffer vazio, será atualizado dinamicamente
        GL30.glBufferData(GL15.GL_ARRAY_BUFFER, 0, usage);
    }

    public void bind(){

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, this.ID);
    }


    // Atualiza todo o conteúdo do VBO
    public void updateData(FloatBuffer verticesBuffer) {
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.ID);
        GL30.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, verticesBuffer);
    }

    // Atualiza parte do conteúdo do VBO
    public void subData(int offset, FloatBuffer verticesBuffer) {
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.ID);
        GL30.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, verticesBuffer);
    }

    public int getID() {
        return ID;
    }
}
