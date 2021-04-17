package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import untamedwilds.entity.mammal.bigcat.AbstractBigCat;

public class ModelBigCat extends AdvancedEntityModel<AbstractBigCat> {

    public AdvancedModelBox body_main;
    public AdvancedModelBox body_abdomen;
    public AdvancedModelBox head_neck;
    public AdvancedModelBox arm_right_upper;
    public AdvancedModelBox arm_left_upper;
    public AdvancedModelBox leg_right_upper;
    public AdvancedModelBox tail_1;
    public AdvancedModelBox leg_left_upper;
    public AdvancedModelBox leg_right_lower;
    public AdvancedModelBox leg_right_paw;
    public AdvancedModelBox tail_2;
    public AdvancedModelBox tail_3;
    public AdvancedModelBox tail_4;
    public AdvancedModelBox leg_left_lower;
    public AdvancedModelBox leg_left_paw;
    public AdvancedModelBox head_main;
    public AdvancedModelBox eye_right;
    public AdvancedModelBox eye_right_1;
    public AdvancedModelBox head_snout;
    public AdvancedModelBox head_jaw;
    public AdvancedModelBox ear_right;
    public AdvancedModelBox ear_left;
    public AdvancedModelBox head_cheek_right;
    public AdvancedModelBox head_cheek_left;
    public AdvancedModelBox head_snout_teeth;
    public AdvancedModelBox arm_right_lower;
    public AdvancedModelBox arm_right_paw;
    public AdvancedModelBox arm_left_lower;
    public AdvancedModelBox arm_left_paw;
    public AdvancedModelBox teeth_right;
    public AdvancedModelBox teeth_left;
    public AdvancedModelBox neck_mane;
    public AdvancedModelBox teeth_down_right;
    public AdvancedModelBox teeth_down_left;

    private final ModelAnimator animator;
    private static AdvancedModelBox[] bodyParts_tail;
    private float tailX = -1;

