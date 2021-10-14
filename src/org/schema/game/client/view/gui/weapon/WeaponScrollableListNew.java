package org.schema.game.client.view.gui.weapon;

import api.mod.config.PersistentObjectUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.hsqldb.lib.StringComparator;
import org.schema.game.client.controller.PlayerGameOkCancelInput;
import org.schema.game.client.controller.PlayerGameTextInput;
import org.schema.game.client.controller.manager.ingame.PlayerGameControlManager;
import org.schema.game.client.controller.manager.ingame.ship.InShipControlManager;
import org.schema.game.client.controller.manager.ingame.ship.WeaponAssignControllerManager;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.PlayerUsableInterface;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.catalog.CatalogManager;
import org.schema.game.common.data.player.catalog.CatalogPermission;
import org.schema.game.common.data.player.faction.FactionManager;
import org.schema.schine.common.TextCallback;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.settings.PrefixNotFoundException;
import org.schema.schine.graphicsengine.forms.gui.*;
import org.schema.schine.graphicsengine.forms.gui.newgui.*;
import org.schema.schine.input.InputState;
import thederpgamer.systemsplus.SystemsPlus;
import thederpgamer.systemsplus.gui.WeaponIconSelectionPane;
import thederpgamer.systemsplus.utils.DataUtils;

import java.util.*;

/**
 * Modified version of WeaponScrollableListNew.
 *
 * @author Schema, TheDerpGamer
 * @since 07/07/2021
 */
public class WeaponScrollableListNew extends ScrollableTableList<WeaponRowElementInterface> implements Observer, DropTarget<WeaponSlotOverlayElement> {

    private final List<WeaponRowElementInterface> weaponRowList = new ObjectArrayList<WeaponRowElementInterface>();
    public static final int rowHeight = 52;
    public WeaponScrollableListNew(InputState state, GUIElement p) {
        super(state, 100, 100, p);
        columnsHeight = rowHeight;

        getAssignWeaponControllerManager().addObserver(this);
    }

    @Override
    public void checkTarget(MouseEvent e) {
        // TODO Auto-generated method stub
        int inMinX = 208;
        int inMinY = 24;

        int inMaxX = 816;
        int inMaxY = 512;

        if ((getRelMousePos().x < inMinX || getRelMousePos().y < inMinY ||
                getRelMousePos().x > inMaxX || getRelMousePos().y > inMaxY) &&
                !(getState().getWorldDrawer().getGuiDrawer().getPlayerPanel()
                        .getWeaponSideBar()
                        .isInside())) {
            Draggable dragging = getState().getController().getInputController().getDragging();

            if (dragging != null && isTarget(dragging)) {

                if (dragging.checkDragReleasedMouseEvent(e)) {
                    onDrop((WeaponSlotOverlayElement) dragging);
                }
            }
        }
    }

    @Override
    public boolean isTarget(Draggable draggable) {
        return draggable instanceof WeaponSlotOverlayElement;
    }

    @Override
    public void onDrop(WeaponSlotOverlayElement draggable) {
        (new WeaponSlotOverlayElement(getState())).onDrop(draggable);
    }

    /* (non-Javadoc)
     * @see org.schema.schine.graphicsengine.forms.gui.newgui.ScrollableTableList#cleanUp()
     */
    @Override
    public void cleanUp() {
        super.cleanUp();
        getAssignWeaponControllerManager().deleteObserver(this);
    }

