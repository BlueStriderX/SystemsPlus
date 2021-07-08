package thederpgamer.systemsplus.gui;

import org.schema.common.util.StringTools;
import org.schema.game.client.view.gui.weapon.WeaponRowElementInterface;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.gui.*;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITilePane;
import org.schema.schine.graphicsengine.forms.gui.newgui.ScrollableTilePane;
import org.schema.schine.input.InputState;
import thederpgamer.systemsplus.utils.Colors;
import thederpgamer.systemsplus.utils.ResourceManager;
import java.util.ArrayList;
import java.util.Set;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/07/2021
 */
public class WeaponIconSelectionPane extends ScrollableTilePane<Sprite> {

    private WeaponRowElementInterface rowElement;
    private Sprite selectedIcon;

    public WeaponIconSelectionPane(InputState state, final WeaponRowElementInterface rowElement) {
        super(state, new GUIAncor(state), 64, 64);
        this.rowElement = rowElement;

        GUITextOverlay[] colorOverlays = new GUITextOverlay[Colors.values().length];
        for(int i = 0; i < colorOverlays.length; i ++) {
            GUITextOverlay overlay = new GUITextOverlay(50, 24, getState());
            overlay.onInit();
            overlay.setTextSimple(Colors.values()[i].name());
            overlay.setUserPointer(Colors.values()[i].name());
        }
        GUIDropDownList dropDownList = new GUIDropDownList(getState(), 100, 24, 108, new DropDownCallback() {
            @Override
            public void onSelectionChanged(GUIListElement guiListElement) {
                rowElement.getUserData().color = (String) guiListElement.getUserPointer();
            }
        }, colorOverlays);
        dropDownList.onInit();
        attach(dropDownList);
    }

    @Override
    public void updateListEntries(GUITilePane<Sprite> guiTilePane, Set<Sprite> set) {
        guiTilePane.deleteObservers();
        guiTilePane.addObserver(this);

        for(final Sprite sprite : set) {
            GUIOverlay overlay = new GUIOverlay(sprite, getState());
            GUIIconButton iconButton = new GUIIconButton(getState(), 64, 64, overlay, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        getState().getController().queueUIAudio("0022_menu_ui - select 1");
                        selectedIcon = sprite;
                    }
                }

                @Override
                public boolean isOccluded() {
                    return false;
                }
            });
            guiTilePane.addTile(iconButton, sprite);
        }
    }

    @Override
    protected ArrayList<Sprite> getElementList() {
        ArrayList<Sprite> iconSprites = new ArrayList<>();
        int layer = ElementKeyMap.getInfo(rowElement.getUserData().type).getBuildIconNum() / 256;
        iconSprites.add(Controller.getResLoader().getSprite("build-icons-" + StringTools.formatTwoZero(layer) + "-16x16-gui-"));
        iconSprites.addAll(ResourceManager.spriteMap.values());
        return iconSprites;
    }

    public Sprite getSelectedIcon() {
        return selectedIcon;
    }
}
