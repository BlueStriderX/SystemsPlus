package thederpgamer.systemsplus.utils;

import javax.vecmath.Vector4f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/07/2021
 */
public enum Colors {
    BLACK(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f)),
    DARK_GREY(new Vector4f(0.3f, 0.3f, 0.3f, 1.0f)),
    LIGHT_GREY(new Vector4f(0.7f, 0.7f, 0.7f, 1.0f)),
    WHITE(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)),
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
}
