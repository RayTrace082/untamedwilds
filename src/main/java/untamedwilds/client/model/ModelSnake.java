package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import untamedwilds.entity.reptile.EntitySnake;

public class ModelSnake extends AdvancedEntityModel<EntitySnake> {
    
    public AdvancedModelBox main_neck;
    public AdvancedModelBox head_face;
    public AdvancedModelBox head_jaw;
    public AdvancedModelBox body_1;
    public AdvancedModelBox head_fangs;
    public AdvancedModelBox head_tongue;
    public AdvancedModelBox body_2;
    public AdvancedModelBox body_3;
    public AdvancedModelBox body_4;
    public AdvancedModelBox body_5;
    public AdvancedModelBox body_6;
    public AdvancedModelBox body_7;
    public AdvancedModelBox body_8;
    public AdvancedModelBox body_9;
    public AdvancedModelBox body_10;
    private final ModelAnimator animator;

    public ModelSnake() {
        this.textureWidth = 32;
        this.textureHeight = 32;

        this.main_neck = new AdvancedModelBox(this, 0, 0);
        this.main_neck.setRotationPoint(0.0F, 0.01F, -4.0F);
        this.main_neck.addBox(-1.5F, -1.0F, -1.0F, 3, 2, 1, 0.0F);
        this.head_face = new AdvancedModelBox(this, 0, 3);
        this.head_face.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.head_face.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 3, 0.0F);
        this.head_fangs = new AdvancedModelBox(this, 1, 8);
        this.head_fangs.setRotationPoint(0.0F, 0.0F, 0.5F);
        this.head_fangs.addBox(-1.0F, 0.0F, -3.0F, 2, 1, 1, 0.0F);
        this.head_jaw = new AdvancedModelBox(this, 0, 12);
        this.head_jaw.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.head_jaw.addBox(-1.5F, 0.0F, -3.0F, 3, 1, 3, 0.0F);
        this.head_tongue = new AdvancedModelBox(this, -1, 16);
        this.head_tongue.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head_tongue.addBox(-1.5F, 0.0F, -3.0F, 3, 0, 4, 0.0F);
        this.setRotateAngle(head_tongue, -0.01F, 0F, 0F);
        this.body_1 = new AdvancedModelBox(this, 12, 0);
        this.body_1.setRotationPoint(0.0F, -0.01F, -4.0F);
        this.body_1.addBox(-1.0F, -1.0F, -4.0F, 2, 2, 4, 0.0F);
        this.body_1.setScale(0.9F, 0.9F, 1);
        this.body_2 = new AdvancedModelBox(this, 12, 0);
        this.body_2.setRotationPoint(0.0F, 0.01F, -4.0F);
        this.body_2.addBox(-1.0F, -1.0F, -4.0F, 2, 2, 4, 0.0F);
        this.body_3 = new AdvancedModelBox(this, 12, 0);
        this.body_3.setRotationPoint(0.0F, -0.01F, -4.0F);
        this.body_3.addBox(-1.0F, -1.0F, -4.0F, 2, 2, 4, 0.0F);
        this.body_3.setScale(1.1F, 1.1F, 1);
        this.body_4 = new AdvancedModelBox(this, 12, 0);
        this.body_4.setRotationPoint(0.0F, 0.01F, 0.0F);
        this.body_4.addBox(-1.0F, -1.0F, -4.0F, 2, 2, 4, 0.0F);
        this.body_4.setScale(1.2F, 1.2F, 1);
        this.body_5 = new AdvancedModelBox(this, 12, 0);
        this.body_5.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.body_5.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.body_5.setScale(1.2F, 1.2F, 1);
        this.body_6 = new AdvancedModelBox(this, 12, 0);
        this.body_6.setRotationPoint(0.0F, 0.01F, 4.0F);
        this.body_6.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.body_6.setScale(1.1F, 1.1F, 1);
        this.body_7 = new AdvancedModelBox(this, 12, 0);
        this.body_7.setRotationPoint(0.0F, -0.01F, 4.0F);
        this.body_7.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.body_8 = new AdvancedModelBox(this, 12, 0);
        this.body_8.setRotationPoint(0.0F, 0.01F, 4.0F);
        this.body_8.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.body_8.setScale(0.9F, 0.9F, 1);
        this.body_9 = new AdvancedModelBox(this, 12, 0);
        this.body_9.setRotationPoint(0.0F, -0.01F, 4.0F);
        this.body_9.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.body_9.setScale(0.8F, 0.8F, 1);
        this.body_10 = new AdvancedModelBox(this, 12, 6);
        this.body_10.setRotationPoint(0.0F, 0.01F, 4.0F);
        this.body_10.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.body_8.addChild(this.body_9);
        this.body_1.addChild(this.main_neck);
        this.body_7.addChild(this.body_8);
        this.main_neck.addChild(this.head_jaw);
        this.body_2.addChild(this.body_1);
        this.body_5.addChild(this.body_4);
        this.body_9.addChild(this.body_10);
        this.main_neck.addChild(this.head_face);
        this.head_face.addChild(this.head_fangs);
        this.body_4.addChild(this.body_3);
        this.body_5.addChild(this.body_6);
        this.head_jaw.addChild(this.head_tongue);
        this.body_6.addChild(this.body_7);
        this.body_3.addChild(this.body_2);
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.body_5);
    }

    public void animate(IAnimatedEntity entity) {
        this.resetToDefaultPose();
        animator.update(entity);

        animator.setAnimation(EntitySnake.ANIMATION_TONGUE);
        animator.startKeyframe(4);
        this.rotate(animator, head_tongue, 26.08F, 36.52F, 0);
        animator.move(head_tongue, 0, 0, -2.5F);
        animator.endKeyframe();
        animator.startKeyframe(3);
        this.rotate(animator, head_tongue, -26.08F, -36.52F, 0);
        animator.move(head_tongue, 0, 0, -3F);
        animator.endKeyframe();
        animator.resetKeyframe(3);
    }
    
    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
            main_neck,
            head_face,
            head_jaw,
            body_1,
            head_fangs,
            head_tongue,
            body_2,
            body_3,
            body_4,
            body_5,
            body_6,
            body_7,
            body_8,
            body_9,
            body_10
        );
    }

    public void setRotationAngles(EntitySnake snake, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(snake);
        limbSwing *= -1.2;
        float globalSpeed = 1f;
        float globalDegree = 2f;
        limbSwingAmount = 0.5F;
        if (snake.sitProgress != 0) {
            limbSwingAmount /= Math.max((double) snake.sitProgress / 6, 1);
            this.progressPosition(body_5, snake.sitProgress, -4.0F, 23.0F, -3.0F, 20);
            this.progressRotation(body_1, snake.sitProgress, (float) Math.toRadians(39.13F), (float) Math.toRadians(67.83F), (float) Math.toRadians(44.35F), 20);
            this.progressRotation(body_2, snake.sitProgress,(float) Math.toRadians(-15.65F), (float) Math.toRadians(70.43F), 0, 20);
            this.progressRotation(body_3, snake.sitProgress,0F, (float) Math.toRadians(75.65F), 0F, 20);
            this.progressRotation(body_4, snake.sitProgress,0F, (float) Math.toRadians(75.65F), 0F, 20);
            this.progressRotation(body_5, snake.sitProgress,0F, (float) Math.toRadians(67.83F), 0F, 20);
            this.progressRotation(body_6, snake.sitProgress,0F, (float) Math.toRadians(-57.39F), 0F, 20);
            this.progressRotation(body_7, snake.sitProgress,0F, (float) Math.toRadians(49.57F), 0F, 20);
            this.progressRotation(body_8, snake.sitProgress,0F, (float) Math.toRadians(57.39F), 0F, 20);
            this.progressRotation(body_9, snake.sitProgress,0F, (float) Math.toRadians(75.65F), 0F, 20);
            this.progressRotation(body_10, snake.sitProgress,0F, (float) Math.toRadians(62.61F), 0F, 20);
        }
        if (snake.isInWater() && snake.isAirBorne) {
            this.setRotateAngle(body_5, (float) (snake.getMotion().getY() * -30 * Math.PI / 180F), 0, 0);
        }

        // This chunk gives the snakes a slithering motion, replacing limbSwingAmount with a constant value prevents the snake from going stiff once not moving
        //float sway = (float) (Math.cos(limbSwing * globalSpeed - 1f) * 3f * limbSwingAmount * globalDegree - limbSwingAmount * globalDegree);
        //body_5.rotationPointX += sway;
        swing(main_neck, 0.5f * globalSpeed, 0.6f * globalDegree, false, -5.4f, 0, limbSwing, limbSwingAmount);
        swing(body_1, 0.5f * globalSpeed, 0.8f * globalDegree, false, -4.4f, 0, limbSwing, limbSwingAmount);
        swing(body_2, 0.5f * globalSpeed, 0.6f * globalDegree, false, -3.6f, 0, limbSwing, limbSwingAmount);
        swing(body_3, 0.5f * globalSpeed, 1f * globalDegree, false, -2.8f, 0, limbSwing, limbSwingAmount);
        swing(body_4, 0.5f * globalSpeed, 0.8f * globalDegree, false, -2f, 0, limbSwing, limbSwingAmount);
        swing(body_5, 0.5f * globalSpeed, 1f * globalDegree, false, 0f, 0, limbSwing, limbSwingAmount);
        swing(body_6, 0.5f * globalSpeed, 0.8f * globalDegree, false, 2f, 0, limbSwing, limbSwingAmount);
        swing(body_7, 0.5f * globalSpeed, 1f * globalDegree, false, 2.8f, 0, limbSwing, limbSwingAmount);
        swing(body_8, 0.5f * globalSpeed, 0.6f * globalDegree, false, 3.6f, 0, limbSwing, limbSwingAmount);
        swing(body_9, 0.5f * globalSpeed, 0.8f * globalDegree, false, 4.4f, 0, limbSwing, limbSwingAmount);
        swing(body_10, 0.5f * globalSpeed, 0.6f * globalDegree, false, 5.4f, 0, limbSwing, limbSwingAmount);
        this.body_1.setScaleX(0.9F);
        this.body_1.setScaleY(0.9F);
        this.body_8.setScaleX(0.9F);
        this.body_8.setScaleY(0.9F);
        this.body_9.setScaleX(0.8F);
        this.body_9.setScaleY(0.8F);
        if (!snake.isRattler()) {
            this.body_10.setScaleX(0.7F);
            this.body_10.setScaleY(0.7F);
        }

        if (snake.isAngry()) {
            this.setRotateAngle(body_1, (float) Math.toRadians(-18.26F),0F,  0F);
            this.setRotateAngle(main_neck, (float) Math.toRadians(18.26F), 0F, 0F);
            this.setRotateAngle(head_face, (float) Math.toRadians(-41.74F), 0F, 0F);
            this.setRotateAngle(head_jaw, (float) Math.toRadians(49.57F),0F,  0F);
            head_face.scaleX = 1.01F;
            head_jaw.scaleX = 1.01F;
            if (snake.isRattler()) {
                body_9.rotateAngleX += Math.toRadians(31.30F);
                body_10.rotateAngleX += Math.toRadians(60F);
                swing(body_10, 1f * globalSpeed, 0.2f * globalDegree, false, 0f, 0, snake.ticksExisted, 0.5F);
                flap(body_10, 1f * globalSpeed, 0.2f * globalDegree, false, 0f, 0, snake.ticksExisted, 0.5F);
            }
        }
    }
}