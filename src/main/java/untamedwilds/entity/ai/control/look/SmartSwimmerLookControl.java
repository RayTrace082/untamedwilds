package untamedwilds.entity.ai.control.look;

import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import untamedwilds.entity.ComplexMob;

public class SmartSwimmerLookControl extends SmoothSwimmingLookControl {
    private final ComplexMob mob;

    public SmartSwimmerLookControl(ComplexMob entity, int maxYRotFromCenter) {
        super(entity, maxYRotFromCenter);
        this.mob = entity;
    }

    public void tick() {
        if (!this.mob.isNotMoving() && this.mob.canMove()) {
            super.tick();
        }
    }
}