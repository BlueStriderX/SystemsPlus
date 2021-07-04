package thederpgamer.systemsplus.systems;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import api.utils.game.module.ModManagerContainerModule;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.graphicsengine.core.Timer;
import thederpgamer.systemsplus.SystemsPlus;
import thederpgamer.systemsplus.utils.ConfigManager;
import java.io.IOException;
import java.util.HashMap;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/04/2021
 */
public class ArmorHPSystem extends ModManagerContainerModule {

    private float currentHP;
    private float maxHP;

    public ArmorHPSystem(SegmentController ship, ManagerContainer<?> managerContainer, short blockId) {
        super(ship, managerContainer, SystemsPlus.getInstance(), blockId);
    }

    @Override
    public void handle(Timer timer) {

    }

    @Override
    public void onTagSerialize(PacketWriteBuffer packetWriteBuffer) throws IOException {

    }

    @Override
    public void onTagDeserialize(PacketReadBuffer packetReadBuffer) throws IOException {

    }

    @Override
    public double getPowerConsumedPerSecondResting() {
        return 0;
    }

    @Override
    public double getPowerConsumedPerSecondCharging() {
        return 0;
    }

    @Override
    public String getName() {
        return "Armor HP System";
    }

    @Override
    public void reloadFromReactor(double v, Timer timer, float v1, boolean b, float v2) {
        calculateHP();
    }

    public void calculateHP() {
        HashMap<Integer[], Integer> blockMap = new HashMap<>();
        Vector3i min = new Vector3i();
        Vector3i max = new Vector3i();
        for(long index : blocks.keySet()) {
            SegmentPiece block = getManagerContainer().getSegmentController().getSegmentBuffer().getPointUnsave(index);
            blockMap.put(new Integer[] {block.getAbsolutePosX(), block.getAbsolutePosY(), block.getAbsolutePosZ()}, block.getHitpointsFull());
            if(block.getAbsolutePos(new Vector3i()).compareTo(min) < 0) min = block.getAbsolutePos(new Vector3i());
            if(block.getAbsolutePos(new Vector3i()).compareTo(max) > 0) max = block.getAbsolutePos(new Vector3i());
        }

        float positiveIntegrity = 0;
        float negativeIntegrity = 0;
        float positiveModifier = (float) ConfigManager.getSystemConfig().getDouble("positive-armor-integrity-modifier");
        float negativeModifier = (float) ConfigManager.getSystemConfig().getDouble("negative-armor-integrity-modifier");
        float negativeAdd = ElementKeyMap.getInfo(getBlockId()).armorValue;

        for(int x = min.x; x < max.x; x ++) {
            for(int z = min.z; z < max.z; z ++) {
                for(int y = min.y; y < max.y; y ++) {
                    Integer[] pos = new Integer[] {x, y, z};
                    if(blockMap.containsKey(pos)) positiveIntegrity += blockMap.get(pos) * positiveModifier;
                    else negativeIntegrity += negativeAdd * negativeModifier;
                }
            }
        }

        setMaxHP(Math.max(positiveIntegrity - negativeIntegrity, 0));
        setCurrentHP(maxHP);
    }

    public float getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(float currentHP) {
        if(currentHP < 0) currentHP = 0;
        this.currentHP = Math.min(currentHP, this.maxHP);
        try {
            SystemsPlus.getInstance().armorHPHudOverlay.updateText(segmentController, this.currentHP, this.maxHP);
        } catch(Exception ignored) { }
    }

    public float getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(float maxHP) {
        this.maxHP = maxHP;
        if(this.currentHP < this.maxHP) this.currentHP = this.maxHP;
    }
}
