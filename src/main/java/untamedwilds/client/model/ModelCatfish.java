package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.fish.EntityCatfish;

public class ModelCatfish extends AdvancedEntityModel<EntityCatfish> {
    
    public AdvancedModelBox body_main;
    public AdvancedModelBox head_main;
    public AdvancedModelBox body_bottom;
    public AdvancedModelBox head_main_1;
    public AdvancedModelBox fin_dorsal;
    public AdvancedModelBox fin_pectoral_left;
    public AdvancedModelBox fin_pelvic_left;
    public AdvancedModelBox fin_pelvic_right;
    public AdvancedModelBox fin_pectoral_right;
    public AdvancedModelBox whisker_left;
    public AdvancedModelBox whisker_right;
    public AdvancedModelBox body_tail;
    public AdvancedModelBox fin_anal;
    public AdvancedModelBox fin_dorsal_1;
    public AdvancedModelBox fin_caudal;

    public ModelCatfish() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.fin_dorsal = new AdvancedModelBox(this, 30, 13);
        this.fin_dorsal.setRotationPoint(0.0F, -1.3F, 1.0F);
        this.fin_dorsal.addBox(0.0F, -8.0F, -3.0F, 0, 8, 5, 0.0F);
        this.setRotateAngle(fin_dorsal, -0.4553564018453205F, 0.0F, 0.0F);
        this.fin_pelvic_right = new AdvancedModelBox(this, 40, 15);
        this.fin_pelvic_right.setRotationPoint(-2.0F, 1.0F, 2.0F);
        this.fin_pelvic_right.addBox(0.0F, 0.0F, -1.5F, 0, 6, 3, 0.0F);
        this.setRotateAngle(fin_pelvic_right, 0.36425021489121656F, 0.136659280431156F, 0.5462880558742251F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 23.0F, -2.0F);
        this.body_main.addBox(-2.5F, -2.5F, -3.0F, 5, 4, 7, 0.0F);
        this.head_main_1 = new AdvancedModelBox(this, 24, 9);
        this.head_main_1.setRotationPoint(0.0F, 1.6F, -3.0F);
        this.head_main_1.addBox(-3.0F, -2.0F, 0.0F, 6, 2, 6, 0.0F);
        this.setRotateAngle(head_main_1, -0.091106186954104F, 0.0F, 0.0F);
        this.head_main = new AdvancedModelBox(this, 24, 0);
        this.head_main.setRotationPoint(0.0F, -0.4F, -5.0F);
        this.head_main.addBox(-3.0F, -2.0F, -3.0F, 6, 3, 6, 0.0F);
        this.head_main.scaleX = 1.1F;
        this.setRotateAngle(head_main, 0.136659280431156F, 0.0F, 0.0F);
        this.fin_caudal = new AdvancedModelBox(this, 0, 18);
        this.fin_caudal.setRotationPoint(0.0F, 0.0F, 1.2F);
        this.fin_caudal.addBox(0.0F, 0.0F, 0.0F, 0, 7, 7, 0.0F);
        this.setRotateAngle(fin_caudal, 0.7853981633974483F, 0.0F, 0.0F);
        this.fin_pectoral_right = new AdvancedModelBox(this, 12, 6);
        this.fin_pectoral_right.setRotationPoint(-2.5F, 0.0F, -2.2F);
        this.fin_pectoral_right.addBox(0.0F, -1.5F, 0.0F, 0, 3, 6, 0.0F);
        this.setRotateAngle(fin_pectoral_right, 0.0F, -0.5462880558742251F, 0.0F);
        this.fin_anal = new AdvancedModelBox(this, 20, 16);
        this.fin_anal.setRotationPoint(0.0F, 0.4F, 2.0F);
        this.fin_anal.addBox(0.0F, 0.0F, -3.0F, 0, 6, 10, 0.0F);
        this.setRotateAngle(fin_anal, 0.22759093446006054F, 0.0F, 0.0F);
        this.whisker_right = new AdvancedModelBox(this, 42, 0);
        this.whisker_right.mirror = true;
        this.whisker_right.setRotationPoint(-2.6F, -1.0F, -2.0F);
        this.whisker_right.addBox(-6.0F, 0.0F, -0.5F, 6, 0, 1, 0.0F);
        this.setRotateAngle(whisker_right, 0.0F, -0.27314402793711257F, -0.4553564018453205F);
        this.whisker_left = new AdvancedModelBox(this, 42, 0);
        this.whisker_left.mirror = true;
        this.whisker_left.setRotationPoint(2.6F, -1.0F, -2.0F);
        this.whisker_left.addBox(0.0F, 0.0F, -0.5F, 6, 0, 1, 0.0F);
        this.setRotateAngle(whisker_left, 0.0F, 0.27314402793711257F, 0.4553564018453205F);
        this.fin_pelvic_left = new AdvancedModelBox(this, 40, 15);
        this.fin_pelvic_left.mirror = true;
        this.fin_pelvic_left.setRotationPoint(2.0F, 1.0F, 2.0F);
        this.fin_pelvic_left.addBox(0.0F, 0.0F, -1.5F, 0, 6, 3, 0.0F);
        this.setRotateAngle(fin_pelvic_left, 0.36425021489121656F, -0.136659280431156F, -0.5462880558742251F);
        this.body_bottom = new AdvancedModelBox(this, 0, 11);
        this.body_bottom.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.body_bottom.addBox(-1.5F, -2.5F, 0.0F, 3, 4, 6, 0.0F);
        this.fin_dorsal_1 = new AdvancedModelBox(this, 19, 14);
        this.fin_dorsal_1.setRotationPoint(0.0F, 0.1F, 3.0F);
        this.fin_dorsal_1.addBox(0.0F, -5.0F, -3.0F, 0, 5, 4, 0.0F);
        this.setRotateAngle(fin_dorsal_1, -1.0016444577195458F, 0.0F, 0.0F);
        this.fin_pectoral_left = new AdvancedModelBox(this, 12, 6);
        this.fin_pectoral_left.mirror = true;
        this.fin_pectoral_left.setRotationPoint(2.5F, 0.0F, -2.2F);
        this.fin_pectoral_left.addBox(0.0F, -1.5F, 0.0F, 0, 3, 6, 0.0F);
        this.setRotateAngle(fin_pectoral_left, 0.0F, 0.5590024477811982F, 0.0F);
        this.body_tail = new AdvancedModelBox(this, 18, 0);
        this.body_tail.setRotationPoint(0.0F, -1.0F, 6.0F);
        this.body_tail.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 3, 0.0F);
        this.body_main.addChild(this.fin_dorsal);
        this.body_main.addChild(this.fin_pelvic_right);
        this.head_main.addChild(this.head_main_1);
        this.body_main.addChild(this.head_main);
        this.body_tail.addChild(this.fin_caudal);
        this.body_main.addChild(this.fin_pectoral_right);
        this.body_bottom.addChild(this.fin_anal);
        this.head_main.addChild(this.whisker_right);
        this.head_main.addChild(this.whisker_left);
        this.body_main.addChild(this.fin_pelvic_left);
        this.body_main.addChild(this.body_bottom);
        this.body_bottom.addChild(this.fin_dorsal_1);
        this.body_main.addChild(this.fin_pectoral_left);
        this.body_bottom.addChild(this.body_tail);
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main, head_main, body_bottom, head_main_1, fin_dorsal, fin_pectoral_left,
                fin_pelvic_left, fin_pelvic_right, fin_pectoral_right, whisker_left, whisker_right, body_tail, fin_anal,
                fin_dorsal_1, fin_caudal);
    }

    public void setupAnim(EntityCatfish catfish, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        float globalSpeed = 0.5f;
        float globalDegree = 1f;

        if (!catfish.isInWater()) {
            this.setRotateAngle(body_main, 0, 0, (float)Math.toRadians(90D));
        }
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{head_main, body_main, body_tail, fin_caudal};
        chainSwing(bodyParts, globalSpeed, globalDegree * 1.1f, -5, limbSwing, limbSwingAmount);

        float speed = Math.min((float)catfish.getCurrentSpeed(), 0.08F);
        swing(fin_pectoral_left, globalSpeed, globalDegree * 0.8f, false, 0, 0.2f, ageInTicks / 6, 0.6F);
        swing(fin_pectoral_right, globalSpeed, globalDegree * 0.8f, true, 0, 0.2f, ageInTicks / 6, 0.6F);

        this.setRotateAngle(whisker_right, 0.0F, -0.27314402793711257F + catfish.whisker_offset.getA(), -0.4553564018453205F + catfish.whisker_offset.getB());
        this.setRotateAngle(whisker_left, 0.0F, 0.27314402793711257F - catfish.whisker_offset.getA(), 0.4553564018453205F - catfish.whisker_offset.getB());
    }


}