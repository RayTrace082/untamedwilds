package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.entity.fish.Sunfish;

@OnlyIn(Dist.CLIENT)
public class ModelSunfish extends AdvancedEntityModel {
    public AdvancedModelBox body_main;
    public AdvancedModelBox body_head;
    public AdvancedModelBox body_tail;
    public AdvancedModelBox body_fin_right;
    public AdvancedModelBox body_fin_left;
    public AdvancedModelBox body_tail_fin;
    public AdvancedModelBox body_fin_top;
    public AdvancedModelBox body_fin_bottom;

    public ModelSunfish() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.body_main.addBox(-3.0F, -13.0F, -8.0F, 6, 26, 16, 0.0F);
        this.body_fin_right = new AdvancedModelBox(this, 0, 0);
        this.body_fin_right.setRotationPoint(-3.0F, -2.0F, -4.0F);
        this.body_fin_right.addBox(0.0F, -2.0F, 0.0F, 0, 4, 5, 0.0F);
        this.setRotateAngle(body_fin_right, 0.0F, -0.5462880558742251F, 0.0F);
        this.body_tail_fin = new AdvancedModelBox(this, 104, 0);
        this.body_tail_fin.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.body_tail_fin.addBox(-1.0F, -11.0F, 0.0F, 2, 22, 5, 0.0F);
        this.body_fin_top = new AdvancedModelBox(this, 82, 32);
        this.body_fin_top.setRotationPoint(0.0F, -10.0F, 6.0F);
        this.body_fin_top.addBox(-1.0F, -20.0F, -4.0F, 2, 20, 8, 0.0F);
        this.setRotateAngle(body_fin_top, -0.22759093446006054F, 0.0F, 0.0F);
        this.body_fin_left = new AdvancedModelBox(this, 0, 0);
        this.body_fin_left.setRotationPoint(3.0F, -2.0F, -4.0F);
        this.body_fin_left.addBox(0.0F, -2.0F, 0.0F, 0, 4, 5, 0.0F);
        this.setRotateAngle(body_fin_left, 0.0F, 0.5462880558742251F, 0.0F);
        this.body_head = new AdvancedModelBox(this, 44, 0);
        this.body_head.setRotationPoint(0.0F, 0.0F, -9.0F);
        this.body_head.addBox(-2.5F, -9.0F, -6.0F, 5, 18, 7, 0.0F);
        this.body_tail = new AdvancedModelBox(this, 78, 0);
        this.body_tail.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.body_tail.addBox(-2.5F, -11.0F, 0.0F, 5, 22, 8, 0.0F);
        this.body_fin_bottom = new AdvancedModelBox(this, 104, 32);
        this.body_fin_bottom.setRotationPoint(0.0F, 10.0F, 6.0F);
        this.body_fin_bottom.addBox(-1.0F, 0.0F, -4.0F, 2, 20, 8, 0.0F);
        this.setRotateAngle(body_fin_bottom, 0.22759093446006054F, 0.0F, 0.0F);
        this.body_main.addChild(this.body_fin_right);
        this.body_tail.addChild(this.body_tail_fin);
        this.body_tail.addChild(this.body_fin_top);
        this.body_main.addChild(this.body_fin_left);
        this.body_main.addChild(this.body_head);
        this.body_main.addChild(this.body_tail);
        this.body_tail.addChild(this.body_fin_bottom);
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
            body_head,
            body_tail,
            body_fin_right,
            body_fin_left,
            body_tail_fin,
            body_fin_top,
            body_fin_bottom
        );
    }

    public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ageInTicks - (float)entityIn.ticksExisted / 10;
        limbSwing *= 0.2F;
        Sunfish sunfish = (Sunfish) entityIn;
        resetToDefaultPose();

        if (!sunfish.isInWater()) {
            body_main.defaultPositionY =+ 20;
            this.setRotateAngle(body_main, 0, 0, (float)Math.toRadians(90D));
        }
        else {
            this.setRotateAngle(body_main, netHeadYaw * 2 * ((float)Math.PI / 180F), headPitch * 2 * ((float)Math.PI / 180F), 0);
            progressRotation(body_main, sunfish.baskProgress, 0, 0, (float)Math.toRadians(90D), 100);
        }
        float limbSwingConstant = 0.5f;
        float globalSpeed = 0.6f;
        float globalDegree = 0.6f;

        flap(body_fin_bottom, globalSpeed * 0.4F, globalDegree * 0.8f, true, 0, 0, f, 1F);
        flap(body_fin_top, globalSpeed * 0.4F, globalDegree * 0.8f, false, 0, 0, f , 1F);
        swing(body_tail_fin, globalSpeed * 0.4F, globalDegree * 0.8f, false, 0, 0, f, 0.6F);

        flap(body_fin_left, globalSpeed, globalDegree * 0.8f, true, 0, 0.2f, f / 6, 1F);
        swing(body_fin_left, globalSpeed, globalDegree * 0.8f, false, 0, 0.2f, f / 6, 0.6F);
        flap(body_fin_right, globalSpeed, globalDegree * 0.8f, false, 0, 0.2f, f / 6, 1F);
        swing(body_fin_right, globalSpeed, globalDegree * 0.8f, true, 0, 0.2f, f / 6, 0.6F);
    }
}