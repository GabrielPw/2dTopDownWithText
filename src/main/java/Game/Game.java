package Game;

import Game.Entidades.EntityManager;
import Game.GUI.GUIManager;
import Game.GUI.TextRenderer;
import Utils.FontsPath;
import Utils.TextureLoader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class Game {

    GUIManager guiManager;
    int fontTexture;
    private Shader fontShader;
    private TextRenderer textRenderer;

    Matrix4f cameraView;
    EntityManager entityManager;
    Window window;
    double previousTime;
    double frameTimeAccumulator;
    int frameCount;

    public Game(String windowTile, int winWidth, int winHeight){
        Matrix4f projection = new Matrix4f();

        window = new Window(windowTile, winWidth,winHeight, projection);
        previousTime = glfwGetTime();
        frameTimeAccumulator = 0.0; // Acumulador para o tempo decorrido
        frameCount = 0;

        window.setZoom(1.f);
        window.updateProjectionMatrix();

        float spriteGlobalScale = 64.f;
        entityManager = new EntityManager(spriteGlobalScale);

        glfwSetFramebufferSizeCallback(window.getID(), (windowID, w, h) -> {
            glViewport(0, 0, w, h);
            window.setWidth(w);
            window.setHeight(h);
            window.updateProjectionMatrix();
        });

        GL30.glFrontFace( GL30.GL_CCW );
        GL30.glCullFace(GL30.GL_BACK);
        GL30.glEnable(GL30.GL_CULL_FACE);
    }

    void run(){

        fontShader = new Shader("text.vert", "text.frag");
        fontTexture = TextureLoader.loadTexture(FontsPath.BITMAP_FRANKLIN_GOTHIC_MEDIUM);
        textRenderer = new TextRenderer(fontTexture, FontsPath.FNTINFO_FRANKLIN_GOTHIC_MEDIUM, fontShader);
        guiManager = new GUIManager(window, textRenderer);

        fontShader = new Shader("text.vert", "text.frag");
        cameraView = new Matrix4f().identity();

        while (!glfwWindowShouldClose(window.getID())) {
            GL11.glClearColor((20.f / 255), (40.f / 255), (51.f / 255), 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);

            double currentTime = glfwGetTime();
            float deltaTime = (float) (currentTime - previousTime); // Calcular deltaTime
            previousTime = currentTime; // Atualizar previousTime

            frameTimeAccumulator += deltaTime; // Acumular o tempo decorrido
            frameCount++;

            if (frameTimeAccumulator >= 1.0) { // Se passou um segundo
                glfwSetWindowTitle(window.getID(), "OpenGL Game. FPS[" + frameCount + "]");
                frameCount = 0; // Resetar contagem de frames
                frameTimeAccumulator = 0.0; // Resetar o acumulador
            }

            window.updateProjectionMatrix();

            entityManager.renderEntities(window, frameCount, cameraView, window.getProjection());
            guiManager.render();

            if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
                System.out.println("Pressionou!!!");
                window.setZoom(window.getZoom() + 0.1f);
                window.updateProjectionMatrix();
            } else if (window.isKeyPressed(GLFW_KEY_LEFT_CONTROL)){
                window.setZoom(window.getZoom() - 0.1f);
                window.updateProjectionMatrix();
            }

            glfwPollEvents();
            glfwSwapBuffers(window.getID());
        }

        GL.createCapabilities();
        glfwSwapInterval(1);
        glfwDestroyWindow(window.getID());
        glfwTerminate();
    }
}
