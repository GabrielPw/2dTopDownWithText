package Game;

import Buffers.VBO;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class Main {

    public static void main(String[] args) {

        Game game = new Game("Pokemon.", 800, 800);
        game.run();
    }
}
