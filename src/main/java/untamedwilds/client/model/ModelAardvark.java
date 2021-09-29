package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import untamedwilds.entity.mammal.EntityAardvark;

public class ModelAardvark extends AdvancedEntityModel<EntityAardvark> {

    public AdvancedModelBox body_main;
    public AdvancedModelBox head_neck;
    public AdvancedModelBox arm_left_1;
    public AdvancedModelBox body_booty;
    public AdvancedModelBox arm_right_1;
    public AdvancedModelBox hair;
    public AdvancedModelBox head_head;
    public AdvancedModelBox head_ear_left;
    public AdvancedModelBox head_ear_right;
    public AdvancedModelBox head_snout;
    public AdvancedModelBox eye_left;
    public AdvancedModelBox eye_right;
    public AdvancedModelBox arm_left_2;
    public AdvancedModelBox leg_left_1;
    public AdvancedModelBox body_tail_1;
    public AdvancedModelBox leg_right_1;
    public AdvancedModelBox leg_left_2;
    public AdvancedModelBox body_tail_2;
    public AdvancedModelBox body_tail_3;
    public AdvancedModelBox leg_right_2;
    public AdvancedModelBox arm_right_2;

    private final ModelAnimator animator;

    public ModelAardvark() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leg_right_1 = new AdvancedModelBox(this, 46, 15);
        this.leg_right_1.mirror = true;
        this.leg_right_1.setRotationPoint(0.0F, -0.2F, 5.6F);
        this.leg_right_1.addBox(-4.0F, -1.0F, -2.0F, 4, 5, 4, 0.0F);
        this.setRotateAngle(leg_right_1, -0.136659280431156F, 0.0F, 0.13962634015954636F);
        this.body_tail_2 = new AdvancedModelBox(this, 14, 17);
        this.body_tail_2.setRotationPoint(0.0F, 0.0F, 3.5F);
        this.body_tail_2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(body_tail_2, -0.22759093446006054F, 0.0F, 0.0F);
        this.body_booty = new AdvancedModelBox(this, 28, 0);
        this.body_booty.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.body_booty.addBox(-3.0F, -4.0F, 0.0F, 6, 6, 8, 0.0F);
        this.setRotateAngle(body_booty, -0.4553564018453205F, 0.0F, 0.0F);
        this.body_tail_3 = new AdvancedModelBox(this, 14, 17);
        this.body_tail_3.setRotationPoint(0.0F, 0.4F, 5.3F);
        this.body_tail_3.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(body_tail_3, 0.8651597102135892F, 0.0F, 0.0F);
        this.body_tail_3.scaleX = 0.7F;
        this.leg_left_1 = new AdvancedModelBox(this, 46, 15);
        this.leg_left_1.setRotationPoint(0.0F, -0.2F, 5.6F);
        this.leg_left_1.addBox(0.0F, -1.0F, -2.0F, 4, 5, 4, 0.0F);
        this.setRotateAngle(leg_left_1, -0.136659280431156F, 0.0F, -0.13962634015954636F);
        this.leg_right_2 = new AdvancedModelBox(this, 46, 24);
        this.leg_right_2.mirror = true;
        this.leg_right_2.setRotationPoint(-2.5F, 3.1F, 1.2F);
        this.leg_right_2.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(leg_right_2, 0.22759093446006054F, 0.0F, -0.13962634015954636F);
        this.head_ear_left = new AdvancedModelBox(this, 0, 21);
        this.head_ear_left.setRotationPoint(1.0F, -1.0F, -1.0F);
        this.head_ear_left.addBox(-1.0F, -4.0F, 0.0F, 2, 4, 1, 0.0F);
        this.setRotateAngle(head_ear_left, -0.091106186954104F, -0.091106186954104F, 0.8651597102135892F);
        this.arm_right_2 = new AdvancedModelBox(this, 30, 23);
        this.arm_right_2.mirror = true;
        this.arm_right_2.setRotationPoint(0.0F, 3.5F, 0.01F);
        this.arm_right_2.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(arm_right_2, 0.0F, 0.0F, -0.136659280431156F);
        this.head_head = new AdvancedModelBox(this, 2, 22);
        this.head_head.setRotationPoint(0.0F, -1.0F, -2.5F);
        this.head_head.addBox(-1.5F, -2.0F, -4.0F, 3, 3, 4, 0.0F);
        this.setRotateAngle(head_head, 0.8196066167365371F, 0.0F, 0.0F);
        this.body_tail_1 = new AdvancedModelBox(this, 0, 13);
        this.body_tail_1.setRotationPoint(0.0F, -1.0F, 7.0F);
        this.body_tail_1.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(body_tail_1, -0.5462880558742251F, 0.0F, 0.0F);
        this.hair = new AdvancedModelBox(this, 34, 18);
        this.hair.setRotationPoint(0.0F, 2.2F, 4.0F);
        this.hair.addBox(0.0F, 0.0F, -6.0F, 0, 2, 12, 0.0F);
        this.setRotateAngle(hair, -0.31869712141416456F, 0.0F, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 16, 26);
        this.head_snout.setRotationPoint(0.0F, -1.0F, -4.0F);
        this.head_snout.addBox(-1.5F, -1.0F, -4.0F, 3, 2, 4, 0.0F);
        this.arm_right_1 = new AdvancedModelBox(this, 28, 15);
        this.arm_right_1.mirror = true;
        this.arm_right_1.setRotationPoint(-1.5F, -0.75F, 0.2F);
        this.arm_right_1.addBox(-1.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F);
        this.setRotateAngle(arm_right_1, -0.36425021489121656F, 0.0F, 0.136659280431156F);
        this.eye_right = new AdvancedModelBox(this, 0, 27);
        this.eye_right.mirror = true;
        this.eye_right.setRotationPoint(-1.51F, -1.0F, -3.0F);
        this.eye_right.addBox(0.0F, -1.0F, -1.0F, 0, 1, 2, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 0, 27);
        this.eye_left.setRotationPoint(1.51F, -1.0F, -3.0F);
        this.eye_left.addBox(0.0F, -1.0F, -1.0F, 0, 1, 2, 0.0F);
        this.head_neck = new AdvancedModelBox(this, 20, 0);
        this.head_neck.setRotationPoint(0.0F, -1.0F, -1.0F);
        this.head_neck.addBox(-1.5F, -2.0F, -4.0F, 3, 3, 4, 0.0F);
        this.setRotateAngle(head_neck, -0.5462880558742251F, 0.0F, 0.0F);
        this.head_neck.scaleX = 0.99F;
        this.arm_left_2 = new AdvancedModelBox(this, 30, 23);
        this.arm_left_2.setRotationPoint(0.0F, 3.5F, 0.01F);
        this.arm_left_2.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(arm_left_2, 0.0F, 0.0F, 0.136659280431156F);
        this.leg_left_2 = new AdvancedModelBox(this, 46, 24);
        this.leg_left_2.setRotationPoint(2.5F, 3.1F, 1.2F);
        this.leg_left_2.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(leg_left_2, 0.22759093446006054F, 0.0F, 0.13962634015954636F);
        this.arm_left_1 = new AdvancedModelBox(this, 28, 15);
        this.arm_left_1.setRotationPoint(1.5F, -0.75F, 0.2F);
        this.arm_left_1.addBox(-1.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F);
        this.setRotateAngle(arm_left_1, -0.36425021489121656F, 0.0F, -0.136659280431156F);
        this.head_ear_right = new AdvancedModelBox(this, 0, 21);
        this.head_ear_right.mirror = true;
        this.head_ear_right.setRotationPoint(-1.0F, -1.0F, -1.0F);
        this.head_ear_right.addBox(-1.0F, -4.0F, 0.0F, 2, 4, 1, 0.0F);
        this.setRotateAngle(head_ear_right, -0.091106186954104F, 0.091106186954104F, -0.8651597102135892F);
        this.body_main = new AdvancedModelBox(this, 0, 1);
        this.body_main.setRotationPoint(0.0F, 17.6F, -5.0F);
        this.body_main.addBox(-2.5F, -3.3F, -2.0F, 5, 5, 6, 0.0F);
        this.setRotateAngle(body_main, 0.36425021489121656F, 0.0F, 0.0F);
        this.body_booty.addChild(this.leg_right_1);
        this.body_tail_1.addChild(this.body_tail_2);
        this.head_head.addChild(this.eye_right);
        this.body_main.addChild(this.body_booty);
        this.body_tail_2.addChild(this.body_tail_3);
        this.body_booty.addChild(this.leg_left_1);
        this.leg_right_1.addChild(this.leg_right_2);
        this.head_head.addChild(this.head_ear_left);
        this.arm_right_1.addChild(this.arm_right_2);
        this.head_neck.addChild(this.head_head);
        this.body_booty.addChild(this.body_tail_1);
        this.body_main.addChild(this.hair);
        this.head_head.addChild(this.head_snout);
        this.body_main.addChild(this.arm_right_1);
        this.head_head.addChild(this.eye_left);
        this.body_main.addChild(this.head_neck);
        this.arm_left_1.addChild(this.arm_left_2);
        this.leg_left_1.addChild(this.leg_left_2);
        this.body_main.addChild(this.arm_left_1);
        this.head_head.addChild(this.head_ear_right);
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
            body_main,
            head_neck,
            arm_left_1,
            body_booty,
            arm_right_1,
            hair,
            head_head,
            head_ear_left,
            head_ear_right,
            head_snout,
            eye_left,
            eye_right,
            arm_left_2,
            leg_left_1,
            body_tail_1,
            leg_right_1,
            leg_left_2,
            body_tail_2,
            body_tail_3,
            leg_right_2,
            arm_right_2
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        animator.update(entityIn);

