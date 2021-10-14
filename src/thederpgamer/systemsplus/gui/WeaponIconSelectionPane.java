package thederpgamer.systemsplus.gui;

import org.schema.common.util.StringTools;
import org.schema.game.client.view.gui.weapon.WeaponRowElementInterface;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.gui.*;
import org.schema.schine.graphicsengine.forms.gui.newgui.*;
import org.schema.schine.input.InputState;
import thederpgamer.systemsplus.utils.Colors;
import thederpgamer.systemsplus.utils.DataUtils;
import thederpgamer.systemsplus.utils.ResourceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/07/2021
 */
public class WeaponIconSelectionPane extends ScrollableTilePane<Sprite> {

    private Sprite selectedIcon;
    private boolean firstDraw = true;

    public WeaponIconSelectionPane(InputState state, final WeaponRowElementInterface rowElement) {
        super(state, new GUIAncor(state, 450, 600), 64, 64);
        addBottomButton(new GUICallback() {
            @Override
            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                if(mouseEvent.pressedLeftMouse()) {
                    int layer = ElementKeyMap.getInfo(DataUtils.getWeaponUserData(rowElement).type).getBuildIconNum() / 256;
                    selectedIcon = Controller.getResLoader().getSprite("build-icons-" + StringTools.formatTwoZero(layer) + "-16x16-gui-");
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        }, "DEFAULT", ControllerElement.FilterRowStyle.LEFT, ControllerElement.FilterPos.TOP);

        addDropdownFilter(new GUIListFilterDropdown<Sprite, String>(getColorsValues()) {
            @Override
            public boolean isOk(String color, Sprite sprite) {
                return sprite.getName().endsWith(color.toLowerCase());
            }
        }, new CreateGUIElementInterface<String>() {
            @Override
            public GUIElement create(String color) {
                GUIAncor anchor = new GUIAncor(getState(), 10.0F, 24.0F);
                GUITextOverlayTableDropDown dropDown;
                (dropDown = new GUITextOverlayTableDropDown(10, 10, getState())).setTextSimple(color);
                dropDown.setPos(4.0F, 4.0F, 0.0F);
                anchor.setUserPointer(color);
                anchor.attach(dropDown);
                return anchor;
            }

            @Override
            public GUIElement createNeutral() {
                return null;
            }
        }, ControllerElement.FilterRowStyle.RIGHT, ControllerElement.FilterPos.TOP);
    }

    @Override
    public void updateListEntries(GUITilePane<Sprite> guiTilePane, Set<Sprite> set) {
        guiTilePane.deleteObservers();
        guiTilePane.addObserver(this);
        guiTilePane.spacingX = 0;
        guiTilePane.spacingY = 0;
        int x = 32;
        int y = 32;
        int i = 0;
        int columns = 7;
        for(final Sprite sprite : set) {
            GUIOverlay overlay = new GUIOverlay(sprite, getState());
            GUIIconButton iconButton = new GUIIconButton(getState(), 64, 64, overlay, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) selectedIcon = sprite;
                }

                @Override
                public boolean isOccluded() {
                    return false;
                }
            });
            iconButton.onInit();
            iconButton.attach(overlay);
            iconButton.getBackgroundColor().w = 0.0f;
            guiTilePane.addTile(iconButton, sprite);
            GUITileParam<Sprite> tile = guiTilePane.getTiles().get(guiTilePane.getTiles().size() - 1);
            tile.getPos().x = x;
            tile.getPos().y = y;
            tile.setWidth(0);
            tile.setHeight(0);

            if(i < columns) {
                x -= 64;
                i ++;
            } else {
                x = 32;
                i = 0;
            }
        }
    }

    @Override
    protected Collection<Sprite> getElementList() {
        if(firstDraw) {
            ArrayList<Sprite> spriteList = new ArrayList<>();
            for(Sprite sprite : ResourceManager.spriteMap.values()) if(sprite.getName().endsWith("white")) spriteList.add(sprite);
            firstDraw = false;
            return spriteList;
        } else return ResourceManager.spriteMap.values();
    }

    public Sprite getSelectedIcon() {
        return selectedIcon;
    }

    private String[] getColorsValues() {
        String[] values = new String[Colors.values().length];
        for(int i = 0; i < values.length; i ++) values[i] = Colors.values()[i].name();
        return values;
    }
}
