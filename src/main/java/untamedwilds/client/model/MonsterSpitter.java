package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import untamedwilds.entity.relict.EntitySpitter;

public class MonsterSpitter extends AdvancedEntityModel<EntitySpitter> {
    
    public AdvancedModelBox body_main;
    public AdvancedModelBox body_abdomen;
    public AdvancedModelBox arm_right_upper;
    public AdvancedModelBox arm_left_upper;
    public AdvancedModelBox head_neck;
    public AdvancedModelBox back_sail;
    public AdvancedModelBox leg_right_upper;
    public AdvancedModelBox leg_left_upper;
    public AdvancedModelBox leg_right_lower;
    public AdvancedModelBox arm_right_claw;
    public AdvancedModelBox leg_left_lower;
    public AdvancedModelBox arm_left_claw;
    public AdvancedModelBox arm_right_lower;
    public AdvancedModelBox arm_right_claw_1;
    public AdvancedModelBox arm_left_lower;
    public AdvancedModelBox arm_left_claw_1;
    public AdvancedModelBox head_snout;
    public AdvancedModelBox head_tube;

    private final ModelAnimator animator;

    public MonsterSpitter() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.head_tube = new AdvancedModelBox(this, 0, 47);
        this.head_tube.setRotationPoint(0.0F, -1.5F, -4.0F);
        this.head_tube.addBox(-1.5F, -1.5F, -8.0F, 3, 3, 8, 0.0F);
        this.body_abdomen = new AdvancedModelBox(this, 40, 0);
        this.body_abdomen.setRotationPoint(0.0F, -1.0F, 6.0F);
        this.body_abdomen.addBox(-3.5F, -4.0F, 0.0F, 7, 6, 10, 0.0F);
        this.leg_left_lower = new AdvancedModelBox(this, 58, 46);
        this.leg_left_lower.mirror = true;
        this.leg_left_lower.setRotationPoint(1.0F, 4.0F, 3.0F);
        this.leg_left_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 9, 3, 0.0F);
        this.setRotateAngle(leg_left_lower, -0.136659280431156F, 0.0F, 0.136659280431156F);
        this.arm_right_lower = new AdvancedModelBox(this, 42, 33);
        this.arm_right_lower.setRotationPoint(-1.0F, 7.0F, 0.5F);
        this.arm_right_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.setRotateAngle(arm_right_lower, -0.5009094953223726F, 0.0F, -0.091106186954104F);
        this.leg_right_upper = new AdvancedModelBox(this, 38, 46);
        this.leg_right_upper.setRotationPoint(-3.0F, -0.5F, 6.0F);
        this.leg_right_upper.addBox(-3.0F, -2.0F, -3.0F, 4, 8, 6, 0.0F);
        this.setRotateAngle(leg_right_upper, -0.27314402793711257F, 0.0F, 0.18203784098300857F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 11.5F, -4.5F);
        this.body_main.addBox(-3.5F, -5.0F, -6.0F, 7, 8, 12, 0.0F);
        this.setRotateAngle(body_main, -0.091106186954104F, 0.0F, 0.0F);
        this.arm_left_upper = new AdvancedModelBox(this, 42, 20);
        this.arm_left_upper.mirror = true;
        this.arm_left_upper.setRotationPoint(2.5F, -2.5F, -0.5F);
        this.arm_left_upper.addBox(-1.0F, 0.0F, -2.5F, 4, 8, 5, 0.0F);
        this.setRotateAngle(arm_left_upper, 0.27314402793711257F, -0.0F, -0.091106186954104F);
        this.head_neck = new AdvancedModelBox(this, 0, 22);
        this.head_neck.setRotationPoint(0.0F, -2.0F, -4.0F);
        this.head_neck.addBox(-2.0F, -2.5F, -8.0F, 4, 5, 8, 0.0F);
        this.setRotateAngle(head_neck, -1.7301448875F, 0.0F, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 0, 36);
        this.head_snout.setRotationPoint(0.0F, 0.2F, -7.0F);
        this.head_snout.addBox(-2.5F, -3.0F, -4.0F, 5, 4, 7, 0.0F);
        this.setRotateAngle(head_snout, 1.957735822F, 0.0F, 0.0F);
        this.arm_left_claw = new AdvancedModelBox(this, 54, 40);
        this.arm_left_claw.setRotationPoint(0.0F, 6.8F, -2.0F);
        this.arm_left_claw.addBox(-1.0F, 0.0F, -4.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(arm_left_claw, 1.6845918F, 0.0F, 0.0F);
        this.arm_right_claw = new AdvancedModelBox(this, 54, 40);
        this.arm_right_claw.mirror = true;
        this.arm_right_claw.setRotationPoint(0.0F, 6.8F, -2.0F);
        this.arm_right_claw.addBox(-1.0F, 0.0F, -4.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(arm_right_claw, 1.6845918F, 0.0F, 0.0F);
        this.leg_left_upper = new AdvancedModelBox(this, 38, 46);
        this.leg_left_upper.mirror = true;
        this.leg_left_upper.setRotationPoint(3.0F, -0.5F, 6.0F);
        this.leg_left_upper.addBox(-1.0F, -2.0F, -3.0F, 4, 8, 6, 0.0F);
        this.setRotateAngle(leg_left_upper, -0.27314402793711257F, -0.0F, -0.18203784098300857F);
        this.arm_left_lower = new AdvancedModelBox(this, 42, 33);
        this.arm_left_lower.mirror = true;
        this.arm_left_lower.setRotationPoint(1.0F, 7.0F, 0.5F);
        this.arm_left_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.setRotateAngle(arm_left_lower, -0.5009094953223726F, 0.0F, 0.091106186954104F);
        this.back_sail = new AdvancedModelBox(this, 78, 40);
        this.back_sail.setRotationPoint(0.0F, -4.0F, 11.0F);
        this.back_sail.addBox(-2.5F, -10.0F, -5.0F, 5, 12, 12, 0.0F);
        this.setRotateAngle(back_sail, -0.31869712141416456F, 0.0F, 0.0F);
        this.arm_right_claw_1 = new AdvancedModelBox(this, 54, 34);
        this.arm_right_claw_1.setRotationPoint(0.0F, 6.0F, -2.5F);
        this.arm_right_claw_1.addBox(-1.0F, 0.0F, -4.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(arm_right_claw_1, 1.548107F, 0.0F, 0.0F);
        this.arm_left_claw_1 = new AdvancedModelBox(this, 54, 34);
        this.arm_left_claw_1.mirror = true;
        this.arm_left_claw_1.setRotationPoint(0.0F, 6.0F, -2.5F);
        this.arm_left_claw_1.addBox(-1.0F, 0.0F, -4.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(arm_left_claw_1, 1.548107F, 0.0F, 0.0F);
        this.arm_right_upper = new AdvancedModelBox(this, 42, 20);
        this.arm_right_upper.setRotationPoint(-2.5F, -2.5F, -0.5F);
        this.arm_right_upper.addBox(-3.0F, 0.0F, -2.5F, 4, 8, 5, 0.0F);
        this.setRotateAngle(arm_right_upper, 0.27314402793711257F, 0.0F, 0.091106186954104F);
        this.leg_right_lower = new AdvancedModelBox(this, 58, 46);
        this.leg_right_lower.setRotationPoint(-1.0F, 4.0F, 3.0F);
        this.leg_right_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 9, 3, 0.0F);
        this.setRotateAngle(leg_right_lower, -0.136659280431156F, 0.0F, -0.136659280431156F);
        this.head_snout.addChild(this.head_tube);
        this.body_main.addChild(this.body_abdomen);
        this.leg_left_upper.addChild(this.leg_left_lower);
        this.arm_right_upper.addChild(this.arm_right_lower);
        this.body_abdomen.addChild(this.leg_right_upper);
        this.body_main.addChild(this.arm_left_upper);
        this.body_main.addChild(this.head_neck);
        this.head_neck.addChild(this.head_snout);
        this.leg_left_lower.addChild(this.arm_left_claw);
        this.leg_right_lower.addChild(this.arm_right_claw);
        this.body_abdomen.addChild(this.leg_left_upper);
        this.arm_left_upper.addChild(this.arm_left_lower);
        this.body_main.addChild(this.back_sail);
        this.arm_right_lower.addChild(this.arm_right_claw_1);
        this.arm_left_lower.addChild(this.arm_left_claw_1);
        this.body_main.addChild(this.arm_right_upper);
        this.leg_right_upper.addChild(this.leg_right_lower);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of( body_main, body_abdomen, arm_right_upper, arm_left_upper, head_neck, back_sail,
            leg_right_upper, leg_left_upper, leg_right_lower, arm_right_claw, leg_left_lower, arm_left_claw, arm_right_lower,
            arm_right_claw_1, arm_left_lower, arm_left_claw_1, head_snout, head_tube
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        EntitySpitter spitter = (EntitySpitter) entityIn;
        animator.update(spitter);

        animator.setAnimation(EntitySpitter.ATTACK_MAUL_RIGHT);
        animator.startKeyframe(8);
        animator.move(body_main, 0, -0.7F, 0);
        this.rotate(animator, body_main, -5.22F, -10.43F, 7.83F);
        this.rotate(animator, body_abdomen, -5.22F, 10.43F, -7.83F);
        this.rotate(animator, head_snout, 31.30F, -13.04F, 23.48F);
        animator.move(arm_right_upper, -1, -1, 0);
        this.rotate(animator, arm_right_upper, -78.26F, 33.91F, -10.43F);
        this.rotate(animator, arm_right_lower, -62.61F, -5.22F, -5.22F);
        animator.move(arm_left_upper, 0, 0.9F, 0);
        this.rotate(animator, arm_left_upper, 23.48F, 10.43F, -10.43F);
        this.rotate(animator, leg_right_upper, -2.61F, 0, 2.61F);
        this.rotate(animator, leg_left_upper, -2.61F, 0, -2.61F);
        animator.endKeyframe();
        animator.startKeyframe(6);
        animator.move(body_main, 0, 1.9F, 0);
        this.rotate(animator, body_main, 13.04F, -10.43F, 7.83F);
        this.rotate(animator, body_abdomen, -23.48F, 10.43F, -7.83F);
        this.rotate(animator, head_snout, 39.13F, 7.83F, 0);
        this.rotate(animator, head_neck, -54.78F, 0, 0);
        animator.move(arm_right_upper, 0, 0, -2);
        this.rotate(animator, arm_right_upper, -73.04F, 13.04F, -10.43F);
        this.rotate(animator, arm_right_lower, -13.04F, -5.22F, -5.22F);
        animator.move(arm_left_upper, 0, -1.2F, 0);
        this.rotate(animator, arm_left_upper, 15.65F, 10.43F, -10.43F);
        this.rotate(animator, arm_left_lower, -60, 0, 5.22F);
        this.rotate(animator, leg_right_upper, -2.61F, 0, 2.61F);
        this.rotate(animator, leg_left_upper, -2.61F, 0, -2.61F);
        animator.endKeyframe();
        animator.resetKeyframe(8);

        animator.setAnimation(EntitySpitter.ATTACK_MAUL_LEFT);
        animator.startKeyframe(8);
        animator.move(body_main, 0, -0.7F, 0);
        this.rotate(animator, body_main, -5.22F, 10.43F, -7.83F);
        this.rotate(animator, body_abdomen, -5.22F, -10.43F, 7.83F);
        this.rotate(animator, head_snout, 31.30F, 13.04F, -23.48F);
        animator.move(arm_left_upper, -1, -1, 0);
        this.rotate(animator, arm_left_upper, -78.26F, -33.91F, 10.43F);
        this.rotate(animator, arm_left_lower, -62.61F, 5.22F, 5.22F);
        animator.move(arm_right_upper, 0, 0.9F, 0);
        this.rotate(animator, arm_right_upper, 23.48F, -10.43F, 10.43F);
        this.rotate(animator, leg_left_upper, -2.61F, 0, -2.61F);
        this.rotate(animator, leg_right_upper, -2.61F, 0, 2.61F);
        animator.endKeyframe();
        animator.startKeyframe(6);
        animator.move(body_main, 0, 1.9F, 0);
        this.rotate(animator, body_main, 13.04F, 10.43F, -7.83F);
        this.rotate(animator, body_abdomen, -23.48F, -10.43F, 7.83F);
        this.rotate(animator, head_snout, 39.13F, -7.83F, 0);
        this.rotate(animator, head_neck, -54.78F, 0, 0);
        animator.move(arm_left_upper, 0, 0, -2);
        this.rotate(animator, arm_left_upper, -73.04F, -13.04F, 10.43F);
        this.rotate(animator, arm_left_lower, -13.04F, 5.22F, 5.22F);
        animator.move(arm_right_upper, 0, -1.2F, 0);
        this.rotate(animator, arm_right_upper, 15.65F, -10.43F, 10.43F);
        this.rotate(animator, arm_right_lower, -60, 0, -5.22F);
        this.rotate(animator, leg_left_upper, -2.61F, 0, -2.61F);
        this.rotate(animator, leg_right_upper, -2.61F, 0, 2.61F);
        animator.endKeyframe();
        animator.resetKeyframe(8);

        animator.setAnimation(EntitySpitter.ATTACK_SPIT);
        animator.startKeyframe(6);
        this.rotate(animator, head_neck, -46.96F, 0, 0);
        animator.move(head_snout, 0, 2, 0);
        this.rotate(animator, head_snout, 15.65F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(4);
        this.rotate(animator, head_neck, 46.96F, 0, 0);
        animator.move(head_snout, 0, 2, 0);
        this.rotate(animator, head_snout, -28.70F, 0, 0);
        animator.move(head_snout, 0, 0, 1.5F);
        this.rotate(animator, head_snout, -28.70F, 0, 0);
        this.rotate(animator, head_snout, 36.52F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(6);

        animator.setAnimation(EntitySpitter.IDLE_WATCH);
        animator.startKeyframe(20);
        animator.move(body_main, 0, -6F, 0);
        this.rotate(animator, body_main, -36.52F, 0, 0);
        this.rotate(animator, head_neck, 41.74F, 0, 0);
        animator.move(head_snout, 0, -1F, -2F);
        this.rotate(animator, head_snout, -20F, 0, 46.96F);
        this.rotate(animator, arm_left_upper, 41.74F, 0, -5.22F);
        this.rotate(animator, arm_right_upper, 41.74F, 0, 5.22F);
        this.rotate(animator, leg_left_upper, 41.74F, 0, 0);
        this.rotate(animator, leg_right_upper, 41.74F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(20);
        animator.move(body_main, 0, -6F, 0);
        this.rotate(animator, body_main, -36.52F, 0, 0);
        this.rotate(animator, head_neck, 41.74F, 0, 0);
        animator.move(head_snout, 0, -1F, -2F);
        this.rotate(animator, head_snout, -20F, 0, -54.78F);
        this.rotate(animator, arm_left_upper, 41.74F, 0, -5.22F);
        this.rotate(animator, arm_right_upper, 41.74F, 0, 5.22F);
        this.rotate(animator, leg_left_upper, 41.74F, 0, 0);
        this.rotate(animator, leg_right_upper, 41.74F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(20);
        animator.move(body_main, 0, -6F, 0);
        this.rotate(animator, body_main, -36.52F, 0, 0);
        this.rotate(animator, head_neck, 41.74F, 0, 0);
        animator.move(head_snout, 0, -1F, -2F);
        this.rotate(animator, head_snout, -20F, 0, 39.13F);
        this.rotate(animator, arm_left_upper, 41.74F, 0, -5.22F);
        this.rotate(animator, arm_right_upper, 41.74F, 0, 5.22F);
        this.rotate(animator, leg_left_upper, 41.74F, 0, 0);
        this.rotate(animator, leg_right_upper, 41.74F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(20);

        animator.setAnimation(EntitySpitter.IDLE_TALK);
        animator.startKeyframe(20);
        this.rotate(animator, head_neck, 41.74F, 0, 0);
        //animator.move(head_snout, 0, -1F, -2F);
        this.rotate(animator, head_snout, -60F, 0, 10.43F);
        animator.endKeyframe();
        animator.resetKeyframe(20);

        animator.setAnimation(EntitySpitter.ANIMATION_EAT);
        animator.startKeyframe(20);
        animator.move(body_main, 0, 2F, 1F);
        this.rotate(animator, body_main, 13.05F, 0, 0);
        this.rotate(animator, head_neck, 65.22F, 0, 0);
        this.rotate(animator, leg_left_upper, -20.87F, 0, 0);
        this.rotate(animator, leg_right_upper, -20.87F, 0, 0);
        animator.move(arm_left_upper, 0, -1F, 0);
        this.rotate(animator, arm_left_upper, -13.04F, 0, -10.43F);
        animator.move(arm_right_upper, 0, -1F, 0);
        this.rotate(animator, arm_right_upper, -13.04F, 0, 10.43F);
        animator.endKeyframe();
        animator.startKeyframe(20);
        animator.move(body_main, 0, 2F, 1F);
        this.rotate(animator, body_main, 13.05F, 0, 0);
        this.rotate(animator, head_neck, 65.22F, 20.87F, 10.43F);
        this.rotate(animator, leg_left_upper, -20.87F, 0, 0);
        this.rotate(animator, leg_right_upper, -20.87F, 0, 0);
        animator.move(arm_left_upper, 0, -1F, 0);
        this.rotate(animator, arm_left_upper, -13.04F, 0, -10.43F);
        animator.move(arm_right_upper, 0, -1F, 0);
        this.rotate(animator, arm_right_upper, -13.04F, 0, 10.43F);
        animator.endKeyframe();
        animator.startKeyframe(20);
        animator.move(body_main, 0, 2F, 1F);
        this.rotate(animator, body_main, 13.05F, 0, 0);
        this.rotate(animator, head_neck, 65.22F, -20.87F, -10.43F);
        this.rotate(animator, leg_left_upper, -20.87F, 0, 0);
        this.rotate(animator, leg_right_upper, -20.87F, 0, 0);
        animator.move(arm_left_upper, 0, -1F, 0);
        this.rotate(animator, arm_left_upper, -13.04F, 0, -10.43F);
        animator.move(arm_right_upper, 0, -1F, 0);
        this.rotate(animator, arm_right_upper, -13.04F, 0, 10.43F);
        animator.endKeyframe();
        animator.resetKeyframe(20);
    }

    public void setupAnim(EntitySpitter spitter, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(spitter);
        float globalSpeed = 2.4f;
        float globalDegree = 1f;
        limbSwingAmount = Math.min(0.6F, limbSwingAmount * 2);
        limbSwing *= 0.5F;

        // Model Parameters

        // Breathing Animation
        final double scaleX = Math.sin(ageInTicks / 20F);
        final double scaleY = Math.sin(ageInTicks / 16);
        this.body_main.setScale((float) (1F + scaleX * 0.08F), (float) (1F + scaleY * 0.06F), 1.0F);
        this.body_abdomen.setScale((float) (1F + scaleX * 0.06F), (float) (1F + scaleY * 0.06F), 1.0F);
        bob(body_main, 0.4F * globalSpeed, 0.03F, false, ageInTicks / 20, 2);
        bob(head_neck, 0.4F * globalSpeed, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right_upper, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left_upper, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right_upper, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left_upper, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);

        // Blinking Animation

        // Head Tracking Animation
        if (!spitter.isSleeping() && spitter.getAnimation() != EntitySpitter.IDLE_WATCH) {
            this.faceTarget(netHeadYaw, headPitch, 2, head_neck);
            this.faceTarget(netHeadYaw, headPitch, 2, head_snout);
        }

        // Pitch/Yaw handler
        if (spitter.isInWater() && !spitter.isOnGround()) {
            limbSwing = ageInTicks / 3;
            limbSwingAmount = 0.5f;
            this.body_main.rotationPointY += 4; // Model offset to make the Big Cat "sink" in water (while not drowning)
            this.setRotateAngle(head_neck, -0.18203784098300857F, 0.0F, 0.0F);
            float pitch = Mth.clamp(spitter.getXRot() - 10, -25F, 25.0F);
            this.setRotateAngle(body_main, (float) (pitch * Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        if (spitter.canMove() && spitter.getAnimation() != EntitySpitter.IDLE_WATCH) {
            if (spitter.getCurrentSpeed() > 0.1f || spitter.isAngry()) { // Running animation
                bob(body_main, 0.5F * globalSpeed, 0.5F, false, limbSwing, limbSwingAmount);
                walk(body_main, 0.5f * globalSpeed, 0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                walk(head_neck, 0.5f * globalSpeed, -0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                walk(body_abdomen, 0.5f * globalSpeed, 0.3f * globalDegree, false, 0.5F, 0f, limbSwing, limbSwingAmount);
                bob(arm_right_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_upper, 0.5f * globalSpeed, globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.2F, 0.2f, limbSwing, limbSwingAmount);
                bob(arm_left_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_upper, 0.5f * globalSpeed, globalDegree, true, 0.6F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.8F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_right_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_upper, 0.5f * globalSpeed, globalDegree, true, 1.4F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 1.6F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_left_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_upper, 0.5f * globalSpeed, globalDegree, true, 2F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.2F, 0.2f, limbSwing, limbSwingAmount);
            } else { // Walking Animation
                bob(arm_right_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_upper, 0.5f * globalSpeed, globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.2F, 0.2f, limbSwing, limbSwingAmount);
                bob(arm_left_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_upper, 0.5f * globalSpeed, globalDegree, true, 2.4F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.6F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_right_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_upper, 0.5f * globalSpeed, globalDegree, true, 1F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 1.2F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_left_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_upper, 0.5f * globalSpeed, globalDegree, true, 3.4F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 3.6F, 0.2f, limbSwing, limbSwingAmount);
            }
        }

        // Sitting Animation
        if (spitter.sitProgress > 0) {
            this.progressPosition(body_main, spitter.sitProgress, 0.0F, 19.5F, 1.0F, 40);
            this.progressPosition(leg_left_upper, spitter.sitProgress, 2.0F, -2.0F, 6F, 40);
            this.progressPosition(leg_right_upper, spitter.sitProgress, -2.0F, -2.0F, 6F, 40);
            //this.progressRotation(head_neck, spitter.sitProgress, -0.5462880558742251F, 0.0F, 0.0F, 40);
            this.progressRotation(leg_left_upper, spitter.sitProgress, -0.27314402793711257F, -0.0F, -0.045553093477052F, 40);
            this.progressRotation(leg_left_lower, spitter.sitProgress, -1.2292353921796064F, -0.22759093446006054F, 0.045553093477052F, 40);
            this.progressRotation(arm_left_upper, spitter.sitProgress, 0.27314402793711207F, -6.200655107570901E-17F, -0.24361070658773F, 40);
            this.progressRotation(arm_left_lower, spitter.sitProgress, -1.8668041679331349F, 0.0F, 0.10803588069844901F, 40);
            this.progressRotation(leg_right_upper, spitter.sitProgress, -0.27314402793711257F, -0.0F, 0.045553093477052F, 40);
            this.progressRotation(leg_right_lower, spitter.sitProgress, -1.2292353921796064F, 0.22759093446006054F, -0.045553093477052F, 40);
            this.progressRotation(arm_right_upper, spitter.sitProgress, 0.27314402793711207F, 6.200655107570901E-17F, 0.24361070658773F, 40);
            this.progressRotation(arm_right_lower, spitter.sitProgress, -1.8668041679331349F, 0.0F, -0.10803588069844901F, 40);
        }

        // Sleeping Animation
        if (spitter.sleepProgress > 0) {
            this.progressPosition(body_main, spitter.sleepProgress, -4.0F, 20F, -4.0F, 40);
            this.progressRotation(body_main, spitter.sleepProgress, 0, 0.0F, -1.50255395F, 40);
            this.progressRotation(body_abdomen, spitter.sleepProgress, -0.455356402F, -0.0F, -0, 40);
            this.progressRotation(leg_right_upper, spitter.sleepProgress, -0.500909495F , -0.0F, 0.045553093477052F, 40);
            this.progressRotation(leg_left_lower, spitter.sleepProgress, -0.13665928F, 0, 0.77405352F, 40);
            //this.progressRotation(head_neck, spitter.sleepProgress, 0.27314402793711207F, 0.22759093446006054F, 0.0F, 40);
            this.progressRotation(arm_right_upper, spitter.sleepProgress, -0.27314402793711207F, 0, 0.09110619F, 40);
            this.progressRotation(arm_left_lower, spitter.sleepProgress, -0.5009095F, -0.09110619F, 1.0472F, 40);
        }
    }
}
