package Game.Entidades;

import Game.Entidades.Animation.AnimationManager;
import Game.Shader;
import Game.Window;
import Utils.TextureLoader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

public class NPC extends PrimitiveEntity{

    AnimationManager animationManager;

    private String name;
    private boolean isMoving;

    public NPC(String texturePath){

        super(new Shader("player.vert", "player.frag"));

        this.texture = TextureLoader.loadTexture(texturePath);
        this.animationManager = new AnimationManager(8, 0, 16, 12);

        this.position.x = 368.f;
        this.position.y = 400.f;
    }

    public void update(Window window, float frameCount, Matrix4f view, Matrix4f projection){

        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);

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

        animationManager.setQuantityFrames(1);
        animationManager.play(frameCount);
    }
}
