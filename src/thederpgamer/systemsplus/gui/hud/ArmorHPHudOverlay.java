package thederpgamer.systemsplus.gui.hud;

import api.common.GameClient;
import org.schema.common.util.StringTools;
import org.schema.game.client.view.BuildModeDrawer;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelpManager;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelperContainer;
import org.schema.game.common.controller.SegmentController;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHelperIcon;
import org.schema.schine.input.InputState;
import java.lang.reflect.Field;
import java.util.List;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/04/2021
 */
public class ArmorHPHudOverlay extends GUIAncor {

    private GUITextOverlay textOverlay;

    public ArmorHPHudOverlay(InputState state) {
        super(state);
    }

    @Override
    public void onInit() {
        super.onInit();
        (textOverlay = new GUITextOverlay(32, 32, FontLibrary.FontSize.MEDIUM, getState())).onInit();
        attach(textOverlay);
    }

    public void updateText(SegmentController segmentController, float current, float max) {
        if(segmentController.isOnServer()) {
            try {
                if(max > 0) {
                   if(BuildModeDrawer.currentPiece.getInfo().isArmor() && !GameClient.getClientState().isInFlightMode() && GameClient.getClientState().isInAnyStructureBuildMode()) {
                        textOverlay.setTextSimple("Armor HP: " + StringTools.formatPointZero(current) + " / " + StringTools.formatPointZero(max));
                        textOverlay.setPos((float) GLFrame.getWidth() / 2 + 10, getMouseYHeight() + 377, 0);
                    } else textOverlay.setTextSimple("");
                }
            } catch(Exception ignored) { }
        }
    }

    private float getMouseYHeight() {
        try {
            Field queueMouseField = HudContextHelpManager.class.getDeclaredField("queueMouse");
            queueMouseField.setAccessible(true);
            List<HudContextHelperContainer> queueMouse = (List<HudContextHelperContainer>) queueMouseField.get(GameClient.getClientState().getWorldDrawer().getGuiDrawer().getHud().getHelpManager());
            int mouseYHeight = 0;
            for(HudContextHelperContainer h : queueMouse) {
                Field iconField = h.getClass().getDeclaredField("icon");
                iconField.setAccessible(true);
                GUIHelperIcon icon = (GUIHelperIcon) iconField.get(h);
                mouseYHeight += icon.getHeight();
            }
            return mouseYHeight;
        } catch(NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
