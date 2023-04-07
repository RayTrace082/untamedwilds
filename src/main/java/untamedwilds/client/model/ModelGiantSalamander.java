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
import untamedwilds.entity.amphibian.EntityGiantSalamander;

public class ModelGiantSalamander extends AdvancedEntityModel<EntityGiantSalamander> {

    public AdvancedModelBox body_main;
    public AdvancedModelBox body_torso;
    public AdvancedModelBox tail_1;
    public AdvancedModelBox leg_left_1;
    public AdvancedModelBox leg_right_1;
    public AdvancedModelBox arm_left_1;
    public AdvancedModelBox arm_right_1;
    public AdvancedModelBox head_main;
    public AdvancedModelBox head_jaw;
    public AdvancedModelBox head_face;
    public AdvancedModelBox tail_2;

    private final ModelAnimator animator;

    public ModelGiantSalamander() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.arm_right_1 = new AdvancedModelBox(this, 24, 0);
        this.arm_right_1.mirror = true;
        this.arm_right_1.setRotationPoint(-2.0F, 0.2F, -3.0F);
        this.arm_right_1.addBox(-1.5F, -1.0F, -4.0F, 3, 2, 4, 0.0F);
        this.setRotateAngle(arm_right_1, 0.22759093446006054F, 1.1838568316277536F, 0.0F);
        this.arm_left_1 = new AdvancedModelBox(this, 24, 0);
        this.arm_left_1.setRotationPoint(2.0F, 0.2F, -3.0F);
        this.arm_left_1.addBox(-1.5F, -1.0F, -4.0F, 3, 2, 4, 0.0F);
        this.setRotateAngle(arm_left_1, 0.22759093446006054F, -1.0471975511965976F, 0.0F);
        this.tail_2 = new AdvancedModelBox(this, 42, 8);
        this.tail_2.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.tail_2.addBox(-1.0F, -2.0F, 0.0F, 2, 4, 7, 0.0F);
        this.body_main = new AdvancedModelBox(this, 0, 10);
        this.body_main.setRotationPoint(0.0F, 22.3F, 0.0F);
        this.body_main.addBox(-2.5F, -1.5F, 0.0F, 5, 3, 7, 0.0F);
        this.leg_left_1 = new AdvancedModelBox(this, 24, 8);
        this.leg_left_1.setRotationPoint(1.5F, 0.2F, 5.0F);
        this.leg_left_1.addBox(-1.5F, -1.0F, -4.0F, 3, 2, 4, 0.0F);
        this.setRotateAngle(leg_left_1, 0.22759093446006054F, -2.0943951023931953F, 0.0F);
        this.tail_1 = new AdvancedModelBox(this, 42, 0);
        this.tail_1.setRotationPoint(0.0F, 0.01F, 7.0F);
        this.tail_1.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 4, 0.0F);
        this.head_main = new AdvancedModelBox(this, 0, 20);
        this.head_main.setRotationPoint(0.0F, -0.4F, -6.0F);
        this.head_main.addBox(-3.0F, -1.5F, -2.5F, 6, 3, 3, 0.0F);
        this.setRotateAngle(head_main, 0.136659280431156F, 0.0F, 0.0F);
        this.body_torso = new AdvancedModelBox(this, 0, 0);
        this.body_torso.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body_torso.addBox(-2.5F, -1.5F, -7.0F, 5, 3, 7, 0.0F);
        this.leg_right_1 = new AdvancedModelBox(this, 24, 8);
        this.leg_right_1.mirror = true;
        this.leg_right_1.setRotationPoint(-1.5F, 0.2F, 5.0F);
        this.leg_right_1.addBox(-1.5F, -1.0F, -4.0F, 3, 2, 4, 0.0F);
        this.setRotateAngle(leg_right_1, 0.22759093446006054F, 2.0943951023931953F, 0.0F);
        this.head_jaw = new AdvancedModelBox(this, 18, 25);
        this.head_jaw.setRotationPoint(0.0F, 0.5F, -2.5F);
        this.head_jaw.addBox(-3.0F, 0.0F, -3.0F, 6, 1, 3, 0.0F);
        this.head_face = new AdvancedModelBox(this, 18, 20);
        this.head_face.setRotationPoint(0.0F, -0.5F, -1.5F);
        this.head_face.addBox(-3.0F, -1.0F, -4.0F, 6, 2, 3, 0.0F);
        this.body_torso.addChild(this.arm_right_1);
        this.body_torso.addChild(this.arm_left_1);
        this.tail_1.addChild(this.tail_2);
        this.body_main.addChild(this.leg_left_1);
        this.body_main.addChild(this.tail_1);
        this.body_torso.addChild(this.head_main);
        this.body_main.addChild(this.body_torso);
        this.body_main.addChild(this.leg_right_1);
        this.head_main.addChild(this.head_jaw);
        this.head_main.addChild(this.head_face);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(    body_main, body_torso, arm_left_1, arm_right_1, leg_left_1, leg_right_1,
                tail_1, tail_2, head_main, head_face, head_jaw
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        animator.update(entityIn);

        animator.setAnimation(EntityGiantSalamander.ATTACK_SWALLOW);
        animator.startKeyframe(5);
        this.rotate(animator, head_main, -5.22F, 15.65F, -20.87F);
        this.rotate(animator, head_jaw, 57.39F, 0, 0);
        this.rotate(animator, head_face, -44.35F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        this.rotate(animator, head_main, -5.22F, -5.22F, 10.43F);
        this.rotate(animator, head_jaw, 57.39F, 0, 0);
        this.rotate(animator, head_face, -44.35F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
    }

    public void setupAnim(EntityGiantSalamander salamander, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();
        animate(salamander);
        float globalSpeed = 0.8f;
        float globalDegree = 1.0f;
        limbSwingAmount = Math.min(0.6F, limbSwingAmount);

        // Breathing Animation
        this.body_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.body_torso.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.head_face.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.head_jaw.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.head_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);

        // Head Tracking Animation
        if (!salamander.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, head_main);
        }

        // Pitch/Yaw handler
        if (salamander.isInWater() && !salamander.isOnGround()) {
            this.setRotateAngle(body_main, salamander.getXRot() * ((float) Math.PI / 180F), 0, 0);
        }
        this.body_torso.rotateAngleY = Mth.rotLerp((float) 0.05, this.body_torso.rotateAngleY, salamander.offset);
        this.tail_1.rotateAngleY = Mth.rotLerp((float) 0.05, this.tail_1.rotateAngleY, -1F * salamander.offset);
        this.tail_2.rotateAngleY = Mth.rotLerp((float) 0.05, this.tail_2.rotateAngleY, -2F * salamander.offset);

        // Movement Animation
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{head_main, body_torso, body_main, tail_1, tail_2};
        chainSwing(bodyParts, globalSpeed * 1.4F, globalDegree * 1.2F, -4, limbSwing, limbSwingAmount * 0.3F);
        float onGround = Math.min(0.8F, limbSwingAmount * (salamander.isOnGround() ? 2 : 1));
        if (salamander.isInWater()) {
            flap(arm_left_1, globalSpeed, globalDegree, false, 0.8F, 1f, limbSwing, limbSwingAmount);
            flap(leg_left_1, globalSpeed, globalDegree * 0.8f, false, 1.6F, 1f, limbSwing, limbSwingAmount);
            flap(arm_right_1, globalSpeed, globalDegree, false, 2.4F, 1f, limbSwing, limbSwingAmount);
            flap(leg_right_1, globalSpeed, globalDegree * 0.8f, false, 3.2F, 1f, limbSwing, limbSwingAmount);

            flap(body_main, globalSpeed / 2, globalDegree * 1.2f, false, 0, 0.1f, limbSwing / 2, limbSwingAmount);
            swing(body_main, globalSpeed / 2, globalDegree * 1.2f, false, 0.8F, 0.1f, limbSwing / 3, limbSwingAmount);
            chainWave(new AdvancedModelBox[]{head_main, body_torso, body_main}, globalSpeed * 0.8F, globalDegree, -4, limbSwing, limbSwingAmount * 0.2F);
        }
        else {
            swing(arm_left_1, globalSpeed, globalDegree * 2f, false, 0.8F, 1f, limbSwing, onGround);
            swing(leg_left_1, globalSpeed, globalDegree * 1.8f, false, 1.6F, 1f, limbSwing, onGround);
            swing(arm_right_1, globalSpeed, globalDegree * 2f, false, 2.4F, 1f, limbSwing, onGround);
            swing(leg_right_1, globalSpeed, globalDegree * 1.8f, false, 3.2F, 1f, limbSwing, onGround);
        }

        // Swimming Animation
        if (salamander.swimProgress > 0) {
            this.progressRotation(arm_right_1, salamander.swimProgress, (float) Math.toRadians(-20.87F), (float) Math.toRadians(172.1F), (float) Math.toRadians(-78.26), 20);
            this.progressRotation(arm_left_1, salamander.swimProgress, (float) Math.toRadians(-20.87F), (float) Math.toRadians(-172.1F), (float) Math.toRadians(78.26), 20);
            this.progressRotation(leg_right_1, salamander.swimProgress, (float) Math.toRadians(-15.65), (float) Math.toRadians(174.7), (float) Math.toRadians(-88.7), 20);
            this.progressRotation(leg_left_1, salamander.swimProgress, (float) Math.toRadians(-15.65), (float) Math.toRadians(-174.7), (float) Math.toRadians(88.7), 20);
        }
    }


}