package Game.Entidades;

import Game.Window;
import Utils.TexturePaths;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    List<PrimitiveEntity> entities = new ArrayList<>();

    float spriteGlobalScale;
    NPC regularNPC = new NPC(TexturePaths.ANNE_NPC);
    Player player = new Player(TexturePaths.JOE01);

    public EntityManager(float spriteGlobalScale){

        this.spriteGlobalScale = spriteGlobalScale;
        entities.add(player);
        entities.add(regularNPC);
    }

    public void renderEntities(Window window, float frameCount, Matrix4f view, Matrix4f projection){

        entities.forEach(
            entity -> {
                entity.scale.x = spriteGlobalScale;
                entity.scale.y = spriteGlobalScale;
                entity.update(window, frameCount, view, projection);
                entity.render();
            }
        );
    }

    public Player getPlayer() {
        return player;
    }
}
