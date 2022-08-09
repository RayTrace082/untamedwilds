package untamedwilds.entity.ai.control.look;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import untamedwilds.entity.ComplexMob;

public class SmartLandLookControl extends SmoothSwimmingLookControl {
    private final ComplexMob mob;
    private final int maxYRot;

    public SmartLandLookControl(ComplexMob entity, int maxYRotFromCenter) {
        super(entity, maxYRotFromCenter);
        this.maxYRot = maxYRotFromCenter;
        this.mob = entity;
    }

    public void tick() {
        if (!this.mob.canMove()) {
            if (this.lookAtCooldown > 0) {
                --this.lookAtCooldown;
                this.getYRotD().ifPresent((p_181134_) -> {
                    this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, p_181134_ + 20.0F, this.yMaxRotSpeed);
                });
                this.getXRotD().ifPresent((p_181132_) -> {
                    this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), p_181132_ + 10.0F, this.xMaxRotAngle));
                });
            } else {
                if (this.mob.getNavigation().isDone()) {
                    this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
                }

                this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
            }

            if (!this.mob.isNotMoving()) {
                float f = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
                if (f < (float)(-this.maxYRot)) {
                    this.mob.yBodyRot -= 4.0F;
                } else if (f > (float)this.maxYRot) {
                    this.mob.yBodyRot += 4.0F;
                }
            }
        }
    }
}