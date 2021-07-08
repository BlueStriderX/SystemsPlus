package thederpgamer.systemsplus.utils;

import api.mod.config.FileConfiguration;
import thederpgamer.systemsplus.SystemsPlus;

/**
 * Manages mod config files and data.
 *
 * @author TheDerpGamer
 * @since 06/07/2021
 */
public class ConfigManager {

    //Main Config
    private static FileConfiguration mainConfig;
    private static final String[] defaultMainConfig = {
            "debug-mode: false",
            "max-world-logs: 5"
    };

    //System Config
    private static FileConfiguration systemConfig;
    private static final String[] defaultSystemConfig = {

    };

    public static void initialize(SystemsPlus instance) {
        mainConfig = instance.getConfig("config");
        mainConfig.saveDefault(defaultMainConfig);

        systemConfig = instance.getConfig("system-config");
        systemConfig.saveDefault(defaultSystemConfig);
    }

    public static FileConfiguration getMainConfig() {
        return mainConfig;
    }

    public static FileConfiguration getSystemConfig() {
        return systemConfig;
    }
}