        animator.setAnimation(EntityAardvark.WORK_DIG);
        animator.startKeyframe(6);
        animator.move(body_main, 0, 0, 1.5F);
        this.rotate(animator, body_main, 31.30F, 0, 0);
        animator.move(body_booty, 0, -0.2F, -0.3F);
        this.rotate(animator, body_booty, -39.13F, 0, 0);
        this.rotate(animator, head_neck, 7.83F, 0, 0);
        this.rotate(animator, arm_left_1, -46.96F, -10.43F, -18.26F);
        animator.endKeyframe();
        for (int i = 0; i < 10; i++) {
            AdvancedModelBox limb = i % 2 == 0 ? arm_right_1 : arm_left_1;
            animator.startKeyframe(6);
            animator.move(body_main, 0, 0, 1.5F);
            this.rotate(animator, body_main, 31.30F, 0, 0);
            animator.move(body_booty, 0, -0.2F, -0.3F);
            this.rotate(animator, body_booty, -39.13F, 0, 0);
            this.rotate(animator, head_neck, 7.83F, 0, 0);
            this.rotate(animator, limb, -46.96F, -10.43F, -18.26F); // Forwards
            animator.endKeyframe();
        }
        animator.resetKeyframe(10);

        animator.setAnimation(EntityAardvark.ATTACK);
        animator.startKeyframe(6);
        animator.move(body_main, 0, -4.6F, 0);
        this.rotate(animator, body_main, -26.09F, 0, 0);
        this.rotate(animator, body_booty, -33.91F, 0, 0);
        animator.move(leg_right_1, 0, -1, -1);
        this.rotate(animator, leg_right_1, 46.96F, 0, 0);
        animator.move(leg_left_1, 0, -1, -1);
        this.rotate(animator, leg_left_1, 46.96F, 0, 0);
        animator.move(body_tail_1, 0, -1, -2);
        this.rotate(animator, body_tail_1, 23.48F, 0, 0);

