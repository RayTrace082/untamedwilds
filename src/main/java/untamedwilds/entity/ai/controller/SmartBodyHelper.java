package untamedwilds.entity.ai.controller;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.util.math.MathHelper;

public class SmartBodyHelper extends BodyController {
    private static final float MAX_ROTATE = 75;

    private static final int HISTORY_SIZE = 10;

    private final MobEntity entity;

    private int rotateTime;

    private float targetYawHead;

    private double[] histPosX = new double[HISTORY_SIZE];

    private double[] histPosZ = new double[HISTORY_SIZE];

    public SmartBodyHelper(MobEntity entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public void updateRenderAngles() {
        for (int i = histPosX.length - 1; i > 0; i--) {
            histPosX[i] = histPosX[i - 1];
            histPosZ[i] = histPosZ[i - 1];
        }
        histPosX[0] = entity.getPosX();
        histPosZ[0] = entity.getPosZ();
        double dx = delta(histPosX);
        double dz = delta(histPosZ);
        double distSq = dx * dx + dz * dz;
        double moveAngle;
        if (distSq > 2.5e-7) {
            moveAngle = (float) MathHelper.atan2(dz, dx) * (180 / (float) Math.PI) - 90;
            entity.renderYawOffset += MathHelper.wrapDegrees(moveAngle - entity.renderYawOffset) * 0.6F;
            if (!entity.getNavigator().noPath()) {
                entity.rotationYaw += MathHelper.wrapDegrees(moveAngle - entity.rotationYaw) * 0.4F;
            }
        } else if (entity.getPassengers().isEmpty() || !(entity.getPassengers().get(0) instanceof MobEntity)) {
            float limit = MAX_ROTATE;
            if (Math.abs(entity.rotationYawHead - targetYawHead) > 15) {
                rotateTime = 0;
                targetYawHead = entity.rotationYawHead;
            } else {
                rotateTime++;
                final int speed = 10;
                if (rotateTime > speed) {
                    limit = Math.max(1 - (rotateTime - speed) / (float) speed, 0) * MAX_ROTATE;
                }
            }
            entity.renderYawOffset = approach(entity.rotationYawHead, entity.renderYawOffset, limit);
            this.entity.rotationYawHead = MathHelper.func_219800_b(this.entity.rotationYawHead, this.entity.renderYawOffset, (float)this.entity.getHorizontalFaceSpeed());
        }
    }

    private double delta(double[] arr) {
        return mean(arr, 0) - mean(arr, HISTORY_SIZE / 2);
    }

    private double mean(double[] arr, int start) {
        double mean = 0;
        for (int i = 0; i < HISTORY_SIZE / 2; i++) {
            mean += arr[i + start];
        }
        return mean / arr.length;
    }

    private static float approach(float target, float current, float limit) {
        float delta = MathHelper.wrapDegrees(current - target);
        if (delta < -limit) {
            delta = -limit;
        } else if (delta >= limit) {
            delta = limit;
        }
        return target + delta * 0.55F;
    }
}