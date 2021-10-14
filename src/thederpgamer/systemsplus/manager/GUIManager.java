package thederpgamer.systemsplus.manager;

import api.common.GameClient;
import api.utils.draw.ModWorldDrawer;
import org.schema.game.client.view.creaturetool.CreatureTool;
import org.schema.schine.graphicsengine.core.Timer;

/**
 * Manages mod GUI drawing.
 *
 * @author TheDerpGamer
 * @version 1.0 - [10/12/2021]
 */
public class GUIManager extends ModWorldDrawer {

    private boolean drawCreatureTool = false;

    private CreatureTool creatureTool;

    @Override
    public void onInit() {

    }

    @Override
    public void update(Timer timer) {
        if(drawCreatureTool) {
            if(creatureTool == null) creatureTool = new CreatureTool(GameClient.getClientState(), timer);
            else {
                creatureTool.onDiasble();
                creatureTool = null;
            }
            drawCreatureTool = false;
        }
    }

    @Override
    public void draw() {
        if(drawCreatureTool && creatureTool != null) creatureTool.draw();
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    public void toggleCreatureTool() {
        drawCreatureTool = !drawCreatureTool;
    }
}
