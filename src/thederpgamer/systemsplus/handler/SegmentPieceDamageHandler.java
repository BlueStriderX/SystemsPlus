package thederpgamer.systemsplus.handler;

import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.ShipManagerContainer;
import org.schema.game.common.controller.elements.SpaceStationManagerContainer;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/04/2021
 */
public class SegmentPieceDamageHandler {

    public static float handle(SegmentPiece hitSegment, float totalDamage) {
        SegmentController segmentController = hitSegment.getSegmentController();
        ManagerContainer<?> managerContainer = getManagerContainer(segmentController);

        return totalDamage;
    }

    private static ManagerContainer<?> getManagerContainer(SegmentController segmentController) {
        if(segmentController.getType().equals(SimpleTransformableSendableObject.EntityType.SHIP)) {
            return (ShipManagerContainer) ((ManagedSegmentController) segmentController).getManagerContainer();
        } else if(segmentController.getType().equals(SimpleTransformableSendableObject.EntityType.SPACE_STATION)) {
            return (SpaceStationManagerContainer) ((ManagedSegmentController) segmentController).getManagerContainer();
        } else return null;
    }
}