        this.rotate(animator, arm_left_1, -46.96F, -10.43F, -18.26F);
        animator.endKeyframe();
        for (int i = 0; i < 3; i++) {
            AdvancedModelBox limb = i % 2 == 0 ? arm_right_1 : arm_left_1;
            animator.startKeyframe(3);
            animator.move(body_main, 0, -4.6F, 0);
            this.rotate(animator, body_main, -26.09F, 0, 0);
            this.rotate(animator, body_booty, -33.91F, 0, 0);
            animator.move(leg_right_1, 0, -1, -1);
            this.rotate(animator, leg_right_1, 46.96F, 0, 0);
            animator.move(leg_left_1, 0, -1, -1);
            this.rotate(animator, leg_left_1, 46.96F, 0, 0);
            animator.move(body_tail_1, 0, -1, -2);
            this.rotate(animator, body_tail_1, 23.48F, 0, 0);
            this.rotate(animator, limb, -46.96F, -10.43F, -18.26F); // Forwards
            animator.endKeyframe();
        }
        animator.resetKeyframe(3);
    }

    public void setRotationAngles(EntityAardvark aardvark, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(aardvark);
        float globalSpeed = 1f;
        float globalDegree = 1f;
        limbSwingAmount = Math.min(limbSwingAmount, 0.4F);

        // Breathing Animation
        this.body_main.setScale((float) (1.0F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1.0F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.body_booty.setScale((float) (1.0F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1.0F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(body_main, 0.4F * globalSpeed, 0.1F, false, ageInTicks / 20, 2);
        bob(arm_right_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(arm_left_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_right_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_left_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        walk(head_neck, 0.4f * globalSpeed, 0.03f, false, 2.4F, 0.08F, ageInTicks / 20, 2);
        walk(head_head, 0.4f * globalSpeed, 0.03f, false, 2.8F, 0.06F, ageInTicks / 20, 2);
        this.head_snout.setScale((float) (1.0F + Math.sin(ageInTicks / 6) * 0.08F + Math.sin(ageInTicks / 2) * 0.1F), (float) (1.0F + Math.sin(ageInTicks / 8) * 0.04F), 1.0F);

        // Blinking Animation
        if (!aardvark.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-0.51F, -1.0F, -3.0F);
            this.eye_left.setRotationPoint(0.51F, -1.0F, -3.0F);
        }

        // Head Tracking Animation
        if (!aardvark.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 3, head_neck);
            this.faceTarget(netHeadYaw, headPitch, 3, head_head);
        }

        // Pitch/Yaw handler
        if (aardvark.isInWater() && !aardvark.isOnGround()) {
            float pitch = MathHelper.clamp(aardvark.rotationPitch, -20F, 20.0F) - 10;
            this.setRotateAngle(body_main, (float) (pitch * Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        if (aardvark.canMove()) {
            if (aardvark.getCurrentSpeed() > 0.1f || aardvark.isAngry()) { // Running animation
                bob(body_main, 0.5F * globalSpeed, 0.5F, false, limbSwing, limbSwingAmount);
                walk(body_main, 0.5f * globalSpeed, 0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                walk(head_neck, 0.5f * globalSpeed, -0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                walk(body_booty, 0.5f * globalSpeed, 0.3f * globalDegree, false, 0.5F, 0f, limbSwing, limbSwingAmount);

                bob(arm_right_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_1, 0.5f * globalSpeed, 1f * globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.2F, 0.2f, limbSwing, limbSwingAmount);
                bob(arm_left_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_1, 0.5f * globalSpeed, 1f * globalDegree, true, 0.6F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.8F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_right_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_1, 0.5f * globalSpeed, 1f * globalDegree, true, 1.4F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 1.6F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_left_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_1, 0.5f * globalSpeed, 1f * globalDegree, true, 2F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.2F, 0.2f, limbSwing, limbSwingAmount);
            } else { // Walking Animation
                bob(arm_right_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_1, 0.5f * globalSpeed, globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.2F, 0.2f, limbSwing, limbSwingAmount);
                bob(arm_left_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_1, 0.5f * globalSpeed, globalDegree, true, 2.4F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.6F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_right_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_1, 0.5f * globalSpeed, globalDegree, true, 1F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 1.2F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_left_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_1, 0.5f * globalSpeed, globalDegree, true, 3.4F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 3.6F, 0.2f, limbSwing, limbSwingAmount);
            }
        }

        // Sleeping Animation
        if (aardvark.sleepProgress > 0) {
            this.progressPosition(body_main, aardvark.sleepProgress, -3.0F, 21F, -5.0F, 40);
            this.progressRotation(body_main, aardvark.sleepProgress, (float)Math.toRadians(20.87F), 0.0F, (float)Math.toRadians(-91.30), 40);
            this.progressRotation(head_neck, aardvark.sleepProgress, (float)Math.toRadians(26.09), (float)Math.toRadians(15.65), 0, 40);
            this.progressRotation(arm_left_1, aardvark.sleepProgress, (float)Math.toRadians(5.22), 0, (float)Math.toRadians(23.48), 40);
            this.progressRotation(leg_left_1, aardvark.sleepProgress, (float)Math.toRadians(10.43), 0, (float)Math.toRadians(36.52), 40);
            this.progressRotation(body_tail_1, aardvark.sleepProgress, (float)Math.toRadians(-31.30), 0, (float)Math.toRadians(23.48), 40);
            this.progressRotation(body_tail_2, aardvark.sleepProgress, (float)Math.toRadians(-39.13), 0, (float)Math.toRadians(-10.43), 40);
            this.progressRotation(body_tail_3, aardvark.sleepProgress, (float)Math.toRadians(-52.17), 0, (float)Math.toRadians(23.48), 40);
        }
        else {
            flap(body_tail_1, 0.4f * globalSpeed, 0.2f * globalDegree, true, 0F, 0f, ageInTicks / 6, 2);
            flap(body_tail_2, 0.4f * globalSpeed, 0.2f * globalDegree, true, 0.5F, 0f, ageInTicks / 6, 2);
            flap(body_tail_3, 0.4f * globalSpeed, 0.2f * globalDegree, true, 1.0F, 0f, ageInTicks / 6, 2);
        }
    }
}
