package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import untamedwilds.entity.mammal.EntityOpossum;

public class ModelOpossum extends AdvancedEntityModel<EntityOpossum> {
   
    public AdvancedModelBox body_main;
    public AdvancedModelBox arm_left_1;
    public AdvancedModelBox arm_right_1;
    public AdvancedModelBox head_head;
    public AdvancedModelBox leg_right_1;
    public AdvancedModelBox leg_left_1;
    public AdvancedModelBox joey_1;
    public AdvancedModelBox joey_2;
    public AdvancedModelBox joey_3;
    public AdvancedModelBox arm_left_2;
    public AdvancedModelBox arm_left_paw;
    public AdvancedModelBox arm_right_2;
    public AdvancedModelBox arm_right_paw;
    public AdvancedModelBox head_ear_left;
    public AdvancedModelBox head_ear_right;
    public AdvancedModelBox head_snout;
    public AdvancedModelBox eye_left;
    public AdvancedModelBox eye_right;
    public AdvancedModelBox head_jaw;
    public AdvancedModelBox whisker_right;
    public AdvancedModelBox whisker_left;
    public AdvancedModelBox leg_right_2;
    public AdvancedModelBox leg_right_paw;
    public AdvancedModelBox leg_left_2;
    public AdvancedModelBox leg_left_paw;
    public AdvancedModelBox tail_1;
    public AdvancedModelBox tail_2;

    private final ModelAnimator animator;

