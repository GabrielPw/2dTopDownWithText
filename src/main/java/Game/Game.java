package Game;

import Game.Entidades.EntityManager;
import Game.GUI.GUIManager;
import Game.GUI.TextRenderer;
import Game.Mapa.Map;
import Game.Mapa.MapLayer;
import Utils.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class Game {

    private Map firstMap;
    private GUIManager guiManager;
    int fontTexture;
    private Shader fontShader;
    private TextRenderer textRenderer;

    private Matrix4f cameraView;
    private Vector2f cameraPosition = new Vector2f(0, 0);
    private float cameraSpeed = 5.0f;

    private EntityManager entityManager;
    private Window window;
    private double previousTime;
    private double frameTimeAccumulator;
    private int frameCount;

    private float spriteGlobalScale = 64.f;
    public Game(String windowTile, int winWidth, int winHeight){
        Matrix4f projection = new Matrix4f();

        window = new Window(windowTile, winWidth,winHeight, projection);
        previousTime = glfwGetTime();
        frameTimeAccumulator = 0.0; // Acumulador para o tempo decorrido
        frameCount = 0;

        window.setZoom(1.f);
        window.updateProjectionMatrix();

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
        cameraView = new Matrix4f().identity();

        java.util.Map<Integer, Integer> layerIndexAndTexture = new HashMap<>();

        layerIndexAndTexture.put(MapLayer.MapLayerIndexValues.FIRST_LAYER, TextureLoader.loadTexture(TexturePaths.ATLASMAP_BASEMAP01));
        layerIndexAndTexture.put(MapLayer.MapLayerIndexValues.SECOND_LAYER, TextureLoader.loadTexture(TexturePaths.ATLASMAP_HOUSEANDSTUFF));

        firstMap = new Map(MapPath.MAP01_TMJ, layerIndexAndTexture, new Vector2f(400, 400), spriteGlobalScale);

        glfwSwapInterval(1); // 1 para ativar o VSinc (60FPS/HZ)
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

            Vector2f playerPosition = entityManager.getPlayer().getPosition();
            cameraPosition.lerp(new Vector2f(playerPosition.x - window.getWidth() / 2, playerPosition.y - window.getHeight() / 2), deltaTime * cameraSpeed);
            cameraView.identity().translate(-cameraPosition.x, -cameraPosition.y, 0);

            firstMap.render(window.getProjection(), cameraView, deltaTime);
            entityManager.renderEntities(window, frameCount, cameraView, window.getProjection());
            guiManager.render(entityManager.getPlayer());

            glfwPollEvents();
            glfwSwapBuffers(window.getID());
        }

        GL.createCapabilities();
        glfwDestroyWindow(window.getID());
        glfwTerminate();
    }
}
