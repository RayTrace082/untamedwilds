package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import untamedwilds.entity.amphibian.EntityNewt;

public class ModelNewt extends AdvancedEntityModel<EntityNewt> {

    private final AdvancedModelBox body_main;
    private final AdvancedModelBox head_main;
    private final AdvancedModelBox body_hip;
    private final AdvancedModelBox arm_right;
    private final AdvancedModelBox arm_left;
    private final AdvancedModelBox body_crest;
    private final AdvancedModelBox gill_l_1;
    private final AdvancedModelBox gill_l_2;
    private final AdvancedModelBox gill_r_1;
    private final AdvancedModelBox gill_r_2;
    private final AdvancedModelBox tail_1;
    private final AdvancedModelBox leg_right;
    private final AdvancedModelBox leg_left;
    private final AdvancedModelBox tail_2;
    private final AdvancedModelBox tail_1_crest;

    public ModelNewt() {
        this.texWidth = 32;
        this.texHeight = 16;
        this.leg_left = new AdvancedModelBox(this, 14, 5);
        this.leg_left.setRotationPoint(1.0F, 0.0F, 0.5F);
        this.leg_left.addBox(0.0F, -0.5F, -1.0F, 3, 1, 2, 0.0F);
        this.setRotateAngle(leg_left, 0.0F, -0.4553564018453205F, 0.5759586531581287F);
        this.body_crest = new AdvancedModelBox(this, 0, 0);
        this.body_crest.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.body_crest.addBox(0.0F, -2.0F, -3.0F, 0, 2, 6, 0.0F);
        this.tail_1_crest = new AdvancedModelBox(this, 12, 4);
        this.tail_1_crest.setRotationPoint(0.0F, 0.0F, 2.5F);
        this.tail_1_crest.addBox(0.0F, -2.0F, -2.5F, 0, 2, 5, 0.0F);
        this.tail_1 = new AdvancedModelBox(this, 20, 10);
        this.tail_1.setRotationPoint(0.0F, -0.2F, 1.5F);
        this.tail_1.addBox(-1.0F, -0.5F, 0.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(tail_1, -0.24434609527920614F, 0.0F, 0.0F);
        this.tail_2 = new AdvancedModelBox(this, 22, 4);
        this.tail_2.setRotationPoint(0.0F, 0.0F, 3.6F);
        this.tail_2.addBox(-0.5F, -0.5F, 0.0F, 1, 2, 4, 0.0F);
        this.setRotateAngle(tail_2, 0.136659280431156F, 0.0F, 0.0F);
        this.arm_right = new AdvancedModelBox(this, 14, 5);
        this.arm_right.mirror = true;
        this.arm_right.setRotationPoint(-1.0F, 0.0F, -2.0F);
        this.arm_right.addBox(-3.0F, -0.5F, -1.0F, 3, 1, 2, 0.0F);
        this.setRotateAngle(arm_right, 0.0F, -0.5009094953223726F, -0.5759586531581287F);
        this.gill_l_2 = new AdvancedModelBox(this, 0, 0);
        this.gill_l_2.setRotationPoint(1.5F, -0.3F, -0.5F);
        this.gill_l_2.addBox(0.0F, -0.5F, 0.0F, 2, 1, 0, 0.0F);
        this.setRotateAngle(gill_l_2, 0.0F, -0.5009094953223726F, -0.18203784098300857F);
        this.leg_right = new AdvancedModelBox(this, 14, 5);
        this.leg_right.mirror = true;
        this.leg_right.setRotationPoint(-1.0F, 0.0F, 0.5F);
        this.leg_right.addBox(-3.0F, -0.5F, -1.0F, 3, 1, 2, 0.0F);
        this.setRotateAngle(leg_right, 0.0F, 0.4553564018453205F, -0.5759586531581287F);
        this.arm_left = new AdvancedModelBox(this, 14, 5);
        this.arm_left.setRotationPoint(1.0F, 0.0F, -2.0F);
        this.arm_left.addBox(0.0F, -0.5F, -1.0F, 3, 1, 2, 0.0F);
        this.setRotateAngle(arm_left, 0.0F, 0.5009094953223726F, 0.6373942428283291F);
        this.gill_r_2 = new AdvancedModelBox(this, 0, 0);
        this.gill_r_2.mirror = true;
        this.gill_r_2.setRotationPoint(-1.5F, -0.3F, -0.5F);
        this.gill_r_2.addBox(-2.0F, -0.5F, 0.0F, 2, 1, 0, 0.0F);
        this.setRotateAngle(gill_r_2, 0.0F, 0.5009094953223726F, 0.18203784098300857F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 22.3F, 0.0F);
        this.body_main.addBox(-1.5F, -1.0F, -4.0F, 3, 2, 4, 0.0F);
        this.body_hip = new AdvancedModelBox(this, 14, 0);
        this.body_hip.setRotationPoint(0.0F, 0.0F, 1.5F);
        this.body_hip.addBox(-1.51F, -1.0F, -1.5F, 3, 2, 3, 0.0F);
        this.gill_r_1 = new AdvancedModelBox(this, 0, 0);
        this.gill_r_1.mirror = true;
        this.gill_r_1.setRotationPoint(-1.5F, 0.3F, -0.5F);
        this.gill_r_1.addBox(-2.0F, -0.5F, 0.0F, 2, 1, 0, 0.0F);
        this.setRotateAngle(gill_r_1, 0.0F, 0.5009094953223726F, -0.22759093446006054F);
        this.head_main = new AdvancedModelBox(this, 0, 10);
        this.head_main.setRotationPoint(0.0F, -0.3F, -2.8F);
        this.head_main.addBox(-1.5F, -1.01F, -4.0F, 3, 2, 4, 0.0F);
        this.setRotateAngle(head_main, 0.136659280431156F, 0.0F, 0.0F);
        this.head_main.scaleX = 1.05F;
        this.gill_l_1 = new AdvancedModelBox(this, 0, 0);
        this.gill_l_1.setRotationPoint(1.5F, 0.3F, -0.5F);
        this.gill_l_1.addBox(0.0F, -0.5F, 0.0F, 2, 1, 0, 0.0F);
        this.setRotateAngle(gill_l_1, 0.0F, -0.5009094953223726F, 0.22759093446006054F);
        this.body_hip.addChild(this.leg_left);
        this.body_main.addChild(this.body_crest);
        this.tail_1.addChild(this.tail_1_crest);
        this.body_hip.addChild(this.tail_1);
        this.tail_1.addChild(this.tail_2);
        this.body_main.addChild(this.arm_right);
        this.head_main.addChild(this.gill_l_2);
        this.body_hip.addChild(this.leg_right);
        this.body_main.addChild(this.arm_left);
        this.head_main.addChild(this.gill_r_2);
        this.body_main.addChild(this.body_hip);
        this.head_main.addChild(this.gill_r_1);
        this.body_main.addChild(this.head_main);
        this.head_main.addChild(this.gill_l_1);

        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main, head_main, body_hip, arm_right, arm_left, leg_left, leg_right, body_crest, tail_1_crest,
                tail_2, tail_1, gill_l_1, gill_l_2, gill_r_1, gill_r_2
        );
    }

