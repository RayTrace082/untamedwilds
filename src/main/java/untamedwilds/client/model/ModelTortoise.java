package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import untamedwilds.entity.reptile.EntityTortoise;

public class ModelTortoise extends AdvancedEntityModel<EntityTortoise> {

    public AdvancedModelBox body_main;
    public AdvancedModelBox neck;
    public AdvancedModelBox hand_right;
    public AdvancedModelBox leg_right;
    public AdvancedModelBox main_body;
    public AdvancedModelBox hand_left;
    public AdvancedModelBox leg_left;
    public AdvancedModelBox body_tail;
    public AdvancedModelBox head;
    public AdvancedModelBox shape8;
    private final ModelAnimator animator;

    public ModelTortoise() {
        this.texWidth = 64;
        this.texHeight = 32;

        this.leg_right = new AdvancedModelBox(this, 0, 10);
        this.leg_right.setRotationPoint(-2.4F, -0.4F, 3.9F);
        this.leg_right.addBox(-1.5F, 0.0F, 0.0F, 3, 4, 2, 0.0F);
        this.setRotateAngle(leg_right, 0.18203784098300868F, -1.0471975511965976F, -0.13665928043115697F);
        this.neck = new AdvancedModelBox(this, 0, 17);
        this.neck.setRotationPoint(0.0F, -0.40000000000000036F, -3.3999999999999995F);
        this.neck.addBox(-1.5F, -1.0F, -4.0F, 3, 3, 4, 0.0F);
        this.setRotateAngle(neck, -0.3642502148912165F, 0.0F, 0.0F);
        this.main_body = new AdvancedModelBox(this, 20, 0);
        this.main_body.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.main_body.addBox(-4.0F, -4.0F, -5.0F, 8, 4, 10, 0.0F);
        this.setRotateAngle(main_body, -0.091106186954104F, 0.0F, 0.0F);
        this.shape8 = new AdvancedModelBox(this, 0, 0);
        this.shape8.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.shape8.addBox(-3.0F, 0.0F, -4.0F, 6, 2, 8, 0.0F);
        this.head = new AdvancedModelBox(this, 0, 24);
        this.head.setRotationPoint(0.0F, 0.30000000000000004F, -3.4000000000000004F);
        this.head.addBox(-1.5F, -1.5F, -4.0F, 3, 3, 4, 0.0F);
        this.setRotateAngle(head, 0.36425021489121656F, 0.0F, 0.0F);
        this.head.scaleX = 1.01F;
        this.body_tail = new AdvancedModelBox(this, 0, 0);
        this.body_tail.setRotationPoint(0.0F, 0.0F, 4.8F);
        this.body_tail.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(body_tail, 0.31869712141416456F, 0.0F, 0.0F);
        this.leg_left = new AdvancedModelBox(this, 0, 10);
        this.leg_left.setRotationPoint(2.4F, -0.4F, 3.9F);
        this.leg_left.addBox(-1.5F, 0.0F, 0.0F, 3, 4, 2, 0.0F);
        this.setRotateAngle(leg_left, 0.18203784098300857F, 1.0471975511965976F, 0.13665928043115697F);
        this.hand_right = new AdvancedModelBox(this, 10, 10);
        this.hand_right.setRotationPoint(-3.1F, -0.1F, -4.299999999999999F);
        this.hand_right.addBox(-1.0F, 0.0F, -1.5F, 2, 4, 3, 0.0F);
        this.setRotateAngle(hand_right, -0.13665928043115597F, -1.2292353921796064F, 0.18203784098300857F);
        this.hand_left = new AdvancedModelBox(this, 10, 10);
        this.hand_left.mirror = true;
        this.hand_left.setRotationPoint(3.1F, -0.1F, -4.299999999999999F);
        this.hand_left.addBox(-1.0F, 0.0F, -1.5F, 2, 4, 3, 0.0F);
        this.setRotateAngle(hand_left, -0.13665928043115597F, 1.2292353921796064F, -0.18203784098300857F);
        this.body_main = new AdvancedModelBox(this, 26, 15);
        this.body_main.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body_main.addBox(-3.0F, -0.6F, -4.0F, 6, 2, 8, 0.0F);
        this.setRotateAngle(body_main, -0.045553093477052F, 0.0F, 0.0F);
        this.body_main.addChild(this.leg_right);
        this.body_main.addChild(this.neck);
        this.body_main.addChild(this.main_body);
        this.main_body.addChild(this.shape8);
        this.neck.addChild(this.head);
        this.body_main.addChild(this.body_tail);
        this.body_main.addChild(this.leg_left);
        this.body_main.addChild(this.hand_right);
        this.body_main.addChild(this.hand_left);
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    public void animate(IAnimatedEntity entity) {
        animator.update(entity);

        /*animator.setAnimation(EntitySnake.ANIMATION_TONGUE);
        animator.startKeyframe(4);
        this.rotate(animator, head_tongue, 26.08F, 36.52F, 0);
        animator.move(head_tongue, 0, 0, -2.5F);
        animator.endKeyframe();
        animator.startKeyframe(3);
        this.rotate(animator, head_tongue, -26.08F, -36.52F, 0);
        animator.move(head_tongue, 0, 0, -3F);
        animator.endKeyframe();
        animator.resetKeyframe(3);*/
    }
    
    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
                body_main,
                neck,
                hand_right,
                leg_right,
                main_body,
                hand_left,
                leg_left,
                body_tail,
                head,
                shape8
        );
    }

    public void setupAnim(EntityTortoise tortoise, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(tortoise);
        limbSwing *= -1.2;
        float globalSpeed = 1.4f;
        float globalDegree = 2f;
        //limbSwingAmount = 0.5F;
        //limbSwing = tortoise.tickCount;

        // Head Tracking Animation
        if (!tortoise.isSitting()) {
            this.faceTarget(netHeadYaw, headPitch, 3, neck);
            this.faceTarget(netHeadYaw, headPitch, 3, head);
        }

        // Movement Animation
        flap(hand_left, globalSpeed, globalDegree * 0.6f, false, 0.5F, -0.8f, limbSwing, limbSwingAmount);
        walk(hand_left, globalSpeed, globalDegree * 0.3F, false, 0F, 0.8f, limbSwing, limbSwingAmount);
        flap(hand_right, globalSpeed, -globalDegree * 0.6f, true, 0F, 0f, limbSwing, limbSwingAmount);
        walk(hand_right, globalSpeed, globalDegree * 0.3F, true, 0.5F, -0.8f, limbSwing, limbSwingAmount);
        swing(leg_left, globalSpeed, globalDegree * 0.6f, false, 2.8F, 0.1f, limbSwing, limbSwingAmount);
        flap(leg_left, globalSpeed, globalDegree * 0.2f, false, 1.2F, -0.8f, limbSwing, limbSwingAmount);
        swing(leg_right, globalSpeed, globalDegree * 0.6f, true, 4.4F, 0.1f, limbSwing, limbSwingAmount);
        flap(leg_right, globalSpeed, globalDegree * 0.2f, true, 2.8F, -0.8f, limbSwing, limbSwingAmount);
        flap(body_main, globalSpeed / 2, globalDegree * 0.1F, false, 0, 0.1f, limbSwing / 2, limbSwingAmount);
        swing(body_main, globalSpeed / 2, globalDegree * 0.1f, false, 0F, 0.1f, limbSwing / 2, limbSwingAmount);

        // Sitting Animation
        if (tortoise.sitProgress != 0) {
            this.progressPosition(body_main, tortoise.sitProgress, 0.0F, 22.6F, 0.0F, 20);
            this.progressPosition(neck, tortoise.sitProgress, 0.0F, -1.1F, 0.5F, 20);
            this.progressRotation(neck, tortoise.sitProgress, (float) Math.toRadians(-2.61F), 0, 0, 20);
            this.progressPosition(head, tortoise.sitProgress, 0.0F, 0.8F, -1.7F, 20);
            this.progressRotation(head, tortoise.sitProgress, 0, 0, 0, 20);
            this.progressPosition(hand_right, tortoise.sitProgress, -1.1F, -0.1F, -5.7F, 20);
            this.progressRotation(hand_right, tortoise.sitProgress, (float) Math.toRadians(80.87F), (float) Math.toRadians(-60F), (float) Math.toRadians(10.43F), 20);
            this.progressPosition(hand_left, tortoise.sitProgress, 1.1F, -0.1F, -5.7F, 20);
            this.progressRotation(hand_left, tortoise.sitProgress, (float) Math.toRadians(80.87F), (float) Math.toRadians(60F), (float) Math.toRadians(-10.43F), 20);
            this.progressPosition(leg_right, tortoise.sitProgress, -4.4F, -0.4F, 3.9F, 20);
            this.progressRotation(leg_right, tortoise.sitProgress, (float) Math.toRadians(10.43F), (float) Math.toRadians(-5.22F), (float) Math.toRadians(-80.87F), 20);
            this.progressPosition(leg_left, tortoise.sitProgress, 4.4F, -0.4F, 3.9F, 20);
            this.progressRotation(leg_left, tortoise.sitProgress, (float) Math.toRadians(10.43F), (float) Math.toRadians(5.22F), (float) Math.toRadians(80.87F), 20);
        }
    }
}