package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import untamedwilds.entity.mammal.EntityBear;
import untamedwilds.entity.mammal.bear.AbstractBear;

public class ModelBear extends AdvancedEntityModel<EntityBear> {

    private final AdvancedModelBox body_main;
    private final AdvancedModelBox body_buttocks;
    private final AdvancedModelBox body_torso;
    private final AdvancedModelBox leg_left_1;
    private final AdvancedModelBox leg_right_1;
    private final AdvancedModelBox leg_left_2;
    private final AdvancedModelBox leg_left_foot;
    private final AdvancedModelBox leg_right_2;
    private final AdvancedModelBox leg_right_foot;
    private final AdvancedModelBox arm_left_1;
    private final AdvancedModelBox head_face;
    private final AdvancedModelBox arm_right_1;
    private final AdvancedModelBox arm_left_2;
    private final AdvancedModelBox arm_left_foot;
    private final AdvancedModelBox head_snout;
    private final AdvancedModelBox head_jaw;
    private final AdvancedModelBox head_eyes;
    private final AdvancedModelBox arm_right_2;
    private final AdvancedModelBox arm_right_foot;

    private final ModelAnimator animator;

    public ModelBear() {
        this.textureWidth = 128;
        this.textureHeight = 64;

        this.body_main = new AdvancedModelBox(this, 52, 0);
        this.body_main.setRotationPoint(0.0F, 8.5F, 0.0F);
        this.body_main.addBox(-5.5F, -5.5F, -5.0F, 11, 11, 10, 0.0F);
        this.body_torso = new AdvancedModelBox(this, 52, 21);
        this.body_torso.setRotationPoint(0.0F, -0.5F, -1.0F);
        this.body_torso.addBox(-5.0F, -5.0F, -10.0F, 10, 10, 8, 0.0F);
        this.setRotateAngle(body_torso, 0.136659280431156F, 0.0F, 0.0F);
        this.body_buttocks = new AdvancedModelBox(this, 0, 0);
        this.body_buttocks.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.body_buttocks.addBox(-6.0F, -6.0F, 0.0F, 12, 12, 14, 0.0F);
        this.setRotateAngle(body_buttocks, -0.18203784098300857F, 0.0F, 0.0F);
        AdvancedModelBox body_tail = new AdvancedModelBox(this, 38, 0);
        body_tail.setRotationPoint(0.0F, -4.0F, 13.0F);
        body_tail.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.setRotateAngle(body_tail, 0.40980330836826856F, 0.0F, 0.0F);
        this.head_face = new AdvancedModelBox(this, 0, 26);
        this.head_face.setRotationPoint(0.0F, 0.0F, -9.0F);
        this.head_face.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
        this.setRotateAngle(head_face, 0.136659280431156F, 0.0F, 0.0F);
        this.head_eyes = new AdvancedModelBox(this, 28, 32);
        this.head_eyes.setRotationPoint(0.0F, -1.5F, -6.01F);
        this.head_eyes.addBox(-4.0F, -0.5F, 0.0F, 8, 1, 0, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 0, 40);
        this.head_snout.setRotationPoint(0.0F, -1.5F, -6.3F);
        this.head_snout.addBox(-2.0F, -1.0F, -4.0F, 4, 4, 5, 0.0F);
        this.setRotateAngle(head_snout, 0.22759093446006054F, 0.0F, 0.0F);
        this.head_snout.scaleX = 1.05F;
        AdvancedModelBox head_teeth = new AdvancedModelBox(this, 0, 50);
        head_teeth.setRotationPoint(0.0F, 2.0F, 0.0F);
        head_teeth.addBox(-2.0F, 0.0F, -4.0F, 4, 1, 4, 0.0F);
        this.head_jaw = new AdvancedModelBox(this, 18, 40);
        this.head_jaw.setRotationPoint(0.0F, 1.0F, -5.5F);
        this.head_jaw.addBox(-2.0F, 0.0F, -4.0F, 4, 2, 4, 0.0F);
        AdvancedModelBox ear_right = new AdvancedModelBox(this, 24, 28);
        ear_right.setRotationPoint(-3.5F, -3.0F, -3.5F);
        ear_right.addBox(-1.5F, -2.0F, 0.0F, 3, 3, 1, 0.0F);
        this.setRotateAngle(ear_right, -0.31869712141416456F, 0.31869712141416456F, -0.7285004297824331F);
        AdvancedModelBox ear_left = new AdvancedModelBox(this, 24, 28);
        ear_left.mirror = true;
        ear_left.setRotationPoint(3.5F, -3.0F, -3.5F);
        ear_left.addBox(-1.5F, -2.0F, 0.0F, 3, 3, 1, 0.0F);
        this.setRotateAngle(ear_left, -0.31869712141416456F, -0.31869712141416456F, 0.7285004297824331F);
        this.leg_left_2 = new AdvancedModelBox(this, 106, 18);
        this.leg_left_2.mirror = true;
        this.leg_left_2.setRotationPoint(0.0F, 6.0F, -2.0F);
        this.leg_left_2.addBox(-2.0F, 0.0F, -3.0F, 5, 6, 6, 0.0F);
        this.setRotateAngle(leg_left_2, 0.22759093446006054F, -0.045553093477052F, 0.18203784098300857F);
        this.arm_left_1 = new AdvancedModelBox(this, 35, 40);
        this.arm_left_1.mirror = true;
        this.arm_left_1.setRotationPoint(2.0F, -2.0F, -5.0F);
        this.arm_left_1.addBox(0.0F, 0.0F, -3.0F, 5, 10, 6, 0.0F);
        this.setRotateAngle(arm_left_1, 0, 0.18203784098301304F, -0.1366592804311559F);
        this.arm_right_1 = new AdvancedModelBox(this, 35, 40);
        this.arm_right_1.setRotationPoint(-2.0F, -2.0F, -5.0F);
        this.arm_right_1.addBox(-5.0F, 0.0F, -3.0F, 5, 10, 6, 0.0F);
        this.setRotateAngle(arm_right_1, 0, -0.18203784098301304F, 0.1366592804311559F);
        this.arm_right_2 = new AdvancedModelBox(this, 57, 40);
        this.arm_right_2.setRotationPoint(-1.5F, 8.0F, -0.5F);
        this.arm_right_2.addBox(-3.0F, 0.0F, -3.0F, 5, 8, 6, 0.0F);
        this.setRotateAngle(arm_right_2, -0.1366592804311541F, 0.18203784098300776F, -0.1366592804311551F);
        this.arm_right_foot = new AdvancedModelBox(this, 54, 54);
        this.arm_right_foot.setRotationPoint(-0.010000000000000009F, 7.5F, -0.99F);
        this.arm_right_foot.addBox(-3.0F, 0.0F, -4.0F, 5, 2, 8, 0.0F);
        this.setRotateAngle(arm_right_foot, 0.0F, 2.4802620430283604E-16F, 0.0F);
        this.leg_right_1 = new AdvancedModelBox(this, 100, 0);
        this.leg_right_1.setRotationPoint(-5.0F, 0.0F, 10.0F);
        this.leg_right_1.addBox(-3.0F, -2.0F, -6.0F, 6, 10, 8, 0.0F);
        this.setRotateAngle(leg_right_1, -0.045553093477052F, -0.045553093477052F, 0.18203784098300857F);
        this.leg_right_2 = new AdvancedModelBox(this, 106, 18);
        this.leg_right_2.setRotationPoint(0.0F, 6.0F, -2.0F);
        this.leg_right_2.addBox(-3.0F, 0.0F, -3.0F, 5, 6, 6, 0.0F);
        this.setRotateAngle(leg_right_2, 0.22759093446006054F, 0.045553093477052F, -0.18203784098300857F);
        this.leg_left_foot = new AdvancedModelBox(this, 100, 30);
        this.leg_left_foot.mirror = true;
        this.leg_left_foot.setRotationPoint(1.0F, 6.0F, 0.0F);
        this.leg_left_foot.addBox(-3.0F, 0.0F, -5.0F, 5, 2, 8, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 57, 40);
        this.arm_left_2.mirror = true;
        this.arm_left_2.setRotationPoint(1.5F, 8.0F, -0.5F);
        this.arm_left_2.addBox(-2.0F, 0.0F, -3.0F, 5, 8, 6, 0.0F);
        this.setRotateAngle(arm_left_2, -0.136659280431156F, -0.18203784098300857F, 0.136659280431156F);
        this.leg_left_1 = new AdvancedModelBox(this, 100, 0);
        this.leg_left_1.mirror = true;
        this.leg_left_1.setRotationPoint(5.0F, 0.0F, 10.0F);
        this.leg_left_1.addBox(-3.0F, -2.0F, -6.0F, 6, 10, 8, 0.0F);
        this.setRotateAngle(leg_left_1, -0.045553093477052F, 0.045553093477052F, -0.18203784098300857F);
        this.arm_left_foot = new AdvancedModelBox(this, 54, 54);
        this.arm_left_foot.mirror = true;
        this.arm_left_foot.setRotationPoint(0.01F, 7.5F, -0.99F);
        this.arm_left_foot.addBox(-2.0F, 0.0F, -4.0F, 5, 2, 8, 0.0F);
        this.leg_right_foot = new AdvancedModelBox(this, 100, 30);
        this.leg_right_foot.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.leg_right_foot.addBox(-3.0F, 0.0F, -5.0F, 5, 2, 8, 0.0F);
        this.body_main.addChild(this.body_torso);
        this.body_main.addChild(this.body_buttocks);
        this.body_torso.addChild(this.arm_right_1);
        this.body_torso.addChild(this.arm_left_1);
        this.body_torso.addChild(this.head_face);
        this.body_buttocks.addChild(this.leg_right_1);
        this.body_buttocks.addChild(this.leg_left_1);
        this.body_buttocks.addChild(body_tail);
        this.head_face.addChild(ear_right);
        this.head_face.addChild(ear_left);
        this.head_face.addChild(this.head_snout);
        this.head_face.addChild(this.head_jaw);
        this.head_face.addChild(this.head_eyes);
        this.head_snout.addChild(head_teeth);
        this.leg_left_1.addChild(this.leg_left_2);
        this.arm_right_2.addChild(this.arm_right_foot);
        this.leg_right_1.addChild(this.leg_right_2);
        this.arm_right_1.addChild(this.arm_right_2);
        this.leg_left_2.addChild(this.leg_left_foot);
        this.arm_left_1.addChild(this.arm_left_2);
        this.arm_left_2.addChild(this.arm_left_foot);
        this.leg_right_2.addChild(this.leg_right_foot);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main, body_buttocks, body_torso, leg_left_1, leg_right_1, leg_left_2, leg_left_foot,
                leg_right_2, leg_right_foot, arm_left_1, head_face, arm_right_1, arm_left_2, arm_left_foot, head_snout,
                head_jaw, head_eyes, arm_right_2, arm_right_foot
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        EntityBear bear = (EntityBear) entityIn;
        animator.update(bear);

        animator.setAnimation(AbstractBear.ATTACK_MAUL);
        animator.startKeyframe(20);
        animator.move(body_main, 0, -6F, 0);
        this.rotate(animator, body_main, -36.52F, 0, 0);
        animator.move(body_torso, 0, -0.5F, 0);
        this.rotate(animator, body_torso, 10.43F, 0, 0);
        this.rotate(animator, head_face, 33.91F, 0, 0);
        this.rotate(animator, leg_right_1, 44.35F, -2.61F, 10.43F);
        animator.move(leg_right_1, 0, -2F, 0);
        this.rotate(animator, leg_right_2, 5.22F, 2.61F, -10.43F);
        this.rotate(animator, leg_left_1, 44.35F, 2.61F, -10.43F);
        animator.move(leg_left_1, 0, -2F, 0F);
        this.rotate(animator, leg_left_2, 5.22F, -2.61F, 10.43F);
        this.rotate(animator, arm_right_1, -33.91F, 7.83F, 36.52F);
        this.rotate(animator, arm_right_2, -20.87F, 5.22F, -33.91F);
        animator.move(arm_right_foot, 0, 2, 2);
        this.rotate(animator, arm_right_foot, 100, 0, 0);
        this.rotate(animator, arm_left_1, -20.87F, -7.83F, -36.52F);
        this.rotate(animator, arm_left_2, -18.26F, -5.22F, 33.91F);
        animator.move(arm_left_foot, 0, 2, 2);
        this.rotate(animator, arm_left_foot, 100, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(6);
        animator.move(body_torso, 0, -0.5F, 0);
        animator.move(body_main, 0, 4F, 0);
        this.rotate(animator, head_face, 23.48F, 0, 0);
        this.rotate(animator, arm_right_1, 0, -10.43F, 33.91F);
        this.rotate(animator, arm_right_2, -18.26F, 5.22F, -33.91F);
        this.rotate(animator, arm_left_1, 0, 10.43F, -33.91F);
        this.rotate(animator, arm_left_2, -18.26F, -5.22F, 33.91F);
        animator.move(leg_right_1, 0, -4F, 0);
        animator.move(leg_left_1, 0, -4F, 0);
        animator.move(arm_right_1, 0, -4F, 0);
        animator.move(arm_left_1, 0, -4F, 0);
        animator.endKeyframe();
        animator.resetKeyframe(12);

        animator.setAnimation(AbstractBear.ATTACK_BITE);
        animator.startKeyframe(8);
        this.rotate(animator, head_face, 0, 0, 30);
        this.rotate(animator, head_snout, -16, 0, 0);
        this.rotate(animator, head_jaw, 64, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(8);
        this.rotate(animator, head_face, 0, 0, -30);
        animator.endKeyframe();
        animator.resetKeyframe(2);

        animator.setAnimation(AbstractBear.ATTACK_SWIPE);
        animator.startKeyframe(10);
        this.rotate(animator, body_torso, 7.83F, -10.43F, 0);
        this.rotate(animator, head_face, 7.83F, 10.43F, 0);
        this.rotate(animator, arm_right_1, -20.87F, 26.09F, 26.09F);
        this.rotate(animator, arm_right_2, -49.57F, 28.70F, -5.22F);
        animator.move(arm_right_foot, -1, 3, 0.5F);
        this.rotate(animator, arm_right_foot, 90F, 96.52F, 0);
        animator.endKeyframe();
        animator.startKeyframe(8);
        this.rotate(animator, body_torso, 7.83F, -28.70F, 0);
        this.rotate(animator, head_face, 7.83F, 0, 0);
        this.rotate(animator, arm_right_1, -52.17F, -2.61F, 10.43F);
        this.rotate(animator, arm_right_2, -49.57F, 28.70F, -5.22F);
        animator.move(arm_right_foot, -1, 3, 0.5F);
        this.rotate(animator, arm_right_foot, 90F, 96.52F, 0);
        animator.endKeyframe();
        animator.resetKeyframe(8);

        animator.setAnimation(AbstractBear.ATTACK_POUND);
        animator.startKeyframe(12);
        this.rotate(animator, body_main, -18.26F, 0, 0);
        animator.move(body_main, 0, -2, 0);
        this.rotate(animator, body_torso, 7.83F, 5.22F, -7.83F);
        this.rotate(animator, head_snout, -2.61F, 0, 0);
        this.rotate(animator, head_jaw, 54.78F, 0, 0);
        this.rotate(animator, leg_right_1, 13.04F, -2.61F, 10.43F);
        animator.move(leg_right_1, 0, -2, 0);
        this.rotate(animator, leg_left_1, 13.04F, 2.61F, -10.43F);
        animator.move(leg_left_1, 0, -2, 0);
        this.rotate(animator, arm_right_1, -60F, 23.47F, 26.09F);
        animator.move(arm_right_1, 0, 2, 0);
        this.rotate(animator, arm_right_2, -46.96F, 10.43F, -7.83F);
        this.rotate(animator, arm_right_foot, 127.83F, 0, 0);
        animator.move(arm_right_foot, 0, 2, 0);
        this.rotate(animator, arm_left_1, -54.78F, -18.26F, -20.87F);
        animator.move(arm_left_1, 0, 2, 0);
        this.rotate(animator, arm_left_2, -46.96F, -10.43F, 7.83F);
        this.rotate(animator, arm_left_foot, 127.83F, 0, 0);
        animator.move(arm_left_foot, 0, 2, 0);
        animator.endKeyframe();
        animator.startKeyframe(8);
        animator.move(body_main, 0, 2, 0);
        this.rotate(animator, body_torso, 18.26F, 5.22F, 2.61F);
        this.rotate(animator, head_face, -28.70F, 0, 5.22F);
        this.rotate(animator, head_snout, -2.61F, 0, 0);
        this.rotate(animator, head_jaw, 54.78F, 0, 0);
        this.rotate(animator, leg_right_1, -2.61F, -2.61F, 10.43F);
        //animator.move(leg_right_1, 0, 2, 0);
        this.rotate(animator, leg_left_1, -2.61F, 2.61F, -10.43F);
        //animator.move(leg_left_1, 0, 2, 0);
        this.rotate(animator, arm_right_1, -20.87F, -5.21F, 18.26F);
        //animator.move(arm_right_1, 0, 2, 0);
        this.rotate(animator, arm_right_2, -33.91F, 10.43F, -7.83F);
        this.rotate(animator, arm_right_foot, 39.13F, 15.65F, 0);
        animator.move(arm_right_foot, 0, -2, 0);
        this.rotate(animator, arm_left_1, -20.87F, 5.21F, -18.26F);
        //animator.move(arm_left_1, 0, 2, 0);
        this.rotate(animator, arm_left_2, -33.91F, -10.43F, 7.83F);
        this.rotate(animator, arm_left_foot, 39.13F, -15.65F, 0);
        animator.move(arm_left_foot, 0, -2, 0);
        animator.endKeyframe();
        animator.resetKeyframe(8);

        animator.setAnimation(AbstractBear.ANIMATION_ROAR);
        animator.startKeyframe(15);
        this.rotate(animator, head_face, 0, 0, 30);
        this.rotate(animator, head_snout, -16, 0, 0);
        this.rotate(animator, head_jaw, 64, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(15);
        this.rotate(animator, head_face, 0, 0, -30);
        this.rotate(animator, head_jaw, 64, 0, 0);
        this.rotate(animator, head_snout, -16, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(15);
        this.rotate(animator, head_face, 0, 0, 30);
        this.rotate(animator, head_jaw, 64, 0, 0);
        this.rotate(animator, head_snout, -16, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);

        animator.setAnimation(AbstractBear.IDLE_STAND);
        animator.startKeyframe(24);
        animator.move(body_main, 0, -6F, 0);
        this.rotate(animator, body_main, -36.52F, 0, 0);
        this.rotate(animator, head_face, 33.91F, 0, 0);
        this.rotate(animator, leg_right_1, 44.35F, -2.61F, 10.43F);
        animator.move(leg_right_1, 0, -2F, 0);
        this.rotate(animator, leg_right_2, 5.22F, 2.61F, -10.43F);
        this.rotate(animator, leg_left_1, 44.35F, 2.61F, -10.43F);
        animator.move(leg_left_1, 0, -2F, 0F);
        this.rotate(animator, leg_left_2, 5.22F, -2.61F, 10.43F);
        animator.endKeyframe();
        animator.startKeyframe(24);
        animator.move(body_main, 0, -6F, 6);
        animator.move(leg_right_1, 0, -2F, -5.20F);
        animator.move(leg_left_1, 0, -2F, -5.20F);
        this.rotate(animator, body_main, -86.09F, 0, 0);
        this.rotate(animator, head_face, 67.83F, 0, 0);
        this.rotate(animator, leg_right_1, 83.48F, -10.43F, 10.43F);
        this.rotate(animator, leg_left_1, 83.48F, 10.43F, -10.43F);
        this.rotate(animator, arm_right_1, 62.61F, -10.43F, 7.83F);
        this.rotate(animator, arm_right_2, -70.43F, 10.43F, -7.83F);
        animator.move(arm_right_foot, 0, 2, 2);
        this.rotate(animator, arm_right_foot, 146.09F, 0, 0);
        this.rotate(animator, arm_left_1, 62.61F, 10.43F, -7.83F);
        this.rotate(animator, arm_left_2, -70.43F, -10.43F, 7.83F);
        animator.move(arm_left_foot, 0, 2, 2);
        this.rotate(animator, arm_left_foot, 146.09F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(80);
        animator.move(body_main, 0, -6F, 7);
        animator.move(leg_right_1, 0, -2F, -5.20F);
        animator.move(leg_left_1, 0, -2F, -5.20F);
        this.rotate(animator, body_main, -86.09F, 0, 0);
        this.rotate(animator, head_face, 67.83F, 0, 0);
        this.rotate(animator, leg_right_1, 83.48F, -10.43F, 10.43F);
        this.rotate(animator, leg_left_1, 83.48F, 10.43F, -10.43F);
        this.rotate(animator, arm_right_1, 62.61F, -10.43F, 0);
        this.rotate(animator, arm_right_2, -70.43F, 10.43F, -7.83F);
        animator.move(arm_right_foot, 0, 2, 2);
        this.rotate(animator, arm_right_foot, 146.09F, 0, 0);
        this.rotate(animator, arm_left_1, 62.61F, 10.43F, 0);
        this.rotate(animator, arm_left_2, -70.43F, -10.43F, 7.83F);
        animator.move(arm_left_foot, 0, 2, 2);
        this.rotate(animator, arm_left_foot, 146.09F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(20);

        animator.setAnimation(AbstractBear.IDLE_TALK);
        animator.startKeyframe(10);
        this.rotate(animator, head_jaw, 26.09F, 0, 0);
        this.rotate(animator, head_face, -26.09F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(10);

        animator.setAnimation(AbstractBear.ANIMATION_EAT);
        animator.startKeyframe(20);
        animator.move(body_main, 0, 3.2F, 0);
        this.rotate(animator, body_main, 13.04F, 0, 0);
        this.rotate(animator, leg_right_1, -15.65F, -2.61F, 10.43F);
        this.rotate(animator, leg_left_1, -15.65F, 2.61F, -10.43F);
        animator.move(arm_right_1, 0, 1, 0);
        this.rotate(animator, arm_right_1, 0, -10.43F, 57.39F);
        animator.move(arm_right_2, -0.5F, -1, 0);
        this.rotate(animator, arm_right_2, -28.70F, 7.83F, -57.39F);
        animator.move(arm_left_1, 0, 1, 0);
        this.rotate(animator, arm_left_1, 0, 10.43F, -57.39F);
        animator.move(arm_left_2, 0.5F, -1, 0);
        this.rotate(animator, arm_left_2, -28.70F, -7.83F, 57.39F);
        this.rotate(animator, head_face, 23.48F, 23.48F, 0);
        animator.endKeyframe();
        animator.startKeyframe(16);
        animator.move(body_main, 0, 3.2F, 0);
        this.rotate(animator, body_main, 13.04F, 0, 0);
        this.rotate(animator, leg_right_1, -15.65F, -2.61F, 10.43F);
        this.rotate(animator, leg_left_1, -15.65F, 2.61F, -10.43F);
        animator.move(arm_right_1, 0, 1, 0);
        this.rotate(animator, arm_right_1, 0, -10.43F, 57.39F);
        animator.move(arm_right_2, -0.5F, -1, 0);
        this.rotate(animator, arm_right_2, -28.70F, 7.83F, -57.39F);
        animator.move(arm_left_1, 0, 1, 0);
        this.rotate(animator, arm_left_1, 0, 10.43F, -57.39F);
        animator.move(arm_left_2, 0.5F, -1, 0);
        this.rotate(animator, arm_left_2, -28.70F, -7.83F, 57.39F);
        this.rotate(animator, head_face, 23.48F, -23.48F, 0);
        animator.endKeyframe();
        animator.startKeyframe(16);
        animator.move(body_main, 0, 3.2F, 0);
        this.rotate(animator, body_main, 13.04F, 0, 0);
        this.rotate(animator, leg_right_1, -15.65F, -2.61F, 10.43F);
        this.rotate(animator, leg_left_1, -15.65F, 2.61F, -10.43F);
        animator.move(arm_right_1, 0, 1, 0);
        this.rotate(animator, arm_right_1, 0, -10.43F, 57.39F);
        animator.move(arm_right_2, -0.5F, -1, 0);
        this.rotate(animator, arm_right_2, -28.70F, 7.83F, -57.39F);
        animator.move(arm_left_1, 0, 1, 0);
        this.rotate(animator, arm_left_1, 0, 10.43F, -57.39F);
        animator.move(arm_left_2, 0.5F, -1, 0);
        this.rotate(animator, arm_left_2, -28.70F, -7.83F, 57.39F);
        this.rotate(animator, head_face, 10.43F, 23.48F, 0);
        animator.endKeyframe();
        animator.resetKeyframe(20);
    }

    public void setRotationAngles(EntityBear bear, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(bear);
        float globalSpeed = 2.5f;
        float globalDegree = 1f;
        limbSwingAmount = Math.min(0.3F, limbSwingAmount * 2);
        limbSwing *= 0.5F;
        if (bear.getAnimation() == AbstractBear.ATTACK_MAUL || bear.getAnimation() == AbstractBear.IDLE_STAND) {
            limbSwingAmount *= 0.5F;
        }

        // Model Parameters
        float shortSnout = bear.hasShortSnout() ? 0.7F : 1;
        this.head_snout.scaleZ = shortSnout;
        this.head_jaw.scaleZ = shortSnout;
        float torsoScale = bear.hasHump() ? 1.2F : 1;
        this.body_torso.scaleY = torsoScale;

        // Breathing Animation
        this.body_main.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.body_buttocks.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        this.body_torso.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (torsoScale + Math.sin(ageInTicks / 16) * 0.06F), 1.0F);
        bob(body_main, 0.4F * globalSpeed, 0.03F, false, ageInTicks / 20, 2);
        bob(arm_right_1, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(arm_left_1, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_right_1, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);
        bob(leg_left_1, 0.4F * globalSpeed, 0.03F, false, -ageInTicks / 20, 2);

        // Blinking Animation
        if (!bear.shouldRenderEyes()) {
            this.head_eyes.setRotationPoint(0.0F, -1.5F, -5.01F);
        }
        this.head_eyes.setScaleY(Math.min(bear.getHealth()/bear.getMaxHealth() + 0.4F, 1.0F));

        // Head Tracking Animation
        if (!bear.isSleeping()) {
            this.faceTarget(netHeadYaw, headPitch, 2, head_face);
        }

        // Pitch/Yaw handler
        if (bear.isInWater() && !bear.isOnGround()) {
            limbSwing = ageInTicks / 3;
            limbSwingAmount = 0.5f;
            this.setRotateAngle(head_face, -0.22759093446006054F, 0.0F, 0.0F);
            float pitch = MathHelper.clamp(bear.rotationPitch - 10, -25F, 25.0F);
            this.setRotateAngle(body_main, (float) (pitch * Math.PI / 180F), 0, 0);
        }

        // Movement Animation
        if (bear.canMove()) {
            if (bear.getCurrentSpeed() > 0.08f || bear.isAngry()) { // Running Animation
                walk(body_main, 0.5f * globalSpeed, 0.6f * globalDegree, true, -0.5F, 0f, limbSwing, limbSwingAmount);
                flap(body_torso, 0.5f * globalSpeed, 0.2f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                flap(body_buttocks, 0.5f * globalSpeed, 0.2f * globalDegree, false, 0.5F, 0f, limbSwing, limbSwingAmount);
                flap(head_face, 0.5f * globalSpeed, 0.2f * globalDegree, false, 1F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_1, 0.5f * globalSpeed, 1.4f * globalDegree, false, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_2, 0.5f * globalSpeed, 0.8f * globalDegree, false, 1F, -0.8f, limbSwing, limbSwingAmount * 1.2f);
                walk(arm_right_foot, 0.5f * globalSpeed, 1.4f * globalDegree, false, -3.0F, 0.75f, limbSwing, limbSwingAmount);
                flap(arm_right_1, 0.5f * globalSpeed, 0.2f * globalDegree, false, 2.3F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_1, 0.5f * globalSpeed, 1.4f * globalDegree, false, 0.3F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_2, 0.5f * globalSpeed, 0.8f * globalDegree, false, 1.3F, -0.8f, limbSwing, limbSwingAmount * 1.2f);
                walk(arm_left_foot, 0.5f * globalSpeed, 1.4f * globalDegree, false, -3.3F, 0.75f, limbSwing, limbSwingAmount);
                flap(arm_left_1, 0.5f * globalSpeed, 0.2f * globalDegree, false, 2.5F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_1, 0.5f * globalSpeed, 0.8f * globalDegree, false, 4.3F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_2, 0.5f * globalSpeed, 0.8f * globalDegree, false, 2.8F, 0.5f, limbSwing, limbSwingAmount);
                walk(leg_left_foot, 0.5f * globalSpeed, 0.8f * globalDegree, false, 1.3F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_1, 0.5f * globalSpeed, 0.8f * globalDegree, false, 4F, 0f, limbSwing, limbSwingAmount);
            }
            else { // Walking Animation
                walk(body_main, 0.5f * globalSpeed, 0.1f * globalDegree, true, -0.5F, 0f, limbSwing, limbSwingAmount);
                flap(body_torso, 0.5f * globalSpeed, 0.2f * globalDegree, true, 0.5F, 0f, limbSwing, limbSwingAmount);
                flap(body_buttocks, 0.5f * globalSpeed, 0.2f * globalDegree, false, 0.5F, 0f, limbSwing, limbSwingAmount);
                flap(head_face, 0.5f * globalSpeed, 0.2f * globalDegree, false, 1F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_1, 0.5f * globalSpeed, globalDegree, false, 0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_right_2, 0.5f * globalSpeed, 0.6f * globalDegree, false, 1F, -0.8f, limbSwing, limbSwingAmount * 1.2f);
                walk(arm_right_foot, 0.5f * globalSpeed, 0.8f * globalDegree, false, -3.0F, 0.75f, limbSwing, limbSwingAmount);
                flap(arm_right_1, 0.5f * globalSpeed, 0.2f * globalDegree, false, 2.2F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_1, 0.5f * globalSpeed, globalDegree, false, 3.0F, 0f, limbSwing, limbSwingAmount);
                walk(arm_left_2, 0.5f * globalSpeed, 0.6f * globalDegree, false, 4F, -0.8f, limbSwing, limbSwingAmount * 1.2f);
                walk(arm_left_foot, 0.5f * globalSpeed, 0.8f * globalDegree, false, 1.0F, 0.75f, limbSwing, limbSwingAmount);
                flap(arm_left_1, 0.5f * globalSpeed, 0.2f * globalDegree, false, 5.2F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_1, 0.5f * globalSpeed, globalDegree, false, 1F, 0f, limbSwing, limbSwingAmount);
                walk(leg_left_2, 0.5f * globalSpeed, 0.8f * globalDegree, false, -0.5F, 0.5f, limbSwing, limbSwingAmount);
                walk(leg_left_foot, 0.5f * globalSpeed, 0.8f * globalDegree, false, -2F, 0f, limbSwing, limbSwingAmount);
                walk(leg_right_1, 0.5f * globalSpeed, globalDegree, false, 4F, 0f, limbSwing, limbSwingAmount);
            }
            walk(leg_right_2, 0.5f * globalSpeed, 0.8f * globalDegree, false, 2.5F, 0.5f, limbSwing, limbSwingAmount);
            walk(leg_right_foot, 0.5f * globalSpeed, 0.6f * globalDegree, false, 1F, 0f, limbSwing, limbSwingAmount);
        }

        // Sitting Animation
        if (bear.sitProgress > 0) {
            this.progressPosition(body_main, bear.sitProgress, 0.0F, 11.7F, 4.0F, 40);
            this.progressPosition(body_torso, bear.sitProgress, 0.0F, -2.5F, -1.0F, 40);
            this.progressPosition(body_buttocks, bear.sitProgress, 0.0F, -1.5F, 1.0F, 40);
            this.progressPosition(head_face, bear.sitProgress, 0.0F, -1.0F, -9.0F, 40);
            this.progressPosition(arm_right_1, bear.sitProgress, -2.0F, -1.0F, -5.0F, 40);
            this.progressPosition(arm_left_1, bear.sitProgress, 2.0F, -1.0F, -5.0F, 40);
            this.progressRotation(body_main, bear.sitProgress, (float)Math.toRadians(-60F), 0.0F, 0.0F, 40);
            this.progressRotation(body_torso, bear.sitProgress, (float)Math.toRadians(46.96F), 0.0F, 0.0F, 40);
            this.progressRotation(body_buttocks, bear.sitProgress, (float)Math.toRadians(-26.08F), 0.0F, 0.0F, 40);
            this.progressRotation(head_face, bear.sitProgress, (float)Math.toRadians(31.31F), 0.0F, 0.0F, 40);
            this.progressRotation(leg_right_1, bear.sitProgress, (float)Math.toRadians(-5.22F), (float)Math.toRadians(-5.22F), (float)Math.toRadians(33.9F), 40);
            this.progressRotation(leg_left_1, bear.sitProgress, (float)Math.toRadians(-5.22F), (float)Math.toRadians(5.22F), (float)Math.toRadians(-33.9F), 40);
            this.progressRotation(arm_right_1, bear.sitProgress, (float)Math.toRadians(13.04F), (float)Math.toRadians(-10.43F), (float)Math.toRadians(7.83F), 40);
            this.progressRotation(arm_left_1, bear.sitProgress, (float)Math.toRadians(13.04F), (float)Math.toRadians(10.43F), (float)Math.toRadians(-7.83F), 40);
        }

        // Sleeping Animation
        else if (bear.sleepProgress > 0) {
            this.progressPosition(body_main, bear.sleepProgress, -2.0F, 18.5F, 0.0F, 40);
            this.progressPosition(leg_right_1, bear.sleepProgress, -5.0F, 0.0F, 10.0F, 40);
            this.progressPosition(leg_left_1, bear.sleepProgress, 5.0F, 0.0F, 10.0F, 40);
            this.progressRotation(body_main, bear.sleepProgress, 0.0F, 1.5707963267948966F, 0.0F, 40);
            this.progressRotation(body_buttocks, bear.sleepProgress, 0.0F, 0.136659280431156F, -0.136659280431156F, 40);
            this.progressRotation(body_torso, bear.sleepProgress, 0.136659280431156F, -0.40980330836826856F, 0.0F, 40);
            this.progressRotation(head_face, bear.sleepProgress, (float)Math.toRadians(10.43F), (float)Math.toRadians(7.83F), (float)Math.toRadians(-80.87F), 40);
            this.progressRotation(arm_right_1, bear.sleepProgress, (float)Math.toRadians(60F), (float)Math.toRadians(78.26F), (float)Math.toRadians(7.83F), 40);
            this.progressRotation(arm_right_2, bear.sleepProgress, (float)Math.toRadians(80.87F), (float)Math.toRadians(-39.13F), (float)Math.toRadians(-78.26F), 40);
            this.progressRotation(arm_left_1, bear.sleepProgress, 0, 0, (float)Math.toRadians(-52.17F), 40);
            this.progressRotation(arm_left_2, bear.sleepProgress, (float)Math.toRadians(-93.91F), (float)Math.toRadians(10.43F), (float)Math.toRadians(101.74F), 40);
            this.progressRotation(arm_left_foot, bear.sleepProgress, (float)Math.toRadians(114.78F), 0, 0, 40);
            this.progressPosition(arm_left_foot, bear.sleepProgress, 0.01F, 7.5F, 0, 40);
            this.progressRotation(leg_right_1, bear.sleepProgress, -0.40980330836826856F, 0.045553093477052F, -1.1838568316277536F, 40);
            this.progressRotation(leg_right_2, bear.sleepProgress, 0.22759093446006054F, 0.045553093477052F, -0.18203784098300857F, 40);
            this.progressRotation(leg_left_1, bear.sleepProgress, 0.22759093446006054F, 0.045553093477052F, -1.0016444577195458F, 40);
            this.progressRotation(leg_left_2, bear.sleepProgress, 2.276432943376204F, -0.36425021489121656F, -0.4553564018453205F, 40);
        }
    }
}