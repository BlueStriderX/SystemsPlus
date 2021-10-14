package thederpgamer.systemsplus;

import api.mod.StarMod;
import thederpgamer.systemsplus.manager.ConfigManager;
import thederpgamer.systemsplus.manager.LogManager;

/**
 * Main mod class for SystemsPlus.
 *
 * @author TheDerpGamer
 * @version 1.0 - [10/12/2021]
 */
public class SystemsPlus extends StarMod {

    //Instance
    private static SystemsPlus instance;
    public static SystemsPlus getInstance() {
        return instance;
    }
    public SystemsPlus() { }
    public static void main(String[] args) { }

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.initialize(this);
        LogManager.initialize();

        registerListeners();
    }

    private void registerListeners() {

    }
}
