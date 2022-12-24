package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import untamedwilds.entity.relict.EntitySpitter;

public class MonsterSpitterLarva extends AdvancedEntityModel<EntitySpitter> {

    private final AdvancedModelBox body_main;
    private final AdvancedModelBox back_sail;
    private final AdvancedModelBox head_snout;
    private final AdvancedModelBox arm_left_lower;
    private final AdvancedModelBox arm_right_lower;
    private final AdvancedModelBox leg_left_lower;
    private final AdvancedModelBox leg_right_lower;
    private final AdvancedModelBox head_tube;
    private final AdvancedModelBox arm_left_claw;
    private final AdvancedModelBox arm_right_claw;
    private final AdvancedModelBox leg_left_claw;
    private final AdvancedModelBox leg_right_claw;

    public MonsterSpitterLarva() {
        this.texWidth = 128;
        this.texHeight = 64;

        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 23.0F, 1.0F);
        this.body_main.addBox(-3.5F, -5.0F, -6.0F, 7, 4, 12, 0.0F);
        this.arm_left_claw = new AdvancedModelBox(this, 54, 34);
        this.arm_left_claw.mirror = true;
        this.arm_left_claw.setRotationPoint(0.0F, 3.0F, -2.5F);
        this.arm_left_claw.addBox(-1.0F, 0.0F, -4.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(arm_left_claw, 1.5481070465189704F, 0.0F, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 2, 38);
        this.head_snout.setRotationPoint(0.0F, -3.5F, -6.0F);
        this.head_snout.addBox(-2.5F, -3.0F, -4.0F, 5, 4, 5, 0.0F);
        this.setRotateAngle(head_snout, -0.18203784098300857F, 0.0F, 0.0F);
        this.head_tube = new AdvancedModelBox(this, 2, 49);
        this.head_tube.setRotationPoint(0.0F, -1.5F, -4.0F);
        this.head_tube.addBox(-1.5F, -1.5F, -6.0F, 3, 3, 6, 0.0F);
        this.leg_left_lower = new AdvancedModelBox(this, 58, 46);
        this.leg_left_lower.mirror = true;
        this.leg_left_lower.setRotationPoint(3.0F, -4.0F, 5.0F);
        this.leg_left_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.setRotateAngle(leg_left_lower, -0.136659280431156F, -0.22759093446006054F, 0.10995574287564275F);
        this.arm_right_claw = new AdvancedModelBox(this, 54, 34);
        this.arm_right_claw.mirror = true;
        this.arm_right_claw.setRotationPoint(0.0F, 3.0F, -2.5F);
        this.arm_right_claw.addBox(-1.0F, 0.0F, -4.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(arm_right_claw, 1.5481070465189704F, 0.0F, 0.0F);
        this.leg_right_claw = new AdvancedModelBox(this, 54, 40);
        this.leg_right_claw.setRotationPoint(0.0F, 3.8F, -2.0F);
        this.leg_right_claw.addBox(-1.0F, 0.0F, -4.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(leg_right_claw, 1.6845917940249266F, 0.0F, 0.0F);
        this.leg_right_lower = new AdvancedModelBox(this, 58, 46);
        this.leg_right_lower.mirror = true;
        this.leg_right_lower.setRotationPoint(-3.0F, -4.0F, 5.0F);
        this.leg_right_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.setRotateAngle(leg_right_lower, -0.136659280431156F, 0.22759093446006054F, -0.10995574287564275F);
        this.arm_right_lower = new AdvancedModelBox(this, 42, 33);
        this.arm_right_lower.mirror = true;
        this.arm_right_lower.setRotationPoint(-3.0F, -2.0F, -2.5F);
        this.arm_right_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(arm_right_lower, -0.5009094953223726F, 0.0F, -0.091106186954104F);
        this.arm_left_lower = new AdvancedModelBox(this, 42, 33);
        this.arm_left_lower.mirror = true;
        this.arm_left_lower.setRotationPoint(3.0F, -2.0F, -2.5F);
        this.arm_left_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(arm_left_lower, -0.5009094953223726F, 0.0F, 0.091106186954104F);
        this.back_sail = new AdvancedModelBox(this, 82, 44);
        this.back_sail.setRotationPoint(0.0F, -3.0F, 3.0F);
        this.back_sail.addBox(-2.5F, -5.0F, -4.0F, 5, 5, 8, 0.0F);
        this.setRotateAngle(back_sail, -0.27314402793711257F, 0.0F, 0.0F);
        this.leg_left_claw = new AdvancedModelBox(this, 54, 40);
        this.leg_left_claw.setRotationPoint(0.0F, 3.8F, -2.0F);
        this.leg_left_claw.addBox(-1.0F, 0.0F, -4.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(leg_left_claw, 1.6845917940249266F, 0.0F, 0.0F);
        this.arm_left_lower.addChild(this.arm_left_claw);
        this.body_main.addChild(this.head_snout);
        this.head_snout.addChild(this.head_tube);
        this.body_main.addChild(this.leg_left_lower);
        this.arm_right_lower.addChild(this.arm_right_claw);
        this.leg_right_lower.addChild(this.leg_right_claw);
        this.body_main.addChild(this.leg_right_lower);
        this.body_main.addChild(this.arm_right_lower);
        this.body_main.addChild(this.arm_left_lower);
        this.body_main.addChild(this.back_sail);
        this.leg_left_lower.addChild(this.leg_left_claw);
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of( body_main, back_sail, leg_right_lower, arm_right_claw, leg_left_lower, arm_left_claw,
                arm_right_lower, arm_left_lower, head_snout, head_tube
        );
    }

    public void setupAnim(EntitySpitter spitter, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();

        float globalSpeed = 2.4f;
        float globalDegree = 1f;
        limbSwingAmount = Math.min(0.6F, limbSwingAmount * 2);
        limbSwing *= 0.5F;

        // Model Parameters

        // Breathing Animation
        final double scaleX = Math.sin(ageInTicks / 20F);
        final double scaleY = Math.sin(ageInTicks / 16);
        this.body_main.setScale((float) (1F + scaleX * 0.08F), (float) (1F + scaleY * 0.06F), 1.0F);
        bob(body_main, 0.4F * globalSpeed, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right_lower, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left_lower, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right_lower, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left_lower, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);

        // Blinking Animation

        // Head Tracking Animation
        if (!spitter.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, head_snout);
        }

        // Pitch/Yaw handler
        if (spitter.isInWater() && !spitter.isOnGround()) {
            limbSwing = ageInTicks / 3;
            limbSwingAmount = 0.5f;
            this.body_main.rotationPointY += 4; // Model offset to make the Big Cat "sink" in water (while not drowning)
            float pitch = Mth.clamp(spitter.getXRot() - 10, -25F, 25.0F);
            this.setRotateAngle(body_main, (float) (pitch * Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        if (spitter.canMove()) {
            if (spitter.getCurrentSpeed() > 0.1f || spitter.isAngry()) { // Running animation
                bob(body_main, 0.5F * globalSpeed, 0.5F, false, limbSwing, limbSwingAmount);
                walk(body_main, 0.5f * globalSpeed, 0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                bob(arm_right_lower, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_lower, 0.5f * globalSpeed, globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                bob(arm_left_lower, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_lower, 0.5f * globalSpeed, globalDegree, true, 0.6F, 0f, limbSwing, limbSwingAmount);
                bob(leg_right_lower, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_lower, 0.5f * globalSpeed, globalDegree, true, 1.4F, 0f, limbSwing, limbSwingAmount);
                bob(leg_left_lower, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_lower, 0.5f * globalSpeed, globalDegree, true, 2F, 0f, limbSwing, limbSwingAmount);
            } else { // Walking Animation
                bob(arm_right_lower, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_lower, 0.5f * globalSpeed, globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                bob(arm_left_lower, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_lower, 0.5f * globalSpeed, globalDegree, true, 2.4F, 0f, limbSwing, limbSwingAmount);
                bob(leg_right_lower, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_lower, 0.5f * globalSpeed, globalDegree, true, 1F, 0f, limbSwing, limbSwingAmount);
                bob(leg_left_lower, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_lower, 0.5f * globalSpeed, globalDegree, true, 3.4F, 0f, limbSwing, limbSwingAmount);
            }
        }

        // Sitting Animation
        if (spitter.sitProgress > 0) {
            this.progressPosition(body_main, spitter.sitProgress, 0.0F, 23F, 1.0F, 40);
            this.progressRotation(leg_left_lower, spitter.sitProgress, -1.2292353921796064F, -0.22759093446006054F, 0.045553093477052F, 40);
            this.progressRotation(arm_left_lower, spitter.sitProgress, -1.8668041679331349F, 0.0F, 0.10803588069844901F, 40);
            this.progressRotation(leg_right_lower, spitter.sitProgress, -1.2292353921796064F, 0.22759093446006054F, -0.045553093477052F, 40);
            this.progressRotation(arm_right_lower, spitter.sitProgress, -1.8668041679331349F, 0.0F, -0.10803588069844901F, 40);
        }

        // Sleeping Animation
        if (spitter.sleepProgress > 0) {
            this.progressPosition(body_main, spitter.sleepProgress, -4.0F, 23F, -4.0F, 40);
            this.progressRotation(body_main, spitter.sleepProgress, 0, 0.0F, -1.50255395F, 40);
            this.progressRotation(leg_left_lower, spitter.sleepProgress, -0.13665928F, 0, 0.77405352F, 40);
            this.progressRotation(arm_left_lower, spitter.sleepProgress, -0.5009095F, -0.09110619F, 1.0472F, 40);
        }
    }
}
