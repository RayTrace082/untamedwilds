package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import untamedwilds.entity.reptile.EntityMonitor;

public class ModelMonitor extends AdvancedEntityModel<EntityMonitor> {
    
    private final AdvancedModelBox main_body;
    private final AdvancedModelBox head_neck;
    private final AdvancedModelBox arm_left;
    private final AdvancedModelBox leg_left;
    private final AdvancedModelBox tail_1;
    private final AdvancedModelBox arm_right;
    private final AdvancedModelBox leg_right;
    private final AdvancedModelBox head_face;
    private final AdvancedModelBox head_snout;
    private final AdvancedModelBox head_jaw;
    private final AdvancedModelBox head_tongue;
    private final AdvancedModelBox arm_left_2;
    private final AdvancedModelBox arm_left_hand;
    private final AdvancedModelBox leg_left_2;
    private final AdvancedModelBox leg_left_feet;
    private final AdvancedModelBox tail_2;
    private final AdvancedModelBox arm_right_2;
    private final AdvancedModelBox arm_right_hand;
    private final AdvancedModelBox leg_right_2;
    private final AdvancedModelBox leg_right_feet;

    private final ModelAnimator animator;

    public ModelMonitor() {
        this.texWidth = 64;
        this.texHeight = 32;

        this.main_body = new AdvancedModelBox(this, 0, 0);
        this.main_body.setRotationPoint(0.0F, 20.2F, 0.0F);
        this.main_body.addBox(-3.0F, -3.0F, -6.0F, 6, 5, 12, 0.0F);
        this.leg_right_2 = new AdvancedModelBox(this, 24, 6);
        this.leg_right_2.setRotationPoint(0.1F, 3.0F, 0.0F);
        this.leg_right_2.addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3, 0.0F);
        this.setRotateAngle(leg_right_2, 0.40980330836826856F, 0.045553093477052F, -0.36425021489121656F);
        this.tail_1 = new AdvancedModelBox(this, 36, 0);
        this.tail_1.setRotationPoint(0.0F, -0.9F, 4.0F);
        this.tail_1.addBox(-1.5F, -2.0F, 0.0F, 3, 4, 10, 0.0F);
        this.setRotateAngle(tail_1, -0.31869712141416456F, 0.0F, 0.0F);
        this.head_face = new AdvancedModelBox(this, 18, 17);
        this.head_face.setRotationPoint(0.0F, -0.9F, -3.4F);
        this.head_face.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        this.setRotateAngle(head_face, 0.22759093446006054F, 0.0F, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 14, 24);
        this.head_snout.setRotationPoint(0.0F, -1.0F, -3.0F);
        this.head_snout.addBox(-1.5F, -1.0F, -4.0F, 3, 3, 4, 0.0F);
        this.leg_right = new AdvancedModelBox(this, 24, 0);
        this.leg_right.setRotationPoint(-3.0F, -1.9F, 4.8F);
        this.leg_right.addBox(-1.0F, 0.0F, -1.5F, 2, 4, 3, 0.0F);
        this.setRotateAngle(leg_right, -0.40980330836826856F, 0.045553093477052F, 0.36425021489121656F);
        this.leg_right_feet = new AdvancedModelBox(this, 33, 0);
        this.leg_right_feet.mirror = true;
        this.leg_right_feet.setRotationPoint(0.5F, 3.01F, -1.5F);
        this.leg_right_feet.addBox(-2.0F, 0.0F, -2.5F, 4, 0, 5, 0.0F);
        this.head_neck = new AdvancedModelBox(this, 0, 17);
        this.head_neck.setRotationPoint(0.0F, -0.30000000000000004F, -5.5F);
        this.head_neck.addBox(-2.5F, -2.5F, -4.0F, 5, 5, 4, 0.0F);
        this.setRotateAngle(head_neck, -0.22759093446006068F, 0.013037238685168382F, 0.0F);
        this.arm_left_hand = new AdvancedModelBox(this, 33, 0);
        this.arm_left_hand.setRotationPoint(-0.5F, 3.01F, -1.1F);
        this.arm_left_hand.addBox(-2.0F, 0.0F, -2.5F, 4, 0, 5, 0.0F);
        this.leg_left_feet = new AdvancedModelBox(this, 33, 0);
        this.leg_left_feet.setRotationPoint(-0.5F, 3.01F, -1.5F);
        this.leg_left_feet.addBox(-2.0F, 0.0F, -2.5F, 4, 0, 5, 0.0F);
        this.arm_right_2 = new AdvancedModelBox(this, 0, 6);
        this.arm_right_2.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.arm_right_2.addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3, 0.0F);
        this.setRotateAngle(arm_right_2, 0.0F, -0.045553093477052F, -0.5462880558742251F);
        this.leg_left_2 = new AdvancedModelBox(this, 24, 6);
        this.leg_left_2.mirror = true;
        this.leg_left_2.setRotationPoint(-0.1F, 3.0F, 0.0F);
        this.leg_left_2.addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3, 0.0F);
        this.setRotateAngle(leg_left_2, 0.40980330836826856F, -0.045553093477052F, 0.36425021489121656F);
        this.arm_right_hand = new AdvancedModelBox(this, 33, 0);
        this.arm_right_hand.mirror = true;
        this.arm_right_hand.setRotationPoint(0.5F, 3.01F, -1.1F);
        this.arm_right_hand.addBox(-2.0F, 0.0F, -2.5F, 4, 0, 5, 0.0F);
        this.leg_left = new AdvancedModelBox(this, 24, 0);
        this.leg_left.mirror = true;
        this.leg_left.setRotationPoint(3.0F, -1.9F, 4.8F);
        this.leg_left.addBox(-1.0F, 0.0F, -1.5F, 2, 4, 3, 0.0F);
        this.setRotateAngle(leg_left, -0.40980330836826867F, -0.045553093477052F, -0.3642502148912164F);
        this.tail_2 = new AdvancedModelBox(this, 40, 14);
        this.tail_2.setRotationPoint(0.0F, 0.5F, 9.0F);
        this.tail_2.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 10, 0.0F);
        this.setRotateAngle(tail_2, 0.22759093446006054F, 0.0F, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 0, 6);
        this.arm_left_2.mirror = true;
        this.arm_left_2.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.arm_left_2.addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3, 0.0F);
        this.setRotateAngle(arm_left_2, 0.0F, 0.04555309347705199F, 0.5462880558742251F);
        this.arm_left = new AdvancedModelBox(this, 0, 0);
        this.arm_left.mirror = true;
        this.arm_left.setRotationPoint(2.8F, -1.0F, -5.2F);
        this.arm_left.addBox(-1.0F, 0.0F, -1.5F, 2, 2, 3, 0.0F);
        this.setRotateAngle(arm_left, 0.0F, 0.045553093477052F, -0.5462880558742247F);
        this.head_jaw = new AdvancedModelBox(this, 0, 26);
        this.head_jaw.setRotationPoint(0.0F, 1.0F, -3.0F);
        this.head_jaw.addBox(-1.5F, 0.0F, -4.0F, 3, 1, 4, 0.0F);
        this.head_tongue = new AdvancedModelBox(this, 22, 26);
        this.head_tongue.setRotationPoint(0.0F, -0.01F, 0.0F);
        this.head_tongue.addBox(-1.5F, 0.0F, -4.0F, 3, 0, 6, 0.0F);
        this.arm_right = new AdvancedModelBox(this, 0, 0);
        this.arm_right.setRotationPoint(-2.8F, -1.0F, -5.2F);
        this.arm_right.addBox(-1.0F, 0.0F, -1.5F, 2, 2, 3, 0.0F);
        this.setRotateAngle(arm_right, 0.0F, -0.045553093477052F, 0.5462880558742251F);
        this.leg_right.addChild(this.leg_right_2);
        this.main_body.addChild(this.tail_1);
        this.head_neck.addChild(this.head_face);
        this.head_face.addChild(this.head_snout);
        this.main_body.addChild(this.leg_right);
        this.leg_right_2.addChild(this.leg_right_feet);
        this.main_body.addChild(this.head_neck);
        this.arm_left_2.addChild(this.arm_left_hand);
        this.leg_left_2.addChild(this.leg_left_feet);
        this.arm_right.addChild(this.arm_right_2);
        this.leg_left.addChild(this.leg_left_2);
        this.arm_right_2.addChild(this.arm_right_hand);
        this.main_body.addChild(this.leg_left);
        this.tail_1.addChild(this.tail_2);
        this.arm_left.addChild(this.arm_left_2);
        this.main_body.addChild(this.arm_left);
        this.head_face.addChild(this.head_jaw);
        this.head_jaw.addChild(this.head_tongue);
        this.main_body.addChild(this.arm_right);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(main_body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(main_body, head_neck, arm_left, leg_left, tail_1, arm_right, leg_right, head_face, head_snout, head_jaw, head_tongue, arm_left_2,
            arm_left_hand, leg_left_2, leg_left_feet, tail_2, arm_right_2, arm_right_hand, leg_right_2, leg_right_feet
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        EntityMonitor monitor = (EntityMonitor) entityIn;
        animator.update(monitor);

        animator.setAnimation(EntityMonitor.ATTACK_THRASH);
        int invert = 1;
        for (int i = 0; i < 4; i++) {
            animator.startKeyframe(6);
            this.rotate(animator, head_neck, 0, 22.73F * invert , 0);
            this.rotate(animator, head_face, -5.21F, 15.65F * invert, 31.30F * invert);
            this.rotate(animator, head_jaw, 20.87F, 0, 0);
            invert = invert * -1;
            animator.endKeyframe();
        }
        animator.resetKeyframe(6);

        animator.setAnimation(EntityMonitor.IDLE_TONGUE);
        animator.startKeyframe(4);
        this.rotate(animator, head_tongue, 26.08F, 36.52F, 0);
        animator.move(head_tongue, 0, 0, -4F);
        animator.endKeyframe();
        animator.startKeyframe(3);
        this.rotate(animator, head_tongue, -26.08F, -36.52F, 0);
        animator.move(head_tongue, 0, 0, -5F);
        animator.endKeyframe();
        animator.startKeyframe(3);
        animator.endKeyframe();
        animator.resetKeyframe(10);
    }

    public void setupAnim(EntityMonitor monitor, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();
        animate(monitor);
        float globalSpeed = 0.8f;
        float globalDegree = 1.0f;
        limbSwingAmount = Math.min(0.6F, limbSwingAmount);
        if (monitor.isNoAi()) {
            limbSwing = ageInTicks / 4;
            limbSwingAmount = 0.5F;
        }

        // Breathing Animation
        this.main_body.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.head_neck.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.head_face.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.tail_1.setScale(1F, (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        //this.head_jaw.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        //this.head_snout.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);

        // Head Tracking Animation
        if (!monitor.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, head_face);
        }

        // Pitch/Yaw handler
        if (monitor.isInWater() && !monitor.isOnGround()) {
            this.setRotateAngle(main_body, monitor.getXRot() * ((float) Math.PI / 180F), 0, 0);
        }
        this.main_body.rotateAngleY = Mth.rotLerp((float) 0.05, this.main_body.rotateAngleY, monitor.offset);
        this.tail_1.rotateAngleY = Mth.rotLerp((float) 0.05, this.tail_1.rotateAngleY, -1F * monitor.offset);
        this.tail_2.rotateAngleY = Mth.rotLerp((float) 0.05, this.tail_2.rotateAngleY, -2F * monitor.offset);

        // Movement Animation
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{head_face, head_neck, main_body, tail_1, tail_2};
        chainSwing(bodyParts, globalSpeed * 1.4F, globalDegree * 1.2F, -4, limbSwing, limbSwingAmount * 0.3F);
        if (monitor.isInWater()) {
            flap(arm_left, globalSpeed, globalDegree, false, 0.8F, 1f, limbSwing, limbSwingAmount);
            flap(leg_left, globalSpeed, globalDegree * 0.8f, false, 1.6F, 1f, limbSwing, limbSwingAmount);
            flap(arm_right, globalSpeed, globalDegree, false, 2.4F, 1f, limbSwing, limbSwingAmount);
            flap(leg_right, globalSpeed, globalDegree * 0.8f, false, 3.2F, 1f, limbSwing, limbSwingAmount);

            flap(main_body, globalSpeed / 2, globalDegree * 1.2f, false, 0, 0.1f, limbSwing / 2, limbSwingAmount);
            swing(main_body, globalSpeed / 2, globalDegree * 1.2f, false, 0.8F, 0.1f, limbSwing / 3, limbSwingAmount);
            chainWave(new AdvancedModelBox[]{head_face, head_neck, main_body}, globalSpeed * 0.8F, globalDegree, -4, limbSwing, limbSwingAmount * 0.2F);
        }
        else {
            walk(arm_left, globalSpeed, globalDegree * 2F, false, -1, 0f, limbSwing, limbSwingAmount);
            flap(arm_left, globalSpeed, globalDegree * 1, false, 1, -1.5f, limbSwing, limbSwingAmount);
            flap(arm_left_2, globalSpeed, globalDegree * 1f, true, -1, 0f, limbSwing, limbSwingAmount);

            walk(arm_right, globalSpeed, globalDegree * 2F, true, 0, 0f, limbSwing, limbSwingAmount);
            flap(arm_right, globalSpeed, globalDegree * 1, false, 2, 1.5f, limbSwing, limbSwingAmount);
            flap(arm_right_2, globalSpeed, globalDegree * 1f, true, 0, 0f, limbSwing, limbSwingAmount);

            walk(leg_left, globalSpeed, globalDegree * 2F, false, 1, 0f, limbSwing, limbSwingAmount);
            flap(leg_left, globalSpeed, globalDegree * 1, false, 3, 0.5f, limbSwing, limbSwingAmount);
            flap(leg_left_2, globalSpeed, globalDegree * 1f, true, 1, 0f, limbSwing, limbSwingAmount);

            walk(leg_right, globalSpeed, globalDegree * 2F, true, 2, 0f, limbSwing, limbSwingAmount);
            flap(leg_right, globalSpeed, globalDegree * 1, false, 4, 0.5f, limbSwing, limbSwingAmount);
            flap(leg_right_2, globalSpeed, globalDegree * 1f, true, 2, 0f, limbSwing, limbSwingAmount);
        }
        if (monitor.getTarget() == null && monitor.getAnimation() != EntityMonitor.ATTACK_THRASH) {
            this.setRotateAngle(head_neck, head_neck.rotateAngleX, monitor.head_movement.getA(), 0.0F);
            this.setRotateAngle(head_face, head_face.rotateAngleX, monitor.head_movement.getB(), 0.0F);
        }

        // Swimming Animation
        if (monitor.swimProgress > 0) {
            this.progressPosition(arm_left, monitor.swimProgress, 2.8F, 1F, -5.2F, 20);
            this.progressRotation(arm_left, monitor.swimProgress, (float) Math.toRadians(93.91F), (float) Math.toRadians(18.26F), (float) Math.toRadians(-15.65F), 20);
            this.progressPosition(arm_left_hand, monitor.swimProgress, 1.1F, 3.01F, -0.5F, 20);
            this.progressRotation(arm_left_hand, monitor.swimProgress, 0, (float) Math.toRadians(-88.7F), (float) Math.toRadians(86.09F), 20);
            this.progressPosition(leg_left, monitor.swimProgress, 3F, 0.1F, 4.8F, 20);
            this.progressRotation(leg_left, monitor.swimProgress, (float) Math.toRadians(83.48F), (float) Math.toRadians(-2.61F), (float) Math.toRadians(-20.87F), 20);
            this.progressRotation(tail_1, monitor.swimProgress, 0, 0, 0, 20);
            this.progressRotation(tail_2, monitor.swimProgress, 0, 0, 0, 20);
            this.progressPosition(arm_right, monitor.swimProgress, -2.8F, 1F, -5.2F, 20);
            this.progressRotation(arm_right, monitor.swimProgress, (float) Math.toRadians(93.91F), (float) Math.toRadians(-18.26F), (float) Math.toRadians(15.65F), 20);
            this.progressPosition(arm_right_hand, monitor.swimProgress, -1.1F, 3.01F, -0.5F, 20);
            this.progressRotation(arm_right_hand, monitor.swimProgress, 0, (float) Math.toRadians(88.7F), (float) Math.toRadians(-86.09F), 20);
            this.progressPosition(leg_right, monitor.swimProgress, -3F, 0.1F, 4.8F, 20);
            this.progressRotation(leg_right, monitor.swimProgress, (float) Math.toRadians(83.48F), (float) Math.toRadians(2.61F), (float) Math.toRadians(20.87F), 20);
        }

        // Sitting Animation
        if (monitor.sitProgress != 0) {
            this.progressPosition(head_neck, monitor.sitProgress, 0, -1.3F, -5.5F, monitor.ticksToSit);
            this.progressRotation(head_neck, monitor.sitProgress, (float) Math.toRadians(-54.78F), (float) Math.toRadians(0.75F), 0, monitor.ticksToSit);
            this.progressRotation(head_face, monitor.sitProgress, (float) Math.toRadians(54.78F), 0, 0, monitor.ticksToSit);
            this.progressPosition(main_body, monitor.sitProgress, 0, 22.4F, 0, monitor.ticksToSit);

            this.progressRotation(arm_left, monitor.sitProgress, (float) Math.toRadians(-31.30F), (float) Math.toRadians(2.61F), (float) Math.toRadians(-54.78F), monitor.ticksToSit);
            this.progressRotation(arm_left_2, monitor.sitProgress, (float) Math.toRadians(-5.22F), (float) Math.toRadians(-5.22F), (float) Math.toRadians(7.83F), monitor.ticksToSit);
            this.progressPosition(arm_left_hand, monitor.sitProgress, -0.5F, 2.51F, -1.1F, monitor.ticksToSit);
            this.progressRotation(arm_left_hand, monitor.sitProgress, (float) Math.toRadians(20.87F), (float) Math.toRadians(-20.87F), (float) Math.toRadians(31.30F), monitor.ticksToSit);
            this.progressRotation(arm_right, monitor.sitProgress, (float) Math.toRadians(-31.30F), (float) Math.toRadians(-2.61F), (float) Math.toRadians(54.78F), monitor.ticksToSit);
            this.progressRotation(arm_right_2, monitor.sitProgress, (float) Math.toRadians(-5.22F), (float) Math.toRadians(5.22F), (float) Math.toRadians(-7.83F), monitor.ticksToSit);
            this.progressRotation(arm_right_hand, monitor.sitProgress, (float) Math.toRadians(20.87F), (float) Math.toRadians(20.87F), (float) Math.toRadians(-31.30F), monitor.ticksToSit);
            this.progressPosition(arm_right_hand, monitor.sitProgress, 0.5F, 2.51F, -1.1F, monitor.ticksToSit);

            this.progressPosition(leg_left, monitor.sitProgress, 3F, -0.9F, 4.8F, monitor.ticksToSit);
            this.progressRotation(leg_left, monitor.sitProgress, (float) Math.toRadians(49.57F), (float) Math.toRadians(-2.61F), (float) Math.toRadians(-75.65F), monitor.ticksToSit);
            this.progressPosition(leg_left_feet, monitor.sitProgress, 0.5F, 3.01F, -1.5F, monitor.ticksToSit);
            this.progressRotation(leg_left_feet, monitor.sitProgress, (float) Math.toRadians(-31.30F), (float) Math.toRadians(-36.52F), (float) Math.toRadians(88.70F), monitor.ticksToSit);
            this.progressPosition(leg_right, monitor.sitProgress, -3F, -0.9F, 4.8F, monitor.ticksToSit);
            this.progressRotation(leg_right, monitor.sitProgress, (float) Math.toRadians(49.57F), (float) Math.toRadians(2.61F), (float) Math.toRadians(75.65F), monitor.ticksToSit);
            this.progressPosition(leg_right_feet, monitor.sitProgress, -0.5F, 3.01F, -1.5F, monitor.ticksToSit);
            this.progressRotation(leg_right_feet, monitor.sitProgress, (float) Math.toRadians(-31.30F), (float) Math.toRadians(36.52F), (float) Math.toRadians(-88.70F), monitor.ticksToSit);

            this.progressRotation(tail_1, monitor.sitProgress, (float) Math.toRadians(-2.61F), tail_1.rotateAngleY, 0, monitor.ticksToSit);
            this.progressRotation(tail_2, monitor.sitProgress, 0, tail_2.rotateAngleY, 0, monitor.ticksToSit);
        }
    }
}
