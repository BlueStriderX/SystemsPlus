package thederpgamer.systemsplus.handler;

import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.ShipManagerContainer;
import org.schema.game.common.controller.elements.SpaceStationManagerContainer;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import thederpgamer.systemsplus.systems.ArmorHPSystem;

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
        if(managerContainer != null && managerContainer.getModMCModule(hitSegment.getType()) != null) {
            ArmorHPSystem armorHPSystem = (ArmorHPSystem) managerContainer.getModMCModule(hitSegment.getType());
            armorHPSystem.setCurrentHP((float) Math.max(armorHPSystem.getCurrentHP() - (totalDamage * 0.1), 0));
            totalDamage -= (totalDamage * (armorHPSystem.getCurrentHP() / armorHPSystem.getMaxHP())) * 0.85;
        }
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
