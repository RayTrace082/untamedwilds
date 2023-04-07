package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import untamedwilds.entity.fish.EntityFootballFish;

public class ModelFootballFish extends AdvancedEntityModel<EntityFootballFish> {

    public AdvancedModelBox main_body;
    public AdvancedModelBox head_jaw;
    public AdvancedModelBox head_fin_left;
    public AdvancedModelBox head_fin_right;
    public AdvancedModelBox body_tail;
    public AdvancedModelBox body_bait;
    public AdvancedModelBox tail_fin;
    public AdvancedModelBox tail_top;
    public AdvancedModelBox tail_bottom;
    public AdvancedModelBox attached_male;

    public ModelFootballFish() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.head_fin_right = new AdvancedModelBox(this, 0, 0);
        this.head_fin_right.mirror = true;
        this.head_fin_right.setRotationPoint(-3.5F, 0.0F, 0.0F);
        this.head_fin_right.addBox(0.0F, -1.5F, 0.0F, 0, 3, 3, 0.0F);
        this.setRotateAngle(head_fin_right, -0.045553093477052F, -0.22759093446006054F, 0.0F);
        this.body_bait = new AdvancedModelBox(this, 16, 10);
        this.body_bait.setRotationPoint(0.0F, -3.0F, -2.0F);
        this.body_bait.addBox(0.0F, -5.0F, -2.5F, 0, 5, 5, 0.0F);
        this.body_tail = new AdvancedModelBox(this, 0, 27);
        this.body_tail.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.body_tail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2, 0.0F);
        this.setRotateAngle(body_tail, -0.22759093446006054F, 0.0F, 0.0F);
        this.tail_top = new AdvancedModelBox(this, 13, 21);
        this.tail_top.setRotationPoint(0.0F, -1.1F, 1.0F);
        this.tail_top.addBox(0.0F, -1.5F, -0.5F, 0, 3, 3, 0.0F);
        this.setRotateAngle(tail_top, 1.1838568316277536F, 0.0F, 0.0F);
        this.head_jaw = new AdvancedModelBox(this, 0, 15);
        this.head_jaw.setRotationPoint(0.0F, 2.0F, -3.0F);
        this.head_jaw.addBox(-2.5F, -4.0F, -1.0F, 5, 4, 2, 0.0F);
        this.setRotateAngle(head_jaw, 0.36425021489121656F, 0.0F, 0.0F);
        this.head_fin_left = new AdvancedModelBox(this, 0, 0);
        this.head_fin_left.setRotationPoint(3.5F, 0.0F, 0.0F);
        this.head_fin_left.addBox(0.0F, -1.5F, 0.0F, 0, 3, 3, 0.0F);
        this.setRotateAngle(head_fin_left, -0.045553093477052F, 0.22759093446006054F, 0.0F);
        this.tail_bottom = new AdvancedModelBox(this, 13, 21);
        this.tail_bottom.setRotationPoint(0.0F, 1.1F, 1.0F);
        this.tail_bottom.addBox(0.0F, -1.5F, -0.5F, 0, 3, 3, 0.0F);
        this.setRotateAngle(tail_bottom, -1.1838568316277536F, 0.0F, 0.0F);
        this.main_body = new AdvancedModelBox(this, 0, 0);
        this.main_body.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.main_body.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 8, 0.0F);
        this.setRotateAngle(main_body, 0.22759093446006054F, 0.0F, 0.0F);
        this.tail_fin = new AdvancedModelBox(this, 10, 24);
        this.tail_fin.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.tail_fin.addBox(0.0F, -1.5F, 0.0F, 0, 3, 3, 0.0F);
        this.attached_male = new AdvancedModelBox(this, 6, 4);
        this.attached_male.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.attached_male.addBox(0.0F, 0.0F, 0.0F, 0, 3, 1, 0.0F);
        this.setRotateAngle(attached_male, 0.36425021489121656F, 0.0F, 0.0F);
        this.main_body.addChild(this.head_fin_right);
        this.main_body.addChild(this.body_bait);
        this.main_body.addChild(this.body_tail);
        this.body_tail.addChild(this.tail_top);
        this.main_body.addChild(this.head_jaw);
        this.main_body.addChild(this.head_fin_left);
        this.body_tail.addChild(this.tail_bottom);
        this.body_tail.addChild(this.tail_fin);
        this.main_body.addChild(this.attached_male);
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(main_body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
            main_body,
            head_jaw,
            head_fin_left,
            head_fin_right,
            body_tail,
            body_bait,
            tail_fin,
            tail_top,
            tail_bottom,
            attached_male
        );
    }

    public void setupAnim(EntityFootballFish football_fish, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ageInTicks - (float)football_fish.tickCount / 10;
        resetToDefaultPose();

        if (!football_fish.isInWater()) {
            main_body.defaultPositionY = 20;
            this.setRotateAngle(main_body, 0, 0, (float)Math.toRadians(90D));
        }
        else {
            this.setRotateAngle(main_body, netHeadYaw * 2 * ((float)Math.PI / 180F), headPitch * 2 * ((float)Math.PI / 180F), 0);
        }
        float globalSpeed = 0.6f;
        float globalDegree = 0.6f;

        if (football_fish.hasAttachedMale()) {
            this.attached_male.setRotationPoint(0.0F, 3.0F, -2F);
        }

        // Movement Animation
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{body_tail, tail_fin};
        chainSwing(bodyParts, globalSpeed * 0.4F, globalDegree, -5, f, 0.6F);
        walk(head_jaw, globalSpeed, globalDegree * 0.2f, false, 0.2F, 0.2f, ageInTicks / 6, 0.6F);
        flap(head_fin_left, globalSpeed, globalDegree * 0.8f, true, 0, 0.2f, f / 6, 1F);
        swing(head_fin_left, globalSpeed, globalDegree * 0.8f, false, 0, 0.2f, f / 6, 0.6F);
        flap(head_fin_right, globalSpeed, globalDegree * 0.8f, false, 0, 0.2f, f / 6, 1F);
        swing(head_fin_right, globalSpeed, globalDegree * 0.8f, true, 0, 0.2f, f / 6, 0.6F);
    }


}