package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import untamedwilds.entity.mammal.EntityBoar;

public class ModelBoar extends AdvancedEntityModel<EntityBoar> {

    public AdvancedModelBox main_body;
    public AdvancedModelBox head_main;
    public AdvancedModelBox leg_left_1;
    public AdvancedModelBox arm_left_1;
    public AdvancedModelBox shape14;
    public AdvancedModelBox shape15;
    public AdvancedModelBox arm_right_1;
    public AdvancedModelBox leg_right_1;
    public AdvancedModelBox head_snout;
    public AdvancedModelBox ear_left;
    public AdvancedModelBox head_mouth;
    public AdvancedModelBox ear_right;
    public AdvancedModelBox tusk_left;
    public AdvancedModelBox tusk_left_1;
    public AdvancedModelBox leg_left_2;
    public AdvancedModelBox arm_left_2;
    public AdvancedModelBox arm_right_2;
    public AdvancedModelBox leg_right_2;
    public AdvancedModelBox eye_left;
    public AdvancedModelBox eye_right;

    private final ModelAnimator animator;

    public ModelBoar() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.shape14 = new AdvancedModelBox(this, 6, 0);
        this.shape14.setRotationPoint(0.0F, -5.0F, 6.0F);
        this.shape14.addBox(-1.5F, 0.0F, 0.0F, 3, 7, 0, 0.0F);
        this.setRotateAngle(shape14, 0.18203784098300857F, 0.0F, 0.0F);
        this.leg_right_1 = new AdvancedModelBox(this, 0, 36);
        this.leg_right_1.mirror = true;
        this.leg_right_1.setRotationPoint(-2.5F, -1.7F, 3.6F);
        this.leg_right_1.addBox(-2.0F, -1.0F, -2.0F, 4, 6, 5, 0.0F);
        this.setRotateAngle(leg_right_1, -0.136659280431156F, 0.0F, 0.13962634015954636F);
        this.head_main = new AdvancedModelBox(this, 0, 22);
        this.head_main.setRotationPoint(0.0F, -0.8F, -8.0F);
        this.head_main.addBox(-2.5F, -4.5F, -4.0F, 5, 7, 6, 0.0F);
        this.setRotateAngle(head_main, 0.31869712141416456F, 0.0F, 0.0F);
        this.tusk_left_1 = new AdvancedModelBox(this, 16, 24);
        this.tusk_left_1.setRotationPoint(-1.4F, 0.4F, -1.0F);
        this.tusk_left_1.addBox(0.0F, -2.0F, -1.0F, 0, 2, 1, 0.0F);
        this.setRotateAngle(tusk_left_1, 0.0F, 0.006773207605036283F, -0.36425021489121656F);
        this.tusk_left = new AdvancedModelBox(this, 16, 24);
        this.tusk_left.setRotationPoint(1.4F, 0.4F, -1.0F);
        this.tusk_left.addBox(0.0F, -2.0F, -1.0F, 0, 2, 1, 0.0F);
        this.setRotateAngle(tusk_left, 0.0F, 0.0F, 0.36425021489121656F);
        this.arm_right_2 = new AdvancedModelBox(this, 56, 10);
        this.arm_right_2.mirror = true;
        this.arm_right_2.setRotationPoint(0.0F, 4.5F, 0.01F);
        this.arm_right_2.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(arm_right_2, 0.0F, 0.0F, -0.136659280431156F);
        this.shape15 = new AdvancedModelBox(this, 34, 13);
        this.shape15.setRotationPoint(0.0F, -4.5F, -4.0F);
        this.shape15.addBox(-1.0F, -2.0F, -5.0F, 2, 2, 10, 0.0F);
        this.setRotateAngle(shape15, -0.045553093477052F, 0.0F, 0.0F);
        this.leg_right_2 = new AdvancedModelBox(this, 0, 47);
        this.leg_right_2.mirror = true;
        this.leg_right_2.setRotationPoint(0.4F, 3.7F, 2.0F);
        this.leg_right_2.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(leg_right_2, 0.18203784098300857F, 0.0F, -0.13962634015954636F);
        this.head_snout = new AdvancedModelBox(this, 22, 22);
        this.head_snout.setRotationPoint(0.0F, -0.8F, -3.0F);
        this.head_snout.addBox(-1.5F, -1.5F, -4.0F, 3, 3, 5, 0.0F);
        this.setRotateAngle(head_snout, 0.4553564018453205F, 0.0F, 0.0F);
        this.arm_right_1 = new AdvancedModelBox(this, 48, 0);
        this.arm_right_1.mirror = true;
        this.arm_right_1.setRotationPoint(-2.3F, -1.85F, -4.8F);
        this.arm_right_1.addBox(-1.5F, 0.0F, -2.0F, 3, 6, 4, 0.0F);
        this.setRotateAngle(arm_right_1, 0.045553093477052F, 0.0F, 0.136659280431156F);
        this.ear_right = new AdvancedModelBox(this, 16, 22);
        this.ear_right.mirror = true;
        this.ear_right.setRotationPoint(-1.1F, -3.4F, -0.3F);
        this.ear_right.addBox(-3.0F, -1.0F, 0.0F, 3, 2, 1, 0.0F);
        this.setRotateAngle(ear_right, -0.22759093446006054F, 0.091106186954104F, 1.0016444577195458F);
        this.head_mouth = new AdvancedModelBox(this, 18, 30);
        this.head_mouth.setRotationPoint(0.0F, 1.2F, -3.5F);
        this.head_mouth.addBox(-1.5F, 0.0F, -2.5F, 3, 1, 5, 0.0F);
        this.setRotateAngle(head_mouth, 0.091106186954104F, 0.0F, 0.0F);
        this.main_body = new AdvancedModelBox(this, 0, 0);
        this.main_body.setRotationPoint(0.0F, 15.6F, 0.0F);
        this.main_body.addBox(-3.5F, -5.0F, -8.0F, 7, 8, 14, 0.0F);
        this.setRotateAngle(main_body, -0.045553093477052F, 0.0F, 0.0F);
        this.leg_left_2 = new AdvancedModelBox(this, 0, 47);
        this.leg_left_2.setRotationPoint(-0.4F, 3.7F, 2.0F);
        this.leg_left_2.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(leg_left_2, 0.18203784098300857F, 0.0F, 0.13962634015954636F);
        this.arm_left_1 = new AdvancedModelBox(this, 48, 0);
        this.arm_left_1.setRotationPoint(2.3F, -1.85F, -4.8F);
        this.arm_left_1.addBox(-1.5F, 0.0F, -2.0F, 3, 6, 4, 0.0F);
        this.setRotateAngle(arm_left_1, 0.045553093477052F, 0.0F, -0.136659280431156F);
        this.leg_left_1 = new AdvancedModelBox(this, 0, 36);
        this.leg_left_1.setRotationPoint(2.5F, -1.7F, 3.6F);
        this.leg_left_1.addBox(-2.0F, -1.0F, -2.0F, 4, 6, 5, 0.0F);
        this.setRotateAngle(leg_left_1, -0.136659280431156F, 0.0F, -0.13962634015954636F);
        this.ear_left = new AdvancedModelBox(this, 16, 22);
        this.ear_left.setRotationPoint(1.1F, -3.4F, -0.3F);
        this.ear_left.addBox(0.0F, -1.0F, 0.0F, 3, 2, 1, 0.0F);
        this.setRotateAngle(ear_left, -0.22759093446006054F, -0.091106186954104F, -1.0016444577195458F);
        this.arm_left_2 = new AdvancedModelBox(this, 56, 10);
        this.arm_left_2.setRotationPoint(0.0F, 4.5F, 0.01F);
        this.arm_left_2.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(arm_left_2, 0.0F, 0.0F, 0.136659280431156F);
        this.eye_right = new AdvancedModelBox(this, 0, 20);
        this.eye_right.mirror = true;
        this.eye_right.setRotationPoint(-2.51F, -2.0F, -2.0F);
        this.eye_right.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 0, 20);
        this.eye_left.setRotationPoint(2.51F, -2.0F, -2.0F);
        this.eye_left.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.head_main.addChild(this.eye_left);
        this.head_main.addChild(this.eye_right);
        this.main_body.addChild(this.shape14);
        this.main_body.addChild(this.leg_right_1);
        this.main_body.addChild(this.head_main);
        this.head_mouth.addChild(this.tusk_left_1);
        this.head_mouth.addChild(this.tusk_left);
        this.arm_right_1.addChild(this.arm_right_2);
        this.main_body.addChild(this.shape15);
        this.leg_right_1.addChild(this.leg_right_2);
        this.head_main.addChild(this.head_snout);
        this.main_body.addChild(this.arm_right_1);
        this.head_main.addChild(this.ear_right);
        this.head_main.addChild(this.head_mouth);
        this.leg_left_1.addChild(this.leg_left_2);
        this.main_body.addChild(this.arm_left_1);
        this.main_body.addChild(this.leg_left_1);
        this.head_main.addChild(this.ear_left);
        this.arm_left_1.addChild(this.arm_left_2);
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main_body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
            main_body,
            head_main,
            leg_left_1,
            arm_left_1,
            shape14,
            shape15,
            arm_right_1,
            leg_right_1,
            head_snout,
            ear_left,
            head_mouth,
            ear_right,
            tusk_left,
            tusk_left_1,
            leg_left_2,
            arm_left_2,
            arm_right_2,
            leg_right_2,
                eye_left,
                eye_right
        );
    }
    
    void animate(IAnimatedEntity entityIn) {
        animator.update(entityIn);

        animator.setAnimation(EntityBoar.TALK);
        animator.startKeyframe(10);
        this.rotate(animator, head_mouth, 26.09F, 0, 0);
        this.rotate(animator, head_main, -26.09F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(10);

        animator.setAnimation(EntityBoar.ATTACK);
        animator.startKeyframe(6);
        this.rotate(animator, head_main, 31.31F, 0, 26.08F);
        animator.endKeyframe();
        animator.startKeyframe(4);
        this.rotate(animator, head_main, -26.08F, 0, -46.96F);
        animator.endKeyframe();
        animator.resetKeyframe(4);

        animator.setAnimation(EntityBoar.WORK_DIG);
        animator.startKeyframe(6);
        this.rotate(animator, main_body, 13.04F, 0, 0);
        animator.move(head_main, 0, 2F, 1.9F);
        this.rotate(animator, head_main, 44.35F, 10.43F, 0);
        this.rotate(animator, arm_left_1, 15.65F, 0, -7.83F);
        this.rotate(animator, arm_left_2, -54.78F, 0, 7.83F);
        this.rotate(animator, arm_right_1, 15.65F, 0, 7.83F);
        this.rotate(animator, arm_right_2, -54.78F, 0, -7.83F);
        this.rotate(animator, leg_left_1, -23.48F, 0, -8);
        animator.move(leg_left_1, 0, 1F, 0);
        this.rotate(animator, leg_right_1, -23.48F, 0, 8);
        animator.move(leg_right_1, 0, 1F, 0);
        animator.endKeyframe();
        for (int i = 0; i < 4; i++) {
            float head_angle = i % 2 == 0 ? -10.43F : 10.43F;
            animator.startKeyframe(8);
            this.rotate(animator, main_body, 13.04F, 0, 0);
            animator.move(head_main, 0, 2F, 1.9F);
            this.rotate(animator, head_main, 44.35F, head_angle, 0);
            this.rotate(animator, arm_left_1, 15.65F, 0, -7.83F);
            this.rotate(animator, arm_left_2, -54.78F, 0, 7.83F);
            this.rotate(animator, arm_right_1, 15.65F, 0, 7.83F);
            this.rotate(animator, arm_right_2, -54.78F, 0, -7.83F);
            this.rotate(animator, leg_left_1, -23.48F, 0, -8);
            animator.move(leg_left_1, 0, 1F, 0);
            this.rotate(animator, leg_right_1, -23.48F, 0, 8);
            animator.move(leg_right_1, 0, 1F, 0);
            animator.endKeyframe();
        }
        animator.resetKeyframe(10);
    }

    public void setupAnim(EntityBoar boar, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(boar);
        float globalSpeed = 1f;
        float globalDegree = 1f;
        limbSwingAmount = Math.min(limbSwingAmount, 0.4F);

        if (boar.isNoAi()) {
            limbSwing = ageInTicks;
            limbSwingAmount = 0.4F;
        }

        // Breathing Animation
        this.main_body.setScale((float) (1.0F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1.0F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(main_body, 0.4F * globalSpeed, 0.1F, false, ageInTicks / 20, 2);
        bob(arm_right_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(arm_left_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_right_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_left_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        walk(head_main, 0.4f * globalSpeed, 0.03f, false, 2.4F, 0.08F, ageInTicks / 20, 2);
        if (boar.tickCount % 30 > 22)
            this.head_snout.setScale((float) (1.0F + Math.sin(ageInTicks / 6) * 0.1F + Math.sin(ageInTicks / 2) * 0.1F), (float) (1.0F + Math.sin(ageInTicks / 8) * 0.06F), 1.0F);
        if (boar.tickCount % 100 > 88) {
            swing(ear_left, 1.5f * globalSpeed, 0.6f * globalDegree, true, 0F, 0f, ageInTicks / 2.2F, limbSwingAmount);
            swing(ear_right, 1.5f * globalSpeed, 0.6f * globalDegree, true, 0.2F, 0f, ageInTicks / 2.2F, limbSwingAmount);
        }

        // Blinking Animation
        if (!boar.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-0.51F, -1.0F, -2.0F);
            this.eye_left.setRotationPoint(0.51F, -1.0F, -2.0F);
        }

        // Head Tracking Animation
        if (!boar.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, head_main);
        }

        // Pitch/Yaw handler
        if (boar.isInWater() && !boar.isOnGround()) {
            float pitch = Mth.clamp(boar.getXRot(), -20F, 20.0F) - 10;
            this.setRotateAngle(main_body, (float) (pitch * Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        if (boar.canMove()) {
            if (boar.getCurrentSpeed() > 0.1f || boar.isAngry()) { // Running animation
                bob(main_body, 0.5F * globalSpeed, 0.8F, true, limbSwing, limbSwingAmount);
                walk(main_body, 0.5f * globalSpeed, 0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                walk(head_main, 0.5f * globalSpeed, -0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                flap(head_main, 0.25f * globalSpeed, -0.3f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                bob(head_main, 0.5F * globalSpeed, 0.8F, true, limbSwing, limbSwingAmount);

                bob(arm_right_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_1, 0.5f * globalSpeed, 1.4f * globalDegree, true, 0F, 0.4f, limbSwing, limbSwingAmount);
                walk(arm_right_2, 0.5f * globalSpeed, 0.8f * globalDegree, true, 0.2F, 0.6f, limbSwing, limbSwingAmount);
                bob(arm_left_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_1, 0.5f * globalSpeed, 1.4f * globalDegree, true, 0.6F, 0.4f, limbSwing, limbSwingAmount);
                walk(arm_left_2, 0.5f * globalSpeed, 0.8f * globalDegree, true, 0.8F, 0.6f, limbSwing, limbSwingAmount);
                bob(leg_right_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_1, 0.5f * globalSpeed, 1f * globalDegree, true, 1.4F, -0.4f, limbSwing, limbSwingAmount);
                walk(leg_right_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 1.6F, -0.6f, limbSwing, limbSwingAmount);
                bob(leg_left_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_1, 0.5f * globalSpeed, 1f * globalDegree, true, 2F, -0.4F, limbSwing, limbSwingAmount);
                walk(leg_left_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.2F, -0.6f, limbSwing, limbSwingAmount);
                flap(shape14, 0.5F * globalSpeed, 1.4F * globalDegree, false, 0, 0.2F, limbSwing, limbSwingAmount);
                walk(shape14, 0.5F * globalSpeed, 1.8F * globalDegree, false, 1.0F, 1.8F, limbSwing, limbSwingAmount);
            } else { // Walking Animation
                bob(main_body, 0.5F * globalSpeed, 0.8F, true, limbSwing, limbSwingAmount);
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
                flap(shape14, 0.5F * globalSpeed, 1.4F * globalDegree, false, 0, 0.2F, limbSwing, limbSwingAmount);
                swing(shape14, 0.5F * globalSpeed, 0.8F * globalDegree, false, 1.0F, 0.2F, limbSwing, limbSwingAmount);

            }
        }

        // Sitting Animation
        if (boar.sitProgress > 0) {
            this.progressPosition(main_body, boar.sitProgress, 0.0F, 21.60F, 0.0F, 40);
            this.progressRotation(main_body, boar.sitProgress, (float) Math.toRadians(-2.61F), (float) Math.toRadians(5.22F), 0, 40);
            this.progressRotation(head_main, boar.sitProgress, 0, (float) Math.toRadians(-23.48F), 0, 40);
            this.progressRotation(arm_right_1, boar.sitProgress, (float) Math.toRadians(-20.87F), 0, (float) Math.toRadians(7.83F), 40);
            this.progressRotation(arm_right_2, boar.sitProgress, (float) Math.toRadians(-65.22F), 0, (float) Math.toRadians(-7.83F), 40);
            this.progressRotation(arm_left_1, boar.sitProgress, (float) Math.toRadians(-26.09F), (float) Math.toRadians(20.87F), (float) Math.toRadians(-7.83F), 40);
            this.progressRotation(arm_left_2, boar.sitProgress, (float) Math.toRadians(-57.39F), 0, (float) Math.toRadians(7.83F), 40);
            this.progressRotation(leg_left_2, boar.sitProgress, (float) Math.toRadians(-83.48F), (float) Math.toRadians(-20.87F), (float) Math.toRadians(8.00F), 40);
            this.progressRotation(leg_right_2, boar.sitProgress, (float) Math.toRadians(-83.48F), (float) Math.toRadians(20.87F), (float) Math.toRadians(-8.00F), 40);
        }
        // Sleeping Animation
        if (boar.sleepProgress > 0) {
            this.progressPosition(main_body, boar.sleepProgress, 0.0F, 21F, 0.0F, 40);
            this.progressRotation(main_body, boar.sleepProgress, (float) Math.toRadians(20.87F), 0.0F, (float) Math.toRadians(-91.30), 40);
            this.progressRotation(head_main, boar.sleepProgress, (float) Math.toRadians(26.09), (float) Math.toRadians(15.65), 0, 40);
            this.progressRotation(arm_left_1, boar.sleepProgress, (float) Math.toRadians(5.22), 0, (float) Math.toRadians(23.48), 40);
            this.progressRotation(leg_left_1, boar.sleepProgress, (float) Math.toRadians(10.43), 0, (float) Math.toRadians(36.52), 40);
        }
    }


}