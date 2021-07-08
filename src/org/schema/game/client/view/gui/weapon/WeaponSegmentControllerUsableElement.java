package org.schema.game.client.view.gui.weapon;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SlotAssignment;
import org.schema.game.common.controller.elements.ElementCollectionManager;
import org.schema.game.common.controller.elements.SegmentControllerUsable;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.input.InputState;
import thederpgamer.systemsplus.data.WeaponUserData;
import thederpgamer.systemsplus.utils.DataUtils;
import thederpgamer.systemsplus.utils.ResourceManager;

import java.util.List;

/**
 * Modified version of WeaponSegmentControllerUsableElement.
 *
 * @author Schema, TheDerpGamer
 * @since 07/07/2021
 */
public class WeaponSegmentControllerUsableElement implements WeaponRowElementInterface {

    private static Vector3i absPosTmp = new Vector3i();
    public final SegmentController segmentController;
    public final GameClientState state;
    public final List<Object> descriptionList = new ObjectArrayList<Object>();
    public final SlotAssignment shipConfiguration;
    public GUIAncor weaponColumn;
    public GUIAncor mainSizeColumn;
    public GUIAncor secondaryColumn;
    public GUIAncor sizeColumn;
    public GUIAncor keyColumn;
    public GUIAncor tertiaryColumn;
    private int key = -1;
    private WeaponSlotOverlayElement weaponIcon;
    private final SegmentControllerUsable rc;

    //INSERTED CODE
    private WeaponUserData userData;

    public WeaponUserData getUserData() {
        return userData;
    }

    public void setUserData(WeaponUserData userData) {
        this.userData = userData;
    }
    //

    public WeaponSegmentControllerUsableElement(SegmentControllerUsable m) {
        super();
        this.state = (GameClientState) m.getSegmentController().getState();
        this.segmentController = m.getSegmentController();
        this.rc = m;
        shipConfiguration = m.getSegmentController().getSlotAssignment();
        //INSERTED CODE
        setUserData(DataUtils.getWeaponUserData(m));
        //
        initOverlays();
    }
    @Override
    public long getUsableId(){
        return rc.getUsableId();
    }
    public String getName(){
        return rc.getWeaponRowName();
    }
    public short getIconType(){
        return rc.getWeaponRowIcon();
    }
    public String getFirstColumn(){
        return "";
    }
    public String getSecondColumn(){
        return "";
    }
    public void initOverlays() {
        float scale = 0.75f;
        float scaleSlave = 0.65f;
        weaponIcon = new WeaponSlotOverlayElement(state);
        weaponIcon.setScale(scale, scale, scale);
        weaponIcon.setType(getIconType(), getUsableId());
        //INSERTED CODE
        if(userData.weaponIcon.equals("DEFAULT")) weaponIcon.setSpriteSubIndex(ElementKeyMap.getInfo(getIconType()).getBuildIconNum());
        else weaponIcon.setSprite(ResourceManager.getSprite(userData.weaponIcon));

        GUITextOverlay nameOverlay = new GUITextOverlay(10, 10, state);
        nameOverlay.setTextSimple(getUserData().name);
        //

        GUITextOverlay keyTextOverlay = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium19(), state);
        int key;
        if ((key = shipConfiguration.getByIndex(getUsableId())) >= 0) {
            this.key = key;
            keyTextOverlay.setTextSimple(((key + 1) % 10) + " [" + (((key) / 10) + 1) + "]");
        } else {
            this.key = -1;
            keyTextOverlay.setTextSimple("");
        }
        GUITextOverlay mainSizeText = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium14(), state);
        mainSizeText.setTextSimple(new Object(){
            @Override
            public String toString() {
                return getFirstColumn();
            }

        });


        GUITextOverlay totalSizeText = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium18(), state);
        totalSizeText.setTextSimple(new Object(){
            @Override
            public String toString() {
                return getSecondColumn();
            }

        });

        this.keyColumn = new GUIAncor(state, 32, WeaponScrollableListNew.rowHeight);
        keyTextOverlay.setPos(2, 8, 0);
        this.keyColumn.attach(keyTextOverlay);

        mainSizeColumn = new GUIAncor(state, 32, WeaponScrollableListNew.rowHeight);
        mainSizeText.setPos(4, 8, 0);
        mainSizeColumn.attach(mainSizeText);

        this.weaponColumn = new GUIAncor(state, 32, WeaponScrollableListNew.rowHeight);
        weaponColumn.attach(weaponIcon);
        nameOverlay.setPos(16, 25, 0);
        weaponColumn.attach(nameOverlay);

        this.sizeColumn = new GUIAncor(state, 32, WeaponScrollableListNew.rowHeight);
        sizeColumn.attach(totalSizeText);

        this.secondaryColumn = new GUIAncor(state, 32, WeaponScrollableListNew.rowHeight);


        this.tertiaryColumn = new GUIAncor(state, 32, WeaponScrollableListNew.rowHeight);


    }

    @Override
    public boolean isBlocked() {
        return weaponIcon.isInside();
    }

    @Override
    public WeaponDescriptionPanel getDescriptionPanel(InputState state, GUIElement dependend) {
        WeaponDescriptionPanel pa = new WeaponDescriptionPanel(state, FontLibrary.getBlenderProMedium14(), dependend);

        return pa;
    }


    public void update(ElementCollectionManager<?, ?, ?> selectedManager, List<Object> text) {
        if (selectedManager.getContainer().getSegmentController() != ((GameClientState) selectedManager.getSegmentController().getState()).getCurrentPlayerObject()) {
            //do not update panel for other controllers
            return;
        }


    }

    public void update(String selectedManager, List<Object> text) {
        StringBuffer b = new StringBuffer();
        b.append(Lng.str("Type: 		")+getName()+"\n"
        );
        text.add(b.toString());
    }
    @Override
    public int getTotalSize(){
        return 1;
    }
    @Override
    public int compareTo(WeaponRowElementInterface s) {
        return (s.getTotalSize()) - getTotalSize();
    }

    @Override
    public GUIAncor getWeaponColumn() {
        return weaponColumn;
    }


    @Override
    public GUIAncor getMainSizeColumn() {
        return mainSizeColumn;
    }


    @Override
    public GUIAncor getSecondaryColumn() {
        return secondaryColumn;
    }


    @Override
    public GUIAncor getSizeColumn() {
        return sizeColumn;
    }


    @Override
    public GUIAncor getKeyColumn() {
        return keyColumn;
    }


    @Override
    public GUIAncor getTertiaryColumn() {
        return tertiaryColumn;
    }


    @Override
    public List<Object> getDescriptionList() {
        return descriptionList;
    }

    @Override
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
    }
    @Override
    public int getMaxCharges() {
        return rc.getMaxCharges();
    }
    @Override
    public int getCurrentCharges() {
        return rc.getCharges();
    }
}