package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import untamedwilds.entity.mammal.EntityBison;

public class ModelBison extends AdvancedEntityModel<EntityBison> {

    private final AdvancedModelBox body_main;
    private final AdvancedModelBox body_torso;
    private final AdvancedModelBox leg_left_thigh;
    private final AdvancedModelBox leg_right_thigh;
    private final AdvancedModelBox body_hair;
    private final AdvancedModelBox tail;
    private final AdvancedModelBox arm_right_1;
    private final AdvancedModelBox arm_left_1;
    private final AdvancedModelBox head_neck;
    private final AdvancedModelBox arm_right_2;
    private final AdvancedModelBox arm_right_fur;
    private final AdvancedModelBox arm_left_2;
    private final AdvancedModelBox arm_left_fur;
    private final AdvancedModelBox head_main;
    private final AdvancedModelBox head_hair;
    private final AdvancedModelBox head_horn_right;
    private final AdvancedModelBox head_ear_right;
    private final AdvancedModelBox head_ear_left;
    private final AdvancedModelBox head_horn_left;
    private final AdvancedModelBox head_beard;
    private final AdvancedModelBox eye_left;
    private final AdvancedModelBox eye_right;
    private final AdvancedModelBox leg_left_calf;
    private final AdvancedModelBox leg_right_calf;

    private final ModelAnimator animator;
    
