package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import untamedwilds.entity.mammal.EntityRhino;

public class ModelRhino extends AdvancedEntityModel<EntityRhino> {
    
    private final AdvancedModelBox body_belly;
    private final AdvancedModelBox body_front;
    private final AdvancedModelBox leg_right;
    private final AdvancedModelBox leg_left;
    private final AdvancedModelBox head_neck;
    private final AdvancedModelBox arm_right_1;
    private final AdvancedModelBox arm_left_1;
    private final AdvancedModelBox head_face;
    private final AdvancedModelBox ear_right;
    private final AdvancedModelBox ear_left;
    private final AdvancedModelBox horn_front;
    private final AdvancedModelBox horn_front_small;
    private final AdvancedModelBox horn_back;
    private final AdvancedModelBox eye_left;
    private final AdvancedModelBox eye_right;
    private final AdvancedModelBox arm_right_2;
    private final AdvancedModelBox arm_left_2;
    private final AdvancedModelBox leg_right_2;
    private final AdvancedModelBox leg_left_2;

    private final ModelAnimator animator;

    public ModelRhino() {
        this.texWidth = 128;
        this.texHeight = 64;
        
        this.horn_back = new AdvancedModelBox(this, 0, 0);
        this.horn_back.setRotationPoint(0.0F, -2.6F, -4.7F);
        this.horn_back.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 3, 0.0F);
        this.setRotateAngle(horn_back, 0.045553093477052F, 0.0F, 0.0F);
        this.horn_front_small = new AdvancedModelBox(this, 11, 0);
        this.horn_front_small.setRotationPoint(0.0F, -3.6F, -9.1F);
        this.horn_front_small.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 3, 0.0F);
        this.setRotateAngle(horn_front_small, 0.18203784F, 0.0F, 0.0F);
        this.arm_left_1 = new AdvancedModelBox(this, 50, 0);
        this.arm_left_1.setRotationPoint(4.0F, 6.0F, -6.0F);
        this.arm_left_1.addBox(-2.5F, 0.0F, -3.0F, 5, 7, 6, 0.0F);
        this.setRotateAngle(arm_left_1, 0.36425021489121656F, 0.0F, 0.09110618695410334F);
        this.horn_front = new AdvancedModelBox(this, 0, 10);
        this.horn_front.setRotationPoint(0.0F, -3.6F, -9.0F);
        this.horn_front.addBox(-1.0F, -8.0F, -1.5F, 2, 8, 3, 0.0F);
        this.setRotateAngle(horn_front, 0.136659280431156F, 0.0F, 0.0F);
        this.leg_left = new AdvancedModelBox(this, 72, 28);
        this.leg_left.setRotationPoint(4.0F, 6.0F, 15.0F);
        this.leg_left.addBox(-2.5F, -2.0F, -3.5F, 5, 7, 7, 0.0F);
        this.setRotateAngle(leg_left, 0.31869712141416456F, 0.0F, 0.091106186954104F);
        this.leg_left_2 = new AdvancedModelBox(this, 76, 42);
        this.leg_left_2.setRotationPoint(-0.01F, 3.0F, 0.5F);
        this.leg_left_2.addBox(-2.5F, 0.0F, -3.0F, 5, 7, 6, 0.0F);
        this.setRotateAngle(leg_left_2, -0.31869712141416456F, 0.0F, -0.091106186954104F);
        this.ear_right = new AdvancedModelBox(this, 0, 36);
        this.ear_right.setRotationPoint(-2.5F, -4.0F, -5.0F);
        this.ear_right.addBox(-1.0F, -5.0F, -0.5F, 2, 5, 1, 0.0F);
        this.setRotateAngle(ear_right, 0.0F, 0.22759093446006054F, -0.5462880558742251F);
        this.eye_left = new AdvancedModelBox(this, 48, 44);
        this.eye_left.setRotationPoint(3.01F, -1.5F, -5.0F);
        this.eye_left.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 28, 36);
        this.arm_left_2.setRotationPoint(-0.01F, 6.3F, 0.5F);
        this.arm_left_2.addBox(-2.5F, 0.0F, -3.0F, 5, 5, 5, 0.0F);
        this.setRotateAngle(arm_left_2, -0.136659280431156F, 0.0F, -0.091106186954104F);
        this.arm_right_1 = new AdvancedModelBox(this, 50, 0);
        this.arm_right_1.mirror = true;
        this.arm_right_1.setRotationPoint(-4.0F, 6.0F, -6.0F);
        this.arm_right_1.addBox(-2.5F, 0.0F, -3.0F, 5, 7, 6, 0.0F);
        this.setRotateAngle(arm_right_1, 0.3642502148912149F, 0.0F, -0.09110618695410382F);
        this.arm_right_2 = new AdvancedModelBox(this, 28, 36);
        this.arm_right_2.mirror = true;
        this.arm_right_2.setRotationPoint(0.01F, 6.3F, 0.5F);
        this.arm_right_2.addBox(-2.5F, 0.0F, -3.0F, 5, 5, 5, 0.0F);
        this.setRotateAngle(arm_right_2, -0.13665928043115597F, 0.0F, 0.091106186954104F);
        this.leg_right = new AdvancedModelBox(this, 72, 28);
        this.leg_right.mirror = true;
        this.leg_right.setRotationPoint(-4.0F, 6.0F, 15.0F);
        this.leg_right.addBox(-2.5F, -2.0F, -3.5F, 5, 7, 7, 0.0F);
        this.setRotateAngle(leg_right, 0.31869712141416456F, 0.0F, -0.091106186954104F);
        this.body_front = new AdvancedModelBox(this, 72, 0);
        this.body_front.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body_front.addBox(-8.0F, -8.0F, -12.0F, 16, 16, 12, 0.0F);
        this.setRotateAngle(body_front, -0.22759093446006054F, 0.0F, 0.0F);
        this.leg_right_2 = new AdvancedModelBox(this, 76, 42);
        this.leg_right_2.mirror = true;
        this.leg_right_2.setRotationPoint(0.01F, 3.0F, 0.5F);
        this.leg_right_2.addBox(-2.5F, 0.0F, -3.0F, 5, 7, 6, 0.0F);
        this.setRotateAngle(leg_right_2, -0.31869712141416456F, 0.0F, 0.091106186954104F);
        this.body_belly = new AdvancedModelBox(this, 0, 0);
        this.body_belly.setRotationPoint(0.0F, 8.5F, 0.0F);
        this.body_belly.addBox(-7.0F, -7.0F, -3.0F, 14, 14, 22, 0.0F);
        this.eye_right = new AdvancedModelBox(this, 44, 44);
        this.eye_right.setRotationPoint(-3.01F, -1.5F, -5.0F);
        this.eye_right.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.head_neck = new AdvancedModelBox(this, 0, 36);
        this.head_neck.setRotationPoint(0.0F, -1.0F, -11.0F);
        this.head_neck.addBox(-4.0F, -5.0F, -8.0F, 8, 10, 12, 0.0F);
        this.setRotateAngle(head_neck, 0.5462880558742241F, 0.0F, 0.0F);
        this.head_face = new AdvancedModelBox(this, 40, 36);
        this.head_face.setRotationPoint(0.0F, 1.0F, -6.0F);
        this.head_face.addBox(-3.0F, -4.0F, -11.0F, 6, 8, 12, 0.0F);
        this.setRotateAngle(head_face, 0.22759093446006054F, 0.0F, 0.0F);
        this.ear_left = new AdvancedModelBox(this, 0, 36);
        this.ear_left.mirror = true;
        this.ear_left.setRotationPoint(2.5F, -4.0F, -5.0F);
        this.ear_left.addBox(-1.0F, -5.0F, -0.5F, 2, 5, 1, 0.0F);
        this.setRotateAngle(ear_left, 0.0F, -0.22759093446006054F, 0.5462880558742251F);
        this.head_face.addChild(this.horn_back);
        this.body_front.addChild(this.arm_left_1);
        this.head_face.addChild(this.horn_front);
        this.head_face.addChild(this.horn_front_small);
        this.body_belly.addChild(this.leg_left);
        this.leg_left.addChild(this.leg_left_2);
        this.head_neck.addChild(this.ear_right);
        this.head_face.addChild(this.eye_left);
        this.arm_left_1.addChild(this.arm_left_2);
        this.body_front.addChild(this.arm_right_1);
        this.arm_right_1.addChild(this.arm_right_2);
        this.body_belly.addChild(this.leg_right);
        this.body_belly.addChild(this.body_front);
        this.leg_right.addChild(this.leg_right_2);
        this.head_face.addChild(this.eye_right);
        this.body_front.addChild(this.head_neck);
        this.head_neck.addChild(this.head_face);
        this.head_neck.addChild(this.ear_left);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(body_belly);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_belly, body_front, leg_right, leg_left, head_neck, arm_right_1, arm_left_1, head_face,
                ear_right, ear_left, horn_front, horn_back, eye_left, eye_right, arm_right_2, arm_left_2, leg_right_2,
                leg_left_2, horn_front_small
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        EntityRhino rhino = (EntityRhino) entityIn;
        animator.update(rhino);

        animator.setAnimation(EntityRhino.ATTACK_THREATEN);
        for (int i = 0; i < 2; i++) {
            animator.startKeyframe(12);
            this.rotate(animator, body_front, 0, 0, 7.83F);
            this.rotate(animator, head_neck, 7.83F, 0, -13.04F);
            this.rotate(animator, arm_right_2, 31.31F, 0, 0);
            animator.move(arm_left_1, 0, -0.6F, 0);
            this.rotate(animator, arm_left_1, 0, 0, -7.83F);
            animator.move(arm_right_1, 0, -0.5F, 0);
            this.rotate(animator, arm_right_1, -46.96F, 0, -5.21F);
            animator.endKeyframe();
            animator.startKeyframe(9);
            this.rotate(animator, body_front, 0, 0, -13.05F);
            this.rotate(animator, head_neck, 7.83F, 0, 26.08F);
            this.rotate(animator, arm_right_2, 31.31F, 0, 0);
            animator.move(arm_left_1, 0, 0.5F, 0);
            this.rotate(animator, arm_left_1, 0, 0, 13.04F);
            animator.move(arm_right_1, 0, 0.5F, 0);
            this.rotate(animator, arm_right_1, 54.79F, 0, 10.43F);
            animator.endKeyframe();
        }
        animator.resetKeyframe(8);

        animator.setAnimation(EntityRhino.ATTACK_GORE);
        animator.startKeyframe(6);
        this.rotate(animator, head_neck, 31.31F, 0, 26.08F);
        animator.endKeyframe();
        animator.startKeyframe(4);
        this.rotate(animator, head_neck, -26.08F, 0, -46.96F);
        animator.endKeyframe();
        animator.resetKeyframe(4);
    }

    public void setupAnim(EntityRhino rhino, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(rhino);
        float globalSpeed = 1.5f;
        float globalDegree = 1f;
        float f = limbSwing / 2;
        limbSwingAmount = Math.min(0.4F, limbSwingAmount);

        // Breathing Animation
        this.body_belly.setScale((float) (1.0F + Math.sin(ageInTicks / 20) * 0.08F), (float) (1.0F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);
        this.body_front.setScale((float) (1.0F + Math.sin(ageInTicks / 20) * 0.08F), (float) (1.0F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);
        bob(body_belly, 0.4F * globalSpeed, 0.1F, false, ageInTicks / 20, 2);
        bob(arm_right_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(arm_left_1, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_right, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_left, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        walk(head_neck, 0.4f * globalSpeed, 0.03f, false, 2.8F, 0.06F, ageInTicks / 20, 2);

        // Blinking Animation
        if (!rhino.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-2F, -2.0F, -4.0F);
            this.eye_left.setRotationPoint(2F, -2.0F, -4.0F);
        }

        // Head Tracking Animation
        if (!rhino.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 3, head_neck);
            this.faceTarget(netHeadYaw, headPitch, 3, head_face);
        }

        // Movement Animation
        if (rhino.canMove()) {
            bob(body_belly, 0.8f * globalSpeed, 0.6f * globalDegree, true, f, limbSwingAmount);
            walk(head_neck, 0.8f * globalSpeed, 0.2f * globalDegree, false, 0, 0, f, limbSwingAmount);
            walk(head_face, 0.8f * globalSpeed, 0.15f * globalDegree, true, 0, 0, f, limbSwingAmount);
            walk(arm_right_1, -0.8f * globalSpeed, 1.4f * globalDegree, true, 0F, 1.4f, f, limbSwingAmount);
            walk(arm_right_2, -0.8f * globalSpeed, 1.4f * globalDegree, false, -1F, 1.4f, f, limbSwingAmount * 1.2f);
            walk(arm_left_1, -0.8f * globalSpeed, 1.4f * globalDegree, true, 2F, 1.4f, f, limbSwingAmount);
            walk(arm_left_2, -0.8f * globalSpeed, 1.4f * globalDegree, false, 1F, 1.4f, f, limbSwingAmount * 1.2f);
            walk(leg_right, 0.8f * globalSpeed, 1.4f * globalDegree, false, 2.8F, 0, f, limbSwingAmount);
            walk(leg_right_2, 0.8f * globalSpeed, 1.4f * globalDegree, true, 1.8F, 0, f, limbSwingAmount);
            walk(leg_left, 0.8f * globalSpeed, 1.4f * globalDegree, false, 0.8F, 0, f, limbSwingAmount);
            walk(leg_left_2, 0.8f * globalSpeed, 1.4f * globalDegree, true, -0.2F, 0, f, limbSwingAmount);
        }

        // Sitting Animation
        if (rhino.sitProgress > 0) {
            this.progressPosition(body_belly, rhino.sitProgress, 0.0F, 17.5F, 0.0F, 40);
            this.progressRotation(body_belly, rhino.sitProgress, 0.0F, 0.0F, (float) Math.toRadians(7.83F), 40);
            this.progressPosition(arm_right_1, rhino.sitProgress, -6.0F, 6.0F, -9.0F, 40);
            this.progressRotation(arm_right_1, rhino.sitProgress, (float) Math.toRadians(-104.3F), (float) Math.toRadians(23.48F), (float) Math.toRadians(-96.52F), 40);
            this.progressRotation(arm_right_2, rhino.sitProgress, (float) Math.toRadians(80.87F), 0.0F, (float) Math.toRadians(5.22F), 40);
            this.progressPosition(arm_left_1, rhino.sitProgress, 6.0F, 6.0F, -9.0F, 40);
            this.progressRotation(arm_left_1, rhino.sitProgress, (float) Math.toRadians(-96.52F), 0.0F, (float) Math.toRadians(5.22F), 40);
            this.progressRotation(arm_left_2, rhino.sitProgress, (float) Math.toRadians(86.09F), 0.0F, (float) Math.toRadians(-5.22F), 40);
            this.progressPosition(leg_right, rhino.sitProgress, -6.0F, 6.0F, 15.0F, 40);
            this.progressRotation(leg_right, rhino.sitProgress, (float) Math.toRadians(-106.9F), (float) Math.toRadians(13.04F), (float) Math.toRadians(-2.61F), 40);
            this.progressRotation(leg_right_2, rhino.sitProgress, (float) Math.toRadians(15.65F), 0.0F, (float) Math.toRadians(5.22F), 40);
            this.progressRotation(leg_left, rhino.sitProgress, (float) Math.toRadians(-13.04F), (float) Math.toRadians(5.22F), (float) Math.toRadians(88.70F), 40);
        }

        // Sleeping Animation
        if (rhino.sleepProgress > 0) {
            this.progressPosition(body_belly, rhino.sleepProgress, 0.0F, 17.5F, 0.0F, 40);
            this.progressRotation(body_belly, rhino.sleepProgress, 0.0F, 0.0F, (float)Math.toRadians(7.83F), 40);
            this.progressPosition(arm_right_1, rhino.sleepProgress, -6.0F, 6.0F, -9.0F, 40);
            this.progressRotation(arm_right_1, rhino.sleepProgress, (float)Math.toRadians(-104.3F), (float)Math.toRadians(23.48F), (float)Math.toRadians(-96.52F), 40);
            this.progressRotation(arm_right_2, rhino.sleepProgress, (float)Math.toRadians(80.87F), 0.0F, (float)Math.toRadians(5.22F), 40);
            this.progressPosition(arm_left_1, rhino.sleepProgress, 6.0F, 6.0F, -9.0F, 40);
            this.progressRotation(arm_left_1, rhino.sleepProgress, (float)Math.toRadians(-96.52F), 0.0F, (float)Math.toRadians(5.22F), 40);
            this.progressRotation(arm_left_2, rhino.sleepProgress, (float)Math.toRadians(86.09F), 0.0F, (float)Math.toRadians(-5.22F), 40);
            this.progressPosition(leg_right, rhino.sleepProgress, -6.0F, 6.0F, 15.0F, 40);
            this.progressRotation(leg_right, rhino.sleepProgress, (float)Math.toRadians(-106.9F), (float)Math.toRadians(13.04F), (float)Math.toRadians(-2.61F), 40);
            this.progressRotation(leg_right_2, rhino.sleepProgress, (float)Math.toRadians(15.65F), 0.0F, (float)Math.toRadians(5.22F), 40);
            this.progressRotation(leg_left, rhino.sleepProgress, (float)Math.toRadians(-13.04F), (float)Math.toRadians(5.22F), (float)Math.toRadians(88.70F), 40);
        }
    }
}
