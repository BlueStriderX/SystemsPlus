package thederpgamer.systemsplus.manager;

import api.mod.config.FileConfiguration;
import org.lwjgl.input.Keyboard;
import thederpgamer.systemsplus.SystemsPlus;

/**
 * Manages mod config files and values.
 *
 * @author TheDerpGamer
 * @version 1.0 - [10/12/2021]
 */
public class ConfigManager {

    //Main Config
    private static FileConfiguration mainConfig;
    public static final String[] defaultMainConfig = {
            "debug-mode: false",
            "max-world-logs: 5"
    };

    //Key Config
    private static FileConfiguration keyConfig;
    public static final String[] defaultKeyConfig = {

    };

    public static void initialize(SystemsPlus instance) {
        mainConfig = instance.getConfig("config");
        mainConfig.saveDefault(defaultMainConfig);

        keyConfig = instance.getConfig("key-bindings");
        keyConfig.saveDefault(defaultKeyConfig);
    }

    public static FileConfiguration getMainConfig() {
        return mainConfig;
    }

    public static FileConfiguration getKeyConfig() {
        return keyConfig;
    }

    public static String getDefaultValue(String field) {
        if(mainConfig.getKeys().contains(field)) {
            for(String s : defaultMainConfig) {
                String fieldName = s.substring(0, s.lastIndexOf(":") - 1).trim().toLowerCase();
                if(fieldName.equals(field.toLowerCase().trim())) return s.substring(s.lastIndexOf(":") + 1).trim();
            }
        }
        return null;
    }

    public static int getKeyBinding(String field) {
        String binding = keyConfig.getString(field).trim().toUpperCase();
        binding = binding.split("KEY_")[1];
        if(binding.toUpperCase().equals("NONE") || binding.length() == 0) return -1;
        return Keyboard.getKeyIndex(binding);
    }
}