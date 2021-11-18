package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import untamedwilds.entity.mammal.EntityBison;

// TODO: Missing sleep/sit animations
public class ModelBisonCalf extends AdvancedEntityModel<EntityBison> {
    
    public AdvancedModelBox body_main;
    public AdvancedModelBox leg_left_thigh;
    public AdvancedModelBox leg_right_thigh;
    public AdvancedModelBox tail;
    public AdvancedModelBox head_neck;
    public AdvancedModelBox arm_right_1;
    public AdvancedModelBox arm_left_1;
    public AdvancedModelBox leg_left_calf;
    public AdvancedModelBox leg_right_calf;
    public AdvancedModelBox head_main;
    public AdvancedModelBox head_ear_right;
    public AdvancedModelBox head_ear_left;
    public AdvancedModelBox eye_left;
    public AdvancedModelBox eye_right;
    public AdvancedModelBox arm_right_2;
    public AdvancedModelBox arm_left_2;

    public ModelBisonCalf() {
        this.textureWidth = 128;
        this.textureHeight = 64;

        this.head_neck = new AdvancedModelBox(this, 16, 15);
        this.head_neck.setRotationPoint(0.0F, -1.0F, -4.5F);
        this.head_neck.addBox(-2.0F, -3.5F, -6.0F, 4, 6, 5, 0.0F);
        this.setRotateAngle(head_neck, -0.22759093446006054F, 0.0F, 0.0F);
        this.leg_right_calf = new AdvancedModelBox(this, 86, 28);
        this.leg_right_calf.mirror = true;
        this.leg_right_calf.setRotationPoint(-0.01F, 2.5F, 1.5F);
        this.leg_right_calf.addBox(-1.5F, 0.0F, -1.0F, 3, 8, 3, 0.0F);
        this.arm_right_1 = new AdvancedModelBox(this, 0, 0);
        this.arm_right_1.mirror = true;
        this.arm_right_1.setRotationPoint(-2.0F, 0.0F, -5.5F);
        this.arm_right_1.addBox(-1.5F, 0.0F, -2.0F, 3, 7, 4, 0.0F);
        this.setRotateAngle(arm_right_1, 0.136659280431156F, 0.0F, 0.0F);
        this.leg_left_thigh = new AdvancedModelBox(this, 62, 26);
        this.leg_left_thigh.setRotationPoint(2.5F, 0.6F, 5.2F);
        this.leg_left_thigh.addBox(-1.5F, -3.5F, -3.5F, 3, 8, 6, 0.0F);
        this.arm_left_1 = new AdvancedModelBox(this, 0, 0);
        this.arm_left_1.setRotationPoint(2.0F, 0.0F, -5.5F);
        this.arm_left_1.addBox(-1.5F, 0.0F, -2.0F, 3, 7, 4, 0.0F);
        this.setRotateAngle(arm_left_1, 0.136659280431156F, 0.0F, 0.0F);
        this.body_main = new AdvancedModelBox(this, 88, 42);
        this.body_main.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.body_main.addBox(-3.0F, -5.0F, -7.0F, 6, 8, 14, 0.0F);
        this.head_ear_left = new AdvancedModelBox(this, 18, 32);
        this.head_ear_left.mirror = true;
        this.head_ear_left.setRotationPoint(2.5F, -2.0F, -1.4F);
        this.head_ear_left.addBox(-1.0F, -1.0F, 0.0F, 3, 2, 1, 0.0F);
        this.setRotateAngle(head_ear_left, 0.18203784098300857F, -0.18203784098300857F, 0.136659280431156F);
        this.leg_left_calf = new AdvancedModelBox(this, 86, 28);
        this.leg_left_calf.setRotationPoint(0.01F, 2.5F, 1.5F);
        this.leg_left_calf.addBox(-1.5F, 0.0F, -1.0F, 3, 8, 3, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 0, 30);
        this.eye_left.setRotationPoint(2.51F, -1.0F, -3.0F);
        this.eye_left.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.eye_right = new AdvancedModelBox(this, 0, 30);
        this.eye_right.setRotationPoint(-2.51F, -1.0F, -3.0F);
        this.eye_right.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.arm_right_2 = new AdvancedModelBox(this, 0, 12);
        this.arm_right_2.mirror = true;
        this.arm_right_2.setRotationPoint(0.0F, 6.3F, 0.0F);
        this.arm_right_2.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(arm_right_2, -0.136659280431156F, 0.0F, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 0, 12);
        this.arm_left_2.setRotationPoint(0.0F, 6.3F, 0.0F);
        this.arm_left_2.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(arm_left_2, -0.136659280431156F, 0.0F, 0.0F);
        this.tail = new AdvancedModelBox(this, 65, 1);
        this.tail.setRotationPoint(0.0F, -5.0F, 7.0F);
        this.tail.addBox(-1.5F, 0.0F, 0.0F, 3, 10, 0, 0.0F);
        this.setRotateAngle(tail, 0.18203784098300857F, 0.0F, 0.0F);
        this.head_main = new AdvancedModelBox(this, 68, 51);
        this.head_main.setRotationPoint(0.0F, 1.7F, -5.0F);
        this.head_main.addBox(-2.5F, -4.5F, -5.0F, 5, 8, 5, 0.0F);
        this.setRotateAngle(head_main, -0.36425021489121656F, 0.0F, 0.0F);
        this.leg_right_thigh = new AdvancedModelBox(this, 62, 26);
        this.leg_right_thigh.mirror = true;
        this.leg_right_thigh.setRotationPoint(-2.5F, 0.6F, 5.2F);
        this.leg_right_thigh.addBox(-1.5F, -3.5F, -3.5F, 3, 8, 6, 0.0F);
        this.head_ear_right = new AdvancedModelBox(this, 18, 32);
        this.head_ear_right.setRotationPoint(-2.5F, -2.0F, -1.4F);
        this.head_ear_right.addBox(-2.0F, -1.0F, 0.0F, 3, 2, 1, 0.0F);
        this.setRotateAngle(head_ear_right, 0.18203784098300857F, 0.18203784098300857F, -0.136659280431156F);
        this.body_main.addChild(this.head_neck);
        this.leg_right_thigh.addChild(this.leg_right_calf);
        this.body_main.addChild(this.arm_right_1);
        this.body_main.addChild(this.leg_left_thigh);
        this.body_main.addChild(this.arm_left_1);
        this.head_main.addChild(this.head_ear_left);
        this.leg_left_thigh.addChild(this.leg_left_calf);
        this.head_main.addChild(this.eye_left);
        this.head_main.addChild(this.eye_right);
        this.arm_right_1.addChild(this.arm_right_2);
        this.arm_left_1.addChild(this.arm_left_2);
        this.body_main.addChild(this.tail);
        this.head_neck.addChild(this.head_main);
        this.body_main.addChild(this.leg_right_thigh);
        this.head_main.addChild(this.head_ear_right);
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
                leg_left_thigh,
                leg_right_thigh,
                tail,
                arm_right_1,
                arm_left_1,
                head_neck,
                arm_right_2,
                arm_left_2,
                head_main,
                head_ear_right,
                head_ear_left,
                eye_left,
                eye_right,
                leg_left_calf,
                leg_right_calf
        );
    }

    public void setRotationAngles(EntityBison bison, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        // Breathing Animation
        this.body_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(body_main, 0.4F * 1.5f, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right_1, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left_1, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right_thigh, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left_thigh, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);

        // Blinking Animation
        if (!bison.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-1F, -2F, -2);
            this.eye_left.setRotationPoint(1F, -2.0F, -2.0F);
        }

        // Head Tracking Animation
        if (!bison.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, head_main);
        }

        // Movement Animation
        if (bison.canMove()) {
            this.arm_right_1.rotateAngleX = MathHelper.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
            this.arm_left_1.rotateAngleX = MathHelper.cos(limbSwing * 0.5F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leg_left_thigh.rotateAngleX = MathHelper.cos(limbSwing * 0.5F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leg_right_thigh.rotateAngleX = MathHelper.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
        }

        // Sitting Animation
        if (bison.sitProgress > 0) {
            this.progressPosition(body_main, bison.sitProgress, 0.0F, 17.5F, 0.0F, 40);
            this.progressRotation(head_neck, bison.sitProgress, (float) Math.toRadians(-33.91F), 0, 0, 40);
            this.progressRotation(head_main, bison.sitProgress, (float) Math.toRadians(-2.61F), 0, 0, 40);
            this.progressPosition(arm_right_1, bison.sitProgress, -4.5F, 0.2F, -2.0F, 40);
            this.progressRotation(arm_right_1, bison.sitProgress, (float) Math.toRadians(-65.22F), 0, 0, 40);
            this.progressRotation(arm_right_2, bison.sitProgress, (float) Math.toRadians(135.65F), 0.0F, (float) Math.toRadians(15.65F), 40);
            this.progressPosition(arm_left_1, bison.sitProgress, 4.5F, 0.2F, -2.0F, 40);
            this.progressRotation(arm_left_1, bison.sitProgress, (float) Math.toRadians(-65.22F), 0, 0, 40);this.progressRotation(arm_left_2, bison.sitProgress, (float) Math.toRadians(86.09F), 0.0F, (float) Math.toRadians(-5.22F), 40);
            this.progressRotation(arm_left_2, bison.sitProgress, (float) Math.toRadians(135.65F), 0.0F, (float) Math.toRadians(15.65F), 40);
            this.progressRotation(leg_right_thigh, bison.sitProgress, (float) Math.toRadians(-73.04F), (float) Math.toRadians(15.65F), 0, 40);
            this.progressRotation(leg_right_calf, bison.sitProgress, (float) Math.toRadians(-10.43F), 0.0F, (float) Math.toRadians(-10.43F), 40);
            this.progressRotation(leg_left_thigh, bison.sitProgress, (float) Math.toRadians(-73.04F), (float) Math.toRadians(-15.65F), 0, 40);
            this.progressRotation(leg_left_calf, bison.sitProgress, (float) Math.toRadians(-10.43F), 0.0F, (float) Math.toRadians(10.43F), 40);
        }

        // Sleeping Animation
        if (bison.sleepProgress > 0) {
            this.progressPosition(body_main, bison.sitProgress, 0.0F, 17.5F, 0.0F, 40);
            this.progressPosition(arm_right_1, bison.sitProgress, -4.5F, 0.2F, -2.0F, 40);
            this.progressRotation(arm_right_1, bison.sitProgress, (float) Math.toRadians(-65.22F), 0, 0, 40);
            this.progressRotation(arm_right_2, bison.sitProgress, (float) Math.toRadians(135.65F), 0.0F, (float) Math.toRadians(15.65F), 40);
            this.progressPosition(arm_left_1, bison.sitProgress, 4.5F, 0.2F, -2.0F, 40);
            this.progressRotation(arm_left_1, bison.sitProgress, (float) Math.toRadians(-65.22F), 0, 0, 40);this.progressRotation(arm_left_2, bison.sitProgress, (float) Math.toRadians(86.09F), 0.0F, (float) Math.toRadians(-5.22F), 40);
            this.progressRotation(arm_left_2, bison.sitProgress, (float) Math.toRadians(135.65F), 0.0F, (float) Math.toRadians(15.65F), 40);
            this.progressRotation(leg_right_thigh, bison.sitProgress, (float) Math.toRadians(-73.04F), (float) Math.toRadians(15.65F), 0, 40);
            this.progressRotation(leg_right_calf, bison.sitProgress, (float) Math.toRadians(-10.43F), 0.0F, (float) Math.toRadians(-10.43F), 40);
            this.progressRotation(leg_left_thigh, bison.sitProgress, (float) Math.toRadians(-73.04F), (float) Math.toRadians(-15.65F), 0, 40);
            this.progressRotation(leg_left_calf, bison.sitProgress, (float) Math.toRadians(-10.43F), 0.0F, (float) Math.toRadians(10.43F), 40);
        }
    }
}
