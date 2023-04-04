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
import untamedwilds.entity.mammal.EntityBigCat;
import untamedwilds.entity.mammal.EntityCamel;
import untamedwilds.entity.mammal.EntityHyena;

public class ModelCamel extends AdvancedEntityModel<EntityCamel> {
    
    public AdvancedModelBox body_main;
    public AdvancedModelBox leg_left_thigh;
    public AdvancedModelBox body_hump_front;
    public AdvancedModelBox arm_left_1;
    public AdvancedModelBox neck_1;
    public AdvancedModelBox body_hump_back;
    public AdvancedModelBox body_hump_centre;
    public AdvancedModelBox arm_right_1;
    public AdvancedModelBox leg_right_thigh;
    public AdvancedModelBox tail;
    public AdvancedModelBox leg_left_calf;
    public AdvancedModelBox leg_left_calf_1;
    public AdvancedModelBox leg_left_hair;
    public AdvancedModelBox arm_left_2;
    public AdvancedModelBox arm_left_hair;
    public AdvancedModelBox neck_2;
    public AdvancedModelBox neck_hair_1;
    public AdvancedModelBox head_main;
    public AdvancedModelBox neck_hair_2;
    public AdvancedModelBox head_nose;
    public AdvancedModelBox head_nose_1;
    public AdvancedModelBox ear_left;
    public AdvancedModelBox head_hair;
    public AdvancedModelBox ear_right;
    public AdvancedModelBox arm_right_2;
    public AdvancedModelBox arm_right_hair;
    public AdvancedModelBox leg_right_calf;
    public AdvancedModelBox leg_right_calf_1;
    public AdvancedModelBox leg_right_hair;
    public AdvancedModelBox eye_left;
    public AdvancedModelBox eye_right;

    private final ModelAnimator animator;

