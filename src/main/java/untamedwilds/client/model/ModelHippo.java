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
import untamedwilds.entity.mammal.EntityHippo;

public class ModelHippo extends AdvancedEntityModel<EntityHippo> {

    public AdvancedModelBox body_main;
    public AdvancedModelBox head_neck;
    public AdvancedModelBox arm_right;
    public AdvancedModelBox arm_left;
    public AdvancedModelBox leg_right;
    public AdvancedModelBox leg_left;
    public AdvancedModelBox head_face;
    public AdvancedModelBox head_jaw;
    public AdvancedModelBox face_ear_right;
    public AdvancedModelBox head_jaw_1;
    public AdvancedModelBox face_ear_left;
    public AdvancedModelBox head_jaw_2;
    public AdvancedModelBox tooth_r;
    public AdvancedModelBox tooth_l;
    public AdvancedModelBox eye_right;
    public AdvancedModelBox eye_left;
    public AdvancedModelBox arm_right_2;
    public AdvancedModelBox arm_left_2;
    public AdvancedModelBox leg_right_2;
    public AdvancedModelBox leg_left_2;

    private final ModelAnimator animator;

    public ModelHippo() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 12.5F, 0.0F);
        this.body_main.addBox(-6.0F, -6.0F, -12.0F, 12, 12, 24, 0.0F);
        this.head_neck = new AdvancedModelBox(this, 48, 0);
        this.head_neck.setRotationPoint(0.0F, -0.6F, -10.0F);
        this.head_neck.addBox(-5.0F, -5.0F, -6.0F, 10, 10, 6, 0.0F);
        this.setRotateAngle(head_neck, 0.045553093477052F, 0.0F, 0.0F);
        this.head_face = new AdvancedModelBox(this, 0, 36);
        this.head_face.setRotationPoint(0.0F, -2.0F, -5.0F);
        this.head_face.addBox(-4.5F, -3.5F, -6.0F, 9, 9, 8, 0.0F);
        this.setRotateAngle(head_face, 0.2F, 0.0F, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 0, 37);
        this.eye_left.setRotationPoint(4.51F, -2.0F, -4.0F);
        this.eye_left.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.eye_right = new AdvancedModelBox(this, 0, 37);
        this.eye_right.setRotationPoint(-4.51F, -2.0F, -4.0F);
        this.eye_right.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.face_ear_left = new AdvancedModelBox(this, 0, 36);
        this.face_ear_left.mirror = true;
        this.face_ear_left.setRotationPoint(3.0F, -3.0F, -3.0F);
        this.face_ear_left.addBox(0.0F, -2.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(face_ear_left, 0.136659280431156F, 0.091106186954104F, 0.36425021489121656F);
        this.face_ear_right = new AdvancedModelBox(this, 0, 36);
        this.face_ear_right.setRotationPoint(-3.0F, -3.0F, -3.0F);
        this.face_ear_right.addBox(-2.0F, -2.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(face_ear_right, 0.136659280431156F, -0.091106186954104F, -0.36425021489121656F);
        this.head_jaw = new AdvancedModelBox(this, 36, 36);
        this.head_jaw.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.head_jaw.addBox(-4.0F, -3.0F, -7.0F, 8, 5, 8, 0.0F);
        this.head_jaw_2 = new AdvancedModelBox(this, 36, 49);
        this.head_jaw_2.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.head_jaw_2.addBox(-4.0F, 0.0F, -7.0F, 8, 1, 8, 0.0F);
        this.head_jaw_1 = new AdvancedModelBox(this, 0, 53);
        this.head_jaw_1.setRotationPoint(0.0F, 1.5F, -4.5F);
        this.head_jaw_1.addBox(-4.0F, 0.0F, -8.0F, 8, 3, 8, 0.0F);
        this.tooth_r = new AdvancedModelBox(this, 0, 53);
        this.tooth_r.setRotationPoint(-2.5F, 0.0F, -5.5F);
        this.tooth_r.addBox(-0.5F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
        this.tooth_l = new AdvancedModelBox(this, 0, 53);
        this.tooth_l.setRotationPoint(2.5F, 0.0F, -5.5F);
        this.tooth_l.addBox(-0.5F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
        this.arm_right = new AdvancedModelBox(this, 0, 0);
        this.arm_right.setRotationPoint(-3.49F, 3.5F, -8.5F);
        this.arm_right.addBox(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);
        this.arm_right_2 = new AdvancedModelBox(this, 0, 12);
        this.arm_right_2.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.arm_right_2.addBox(-2.51F, 0.0F, -2.51F, 5, 5, 5, 0.0F);
        this.arm_left = new AdvancedModelBox(this, 0, 0);
        this.arm_left.mirror = true;
        this.arm_left.setRotationPoint(3.49F, 3.5F, -8.5F);
        this.arm_left.addBox(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 0, 12);
        this.arm_left_2.mirror = true;
        this.arm_left_2.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.arm_left_2.addBox(-2.51F, 0.0F, -2.51F, 5, 5, 5, 0.0F);
        this.leg_right = new AdvancedModelBox(this, 0, 0);
        this.leg_right.setRotationPoint(-3.49F, 3.5F, 8.5F);
        this.leg_right.addBox(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);
        this.leg_right_2 = new AdvancedModelBox(this, 0, 12);
        this.leg_right_2.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.leg_right_2.addBox(-2.51F, 0.0F, -2.51F, 5, 5, 5, 0.0F);
        this.leg_left = new AdvancedModelBox(this, 0, 0);
        this.leg_left.setRotationPoint(3.49F, 3.5F, 8.5F);
        this.leg_left.addBox(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);
        this.leg_left_2 = new AdvancedModelBox(this, 0, 12);
        this.leg_left_2.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.leg_left_2.addBox(-2.51F, 0.0F, -2.51F, 5, 5, 5, 0.0F);
        this.body_main.addChild(this.arm_left);
        this.head_jaw_1.addChild(this.tooth_r);
        this.head_face.addChild(this.face_ear_left);
        this.leg_left.addChild(this.leg_left_2);
        this.arm_left.addChild(this.arm_left_2);
        this.head_neck.addChild(this.head_face);
        this.head_jaw.addChild(this.head_jaw_2);
        this.head_face.addChild(this.head_jaw);
        this.head_face.addChild(this.head_jaw_1);
        this.body_main.addChild(this.head_neck);
        this.body_main.addChild(this.leg_right);
        this.body_main.addChild(this.leg_left);
        this.head_jaw_1.addChild(this.tooth_l);
        this.arm_right.addChild(this.arm_right_2);
        this.body_main.addChild(this.arm_right);
        this.head_face.addChild(this.face_ear_right);
        this.leg_right.addChild(this.leg_right_2);
        this.head_face.addChild(this.eye_left);
        this.head_face.addChild(this.eye_right);
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
                head_neck,
                arm_right,
                arm_left,
                leg_right,
                leg_left,
                head_face,
                head_jaw,
                face_ear_right,
                head_jaw_1,
                face_ear_left,
                head_jaw_2,
                tooth_r,
                tooth_l,
                eye_right,
                eye_left,
                arm_right_2,
                arm_left_2,
                leg_right_2,
                leg_left_2
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        this.resetToDefaultPose();
        EntityHippo bear = (EntityHippo) entityIn;
        animator.update(bear);

        animator.setAnimation(EntityHippo.EAT);
        animator.startKeyframe(12);
        this.rotate(animator, head_neck, 18.26F, 0, 0);
        this.rotate(animator, head_face, 26.09F, 0, 5.22F);
        animator.endKeyframe();
        animator.startKeyframe(12);
        this.rotate(animator, head_neck, 18.26F, 0, 0);
        this.rotate(animator, head_face, 20.87F, 5.22F, 0);
        this.rotate(animator, head_jaw, -10.43F, 0, 0);
        this.rotate(animator, head_jaw_1, 10.43F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(12);
        this.rotate(animator, head_neck, 18.26F, 0, 0);
        this.rotate(animator, head_face, 26.09F, -5.22F, 0);
        animator.endKeyframe();
        animator.resetKeyframe(12);

        animator.setAnimation(EntityHippo.IDLE_YAWN);
        animator.startKeyframe(8);
        this.rotate(animator, head_neck, -28.70F, 0, 0);
        this.rotate(animator, head_face, -10.43F, 0, 0);
        animator.move(head_jaw, 0, -1F, 0);
        this.rotate(animator, head_jaw, -28.70F, 0, 0);
        this.rotate(animator, head_jaw_1, 54.78F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(10);
        this.rotate(animator, head_neck, -28.70F, 0, 0);
        this.rotate(animator, head_face, -10.43F, 0, 2.61F);
        animator.move(head_jaw, 0, -1F, 0);
        this.rotate(animator, head_jaw, -44.35F, 0, 0);
        this.rotate(animator, head_jaw_1, 62.61F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(10);
        this.rotate(animator, head_neck, -28.70F, 0, 0);
        this.rotate(animator, head_face, -10.43F, 0, -2.61F);
        animator.move(head_jaw, 0, -1F, 0);
        this.rotate(animator, head_jaw, -44.35F, 0, 0);
        this.rotate(animator, head_jaw_1, 62.61F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(8);

        animator.setAnimation(EntityHippo.IDLE_LOOK);
        animator.startKeyframe(32);
        this.rotate(animator, head_neck, -20.87F, 5.22F, 0);
        this.rotate(animator, head_face, 15.65F, 31.30F, 7.83F);
        animator.endKeyframe();
        animator.startKeyframe(64);
        this.rotate(animator, head_neck, -20.87F, -7.83F, 0);
        this.rotate(animator, head_face, 13.04F, -7.83F, -5.22F);
        animator.endKeyframe();
        animator.resetKeyframe(32);

        animator.setAnimation(EntityHippo.ATTACK);
        animator.startKeyframe(6);
        this.rotate(animator, head_neck, -13.05F, 0, 0);
        this.rotate(animator, head_face, 13.05F, -10.43F, -7.83F);
        animator.move(head_jaw, 0, -1F, 0);
        this.rotate(animator, head_jaw, -28.70F, 0, 0);
        this.rotate(animator, head_jaw_1, 36F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(6);
        this.rotate(animator, head_neck, -13.05F, 7.83F, 0);
        this.rotate(animator, head_face, -13.04F, 23.48F, -26.09F);
        animator.move(head_jaw, 0, -1F, 0);
        this.rotate(animator, head_jaw, -28.70F, 0, 0);
        this.rotate(animator, head_jaw_1, 36F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(6);
        this.rotate(animator, head_neck, -13.05F, -10.43F, 0);
        this.rotate(animator, head_face, -13.04F, -20.87F, -2.61F);
        animator.move(head_jaw, 0, -1F, 0);
        this.rotate(animator, head_jaw, -28.70F, 0, 0);
        this.rotate(animator, head_jaw_1, 36F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(6);

        animator.setAnimation(EntityHippo.IDLE_TALK);
        animator.startKeyframe(10);
        this.rotate(animator, head_jaw, 26.09F, 0, 0);
        this.rotate(animator, head_face, -26.09F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(10);
    }

    public void setupAnim(EntityHippo hippo, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(hippo);
        //limbSwing = ageInTicks / 3;
        //limbSwingAmount = 0.5f;
        float globalSpeed = 1.5f;
        float globalDegree = 1f;
        float f = limbSwing / 2;
        if (limbSwingAmount > 0.4F) {
            limbSwingAmount = 0.4F;
        }
        if (!hippo.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-2F, -2.0F, -4.0F);
            this.eye_left.setRotationPoint(2F, -2.0F, -4.0F);
        }
        if (hippo.isInWater() && !hippo.isOnGround()) {
            float pitch = Mth.clamp(hippo.getXRot() - 10, -25F, 25.0F);
            this.setRotateAngle(body_main, (float) (pitch * Math.PI / 180F), 0, 0);
        }

        if (!hippo.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 3, head_neck);
            this.faceTarget(netHeadYaw, headPitch, 3, head_face);
        }
        this.head_jaw_1.setScaleX(0.9F);
        this.body_main.setScale((float) (1.0F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1.0F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(body_main, 0.4F * globalSpeed, 0.1F, false, ageInTicks / 20, 2);
        bob(arm_right, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(arm_left, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_right, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        bob(leg_left, 0.4F * globalSpeed, 0.1F, false, -ageInTicks / 20, 2);
        walk(head_face, 0.4f * globalSpeed, 0.03f, false, 2.8F, 0.06F, ageInTicks / 20, 2);

        if (hippo.angryProgress != 0) {
            this.progressRotation(head_jaw_1, hippo.angryProgress, 0.36425021489121656F, 0.0F, 0.0F, 40);
        }

        if (hippo.sitProgress != 0) {
            if (hippo.isSitting()) {
                this.progressPosition(body_main, hippo.sitProgress, 0.0F, 18.5F, 0.0F, 40);
                this.progressRotation(body_main, hippo.sitProgress, 0.0F, 0.091106186954104F, 0.0F, 40);
                this.progressPosition(head_neck, hippo.sitProgress, 0, 0, -10.0F, 40);
                this.progressRotation(head_neck, hippo.sitProgress, 0.18203784098300857F, 0.0F, 0.0F, 40);
                this.progressPosition(head_face, hippo.sitProgress, 0.0F, -1.0F, -5.0F, 40);
                this.progressRotation(head_face, hippo.sitProgress, 00.0F, -0.27314402793711257F, 0.045553093477052F, 40);
                this.progressRotation(arm_right, hippo.sitProgress, -1.5025539530419183F, 0.27314402793711257F, 0.0F, 40);
                this.progressRotation(arm_left, hippo.sitProgress, -1.5025539530419183F, -0.27314402793711257F, 0.0F, 40);
                this.progressRotation(leg_right, hippo.sitProgress, -1.5481070465189704F, 2.5497515042385164F, 0.0F, 40);
                this.progressRotation(leg_left, hippo.sitProgress, -1.5481070465189704F, -2.5497515042385164F, 0.0F, 40);
            }
            if (hippo.isSleeping()) {
                this.progressPosition(body_main, hippo.sitProgress, 0.0F, 18.5F, 0.0F, 40);
                this.progressRotation(body_main, hippo.sitProgress, 0.0F, 0.091106186954104F, 0.0F, 40);
                this.progressPosition(head_neck, hippo.sitProgress, 0, 0, -10.0F, 40);
                this.progressRotation(head_neck, hippo.sitProgress, 0.18203784098300857F, 0.0F, 0.0F, 40);
                this.progressPosition(head_face, hippo.sitProgress, 0.0F, -1.0F, -5.0F, 40);
                this.progressRotation(head_face, hippo.sitProgress, 0.0F, -0.27314402793711257F, 0.045553093477052F, 40);
                this.progressRotation(arm_right, hippo.sitProgress, -1.5025539530419183F, 0.27314402793711257F, 0.0F, 40);
                this.progressRotation(arm_left, hippo.sitProgress, -1.5025539530419183F, -0.27314402793711257F, 0.0F, 40);
                this.progressRotation(leg_right, hippo.sitProgress, -1.5481070465189704F, 2.5497515042385164F, 0.0F, 40);
                this.progressRotation(leg_left, hippo.sitProgress, -1.5481070465189704F, -2.5497515042385164F, 0.0F, 40);
            }
        }

        // Controls the walking animation
        if (hippo.canMove()) {
            bob(body_main, 0.6f * globalSpeed, 0.6f * globalDegree, true, f, limbSwingAmount);
            walk(head_neck, 0.6f * globalSpeed, 0.2f * globalDegree, false, 0, 0, f, limbSwingAmount);
            walk(head_face, 0.6f * globalSpeed, 0.15f * globalDegree, true, 0, 0, f, limbSwingAmount);

            walk(arm_right, -0.6f * globalSpeed, 1.4f * globalDegree, true, 0F, 1.4f, f, limbSwingAmount);
            walk(arm_right_2, -0.6f * globalSpeed, 1.4f * globalDegree, false, -1F, 1.4f, f, limbSwingAmount * 1.2f);
            walk(arm_left, -0.6f * globalSpeed, 1.4f * globalDegree, true, 2F, 1.4f, f, limbSwingAmount);
            walk(arm_left_2, -0.6f * globalSpeed, 1.4f * globalDegree, false, 1F, 1.4f, f, limbSwingAmount * 1.2f);
            walk(leg_right, 0.6f * globalSpeed, 1.4f * globalDegree, false, 2.8F, 0, f, limbSwingAmount);
            walk(leg_right_2, 0.6f * globalSpeed, 1.4f * globalDegree, true, 1.8F, 0, f, limbSwingAmount);
            walk(leg_left, 0.6f * globalSpeed, 1.4f * globalDegree, false, 0.8F, 0, f, limbSwingAmount);
            walk(leg_left_2, 0.6f * globalSpeed, 1.4f * globalDegree, true, -0.2F, 0, f, limbSwingAmount);
        }
    }


}