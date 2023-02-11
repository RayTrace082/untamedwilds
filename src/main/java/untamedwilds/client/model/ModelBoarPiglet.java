package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import untamedwilds.entity.mammal.EntityBoar;

public class ModelBoarPiglet extends AdvancedEntityModel<EntityBoar> {
    
    public AdvancedModelBox main_body;
    public AdvancedModelBox leg_right;
    public AdvancedModelBox leg_left;
    public AdvancedModelBox head_main;
    public AdvancedModelBox arm_right;
    public AdvancedModelBox arm_left;
    public AdvancedModelBox shape14;
    public AdvancedModelBox head_snout;
    public AdvancedModelBox ear_left;
    public AdvancedModelBox ear_right;
    public AdvancedModelBox eye_left;
    public AdvancedModelBox eye_right;

    public ModelBoarPiglet() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.arm_left = new AdvancedModelBox(this, 42, 40);
        this.arm_left.setRotationPoint(1.3F, 0.2F, -2.99F);
        this.arm_left.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(arm_left, 0.045553093477052F, 0.0F, 0.0F);
        this.shape14 = new AdvancedModelBox(this, 42, 59);
        this.shape14.setRotationPoint(0.0F, -2.5F, 4.0F);
        this.shape14.addBox(-1.5F, 0.0F, 0.0F, 3, 3, 0, 0.0F);
        this.setRotateAngle(shape14, 0.18203784098300857F, 0.0F, 0.0F);
        this.main_body = new AdvancedModelBox(this, 24, 40);
        this.main_body.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.main_body.addBox(-2.0F, -2.5F, -4.0F, 4, 5, 8, 0.0F);
        this.setRotateAngle(main_body, -0.045553093477052F, 0.0F, 0.0F);
        this.ear_right = new AdvancedModelBox(this, 24, 43);
        this.ear_right.mirror = true;
        this.ear_right.setRotationPoint(-1.1F, -1.2F, -1.5F);
        this.ear_right.addBox(-3.0F, -1.0F, 0.0F, 3, 2, 1, 0.0F);
        this.setRotateAngle(ear_right, 0.27314402793711257F, 0.7740535232594852F, 0.5462880558742251F);
        this.eye_right = new AdvancedModelBox(this, 0, 20);
        this.eye_right.setRotationPoint(-2.01F, -1.0F, -2.0F);
        this.eye_right.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.leg_right = new AdvancedModelBox(this, 42, 40);
        this.leg_right.mirror = true;
        this.leg_right.setRotationPoint(-1.3F, -0.3F, 3.0F);
        this.leg_right.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(leg_right, 0.045553093477052F, 0.0F, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 0, 20);
        this.eye_left.setRotationPoint(2.01F, -1.0F, -2.0F);
        this.eye_left.addBox(0.0F, -0.5F, -1.0F, 0, 1, 2, 0.0F);
        this.ear_left = new AdvancedModelBox(this, 24, 43);
        this.ear_left.setRotationPoint(1.1F, -1.2F, -1.5F);
        this.ear_left.addBox(0.0F, -1.0F, 0.0F, 3, 2, 1, 0.0F);
        this.setRotateAngle(ear_left, 0.27314402793711257F, -0.7740535232594852F, -0.5462880558742251F);
        this.leg_left = new AdvancedModelBox(this, 42, 40);
        this.leg_left.setRotationPoint(1.3F, -0.3F, 3.0F);
        this.leg_left.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(leg_left, 0.045553093477052F, 0.0F, 0.0F);
        this.head_main = new AdvancedModelBox(this, 24, 53);
        this.head_main.setRotationPoint(0.0F, -0.8F, -3.0F);
        this.head_main.addBox(-2.0F, -2.5F, -4.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(head_main, 0.31869712141416456F, 0.0F, 0.0F);
        this.arm_right = new AdvancedModelBox(this, 42, 40);
        this.arm_right.mirror = true;
        this.arm_right.setRotationPoint(-1.3F, 0.0F, -2.99F);
        this.arm_right.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(arm_right, 0.045553093477052F, 0.0F, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 40, 53);
        this.head_snout.setRotationPoint(0.0F, -0.5F, -2.7F);
        this.head_snout.addBox(-1.5F, -1.0F, -3.0F, 3, 3, 3, 0.0F);
        this.setRotateAngle(head_snout, 0.18203784098300857F, 0.0F, 0.0F);
        this.main_body.addChild(this.arm_left);
        this.main_body.addChild(this.shape14);
        this.head_main.addChild(this.ear_right);
        this.head_main.addChild(this.eye_right);
        this.main_body.addChild(this.leg_right);
        this.head_main.addChild(this.eye_left);
        this.head_main.addChild(this.ear_left);
        this.main_body.addChild(this.leg_left);
        this.main_body.addChild(this.head_main);
        this.main_body.addChild(this.arm_right);
        this.head_main.addChild(this.head_snout);
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main_body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(     main_body,
                leg_right,
                leg_left,
             head_main,
                arm_right,
                arm_left,
             shape14,
             head_snout,
             ear_left,
             ear_right,
             eye_left,
             eye_right
        );
    }

