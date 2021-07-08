package thederpgamer.systemsplus.data;

import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;

import java.io.Serializable;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/07/2021
 */
public class WeaponUserData implements Serializable {

    public short type;
    public String name;
    public String weaponIcon;
    public String color;

    public int entityId;
    public long index;

    public WeaponUserData(SegmentPiece segmentPiece) {
        SegmentController segmentController = segmentPiece.getSegmentController();
        if(segmentController instanceof ManagedSegmentController<?>) {
            type = segmentPiece.getType();
            weaponIcon = "DEFAULT";
            name = segmentPiece.getInfo().getName();
            index = segmentPiece.getAbsoluteIndex();
            entityId = segmentController.getId();
        }
    }
}
