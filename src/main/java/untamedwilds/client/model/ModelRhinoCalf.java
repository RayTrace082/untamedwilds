package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import untamedwilds.entity.mammal.EntityRhino;

public class ModelRhinoCalf extends AdvancedEntityModel<EntityRhino> {
    
    public AdvancedModelBox body_main;
    public AdvancedModelBox leg_right;
    public AdvancedModelBox leg_left;
    public AdvancedModelBox body_torso;
    public AdvancedModelBox body_head;
    public AdvancedModelBox arm_right;
    public AdvancedModelBox arm_left;
    public AdvancedModelBox horn_back;
    public AdvancedModelBox ear_right;
    public AdvancedModelBox ear_left;
    public AdvancedModelBox eye_left;
    public AdvancedModelBox eye_right;

    public ModelRhinoCalf() {
        this.texWidth = 128;
        this.texHeight = 64;

        this.leg_left = new AdvancedModelBox(this, 79, 46);
        this.leg_left.setRotationPoint(-1.99F, 7.0F, 3.01F);
        this.leg_left.addBox(-2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F);
        this.eye_right = new AdvancedModelBox(this, 48, 44);
        this.eye_right.setRotationPoint(-2.51F, -1.5F, -3.0F);
        this.eye_right.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.body_head = new AdvancedModelBox(this, 97, 28);
        this.body_head.setRotationPoint(0.0F, -1.0F, -7.0F);
        this.body_head.addBox(-2.5F, -3.0F, -8.0F, 5, 6, 9, 0.0F);
        this.setRotateAngle(body_head, 0.6373942428283291F, 0.0F, 0.0F);
        this.ear_left = new AdvancedModelBox(this, 0, 37);
        this.ear_left.setRotationPoint(2.5F, -2.0F, -5.0F);
        this.ear_left.addBox(-1.0F, -3.0F, 4.5F, 2, 3, 1, 0.0F);
        this.setRotateAngle(ear_left, 0.0F, -0.22759093446006054F, 0.5462880558742251F);
        this.body_main = new AdvancedModelBox(this, 88, 44);
        this.body_main.setRotationPoint(0.0F, 12.2F, 3.0F);
        this.body_main.addBox(-4.0F, 0.0F, -7.0F, 8, 8, 12, 0.0F);
        this.leg_right = new AdvancedModelBox(this, 79, 46);
        this.leg_right.mirror = true;
        this.leg_right.setRotationPoint(1.99F, 7.0F, 3.01F);
        this.leg_right.addBox(-2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F);
        this.body_torso = new AdvancedModelBox(this, 79, 8);
        this.body_torso.setRotationPoint(0.0F, 4.0F, -5.0F);
        this.body_torso.addBox(-4.5F, -5.0F, -7.0F, 9, 10, 8, 0.0F);
        this.setRotateAngle(body_torso, -0.22759093446006054F, 0.0F, 0.0F);
        this.arm_right = new AdvancedModelBox(this, 79, 46);
        this.arm_right.mirror = true;
        this.arm_right.setRotationPoint(1.99F, 3.6F, -1.99F);
        this.arm_right.addBox(-2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F);
        this.setRotateAngle(arm_right, 0.22759093446006054F, 0.0F, 0.0F);
        this.ear_right = new AdvancedModelBox(this, 0, 37);
        this.ear_right.setRotationPoint(-2.5F, -2.0F, -5.0F);
        this.ear_right.addBox(-1.0F, -3.0F, 4.5F, 2, 3, 1, 0.0F);
        this.setRotateAngle(ear_right, 0.0F, 0.22759093446006054F, -0.5462880558742251F);
        this.arm_left = new AdvancedModelBox(this, 79, 46);
        this.arm_left.setRotationPoint(-1.99F, 3.6F, -1.99F);
        this.arm_left.addBox(-2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F);
        this.setRotateAngle(arm_left, 0.22759093446006054F, 0.0F, 0.0F);
        this.horn_back = new AdvancedModelBox(this, 11, 14);
        this.horn_back.setRotationPoint(0.0F, 0.0F, -5.7F);
        this.horn_back.addBox(-1.0F, -4.0F, -1.5F, 2, 2, 3, 0.0F);
        this.setRotateAngle(horn_back, 0.31869712141416456F, 0.0F, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 48, 44);
        this.eye_left.setRotationPoint(2.51F, -1.5F, -3.0F);
        this.eye_left.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.body_main.addChild(this.leg_left);
        this.body_head.addChild(this.eye_right);
        this.body_torso.addChild(this.body_head);
        this.body_head.addChild(this.ear_left);
        this.body_main.addChild(this.leg_right);
        this.body_main.addChild(this.body_torso);
        this.body_torso.addChild(this.arm_right);
        this.body_head.addChild(this.ear_right);
        this.body_torso.addChild(this.arm_left);
        this.body_head.addChild(this.horn_back);
        this.body_head.addChild(this.eye_left);
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }


    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main, leg_right, leg_left, body_torso, body_head, arm_right, arm_left, horn_back,
            ear_right, ear_left, eye_left, eye_right
        );
    }

    public void setupAnim(EntityRhino rhino, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        // Breathing Animation
        this.body_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(body_main, 0.4F * 1.5f, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);

        // Blinking Animation
        if (!rhino.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-1F, -2F, -2);
            this.eye_left.setRotationPoint(1F, -2.0F, -2.0F);
        }

        // Head Tracking Animation
        if (!rhino.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, body_head);
        }

        // Movement Animation
        if (rhino.canMove()) {
            this.arm_right.rotateAngleX = Mth.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
            this.arm_left.rotateAngleX = Mth.cos(limbSwing * 0.5F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leg_left.rotateAngleX = Mth.cos(limbSwing * 0.5F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leg_right.rotateAngleX = Mth.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
        }
    }


}