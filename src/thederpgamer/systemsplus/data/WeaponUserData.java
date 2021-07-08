package thederpgamer.systemsplus.data;

import api.mod.config.PersistentObjectUtil;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.SegmentControllerUsable;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.systemsplus.SystemsPlus;
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
        PersistentObjectUtil.addObject(SystemsPlus.getInstance().getSkeleton(), this);
        PersistentObjectUtil.save(SystemsPlus.getInstance().getSkeleton());
    }

    public WeaponUserData(SegmentControllerUsable usable) {
        SegmentController segmentController = usable.getSegmentController();
        if(segmentController instanceof ManagedSegmentController<?>) {
            type = usable.getWeaponRowIcon();
            weaponIcon = "DEFAULT";
            name = ElementKeyMap.getInfo(type).getName();
            index = 0;
            entityId = segmentController.getId();
        }
        PersistentObjectUtil.addObject(SystemsPlus.getInstance().getSkeleton(), this);
        PersistentObjectUtil.save(SystemsPlus.getInstance().getSkeleton());
    }
}
