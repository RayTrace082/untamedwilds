package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import untamedwilds.entity.mammal.EntityBigCat;

public class ModelBigCatCub extends AdvancedEntityModel<EntityBigCat> {

    private final AdvancedModelBox main_body;
    private final AdvancedModelBox main_buttocks;
    private final AdvancedModelBox head_face;
    private final AdvancedModelBox arm_right;
    private final AdvancedModelBox arm_left;
    private final AdvancedModelBox leg_left;
    private final AdvancedModelBox leg_right;
    private final AdvancedModelBox tail_1;
    private final AdvancedModelBox tail_2;
    private final AdvancedModelBox head_snout;
    private final AdvancedModelBox ear_left;
    private final AdvancedModelBox ear_right;
    private final AdvancedModelBox eye_left;
    private final AdvancedModelBox eye_right;

    public ModelBigCatCub() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.arm_left = new AdvancedModelBox(this, 114, 32);
        this.arm_left.mirror = true;
        this.arm_left.setRotationPoint(2.2F, -1.0F, -1.0F);
        this.arm_left.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.eye_right = new AdvancedModelBox(this, 94, 25);
        this.eye_right.setRotationPoint(-0.51F, 0.0F, -3.01F);
        this.eye_right.addBox(-2.0F, -0.5F, -1.0F, 2, 1, 1, 0.0F);
        this.main_buttocks = new AdvancedModelBox(this, 96, 12);
        this.main_buttocks.setRotationPoint(0.0F, 0.0F, 6.5F);
        this.main_buttocks.addBox(-2.5F, -3.0F, -3.5F, 5, 6, 7, 0.0F);
        this.ear_right = new AdvancedModelBox(this, 110, 25);
        this.ear_right.setRotationPoint(-2.5F, -2.5F, -2.5F);
        this.ear_right.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(ear_right, -0.31869712141416456F, 0.31869712141416456F, -0.7285004297824331F);
        this.head_face = new AdvancedModelBox(this, 96, 25);
        this.head_face.setRotationPoint(0.0F, -2.5F, -3.0F);
        this.head_face.addBox(-2.5F, -2.5F, -4.0F, 5, 5, 4, 0.0F);
        this.setRotateAngle(head_face, 0.136659280431156F, 0.0F, 0.0F);
        this.leg_left = new AdvancedModelBox(this, 114, 32);
        this.leg_left.mirror = true;
        this.leg_left.setRotationPoint(2.4F, -1.0F, 1.0F);
        this.leg_left.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.tail_2 = new AdvancedModelBox(this, 104, 37);
        this.tail_2.setRotationPoint(0.0F, 0.0F, 6.0F);
        this.tail_2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(tail_2, 0.4553564018453205F, 0.0F, 0.0F);
        this.leg_right = new AdvancedModelBox(this, 114, 32);
        this.leg_right.setRotationPoint(-2.4F, -1.0F, 1.0F);
        this.leg_right.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.ear_left = new AdvancedModelBox(this, 110, 25);
        this.ear_left.mirror = true;
        this.ear_left.setRotationPoint(2.5F, -2.5F, -2.5F);
        this.ear_left.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(ear_left, -0.31869712141416456F, -0.31869712141416456F, 0.7285004297824331F);
        this.main_body = new AdvancedModelBox(this, 96, 0);
        this.main_body.setRotationPoint(0.0F, 17.0F, -2.0F);
        this.main_body.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 114, 25);
        this.head_snout.setRotationPoint(0.0F, 1.0F, -1.3F);
        this.head_snout.addBox(-1.5F, -2.0F, -5.0F, 3, 3, 3, 0.0F);
        this.setRotateAngle(head_snout, 0.091106186954104F, 0.0F, 0.0F);
        this.arm_right = new AdvancedModelBox(this, 114, 32);
        this.arm_right.setRotationPoint(-2.2F, -1.0F, -1.0F);
        this.arm_right.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.tail_1 = new AdvancedModelBox(this, 94, 34);
        this.tail_1.setRotationPoint(0.0F, -2.0F, 3.0F);
        this.tail_1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(tail_1, -0.9560913642424937F, 0.0F, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 94, 25);
        this.eye_left.mirror = true;
        this.eye_left.setRotationPoint(0.51F, 0.0F, -3.01F);
        this.eye_left.addBox(0.0F, -0.5F, -1.0F, 2, 1, 1, 0.0F);
        this.main_body.addChild(this.arm_left);
        this.head_face.addChild(this.eye_right);
        this.main_body.addChild(this.main_buttocks);
        this.head_face.addChild(this.ear_right);
        this.main_body.addChild(this.head_face);
        this.main_buttocks.addChild(this.leg_left);
        this.tail_1.addChild(this.tail_2);
        this.main_buttocks.addChild(this.leg_right);
        this.head_face.addChild(this.ear_left);
        this.head_face.addChild(this.head_snout);
        this.main_body.addChild(this.arm_right);
        this.main_buttocks.addChild(this.tail_1);
        this.head_face.addChild(this.eye_left);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main_body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(main_body,
        main_buttocks,
        head_face,
        arm_right,
        arm_left,
        leg_left,
        leg_right,
        head_snout,
        ear_left,
        ear_right,
        eye_left,
        eye_right, tail_1, tail_2
        );
    }

    public void setupAnim(EntityBigCat big_cat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();

        // Breathing Animation
        this.main_body.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.main_buttocks.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(main_body, 0.4F * 1.5f, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        flap(tail_1, 0.8f, 0.4f, true, 0F, 0f, ageInTicks / 6, 2);
        flap(tail_2, 0.8f, 0.4f, true, 0.5F, 0f, ageInTicks / 6, 2);

        // Blinking Animation
        if (!big_cat.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-0.49F, 0, -2F);
            this.eye_left.setRotationPoint(0.49F, 0, -2F);
        }

        // Head Tracking Animation
        this.faceTarget(netHeadYaw, headPitch, 1, head_face);

        // Movement Animation
        if (big_cat.canMove()) {
            this.arm_right.rotateAngleX = Mth.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
            this.arm_left.rotateAngleX = Mth.cos(limbSwing * 0.5F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.leg_right.rotateAngleX = Mth.cos(limbSwing * 0.5F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.leg_left.rotateAngleX = Mth.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
        }

        // Sitting Animation
        if (big_cat.sitProgress > 0) {
            this.progressRotation(main_body, big_cat.sitProgress, -0.5462880558742251F, 0.0F, 0.0F, 40);
            this.progressRotation(main_buttocks, big_cat.sitProgress, -0.7285004297824331F, 0.0F, 0.0F, 40);
            this.progressRotation(arm_left, big_cat.sitProgress, 0.18203784098300857F, 0.0F, 0.0F, 40);
            this.progressRotation(arm_right, big_cat.sitProgress, 0.18203784098300857F, 0.0F, 0.0F, 40);
            this.progressRotation(head_face, big_cat.sitProgress, 0.36425021489121656F, 0.0F, 0.0F, 40);
            this.progressRotation(leg_right, big_cat.sitProgress, -0.045553093477052F, 0.0F, 0.22759093446006054F, 40);
            this.progressRotation(leg_left, big_cat.sitProgress, -0.045553093477052F, 0.0F, -0.22759093446006054F, 40);
            this.progressRotation(tail_1, big_cat.sitProgress, 1.2685004297824331F, 0.0F, 0.0f, 40);
            this.progressPosition(main_body, big_cat.sitProgress, 0.0F, 17.5F, -1.0F, 40);
            this.progressPosition(main_buttocks, big_cat.sitProgress, 0.0F, 2.0F, 3.5F, 40);
        }

        // Sleeping Animation
        else if (big_cat.isSleeping()) {
            this.progressRotation(main_buttocks, big_cat.sleepProgress, -0.136659280431156F, 0.0F, 0.0F, 40);
            this.progressRotation(leg_right, big_cat.sleepProgress, 1.5025539530419183F, -0.40980330836826856F, 0.0F, 40);
            this.progressRotation(leg_left, big_cat.sleepProgress, 1.5025539530419183F, 0.40980330836826856F, 0.0F, 40);
            this.progressRotation(arm_left, big_cat.sleepProgress, -1.3658946726107624F, -0.36425021489121656F, 0.0F, 40);
            this.progressRotation(arm_right, big_cat.sleepProgress, -1.3658946726107624F, 0.36425021489121656F, 0.0F, 40);
            this.progressPosition(main_body, big_cat.sleepProgress, 0.0F, 22.0F, -2.0F, 40);
            this.progressPosition(main_buttocks, big_cat.sleepProgress, 0.0F, 0.0F, 6.0F, 40);
            this.progressPosition(head_face, big_cat.sleepProgress, 0.0F, -0.5F, -2.5F, 40);
            this.progressPosition(arm_right, big_cat.sleepProgress, -2.5F, 0.0F, -1.0F, 40);
            this.progressPosition(arm_left, big_cat.sleepProgress, 2.5F, 0.0F, -1.0F, 40);
            this.progressPosition(leg_right, big_cat.sleepProgress, -2.8F, 0.0F, 1.0F, 40);
            this.progressPosition(leg_left, big_cat.sleepProgress, 2.8F, 0.0F, 1.0F, 40);
        }
    }
}