    @Override
    public void initColumns() {

        final StringComparator c = new StringComparator();

        addColumn(Lng.str("Hotbar"), 0, new Comparator<WeaponRowElementInterface>() {
            @Override
            public int compare(WeaponRowElementInterface o1, WeaponRowElementInterface o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        addColumn(Lng.str("System"), 4, new Comparator<WeaponRowElementInterface>() {
            @Override
            public int compare(WeaponRowElementInterface o1, WeaponRowElementInterface o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        addFixedWidthColumn(Lng.str("Total Size"), 80, new Comparator<WeaponRowElementInterface>() {
            @Override
            public int compare(WeaponRowElementInterface o1, WeaponRowElementInterface o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        addFixedWidthColumn(Lng.str("Main Size"), 60, new Comparator<WeaponRowElementInterface>() {
            @Override
            public int compare(WeaponRowElementInterface o1, WeaponRowElementInterface o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        addColumn(Lng.str("Secondary Slot"), 2, new Comparator<WeaponRowElementInterface>() {
            @Override
            public int compare(WeaponRowElementInterface o1, WeaponRowElementInterface o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        addColumn(Lng.str("Tertiary Slot"), 2, new Comparator<WeaponRowElementInterface>() {
            @Override
            public int compare(WeaponRowElementInterface o1, WeaponRowElementInterface o2) {
                return o1.getKey() - o2.getKey();
            }
        });

    }

    @Override
    protected Collection<WeaponRowElementInterface> getElementList() {
        reconstructWeaponList();
        return weaponRowList;
    }

    @Override
    public void updateListEntries(GUIElementList mainList,
                                  Set<WeaponRowElementInterface> collection) {
        mainList.deleteObservers();
        mainList.addObserver(this);

        final FactionManager factionManager = getState().getGameState().getFactionManager();
        final CatalogManager catalogManager = getState().getGameState().getCatalogManager();
        final PlayerState player = getState().getPlayer();
        int i = 0;
        //collection is sometimes 1 smaller than the reconstructedWeaponList, see https://phab.starma.de/T2220 which those cases are
        //System.out.println("WeaponSlotOverlay mainList "+ mainList.size() + " collection size " + collection.size() + " weaponRowList size " + getElementList().size());
        List<WeaponRowElementInterface> list = new ArrayList<WeaponRowElementInterface>(getElementList());

        for (final WeaponRowElementInterface f : list) {


            final WeaponRow r = new WeaponRow(getState(), f, f.getKeyColumn(), f.getWeaponColumn(), f.getSizeColumn(), f.getMainSizeColumn(), f.getSecondaryColumn(), f.getTertiaryColumn());

            r.extendableBlockedInterface = f;
            r.expanded = new GUIElementList(getState());

            //INSERTED CODE
            int width = (r.expanded.getWidth() < 100) ? 643 : (int) r.expanded.getWidth();
            final GUITextOverlayTableInnerDescription description = new GUITextOverlayTableInnerDescription(width, 10, getState());
            description.setText(f.getDescriptionList());
            description.setPos(4, 30, 0);

            GUIHorizontalButtonTablePane buttonPane = new GUIHorizontalButtonTablePane(getState(), 3, 1, r.expanded);
            buttonPane.onInit();
            buttonPane.addButton(0, 0, "DETAILS", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        getState().getController().queueUIAudio("0022_menu_ui - select 1");
                        PlayerGameOkCancelInput p = new PlayerGameOkCancelInput("WeaponScrollableListNew_DETAILS", getState(), 400, 400,
                                Lng.str("Details"), "") {

                            @Override
                            public void onDeactivate() {

                            }

                            @Override
                            public void pressedOK() {
                                getState().getController().queueUIAudio("0022_menu_ui - back");
                                deactivate();
                            }
                        };
                        p.getInputPanel().setCancelButton(false);
                        p.getInputPanel().setOkButtonText(Lng.str("DONE"));
                        p.getInputPanel().onInit();
                        WeaponDescriptionPanel pa = f.getDescriptionPanel(getState(), p.getInputPanel().getContent());
                        p.getInputPanel().getContent().attach(pa);
                        p.activate();
                    }
                }

                @Override
                public boolean isOccluded() {
                    return false;
                }
            }, new GUIActivationCallback() {
                @Override
                public boolean isVisible(InputState inputState) {
                    return true;
                }

                @Override
                public boolean isActive(InputState inputState) {
                    return true;
                }
            });

            buttonPane.addButton(1, 0, "RENAME", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        getState().getController().queueUIAudio("0022_menu_ui - select 2");
                        (new PlayerGameTextInput("WeaponRenameInput", getState(), 32, "Rename", "Set a custom name for this system.", DataUtils.getWeaponUserData(f).name) {
                            @Override
                            public String[] getCommandPrefixes() {
                                return null;
                            }

                            @Override
                            public String handleAutoComplete(String s, TextCallback textCallback, String s1) throws PrefixNotFoundException {
                                return null;
                            }

                            @Override
                            public void onFailedTextCheck(String s) {

                            }

                            @Override
                            public void onDeactivate() {
                                getState().getController().queueUIAudio("0022_menu_ui - enter");
                                getAssignWeaponControllerManager().setActive(true);
                            }

                            @Override
                            public boolean onInput(String entry) {
                                if(entry.length() > 0 && !entry.equals(DataUtils.getWeaponUserData(f).name)) {
                                    DataUtils.getWeaponUserData(f).name = entry;
                                    if(f instanceof WeaponPowerBatteryRowElement) ((WeaponPowerBatteryRowElement) f).initOverlays();
                                    else if(f instanceof WeaponRowElement) ((WeaponRowElement) f).initOverlays();
                                    else if(f instanceof WeaponSegmentControllerUsableElement) ((WeaponSegmentControllerUsableElement) f).initOverlays();
                                    PersistentObjectUtil.save(SystemsPlus.getInstance().getSkeleton());
                                    flagDirty();
                                    handleDirty();
                                    return true;
                                } else return false;
                            }
                        }).activate();
                        getAssignWeaponControllerManager().setActive(false);
                    }
                }

                @Override
                public boolean isOccluded() {
                    return false;
                }
            }, new GUIActivationCallback() {
                @Override
                public boolean isVisible(InputState inputState) {
                    return true;
                }

                @Override
                public boolean isActive(InputState inputState) {
                    return true;
                }
            });

            buttonPane.addButton(2, 0, "CHANGE ICON", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        getState().getController().queueUIAudio("0022_menu_ui - select 3");
                        final WeaponIconSelectionPane selectionPane = new WeaponIconSelectionPane(getState(), f);
                        PlayerGameOkCancelInput inputPanel = new PlayerGameOkCancelInput("ChangeWeaponIconPanel", getState(), "Choose Icon", "Choose a custom icon for this weapon.") {
                            @Override
                            public void onDeactivate() {
                                getAssignWeaponControllerManager().setActive(true);
                            }

                            @Override
                            public void pressedOK() {
                                if(selectionPane.getSelectedIcon() != null) {
                                    getState().getController().queueUIAudio("0022_menu_ui - enter");
                                    if(selectionPane.getSelectedIcon().getName().endsWith("-")) DataUtils.getWeaponUserData(f).weaponIcon = "DEFAULT";
                                    else DataUtils.getWeaponUserData(f).weaponIcon = selectionPane.getSelectedIcon().getName();
                                    if(f instanceof WeaponPowerBatteryRowElement) ((WeaponPowerBatteryRowElement) f).initOverlays();
                                    else if(f instanceof WeaponRowElement) ((WeaponRowElement) f).initOverlays();
                                    else if(f instanceof WeaponSegmentControllerUsableElement) ((WeaponSegmentControllerUsableElement) f).initOverlays();
                                    PersistentObjectUtil.save(SystemsPlus.getInstance().getSkeleton());
                                    flagDirty();
                                    handleDirty();
                                    deactivate();
                                }
                            }
                        };
                        inputPanel.getInputPanel().setOkButtonText("SELECT");
                        inputPanel.getInputPanel().onInit();
                        inputPanel.getInputPanel().getContent().setWidth(514);
                        inputPanel.getInputPanel().getBackground().setWidth(514);
                        selectionPane.getPos().y += 30;
                        selectionPane.onInit();
                        inputPanel.getInputPanel().getContent().attach(selectionPane);
                        inputPanel.activate();
                        getAssignWeaponControllerManager().setActive(false);
                    }
                }

                @Override
                public boolean isOccluded() {
                    return false;
                }
            }, new GUIActivationCallback() {
                @Override
                public boolean isVisible(InputState inputState) {
                    return true;
                }

                @Override
                public boolean isActive(InputState inputState) {
                    return true;
                }
            });


            /*
            GUITextButton detailsButton = new GUITextButton(getState(), 80, 24, ColorPalette.OK, "DETAILS", new GUICallback() {
                @Override
                public void callback(GUIElement callingGuiElement, MouseEvent event) {
                    if (event.pressedLeftMouse()) {

                        PlayerGameOkCancelInput p = new PlayerGameOkCancelInput("WeaponScrollableListNew_DETAILS", getState(), 400, 400,
                                Lng.str("Details"), "") {

                            @Override
                            public void onDeactivate() {

                            }

                            @Override
                            public void pressedOK() {
                                deactivate();
                            }
                        };
                        p.getInputPanel().setCancelButton(false);
                        p.getInputPanel().setOkButtonText(Lng.str("DONE"));
                        p.getInputPanel().onInit();
                        WeaponDescriptionPanel pa = f.getDescriptionPanel(getState(), p.getInputPanel().getContent());
                        p.getInputPanel().getContent().attach(pa);
                        p.activate();
                    }
                }

                @Override
                public boolean isOccluded() {
                    return !isActive();
                }
            });
             */
            GUIAncor c = new GUIAncor(getState(), width, 100){

                @Override
                public void draw() {
                    setWidth(r.getWidth());
                    setHeight(description.getTextHeight());
                    super.draw();
                }

            };

            c.attach(description);

            GUIScrollablePanel scroll = new GUIScrollablePanel(width, 100, getState()){

                @Override
                public void draw() {
                    setWidth(r.bg.getWidth()-24);
                    super.draw();
                }

            };
            scroll.setContent(c);
            scroll.onInit();


            GUIListElement elem = new GUIListElement(scroll, scroll, getState());
            elem.heightDiff = 4;
            r.expanded.add(elem);
            r.expanded.attach(buttonPane);
            //
            r.onExpanded = new GUIEnterableListOnExtendedCallback() {

                @Override
                public void extended() {
                    getAssignWeaponControllerManager().setSelectedPiece(f.getUsableId());
                }

                @Override
                public void collapsed() {
                    if (getAssignWeaponControllerManager().getSelectedPiece() == f.getUsableId()) {
                        getAssignWeaponControllerManager().setSelectedPiece(Long.MIN_VALUE);
                    }
                }
            };

            r.onInit();
            mainList.addWithoutUpdate(r);
            i++;
        }
        mainList.updateDim();
    }

    public boolean isPlayerAdmin() {
        return getState().getPlayer().getNetworkObject().isAdminClient.get();
    }

    public boolean canEdit(CatalogPermission f) {
        return f.ownerUID.toLowerCase(Locale.ENGLISH).equals(getState().getPlayer().getName().toLowerCase(Locale.ENGLISH)) || isPlayerAdmin();
    }

    public WeaponAssignControllerManager getAssignWeaponControllerManager() {
        return getPlayerGameControlManager().getWeaponControlManager();
    }

    public InShipControlManager getInShipControlManager() {
        return getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager();
    }

    public PlayerGameControlManager getPlayerGameControlManager() {
        return getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager();
    }

    @Override
    public GameClientState getState() {
        return (GameClientState) super.getState();
    }

    private void reconstructWeaponList() {
        weaponRowList.clear();
        if (getInShipControlManager().getEntered() != null) {
            Ship ship = getState().getShip();
            if (ship == null) {
                return;
            }

            SegmentPiece entered = getInShipControlManager().getEntered();
            final long index = entered.getAbsoluteIndex();
            final short type = entered.getType();

            Collection<PlayerUsableInterface> playerUsable = ship.getManagerContainer().getPlayerUsable();

            for(PlayerUsableInterface p : playerUsable){
                if(p.isPlayerUsable() && p.isControllerConnectedTo(index, type)){
                    WeaponRowElementInterface weaponRow = p.getWeaponRow();
                    if(weaponRow != null){
                        weaponRowList.add(weaponRow);
                    }
                }
            }
        }
        Collections.sort(weaponRowList);
    }


    private class WeaponRow extends Row {


        public WeaponRow(InputState state, WeaponRowElementInterface f, GUIElement... elements) {
            super(state, f, elements);

            this.highlightSelect = true;
        }

        @Override
        public float getExtendedHighlightBottomDist() {
            return 40;
        }


    }

}