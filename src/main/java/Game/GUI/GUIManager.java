package Game.GUI;

import Game.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class GUIManager {

    private Window window;
    private TextRenderer textRenderer;
    private float guiScale;

    public GUIManager(Window window, TextRenderer textRenderer){

        this.window       = window;
        this.textRenderer = textRenderer;
    }

    public void render(){

        renderText();
        //renderIcons();
    }

    private void renderText(){
        textRenderer.renderText("Coins: 0", new Vector2f(8, 788), new Vector2f(128, 128), window.getProjection(), new Matrix4f().identity());
    }

    private void renderIcons(){

    }
}
