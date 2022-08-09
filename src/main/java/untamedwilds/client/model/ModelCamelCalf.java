package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import untamedwilds.entity.mammal.EntityCamel;

public class ModelCamelCalf extends AdvancedEntityModel<EntityCamel> {
    
    public AdvancedModelBox body_main;
    public AdvancedModelBox arm_left;
    public AdvancedModelBox arm_right;
    public AdvancedModelBox arm_left_2;
    public AdvancedModelBox arm_right_2;
    public AdvancedModelBox neck;
    public AdvancedModelBox leg_left_calf;
    public AdvancedModelBox leg_right_calf;
    public AdvancedModelBox head_main;
    public AdvancedModelBox head_nose;
    public AdvancedModelBox head_nose_1;
    public AdvancedModelBox ear_left;
    public AdvancedModelBox ear_right;
    public AdvancedModelBox leg_left_calf_1;
    public AdvancedModelBox leg_right_calf_1;
    public AdvancedModelBox eye_left;
    public AdvancedModelBox eye_right;

    public ModelCamelCalf() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.ear_right = new AdvancedModelBox(this, 16, 0);
        this.ear_right.setRotationPoint(-3.0F, -3.0F, -1.0F);
        this.ear_right.addBox(-3.0F, -2.0F, 0.0F, 3, 3, 1, 0.0F);
        this.setRotateAngle(ear_right, 0.7285004297824331F, 0.5009094953223726F, 0.7740535232594852F);
        this.head_nose = new AdvancedModelBox(this, 94, 0);
        this.head_nose.setRotationPoint(0.0F, -4.5F, -6.0F);
        this.head_nose.addBox(-3.0F, 0.0F, -6.0F, 6, 3, 6, 0.0F);
        this.setRotateAngle(head_nose, 0.18203784098300857F, 0.0F, 0.0F);
        this.leg_right_calf_1 = new AdvancedModelBox(this, 84, 26);
        this.leg_right_calf_1.mirror = true;
        this.leg_right_calf_1.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.leg_right_calf_1.addBox(-1.5F, 0.0F, -1.5F, 3, 11, 3, 0.0F);
        this.setRotateAngle(leg_right_calf_1, -0.40980330836826856F, 0.0F, 0.0F);
        this.body_main = new AdvancedModelBox(this, 8, 6);
        this.body_main.setRotationPoint(0.0F, 7.3F, 0.0F);
        this.body_main.addBox(-3.0F, -5.0F, -9.0F, 6, 10, 18, 0.0F);
        this.setRotateAngle(body_main, -0.045553093477052F, 0.0F, 0.0F);
        this.leg_left_calf_1 = new AdvancedModelBox(this, 84, 26);
        this.leg_left_calf_1.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.leg_left_calf_1.addBox(-1.5F, 0.0F, -1.5F, 3, 11, 3, 0.0F);
        this.setRotateAngle(leg_left_calf_1, -0.40980330836826856F, 0.0F, 0.0F);
        this.leg_left_calf = new AdvancedModelBox(this, 68, 20);
        this.leg_left_calf.setRotationPoint(-2.5F, -2.5F, 6.5F);
        this.leg_left_calf.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.0F);
        this.setRotateAngle(leg_left_calf, 0.4553564018453205F, 0.0F, 0.0F);
        this.head_nose_1 = new AdvancedModelBox(this, 62, 0);
        this.head_nose_1.setRotationPoint(0.0F, -1.8F, -6.0F);
        this.head_nose_1.addBox(-2.5F, 0.0F, -5.0F, 5, 2, 5, 0.0F);
        this.neck = new AdvancedModelBox(this, 58, 34);
        this.neck.setRotationPoint(0.0F, 0.0F, -9.2F);
        this.neck.addBox(-2.5F, -2.5F, -8.5F, 5, 5, 10, 0.0F);
        this.setRotateAngle(neck, -0.9560913642424937F, 0.0F, 0.0F);
        this.arm_right = new AdvancedModelBox(this, 84, 20);
        this.arm_right.mirror = true;
        this.arm_right.setRotationPoint(-2.5F, 0.05F, -7.99F);
        this.arm_right.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
        this.setRotateAngle(arm_right, 0.045553093477052F, 0.0F, 0.0F);
        this.arm_left = new AdvancedModelBox(this, 84, 20);
        this.arm_left.setRotationPoint(2.5F, 0.05F, -7.99F);
        this.arm_left.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
        this.setRotateAngle(arm_left, 0.045553093477052F, 0.0F, 0.0F);
        this.arm_right_2 = new AdvancedModelBox(this, 84, 27);
        this.arm_right_2.mirror = true;
        this.arm_right_2.setRotationPoint(0, 7, 0F);
        this.arm_right_2.addBox(-1.5F, 0.0F, -1.5F, 3, 10, 3, 0.0F);
        this.setRotateAngle(arm_right_2, 0.045553093477052F, 0.0F, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 84, 27);
        this.arm_left_2.setRotationPoint(0, 7, 0);
        this.arm_left_2.addBox(-1.5F, 0.0F, -1.5F, 3, 10, 3, 0.0F);
        this.setRotateAngle(arm_left_2, 0.045553093477052F, 0.0F, 0.0F);

        this.leg_right_calf = new AdvancedModelBox(this, 68, 20);
        this.leg_right_calf.mirror = true;
        this.leg_right_calf.setRotationPoint(2.5F, -2.5F, 6.5F);
        this.leg_right_calf.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.0F);
        this.setRotateAngle(leg_right_calf, 0.4553564018453205F, 0.0F, 0.0F);
        this.head_main = new AdvancedModelBox(this, 0, 36);
        this.head_main.setRotationPoint(0.0F, -2.4F, -6.3F);
        this.head_main.addBox(-3.0F, -5F, -6.0F, 6, 5, 6, 0.0F);
        this.setRotateAngle(head_main, 1.2747884856566583F, 0.0F, 0.0F);
        this.ear_left = new AdvancedModelBox(this, 16, 0);
        this.ear_left.setRotationPoint(3.0F, -3.0F, -1.0F);
        this.ear_left.addBox(0.0F, -2.0F, 0.0F, 3, 3, 1, 0.0F);
        this.setRotateAngle(ear_left, 0.7285004297824331F, -0.5009094953223726F, -0.7740535232594852F);
        this.eye_right = new AdvancedModelBox(this, 0, 0);
        this.eye_right.mirror = true;
        this.eye_right.setRotationPoint(-3.01F, -3.0F, -4.0F);
        this.eye_right.addBox(0.0F, -1F, -1.0F, 0, 1, 2, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 0, 0);
        this.eye_left.setRotationPoint(3.01F, -3.0F, -4.0F);
        this.eye_left.addBox(0.0F, -1F, -1.0F, 0, 1, 2, 0.0F);
        this.head_main.addChild(this.ear_right);
        this.head_main.addChild(this.head_nose);
        this.arm_left.addChild(this.arm_left_2);
        this.arm_right.addChild(this.arm_right_2);
        this.leg_right_calf.addChild(this.leg_right_calf_1);
        this.leg_left_calf.addChild(this.leg_left_calf_1);
        this.body_main.addChild(this.leg_left_calf);
        this.head_main.addChild(this.head_nose_1);
        this.body_main.addChild(this.neck);
        this.body_main.addChild(this.arm_right);
        this.body_main.addChild(this.arm_left);
        this.body_main.addChild(this.leg_right_calf);
        this.neck.addChild(this.head_main);
        this.head_main.addChild(this.ear_left);
        this.head_main.addChild(this.eye_left);
        this.head_main.addChild(this.eye_right);
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
                arm_left,
                arm_right,
                arm_left_2,
                arm_right_2,
                neck,
                leg_left_calf,
                leg_left_calf_1,
                leg_right_calf,
                leg_right_calf_1,
                head_main,
                head_nose,
                head_nose_1,
                ear_left, 
                ear_right,
                eye_left,
                eye_right
        );
    }

    public void setupAnim(EntityCamel camel, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        // Breathing Animation
        this.body_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(body_main, 0.4F * 1.5f, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right_calf, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left_calf, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);

        // Blinking Animation
        if (!camel.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-1F, -2F, -2);
            this.eye_left.setRotationPoint(1F, -2.0F, -2.0F);
        }

        // Head Tracking Animation
        if (!camel.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, neck);
            this.faceTarget(netHeadYaw, headPitch, 2, head_main);
        }

        // Movement Animation
        if (camel.canMove()) {
            flap(body_main, 0.5F, 0.2F,false, 0, 0, limbSwing, limbSwingAmount);
            flap(neck, 0.5F, 0.2F,true, 0.2F, 0, limbSwing, limbSwingAmount);
            bob(arm_right, 0.5F, 0.8F, false, limbSwing, limbSwingAmount);
            walk(arm_right, 0.5f, 1, true, 0F, 0f, limbSwing, limbSwingAmount);
            walk(arm_right_2, 0.5f, 0.6f, true, 0.2F, 0.2f, limbSwing, limbSwingAmount);
            bob(arm_left, 0.5F, 0.8F, false, limbSwing, limbSwingAmount);
            walk(arm_left, 0.5f, 1, true, 2.4F, 0f, limbSwing, limbSwingAmount);
            walk(arm_left_2, 0.5f, 0.6f, true, 2.6F, 0.2f, limbSwing, limbSwingAmount);
            bob(leg_right_calf, 0.5F, 0.8F, false, limbSwing, limbSwingAmount);
            walk(leg_right_calf, 0.5f, 1, true, 0.2F, 0f, limbSwing, limbSwingAmount);
            walk(leg_right_calf_1, 0.5f, 0.6f, true, 0.4F, 0.2f, limbSwing, limbSwingAmount);
            bob(leg_left_calf, 0.5F, 0.8F, false, limbSwing, limbSwingAmount);
            walk(leg_left_calf, 0.5f, 1, true, 2.6F, 0f, limbSwing, limbSwingAmount);
            walk(leg_left_calf_1, 0.5f, 0.6f, true, 2.8F, 0.2f, limbSwing, limbSwingAmount);

        }

        // Sitting Animation
        if (camel.sitProgress > 0) {
            this.progressPosition(body_main, camel.sitProgress, 0F, 17.3F, 0F, 40);
            this.progressRotation(neck, camel.sitProgress, (float) Math.toRadians(-54.78), 0, 0, 40);
            this.progressRotation(arm_right, camel.sitProgress, (float) Math.toRadians(-10.43F), 0, 0, 40);
            this.progressPosition(arm_right_2, camel.sitProgress, 0F, 7F, -1.7F, 40);
            this.progressRotation(arm_right_2, camel.sitProgress, (float) Math.toRadians(104.35), (float) Math.toRadians(2.61), 0.0F, 40);
            this.progressRotation(arm_left, camel.sitProgress, (float) Math.toRadians(-10.43F), 0, 0, 40);
            this.progressPosition(arm_left_2, camel.sitProgress, 0F, 7F, -1.7F, 40);
            this.progressRotation(arm_left_2, camel.sitProgress, (float) Math.toRadians(104.35), (float) Math.toRadians(-2.61), 0.0F, 40);
            this.progressRotation(leg_right_calf, camel.sitProgress, (float) Math.toRadians(18.26), 0.0F, 0.0F, 40);
            this.progressRotation(leg_right_calf_1, camel.sitProgress, (float) Math.toRadians(-112.17), 0.0F, 0.0F, 40);
            this.progressRotation(leg_left_calf, camel.sitProgress, (float) Math.toRadians(18.26), 0.0F, 0.0F, 40);
            this.progressRotation(leg_left_calf_1, camel.sitProgress, (float) Math.toRadians(-112.17), 0.0F, 0.0F, 40);
        }

        // Sleeping Animation
        if (camel.sleepProgress > 0) {
            this.progressPosition(body_main, camel.sleepProgress, 0F, 17.3F, 0F, 40);
            this.progressRotation(neck, camel.sleepProgress, (float) Math.toRadians(39.13), 0, 0, 40);
            this.progressPosition(head_main, camel.sleepProgress, 0F, 2.6F, -8.3F, 40);
            this.progressRotation(head_main, camel.sleepProgress, (float) Math.toRadians(-33.91), 0.0F, 0.0F, 40);
            this.progressRotation(arm_right, camel.sleepProgress, (float) Math.toRadians(-10.43F), 0, 0, 40);
            this.progressPosition(arm_right_2, camel.sleepProgress, 0F, 7F, -1.7F, 40);
            this.progressRotation(arm_right_2, camel.sleepProgress, (float) Math.toRadians(104.35), (float) Math.toRadians(2.61), 0.0F, 40);
            this.progressRotation(arm_left, camel.sleepProgress, (float) Math.toRadians(-10.43F), 0, 0, 40);
            this.progressPosition(arm_left_2, camel.sleepProgress, 0F, 7F, -1.7F, 40);
            this.progressRotation(arm_left_2, camel.sleepProgress, (float) Math.toRadians(104.35), (float) Math.toRadians(-2.61), 0.0F, 40);
            this.progressRotation(leg_right_calf, camel.sleepProgress, (float) Math.toRadians(18.26), 0.0F, 0.0F, 40);
            this.progressRotation(leg_right_calf_1, camel.sleepProgress, (float) Math.toRadians(-112.17), 0.0F, 0.0F, 40);
            this.progressRotation(leg_left_calf, camel.sleepProgress, (float) Math.toRadians(18.26), 0.0F, 0.0F, 40);
            this.progressRotation(leg_left_calf_1, camel.sleepProgress, (float) Math.toRadians(-112.17), 0.0F, 0.0F, 40);
        }
    }
}