    public ModelCamel() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.arm_right_1 = new AdvancedModelBox(this, 0, 0);
        this.arm_right_1.mirror = true;
        this.arm_right_1.setRotationPoint(-3.5F, -0.8F, -7.8F);
        this.arm_right_1.addBox(-2.5F, -1.5F, -3.0F, 5, 12, 6, 0.0F);
        this.setRotateAngle(arm_right_1, 0.18203784098300857F, 0.0F, 0.0F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, -0.7F, 0.0F);
        this.body_main.addBox(-5.0F, -6.0F, -10.0F, 10, 12, 24, 0.0F);
        this.setRotateAngle(body_main, -0.045553093477052F, 0.0F, 0.0F);
        this.leg_right_thigh = new AdvancedModelBox(this, 44, 0);
        this.leg_right_thigh.mirror = true;
        this.leg_right_thigh.setRotationPoint(-3.8F, -2.0F, 11.5F);
        this.leg_right_thigh.addBox(-2.5F, -1.5F, -4.0F, 5, 10, 8, 0.0F);
        this.setRotateAngle(leg_right_thigh, 0.136659280431156F, 0.0F, 0.0F);
        this.ear_right = new AdvancedModelBox(this, 16, 0);
        this.ear_right.setRotationPoint(-3.0F, -3.0F, -1.0F);
        this.ear_right.addBox(-3.0F, -2.0F, 0.0F, 3, 3, 1, 0.0F);
        this.setRotateAngle(ear_right, 0.7285004297824331F, 0.5009094953223726F, 0.7740535232594852F);
        this.leg_right_hair = new AdvancedModelBox(this, 89, 48);
        this.leg_right_hair.mirror = true;
        this.leg_right_hair.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.leg_right_hair.addBox(-3.0F, 0.0F, -3.5F, 6, 9, 7, 0.0F);
        this.setRotateAngle(leg_right_hair, -0.136659280431156F, 0.0F, 0.0F);
        this.neck_hair_2 = new AdvancedModelBox(this, 88, 45);
        this.neck_hair_2.setRotationPoint(0.0F, -0.6F, -2.0F);
        this.neck_hair_2.addBox(-3.0F, -3.1F, -7.3F, 6, 8, 8, 0.0F);
        this.setRotateAngle(neck_hair_2, -0.22759093446006054F, 0.0F, 0.0F);
        this.neck_hair_2.scaleX = 1.01F;
        this.arm_left_hair = new AdvancedModelBox(this, 89, 48);
        this.arm_left_hair.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.arm_left_hair.addBox(-3.0F, 0.0F, -3.5F, 6, 9, 7, 0.0F);
        this.leg_right_calf = new AdvancedModelBox(this, 68, 20);
        this.leg_right_calf.mirror = true;
        this.leg_right_calf.setRotationPoint(0.0F, 7.0F, -0.5F);
        this.leg_right_calf.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.0F);
        this.setRotateAngle(leg_right_calf, 0.31869712141416456F, 0.0F, 0.0F);
        this.body_hump_back = new AdvancedModelBox(this, 30, 46);
        this.body_hump_back.setRotationPoint(0.0F, -5.8F, 9.0F);
        this.body_hump_back.addBox(-3.0F, -6.0F, -6.0F, 6, 8, 10, 0.0F);
        this.setRotateAngle(body_hump_back, -0.045553093477052F, 0.0F, 0.0F);
        this.arm_right_2 = new AdvancedModelBox(this, 84, 20);
        this.arm_right_2.mirror = true;
        this.arm_right_2.setRotationPoint(0.01F, 9.05F, 0.01F);
        this.arm_right_2.addBox(-1.5F, 0.0F, -1.5F, 3, 17, 3, 0.0F);
        this.setRotateAngle(arm_right_2, -0.136659280431156F, 0.0F, 0.0F);
        this.arm_left_1 = new AdvancedModelBox(this, 0, 0);
        this.arm_left_1.setRotationPoint(3.5F, -0.8F, -7.8F);
        this.arm_left_1.addBox(-2.5F, -1.5F, -3.0F, 5, 12, 6, 0.0F);
        this.setRotateAngle(arm_left_1, 0.18203784098300857F, 0.0F, 0.0F);
        this.arm_right_hair = new AdvancedModelBox(this, 89, 48);
        this.arm_right_hair.mirror = true;
        this.arm_right_hair.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.arm_right_hair.addBox(-3.0F, 0.0F, -3.5F, 6, 9, 7, 0.0F);
        this.body_hump_front = new AdvancedModelBox(this, 0, 48);
        this.body_hump_front.setRotationPoint(0.0F, -6.0F, -3.0F);
        this.body_hump_front.addBox(-3.0F, -6.0F, -6.0F, 6, 8, 8, 0.0F);
        this.setRotateAngle(body_hump_front, 0.136659280431156F, 0.0F, 0.0F);
        this.tail = new AdvancedModelBox(this, 62, 49);
        this.tail.setRotationPoint(0.0F, -5.0F, 14.0F);
        this.tail.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        this.setRotateAngle(tail, 0.22759093446006054F, 0.0F, 0.0F);
        this.leg_left_thigh = new AdvancedModelBox(this, 44, 0);
        this.leg_left_thigh.setRotationPoint(3.8F, -2.0F, 11.5F);
        this.leg_left_thigh.addBox(-2.5F, -1.5F, -4.0F, 5, 10, 8, 0.0F);
        this.setRotateAngle(leg_left_thigh, 0.136659280431156F, 0.0F, 0.0F);
        this.neck_2 = new AdvancedModelBox(this, 58, 34);
        this.neck_2.setRotationPoint(0.0F, 0.0F, -10.2F);
        this.neck_2.addBox(-2.5F, -2.5F, -8.5F, 5, 5, 10, 0.0F);
        this.setRotateAngle(neck_2, -1.3658946726107624F, 0.0F, 0.0F);
        this.ear_left = new AdvancedModelBox(this, 16, 0);
        this.ear_left.setRotationPoint(3.0F, -3.0F, -1.0F);
        this.ear_left.addBox(0.0F, -2.0F, 0.0F, 3, 3, 1, 0.0F);
        this.setRotateAngle(ear_left, 0.7285004297824331F, -0.5009094953223726F, -0.7740535232594852F);
        this.head_nose = new AdvancedModelBox(this, 94, 0);
        this.head_nose.setRotationPoint(0.0F, -4.5F, -6.0F);
        this.head_nose.addBox(-3.0F, 0.0F, -6.0F, 6, 3, 6, 0.0F);
        this.setRotateAngle(head_nose, 0.18203784098300857F, 0.0F, 0.0F);
        this.leg_left_hair = new AdvancedModelBox(this, 89, 48);
        this.leg_left_hair.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.leg_left_hair.addBox(-3.0F, 0.0F, -3.5F, 6, 9, 7, 0.0F);
        this.setRotateAngle(leg_left_hair, -0.136659280431156F, 0.0F, 0.0F);
        this.leg_left_calf_1 = new AdvancedModelBox(this, 84, 26);
        this.leg_left_calf_1.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.leg_left_calf_1.addBox(-1.5F, 0.0F, -1.5F, 3, 11, 3, 0.0F);
        this.setRotateAngle(leg_left_calf_1, -0.40980330836826856F, 0.0F, 0.0F);
        this.head_nose_1 = new AdvancedModelBox(this, 62, 0);
        this.head_nose_1.setRotationPoint(0.0F, -1.8F, -6.0F);
        this.head_nose_1.addBox(-2.5F, 0.0F, -5.0F, 5, 2, 5, 0.0F);
        this.leg_right_calf_1 = new AdvancedModelBox(this, 84, 26);
        this.leg_right_calf_1.mirror = true;
        this.leg_right_calf_1.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.leg_right_calf_1.addBox(-1.5F, 0.0F, -1.5F, 3, 11, 3, 0.0F);
        this.setRotateAngle(leg_right_calf_1, -0.40980330836826856F, 0.0F, 0.0F);
        this.neck_hair_1 = new AdvancedModelBox(this, 89, 44);
        this.neck_hair_1.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.neck_hair_1.addBox(-3.0F, -4.0F, -12.0F, 6, 8, 12, 0.0F);
        this.head_main = new AdvancedModelBox(this, 0, 36);
        this.head_main.setRotationPoint(0.0F, -2.4F, -6.3F);
        this.head_main.addBox(-3.0F, -5.0F, -6.0F, 6, 5, 6, 0.0F);
        this.setRotateAngle(head_main, 1.2747884856566583F, 0.0F, 0.0F);
        this.leg_left_calf = new AdvancedModelBox(this, 68, 20);
        this.leg_left_calf.setRotationPoint(0.0F, 7.0F, -0.5F);
        this.leg_left_calf.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.0F);
        this.setRotateAngle(leg_left_calf, 0.31869712141416456F, 0.0F, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 84, 20);
        this.arm_left_2.setRotationPoint(0.01F, 9.05F, 0.01F);
        this.arm_left_2.addBox(-1.5F, 0.0F, -1.5F, 3, 17, 3, 0.0F);
        this.setRotateAngle(arm_left_2, -0.136659280431156F, 0.0F, 0.0F);
        this.body_hump_centre = new AdvancedModelBox(this, 70, 0);
        this.body_hump_centre.setRotationPoint(0.0F, -6.8F, 3.0F);
        this.body_hump_centre.addBox(-3.0F, -6.0F, -6.0F, 6, 8, 12, 0.0F);
        this.setRotateAngle(body_hump_centre, -0.045553093477052F, 0.0F, 0.0F);
        this.head_hair = new AdvancedModelBox(this, 96, 29);
        this.head_hair.setRotationPoint(0.0F, -3.0F, 0.8F);
        this.head_hair.addBox(-3.0F, -3.1F, -7.3F, 6, 8, 8, 0.0F);
        this.setRotateAngle(head_hair, -0.22759093446006054F, 0.0F, 0.0F);
        this.head_hair.scaleX = 1.02F;
        this.neck_1 = new AdvancedModelBox(this, 96, 10);
        this.neck_1.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.neck_1.addBox(-2.5F, -2.5F, -12.0F, 5, 5, 10, 0.0F);
        this.setRotateAngle(neck_1, 0.136659280431156F, 0.0F, 0.0F);
        this.neck_1.scaleX = 1.01F;
        this.eye_right = new AdvancedModelBox(this, 0, 0);
        this.eye_right.mirror = true;
        this.eye_right.setRotationPoint(-3.01F, -3.0F, -4.0F);
        this.eye_right.addBox(0.0F, -1F, -1.0F, 0, 1, 2, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 0, 0);
        this.eye_left.setRotationPoint(3.01F, -3.0F, -4.0F);
        this.eye_left.addBox(0.0F, -1F, -1.0F, 0, 1, 2, 0.0F);
        this.body_main.addChild(this.arm_right_1);
        this.body_main.addChild(this.leg_right_thigh);
        this.head_main.addChild(this.ear_right);
        this.leg_right_calf.addChild(this.leg_right_hair);
        this.neck_2.addChild(this.neck_hair_2);
        this.arm_left_1.addChild(this.arm_left_hair);
        this.leg_right_thigh.addChild(this.leg_right_calf);
        this.body_main.addChild(this.body_hump_back);
        this.arm_right_1.addChild(this.arm_right_2);
        this.body_main.addChild(this.arm_left_1);
        this.arm_right_1.addChild(this.arm_right_hair);
        this.body_main.addChild(this.body_hump_front);
        this.body_main.addChild(this.tail);
        this.body_main.addChild(this.leg_left_thigh);
        this.neck_1.addChild(this.neck_2);
        this.head_main.addChild(this.ear_left);
        this.head_main.addChild(this.head_nose);
        this.leg_left_calf.addChild(this.leg_left_hair);
        this.leg_left_calf.addChild(this.leg_left_calf_1);
        this.head_main.addChild(this.head_nose_1);
        this.leg_right_calf.addChild(this.leg_right_calf_1);
        this.neck_1.addChild(this.neck_hair_1);
        this.neck_2.addChild(this.head_main);
        this.leg_left_thigh.addChild(this.leg_left_calf);
        this.arm_left_1.addChild(this.arm_left_2);
        this.body_main.addChild(this.body_hump_centre);
        this.head_main.addChild(this.head_hair);
        this.body_main.addChild(this.neck_1);
        this.head_main.addChild(this.eye_left);
        this.head_main.addChild(this.eye_right);
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
            body_main,
            leg_left_thigh,
            body_hump_front,
            arm_left_1,
            neck_1,
            body_hump_back,
            body_hump_centre,
            arm_right_1,
            leg_right_thigh,
            tail,
            leg_left_calf,
            leg_left_calf_1,
            leg_left_hair,
            arm_left_2,
            arm_left_hair,
            neck_2,
            neck_hair_1,
            head_main,
            neck_hair_2,
            head_nose,
            head_nose_1,
            ear_left,
            head_hair,
            ear_right,
            arm_right_2,
            arm_right_hair,
            leg_right_calf,
            leg_right_calf_1,
            leg_right_hair,
            eye_left,
            eye_right
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        animator.update(entityIn);

        animator.setAnimation(EntityCamel.IDLE_TALK);
        animator.startKeyframe(10);
        this.rotate(animator, neck_1, -5.22F, 0, 0);
        this.rotate(animator, neck_2, -20F, 0, 0);
        this.rotate(animator, head_nose_1, 26.09F, 0, 0);
        this.rotate(animator, head_main, -26.09F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(10);

        animator.setAnimation(EntityCamel.ATTACK_SPIT);
        animator.startKeyframe(6);
        this.rotate(animator, neck_1, 5.22F, 0, 0);
        this.rotate(animator, neck_2, -46.96F, 0, 0);
        animator.move(head_main, 0, 2, 0);
        this.rotate(animator, head_main, 15.65F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(4);
        this.rotate(animator, neck_2, 46.96F, 0, 0);
        animator.move(head_main, 0, 2, 0);
        this.rotate(animator, head_main, -28.70F, 0, 0);
        animator.move(head_nose, 0, 0, 1.5F);
        this.rotate(animator, head_nose, -28.70F, 0, 0);
        this.rotate(animator, head_nose, 36.52F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(6);
    }

    public void setupAnim(EntityCamel camel, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(camel);
        float globalSpeed = 1f;
        float globalDegree = 1f;
        limbSwingAmount = Math.min(limbSwingAmount, 0.4F);
        if (camel.isNoAi()) {
            limbSwing = camel.tickCount / 4F;
            limbSwingAmount = 0.4F;
        }
        // Breathing Animation
        this.body_main.setScale((float) (1.0F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1.0F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(body_main, 0.4F * globalSpeed, 0.1F, false, ageInTicks / 20, 2);
        bob(arm_right_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(arm_left_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_right_calf, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_left_calf, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        walk(neck_1, 0.4f * globalSpeed, 0.03f, false, 2.4F, 0.08F, ageInTicks / 20, 2);
        if (camel.sleepProgress == 0)
            walk(neck_2, 0.4f * globalSpeed, 0.03f, false, 2.8F, 0.06F, ageInTicks / 20, 2);

        // Blinking Animation
        if (!camel.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-0.51F, -0.5F, -3.0F);
            this.eye_left.setRotationPoint(0.51F, -0.5F, -3.0F);
        }

        // Head Tracking Animation
        if (!camel.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 4, neck_1);
            this.faceTarget(netHeadYaw, headPitch, 4, neck_2);
            this.faceTarget(netHeadYaw, headPitch, 3, head_main);
        }

        // Pitch/Yaw handler
        if (camel.isInWater() && !camel.isOnGround()) {
            float pitch = Mth.clamp(camel.getXRot(), -20F, 20.0F) - 10;
            this.setRotateAngle(body_main, (float) (pitch * Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        if (camel.canMove()) {
            if (camel.getCurrentSpeed() > 0.1f || camel.isAngry()) { // Running animation
                bob(body_main, 0.5F * globalSpeed, 0.5F, false, limbSwing, limbSwingAmount);
                walk(body_main, 0.5f * globalSpeed, 0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                walk(neck_1, 0.5f * globalSpeed, -0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                walk(neck_2, 0.5f * globalSpeed, 0.3f * globalDegree, false, 0.5F, 0f, limbSwing, limbSwingAmount);

                bob(arm_right_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_1, 0.5f * globalSpeed, 1f * globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.2F, 0.2f, limbSwing, limbSwingAmount);
                bob(arm_left_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_1, 0.5f * globalSpeed, 1f * globalDegree, true, 0.6F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.8F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_right_calf, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_calf, 0.5f * globalSpeed, 1f * globalDegree, true, 1.4F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_calf_1, 0.5f * globalSpeed, 0.6f * globalDegree, true, 1.6F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_left_calf, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_calf, 0.5f * globalSpeed, 1f * globalDegree, true, 2F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_calf_1, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.2F, 0.2f, limbSwing, limbSwingAmount);
            } else { // Walking Animation
                flap(body_main, 0.5F * globalSpeed, 0.2F,false, 0, 0, limbSwing, limbSwingAmount);
                flap(neck_1, 0.5F * globalSpeed, 0.2F,true, 0.2F, 0, limbSwing, limbSwingAmount);

                bob(arm_right_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_1, 0.5f * globalSpeed, globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.2F, 0.2f, limbSwing, limbSwingAmount);
                bob(arm_left_1, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_1, 0.5f * globalSpeed, globalDegree, true, 2.4F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_2, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.6F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_right_thigh, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_thigh, 0.5f * globalSpeed, globalDegree, true, 0.2F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_calf_1, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.4F, 0.2f, limbSwing, limbSwingAmount);
                bob(leg_left_thigh, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_thigh, 0.5f * globalSpeed, globalDegree, true, 2.6F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_calf_1, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.8F, 0.2f, limbSwing, limbSwingAmount);
            }
        }

        // Sitting Animation
        if (camel.sitProgress > 0) {
            this.progressPosition(body_main, camel.sitProgress, 0F, 15.30F, 0F, 40);
            this.progressRotation(neck_1, camel.sitProgress, 0F, 0, 0F, 40);

            this.progressRotation(arm_left_1, camel.sitProgress, (float)Math.toRadians(-26.09), 0.0F, (float)Math.toRadians(-10.43), 40);
            this.progressRotation(arm_left_2, camel.sitProgress, (float)Math.toRadians(112.17), (float)Math.toRadians(-2.61), (float)Math.toRadians(10.43), 40);
            this.progressRotation(arm_right_1, camel.sitProgress, (float)Math.toRadians(-26.09), 0.0F, (float)Math.toRadians(10.43), 40);
            this.progressRotation(arm_right_2, camel.sitProgress, (float)Math.toRadians(112.17), (float)Math.toRadians(2.61), (float)Math.toRadians(-10.43), 40);

            this.progressPosition(leg_left_thigh, camel.sitProgress, 3.80F, -3F, 11.50F, 40);
            this.progressRotation(leg_left_thigh, camel.sitProgress, (float)Math.toRadians(-7.83), (float)Math.toRadians(-5.22), (float)Math.toRadians(-7.83), 40);
            this.progressRotation(leg_left_calf, camel.sitProgress, (float)Math.toRadians(-54.78F), 0, 0F, 40);
            this.progressRotation(leg_left_calf_1, camel.sitProgress, (float)Math.toRadians(161.74), 0, 0F, 40);

            this.progressPosition(leg_right_thigh, camel.sitProgress, -3.80F, -3F, 11.50F, 40);
            this.progressRotation(leg_right_thigh, camel.sitProgress, (float)Math.toRadians(-7.83), (float)Math.toRadians(5.22), (float)Math.toRadians(7.83), 40);
            this.progressRotation(leg_right_calf, camel.sitProgress, (float)Math.toRadians(-54.78F), 0, 0F, 40);
            this.progressRotation(leg_right_calf_1, camel.sitProgress, (float)Math.toRadians(161.74), 0, 0F, 40);
        }
        
        // Sleeping Animation
        if (camel.sleepProgress > 0) {
            this.progressPosition(body_main, camel.sleepProgress, 0F, 15.30F, 0F, 40);
            this.progressRotation(neck_1, camel.sleepProgress, (float)Math.toRadians(16), 0, 0F, 40);
            this.progressRotation(neck_2, camel.sleepProgress, (float)Math.toRadians(-2.61), 0, 0F, 40);
            this.progressPosition(head_main, camel.sleepProgress, 0F, 2.6F, -8.3F, 40);
            this.progressRotation(head_main, camel.sleepProgress, (float)Math.toRadians(-13.04), 0, 0F, 40);

            this.progressRotation(arm_left_1, camel.sleepProgress, (float)Math.toRadians(-26.09), 0.0F, (float)Math.toRadians(-10.43), 40);
            this.progressRotation(arm_left_2, camel.sleepProgress, (float)Math.toRadians(112.17), (float)Math.toRadians(-2.61), (float)Math.toRadians(10.43), 40);
            this.progressRotation(arm_right_1, camel.sleepProgress, (float)Math.toRadians(-26.09), 0.0F, (float)Math.toRadians(10.43), 40);
            this.progressRotation(arm_right_2, camel.sleepProgress, (float)Math.toRadians(112.17), (float)Math.toRadians(2.61), (float)Math.toRadians(-10.43), 40);

            this.progressPosition(leg_left_thigh, camel.sleepProgress, 3.80F, -3F, 11.50F, 40);
            this.progressRotation(leg_left_thigh, camel.sleepProgress, (float)Math.toRadians(-7.83), (float)Math.toRadians(-5.22), (float)Math.toRadians(-7.83), 40);
            this.progressRotation(leg_left_calf, camel.sleepProgress, (float)Math.toRadians(-54.78F), 0, 0F, 40);
            this.progressRotation(leg_left_calf_1, camel.sleepProgress, (float)Math.toRadians(161.74), 0, 0F, 40);

            this.progressPosition(leg_right_thigh, camel.sleepProgress, -3.80F, -3F, 11.50F, 40);
            this.progressRotation(leg_right_thigh, camel.sleepProgress, (float)Math.toRadians(-7.83), (float)Math.toRadians(5.22), (float)Math.toRadians(7.83), 40);
            this.progressRotation(leg_right_calf, camel.sleepProgress, (float)Math.toRadians(-54.78F), 0, 0F, 40);
            this.progressRotation(leg_right_calf_1, camel.sleepProgress, (float)Math.toRadians(161.74), 0, 0F, 40);
        }
        else {
            flap(tail, 0.4f * globalSpeed, 0.2f * globalDegree, true, 0F, 0f, ageInTicks / 6, 2);
        }
    }


}