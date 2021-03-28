package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import untamedwilds.entity.fish.EntityTrevally;

public class ModelTrevally extends AdvancedEntityModel<EntityTrevally> {

    public AdvancedModelBox body_main;
    public AdvancedModelBox head_main;
    public AdvancedModelBox body_tail;
    public AdvancedModelBox fin_top;
    public AdvancedModelBox fin_bottom;
    public AdvancedModelBox fin_dorsal;
    public AdvancedModelBox head_mouth;
    public AdvancedModelBox fin_right;
    public AdvancedModelBox fin_left;
    public AdvancedModelBox fin_tail;

    public ModelTrevally() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.head_main = new AdvancedModelBox(this, 0, 18);
        this.head_main.setRotationPoint(0.0F, 1.57F, -2.53F);
        this.head_main.addBox(-1.5F, -3.5F, -5.0F, 3, 6, 5, 0.0F);
        this.head_main.scaleX = 1.1F;
        this.setRotateAngle(head_main, -0.5462880558742251F, 0.0F, 0.0F);
        this.body_tail = new AdvancedModelBox(this, 18, 0);
        this.body_tail.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.body_tail.addBox(-1.0F, -2.0F, 0.0F, 2, 4, 3, 0.0F);
        this.fin_tail = new AdvancedModelBox(this, 28, 8);
        this.fin_tail.setRotationPoint(0.0F, 0.0F, 0.7F);
        this.fin_tail.addBox(0.0F, 0.0F, 0.0F, 0, 8, 8, 0.0F);
        this.setRotateAngle(fin_tail, 0.7853981633974483F, 0.0F, 0.0F);
        this.fin_right = new AdvancedModelBox(this, 16, 20);
        this.fin_right.setRotationPoint(-1.4F, -0.5F, -1.0F);
        this.fin_right.addBox(0.0F, -1.0F, 0.0F, 0, 2, 6, 0.0F);
        this.setRotateAngle(fin_right, 0.5918411493512771F, -0.27314402793711257F, -0.18203784098300857F);
        this.fin_bottom = new AdvancedModelBox(this, 28, 2);
        this.fin_bottom.setRotationPoint(0.0F, 2.0F, 5.0F);
        this.fin_bottom.addBox(0.0F, -1.0F, -3.0F, 0, 8, 6, 0.0F);
        this.setRotateAngle(fin_bottom, 0.5918411493512771F, 0.0F, 0.0F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.body_main.addBox(-1.5F, -4.0F, -5.0F, 3, 8, 10, 0.0F);
        this.fin_dorsal = new AdvancedModelBox(this, 28, 21);
        this.fin_dorsal.setRotationPoint(0.0F, -3.5F, -1.4F);
        this.fin_dorsal.addBox(0.0F, -3.0F, -0.5F, 0, 3, 3, 0.0F);
        this.setRotateAngle(fin_dorsal, -0.6373942428283291F, 0.0F, 0.0F);
        this.fin_left = new AdvancedModelBox(this, 16, 20);
        this.fin_left.setRotationPoint(1.4F, -0.5F, -1.0F);
        this.fin_left.addBox(0.0F, -1.0F, 0.0F, 0, 2, 6, 0.0F);
        this.setRotateAngle(fin_left, 0.5918411493512771F, 0.27314402793711257F, 0.18203784098300857F);
        this.fin_top = new AdvancedModelBox(this, 28, -6);
        this.fin_top.setRotationPoint(0.0F, -2.0F, 5.0F);
        this.fin_top.addBox(0.0F, -7.0F, -3.0F, 0, 8, 6, 0.0F);
        this.setRotateAngle(fin_top, -0.5918411493512771F, 0.0F, 0.0F);
        this.head_mouth = new AdvancedModelBox(this, 12, 18);
        this.head_mouth.setRotationPoint(0.0F, 2.4F, -1.8F);
        this.head_mouth.addBox(-1.5F, 0.0F, -3.0F, 3, 1, 4, 0.0F);
        this.setRotateAngle(head_mouth, 0.091106186954104F, 0.0F, 0.0F);
        this.body_main.addChild(this.head_main);
        this.body_main.addChild(this.body_tail);
        this.body_tail.addChild(this.fin_tail);
        this.head_main.addChild(this.fin_right);
        this.body_main.addChild(this.fin_bottom);
        this.body_main.addChild(this.fin_dorsal);
        this.head_main.addChild(this.fin_left);
        this.body_main.addChild(this.fin_top);
        this.head_main.addChild(this.head_mouth);
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
            body_main,
            head_main,
            body_tail,
            fin_top,
            fin_bottom,
            fin_dorsal,
            head_mouth,
            fin_right,
            fin_left,
            fin_tail
        );
    }

    public void setRotationAngles(EntityTrevally trevally, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        float globalSpeed = 0.6f;
        float globalDegree = 1f;

        if (!trevally.isInWater()) {
            this.setRotateAngle(body_main, 0, 0, (float)Math.toRadians(90D));
        }
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{head_main, body_main, body_tail, fin_tail};
        chainSwing(bodyParts, globalSpeed, globalDegree * 1.3f, -5, limbSwing, limbSwingAmount);

        float speed = Math.min((float)trevally.getSpeed(), 0.08F);
        this.fin_dorsal.rotateAngleX = this.fin_dorsal.defaultRotationX + speed * -8;
        this.fin_top.rotateAngleX = this.fin_top.defaultRotationX + speed * -3;
        this.fin_bottom.rotateAngleX = this.fin_bottom.defaultRotationX + speed * 3;
        swing(fin_left, globalSpeed, globalDegree * 0.8f, false, 0, 0.2f, ageInTicks / 6, 0.6F);
        swing(fin_right, globalSpeed, globalDegree * 0.8f, true, 0, 0.2f, ageInTicks / 6, 0.6F);
    }
}