    public ModelBison() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        
        this.body_torso = new AdvancedModelBox(this, 64, 0);
        this.body_torso.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.body_torso.addBox(-6.0F, -7.0F, -6.0F, 12, 14, 12, 0.0F);
        this.setRotateAngle(body_torso, 0.31869712141416456F, 0.0F, 0.0F);
        this.arm_left_1 = new AdvancedModelBox(this, 0, 0);
        this.arm_left_1.setRotationPoint(4.5F, 2.2F, -2.0F);
        this.arm_left_1.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(arm_left_1, -0.18203784098300857F, 0.0F, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 0, 12);
        this.arm_left_2.setRotationPoint(0.0F, 6.3F, 0.0F);
        this.arm_left_2.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(arm_left_2, -0.136659280431156F, 0.0F, 0.0F);
        this.arm_right_2 = new AdvancedModelBox(this, 0, 12);
        this.arm_right_2.mirror = true;
        this.arm_right_2.setRotationPoint(0.0F, 6.3F, 0.0F);
        this.arm_right_2.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(arm_right_2, -0.136659280431156F, 0.0F, 0.0F);
        this.head_horn_right = new AdvancedModelBox(this, 0, 47);
        this.head_horn_right.mirror = true;
        this.head_horn_right.setRotationPoint(-2.0F, -3.0F, -2.5F);
        this.head_horn_right.addBox(-4.0F, -1.0F, 0.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(head_horn_right, 0.18203784098300857F, -0.18203784098300857F, 0.36425021489121656F);
        this.leg_left_calf = new AdvancedModelBox(this, 86, 26);
        this.leg_left_calf.setRotationPoint(0.7F, 3.5F, 2.5F);
        this.leg_left_calf.addBox(-1.0F, 0.0F, -1.0F, 3, 10, 3, 0.0F);
        this.head_horn_left = new AdvancedModelBox(this, 0, 47);
        this.head_horn_left.setRotationPoint(2.0F, -3.0F, -2.5F);
        this.head_horn_left.addBox(0.0F, -1.0F, 0.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(head_horn_left, 0.18203784098300857F, 0.18203784098300857F, -0.36425021489121656F);
        this.head_beard = new AdvancedModelBox(this, 22, 44);
        this.head_beard.setRotationPoint(0.0F, 3.0F, -0.5F);
        this.head_beard.addBox(-2.0F, 0.0F, -1.5F, 4, 5, 3, 0.0F);
        this.setRotateAngle(head_beard, 0.18203784098300857F, 0.0F, 0.0F);
        this.body_hair = new AdvancedModelBox(this, 0, 36);
        this.body_hair.setRotationPoint(0.0F, 5.3F, -0.4F);
        this.body_hair.addBox(0.0F, 0.0F, -12.0F, 0, 4, 24, 0.0F);
        this.setRotateAngle(body_hair, 0.091106186954104F, 0.0F, 0.0F);
        this.eye_right = new AdvancedModelBox(this, 0, 30);
        this.eye_right.setRotationPoint(-2.51F, 0.0F, -2.0F);
        this.eye_right.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.body_main.addBox(-5.5F, -6.0F, -10.0F, 11, 12, 20, 0.0F);
        this.leg_right_thigh = new AdvancedModelBox(this, 62, 26);
        this.leg_right_thigh.mirror = true;
        this.leg_right_thigh.setRotationPoint(-4.0F, 0.5F, 7.5F);
        this.leg_right_thigh.addBox(-3.0F, -3.5F, -3.5F, 5, 10, 7, 0.0F);
        this.head_neck = new AdvancedModelBox(this, 8, 47);
        this.head_neck.setRotationPoint(0.0F, -1.0F, -3.5F);
        this.head_neck.addBox(-2.0F, -3.5F, -6.0F, 4, 7, 6, 0.0F);
        this.setRotateAngle(head_neck, -0.22759093446006054F, 0.0F, 0.0F);
        this.leg_right_calf = new AdvancedModelBox(this, 86, 26);
        this.leg_right_calf.mirror = true;
        this.leg_right_calf.setRotationPoint(-0.7F, 3.5F, 2.5F);
        this.leg_right_calf.addBox(-2.0F, 0.0F, -1.0F, 3, 10, 3, 0.0F);
        this.head_ear_right = new AdvancedModelBox(this, 18, 32);
        this.head_ear_right.setRotationPoint(-3.5F, -1.3F, -0.4F);
        this.head_ear_right.addBox(-2.0F, -1.0F, 0.0F, 3, 2, 1, 0.0F);
        this.setRotateAngle(head_ear_right, 0.18203784098300857F, 0.18203784098300857F, -0.136659280431156F);
        this.head_main = new AdvancedModelBox(this, 0, 32);
        this.head_main.setRotationPoint(0.0F, 2.0F, -5.0F);
        this.head_main.addBox(-2.5F, -4.5F, -5.0F, 5, 9, 6, 0.0F);
        this.setRotateAngle(head_main, -0.36425021489121656F, 0.0F, 0.0F);
        this.head_hair = new AdvancedModelBox(this, 22, 32);
        this.head_hair.setRotationPoint(0.0F, -2.4F, -2.4F);
        this.head_hair.addBox(-3.0F, -3.0F, -3.0F, 6, 4, 7, 0.0F);
        this.setRotateAngle(head_hair, 0.22759093446006054F, 0.0F, 0.0F);
        this.arm_right_1 = new AdvancedModelBox(this, 0, 0);
        this.arm_right_1.mirror = true;
        this.arm_right_1.setRotationPoint(-4.5F, 2.2F, -2.0F);
        this.arm_right_1.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.setRotateAngle(arm_right_1, -0.18203784098300857F, 0.0F, 0.0F);
        this.arm_left_fur = new AdvancedModelBox(this, 42, 0);
        this.arm_left_fur.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.arm_left_fur.addBox(-2.5F, -0.1F, -3.0F, 5, 9, 6, 0.0F);
        this.leg_left_thigh = new AdvancedModelBox(this, 62, 26);
        this.leg_left_thigh.setRotationPoint(4.0F, 0.5F, 7.5F);
        this.leg_left_thigh.addBox(-2.0F, -3.5F, -3.5F, 5, 10, 7, 0.0F);
        this.tail = new AdvancedModelBox(this, 64, 0);
        this.tail.setRotationPoint(0.0F, -6.0F, 10.0F);
        this.tail.addBox(-2.5F, 0.0F, 0.0F, 5, 10, 0, 0.0F);
        this.setRotateAngle(tail, 0.18203784098300857F, 0.0F, 0.0F);
        this.head_ear_left = new AdvancedModelBox(this, 18, 32);
        this.head_ear_left.mirror = true;
        this.head_ear_left.setRotationPoint(3.5F, -1.3F, -0.4F);
        this.head_ear_left.addBox(-1.0F, -1.0F, 0.0F, 3, 2, 1, 0.0F);
        this.setRotateAngle(head_ear_left, 0.18203784098300857F, -0.18203784098300857F, 0.136659280431156F);
        this.eye_left = new AdvancedModelBox(this, 0, 30);
        this.eye_left.setRotationPoint(2.51F, 0.0F, -2.0F);
        this.eye_left.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.arm_right_fur = new AdvancedModelBox(this, 42, 0);
        this.arm_right_fur.mirror = true;
        this.arm_right_fur.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.arm_right_fur.addBox(-2.5F, -0.1F, -3.0F, 5, 9, 6, 0.0F);
        this.body_main.addChild(this.body_torso);
        this.body_torso.addChild(this.arm_left_1);
        this.arm_left_1.addChild(this.arm_left_2);
        this.arm_right_1.addChild(this.arm_right_2);
        this.head_main.addChild(this.head_horn_right);
        this.leg_left_thigh.addChild(this.leg_left_calf);
        this.head_main.addChild(this.head_horn_left);
        this.head_main.addChild(this.head_beard);
        this.body_main.addChild(this.body_hair);
        this.head_main.addChild(this.eye_right);
        this.body_main.addChild(this.leg_right_thigh);
        this.body_torso.addChild(this.head_neck);
        this.leg_right_thigh.addChild(this.leg_right_calf);
        this.head_main.addChild(this.head_ear_right);
        this.head_neck.addChild(this.head_main);
        this.head_main.addChild(this.head_hair);
        this.body_torso.addChild(this.arm_right_1);
        this.arm_left_1.addChild(this.arm_left_fur);
        this.body_main.addChild(this.leg_left_thigh);
        this.body_main.addChild(this.tail);
        this.head_main.addChild(this.head_ear_left);
        this.head_main.addChild(this.eye_left);
        this.arm_right_1.addChild(this.arm_right_fur);
        
        animator = ModelAnimator.create();
        updateDefaultPose();
    }
    
    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
            body_main,
            body_torso,
            leg_left_thigh,
            leg_right_thigh,
            body_hair,
            tail,
            arm_right_1,
            arm_left_1,
            head_neck,
            arm_right_2,
            arm_right_fur,
            arm_left_2,
            arm_left_fur,
            head_main,
            head_hair,
            head_horn_right,
            head_ear_right,
            head_ear_left,
            head_horn_left,
            head_beard,
            eye_left,
            eye_right,
            leg_left_calf,
            leg_right_calf
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        EntityBison bison = (EntityBison) entityIn;
        animator.update(bison);

        animator.setAnimation(EntityBison.ATTACK_THREATEN);
        for (int i = 0; i < 2; i++) {
            animator.startKeyframe(12);
            this.rotate(animator, body_torso, 0, 0, 7.83F);
            this.rotate(animator, head_neck, 7.83F, 0, -13.04F);
            this.rotate(animator, arm_right_2, 31.31F, 0, 0);
            animator.move(arm_left_1, 0, -0.6F, 0);
            this.rotate(animator, arm_left_1, 0, 0, -7.83F);
            animator.move(arm_right_1, 0, -0.5F, 0);
            this.rotate(animator, arm_right_1, -46.96F, 0, -5.21F);
            animator.endKeyframe();
            animator.startKeyframe(9);
            this.rotate(animator, body_torso, 0, 0, -13.05F);
            this.rotate(animator, head_neck, 7.83F, 0, 26.08F);
            this.rotate(animator, arm_right_2, 31.31F, 0, 0);
            animator.move(arm_left_1, 0, 0.5F, 0);
            this.rotate(animator, arm_left_1, 0, 0, 13.04F);
            animator.move(arm_right_1, 0, 0.5F, 0);
            this.rotate(animator, arm_right_1, 54.79F, 0, 10.43F);
            animator.endKeyframe();
        }
        animator.resetKeyframe(8);

        animator.setAnimation(EntityBison.ATTACK_GORE);
        animator.startKeyframe(6);
        this.rotate(animator, head_neck, 31.31F, 0, 26.08F);
        animator.endKeyframe();
        animator.startKeyframe(4);
        this.rotate(animator, head_neck, -26.08F, 0, -46.96F);
        animator.endKeyframe();
        animator.resetKeyframe(4);
    }

