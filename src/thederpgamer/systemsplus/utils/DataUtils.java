package thederpgamer.systemsplus.utils;

import api.common.GameClient;
import api.common.GameCommon;
import api.mod.config.PersistentObjectUtil;
import org.schema.game.common.data.SegmentPiece;
import thederpgamer.systemsplus.SystemsPlus;
import thederpgamer.systemsplus.data.WeaponUserData;

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
        for(Object object : PersistentObjectUtil.getObjects(SystemsPlus.getInstance().getSkeleton(), WeaponUserData.class)) {
            WeaponUserData userData = (WeaponUserData) object;
            if(userData.entityId == segmentPiece.getSegmentController().getId() && userData.index == segmentPiece.getAbsoluteIndex()) {
                return userData;
            }
        }
        return new WeaponUserData(segmentPiece);
    }
}