package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import untamedwilds.entity.arthropod.EntityTarantula;

public class ModelTarantula extends AdvancedEntityModel<EntityTarantula> {

    public AdvancedModelBox body_main;
    public AdvancedModelBox abdomen;
    public AdvancedModelBox legR4;
    public AdvancedModelBox legR3;
    public AdvancedModelBox legR2;
    public AdvancedModelBox legR1;
    public AdvancedModelBox legL4;
    public AdvancedModelBox legL3;
    public AdvancedModelBox legL2;
    public AdvancedModelBox legL1;
    public AdvancedModelBox legR42;
    public AdvancedModelBox legR32;
    public AdvancedModelBox legR22;
    public AdvancedModelBox legR12;
    public AdvancedModelBox legL42;
    public AdvancedModelBox legL32;
    public AdvancedModelBox legL22;
    public AdvancedModelBox legL12;

    public ModelTarantula() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.legL32 = new AdvancedModelBox(this, 24, 16);
        this.legL32.setRotationPoint(-4.0F, -0.9F, 0.01F);
        this.legL32.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.legL12 = new AdvancedModelBox(this, 0, 16);
        this.legL12.setRotationPoint(-3.5F, -0.9F, -0.01F);
        this.legL12.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(legL12, 0.41887902047863906F, 0.0F, 0.3490658503988659F);
        this.legL1 = new AdvancedModelBox(this, 0, 12);
        this.legL1.setRotationPoint(2.0F, 1.0F, -2.6F);
        this.legL1.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(legL1, 0.13962634015954636F, -2.356194490192345F, -0.3141592653589793F);
        this.legR2 = new AdvancedModelBox(this, 12, 12);
        this.legR2.setRotationPoint(-2.6F, 1.0F, -1.0F);
        this.legR2.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(legR2, -0.2617993877991494F, -0.3141592653589793F, 0.5759586531581287F);
        this.legL3 = new AdvancedModelBox(this, 24, 12);
        this.legL3.setRotationPoint(2.6F, 1.0F, 1.0F);
        this.legL3.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(legL3, -0.3141592653589793F, 2.6878070480712672F, -0.5759586531581287F);
        this.legR1 = new AdvancedModelBox(this, 0, 12);
        this.legR1.setRotationPoint(-2.0F, 1.0F, -2.6F);
        this.legR1.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(legR1, -0.045553093477052F, -0.7853981633974483F, 0.3141592653589793F);
        this.legR32 = new AdvancedModelBox(this, 24, 16);
        this.legR32.setRotationPoint(-4.0F, -0.9F, 0.01F);
        this.legR32.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.legL2 = new AdvancedModelBox(this, 12, 12);
        this.legL2.setRotationPoint(2.6F, 1.0F, -1.0F);
        this.legL2.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(legL2, 0.2617993877991494F, -2.827433388230814F, -0.5759586531581287F);
        this.legR22 = new AdvancedModelBox(this, 12, 16);
        this.legR22.setRotationPoint(-4.0F, -0.9F, -0.01F);
        this.legR22.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.legL4 = new AdvancedModelBox(this, 36, 12);
        this.legL4.setRotationPoint(3.0F, 1.0F, 2.0F);
        this.legL4.addBox(-4.0F, -1.0F, -2.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(legL4, -0.3490658503988659F, 2.2689280275926285F, -0.3490658503988659F);
        this.legR3 = new AdvancedModelBox(this, 24, 12);
        this.legR3.setRotationPoint(-2.6F, 1.0F, 1.0F);
        this.legR3.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(legR3, 0.3141592653589793F, 0.41887902047863906F, 0.5759586531581287F);
        this.legR12 = new AdvancedModelBox(this, 0, 16);
        this.legR12.setRotationPoint(-3.5F, -0.9F, -0.01F);
        this.legR12.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(legR12, -0.41887902047863906F, 0.0F, 0.3490658503988659F);
        this.legR42 = new AdvancedModelBox(this, 36, 16);
        this.legR42.setRotationPoint(-3.5F, 0.0F, 1.01F);
        this.legR42.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(legR42, 0.0F, 0.0F, 0.7853981633974483F);
        this.legL42 = new AdvancedModelBox(this, 36, 16);
        this.legL42.setRotationPoint(-3.5F, 0.0F, -0.99F);
        this.legL42.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(legL42, 0.0F, 0.0F, 0.7853981633974483F);
        this.legL22 = new AdvancedModelBox(this, 12, 16);
        this.legL22.setRotationPoint(-4.0F, -0.9F, -0.01F);
        this.legL22.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body_main.addBox(-3.0F, 0.0F, -3.0F, 6, 3, 6, 0.0F);
        this.legR4 = new AdvancedModelBox(this, 36, 12);
        this.legR4.setRotationPoint(-3.0F, 1.0F, 2.0F);
        this.legR4.addBox(-4.0F, -1.0F, 0.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(legR4, 0.3490658503988659F, 0.8726646259971648F, 0.3490658503988659F);
        this.abdomen = new AdvancedModelBox(this, 24, 0);
        this.abdomen.setRotationPoint(0.0F, 1.0F, 2.5F);
        this.abdomen.addBox(-3.5F, -2.0F, 0.0F, 7, 4, 7, 0.0F);
        this.setRotateAngle(abdomen, -0.17453292519943295F, 0.0F, 0.0F);
        this.legL3.addChild(this.legL32);
        this.legL1.addChild(this.legL12);
        this.body_main.addChild(this.legL1);
        this.body_main.addChild(this.legR2);
        this.body_main.addChild(this.legL3);
        this.body_main.addChild(this.legR1);
        this.legR3.addChild(this.legR32);
        this.body_main.addChild(this.legL2);
        this.legR2.addChild(this.legR22);
        this.body_main.addChild(this.legL4);
        this.body_main.addChild(this.legR3);
        this.legR1.addChild(this.legR12);
        this.legR4.addChild(this.legR42);
        this.legL4.addChild(this.legL42);
        this.legL2.addChild(this.legL22);
        this.body_main.addChild(this.legR4);
        this.body_main.addChild(this.abdomen);
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main, abdomen, legR4, legR3, legR2, legR1, legL4, legL3, legL2, legL1, legR42,
                legR32, legR22, legR12, legL42, legL32, legL22, legL12
        );
    }