    public ModelOpossum() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.joey_1 = new AdvancedModelBox(this, 40, 0);
        this.joey_1.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.joey_1.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.setRotateAngle(joey_1, 0.0F, 0.0F, -0.5462880558742251F);
        this.head_jaw = new AdvancedModelBox(this, 6, 17);
        this.head_jaw.setRotationPoint(0.0F, 1.01F, -2.0F);
        this.head_jaw.addBox(-1.0F, 0.0F, -4.0F, 2, 1, 4, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 30, 23);
        this.arm_left_2.mirror = true;
        this.arm_left_2.setRotationPoint(0.0F, 3.5F, 0.01F);
        this.arm_left_2.addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(arm_left_2, 0.0F, 0.0F, 0.136659280431156F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 19.0F, -3.8F);
        this.body_main.addBox(-2.5F, -3.3F, -2.0F, 5, 5, 10, 0.0F);
        this.whisker_right = new AdvancedModelBox(this, 0, 0);
        this.whisker_right.setRotationPoint(-0.6F, 0.0F, -3.0F);
        this.whisker_right.addBox(-3.0F, -1.5F, 0.0F, 3, 3, 0, 0.0F);
        this.setRotateAngle(whisker_right, 0.0F, -0.22759093446006054F, -0.27314402793711257F);
        this.arm_left_paw = new AdvancedModelBox(this, 0, 4);
        this.arm_left_paw.mirror = true;
        this.arm_left_paw.setRotationPoint(0.5F, 3.02F, 1.0F);
        this.arm_left_paw.addBox(-1.5F, 0.0F, -3.0F, 3, 0, 4, 0.0F);
        this.leg_right_2 = new AdvancedModelBox(this, 46, 24);
        this.leg_right_2.setRotationPoint(0.0F, 3.1F, 1.2F);
        this.leg_right_2.addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(leg_right_2, 0.27314402793711257F, 0.0F, -0.13962634015954636F);
        this.eye_left = new AdvancedModelBox(this, 0, 17);
        this.eye_left.setRotationPoint(0.01F, 0.0F, -3.01F);
        this.eye_left.addBox(0.0F, -1.0F, -1.0F, 2, 1, 1, 0.0F);
        this.tail_2 = new AdvancedModelBox(this, 14, 17);
        this.tail_2.setRotationPoint(0.0F, 0.1F, 3.9F);
        this.tail_2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(tail_2, 0.1366592804F, 0.0F, 0.0F);
        this.tail_2.scaleX = 0.75F;
        this.arm_right_2 = new AdvancedModelBox(this, 30, 23);
        this.arm_right_2.setRotationPoint(0.0F, 3.5F, 0.01F);
        this.arm_right_2.addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(arm_right_2, 0.0F, 0.0F, -0.136659280431156F);
        this.leg_left_1 = new AdvancedModelBox(this, 46, 15);
        this.leg_left_1.mirror = true;
        this.leg_left_1.setRotationPoint(1.5F, -1.3F, 6.8F);
        this.leg_left_1.addBox(-1.5F, -1.0F, -2.0F, 3, 5, 4, 0.0F);
        this.setRotateAngle(leg_left_1, -0.27314402793711257F, 0.0F, -0.13962634015954636F);
        this.head_ear_left = new AdvancedModelBox(this, 0, 21);
        this.head_ear_left.setRotationPoint(1.3F, -1.3F, -1.5F);
        this.head_ear_left.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(head_ear_left, -0.091106186954104F, -0.091106186954104F, 0.8651597102135892F);
        this.whisker_left = new AdvancedModelBox(this, 0, 0);
        this.whisker_left.mirror = true;
        this.whisker_left.setRotationPoint(0.6F, 0.0F, -3.0F);
        this.whisker_left.addBox(0.0F, -1.5F, 0.0F, 3, 3, 0, 0.0F);
        this.setRotateAngle(whisker_left, 0.0F, 0.22759093446006054F, 0.27314402793711257F);
        this.head_head = new AdvancedModelBox(this, 2, 22);
        this.head_head.setRotationPoint(0.0F, -1.0F, -1.9F);
        this.head_head.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(head_head, 0.091106186954104F, 0.0F, 0.0F);
        this.leg_left_paw = new AdvancedModelBox(this, 0, 4);
        this.leg_left_paw.mirror = true;
        this.leg_left_paw.setRotationPoint(0.5F, 3.01F, 1.0F);
        this.leg_left_paw.addBox(-1.5F, 0.0F, -3.0F, 3, 0, 4, 0.0F);
        this.setRotateAngle(leg_left_paw, 0.0036651914291880917F, 0.0F, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 18, 26);
        this.head_snout.setRotationPoint(0.0F, 1.0F, -2.0F);
        this.head_snout.addBox(-1.0F, -1.0F, -4.0F, 2, 2, 2, 0.0F);
        this.arm_right_1 = new AdvancedModelBox(this, 28, 15);
        this.arm_right_1.setRotationPoint(-1.5F, -1.55F, 0.2F);
        this.arm_right_1.addBox(-1.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F);
        this.setRotateAngle(arm_right_1, 0.0F, 0.0F, 0.136659280431156F);
        this.leg_right_1 = new AdvancedModelBox(this, 46, 15);
        this.leg_right_1.setRotationPoint(-1.5F, -1.3F, 6.8F);
        this.leg_right_1.addBox(-1.5F, -1.0F, -2.0F, 3, 5, 4, 0.0F);
        this.setRotateAngle(leg_right_1, -0.27314402793711257F, 0.0F, 0.13962634015954636F);
        this.leg_right_paw = new AdvancedModelBox(this, 0, 4);
        this.leg_right_paw.setRotationPoint(-0.5F, 3.01F, 1.0F);
        this.leg_right_paw.addBox(-1.5F, 0.0F, -3.0F, 3, 0, 4, 0.0F);
        this.setRotateAngle(leg_right_paw, 0.0036651914291880917F, 0.0F, 0.0F);
        this.joey_2 = new AdvancedModelBox(this, 40, 0);
        this.joey_2.mirror = true;
        this.joey_2.setRotationPoint(0.0F, -5.0F, 3.0F);
        this.joey_2.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.setRotateAngle(joey_2, 0.0F, 0.0F, 0.5462880558742251F);
        this.joey_3 = new AdvancedModelBox(this, 40, 0);
        this.joey_3.setRotationPoint(0.0F, -5.0F, 6.0F);
        this.joey_3.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.setRotateAngle(joey_3, 0.0F, 0.0F, -0.5462880558742251F);
        this.tail_1 = new AdvancedModelBox(this, 24, 2);
        this.tail_1.setRotationPoint(0.0F, -2.0F, 7.5F);
        this.tail_1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(tail_1, -0.4098033084F, 0.0F, 0.0F);
        this.arm_right_paw = new AdvancedModelBox(this, 0, 4);
        this.arm_right_paw.setRotationPoint(-0.5F, 3.02F, 1.0F);
        this.arm_right_paw.addBox(-1.5F, 0.0F, -3.0F, 3, 0, 4, 0.0F);
        this.eye_right = new AdvancedModelBox(this, 0, 17);
        this.eye_right.mirror = true;
        this.eye_right.setRotationPoint(-0.01F, 0.0F, -3.01F);
        this.eye_right.addBox(-2.0F, -1.0F, -1.0F, 2, 1, 1, 0.0F);
        this.head_ear_right = new AdvancedModelBox(this, 0, 21);
        this.head_ear_right.mirror = true;
        this.head_ear_right.setRotationPoint(-1.3F, -1.2F, -1.5F);
        this.head_ear_right.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(head_ear_right, -0.091106186954104F, 0.091106186954104F, -0.8651597102135892F);
        this.arm_left_1 = new AdvancedModelBox(this, 28, 15);
        this.arm_left_1.mirror = true;
        this.arm_left_1.setRotationPoint(1.5F, -1.55F, 0.2F);
        this.arm_left_1.addBox(-1.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F);
        this.setRotateAngle(arm_left_1, 0.0F, 0.0F, -0.136659280431156F);
        this.leg_left_2 = new AdvancedModelBox(this, 46, 24);
        this.leg_left_2.mirror = true;
        this.leg_left_2.setRotationPoint(0.0F, 3.1F, 1.2F);
        this.leg_left_2.addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(leg_left_2, 0.27314402793711257F, 0.0F, 0.13962634015954636F);
        this.body_main.addChild(this.joey_1);
        this.head_head.addChild(this.head_jaw);
        this.arm_left_1.addChild(this.arm_left_2);
        this.head_snout.addChild(this.whisker_right);
        this.arm_left_2.addChild(this.arm_left_paw);
        this.leg_right_1.addChild(this.leg_right_2);
        this.head_head.addChild(this.eye_left);
        this.tail_1.addChild(this.tail_2);
        this.arm_right_1.addChild(this.arm_right_2);
        this.body_main.addChild(this.leg_left_1);
        this.head_head.addChild(this.head_ear_left);
        this.head_snout.addChild(this.whisker_left);
        this.body_main.addChild(this.head_head);
        this.leg_left_2.addChild(this.leg_left_paw);
        this.head_head.addChild(this.head_snout);
        this.body_main.addChild(this.arm_right_1);
        this.body_main.addChild(this.leg_right_1);
        this.leg_right_2.addChild(this.leg_right_paw);
        this.body_main.addChild(this.joey_2);
        this.body_main.addChild(this.joey_3);
        this.body_main.addChild(this.tail_1);
        this.arm_right_2.addChild(this.arm_right_paw);
        this.head_head.addChild(this.eye_right);
        this.head_head.addChild(this.head_ear_right);
        this.body_main.addChild(this.arm_left_1);
        this.leg_left_1.addChild(this.leg_left_2);
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main, arm_left_1, arm_right_1, head_head, leg_right_1, leg_left_1, tail_1,
    joey_1, joey_2, joey_3, arm_left_2, arm_left_paw, arm_right_2, arm_right_paw, head_ear_left, head_ear_right, head_snout,
    eye_left, eye_right, head_jaw, whisker_right, whisker_left, leg_right_2, leg_right_paw, leg_left_2, leg_left_paw, tail_2);
    }

    private void animate(IAnimatedEntity entityIn) {
        EntityOpossum monitor = (EntityOpossum) entityIn;
        animator.update(monitor);

        animator.setAnimation(EntityOpossum.THREAT_BACK_OFF);
        int invert = 1;
        for (int i = 0; i < 4; i++) {
            animator.startKeyframe(6);
            animator.move(head_snout, 0, -1F, 0);
            this.rotate(animator, head_head, -15.65F, 2.61F * invert, 13.04F * invert);
            this.rotate(animator, head_jaw, 44.35F, 0, 0);
            invert = invert * -1;
            animator.endKeyframe();
        }
        animator.resetKeyframe(6);

        animator.setAnimation(EntityOpossum.IDLE_SCRATCH);
        animator.startKeyframe(8);
        this.rotate(animator, body_main, 0, 7.83F, -7.83F);
        this.rotate(animator, head_head, -23.48F, 20.87F, -39.13F);
        this.rotate(animator, tail_1, -23.48F, -15.65F, 0);
        this.rotate(animator, head_ear_right, -28.70F, 5.22F, -49.57F);

        animator.move(arm_right_1, 0, -0.3F, 0);
        this.rotate(animator, arm_right_1, 0, 0, 13.04F);
        this.rotate(animator, leg_left_1, -15.65F, 0, 2.61F);

        animator.move(leg_right_1, 0, 0, -1);
        this.rotate(animator, leg_right_1, -60F, 18.26F, 8F);
        animator.move(leg_right_paw, 0, -1, -1);
        this.rotate(animator, leg_right_paw, 88.70F, 0, 0);

        int leg_offset = 1;
        for (int i = 0; i < 6; i++) {
            animator.startKeyframe(4);
            this.rotate(animator, body_main, 0, 7.83F, -7.83F);
            this.rotate(animator, head_head, -23.48F, 20.87F - 10 * leg_offset, -39.13F - 10 * leg_offset);
            this.rotate(animator, tail_1, -23.48F, -15.65F, 0);
            this.rotate(animator, head_ear_right, -28.70F, 5.22F, -49.57F);

            animator.move(arm_right_1, 0, -0.3F, 0);
            this.rotate(animator, arm_right_1, 0, 0, 13.04F);
            this.rotate(animator, leg_left_1, -15.65F, 0, 2.61F);

            animator.move(leg_right_1, 0, 0, -1);
            this.rotate(animator, leg_right_1, -80 - 20 * leg_offset, 18.26F, 8F);
            animator.move(leg_right_paw, 0, -1, -1);
            this.rotate(animator, leg_right_paw, 88.70F, 0, 0);
            leg_offset = leg_offset * -1;
            animator.endKeyframe();
        }
        animator.resetKeyframe(8);
    }

    public void setupAnim(EntityOpossum opossum, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();
        animate(opossum);
        float globalSpeed = 1.6f;
        float globalDegree = 1.4f;
        limbSwingAmount = Math.min(0.5F, limbSwingAmount);

        if (opossum.isNoAi()) {
            limbSwing = ageInTicks / 4;
            limbSwingAmount = 0.5F;
        }

        // Breathing Animation
        if (!opossum.isPlayingDead()) {
            this.body_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
            this.head_head.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), 1.0F, 1.0F);
            swing(this.whisker_left, globalSpeed, 0.12F * globalDegree, false, 1F, 0, ageInTicks / 6, 2);
            swing(this.whisker_right, globalSpeed, 0.12F * globalDegree, true, 1F, 0, ageInTicks / 6, 2);//this.head_jaw.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
            AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{tail_1, tail_2};
            chainSwing(bodyParts, 0.3F * globalSpeed, 0.125F * globalDegree, 1F, ageInTicks / 6, 1);
        }
        this.joey_1.showModel = opossum.getJoeys() >= 1;
        this.joey_2.showModel = opossum.getJoeys() >= 2;
        this.joey_3.showModel = opossum.getJoeys() >= 3;

        // Head Tracking Animation
        if (!opossum.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, head_head);
        }

        // Pitch/Yaw handler
        if (opossum.isInWater() && !opossum.isOnGround()) {
            this.setRotateAngle(body_main, opossum.getXRot() * ((float) Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        if (opossum.isInWater()) {
            flap(arm_left_1, globalSpeed, globalDegree, false, 0.8F, 1f, limbSwing, limbSwingAmount);
            flap(leg_left_1, globalSpeed, globalDegree * 0.8f, false, 1.6F, 1f, limbSwing, limbSwingAmount);
            flap(arm_right_1, globalSpeed, globalDegree, false, 2.4F, 1f, limbSwing, limbSwingAmount);
            flap(leg_right_1, globalSpeed, globalDegree * 0.8f, false, 3.2F, 1f, limbSwing, limbSwingAmount);

            flap(body_main, globalSpeed / 2, globalDegree * 1.2f, false, 0, 0.1f, limbSwing / 2, limbSwingAmount);
            swing(body_main, globalSpeed / 2, globalDegree * 1.2f, false, 0.8F, 0.1f, limbSwing / 3, limbSwingAmount);
            chainWave(new AdvancedModelBox[]{head_head, body_main}, globalSpeed * 0.8F, globalDegree, -4, limbSwing, limbSwingAmount * 0.2F);
        }
        else {
            bob(body_main, 0.5F * globalSpeed, 0.8F, true, limbSwing, limbSwingAmount);
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

        // Sitting Animation
        if (opossum.sitProgress != 0) {
            body_main.setScaleY(1 + 0.4F * (float) opossum.sitProgress / 20);
            this.progressPosition(body_main, opossum.sitProgress, 0, 22F, -3.8F, 20);
            this.progressPosition(arm_left_1, opossum.sitProgress, 1.5F, -2.55F, 0.2F, 20);
            this.progressPosition(arm_left_2, opossum.sitProgress, 0, 1.5F, 0.01F, 20);
            this.progressPosition(arm_right_1, opossum.sitProgress, -1.5F, -2.55F, 0.2F, 20);
            this.progressPosition(arm_right_2, opossum.sitProgress, 0, 1.5F, 0.01F, 20);
            this.progressPosition(leg_left_1, opossum.sitProgress, 1.5F, -2.3F, 6.8F, 20);
            this.progressPosition(leg_left_2, opossum.sitProgress, 0, 1, 1.2F, 20);
            this.progressPosition(leg_right_1, opossum.sitProgress, -1.5F, -2.3F, 6.8F, 20);
            this.progressPosition(leg_right_2, opossum.sitProgress, 0, 1, 1.2F, 20);
        }

        // Sleeping Animation
        if (opossum.sleepProgress != 0) {
            this.progressRotation(body_main, opossum.sleepProgress, 0, 0, (float) Math.toRadians(-80.65F), 40);
            this.progressPosition(body_main, opossum.sleepProgress, -1, 22F, -1.8F, 40);
            this.progressRotation(head_head, opossum.sleepProgress, (float) Math.toRadians(20.87F), 0, (float) Math.toRadians(-18.26F), 40);
            if (opossum.isPlayingDead())
                this.progressRotation(head_jaw, opossum.sleepProgress, (float) Math.toRadians(46.96F), 0, (float) Math.toRadians(23.48F), 40);

            this.progressRotation(arm_left_1, opossum.sleepProgress, 0, (float) Math.toRadians(7.83F), (float) Math.toRadians(10.43F), 40);
            this.progressRotation(leg_left_1, opossum.sleepProgress, (float) Math.toRadians(-36.52F), (float) Math.toRadians(2.61F), (float) Math.toRadians(5.22F), 40);
            this.progressRotation(tail_1, opossum.sleepProgress, (float) Math.toRadians(-65.22F), 0, 0, 40);
            this.progressRotation(tail_2, opossum.sleepProgress, (float) Math.toRadians(-44.35F), 0, 0, 40);
        }
    }
}
