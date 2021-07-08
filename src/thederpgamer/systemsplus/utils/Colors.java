package thederpgamer.systemsplus.utils;

import javax.vecmath.Vector4f;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/07/2021
 */
public enum Colors {
    WHITE(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)),
    LIGHT_GREY(new Vector4f(0.7f, 0.7f, 0.7f, 1.0f)),
    DARK_GREY(new Vector4f(0.3f, 0.3f, 0.3f, 1.0f)),
    BLACK(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f)),
    YELLOW(new Vector4f(1.0f, 1.0f, 0.0f, 1.0f)),
    ORANGE(new Vector4f(1.0f, 0.78f, 0.0f, 1.0f)),
    RED(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)),
    BLUE(new Vector4f(0.0f, 0.0f, 1.0f, 1.0f)),
    CYAN(new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)),
    GREEN(new Vector4f(0.0f, 1.0f, 0.0f, 1.0f)),
    MAGENTA(new Vector4f(1.0f, 0.0f, 1.0f, 1.0f)),
    PINK(new Vector4f(1.0f, 0.68f, 0.68f, 1.0f));

    public Vector4f color;

    Colors(Vector4f color) {
        this.color = color;
    }

    public int getR() {
        return (int) (color.x * 255.0f);
    }

    public int getG() {
        return (int) (color.y * 255.0f);
    }

    public int getB() {
        return (int) (color.z * 255.0f);
    }

    public int getRGB() {
        return new Color(getR(), getG(), getB()).getRGB();
    }

    public static Colors getFromName(String name) {
        for(Colors color : values()) if(color.name().equals(name)) return color;
        return WHITE;
    }

    public static BufferedImage setTint(BufferedImage image, Colors color) {
        for(int x = 0; x < image.getWidth(); x ++) {
            for(int y = 0; y < image.getHeight(); y ++) {
                int pixel = image.getRGB(x, y);
                float alpha = ((pixel >> 24) & 0xff) / 255.0f;
                float red = ((pixel >> 16) & 0xff) / 255.0f;
                float green = ((pixel >> 8) & 0xff) / 255.0f;
                float blue = ((pixel >> 0) & 0xff) / 255.0f;
                Vector4f currentColor = new Vector4f(red, green, blue, alpha);
                if(currentColor.w != 0.0f && currentColor != color.color) image.setRGB(x, y, color.getRGB());
            }
        }
        return image;
    }
}
