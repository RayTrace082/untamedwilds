package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.entity.mammal.bear.AbstractBear;

@OnlyIn(Dist.CLIENT)
public class ModelBearCub extends AdvancedEntityModel<AbstractBear>
{
    private final AdvancedModelBox body_main;
    private final AdvancedModelBox body_buttocks;
    private final AdvancedModelBox head_face;
    private final AdvancedModelBox arm_right;
    private final AdvancedModelBox arm_left;
    private final AdvancedModelBox leg_left;
    private final AdvancedModelBox leg_right;
    private final AdvancedModelBox head_snout;
    private final AdvancedModelBox ear_left;
    private final AdvancedModelBox ear_right;
    private final AdvancedModelBox eye_left;
    private final AdvancedModelBox eye_right;

    public ModelBearCub() {
        this.textureWidth = 128;
        this.textureHeight = 64;

        this.arm_right = new AdvancedModelBox(this, 85, 53);
        this.arm_right.setRotationPoint(-2.5F, -1.0F, -1.0F);
        this.arm_right.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.body_buttocks = new AdvancedModelBox(this, 98, 50);
        this.body_buttocks.setRotationPoint(0.0F, 0.75F, 6.0F);
        this.body_buttocks.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7, 0.0F);
        this.setRotateAngle(body_buttocks, -0.136659280431156F, 0.0F, 0.0F);
        this.ear_left = new AdvancedModelBox(this, 14, 55);
        this.ear_left.mirror = true;
        this.ear_left.setRotationPoint(2.5F, -2.5F, -2.5F);
        this.ear_left.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(ear_left, -0.31869712141416456F, -0.31869712141416456F, 0.7285004297824331F);
        this.ear_right = new AdvancedModelBox(this, 14, 55);
        this.ear_right.setRotationPoint(-2.5F, -2.5F, -2.5F);
        this.ear_right.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(ear_right, -0.31869712141416456F, 0.31869712141416456F, -0.7285004297824331F);
        this.arm_left = new AdvancedModelBox(this, 85, 53);
        this.arm_left.mirror = true;
        this.arm_left.setRotationPoint(2.5F, -1.0F, -1.0F);
        this.arm_left.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.leg_left = new AdvancedModelBox(this, 85, 53);
        this.leg_left.mirror = true;
        this.leg_left.setRotationPoint(2.8F, -1.9F, 1.0F);
        this.leg_left.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.setRotateAngle(leg_left, 0.136659280431156F, 0.0F, 0.0F);
        this.body_main = new AdvancedModelBox(this, 81, 41);
        this.body_main.setRotationPoint(0.0F, 17.0F, -2.0F);
        this.body_main.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.eye_right = new AdvancedModelBox(this, 20, 55);
        this.eye_right.setRotationPoint(-0.51F, 0.0F, -3.01F);
        this.eye_right.addBox(-2.0F, -0.5F, -1.0F, 2, 1, 1, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 18, 58);
        this.head_snout.setRotationPoint(0.0F, 1.0F, -1.3F);
        this.head_snout.addBox(-1.5F, -2.0F, -5.0F, 3, 3, 3, 0.0F);
        this.setRotateAngle(head_snout, 0.091106186954104F, 0.0F, 0.0F);
        this.leg_right = new AdvancedModelBox(this, 85, 53);
        this.leg_right.setRotationPoint(-2.8F, -1.9F, 1.0F);
        this.leg_right.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.setRotateAngle(leg_right, 0.136659280431156F, 0.0F, 0.0F);
        this.eye_left = new AdvancedModelBox(this, 20, 55);
        this.eye_left.mirror = true;
        this.eye_left.setRotationPoint(0.51F, 0.0F, -3.01F);
        this.eye_left.addBox(0.0F, -0.5F, -1.0F, 2, 1, 1, 0.0F);
        this.head_face = new AdvancedModelBox(this, 0, 55);
        this.head_face.setRotationPoint(0.0F, -1.5F, -2.5F);
        this.head_face.addBox(-2.5F, -2.5F, -4.0F, 5, 5, 4, 0.0F);
        this.setRotateAngle(head_face, 0.18203784098300857F, 0.0F, 0.0F);
        this.body_main.addChild(this.arm_right);
        this.body_main.addChild(this.body_buttocks);
        this.head_face.addChild(this.ear_left);
        this.head_face.addChild(this.ear_right);
        this.body_main.addChild(this.arm_left);
        this.body_buttocks.addChild(this.leg_left);
        this.head_face.addChild(this.eye_right);
        this.head_face.addChild(this.head_snout);
        this.body_buttocks.addChild(this.leg_right);
        this.head_face.addChild(this.eye_left);
        this.body_main.addChild(this.head_face);
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main,
        body_buttocks,
        head_face,
        arm_right,
        arm_left,
        leg_left,
        leg_right,
        head_snout,
        ear_left,
        ear_right,
        eye_left,
        eye_right
        );
    }

    public void setRotationAngles(AbstractBear bear, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        this.faceTarget(netHeadYaw, headPitch, 1, head_face);
        this.body_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.body_buttocks.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(body_main, 0.4F * 1.5f, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left, 0.4F * 1.5f, 0.03F, false, -ageInTicks / 20, 2);

        if (!bear.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-0.49F, 0, -2F);
            this.eye_left.setRotationPoint(0.49F, 0, -2F);
        }
        if (bear.sitProgress != 0) {
            // Sitting Animation
            if (bear.isSitting()) {
                this.progressRotation(body_main, bear.sitProgress, -0.5462880558742251F, 0.0F, 0.0F, 40);
                this.progressRotation(body_buttocks, bear.sitProgress, -0.7285004297824331F, 0.0F, 0.0F, 40);
                this.progressRotation(arm_left, bear.sitProgress, 0.18203784098300857F, 0.0F, 0.0F, 40);
                this.progressRotation(arm_right, bear.sitProgress, 0.18203784098300857F, 0.0F, 0.0F, 40);
                this.progressRotation(head_face, bear.sitProgress, 0.36425021489121656F, 0.0F, 0.0F, 40);
                this.progressRotation(leg_right, bear.sitProgress, -0.045553093477052F, 0.0F, 0.22759093446006054F, 40);
                this.progressRotation(leg_left, bear.sitProgress, -0.045553093477052F, 0.0F, -0.22759093446006054F, 40);
                this.progressPosition(body_main, bear.sitProgress, 0.0F, 17.5F, -1.0F, 40);
                this.progressPosition(body_buttocks, bear.sitProgress, 0.0F, 2.0F, 3.5F, 40);
            }
            // Sleeping Animation
            if (bear.isSleeping()) {
                this.progressRotation(body_buttocks, bear.sitProgress, -0.136659280431156F, 0.0F, 0.0F, 40);
                this.progressRotation(leg_right, bear.sitProgress, 1.5025539530419183F, -0.40980330836826856F, 0.0F, 40);
                this.progressRotation(leg_left, bear.sitProgress, 1.5025539530419183F, 0.40980330836826856F, 0.0F, 40);
                this.progressRotation(arm_left, bear.sitProgress, -1.3658946726107624F, -0.36425021489121656F, 0.0F, 40);
                this.progressRotation(arm_right, bear.sitProgress, -1.3658946726107624F, 0.36425021489121656F, 0.0F, 40);
                this.progressPosition(body_main, bear.sitProgress, 0.0F, 22.0F, -2.0F, 40);
                this.progressPosition(body_buttocks, bear.sitProgress, 0.0F, 0.0F, 6.0F, 40);
                this.progressPosition(head_face, bear.sitProgress, 0.0F, -0.5F, -2.5F, 40);
                this.progressPosition(arm_right, bear.sitProgress, -2.5F, 0.0F, -1.0F, 40);
                this.progressPosition(arm_left, bear.sitProgress, 2.5F, 0.0F, -1.0F, 40);
                this.progressPosition(leg_right, bear.sitProgress, -2.8F, 0.0F, 1.0F, 40);
                this.progressPosition(leg_left, bear.sitProgress, 2.8F, 0.0F, 1.0F, 40);
            }
        }

        // Controls the walking animation
        if (bear.canMove()) {
            this.arm_right.rotateAngleX = MathHelper.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
            this.arm_left.rotateAngleX = MathHelper.cos(limbSwing * 0.5F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.leg_right.rotateAngleX = MathHelper.cos(limbSwing * 0.5F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.leg_left.rotateAngleX = MathHelper.cos(limbSwing * 0.5F) * 1.4F * limbSwingAmount;
        }
    }
}
