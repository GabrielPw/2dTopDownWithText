package Game.Entidades;

import Game.Entidades.Animation.ActualAnimation;
import Game.Entidades.Animation.AnimationManager;
import Utils.Primitives;
import Game.Shader;
import Game.Window;
import Utils.TextureLoader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Player extends PrimitiveEntity{

    private float healthPoints;
    private float speed;

    private boolean isMoving;
    private AnimationManager animationManager;

    public Player(String texturePath){

        super(
            new Shader("player.vert", "player.frag")
        );

        // a janela tem (800, 800)

        createBuffers();
        healthPoints = 100.f;
        speed        = 7.8f;

        position.x =  1900.f;
        position.y = -0.f;

        this.texture = TextureLoader.loadTexture(texturePath);
        this.animationManager = new AnimationManager(3, 0, 16, 3);
    }

    public void render() {

        GL30.glBindVertexArray(VAO);
        VBO.bind();
        EBO.bind();

        GL11.glDrawElements(GL11.GL_TRIANGLES, Primitives.squareIndices.length, GL11.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
    }

    public void update(Window window, float frameCount, Matrix4f view, Matrix4f projection){

        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);

        isMoving = false;
        move(window, frameCount);
        if (isMoving) {
            animationManager.setQuantityFrames(8);
            //System.out.println("IsMoving");
        }else {
            animationManager.setQuantityFrames(1);
        }
        animationManager.play(frameCount);

        shader.use();

        model.identity();
        shader.addUniform1f("time", frameCount);
        shader.addUniformMatrix4fv("projection", projection);
        shader.addUniformMatrix4fv("view", view);
        shader.addUniform1f("texture", texture);
        shader.addUniform2fv("actualSpriteOffset", new Vector2f(animationManager.getActualFrame(), animationManager.getActualAnimation()));

        model.identity();

        model.translate(new Vector3f(this.position, 0.f));
        model.scale(this.scale.x, this.scale.y, 1.f);

        shader.addUniformMatrix4fv("model", model);

        animationManager.setQuantityFrames(3);
        animationManager.play(frameCount);
    }

    private void move(Window window, float deltaTime){
        Vector2f direction = new Vector2f(0, 0);

        int framesPerSecond = 1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            isMoving = true;
            animationManager.setActualAnimation(ActualAnimation.WALKING_UP, 3);
            direction.y += 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            isMoving = true;
            animationManager.setFramesPerSecond(framesPerSecond);
            animationManager.setActualAnimation(0, 3);
            direction.y -= 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            isMoving = true;
            animationManager.setFramesPerSecond(framesPerSecond);
            animationManager.setActualAnimation(ActualAnimation.WALKING_RIGHT, 3);
            direction.x -= 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            isMoving = true;
            animationManager.setFramesPerSecond(framesPerSecond);
            animationManager.setActualAnimation(ActualAnimation.WALKING_LEFT, 3);
            direction.x += 1;
        }

        if (direction.length() > 0) {
            direction.normalize();
            position.add(direction.mul(speed));
        }
    }

}