    public void setRotationAngles(EntityBison bison, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(bison);
        float globalSpeed = 1.5f;
        float globalDegree = 1f;
        float f = limbSwing / 2;
        limbSwingAmount = Math.min(0.4F, limbSwingAmount);

        // Breathing Animation
        this.body_main.setScale((float) (1.0F + Math.sin(ageInTicks / 20) * 0.08F), (float) (1.0F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);
        this.body_torso.setScale((float) (1.0F + Math.sin(ageInTicks / 20) * 0.08F), (float) (1.0F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);
        bob(body_main, 0.4F * globalSpeed, 0.1F, false, ageInTicks / 20, 2);
        bob(arm_right_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(arm_left_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_right_thigh, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_left_thigh, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        walk(head_neck, 0.4f * globalSpeed, 0.03f, false, 2.8F, 0.06F, ageInTicks / 20, 2);

        // Blinking Animation
        if (!bison.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-2F, -2.0F, -4.0F);
            this.eye_left.setRotationPoint(2F, -2.0F, -4.0F);
        }

        // Head Tracking Animation
        if (!bison.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 3, head_neck);
            this.faceTarget(netHeadYaw, headPitch, 3, head_main);
        }

        // Movement Animation
        if (bison.canMove()) {
            bob(body_main, 0.8f * globalSpeed, 0.6f * globalDegree, true, f, limbSwingAmount);
            walk(head_neck, 0.8f * globalSpeed, 0.2f * globalDegree, false, 0, 0, f, limbSwingAmount);
            walk(head_main, 0.8f * globalSpeed, 0.15f * globalDegree, true, 0, 0, f, limbSwingAmount);
            walk(arm_right_1, -0.8f * globalSpeed, 1.4f * globalDegree, true, 0F, 1.4f, f, limbSwingAmount);
            walk(arm_right_2, -0.8f * globalSpeed, 1.4f * globalDegree, false, -1F, 1.4f, f, limbSwingAmount * 1.2f);
            walk(arm_left_1, -0.8f * globalSpeed, 1.4f * globalDegree, true, 2F, 1.4f, f, limbSwingAmount);
            walk(arm_left_2, -0.8f * globalSpeed, 1.4f * globalDegree, false, 1F, 1.4f, f, limbSwingAmount * 1.2f);
            walk(leg_right_thigh, 0.8f * globalSpeed, 1.4f * globalDegree, false, 2.8F, 0, f, limbSwingAmount);
            walk(leg_right_calf, 0.8f * globalSpeed, 1.4f * globalDegree, true, 1.8F, 0, f, limbSwingAmount);
            walk(leg_left_thigh, 0.8f * globalSpeed, 1.4f * globalDegree, false, 0.8F, 0, f, limbSwingAmount);
            walk(leg_left_calf, 0.8f * globalSpeed, 1.4f * globalDegree, true, -0.2F, 0, f, limbSwingAmount);
        }

        // Sitting Animation
        if (bison.sitProgress > 0) {
            this.progressPosition(body_main, bison.sitProgress, 0.0F, 17.5F, 0.0F, 40);
            this.progressRotation(body_main, bison.sitProgress, 0.0F, 0.0F, (float) Math.toRadians(7.83F), 40);
            this.progressPosition(arm_right_1, bison.sitProgress, -6.0F, 6.0F, -9.0F, 40);
            this.progressRotation(arm_right_1, bison.sitProgress, (float) Math.toRadians(-104.3F), (float) Math.toRadians(23.48F), (float) Math.toRadians(-96.52F), 40);
            this.progressRotation(arm_right_2, bison.sitProgress, (float) Math.toRadians(80.87F), 0.0F, (float) Math.toRadians(5.22F), 40);
            this.progressPosition(arm_left_1, bison.sitProgress, 6.0F, 6.0F, -9.0F, 40);
            this.progressRotation(arm_left_1, bison.sitProgress, (float) Math.toRadians(-96.52F), 0.0F, (float) Math.toRadians(5.22F), 40);
            this.progressRotation(arm_left_2, bison.sitProgress, (float) Math.toRadians(86.09F), 0.0F, (float) Math.toRadians(-5.22F), 40);
            this.progressPosition(leg_right_thigh, bison.sitProgress, -6.0F, 6.0F, 15.0F, 40);
            this.progressRotation(leg_right_thigh, bison.sitProgress, (float) Math.toRadians(-106.9F), (float) Math.toRadians(13.04F), (float) Math.toRadians(-2.61F), 40);
            this.progressRotation(leg_right_calf, bison.sitProgress, (float) Math.toRadians(15.65F), 0.0F, (float) Math.toRadians(5.22F), 40);
            this.progressRotation(leg_left_thigh, bison.sitProgress, (float) Math.toRadians(-13.04F), (float) Math.toRadians(5.22F), (float) Math.toRadians(88.70F), 40);
        }

        // Sleeping Animation
        if (bison.sleepProgress > 0) {
            this.progressPosition(body_main, bison.sleepProgress, 0.0F, 17.5F, 0.0F, 40);
            this.progressRotation(body_main, bison.sleepProgress, 0.0F, 0.0F, (float)Math.toRadians(7.83F), 40);
            this.progressPosition(arm_right_1, bison.sleepProgress, -6.0F, 6.0F, -9.0F, 40);
            this.progressRotation(arm_right_1, bison.sleepProgress, (float)Math.toRadians(-104.3F), (float)Math.toRadians(23.48F), (float)Math.toRadians(-96.52F), 40);
            this.progressRotation(arm_right_2, bison.sleepProgress, (float)Math.toRadians(80.87F), 0.0F, (float)Math.toRadians(5.22F), 40);
            this.progressPosition(arm_left_1, bison.sleepProgress, 6.0F, 6.0F, -9.0F, 40);
            this.progressRotation(arm_left_1, bison.sleepProgress, (float)Math.toRadians(-96.52F), 0.0F, (float)Math.toRadians(5.22F), 40);
            this.progressRotation(arm_left_2, bison.sleepProgress, (float)Math.toRadians(86.09F), 0.0F, (float)Math.toRadians(-5.22F), 40);
            this.progressPosition(leg_right_thigh, bison.sleepProgress, -6.0F, 6.0F, 15.0F, 40);
            this.progressRotation(leg_right_thigh, bison.sleepProgress, (float)Math.toRadians(-106.9F), (float)Math.toRadians(13.04F), (float)Math.toRadians(-2.61F), 40);
            this.progressRotation(leg_right_calf, bison.sleepProgress, (float)Math.toRadians(15.65F), 0.0F, (float)Math.toRadians(5.22F), 40);
            this.progressRotation(leg_left_thigh, bison.sleepProgress, (float)Math.toRadians(-13.04F), (float)Math.toRadians(5.22F), (float)Math.toRadians(88.70F), 40);
        }
    }
}