    public void setupAnim(EntityNewt newt, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();
        float globalSpeed = 0.8f;
        float globalDegree = 1.0f;
        limbSwingAmount = Math.min(0.6F, limbSwingAmount);

        // Breathing Animation
        this.body_hip.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.body_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.head_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);

        // Head Tracking Animation
        if (!newt.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, head_main);
        }

        // Pitch/Yaw handler
        if (newt.isInWater() && !newt.isOnGround()) {
            this.setRotateAngle(body_main, newt.getXRot() * ((float) Math.PI / 180F), 0, 0);
        }
        this.head_main.rotateAngleY = Mth.rotLerp((float) 0.05, this.head_main.rotateAngleY, newt.offset);
        this.body_hip.rotateAngleY = Mth.rotLerp((float) 0.05, this.body_hip.rotateAngleY, -1F * newt.offset);
        this.tail_2.rotateAngleY = Mth.rotLerp((float) 0.05, this.tail_2.rotateAngleY, -2F * newt.offset);


        // Movement Animation
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{head_main, body_main, body_hip, tail_1, tail_2};
        chainSwing(bodyParts, globalSpeed * 1.4F, globalDegree * 1.2F, -4, limbSwing, limbSwingAmount * 0.3F);
        float onGround = Math.min(0.8F, limbSwingAmount * (newt.isOnGround() ? 2 : 1));
        if (newt.isInWater()) {
            flap(arm_left, globalSpeed, globalDegree, false, 0.8F, 1f, limbSwing, limbSwingAmount);
            flap(leg_left, globalSpeed, globalDegree * 0.8f, false, 1.6F, 1f, limbSwing, limbSwingAmount);
            flap(arm_right, globalSpeed, globalDegree, false, 2.4F, 1f, limbSwing, limbSwingAmount);
            flap(leg_right, globalSpeed, globalDegree * 0.8f, false, 3.2F, 1f, limbSwing, limbSwingAmount);

            flap(body_main, globalSpeed / 2, globalDegree * 1.2f, false, 0, 0.1f, limbSwing / 2, limbSwingAmount);
            swing(body_main, globalSpeed / 2, globalDegree * 1.2f, false, 0.8F, 0.1f, limbSwing / 3, limbSwingAmount);
            chainWave(new AdvancedModelBox[]{head_main, body_main, body_hip}, globalSpeed * 0.8F, globalDegree, -4, limbSwing, limbSwingAmount * 0.2F);
        }
        else {
            swing(arm_left, globalSpeed, globalDegree * 1.2f, false, 0.8F, 1f, limbSwing, onGround);
            swing(leg_left, globalSpeed, globalDegree * 1.1f, false, 1.6F, 1f, limbSwing, onGround);
            swing(arm_right, globalSpeed, globalDegree * 1.2f, false, 2.4F, 1f, limbSwing, onGround);
            swing(leg_right, globalSpeed, globalDegree * 1.1f, false, 3.2F, 1f, limbSwing, onGround);
        }

        // Sleeping Animation
        if (newt.swimProgress > 0) {
            this.progressRotation(arm_right, newt.swimProgress, (float) Math.toRadians(41.74F), (float) Math.toRadians(70.43F), (float) Math.toRadians(-36.52F), 20);
            this.progressRotation(arm_left, newt.swimProgress, (float) Math.toRadians(41.74F), (float) Math.toRadians(-70.43F), (float) Math.toRadians(36.52F), 20);
            this.progressRotation(leg_right, newt.swimProgress, (float) Math.toRadians(49.57F), (float) Math.toRadians(73.04F), (float) Math.toRadians(-33.91F), 20);
            this.progressRotation(leg_left, newt.swimProgress, (float) Math.toRadians(49.57F), (float) Math.toRadians(-73.04F), (float) Math.toRadians(33.91F), 20);
        }
    }


}