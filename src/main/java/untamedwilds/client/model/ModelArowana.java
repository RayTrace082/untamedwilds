package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import untamedwilds.entity.fish.EntityArowana;

public class ModelArowana extends AdvancedEntityModel<EntityArowana> {
    
    public AdvancedModelBox body_main;
    public AdvancedModelBox body_tail_1;
    public AdvancedModelBox body_head;
    public AdvancedModelBox body_tail_fin_1;
    public AdvancedModelBox body_tail_fin_2;
    public AdvancedModelBox body_tail_3;
    public AdvancedModelBox body_tail_fin;
    public AdvancedModelBox head_mouth;
    public AdvancedModelBox shape15;
    public AdvancedModelBox shape16;
    public AdvancedModelBox head_mouth_whisker_1;
    public AdvancedModelBox head_mouth_whisker_2;

    public ModelArowana() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.head_mouth = new AdvancedModelBox(this, 0, 25);
        this.head_mouth.setRotationPoint(0.0F, 1.5F, -6.0F);
        this.head_mouth.addBox(-1.5F, -4.0F, -1.0F, 3, 5, 1, 0.0F);
        this.head_mouth.scaleX = 1.1F;
        this.head_mouth_whisker_1 = new AdvancedModelBox(this, 16, 20);
        this.head_mouth_whisker_1.setRotationPoint(0.5F, -3.9F, 0.0F);
        this.head_mouth_whisker_1.addBox(-0.5F, 0.0F, -3.0F, 1, 0, 3, 0.0F);
        this.setRotateAngle(head_mouth_whisker_1, 0.0F, -0.22759093446006054F, 0.0F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 21.1F, -2.0F);
        this.body_main.addBox(-1.5F, -2.5F, -4.0F, 3, 5, 7, 0.0F);
        this.body_head = new AdvancedModelBox(this, 0, 14);
        this.body_head.setRotationPoint(0.0F, -0.2F, -2.0F);
        this.body_head.addBox(-1.5F, -2.5F, -6.0F, 3, 5, 5, 0.0F);
        this.setRotateAngle(body_head, 0.091106186954104F, 0.0F, 0.0F);
        this.body_head.scaleX = 1.1F;
        this.body_tail_fin_1 = new AdvancedModelBox(this, 24, 8);
        this.body_tail_fin_1.setRotationPoint(0.0F, -1.4F, 4.5F);
        this.body_tail_fin_1.addBox(0.0F, -5.0F, 0.0F, 0, 10, 4, 0.0F);
        this.setRotateAngle(body_tail_fin_1, 1.4570008595648662F, 0.0F, 0.0F);
        this.body_tail_fin_2 = new AdvancedModelBox(this, 32, 8);
        this.body_tail_fin_2.setRotationPoint(0.0F, 1.5F, 4.5F);
        this.body_tail_fin_2.addBox(0.0F, -5.0F, 0.0F, 0, 10, 4, 0.0F);
        this.setRotateAngle(body_tail_fin_2, -1.4114477660878142F, 0.0F, 0.0F);
        this.head_mouth_whisker_2 = new AdvancedModelBox(this, 16, 20);
        this.head_mouth_whisker_2.setRotationPoint(-0.5F, -3.9F, 0.0F);
        this.head_mouth_whisker_2.addBox(-0.5F, 0.0F, -3.0F, 1, 0, 3, 0.0F);
        this.setRotateAngle(head_mouth_whisker_2, 0.0F, 0.22759093446006054F, 0.0F);
        this.body_tail_fin = new AdvancedModelBox(this, 44, 8);
        this.body_tail_fin.setRotationPoint(0.0F, 0.0F, 2.3F);
        this.body_tail_fin.addBox(0.0F, 0.0F, 0.0F, 0, 5, 5, 0.0F);
        this.setRotateAngle(body_tail_fin, 0.7853981633974483F, 0.0F, 0.0F);
        this.shape15 = new AdvancedModelBox(this, 24, 20);
        this.shape15.setRotationPoint(1.3F, 1.0F, -3.0F);
        this.shape15.addBox(0.0F, 0.0F, -1.5F, 0, 7, 3, 0.0F);
        this.setRotateAngle(shape15, 1.0471975511965976F, 0.31869712141416456F, 0.0F);
        this.shape16 = new AdvancedModelBox(this, 24, 20);
        this.shape16.setRotationPoint(-1.3F, 1.0F, -3.0F);
        this.shape16.addBox(0.0F, 0.0F, -1.5F, 0, 7, 3, 0.0F);
        this.setRotateAngle(shape16, 1.0471975511965976F, -0.31869712141416456F, 0.0F);
        this.body_tail_1 = new AdvancedModelBox(this, 24, 0);
        this.body_tail_1.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.body_tail_1.addBox(-1.5F, -2.5F, 0.0F, 3, 5, 7, 0.0F);
        this.body_tail_3 = new AdvancedModelBox(this, 44, 6);
        this.body_tail_3.setRotationPoint(0.0F, 0.0F, 5.6F);
        this.body_tail_3.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 4, 0.0F);
        this.body_head.addChild(this.head_mouth);
        this.head_mouth.addChild(this.head_mouth_whisker_1);
        this.body_main.addChild(this.body_head);
        this.body_tail_1.addChild(this.body_tail_fin_1);
        this.body_tail_1.addChild(this.body_tail_fin_2);
        this.head_mouth.addChild(this.head_mouth_whisker_2);
        this.body_tail_3.addChild(this.body_tail_fin);
        this.body_head.addChild(this.shape15);
        this.body_head.addChild(this.shape16);
        this.body_main.addChild(this.body_tail_1);
        this.body_tail_1.addChild(this.body_tail_3);
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main, body_tail_1, body_head, body_tail_fin_1, body_tail_fin_2, body_tail_3,
                body_tail_fin, head_mouth, shape15, shape16, head_mouth_whisker_1, head_mouth_whisker_2);
    }

    public void setRotationAngles(EntityArowana arowana, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();
        float globalSpeed = 0.5f;
        float globalDegree = 1f;

        // Breathing Animation
        walk(head_mouth, globalSpeed, globalDegree * 0.2f, false, 0.2F, 0.2f, ageInTicks / 6, 0.6F);
        swing(shape15, globalSpeed, globalDegree * 0.5f, false, 0, 0.2f, ageInTicks / 6, 0.6F);
        swing(shape16, globalSpeed, globalDegree * 0.5f, true, 0, 0.2f, ageInTicks / 6, 0.6F);

        // Pitch/Yaw handler
        if (!arowana.isInWater()) {
            this.setRotateAngle(body_main, 0, 0, (float)Math.toRadians(90D));
        }
        else {
            this.setRotateAngle(body_main, netHeadYaw * 2 * ((float) Math.PI / 180F), headPitch * 2 * ((float) Math.PI / 180F), 0);
        }

        // Movement Animation
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{body_head, body_main, body_tail_1, body_tail_3, body_tail_fin};
        chainSwing(bodyParts, globalSpeed, globalDegree * 1.1F, -5, limbSwing, limbSwingAmount);
    }
}
