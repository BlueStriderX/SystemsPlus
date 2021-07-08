package thederpgamer.systemsplus.utils;

import api.utils.textures.StarLoaderTexture;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.systemsplus.SystemsPlus;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Manages mod files and resources.
 *
 * @author TheDerpGamer
 * @since 07/02/2021
 */
public class ResourceManager {

    private static final String[] spriteNames = {
            "hex-icon-1",
            "hex-icon-2",
            "hex-icon-3",
            "triangle-icon-1",
            "triangle-icon-2",
            "triangle-icon-3",
            "inverted-triangle-icon-1",
            "inverted-triangle-icon-2",
            "inverted-triangle-icon-3"
    };

    public static HashMap<String, Sprite> spriteMap = new HashMap<>();

    public static void loadResources(final SystemsPlus instance, final ResourceLoader loader) {
        StarLoaderTexture.runOnGraphicsThread(new Runnable() {
            @Override
            public void run() {
                //Load Sprites
                for(String spriteName : spriteNames) {
                    BufferedImage spriteImage = instance.getJarBufferedImage("thederpgamer/systemsplus/resources/sprites/" + spriteName + ".png");
                    for(Colors color : Colors.values()) {
                        String spriteVariant = spriteName + "-" + color.name().toLowerCase();
                        Sprite sprite = StarLoaderTexture.newSprite(Colors.setTint(spriteImage, color), instance, spriteVariant);
                        sprite.setName(spriteVariant);
                        spriteMap.put(spriteVariant, sprite);
                    }
                }
            }
        });
    }

    public static Sprite getSprite(String name) {
        return spriteMap.get(name);
    }
}