    public void setupAnim(EntityBoar bear, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();

        // Breathing Animation
        this.main_body.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(main_body, 0.4F * 1.5f, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);

        // Blinking Animation
        if (!bear.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-0.49F, 0, -2F);
            this.eye_left.setRotationPoint(0.49F, 0, -2F);
        }

        // Head Tracking Animation
        this.faceTarget(netHeadYaw, headPitch, 1, head_main);

        // Movement Animation
        if (bear.canMove()) {
            this.arm_right.rotateAngleX = Mth.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
            this.arm_left.rotateAngleX = Mth.cos(limbSwing * 0.5F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.leg_right.rotateAngleX = Mth.cos(limbSwing * 0.5F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.leg_left.rotateAngleX = Mth.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
        }

        // Sitting Animation
        if (bear.sitProgress > 0) {
            this.progressRotation(main_body, bear.sitProgress, -0.5462880558742251F, 0.0F, 0.0F, 40);
            this.progressRotation(arm_left, bear.sitProgress, 0.18203784098300857F, 0.0F, 0.0F, 40);
            this.progressRotation(arm_right, bear.sitProgress, 0.18203784098300857F, 0.0F, 0.0F, 40);
            this.progressRotation(head_main, bear.sitProgress, 0.36425021489121656F, 0.0F, 0.0F, 40);
            this.progressRotation(leg_right, bear.sitProgress, -0.045553093477052F, 0.0F, 0.22759093446006054F, 40);
            this.progressRotation(leg_left, bear.sitProgress, -0.045553093477052F, 0.0F, -0.22759093446006054F, 40);
            this.progressPosition(main_body, bear.sitProgress, 0.0F, 17.5F, -1.0F, 40);
        }

        // Sleeping Animation
        else if (bear.sleepProgress > 0) {
            this.progressRotation(leg_right, bear.sleepProgress, 1.5025539530419183F, -0.40980330836826856F, 0.0F, 40);
            this.progressRotation(leg_left, bear.sleepProgress, 1.5025539530419183F, 0.40980330836826856F, 0.0F, 40);
            this.progressRotation(arm_left, bear.sleepProgress, -1.3658946726107624F, -0.36425021489121656F, 0.0F, 40);
            this.progressRotation(arm_right, bear.sleepProgress, -1.3658946726107624F, 0.36425021489121656F, 0.0F, 40);
            this.progressPosition(main_body, bear.sleepProgress, 0.0F, 22.0F, -2.0F, 40);
            this.progressPosition(head_main, bear.sleepProgress, 0.0F, -0.5F, -2.5F, 40);
            this.progressPosition(arm_right, bear.sleepProgress, -2.5F, 0.0F, -1.0F, 40);
            this.progressPosition(arm_left, bear.sleepProgress, 2.5F, 0.0F, -1.0F, 40);
            this.progressPosition(leg_right, bear.sleepProgress, -2.8F, 0.0F, 1.0F, 40);
            this.progressPosition(leg_left, bear.sleepProgress, 2.8F, 0.0F, 1.0F, 40);
        }
    }

   
}
