package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.fish.EntitySunfish;
import untamedwilds.entity.mammal.EntityHippo;
import untamedwilds.entity.mammal.EntityManatee;

public class ModelManatee extends AdvancedEntityModel<EntityManatee> {

    public AdvancedModelBox body_main;
    public AdvancedModelBox body_torso;
    public AdvancedModelBox body_tail_1;
    public AdvancedModelBox head_face;
    public AdvancedModelBox arm_right;
    public AdvancedModelBox arm_left;
    public AdvancedModelBox head_snout_1;
    public AdvancedModelBox head_jaw;
    public AdvancedModelBox head_snout_2;
    public AdvancedModelBox body_tail_2;
    public AdvancedModelBox body_tail_3;
    public AdvancedModelBox body_tail_4;
    public AdvancedModelBox body_tail_5;
    public AdvancedModelBox body_tail_6;

    private static AdvancedModelBox[] bodyParts_passive;
    private static AdvancedModelBox[] bodyParts_head;

    public ModelManatee() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.head_jaw = new AdvancedModelBox(this, 0, 56);
        this.head_jaw.setRotationPoint(0.0F, 2.0F, -4.0F);
        this.head_jaw.addBox(-2.0F, 0.0F, -4.0F, 4, 1, 4, 0.0F);
        this.setRotateAngle(head_jaw, -0.136659280431156F, 0.0F, 0.0F);
        this.body_tail_4 = new AdvancedModelBox(this, 84, 14);
        this.body_tail_4.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.body_tail_4.addBox(-6.0F, -1.0F, 0.0F, 12, 2, 10, 0.0F);
        this.head_snout_1 = new AdvancedModelBox(this, 28, 42);
        this.head_snout_1.setRotationPoint(0.0F, -0.99F, -6.0F);
        this.head_snout_1.addBox(-3.5F, -3.0F, -4.0F, 7, 6, 4, 0.0F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.body_main.addBox(-7.0F, -6.0F, -6.0F, 14, 12, 12, 0.0F);
        this.arm_right = new AdvancedModelBox(this, 52, 34);
        this.arm_right.mirror = true;
        this.arm_right.setRotationPoint(-6.0F, 4.5F, -5.0F);
        this.arm_right.addBox(-6.0F, -0.5F, -2.0F, 6, 2, 5, 0.0F);
        this.setRotateAngle(arm_right, 0.045553093477052F, 0.0F, -2.367539130330308F);
        this.body_tail_1 = new AdvancedModelBox(this, 52, 0);
        this.body_tail_1.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.body_tail_1.addBox(-6.0F, -5.0F, 0.0F, 12, 10, 8, 0.0F);
        this.setRotateAngle(body_tail_1, -0.091106186954104F, 0.0F, 0.0F);
        this.body_torso = new AdvancedModelBox(this, 0, 24);
        this.body_torso.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.body_torso.addBox(-6.0F, -5.0F, -7.0F, 12, 10, 8, 0.0F);
        this.setRotateAngle(body_torso, 0.045553093477052F, 0.0F, 0.0F);
        this.arm_left = new AdvancedModelBox(this, 52, 34);
        this.arm_left.setRotationPoint(6.0F, 4.5F, -5.0F);
        this.arm_left.addBox(0.0F, -0.5F, -2.0F, 6, 2, 5, 0.0F);
        this.setRotateAngle(arm_left, 0.045553093477052F, 0.0F, 2.367539130330308F);
        this.body_tail_3 = new AdvancedModelBox(this, 92, 0);
        this.body_tail_3.setRotationPoint(0.0F, -0.5F, 7.0F);
        this.body_tail_3.addBox(-3.0F, -2.5F, 0.0F, 6, 5, 8, 0.0F);
        this.setRotateAngle(body_tail_3, 0.091106186954104F, 0.0F, 0.0F);
        this.head_face = new AdvancedModelBox(this, 0, 42);
        this.head_face.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.head_face.addBox(-4.0F, -4.0F, -6.0F, 8, 7, 6, 0.0F);
        this.setRotateAngle(head_face, 0.045553093477052F, 0.0F, 0.0F);
        this.body_tail_5 = new AdvancedModelBox(this, 90, 26);
        this.body_tail_5.mirror = true;
        this.body_tail_5.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.body_tail_5.addBox(-14.0F, -1.0F, -1.0F, 12, 2, 6, 0.0F);
        this.setRotateAngle(body_tail_5, 0.0F, 0.5918411493512771F, 0.0F);
        this.body_tail_6 = new AdvancedModelBox(this, 90, 26);
        this.body_tail_6.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.body_tail_6.addBox(2.0F, -1.0F, -1.0F, 12, 2, 6, 0.0F);
        this.setRotateAngle(body_tail_6, 0.0F, -0.5918411493512771F, 0.0F);
        this.head_snout_2 = new AdvancedModelBox(this, 30, 54);
        this.head_snout_2.setRotationPoint(0.0F, 0.82F, -0.8F);
        this.head_snout_2.addBox(-4.0F, -3.0F, -4.0F, 8, 6, 4, 0.0F);
        this.setRotateAngle(head_snout_2, -0.22759093446006054F, 0.0F, 0.0F);
        this.body_tail_2 = new AdvancedModelBox(this, 52, 18);
        this.body_tail_2.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.body_tail_2.addBox(-5.0F, -4.0F, 0.0F, 10, 7, 8, 0.0F);
        this.setRotateAngle(body_tail_2, -0.091106186954104F, 0.0F, 0.0F);
        this.head_face.addChild(this.head_jaw);
        this.body_tail_3.addChild(this.body_tail_4);
        this.head_face.addChild(this.head_snout_1);
        this.body_torso.addChild(this.arm_right);
        this.body_main.addChild(this.body_tail_1);
        this.body_main.addChild(this.body_torso);
        this.body_torso.addChild(this.arm_left);
        this.body_tail_2.addChild(this.body_tail_3);
        this.body_torso.addChild(this.head_face);
        this.body_tail_3.addChild(this.body_tail_5);
        this.body_tail_3.addChild(this.body_tail_6);
        this.head_snout_1.addChild(this.head_snout_2);
        this.body_tail_1.addChild(this.body_tail_2);

        bodyParts_passive = new AdvancedModelBox[]{body_main, body_tail_1, body_tail_2, body_tail_3};
        bodyParts_head = new AdvancedModelBox[]{head_face, head_jaw, head_snout_1, head_snout_2};
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
        body_main,
        body_torso,
        body_tail_1,
        head_face,
        arm_right,
        arm_left,
        head_snout_1,
        head_jaw,
        head_snout_2,
        body_tail_2,
        body_tail_3,
        body_tail_4,
        body_tail_5,
        body_tail_6
        );
    }

    public void setupAnim(EntityManatee manatee, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ageInTicks - (float)manatee.tickCount / 20;
        resetToDefaultPose();

        float globalSpeed = 0.6f;
        float globalDegree = 0.6f;

        if (manatee.isEating()) {
            this.head_snout_2.setScale((float) (1.0F + Math.sin(ageInTicks / 6) * 0.08F + Math.sin(ageInTicks / 2) * 0.1F), (float) (1.0F + Math.sin(ageInTicks / 8) * 0.04F), 1.0F);
        }

        // Pitch/Yaw handler
        if (manatee.isInWater()) {
            this.setRotateAngle(body_main, manatee.getXRot() * ((float) Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        float partialTicks = ageInTicks - manatee.tickCount;
        float renderYaw = (float)manatee.getMovementOffsets(0, partialTicks)[0];
        this.body_tail_1.rotateAngleY += smartClamp((float)manatee.getMovementOffsets(15, partialTicks)[0] - renderYaw, -40, 40) * ((float) Math.PI / 180F) * 0.3F;
        this.body_tail_2.rotateAngleY += smartClamp((float)manatee.getMovementOffsets(17, partialTicks)[0] - renderYaw, -40, 40) * ((float) Math.PI / 180F) * 0.3F;
        this.body_main.rotateAngleZ += smartClamp((float)manatee.getMovementOffsets(7, partialTicks)[0] - renderYaw, -20, 20) * ((float) Math.PI / 180F) * 0.3F;
        walk(body_main, globalSpeed * 0.2F, globalDegree * 0.2f, true, 0, 0, f, Math.max(0.4F, limbSwingAmount));
        walk(body_tail_1, globalSpeed * 0.2F, globalDegree * 0.6f, true, 1.2f, 0, f, Math.max(0.4F, limbSwingAmount));
        walk(body_tail_2, globalSpeed * 0.2F, globalDegree * 0.6f, true, 2.4f, 0, f, Math.max(0.4F, limbSwingAmount));
        walk(body_tail_3, globalSpeed * 0.2F, globalDegree * 0.4f, true, 3.6f, 0, f, Math.max(0.4F, limbSwingAmount));
    }

    // This wrapper handles cases where the returned MovementOffset is a large negative angle, which throws off the clamp function
    public float smartClamp(float angle, int min, int max) {
        float val = Math.abs(angle);
        if (val > 180) {
            angle = 360 - val;
        }
        return Mth.clamp(angle, min, max);
    }
}
