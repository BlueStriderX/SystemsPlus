package org.schema.game.client.view.gui.weapon;

import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIEnterableListBlockedInterface;
import org.schema.schine.input.InputState;
import thederpgamer.systemsplus.data.WeaponUserData;

import java.util.List;

/**
 * Modified version of WeaponRowElementInterface.
 *
 * @author Schema, TheDerpGamer
 * @since 07/07/2021
 */
public interface WeaponRowElementInterface extends GUIEnterableListBlockedInterface, Comparable<WeaponRowElementInterface>{

    //INSERTED CODE
    public WeaponUserData getUserData();
    public void setUserData(WeaponUserData userData);
    //


    public WeaponDescriptionPanel getDescriptionPanel(InputState state, GUIElement dependend);
    public GUIAncor getWeaponColumn();


    public GUIAncor getMainSizeColumn();


    public GUIAncor getSecondaryColumn();


    public GUIAncor getSizeColumn();


    public GUIAncor getKeyColumn() ;


    public GUIAncor getTertiaryColumn();


    public List<Object> getDescriptionList();
    public int getKey();
    public int getTotalSize();
    public long getUsableId();

    public int getMaxCharges();
    public int getCurrentCharges();
}