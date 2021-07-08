package thederpgamer.systemsplus;

import api.mod.StarMod;
import org.apache.commons.io.IOUtils;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.systemsplus.utils.ConfigManager;
import thederpgamer.systemsplus.utils.LogManager;
import thederpgamer.systemsplus.utils.MessageType;
import thederpgamer.systemsplus.utils.ResourceManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * SystemsPlus mod main class.
 *
 * @author TheDerpGamer
 * @since 07/04/2021
 */
public class SystemsPlus extends StarMod {

    //Instance
    private static SystemsPlus instance;
    public static SystemsPlus getInstance() {
        return instance;
    }
    public SystemsPlus() {

    }
    public static void main(String[] args) {

    }

    //Data
    private final String[] overwriteClasses = {
            "WeaponRowElement",
            "WeaponSegmentControllerUsableElement",
            "WeaponPowerBatteryRowElement",
            "WeaponScrollableListNew",
            "WeaponSlotOverlayElement",
            "ProjectileHandlerSegmentController",
            "DamageBeamHitHandlerSegmentController"
    };

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.initialize(this);
        LogManager.initialize();
        LogManager.logMessage(MessageType.INFO, "Successfully loaded mod data.");
        registerListeners();
    }

    @Override
    public void onResourceLoad(ResourceLoader loader) {
        ResourceManager.loadResources(this, loader);
    }

    @Override
    public byte[] onClassTransform(String className, byte[] byteCode) {
        for(String name : overwriteClasses) if(className.endsWith(name)) return overwriteClass(className, byteCode);
        return super.onClassTransform(className, byteCode);
    }

    private void registerListeners() {

    }

    private byte[] overwriteClass(String className, byte[] byteCode) {
        byte[] bytes = null;
        try {
            ZipInputStream file = new ZipInputStream(new FileInputStream(this.getSkeleton().getJarFile()));
            while(true) {
                ZipEntry nextEntry = file.getNextEntry();
                if(nextEntry == null) break;
                if(nextEntry.getName().endsWith(className + ".class")) bytes = IOUtils.toByteArray(file);
            }
            file.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        if(bytes != null) return bytes;
        else return byteCode;
    }
}
