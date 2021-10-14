package thederpgamer.systemsplus.utils;

import api.common.GameCommon;
import api.mod.ModSkeleton;
import thederpgamer.systemsplus.SystemsPlus;

/**
 * Miscellaneous data related utilities.
 *
 * @author TheDerpGamer
 * @version 1.0 - [10/12/2021]
 */
public class DataUtils {

    private static final ModSkeleton instance = SystemsPlus.getInstance().getSkeleton();

    public static String getResourcesPath() {
        return instance.getResourcesFolder().getPath().replace('\\', '/');
    }

    public static String getWorldDataPath() {
        return getResourcesPath() + "/data/" + GameCommon.getUniqueContextId();
    }
}
