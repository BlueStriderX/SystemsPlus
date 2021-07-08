package thederpgamer.systemsplus.utils;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 06/09/2021
 */
public enum MessageType {
    DEBUG("[DEBUG]: "),
    INFO("[INFO]: "),
    WARNING("[WARNING]: "),
    ERROR("[ERROR]: "),
    CRITICAL("[CRITICAL]: ");

    public String prefix;

    MessageType(String prefix) {
        this.prefix = prefix;
    }
}