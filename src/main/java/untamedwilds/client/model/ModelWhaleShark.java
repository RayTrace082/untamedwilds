package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import untamedwilds.entity.fish.EntityWhaleShark;

public class ModelWhaleShark extends AdvancedEntityModel<EntityWhaleShark> {

    private final AdvancedModelBox body_main;
    private final AdvancedModelBox head_snout_1;
    private final AdvancedModelBox body_tail_1;
    private final AdvancedModelBox fin_pectoral_right;
    private final AdvancedModelBox fin_pectoral_left;
    private final AdvancedModelBox head_snout_2;
    private final AdvancedModelBox body_tail_2;
    private final AdvancedModelBox fin_dorsal;
    private final AdvancedModelBox fin_pelvic_right;
    private final AdvancedModelBox fin_pelvic_right_1;
    private final AdvancedModelBox body_tail_3;
    private final AdvancedModelBox fin_dorsal_2;
    private final AdvancedModelBox fin_anal;
    private final AdvancedModelBox fin_caudal_top;
    private final AdvancedModelBox fin_caudal_bottom;
    private final AdvancedModelBox head_jaw;

    private static AdvancedModelBox[] bodyParts_passive;

    public ModelWhaleShark() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.fin_pelvic_right_1 = new AdvancedModelBox(this, 40, 102);
        this.fin_pelvic_right_1.setRotationPoint(3.0F, 4.5F, 5.0F);
        this.fin_pelvic_right_1.addBox(-1.0F, 0.0F, -3.5F, 2, 7, 7, 0.0F);
        this.setRotateAngle(fin_pelvic_right_1, 0.31869712141416456F, 0.0F, -1.0927506446736497F);
        this.head_snout_2 = new AdvancedModelBox(this, 0, 31);
        this.head_snout_2.mirror = true;
        this.head_snout_2.setRotationPoint(0.0F, 5.6F, 0.0F);
        this.head_snout_2.addBox(-10.0F, -4.0F, -24.0F, 20, 8, 24, 0.0F);
        this.setRotateAngle(head_snout_2, -0.136659280431156F, 0.0F, 0.0F);this.body_tail_3 = new AdvancedModelBox(this, 74, 0);
        this.body_tail_3.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.body_tail_3.addBox(-2.0F, -3.0F, -0.5F, 4, 6, 13, 0.0F);
        this.setRotateAngle(body_tail_3, 0.045553093477052F, 0.0F, 0.0F);
        this.fin_pelvic_right = new AdvancedModelBox(this, 40, 102);
        this.fin_pelvic_right.mirror = true;
        this.fin_pelvic_right.setRotationPoint(-3.0F, 4.5F, 5.0F);
        this.fin_pelvic_right.addBox(-1.0F, 0.0F, -3.5F, 2, 7, 7, 0.0F);
        this.setRotateAngle(fin_pelvic_right, 0.31869712141416456F, 0.0F, 1.0927506446736497F);
        this.head_jaw = new AdvancedModelBox(this, 44, 31);
        this.head_jaw.setRotationPoint(0.0F, 2F, 0.0F);
        this.head_jaw.addBox(-9.0F, 0.0F, -24.0F, 18, 0, 24, 0.0F);
        this.body_tail_1 = new AdvancedModelBox(this, 58, 82);
        this.body_tail_1.setRotationPoint(0.0F, 1.0F, 15.0F);
        this.body_tail_1.addBox(-5.0F, -6.0F, 0.0F, 10, 12, 20, 0.0F);
        this.setRotateAngle(body_tail_1, -0.136659280431156F, 0.0F, 0.0F);
        this.fin_dorsal = new AdvancedModelBox(this, 0, 0);
        this.fin_dorsal.setRotationPoint(0.0F, -8.0F, -4.0F);
        this.fin_dorsal.addBox(-1.0F, -11.0F, 1.0F, 2, 15, 10, 0.0F);
        this.setRotateAngle(fin_dorsal, -0.4553564018453205F, 0.0F, 0.0F);
        this.body_tail_2 = new AdvancedModelBox(this, 0, 102);
        this.body_tail_2.setRotationPoint(0.0F, 0.0F, 20.0F);
        this.body_tail_2.addBox(-4.0F, -5.0F, -0.5F, 8, 10, 14, 0.0F);
        this.setRotateAngle(body_tail_2, 0.045553093477052F, 0.0F, 0.0F);
        this.fin_anal = new AdvancedModelBox(this, 74, 114);
        this.fin_anal.setRotationPoint(0.0F, 2.5F, 4.0F);
        this.fin_anal.addBox(-1.0F, 0.0F, -2.0F, 2, 6, 5, 0.0F);
        this.setRotateAngle(fin_anal, 0.8196066167365371F, 0.0F, 0.0F);
        this.fin_caudal_top = new AdvancedModelBox(this, 108, 0);
        this.fin_caudal_top.setRotationPoint(0.0F, -1.0F, 7.0F);
        this.fin_caudal_top.addBox(-1.0F, -24.0F, 0.0F, 2, 24, 8, 0.0F);
        this.setRotateAngle(fin_caudal_top, -0.8651597102135892F, 0.0F, 0.0F);
        this.body_main = new AdvancedModelBox(this, 0, 64);
        this.body_main.setRotationPoint(0.0F, 18.0F, -20.0F);
        this.body_main.addBox(-7.0F, -7.0F, -8.0F, 14, 14, 24, 0.0F);
        this.setRotateAngle(body_main, 0.045553093477052F, 0.0F, 0.0F);
        this.fin_pectoral_left = new AdvancedModelBox(this, 100, 70);
        this.fin_pectoral_left.mirror = true;
        this.fin_pectoral_left.setRotationPoint(3.0F, 3.2F, -7.0F);
        this.fin_pectoral_left.addBox(-1.0F, 0.0F, -2.0F, 2, 20, 9, 0.0F);
        this.setRotateAngle(fin_pectoral_left, 0.31869712141416456F, 0.0F, -1.0927506446736497F);
        this.fin_pectoral_right = new AdvancedModelBox(this, 100, 70);
        this.fin_pectoral_right.setRotationPoint(-3.0F, 3.2F, -7.0F);
        this.fin_pectoral_right.addBox(-1.0F, 0.0F, -2.0F, 2, 20, 9, 0.0F);
        this.setRotateAngle(fin_pectoral_right, 0.31869712141416456F, 0.0F, 1.0927506446736497F);
        this.fin_dorsal_2 = new AdvancedModelBox(this, 58, 114);
        this.fin_dorsal_2.setRotationPoint(0.0F, -2.5F, 5.0F);
        this.fin_dorsal_2.addBox(-1.0F, -6.0F, -2.0F, 2, 6, 5, 0.0F);
        this.setRotateAngle(fin_dorsal_2, -0.6373942428283291F, 0.0F, 0.0F);
        this.head_snout_1 = new AdvancedModelBox(this, 1, 1);
        this.head_snout_1.setRotationPoint(0.0F, -4.1F, -7.5F);
        this.head_snout_1.addBox(-10.0F, -3.0F, -23.8F, 20, 5, 25, 0.0F);
        this.setRotateAngle(head_snout_1, 0.045553093477052F, 0.0F, 0.0F);
        this.head_snout_1.scaleX = 1.01F;
        this.fin_caudal_bottom = new AdvancedModelBox(this, 108, 34);
        this.fin_caudal_bottom.setRotationPoint(0.0F, -8.0F, 8.0F);
        this.fin_caudal_bottom.addBox(-1.0F, -10.0F, 0.0F, 2, 10, 8, 0.0F);
        this.setRotateAngle(fin_caudal_bottom, -1.5707963267948966F, 0.0F, 0.0F);
        this.body_tail_1.addChild(this.fin_pelvic_right_1);
        this.head_snout_1.addChild(this.head_snout_2);
        this.body_tail_2.addChild(this.body_tail_3);
        this.body_tail_1.addChild(this.fin_pelvic_right);
        this.body_main.addChild(this.body_tail_1);
        this.body_tail_1.addChild(this.fin_dorsal);
        this.body_tail_1.addChild(this.body_tail_2);
        this.body_tail_2.addChild(this.fin_anal);
        this.body_tail_3.addChild(this.fin_caudal_top);
        this.body_main.addChild(this.fin_pectoral_left);
        this.body_main.addChild(this.fin_pectoral_right);
        this.body_tail_2.addChild(this.fin_dorsal_2);
        this.head_snout_2.addChild(this.head_jaw);
        this.body_main.addChild(this.head_snout_1);
        this.fin_caudal_top.addChild(this.fin_caudal_bottom);