    public ModelBigCat() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.arm_left_lower = new AdvancedModelBox(this, 42, 33);
        this.arm_left_lower.mirror = true;
        this.arm_left_lower.setRotationPoint(1.0F, 7.0F, 0.5F);
        this.arm_left_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.setRotateAngle(arm_left_lower, -0.5009094953223726F, 0.0F, 0.091106186954104F);
        this.eye_right = new AdvancedModelBox(this, 19, 34);
        this.eye_right.setRotationPoint(-2.5F, -2.0F, -5.01F);
        this.eye_right.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 0, 0.0F);
        this.eye_right_1 = new AdvancedModelBox(this, 23, 34);
        this.eye_right_1.setRotationPoint(2.5F, -2.0F, -5.01F);
        this.eye_right_1.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 0, 0.0F);
        this.arm_left_upper = new AdvancedModelBox(this, 42, 20);
        this.arm_left_upper.mirror = true;
        this.arm_left_upper.setRotationPoint(2.0F, -2.0F, -2.5F);
        this.arm_left_upper.addBox(-1.0F, 0.0F, -2.5F, 4, 8, 5, 0.0F);
        this.setRotateAngle(arm_left_upper, 0.27314402793711257F, 0.0F, -0.091106186954104F);
        this.ear_left = new AdvancedModelBox(this, 20, 35);
        this.ear_left.mirror = true;
        this.ear_left.setRotationPoint(2F, -2.5F, -2.0F);
        this.ear_left.addBox(0.0F, -2.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(ear_left, 0.0F, -0.045553093477052F, 0.22759093446006054F);
        this.body_abdomen = new AdvancedModelBox(this, 40, 0);
        this.body_abdomen.setRotationPoint(0.0F, -1.0F, 6.0F);
        this.body_abdomen.addBox(-3.5F, -4.0F, 0.0F, 7, 10, 10, 0.0F);
        this.leg_left_paw = new AdvancedModelBox(this, 54, 40);
        this.leg_left_paw.setRotationPoint(0.0F, 6.8F, -1.0F);
        this.leg_left_paw.addBox(-2.0F, 0.0F, -2.0F, 4, 2, 4, 0.0F);
        this.setRotateAngle(leg_left_paw, 0.31869712141416456F, 0.0F, 0.0F);
        this.head_main = new AdvancedModelBox(this, 0, 34);
        this.head_main.setRotationPoint(0.0F, -0.5F, -4.5F);
        this.head_main.addBox(-3.5F, -3.0F, -5.0F, 7, 6, 5, 0.0F);
        this.setRotateAngle(head_main, 0.091106186954104F, 0.0F, 0.0F);
        this.arm_right_upper = new AdvancedModelBox(this, 42, 20);
        this.arm_right_upper.setRotationPoint(-2.0F, -2.0F, -2.5F);
        this.arm_right_upper.addBox(-3.0F, 0.0F, -2.5F, 4, 8, 5, 0.0F);
        this.setRotateAngle(arm_right_upper, 0.27314402793711257F, 0.0F, 0.091106186954104F);
        this.head_neck = new AdvancedModelBox(this, 0, 22);
        this.head_neck.setRotationPoint(0.0F, -1.5F, -4.5F);
        this.head_neck.addBox(-3.0F, -3.0F, -6.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(head_neck, -0.136659280431156F, 0.0F, 0.0F);
        this.leg_right_lower = new AdvancedModelBox(this, 58, 46);
        this.leg_right_lower.setRotationPoint(-1.0F, 5.5F, 3.0F);
        this.leg_right_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
        this.setRotateAngle(leg_right_lower, -0.136659280431156F, 0.0F, -0.045553093477052F);
        this.head_cheek_left = new AdvancedModelBox(this, 28, 40);
        this.head_cheek_left.setRotationPoint(2.0F, 0.0F, -2.0F);
        this.head_cheek_left.addBox(0.0F, -2.0F, -1.0F, 3, 5, 2, 0.0F);
        this.setRotateAngle(head_cheek_left, 0.0F, 0.0F, 0.22759093446006054F);
        this.tail_1 = new AdvancedModelBox(this, 74, 0);
        this.tail_1.setRotationPoint(0.0F, -2.5F, 9.0F);
        this.tail_1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(tail_1, -1.0016444577195458F, 0.0F, 0.0F);
        this.ear_right = new AdvancedModelBox(this, 20, 35);
        this.ear_right.setRotationPoint(-2F, -2.5F, -2.0F);
        this.ear_right.addBox(-2.0F, -2.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(ear_right, 0.0F, 0.045553093477052F, -0.22759093446006054F);
        this.tail_4 = new AdvancedModelBox(this, 74, 24);
        this.tail_4.setRotationPoint(0.0F, -0.1F, 5.5F);
        this.tail_4.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(tail_4, 0.6373942428283291F, 0.0F, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 0, 45);
        this.head_snout.setRotationPoint(0.0F, -1.2F, -4.0F);
        this.head_snout.addBox(-2.0F, 0.0F, -4.0F, 4, 3, 4, 0.0F);
        this.setRotateAngle(head_snout, 0.31869712141416456F, 0.0F, 0.0F);
        this.head_jaw = new AdvancedModelBox(this, 25, 35);
        this.head_jaw.setRotationPoint(0.0F, 1.9F, -3.5F);
        this.head_jaw.addBox(-2.0F, 0.0F, -3.5F, 4, 1, 3, 0.0F);
        this.setRotateAngle(head_jaw, 0.136659280431156F, 0.0F, 0.0F);
        this.head_jaw.scaleX = 0.95F;
        this.leg_left_upper = new AdvancedModelBox(this, 38, 46);
        this.leg_left_upper.mirror = true;
        this.leg_left_upper.setRotationPoint(2.0F, 0.0F, 6.0F);
        this.leg_left_upper.addBox(-1.0F, -2.0F, -3.0F, 4, 10, 6, 0.0F);
        this.setRotateAngle(leg_left_upper, -0.18203784098300857F, 0.0F, -0.045553093477052F);
        this.arm_left_paw = new AdvancedModelBox(this, 54, 34);
        this.arm_left_paw.mirror = true;
        this.arm_left_paw.setRotationPoint(0.0F, 7.0F, -1.0F);
        this.arm_left_paw.addBox(-2.0F, 0.0F, -2.0F, 4, 2, 4, 0.0F);
        this.setRotateAngle(arm_left_paw, 0.22759093446006054F, 0.0F, 0.0F);
        this.leg_right_upper = new AdvancedModelBox(this, 38, 46);
        this.leg_right_upper.setRotationPoint(-2.0F, 0.0F, 6.0F);
        this.leg_right_upper.addBox(-3.0F, -2.0F, -3.0F, 4, 10, 6, 0.0F);
        this.setRotateAngle(leg_right_upper, -0.18203784098300857F, 0.0F, 0.045553093477052F);
        this.head_snout_teeth = new AdvancedModelBox(this, 16, 45);
        this.head_snout_teeth.setRotationPoint(0.0F, 2.0F, 0.1F);
        this.head_snout_teeth.addBox(-2.0F, 0.0F, -4.0F, 4, 2, 4, 0.0F);
        this.arm_right_paw = new AdvancedModelBox(this, 54, 34);
        this.arm_right_paw.setRotationPoint(0.0F, 7.0F, -1.0F);
        this.arm_right_paw.addBox(-2.0F, 0.0F, -2.0F, 4, 2, 4, 0.0F);
        this.setRotateAngle(arm_right_paw, 0.22759093446006054F, 0.0F, 0.0F);
        this.leg_left_lower = new AdvancedModelBox(this, 58, 46);
        this.leg_left_lower.mirror = true;
        this.leg_left_lower.setRotationPoint(1.0F, 5.5F, 3.0F);
        this.leg_left_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3, 0.0F);
        this.setRotateAngle(leg_left_lower, -0.136659280431156F, 0.0F, 0.045553093477052F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 11.0F, -4.0F);
        this.body_main.addBox(-4.0F, -5.0F, -6.0F, 8, 10, 12, 0.0F);
        this.leg_right_paw = new AdvancedModelBox(this, 54, 40);
        this.leg_right_paw.mirror = true;
        this.leg_right_paw.setRotationPoint(0.0F, 6.8F, -1.0F);
        this.leg_right_paw.addBox(-2.0F, 0.0F, -2.0F, 4, 2, 4, 0.0F);
        this.setRotateAngle(leg_right_paw, 0.31869712141416456F, 0.0F, 0.0F);
        this.arm_right_lower = new AdvancedModelBox(this, 42, 33);
        this.arm_right_lower.setRotationPoint(-1.0F, 7.0F, 0.5F);
        this.arm_right_lower.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
        this.setRotateAngle(arm_right_lower, -0.5009094953223726F, 0.0F, -0.091106186954104F);
        this.tail_2 = new AdvancedModelBox(this, 74, 8);
        this.tail_2.setRotationPoint(0.0F, -0.1F, 5.5F);
        this.tail_2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(tail_2, -0.22759093446006054F, 0.0F, 0.0F);
        this.head_cheek_right = new AdvancedModelBox(this, 28, 40);
        this.head_cheek_right.mirror = true;
        this.head_cheek_right.setRotationPoint(-2.0F, 0.0F, -2.0F);
        this.head_cheek_right.addBox(-3.0F, -2.0F, -1.0F, 3, 5, 2, 0.0F);
        this.setRotateAngle(head_cheek_right, 0.0F, 0.0F, -0.22759093446006054F);
        this.tail_3 = new AdvancedModelBox(this, 74, 16);
        this.tail_3.setRotationPoint(0.0F, 0.0F, 5.5F);
        this.tail_3.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(tail_3, 0.31869712141416456F, 0.0F, 0.0F);
        this.teeth_right = new AdvancedModelBox(this, 16, 52);
        this.teeth_right.setRotationPoint(-1.48F, 2.4F, -2.70F);
        this.teeth_right.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(teeth_right, 0.0F, 0.0F, 0.045553093477052F);
        this.teeth_left = new AdvancedModelBox(this, 16, 52);
        this.teeth_left.setRotationPoint(1.48F, 2.4F, -2.70F);
        this.teeth_left.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(teeth_left, 0.0F, 0.0F, -0.045553093477052F);

        this.neck_mane = new AdvancedModelBox(this, 70, 44);
        this.neck_mane.setRotationPoint(0F, -1F, -2.60F);
        this.neck_mane.addBox(-5.5F, -3.5F, -4.0F, 11, 12, 8, 0.0F);

        this.teeth_down_right = new AdvancedModelBox(this, 22, 52);
        this.teeth_down_right.setRotationPoint(-1.48F, 0.8F, -2.70F);
        this.teeth_down_right.addBox(-0.5F, -4.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(teeth_down_right, 0.091106186954104F, 0.0F, -0.18203784098300857F);
        this.teeth_down_left = new AdvancedModelBox(this, 22, 52);
        this.teeth_down_left.setRotationPoint(1.48F, 0.8F, -2.70F);
        this.teeth_down_left.addBox(-0.5F, -4.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(teeth_down_left, 0.091106186954104F, 0.0F, 0.18203784098300857F);

        this.head_jaw.addChild(this.teeth_down_left);
        this.head_jaw.addChild(this.teeth_down_right);
        this.head_neck.addChild(this.neck_mane);
        this.head_snout.addChild(this.teeth_right);
        this.head_snout.addChild(this.teeth_left);
        this.arm_left_upper.addChild(this.arm_left_lower);
        this.head_main.addChild(this.eye_right);
        this.body_main.addChild(this.arm_left_upper);
        this.head_main.addChild(this.ear_left);
        this.body_main.addChild(this.body_abdomen);
        this.leg_left_lower.addChild(this.leg_left_paw);
        this.head_neck.addChild(this.head_main);
        this.head_main.addChild(this.eye_right_1);
        this.body_main.addChild(this.arm_right_upper);
        this.body_main.addChild(this.head_neck);
        this.leg_right_upper.addChild(this.leg_right_lower);
        this.head_main.addChild(this.head_cheek_left);
        this.body_abdomen.addChild(this.tail_1);
        this.head_main.addChild(this.ear_right);
        this.tail_3.addChild(this.tail_4);
        this.head_main.addChild(this.head_snout);
        this.head_main.addChild(this.head_jaw);
        this.body_abdomen.addChild(this.leg_left_upper);
        this.arm_left_lower.addChild(this.arm_left_paw);
        this.body_abdomen.addChild(this.leg_right_upper);
        this.head_snout.addChild(this.head_snout_teeth);
        this.arm_right_lower.addChild(this.arm_right_paw);
        this.leg_left_upper.addChild(this.leg_left_lower);
        this.leg_right_lower.addChild(this.leg_right_paw);
        this.arm_right_upper.addChild(this.arm_right_lower);
        this.tail_1.addChild(this.tail_2);
        this.head_main.addChild(this.head_cheek_right);
        this.tail_2.addChild(this.tail_3);

        bodyParts_tail = new AdvancedModelBox[]{tail_1, tail_2, tail_3, tail_4};
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of( body_main, body_abdomen, head_neck, arm_right_upper, arm_left_upper, leg_right_upper,
            tail_1, leg_left_upper, leg_right_lower, leg_right_paw, tail_2, tail_3, tail_4, leg_left_lower, leg_left_paw, head_main,
            eye_right, eye_right_1, head_snout, head_jaw, ear_right, ear_left, head_cheek_right, head_cheek_left, head_snout_teeth,
            arm_right_lower, arm_right_paw, arm_left_lower, arm_left_paw);
    }

    private void animate(IAnimatedEntity entityIn) {
        AbstractBigCat big_cat = (AbstractBigCat) entityIn;
        animator.update(big_cat);

        animator.setAnimation(AbstractBigCat.IDLE_TALK);
        animator.startKeyframe(10);
        this.rotate(animator, head_jaw, 26.09F, 0, 0);
        this.rotate(animator, head_main, -26.09F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(10);

        animator.setAnimation(AbstractBigCat.ATTACK_POUNCE);
        animator.startKeyframe(12);
        this.rotate(animator, body_main, -18.26F, 0, 0);
        animator.move(body_main, 0, -2, 0);
        this.rotate(animator, body_abdomen, -7.83F, 0, 0);
        this.rotate(animator, head_jaw, 52.17F, 0, 0);
        this.rotate(animator, arm_left_upper, -20.87F, 0, -20.87F);
        this.rotate(animator, arm_left_lower, -60F, 0, 7.83F);
        this.rotate(animator, arm_left_paw, 52.17F, 0, 0);
        animator.move(arm_left_paw, 0, 1.5F, 0);
        this.rotate(animator, arm_right_upper, -20.87F, 0, 20.87F);
        this.rotate(animator, arm_right_lower, -60F, 0, -7.83F);
        this.rotate(animator, arm_right_paw, 52.17F, 0, 0);
        animator.move(arm_right_paw, 0, 1.5F, 0);
        this.rotate(animator, leg_left_upper, 15.65F, 0, -2.61F);
        animator.move(leg_left_lower, 0, -1.5F, 0);
        this.rotate(animator, leg_right_upper, 15.65F, 0, 2.61F);
        animator.move(leg_right_lower, 0, -1.5F, 0);
        this.rotate(animator, tail_1, -15.65F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(6);
        this.rotate(animator, body_main, 2.61F, 0, 0);
        //animator.move(body_main, 0, -6, 0);
        this.rotate(animator, body_abdomen, -7.83F, 0, 0);
        this.rotate(animator, head_neck, -10.43F, -5.22F, 7.83F);
        this.rotate(animator, head_main, 0, 2.61F, -18.26F);
        this.rotate(animator, head_jaw, 52.17F, 0, 0);
        this.rotate(animator, arm_left_upper, -52.17F, -18.26F, -10.44F);
        this.rotate(animator, arm_left_lower, -60F, 0, 7.83F);
        this.rotate(animator, arm_left_paw, 60F, 0, 0);
        animator.move(arm_left_paw, 0, 1.5F, 0);
        this.rotate(animator, arm_right_upper, -52.17F, 18.26F, 10.44F);
        this.rotate(animator, arm_right_lower, -60F, 0, -7.83F);
        this.rotate(animator, arm_right_paw, 60F, 0, 0);
        animator.move(arm_right_paw, 0, 1.5F, 0);
        this.rotate(animator, leg_left_upper, 62.61F, 15.65F, -2.61F);
        this.rotate(animator, leg_left_paw, 78.26F, 0, 0);
        animator.move(leg_left_paw, 0, 1F, 1F);
        this.rotate(animator, leg_right_upper, 62.61F, -15.65F, 2.61F);
        this.rotate(animator, leg_right_paw, 78.26F, 0, 0);
        animator.move(leg_right_paw, 0, 1F, 1F);
        this.rotate(animator, tail_1, -15.65F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(10);
        this.rotate(animator, body_main, 23.48F, 0, 0);
        //animator.move(body_main, 0, -2, 0);
        this.rotate(animator, body_abdomen, -23.48F, 0, 0);
        this.rotate(animator, head_main, 0, 2.61F, -18.26F);
        this.rotate(animator, arm_left_upper, 5.22F, 0, -15.65F);
        this.rotate(animator, arm_left_lower, -60F, 0, 23.48F);
        animator.move(arm_left_upper, 0, 1F, 0);
        this.rotate(animator, arm_left_paw, 23.48F, 0, 0);
        this.rotate(animator, arm_right_upper, 5.22F, 0, 15.65F);
        this.rotate(animator, arm_right_lower, -60F, 0, -23.48F);
        animator.move(arm_right_upper, 0, 1F, 0);
        this.rotate(animator, arm_right_paw, 23.48F, 0, 0);
        this.rotate(animator, leg_left_upper, 2.61F, -5.22F, -10.43F);
        //this.rotate(animator, leg_left_paw, 78.26F, 0, 0);
        animator.move(leg_left_paw, 0, 1F, 1F);
        this.rotate(animator, leg_right_upper, 2.61F, 5.22F, 10.43F);
        //this.rotate(animator, leg_right_paw, 78.26F, 0, 0);
        animator.move(leg_right_paw, 0, 1F, 1F);
        this.rotate(animator, tail_1, -15.65F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(6);
        this.rotate(animator, body_main, 7.83F, 0, 0);
        animator.move(body_main, 0, 2, 0);
        this.rotate(animator, body_abdomen, -10.43F, 0, 0);
        this.rotate(animator, head_main, 2.61F, 7.83F, -2.61F);
        this.rotate(animator, arm_left_upper, 39.13F, 0, -15.65F);
        animator.move(arm_left_upper, 0, 3, 0);
        this.rotate(animator, arm_left_lower, -60F, 0, 23.48F);
        this.rotate(animator, arm_left_paw, 23.48F, 0, 0);
        this.rotate(animator, arm_right_upper, 39.13F, 0, 15.65F);
        animator.move(arm_right_upper, 0, 3, 0);
        this.rotate(animator, arm_right_lower, -60F, 0, -23.48F);
        this.rotate(animator, arm_right_paw, 23.48F, 0, 0);
        this.rotate(animator, leg_left_upper, -5.22F, 0, -2.61F);
        this.rotate(animator, leg_right_upper, -5.22F, 0, 2.61F);
        this.rotate(animator, tail_1, -15.65F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(8);

        animator.setAnimation(AbstractBigCat.ATTACK_MAUL);
        animator.startKeyframe(8);
        animator.move(body_main, 0, -0.7F, 0);
        this.rotate(animator, body_main, -5.22F, -10.43F, 7.83F);
        this.rotate(animator, body_abdomen, -5.22F, 10.43F, -7.83F);
        this.rotate(animator, head_main, 31.30F, -13.04F, 23.48F);
        animator.move(arm_right_upper, -1, -1, 0);
        this.rotate(animator, arm_right_upper, -78.26F, 33.91F, -10.43F);
        this.rotate(animator, arm_right_lower, -62.61F, -5.22F, -5.22F);
        animator.move(arm_right_paw, 0, 2F, 1.4F);
        this.rotate(animator, arm_right_paw, 164.35F, 0, 0);
        animator.move(arm_left_upper, 0, 0.9F, 0);
        this.rotate(animator, arm_left_upper, 23.48F, 10.43F, -10.43F);
        this.rotate(animator, leg_right_upper, -2.61F, 0, 2.61F);
        this.rotate(animator, leg_left_upper, -2.61F, 0, -2.61F);
        animator.endKeyframe();
        animator.startKeyframe(6);
        animator.move(body_main, 0, 1.9F, 0);
        this.rotate(animator, body_main, 13.04F, -10.43F, 7.83F);
        this.rotate(animator, body_abdomen, -23.48F, 10.43F, -7.83F);
        this.rotate(animator, head_main, 39.13F, 7.83F, 0);
        this.rotate(animator, head_jaw, 33.91F, 0, 0);
        this.rotate(animator, head_neck, -54.78F, 0, 0);
        animator.move(arm_right_upper, 0, 0, -2);
        this.rotate(animator, arm_right_upper, -73.04F, 13.04F, -10.43F);
        this.rotate(animator, arm_right_lower, -13.04F, -5.22F, -5.22F);
        animator.move(arm_right_paw, 0, 0, -0.7F);
        this.rotate(animator, arm_right_paw, 117.39F, 0, 0);
        animator.move(arm_left_upper, 0, -1.2F, 0);
        this.rotate(animator, arm_left_upper, 15.65F, 10.43F, -10.43F);
        this.rotate(animator, arm_left_lower, -60, 0, 5.22F);
        this.rotate(animator, arm_left_paw, 31.30F, 0, 0);
        this.rotate(animator, leg_right_upper, -2.61F, 0, 2.61F);
        this.rotate(animator, leg_left_upper, -2.61F, 0, -2.61F);
        animator.endKeyframe();
        animator.resetKeyframe(8);

        animator.setAnimation(AbstractBigCat.IDLE_STRETCH);
        animator.startKeyframe(20);
        animator.move(body_main, 0, 5, 6);
        this.rotate(animator, body_main, 18.26F, 0, 0);
        this.rotate(animator, head_neck, -15.65F, 0, 0);
        this.rotate(animator, head_main, -7.83F, 0, 0);
        animator.move(body_abdomen, 0, 0, -2);
        this.rotate(animator, body_abdomen, 20.87F, 0, 0);
        animator.move(arm_left_upper, 0, -2, 0);
        //this.rotate(animator, arm_left_upper, -44.35F, 0, -5.22F);
        this.rotate(animator, arm_left_upper, -62, 0, -5.22F);
        this.rotate(animator, arm_left_paw, 57.39F, 0, 0);
        animator.move(arm_right_upper, 0, -2, 0);
        //this.rotate(animator, arm_right_upper, -44.35F, 0, 5.22F);
        this.rotate(animator, arm_right_upper, -62, 0, -5.22F);
        this.rotate(animator, arm_right_paw, 57.39F, 0, 0);
        this.rotate(animator, leg_left_upper, -39.14F, 0, 0);
        this.rotate(animator, leg_right_upper, -39.14F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(70);
        animator.move(body_main, 0, 5, 6);
        this.rotate(animator, body_main, 18.26F, 0, 0);
        this.rotate(animator, head_neck, -15.65F, 0, 0);
        this.rotate(animator, head_main, -7.83F, 0, 0);
        animator.move(body_abdomen, 0, 0, -2);
        this.rotate(animator, body_abdomen, 20.87F, 0, 0);
        animator.move(arm_left_upper, 0, -2, 0);
        //this.rotate(animator, arm_left_upper, -44.35F, 0, -5.22F);
        this.rotate(animator, arm_left_upper, -62, 0, -5.22F);
        this.rotate(animator, arm_left_paw, 57.39F, 0, 0);
        animator.move(arm_right_upper, 0, -2, 0);
        //this.rotate(animator, arm_right_upper, -44.35F, 0, 5.22F);
        this.rotate(animator, arm_right_upper, -62, 0, -5.22F);
        this.rotate(animator, arm_right_paw, 57.39F, 0, 0);
        this.rotate(animator, leg_left_upper, -39.14F, 0, 0);
        this.rotate(animator, leg_right_upper, -39.14F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(20);
    }

    public void setRotationAngles(AbstractBigCat big_cat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(big_cat);
        float globalSpeed = 2.4f;
        float globalDegree = 1f;
        limbSwingAmount = Math.min(0.6F, limbSwingAmount * 2);
        limbSwing *= 0.5F;

        // Breathing Animation
        boolean isPurring = big_cat.getAnimation() == AbstractBigCat.IDLE_STRETCH && big_cat.getAnimationTick() > 20;
        final double scaleX = Math.sin(ageInTicks * (isPurring ? 2 : 1 / 20F));
        final double scaleY = Math.sin(ageInTicks / (isPurring ? 8 : 16));
        this.body_main.setScale((float) (1F + scaleX * 0.08F), (float) (1F + scaleY * 0.06F), 1.0F);
        this.body_abdomen.setScale((float) (1F + scaleX * 0.06F), (float) (1F + scaleY * 0.06F), 1.0F);
        bob(body_main, 0.4F * globalSpeed, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right_upper, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left_upper, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right_upper, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left_upper, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        if (!big_cat.isSleeping()) {
            chainFlap(bodyParts_tail, 0.3F * globalSpeed, 0.25F * globalDegree, 0.5F, ageInTicks / 6, 2);
        }

        // Blinking Animation
        if (!big_cat.shouldRenderEyes()) {
            this.eye_right.setRotationPoint(-2.5F, -2.0F, -4.5F);
            this.eye_right_1.setRotationPoint(2.5F, -2.0F, -4.5F);
        }
        
        // Head Tracking Animation
        if (!big_cat.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 3, head_neck);
            this.faceTarget(netHeadYaw, headPitch, 3, head_main);
        }

        // Pitch/Yaw handler
        if (big_cat.isInWater() && !big_cat.isOnGround()) {
            limbSwing = ageInTicks / 3;
            limbSwingAmount = 0.5f;
            this.body_main.rotationPointY += 4; // Model offset to make the Big Cat "sink" in water (while not drowning)
            this.setRotateAngle(head_neck, -0.18203784098300857F, 0.0F, 0.0F);
            float pitch = MathHelper.clamp(big_cat.rotationPitch - 10, -25F, 25.0F);
            this.setRotateAngle(body_main, (float) (pitch * Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        float newZ = MathHelper.lerp(0.4F, this.tailX, this.tail_1.defaultRotationX + (float)big_cat.getSpeed() * 2);
        this.tail_1.rotateAngleX = newZ;
        this.tailX = newZ;
        if (big_cat.canMove()) {
            if (big_cat.getSpeed() > 0.1f || big_cat.isAngry()) { // Running animation
                bob(body_main, 0.5F * globalSpeed, 0.5F, false, limbSwing, limbSwingAmount);
                walk(body_main, 0.5f * globalSpeed, 0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                walk(head_neck, 0.5f * globalSpeed, -0.5f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                walk(body_abdomen, 0.5f * globalSpeed, 0.3f * globalDegree, false, 0.5F, 0f, limbSwing, limbSwingAmount);
                bob(arm_right_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_upper, 0.5f * globalSpeed, globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.2F, 0.2f, limbSwing, limbSwingAmount);
                walk(arm_right_paw, 0.5f * globalSpeed, 2f * globalDegree, false, 0.4F, 1.0f, limbSwing, limbSwingAmount);
                bob(arm_left_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_upper, 0.5f * globalSpeed, globalDegree, true, 0.6F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.8F, 0.2f, limbSwing, limbSwingAmount);
                walk(arm_left_paw, 0.5f * globalSpeed, 2f * globalDegree, false, 1F, 1.0f, limbSwing, limbSwingAmount);
                bob(leg_right_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_upper, 0.5f * globalSpeed, globalDegree, true, 1.4F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 1.6F, 0.2f, limbSwing, limbSwingAmount);
                walk(leg_right_paw, 0.5f * globalSpeed, 0.8f * globalDegree, false, 1.8F, 0.0f, limbSwing, limbSwingAmount);
                bob(leg_left_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_upper, 0.5f * globalSpeed, globalDegree, true, 2F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.2F, 0.2f, limbSwing, limbSwingAmount);
                walk(leg_left_paw, 0.5f * globalSpeed, 0.8f * globalDegree, false, 2.4F, 0.0f, limbSwing, limbSwingAmount);
            } else { // Walking Animation
                bob(arm_right_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_right_upper, 0.5f * globalSpeed, globalDegree, true, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 0.2F, 0.2f, limbSwing, limbSwingAmount);
                walk(arm_right_paw, 0.5f * globalSpeed, 2f * globalDegree, false, 0.4F, 1.0f, limbSwing, limbSwingAmount);
                bob(arm_left_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(arm_left_upper, 0.5f * globalSpeed, globalDegree, true, 2.4F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 2.6F, 0.2f, limbSwing, limbSwingAmount);
                walk(arm_left_paw, 0.5f * globalSpeed, 2f * globalDegree, false, 2.8F, 1.0f, limbSwing, limbSwingAmount);
                bob(leg_right_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_right_upper, 0.5f * globalSpeed, globalDegree, true, 1F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 1.2F, 0.2f, limbSwing, limbSwingAmount);
                walk(leg_right_paw, 0.5f * globalSpeed, 0.8f * globalDegree, false, 1.4F, 0.0f, limbSwing, limbSwingAmount);
                bob(leg_left_upper, 0.5F * globalSpeed, 0.8F, false, limbSwing, limbSwingAmount);
                walk(leg_left_upper, 0.5f * globalSpeed, globalDegree, true, 3.4F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_lower, 0.5f * globalSpeed, 0.6f * globalDegree, true, 3.6F, 0.2f, limbSwing, limbSwingAmount);
                walk(leg_left_paw, 0.5f * globalSpeed, 0.8f * globalDegree, false, 3.8F, 0.0f, limbSwing, limbSwingAmount);
            }
        }
        if (big_cat.aggroProgress != 0) { // Aggro/Stalking animation
            this.head_snout_teeth.scaleX = 1.05F;
            this.progressPosition(body_main, big_cat.aggroProgress, 0, 14, -4, 40);
            this.progressPosition(arm_right_upper, big_cat.aggroProgress, -2, -4, -2.5F, 40);
            this.progressPosition(arm_left_upper, big_cat.aggroProgress, 2, -4, -2.5F, 40);
            this.progressPosition(leg_right_upper, big_cat.aggroProgress, -2, -2, 6, 40);
            this.progressPosition(leg_left_upper, big_cat.aggroProgress, 2, -2, 6, 40);
            this.progressRotation(head_neck, big_cat.aggroProgress, 0.22759093446006054F, 0, 0, 40);
            this.progressRotation(head_main, big_cat.aggroProgress, -0.22759093446006054F, 0, 0, 40);
            this.progressRotation(ear_right, big_cat.aggroProgress, -0.22759093446006054F, 0.045553093477052F, -0.22759093446006054F, 40);
            this.progressRotation(ear_left, big_cat.aggroProgress, -0.22759093446006054F, -0.045553093477052F, 0.22759093446006054F, 40);
            this.progressRotation(tail_1, big_cat.aggroProgress, (float) Math.toRadians(-44.35), 0, 0, 40);
        }
        else {
            this.head_snout_teeth.scaleX = 0.9F;
        }
        
        // Sitting Animation
        if (big_cat.sitProgress > 0) {
            this.progressPosition(body_main, big_cat.sitProgress, 0.0F, 18.5F, 1.0F, 40);
            this.progressPosition(leg_left_upper, big_cat.sitProgress, 2.0F, -2.0F, 6F, 40);
            this.progressPosition(leg_left_paw, big_cat.sitProgress, 0.0F, 7.8F, 0.0F, 40);
            this.progressPosition(arm_left_paw, big_cat.sitProgress, 0.0F, 7.8F, 0.0F, 40);
            this.progressPosition(leg_right_upper, big_cat.sitProgress, -2.0F, -2.0F, 6F, 40);
            this.progressPosition(leg_right_paw, big_cat.sitProgress, 0.0F, 7.8F, 0.0F, 40);
            this.progressPosition(arm_right_paw, big_cat.sitProgress, 0.0F, 7.8F, 0.0F, 40);
            this.progressRotation(head_neck, big_cat.sitProgress, -0.5462880558742251F, 0.0F, 0.0F, 40);
            this.progressRotation(leg_left_upper, big_cat.sitProgress, -0.27314402793711257F, -0.0F, -0.045553093477052F, 40);
            this.progressRotation(leg_left_lower, big_cat.sitProgress, -1.2292353921796064F, -0.22759093446006054F, 0.045553093477052F, 40);
            this.progressRotation(leg_left_paw, big_cat.sitProgress, 1.5481070465189704F, 0.0F, 0.0F, 40);
            this.progressRotation(arm_left_upper, big_cat.sitProgress, 0.27314402793711207F, -6.200655107570901E-17F, -0.24361070658773F, 40);
            this.progressRotation(arm_left_lower, big_cat.sitProgress, -1.8668041679331349F, 0.0F, 0.10803588069844901F, 40);
            this.progressRotation(arm_left_paw, big_cat.sitProgress, 1.593485607070823F, -0.18203784098300857F, 0.0F, 40);
            this.progressRotation(leg_right_upper, big_cat.sitProgress, -0.27314402793711257F, -0.0F, 0.045553093477052F, 40);
            this.progressRotation(leg_right_lower, big_cat.sitProgress, -1.2292353921796064F, 0.22759093446006054F, -0.045553093477052F, 40);
            this.progressRotation(leg_right_paw, big_cat.sitProgress, 1.5481070465189704F, 0.0F, 0.0F, 40);
            this.progressRotation(arm_right_upper, big_cat.sitProgress, 0.27314402793711207F, 6.200655107570901E-17F, 0.24361070658773F, 40);
            this.progressRotation(arm_right_lower, big_cat.sitProgress, -1.8668041679331349F, 0.0F, -0.10803588069844901F, 40);
            this.progressRotation(arm_right_paw, big_cat.sitProgress, 1.593485607070823F, 0.18203784098300857F, 0.0F, 40);
            this.progressRotation(tail_1, big_cat.sitProgress, -1.0471975511965976F, 0.0F, 0.0F, 40);
            this.progressRotation(tail_2, big_cat.sitProgress, 0.5918411493512771F, 0.0F, 0.0F, 40);
        }

        // Sleeping Animation
        if (big_cat.sleepProgress > 0) {
            this.progressPosition(body_main, big_cat.sleepProgress, -4.0F, 19F, -4.0F, 40);
            this.progressRotation(body_main, big_cat.sleepProgress, 0, 0.0F, -1.50255395F, 40);
            this.progressRotation(body_abdomen, big_cat.sleepProgress, -0.455356402F, -0.0F, -0, 40);
            this.progressRotation(leg_right_upper, big_cat.sleepProgress, -0.500909495F , -0.0F, 0.045553093477052F, 40);
            this.progressRotation(leg_left_lower, big_cat.sleepProgress, -0.13665928F, 0, 0.77405352F, 40);
            this.progressRotation(head_neck, big_cat.sleepProgress, 0.27314402793711207F, 0.22759093446006054F, 0.0F, 40);
            this.progressRotation(head_main, big_cat.sleepProgress, 0.4553564F, 0, 0.0F, 40);
            this.progressRotation(arm_right_upper, big_cat.sleepProgress, -0.27314402793711207F, 0, 0.09110619F, 40);
            this.progressRotation(arm_left_lower, big_cat.sleepProgress, -0.5009095F, -0.09110619F, 1.0472F, 40);
        }
    }
}