package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import untamedwilds.entity.fish.EntitySpadefish;

public class ModelSpadefish extends AdvancedEntityModel<EntitySpadefish> {
   
    public AdvancedModelBox main_body;
    public AdvancedModelBox fin_top;
    public AdvancedModelBox fin_bottom;
    public AdvancedModelBox body_tail;
    public AdvancedModelBox fin_pec_left;
    public AdvancedModelBox fin_pec_right;
    public AdvancedModelBox fin_side_left;
    public AdvancedModelBox fin_side_right;
    public AdvancedModelBox body_hump;
    public AdvancedModelBox fin_tail;

    public ModelSpadefish() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.fin_pec_right = new AdvancedModelBox(this, 0, -2);
        this.fin_pec_right.setRotationPoint(-0.5F, 3.5F, -3.0F);
        this.fin_pec_right.addBox(0.0F, 0.0F, 0.0F, 0, 5, 2, 0.0F);
        this.setRotateAngle(fin_pec_right, 0.27314402793711257F, 0.0F, 0.27314402793711257F);
        this.body_tail = new AdvancedModelBox(this, 0, 5);
        this.body_tail.setRotationPoint(0.0F, 0.0F, 6.0F);
        this.body_tail.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 2, 0.0F);
        this.fin_side_left = new AdvancedModelBox(this, 20, 15);
        this.fin_side_left.setRotationPoint(1.5F, 1.0F, -1.0F);
        this.fin_side_left.addBox(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
        this.setRotateAngle(fin_side_left, 0.0F, 0.36425021489121656F, 0.0F);
        this.fin_bottom = new AdvancedModelBox(this, 16, 16);
        this.fin_bottom.setRotationPoint(0.0F, 2.0F, 3.0F);
        this.fin_bottom.addBox(0.0F, 0.0F, -4.0F, 0, 8, 8, 0.0F);
        this.setRotateAngle(fin_bottom, 0.27314402793711257F, 0.0F, 0.0F);
        this.body_hump = new AdvancedModelBox(this, 22, 0);
        this.body_hump.setRotationPoint(0.0F, -3.0F, -1.0F);
        this.body_hump.addBox(-1.0F, 0.0F, 0.0F, 2, 7, 3, 0.0F);
        this.setRotateAngle(body_hump, 1.4570008595648662F, 0.0F, 0.0F);
        this.fin_top = new AdvancedModelBox(this, 0, 16);
        this.fin_top.setRotationPoint(0.0F, -2.5F, 3.0F);
        this.fin_top.addBox(0.0F, -8.0F, -4.0F, 0, 8, 8, 0.0F);
        this.setRotateAngle(fin_top, -0.27314402793711257F, 0.0F, 0.0F);
        this.main_body = new AdvancedModelBox(this, 0, 0);
        this.main_body.setRotationPoint(0.0F, 21.0F, -2.0F);
        this.main_body.addBox(-1.4F, -4.0F, -4.0F, 3, 8, 10, 0.0F);
        this.fin_pec_left = new AdvancedModelBox(this, 0, -2);
        this.fin_pec_left.setRotationPoint(0.5F, 3.5F, -3.0F);
        this.fin_pec_left.addBox(0.0F, 0.0F, 0.0F, 0, 5, 2, 0.0F);
        this.setRotateAngle(fin_pec_left, 0.27314402793711257F, 0.0F, -0.27314402793711257F);
        this.fin_side_right = new AdvancedModelBox(this, 20, 15);
        this.fin_side_right.setRotationPoint(-1.5F, 1.0F, -1.0F);
        this.fin_side_right.addBox(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
        this.setRotateAngle(fin_side_right, 0.0F, -0.36425021489121656F, 0.0F);
        this.fin_tail = new AdvancedModelBox(this, 12, 14);
        this.fin_tail.setRotationPoint(0.0F, 0.0F, 0.5F);
        this.fin_tail.addBox(0.0F, -3.0F, 0.0F, 0, 6, 4, 0.0F);
        this.main_body.addChild(this.fin_pec_right);
        this.main_body.addChild(this.body_tail);
        this.main_body.addChild(this.fin_side_left);
        this.main_body.addChild(this.fin_bottom);
        this.main_body.addChild(this.body_hump);
        this.main_body.addChild(this.fin_top);
        this.main_body.addChild(this.fin_pec_left);
        this.main_body.addChild(this.fin_side_right);
        this.body_tail.addChild(this.fin_tail);
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main_body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
                main_body,
                fin_top,
                fin_bottom,
                body_tail,
                fin_pec_left,
                fin_pec_right,
                fin_side_left,
                fin_side_right,
                body_hump,
                fin_tail
        );
    }

    public void setupAnim(EntitySpadefish spadefish, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        float globalSpeed = 0.5f;
        float globalDegree = 1f;

        if (!spadefish.isInWater()) {
            this.setRotateAngle(main_body, 0, 0, (float)Math.toRadians(90D));
        }
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{main_body, body_tail, fin_tail};
        chainSwing(bodyParts, globalSpeed, globalDegree * 1.1f, -5, limbSwing, limbSwingAmount);

        float speed = Math.min((float)spadefish.getCurrentSpeed(), 0.08F);
        //this.fin_top.rotateAngleX = this.fin_top.defaultRotationX + speed * -3;
        //this.fin_bottom.rotateAngleX = this.fin_bottom.defaultRotationX + speed * 3;
        swing(fin_pec_left, globalSpeed, globalDegree * 0.8f, false, 0, 0.2f, ageInTicks / 6, 0.6F);
        swing(fin_pec_right, globalSpeed, globalDegree * 0.8f, true, 0, 0.2f, ageInTicks / 6, 0.6F);
    }
}