        bodyParts_passive = new AdvancedModelBox[]{head_snout_1, body_main, body_tail_1, body_tail_2, body_tail_3};
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main,
            head_snout_1,
            body_tail_1,
            fin_pectoral_right,
            fin_pectoral_left,
            head_snout_2,
            head_jaw,
            body_tail_2,
            fin_dorsal,
            fin_pelvic_right,
            fin_pelvic_right_1,
            body_tail_3,
            fin_dorsal_2,
            fin_anal,
            fin_caudal_top,
            fin_caudal_bottom
        );
    }

    public void setRotationAngles(EntityWhaleShark whale_shark, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();

        float globalSpeed = 0.6f;
        float globalDegree = 1f;

        // Breathing Animation
        this.head_snout_1.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.08F) + 0.01F, (float) (1F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);
        this.head_snout_2.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.08F), (float) (1F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);

        // Pitch/Yaw handler
        if (whale_shark.isInWater()) {
            this.setRotateAngle(body_main, MathHelper.clamp(whale_shark.rotationPitch, -20, 20) * ((float) Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        float partialTicks = ageInTicks - whale_shark.ticksExisted;
        float renderYaw = (float)whale_shark.getMovementOffsets(0, partialTicks)[0];
        chainSwing(bodyParts_passive, globalSpeed * 0.8F, globalDegree * 0.75F, -5, limbSwing / 4, Math.max(0.2F, limbSwingAmount));
        this.body_tail_1.rotateAngleY += smartClamp((float)whale_shark.getMovementOffsets(15, partialTicks)[0] - renderYaw, -40, 40) * ((float) Math.PI / 180F);
        this.body_tail_2.rotateAngleY += smartClamp((float)whale_shark.getMovementOffsets(17, partialTicks)[0] - renderYaw, -40, 40) * ((float) Math.PI / 180F);
        this.body_main.rotateAngleZ += smartClamp((float)whale_shark.getMovementOffsets(7, partialTicks)[0] - renderYaw, -20, 20) * ((float) Math.PI / 180F);
    }

    // This wrapper handles cases where the returned MovementOffset is a large negative angle, which throws off the clamp function
    public float smartClamp(float angle, int min, int max) {
        float val = Math.abs(angle);
        if (val > 180) {
            angle = 360 - val;
        }
        return MathHelper.clamp(angle, min, max);
    }
}

