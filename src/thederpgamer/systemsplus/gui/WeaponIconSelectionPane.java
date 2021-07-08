package thederpgamer.systemsplus.gui;

import org.schema.common.util.StringTools;
import org.schema.game.client.view.gui.weapon.WeaponRowElementInterface;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.*;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITilePane;
import org.schema.schine.graphicsengine.forms.gui.newgui.ScrollableTilePane;
import org.schema.schine.input.InputState;
import thederpgamer.systemsplus.utils.Colors;
import thederpgamer.systemsplus.utils.DataUtils;
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
    private Colors selectedColor;

    public WeaponIconSelectionPane(InputState state, final WeaponRowElementInterface rowElement) {
        super(state, new GUIAncor(state, 500, 500), 64, 64);
        this.rowElement = rowElement;
        this.selectedColor = Colors.WHITE;

        GUITextOverlay[] colorOverlays = new GUITextOverlay[Colors.values().length];
        for(int i = 0; i < colorOverlays.length; i ++) {
            GUITextOverlay overlay = new GUITextOverlay(300, 24, getState());
            overlay.onInit();
            overlay.setFont(FontLibrary.FontSize.MEDIUM.getFont());
            overlay.setTextSimple(Colors.values()[i].name().replace("_", " "));
            overlay.setUserPointer(Colors.values()[i].name());
            colorOverlays[i] = overlay;
        }
        GUIDropDownList dropDownList = new GUIDropDownList(getState(), 300, 24, 150, new DropDownCallback() {
            @Override
            public void onSelectionChanged(GUIListElement guiListElement) {
                String newWeaponIcon = DataUtils.getWeaponUserData(rowElement).weaponIcon;
                if(!newWeaponIcon.equals("DEFAULT") && !newWeaponIcon.endsWith("-")) {
                    newWeaponIcon = (newWeaponIcon.substring(0, newWeaponIcon.lastIndexOf('-'))) + ((String) guiListElement.getUserPointer()).toLowerCase();
                    DataUtils.getWeaponUserData(rowElement).weaponIcon = newWeaponIcon;
                }
                selectedColor = Colors.getFromName((String) guiListElement.getUserPointer());
                flagDirty();
            }
        }, colorOverlays);
        dropDownList.onInit();
        dropDownList.getPos().y -= 40;
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
                    if(mouseEvent.pressedLeftMouse()) selectedIcon = sprite;
                }

                @Override
                public boolean isOccluded() {
                    return false;
                }
            });
            iconButton.onInit();
            overlay.setPos(iconButton.getPos());
            overlay.translate();
            overlay.transform();
            iconButton.getForegroundColor().w = 0.0f;
            iconButton.getBackgroundColor().w = 0.0f;
            guiTilePane.addTile(iconButton, sprite);
        }
    }

    @Override
    protected ArrayList<Sprite> getElementList() {
        ArrayList<Sprite> iconSprites = new ArrayList<>();
        int layer = ElementKeyMap.getInfo(DataUtils.getWeaponUserData(rowElement).type).getBuildIconNum() / 256;
        iconSprites.add(Controller.getResLoader().getSprite("build-icons-" + StringTools.formatTwoZero(layer) + "-16x16-gui-"));
        for(Sprite sprite : ResourceManager.spriteMap.values()) {
            if(sprite.getName().endsWith(selectedColor.name().toLowerCase())) iconSprites.add(sprite);
        }
        return iconSprites;
    }

    public Sprite getSelectedIcon() {
        return selectedIcon;
    }
}
