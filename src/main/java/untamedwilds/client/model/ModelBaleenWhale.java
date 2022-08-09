package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import untamedwilds.entity.mammal.EntityBaleenWhale;

public class ModelBaleenWhale extends AdvancedEntityModel<EntityBaleenWhale> {
    
    private final AdvancedModelBox main_body;
    private final AdvancedModelBox main_head;
    private final AdvancedModelBox fin_left;
    private final AdvancedModelBox tail_1;
    private final AdvancedModelBox fin_right;
    private final AdvancedModelBox head_mouth_top;
    private final AdvancedModelBox head_jaw_1;
    private final AdvancedModelBox head_mouth_top_1;
    private final AdvancedModelBox head_jaw_2;
    private final AdvancedModelBox fin_dorsal;
    private final AdvancedModelBox tail_2;
    private final AdvancedModelBox tail_3;
    private final AdvancedModelBox tail_left;
    private final AdvancedModelBox tail_right;

    private static AdvancedModelBox[] bodyParts;

    public ModelBaleenWhale() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.tail_3 = new AdvancedModelBox(this, 0, 16);
        this.tail_3.setRotationPoint(0.0F, 0.0F, 30.0F);
        this.tail_3.addBox(-6.0F, -6.0F, 0.0F, 12, 12, 10, 0.0F);
        this.head_mouth_top_1 = new AdvancedModelBox(this, 160, 72);
        this.head_mouth_top_1.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.head_mouth_top_1.addBox(-9.0F, 0.0F, -26.0F, 18, 8, 26, 0.0F);
        this.main_head = new AdvancedModelBox(this, 92, 0);
        this.main_head.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.main_head.addBox(-12.0F, -6.0F, -10.0F, 24, 12, 12, 0.0F);
        this.setRotateAngle(main_head, 0.045553093477052F, 0.0F, 0.0F);
        this.main_head.scaleX = 1.02F;
        this.tail_1 = new AdvancedModelBox(this, 136, 16);
        this.tail_1.setRotationPoint(0.0F, 0.0F, 38.0F);
        this.tail_1.addBox(-10.0F, -9.0F, 0.0F, 20, 18, 38, 0.0F);
        this.main_body = new AdvancedModelBox(this, 0, 0);
        this.main_body.setRotationPoint(0.0F, 12.0F, -32.0F);
        this.main_body.addBox(-12.0F, -10.0F, 0.0F, 24, 22, 44, 0.0F);
        this.tail_right = new AdvancedModelBox(this, 0, 162);
        this.tail_right.mirror = true;
        this.tail_right.setRotationPoint(-1.0F, 0.0F, 7.0F);
        this.tail_right.addBox(-30.0F, -2.0F, -8.0F, 30, 4, 14, 0.0F);
        this.setRotateAngle(tail_right, 0.0F, 0.5918411493512771F, 0.0F);
        this.head_jaw_1 = new AdvancedModelBox(this, 68, 72);
        this.head_jaw_1.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.head_jaw_1.addBox(-12.0F, 4.0F, -42.0F, 24, 8, 44, 0.0F);
        this.setRotateAngle(head_jaw_1, -0.136659280431156F, 0.0F, 0.0F);
        this.head_jaw_1.scaleX = 1.01F;
        this.fin_right = new AdvancedModelBox(this, 164, 0);
        this.fin_right.mirror = true;
        this.fin_right.setRotationPoint(-7.0F, 5.0F, 11.0F);
        this.fin_right.addBox(-28.0F, -2.0F, -6.0F, 28, 4, 12, 0.0F);
        this.setRotateAngle(fin_right, 0.0F, 0.5918411493512771F, -0.6829473363053812F);
        this.head_jaw_2 = new AdvancedModelBox(this, 0, 124);
        this.head_jaw_2.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.head_jaw_2.addBox(-12.0F, -6.0F, -32.0F, 24, 6, 32, 0.0F);
        this.tail_2 = new AdvancedModelBox(this, 114, 124);
        this.tail_2.setRotationPoint(0.0F, 0.0F, 38.0F);
        this.tail_2.addBox(-6.0F, -7.0F, 0.0F, 12, 14, 30, 0.0F);
        this.tail_left = new AdvancedModelBox(this, 0, 162);
        this.tail_left.setRotationPoint(1.0F, 0.0F, 7.0F);
        this.tail_left.addBox(0.0F, -2.0F, -8.0F, 30, 4, 14, 0.0F);
        this.setRotateAngle(tail_left, 0.0F, -0.5918411493512771F, 0.0F);
        this.head_mouth_top = new AdvancedModelBox(this, 0, 66);
        this.head_mouth_top.setRotationPoint(0.0F, -6.0F, -10.0F);
        this.head_mouth_top.addBox(-12.0F, 0.0F, -32.0F, 24, 6, 32, 0.0F);
        this.head_mouth_top.scaleX = 1.01F;
        this.fin_left = new AdvancedModelBox(this, 164, 0);
        this.fin_left.setRotationPoint(7.0F, 5.0F, 11.0F);
        this.fin_left.addBox(0.0F, -2.0F, -6.0F, 28, 4, 12, 0.0F);
        this.setRotateAngle(fin_left, 0.0F, -0.5918411493512771F, 0.6829473363053812F);
        this.fin_dorsal = new AdvancedModelBox(this, 0, 0);
        this.fin_dorsal.setRotationPoint(0.0F, -6.0F, 36.0F);
        this.fin_dorsal.addBox(-2.0F, -8.0F, -4.0F, 4, 8, 8, 0.0F);
        this.setRotateAngle(fin_dorsal, -0.5462880558742251F, 0.0F, 0.0F);
        this.tail_2.addChild(this.tail_3);
        this.head_mouth_top.addChild(this.head_mouth_top_1);
        this.main_body.addChild(this.main_head);
        this.main_body.addChild(this.tail_1);
        this.tail_3.addChild(this.tail_right);
        this.main_head.addChild(this.head_jaw_1);
        this.main_body.addChild(this.fin_right);
        this.head_jaw_1.addChild(this.head_jaw_2);
        this.tail_1.addChild(this.tail_2);
        this.tail_3.addChild(this.tail_left);
        this.main_head.addChild(this.head_mouth_top);
        this.main_body.addChild(this.fin_left);
        this.tail_1.addChild(this.fin_dorsal);
        bodyParts = new AdvancedModelBox[]{main_head, main_body, tail_1, tail_2, tail_3};
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(main_body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(main_body,
            main_head,
            fin_left,
            tail_1,
            fin_right,
            head_mouth_top,
            head_jaw_1,
            head_mouth_top_1,
            head_jaw_2,
            fin_dorsal,
            tail_2,
            tail_3,
            tail_left,
            tail_right
        );
    }