    public void setupAnim(EntityTarantula tarantula, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        limbSwing = ageInTicks;
        limbSwingAmount = Math.min(0.4F, limbSwingAmount);

        float globalSpeed = 1.2f;
        float globalDegree = 1.4f;

        this.abdomen.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F));
        bob(body_main, 0.4F * 1.5f, 0.03F, false, ageInTicks / 20, 2);
        bob(legR1, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(legL1, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(legR2, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(legL2, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(legR3, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(legL3, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(legR4, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(legL4, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);

        // Attacking animation
        if (tarantula.aggroProgress != 0) {
            this.progressRotation(legR1, tarantula.aggroProgress, (float)Math.toRadians(-41.74D), 0, (float)Math.toRadians(75.65F), 40);
            this.progressRotation(legL1, tarantula.aggroProgress, (float)Math.toRadians(41.74D), (float)Math.toRadians(-180D), (float)Math.toRadians(-75.65F), 40);
        }
        else {
            animateArthropodLeg(legL1, legL12, globalSpeed, globalDegree, 3, limbSwing, limbSwingAmount);
            animateArthropodLeg(legR1, legR12, globalSpeed, globalDegree, 0, limbSwing, limbSwingAmount);
        }

        if (!tarantula.isClimbing()) {
            animateArthropodLeg(legL2, legL22, globalSpeed, globalDegree, 4, limbSwing, limbSwingAmount);
            animateArthropodLeg(legL3, legL32, globalSpeed, globalDegree, 0, limbSwing, limbSwingAmount);
            animateArthropodLeg(legL4, legL42, globalSpeed, globalDegree, 1, limbSwing, limbSwingAmount);

            animateArthropodLeg(legR2, legR22, globalSpeed, globalDegree, 2, limbSwing, limbSwingAmount);
            animateArthropodLeg(legR3, legR32, globalSpeed, globalDegree, 3, limbSwing, limbSwingAmount);
            animateArthropodLeg(legR4, legR42, globalSpeed, globalDegree, 5, limbSwing, limbSwingAmount);
        }

        // Climbing animation
        this.progressRotation(body_main, tarantula.climbProgress, 90 * ((float)Math.PI / 180F) * (tarantula.invertClimbing ? 1 : -1), body_main.rotateAngleY, body_main.rotateAngleZ , 20);
        if (tarantula.isClimbing() && Math.abs(tarantula.getDeltaMovement().y()) > 0.05F) {
            float swing = tarantula.getDeltaMovement().y() != 0 ? 0.3F : limbSwingAmount;
            animateArthropodLeg(legL2, legL22, globalSpeed, globalDegree, 4, ageInTicks / 3, swing);
            animateArthropodLeg(legL3, legL32, globalSpeed, globalDegree, 0, ageInTicks / 3, swing);
            animateArthropodLeg(legL4, legL42, globalSpeed, globalDegree, 1, ageInTicks / 3, swing);
            animateArthropodLeg(legR2, legR22, globalSpeed, globalDegree, 2, ageInTicks / 3, swing);
            animateArthropodLeg(legR3, legR32, globalSpeed, globalDegree, 3, ageInTicks / 3, swing);
            animateArthropodLeg(legR4, legR42, globalSpeed, globalDegree, 5, ageInTicks / 3, swing);
        }
    }

    private void animateArthropodLeg(AdvancedModelBox limb_1, AdvancedModelBox limb_2, float speed, float degree, int offset, float limbSwing, float limbSwingAmount) {
        swing(limb_1, speed, degree * 1.2f, false, offset, 0.1f, limbSwing, limbSwingAmount);
        flap(limb_1, speed, degree * 0.8f, true, offset + 1.5F, 0.2f, limbSwing, limbSwingAmount);
        flap(limb_2, speed, degree * 0.8f, true, offset + 1.5F, 0f, limbSwing, limbSwingAmount);
    }


}