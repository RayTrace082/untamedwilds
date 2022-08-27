package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import untamedwilds.entity.fish.EntityTriggerfish;
import untamedwilds.util.EntityUtils;

public class ModelTriggerfish extends AdvancedEntityModel<EntityTriggerfish> {

    private final AdvancedModelBox main_body;
    private final AdvancedModelBox body_tail;
    private final AdvancedModelBox fin_trigger;
    private final AdvancedModelBox head_main;
    private final AdvancedModelBox fin_anal;
    private final AdvancedModelBox fin_dorsal_2;
    private final AdvancedModelBox fin_caudal;
    private final AdvancedModelBox fin_pectoral_left;
    private final AdvancedModelBox fin_pectoral_right;
    private final AdvancedModelBox head_jaw;

    public ModelTriggerfish() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.main_body = new AdvancedModelBox(this, 0, 0);
        this.main_body.setRotationPoint(0.0F, 19.9F, 0.0F);
        this.main_body.addBox(-1.51F, -3.5F, -3.5F, 3, 7, 7, 0.0F);
        this.fin_pectoral_right = new AdvancedModelBox(this, 12, 21);
        this.fin_pectoral_right.setRotationPoint(-1.5F, 1.5F, -1.0F);
        this.fin_pectoral_right.addBox(0.0F, -2.0F, 0.0F, 0, 4, 3, 0.0F);
        this.setRotateAngle(fin_pectoral_right, -0.6373942428283291F, -0.36425021489121656F, 0.18203784098300857F);
        this.head_jaw = new AdvancedModelBox(this, 14, 14);
        this.head_jaw.setRotationPoint(0.0F, 3.0F, -5.0F);
        this.head_jaw.addBox(-1.5F, -4.0F, 0.0F, 3, 4, 1, 0.0F);
        this.head_jaw.scaleX = 1.1F;
        this.fin_anal = new AdvancedModelBox(this, 20, 20);
        this.fin_anal.setRotationPoint(0.0F, 1.5F, -0.4F);
        this.fin_anal.addBox(0.0F, 0.0F, -3.0F, 0, 6, 6, 0.0F);
        this.setRotateAngle(fin_anal, 0.5009094953223726F, 0.0F, 0.0F);
        this.body_tail = new AdvancedModelBox(this, 13, 0);
        this.body_tail.setRotationPoint(0.0F, 0.0F, 2.5F);
        this.body_tail.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 3, 0.0F);
        this.fin_pectoral_left = new AdvancedModelBox(this, 12, 21);
        this.fin_pectoral_left.setRotationPoint(1.5F, 1.5F, -1.0F);
        this.fin_pectoral_left.addBox(0.0F, -2.0F, 0.0F, 0, 4, 3, 0.0F);
        this.setRotateAngle(fin_pectoral_left, -0.6373942428283291F, 0.36425021489121656F, -0.18203784098300857F);
        this.fin_caudal = new AdvancedModelBox(this, 0, 20);
        this.fin_caudal.setRotationPoint(0.0F, 0.0F, 1.7F);
        this.fin_caudal.addBox(0.0F, 0.0F, 0.0F, 0, 6, 6, 0.0F);
        this.setRotateAngle(fin_caudal, 0.7853981633974483F, 0.0F, 0.0F);
        this.fin_dorsal_2 = new AdvancedModelBox(this, 20, 14);
        this.fin_dorsal_2.setRotationPoint(0.0F, -1.5F, -0.1F);
        this.fin_dorsal_2.addBox(0.0F, -6.0F, -3.0F, 0, 6, 6, 0.0F);
        this.setRotateAngle(fin_dorsal_2, -0.36425021489121656F, 0.0F, 0.0F);
        this.fin_trigger = new AdvancedModelBox(this, 0, 20);
        this.fin_trigger.setRotationPoint(0.0F, -3.0F, -3.0F);
        this.fin_trigger.addBox(0.0F, -3.0F, 0.0F, 0, 3, 3, 0.0F);
        this.setRotateAngle(fin_trigger, -1.4570008595648662F, 0.0F, 0.0F);
        this.head_main = new AdvancedModelBox(this, 0, 14);
        this.head_main.setRotationPoint(0.0F, -2.1F, -2.1F);
        this.head_main.addBox(-1.5F, -2.0F, -4.0F, 3, 5, 4, 0.0F);
        this.setRotateAngle(head_main, 0.7853981633974483F, 0.0F, 0.0F);
        this.head_main.scaleX = 1.1F;
        this.head_main.addChild(this.fin_pectoral_right);
        this.head_main.addChild(this.head_jaw);
        this.body_tail.addChild(this.fin_anal);
        this.main_body.addChild(this.body_tail);
        this.head_main.addChild(this.fin_pectoral_left);
        this.body_tail.addChild(this.fin_caudal);
        this.body_tail.addChild(this.fin_dorsal_2);
        this.main_body.addChild(this.fin_trigger);
        this.main_body.addChild(this.head_main);
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
            body_tail,
            fin_trigger,
            head_main,
            fin_anal,
            fin_dorsal_2,
            fin_caudal,
            fin_pectoral_left,
            fin_pectoral_right,
            head_jaw
        );
    }

    public void setupAnim(EntityTriggerfish triggerfish, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ageInTicks - (float)triggerfish.tickCount / 10;
        resetToDefaultPose();

        float globalSpeed = 0.5f;
        float globalDegree = 1f;

        if (!triggerfish.isInWater()) {
            //this.main_body.defaultPositionY += 20;
            this.setRotateAngle(main_body, 0, 0, (float)Math.toRadians(90D));
        }
        else {
            this.setRotateAngle(main_body, triggerfish.getXRot() * ((float) Math.PI / 180F), 0, 0);
            //this.setRotateAngle(main_body, netHeadYaw * 2 * ((float)Math.PI / 180F), headPitch * 2 * ((float)Math.PI / 180F), 0);
        }

        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{main_body, body_tail, fin_caudal};
        chainSwing(bodyParts, globalSpeed, globalDegree * 1.1f, -5, limbSwing, limbSwingAmount);

        if (!EntityUtils.hasFullHealth(triggerfish) || triggerfish.isAngry()) {
            this.setRotateAngle(fin_trigger, -0.4F, 0, 0);
        }

        chainSwing(bodyParts, globalSpeed * 0.4F, globalDegree, -5, limbSwing, 0.6F);
        flap(fin_pectoral_left, globalSpeed, globalDegree * 0.8f, true, 0, 0.2f, f / 4, 1F);
        swing(fin_pectoral_left, globalSpeed, globalDegree * 0.8f, false, 0, 0.2f, f / 4, 0.6F);
        flap(fin_pectoral_right, globalSpeed, globalDegree * 0.8f, false, 0, 0.2f, f / 4, 1F);
        swing(fin_pectoral_right, globalSpeed, globalDegree * 0.8f, true, 0, 0.2f, f / 4, 0.6F);
        flap(fin_dorsal_2, globalSpeed * 0.8f, globalDegree * 0.6f, true, 0, 0, Math.max(f / 6, limbSwing), 1F);
        flap(fin_anal, globalSpeed * 0.8f, globalDegree * 0.6f, false, 0, 0, Math.max(f / 6, limbSwing), 1F);
    }
}
