package thederpgamer.systemsplus.utils;

import api.common.GameClient;
import api.common.GameCommon;
import api.mod.config.PersistentObjectUtil;
import org.schema.game.client.view.gui.weapon.WeaponPowerBatteryRowElement;
import org.schema.game.client.view.gui.weapon.WeaponRowElement;
import org.schema.game.client.view.gui.weapon.WeaponRowElementInterface;
import org.schema.game.client.view.gui.weapon.WeaponSegmentControllerUsableElement;
import org.schema.game.common.controller.elements.SegmentControllerUsable;
import org.schema.game.common.data.SegmentPiece;
import thederpgamer.systemsplus.SystemsPlus;
import thederpgamer.systemsplus.data.WeaponUserData;

import java.util.ArrayList;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 06/09/2021
 */
public class DataUtils {

    public static String getResourcesPath() {
        return SystemsPlus.getInstance().getSkeleton().getResourcesFolder().getPath().replace('\\', '/');
    }

    public static String getWorldDataPath() {
        String universeName = GameCommon.getUniqueContextId();
        if(!universeName.contains(":")) return getResourcesPath() + "/data/" + universeName;
        else {
            try {
                LogManager.logMessage(MessageType.ERROR, "Client " + GameClient.getClientPlayerState().getName() + " attempted to illegally access server data.");
            } catch(Exception ignored) { }
            return null;
        }
    }

    public static WeaponUserData getWeaponUserData(SegmentPiece segmentPiece) {
        ArrayList<WeaponUserData> toRemove = new ArrayList<>();
        for(Object object : PersistentObjectUtil.getObjects(SystemsPlus.getInstance().getSkeleton(), WeaponUserData.class)) {
            WeaponUserData userData = (WeaponUserData) object;
            if(GameCommon.getGameObject(userData.entityId) == null) toRemove.add(userData);
            else if(userData.entityId == segmentPiece.getSegmentController().getId() && userData.index == segmentPiece.getAbsoluteIndex()) {
                return userData;
            }
        }
        for(WeaponUserData userData : toRemove) PersistentObjectUtil.removeObject(SystemsPlus.getInstance().getSkeleton(), userData);
        PersistentObjectUtil.save(SystemsPlus.getInstance().getSkeleton());
        return new WeaponUserData(segmentPiece);
    }

    public static WeaponUserData getWeaponUserData(SegmentControllerUsable usable) {
        ArrayList<WeaponUserData> toRemove = new ArrayList<>();
        for(Object object : PersistentObjectUtil.getObjects(SystemsPlus.getInstance().getSkeleton(), WeaponUserData.class)) {
            WeaponUserData userData = (WeaponUserData) object;
            if(GameCommon.getGameObject(userData.entityId) == null) toRemove.add(userData);
            else if(userData.entityId == usable.getSegmentController().getId() && userData.type == usable.getWeaponRowIcon()) {
                return userData;
            }
        }
        for(WeaponUserData userData : toRemove) PersistentObjectUtil.removeObject(SystemsPlus.getInstance().getSkeleton(), userData);
        PersistentObjectUtil.save(SystemsPlus.getInstance().getSkeleton());
        return new WeaponUserData(usable);
    }

    public static WeaponUserData getWeaponUserData(WeaponRowElementInterface elementInterface) {
        if(elementInterface instanceof WeaponPowerBatteryRowElement) return ((WeaponPowerBatteryRowElement) elementInterface).getUserData();
        else if(elementInterface instanceof WeaponRowElement) return ((WeaponRowElement) elementInterface).getUserData();
        else if(elementInterface instanceof WeaponSegmentControllerUsableElement) return ((WeaponSegmentControllerUsableElement) elementInterface).getUserData();
        else return null;
    }
}