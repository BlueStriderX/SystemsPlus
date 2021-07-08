package org.schema.game.client.view.gui.weapon;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SlotAssignment;
import org.schema.game.common.controller.elements.ElementCollectionManager;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.controller.elements.powerbattery.PowerBatteryCollectionManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.input.InputState;
import thederpgamer.systemsplus.data.WeaponUserData;
import thederpgamer.systemsplus.utils.Colors;
import thederpgamer.systemsplus.utils.DataUtils;
import thederpgamer.systemsplus.utils.ResourceManager;

import java.util.List;

/**
 * Modified version of WeaponPowerBatteryRowElement.
 *
 * @author Schema, TheDerpGamer
 * @since 07/07/2021
 */
public class WeaponPowerBatteryRowElement implements WeaponRowElementInterface {

    private static Vector3i absPosTmp = new Vector3i();
    private final SegmentPiece piece;
    public final ElementInformation info;
    public final SegmentController segmentController;
    public final GameClientState state;
    public final List<Object> descriptionList = new ObjectArrayList<Object>();
    public SlotAssignment shipConfiguration;
    public GUIAncor weaponColumn;
    public GUIAncor mainSizeColumn;
    public GUIAncor secondaryColumn;
    public GUIAncor sizeColumn;
    public GUIAncor keyColumn;
    public GUIAncor tertiaryColumn;
    private int key = -1;
    private WeaponSlotOverlayElement weaponIcon;
    private PowerBatteryCollectionManager man;

    //INSERTED CODE
    private WeaponUserData userData;

    @Override
    public WeaponUserData getUserData() {
        return userData;
    }

    @Override
    public void setUserData(WeaponUserData userData) {
        this.userData = userData;
    }
    //

    public WeaponPowerBatteryRowElement(SegmentPiece piece, PowerBatteryCollectionManager man) {
        super();
        assert (ElementKeyMap.isValidType(piece.getType()));
        this.man = man;
        this.piece = piece;
        this.info = ElementKeyMap.getInfo(piece.getType());
        //INSERTED CODE
        setUserData(DataUtils.getWeaponUserData(piece));
        //
        this.state = (GameClientState) piece.getSegment().getSegmentController().getState();
        this.segmentController = piece.getSegment().getSegmentController();
        shipConfiguration = piece.getSegmentController().getSlotAssignment();



        initOverlays();
        updateInfo(piece, descriptionList);
    }

    public void initOverlays() {
        float scale = 0.75f;
        float scaleSlave = 0.65f;
        weaponIcon = new WeaponSlotOverlayElement(state);
        weaponIcon.setScale(scale, scale, scale);
        weaponIcon.setType(piece.getType(), piece.getAbsoluteIndex());
        //INSERTED CODE
        if(userData.weaponIcon.equals("DEFAULT")) weaponIcon.setSpriteSubIndex(ElementKeyMap.getInfo(piece.getType()).getBuildIconNum());
        else {
            weaponIcon.setSprite(ResourceManager.getSprite(userData.weaponIcon));
            weaponIcon.getSprite().setTint(Colors.valueOf(userData.color).color);
        }
        GUITextOverlay nameOverlay = new GUITextOverlay(10, 10, state);
        if (info.id == ElementKeyMap.LOGIC_REMOTE_INNER) {

            nameOverlay.setTextSimple(new Object() {

                /* (non-Javadoc)
                 * @see java.lang.Object#toString()
                 */
                @Override
                public String toString() {
                    long index = piece.getAbsoluteIndexWithType4();
                    String tx = piece.getSegmentController().getTextMap().get(index);

                    if (tx == null) {
                        ((ClientSegmentProvider) piece.getSegmentController().getSegmentProvider()).getSendableSegmentProvider().clientTextBlockRequest(index);
                        tx = "";
                    }
                    return tx;
                }

            });
        } else nameOverlay.setTextSimple(userData.name);
        //



        GUITextOverlay keyTextOverlay = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium19(), state);
        int key;
        if ((key = shipConfiguration.getByPos(piece.getAbsolutePos(absPosTmp))) >= 0) {
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
                return String.valueOf(man.getTotalSize());
            }

        });


        GUITextOverlay totalSizeText = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium18(), state);
        totalSizeText.setTextSimple(new Object(){
            @Override
            public String toString() {
                return String.valueOf(man.getTotalSize());
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

        pa.update(man);

        return pa;
    }

    private void updateInfo(SegmentPiece updateFor, List<Object> text) {
        text.clear();
        if (updateFor == null) {
            //reset
            return;
        }
        if (piece != null) {
            SegmentController s = segmentController;
            if (s == null) {
                return;
            }
            System.err.println("[WEAPONROW] UPDATE FOR: " + updateFor);
            if (updateFor != null && updateFor.getType() != Element.TYPE_NONE) {
                ManagerModuleCollection<?, ?, ?> managerModuleCollection = ((ManagedSegmentController<?>) s).getManagerContainer().getModulesControllerMap().get(updateFor.getType());

                update("POWER_BATTERY", text);

            }
        }
    }

    public void update(ElementCollectionManager<?, ?, ?> selectedManager, List<Object> text) {
        if (selectedManager.getContainer().getSegmentController() != ((GameClientState) selectedManager.getSegmentController().getState()).getCurrentPlayerObject()) {
            //do not update panel for other controllers
            return;
        }


    }

    public void update(String selectedManager, List<Object> text) {
        StringBuffer b = new StringBuffer();
        b.append(Lng.str("Type: 		")+ElementKeyMap.getInfo(piece.getType()).getName()+"\n"
        );
        text.add(b.toString());
    }
    @Override
    public int getTotalSize(){
        return man.getTotalSize();
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

    public SegmentPiece getPiece() {
        return piece;
    }

    @Override
    public long getUsableId() {
        return piece.getAbsoluteIndex();
    }

    @Override
    public int getMaxCharges() {
        return 0;
    }

    @Override
    public int getCurrentCharges() {
        return 0;
    }


}