    public void setupAnim(EntityBaleenWhale whale, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();

        float globalSpeed = 0.6f;
        float globalDegree = 1f;

        // Model Parameters
        float fin_scale = whale.hasLongFins() ? 1.7F : 1;
        fin_left.scaleX = fin_scale;
        fin_right.scaleX = fin_scale;

        // Breathing Animation
        this.main_body.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.08F) + 0.01F, (float) (1F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);
        //this.main_head.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.08F), (float) (1F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);
        this.tail_1.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.04F), (float) (1F + Math.sin(ageInTicks / 16) * 0.04F), 1.0F);

        this.main_body.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.main_body.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        //if (whale_shark.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7D) {
        //    this.main_body.rotateAngleX += -0.05F - 0.05F * Mth.cos(ageInTicks * 0.3F);
        //    this.tail_1.rotateAngleX = -0.1F * Mth.cos(ageInTicks * 0.3F);
        //    this.tail_2.rotateAngleX = -0.2F * Mth.cos(ageInTicks * 0.3F);
        //}
        // Pitch/Yaw handler
        if (whale.isInWater()) {
            this.setRotateAngle(main_body, Mth.clamp(whale.getXRot(), -20, 20) * ((float) Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        float partialTicks = ageInTicks - whale.tickCount;
        float renderYaw = (float)whale.getMovementOffsets(0, partialTicks)[0];
        if (!whale.isFeeding())
            chainWave(bodyParts, globalSpeed * 0.3F, globalDegree * 0.6F, -5, limbSwing, limbSwingAmount);
        flap(fin_right, globalSpeed * 0.3F, globalDegree * 0.6F, true, 0, 0, limbSwing, limbSwingAmount);
        flap(fin_left, globalSpeed * 0.3F, globalDegree * 0.6F, false, 0, 0, limbSwing, limbSwingAmount);
        this.tail_1.rotateAngleY += smartClamp((float)whale.getMovementOffsets(15, partialTicks)[0] - renderYaw, -40, 40) * ((float) Math.PI / 180F);
        this.tail_2.rotateAngleY += smartClamp((float)whale.getMovementOffsets(17, partialTicks)[0] - renderYaw, -40, 40) * ((float) Math.PI / 180F);
        //this.main_body.rotateAngleZ += smartClamp((float)whale_shark.getMovementOffsets(7, partialTicks)[0] - renderYaw, -20, 20) * ((float) Math.PI / 180F);

        // Sitting Animation
        //if (whale.gulpProgress != 0) {
            //this.progressPosition(body_5, snake.sitProgress, -4.0F, 23.0F, -3.0F, 20);
            this.progressRotation(head_jaw_1, whale.gulpProgress, (float) Math.toRadians(57.39F), 0, 0, 50);
            this.progressPosition(head_jaw_2, whale.gulpProgress, 0, 6, 0, 50);
            this.head_jaw_1.scaleY = 1 + (float) (0.3 * whale.gulpProgress / 50F);
            this.head_jaw_2.scaleZ = 1 + (float) (0.3 * whale.gulpProgress / 50F);
            this.main_body.scaleX = 1 + (float) 0.1 * whale.gulpProgress / 50F;
            this.main_body.scaleY = 1 + (float) 0.1 * whale.gulpProgress / 50F;
        //}
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
