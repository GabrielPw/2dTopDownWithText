package Game.GUI;

import Game.Entidades.PrimitiveEntity;
import Game.Shader;

public class RepeatedSpite extends PrimitiveEntity {

    public RepeatedSpite(Shader shader){

        super(shader);

        this.createBuffers();
    }

    @Override
    protected void createBuffers() {

    }
}
