package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import untamedwilds.entity.arthropod.EntityKingCrab;
import untamedwilds.entity.mammal.EntityCamel;

/**
 * MobKingCrab - RayTrace082
 * Created using Tabula 7.0.0
 */
public class ModelKingCrab extends AdvancedEntityModel<EntityKingCrab> {
    
    public AdvancedModelBox body_main;
    public AdvancedModelBox body_spikes;
    public AdvancedModelBox arm_left_1;
    public AdvancedModelBox arm_r_1;
    public AdvancedModelBox leg_l_11;
    public AdvancedModelBox leg_l_21;
    public AdvancedModelBox leg_l_31;
    public AdvancedModelBox leg_r_11;
    public AdvancedModelBox leg_r_21;
    public AdvancedModelBox leg_r_31;
    public AdvancedModelBox head;
    public AdvancedModelBox shape69;
    public AdvancedModelBox arm_left_1_spikes;
    public AdvancedModelBox arm_left_2;
    public AdvancedModelBox arm_left_2_spikes;
    public AdvancedModelBox arm_left_pincer_1;
    public AdvancedModelBox arm_left_pincer_2;
    public AdvancedModelBox arm_r_1_spikes;
    public AdvancedModelBox arm_r_2;
    public AdvancedModelBox arm_r_2_spikes;
    public AdvancedModelBox arm_r_pincer_1;
    public AdvancedModelBox arm_r_pincer_2;
    public AdvancedModelBox leg_l_11_spikes;
    public AdvancedModelBox leg_l_12;
    public AdvancedModelBox leg_l_12_spikes;
    public AdvancedModelBox leg_l_21_spikes;
    public AdvancedModelBox leg_l_22;
    public AdvancedModelBox leg_l_22_spikes;
    public AdvancedModelBox leg_l_31_spikes;
    public AdvancedModelBox leg_l_32;
    public AdvancedModelBox leg_l_32_spikes;
    public AdvancedModelBox leg_r_11_spikes;
    public AdvancedModelBox leg_r_12;
    public AdvancedModelBox leg_r_12_spikes;
    public AdvancedModelBox leg_r_21_spikes;
    public AdvancedModelBox leg_r_22;
    public AdvancedModelBox leg_r_22_spikes;
    public AdvancedModelBox leg_r_31_spikes;
    public AdvancedModelBox leg_r_32;
    public AdvancedModelBox leg_r_32_spikes;

    private final ModelAnimator animator;

    public ModelKingCrab() {
        this.texWidth = 64;
        this.texHeight = 32;

        this.head = new AdvancedModelBox(this, 0, 27);
        this.head.setRotationPoint(0.0F, -0.49F, -3.5F);
        this.head.addBox(-2.0F, -1.5F, -2.0F, 4, 3, 2, 0.0F);
        this.leg_l_12_spikes = new AdvancedModelBox(this, 20, 14);
        this.leg_l_12_spikes.setRotationPoint(1.0F, 3.3F, 0.0F);
        this.leg_l_12_spikes.addBox(0.0F, -3.0F, 0.0F, 2, 9, 0, 0.0F);
        this.leg_l_32 = new AdvancedModelBox(this, 12, 14);
        this.leg_l_32.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.leg_l_32.addBox(-1.0F, 0.0F, -1.0F, 2, 9, 2, 0.0F);
        this.setRotateAngle(leg_l_32, 0.36425021489121656F, 0.091106186954104F, 1.3658946726107624F);
        this.arm_r_1_spikes = new AdvancedModelBox(this, 48, 16);
        this.arm_r_1_spikes.setRotationPoint(0.0F, -0.7F, 0.0F);
        this.arm_r_1_spikes.addBox(-6.0F, -2.0F, 0.0F, 6, 2, 0, 0.0F);
        this.arm_left_2 = new AdvancedModelBox(this, 32, 18);
        this.arm_left_2.setRotationPoint(6.0F, 0.0F, 0.0F);
        this.arm_left_2.addBox(0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(arm_left_2, -0.045553093477052F, 0.0F, 2.367539130330308F);
        this.leg_l_21_spikes = new AdvancedModelBox(this, 8, 14);
        this.leg_l_21_spikes.setRotationPoint(1.0F, 1.3F, 0.0F);
        this.leg_l_21_spikes.addBox(0.0F, -2.0F, 0.0F, 2, 7, 0, 0.0F);
        this.leg_l_11 = new AdvancedModelBox(this, 0, 14);
        this.leg_l_11.setRotationPoint(4.0F, 1.0F, -1.0F);
        this.leg_l_11.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(leg_l_11, -0.40980330836826856F, -0.091106186954104F, -1.7756979809790308F);
        this.leg_l_21 = new AdvancedModelBox(this, 0, 14);
        this.leg_l_21.setRotationPoint(4.0F, 1.0F, 1.0F);
        this.leg_l_21.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(leg_l_21, 0.136659280431156F, 0.22759093446006054F, -1.9577358219620393F);
        this.arm_r_2 = new AdvancedModelBox(this, 32, 18);
        this.arm_r_2.mirror = true;
        this.arm_r_2.setRotationPoint(-6.0F, 0.2F, 0.0F);
        this.arm_r_2.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(arm_r_2, -0.045553093477052F, 0.0F, -2.4586453172844123F);
        this.leg_l_12 = new AdvancedModelBox(this, 12, 14);
        this.leg_l_12.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.leg_l_12.addBox(-1.0F, 0.0F, -1.0F, 2, 9, 2, 0.0F);
        this.setRotateAngle(leg_l_12, 0.0F, 0.091106186954104F, 1.3203415791337103F);
        this.arm_left_2_spikes = new AdvancedModelBox(this, 48, 18);
        this.arm_left_2_spikes.setRotationPoint(0.0F, -0.7F, 0.0F);
        this.arm_left_2_spikes.addBox(0.0F, -2.0F, 0.0F, 4, 2, 0, 0.0F);
        this.leg_l_32_spikes = new AdvancedModelBox(this, 20, 14);
        this.leg_l_32_spikes.setRotationPoint(1.0F, 3.3F, 0.0F);
        this.leg_l_32_spikes.addBox(0.0F, -3.0F, 0.0F, 2, 9, 0, 0.0F);
        this.arm_left_1 = new AdvancedModelBox(this, 32, 14);
        this.arm_left_1.setRotationPoint(2.5F, 1.0F, -3.0F);
        this.arm_left_1.addBox(0.0F, -1.0F, -1.0F, 6, 2, 2, 0.0F);
        this.setRotateAngle(arm_left_1, 0.8651597102135892F, 1.0927506446736497F, 0.8196066167365371F);
        this.arm_left_1_spikes = new AdvancedModelBox(this, 48, 16);
        this.arm_left_1_spikes.setRotationPoint(0.0F, -0.7F, 0.0F);
        this.arm_left_1_spikes.addBox(0.0F, -2.0F, 0.0F, 6, 2, 0, 0.0F);
        this.arm_left_pincer_2 = new AdvancedModelBox(this, 44, 22);
        this.arm_left_pincer_2.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.arm_left_pincer_2.addBox(0.0F, 0.0F, -1.0F, 4, 1, 2, 0.0F);
        this.setRotateAngle(arm_left_pincer_2, 1.5707963267948966F, 0.0F, 0.0F);
        this.leg_l_22_spikes = new AdvancedModelBox(this, 20, 14);
        this.leg_l_22_spikes.setRotationPoint(1.0F, 3.3F, 0.0F);
        this.leg_l_22_spikes.addBox(0.0F, -3.0F, 0.0F, 2, 9, 0, 0.0F);
        this.leg_l_31_spikes = new AdvancedModelBox(this, 8, 14);
        this.leg_l_31_spikes.setRotationPoint(1.0F, 1.3F, 0.0F);
        this.leg_l_31_spikes.addBox(0.0F, -2.0F, 0.0F, 2, 7, 0, 0.0F);
        this.arm_left_pincer_1 = new AdvancedModelBox(this, 32, 22);
        this.arm_left_pincer_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.arm_left_pincer_1.addBox(0.0F, -1.0F, -1.0F, 4, 1, 2, 0.0F);
        this.setRotateAngle(arm_left_pincer_1, 1.5707963267948966F, 0.22759093446006054F, 0.0F);
        this.arm_r_pincer_2 = new AdvancedModelBox(this, 44, 22);
        this.arm_r_pincer_2.mirror = true;
        this.arm_r_pincer_2.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.arm_r_pincer_2.addBox(-4.0F, 0.0F, -1.0F, 4, 1, 2, 0.0F);
        this.setRotateAngle(arm_r_pincer_2, 1.5707963267948966F, 0.0F, 0.0F);
        this.arm_r_1 = new AdvancedModelBox(this, 32, 14);
        this.arm_r_1.mirror = true;
        this.arm_r_1.setRotationPoint(-2.5F, 1.0F, -3.0F);
        this.arm_r_1.addBox(-6.0F, -1.0F, -1.0F, 6, 2, 2, 0.0F);
        this.setRotateAngle(arm_r_1, 0.40980330836826856F, -0.9560913642424937F, -0.36425021489121656F);
        this.arm_r_pincer_1 = new AdvancedModelBox(this, 32, 22);
        this.arm_r_pincer_1.mirror = true;
        this.arm_r_pincer_1.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.arm_r_pincer_1.addBox(-4.0F, -1.0F, -1.0F, 4, 1, 2, 0.0F);
        this.setRotateAngle(arm_r_pincer_1, 1.5707963267948966F, -0.22759093446006054F, 0.0F);
        this.arm_r_2_spikes = new AdvancedModelBox(this, 48, 18);
        this.arm_r_2_spikes.setRotationPoint(0.0F, -0.7F, 0.0F);
        this.arm_r_2_spikes.addBox(-4.0F, -2.0F, 0.0F, 4, 2, 0, 0.0F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.body_main.addBox(-4.0F, -2.0F, -3.5F, 8, 4, 7, 0.0F);
        this.setRotateAngle(body_main, -0.136659280431156F, 0.0F, 0.0F);
        this.leg_l_11_spikes = new AdvancedModelBox(this, 8, 14);
        this.leg_l_11_spikes.setRotationPoint(1.0F, 1.3F, 0.0F);
        this.leg_l_11_spikes.addBox(0.0F, -2.0F, 0.0F, 2, 7, 0, 0.0F);
        this.shape69 = new AdvancedModelBox(this, 14, 24);
        this.shape69.setRotationPoint(0.0F, 1.5F, 3.0F);
        this.shape69.addBox(-3.0F, -1.0F, -4.0F, 6, 3, 5, 0.0F);
        this.setRotateAngle(shape69, 0.31869712141416456F, 0.0F, 0.0F);
        this.leg_l_22 = new AdvancedModelBox(this, 12, 14);
        this.leg_l_22.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.leg_l_22.addBox(-1.0F, 0.0F, -1.0F, 2, 9, 2, 0.0F);
        this.setRotateAngle(leg_l_22, 0.045553093477052F, 0.18203784098300857F, 1.593485607070823F);
        this.body_spikes = new AdvancedModelBox(this, 18, 0);
        this.body_spikes.setRotationPoint(0.0F, -1.99F, -0.5F);
        this.body_spikes.addBox(-6.0F, 0.0F, -6.0F, 12, 0, 12, 0.0F);
        this.leg_l_31 = new AdvancedModelBox(this, 0, 14);
        this.leg_l_31.setRotationPoint(2.5F, 1.0F, 3.0F);
        this.leg_l_31.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(leg_l_31, 0.5462880558742251F, 0.38379790251355306F, -1.7756979809790308F);

        this.leg_r_31 = new AdvancedModelBox(this, 0, 14);
        this.leg_r_31.mirror = true;
        this.leg_r_31.setRotationPoint(-2.5F, 1.0F, 3.0F);
        this.leg_r_31.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(leg_r_31, 0.5462880558742251F, -0.40980330836826856F, 1.7756979809790308F);
        this.leg_r_32 = new AdvancedModelBox(this, 12, 14);
        this.leg_r_32.mirror = true;
        this.leg_r_32.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.leg_r_32.addBox(-1.0F, 0.0F, -1.0F, 2, 9, 2, 0.0F);
        this.setRotateAngle(leg_r_32, 0.0F, 0.091106186954104F, -1.3658946726107624F);
        this.leg_r_11 = new AdvancedModelBox(this, 0, 14);
        this.leg_r_11.mirror = true;
        this.leg_r_11.setRotationPoint(-4.0F, 1.0F, -1.0F);
        this.leg_r_11.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(leg_r_11, -0.40980330836826856F, 0.091106186954104F, 1.7756979809790308F);
        this.leg_r_22 = new AdvancedModelBox(this, 12, 14);
        this.leg_r_22.mirror = true;
        this.leg_r_22.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.leg_r_22.addBox(-1.0F, 0.0F, -1.0F, 2, 9, 2, 0.0F);
        this.setRotateAngle(leg_r_22, 0.045553093477052F, -0.18203784098300857F, -1.593485607070823F);
        this.leg_r_11_spikes = new AdvancedModelBox(this, 8, 14);
        this.leg_r_11_spikes.mirror = true;
        this.leg_r_11_spikes.setRotationPoint(-1.0F, 1.3F, 0.0F);
        this.leg_r_11_spikes.addBox(-2.0F, -2.0F, 0.0F, 2, 7, 0, 0.0F);
        this.leg_r_22_spikes = new AdvancedModelBox(this, 20, 14);
        this.leg_r_22_spikes.mirror = true;
        this.leg_r_22_spikes.setRotationPoint(-1.0F, 3.3F, 0.0F);
        this.leg_r_22_spikes.addBox(-2.0F, -3.0F, 0.0F, 2, 9, 0, 0.0F);
        this.leg_r_12 = new AdvancedModelBox(this, 12, 14);
        this.leg_r_12.mirror = true;
        this.leg_r_12.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.leg_r_12.addBox(-1.0F, 0.0F, -1.0F, 2, 9, 2, 0.0F);
        this.setRotateAngle(leg_r_12, 0.0F, 0.091106186954104F, -1.3203415791337103F);
        this.leg_r_12_spikes = new AdvancedModelBox(this, 20, 14);
        this.leg_r_12_spikes.mirror = true;
        this.leg_r_12_spikes.setRotationPoint(-1.0F, 3.3F, 0.0F);
        this.leg_r_12_spikes.addBox(-2.0F, -3.0F, 0.0F, 2, 9, 0, 0.0F);
        this.leg_r_32_spikes = new AdvancedModelBox(this, 20, 14);
        this.leg_r_32_spikes.mirror = true;
        this.leg_r_32_spikes.setRotationPoint(-1.0F, 3.3F, 0.0F);
        this.leg_r_32_spikes.addBox(-2.0F, -3.0F, 0.0F, 2, 9, 0, 0.0F);
        this.leg_r_21 = new AdvancedModelBox(this, 0, 14);
        this.leg_r_21.mirror = true;
        this.leg_r_21.setRotationPoint(-4.0F, 1.0F, 1.0F);
        this.leg_r_21.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(leg_r_21, 0.045553093477052F, -0.045553093477052F, 1.9577358219620393F);
        this.leg_r_21_spikes = new AdvancedModelBox(this, 8, 14);
        this.leg_r_21_spikes.mirror = true;
        this.leg_r_21_spikes.setRotationPoint(-1.0F, 1.3F, 0.0F);
        this.leg_r_21_spikes.addBox(-2.0F, -2.0F, 0.0F, 2, 7, 0, 0.0F);
        this.leg_r_31_spikes = new AdvancedModelBox(this, 8, 14);
        this.leg_r_31_spikes.mirror = true;
        this.leg_r_31_spikes.setRotationPoint(-1.0F, 1.3F, 0.0F);
        this.leg_r_31_spikes.addBox(-2.0F, -2.0F, 0.0F, 2, 7, 0, 0.0F);

        this.leg_r_32.addChild(this.leg_r_32_spikes);
        this.body_main.addChild(this.leg_r_21);
        this.body_main.addChild(this.head);
        this.leg_r_11.addChild(this.leg_r_12);
        this.leg_r_11.addChild(this.leg_r_11_spikes);
        this.leg_l_12.addChild(this.leg_l_12_spikes);
        this.leg_l_31.addChild(this.leg_l_32);
        this.leg_r_21.addChild(this.leg_r_22);
        this.arm_r_1.addChild(this.arm_r_1_spikes);
        this.arm_left_1.addChild(this.arm_left_2);
        this.body_main.addChild(this.leg_r_31);
        this.leg_l_21.addChild(this.leg_l_21_spikes);
        this.body_main.addChild(this.leg_l_11);
        this.body_main.addChild(this.leg_l_21);
        this.leg_r_21.addChild(this.leg_r_21_spikes);
        this.arm_r_1.addChild(this.arm_r_2);
        this.leg_l_11.addChild(this.leg_l_12);
        this.arm_left_2.addChild(this.arm_left_2_spikes);
        this.leg_l_32.addChild(this.leg_l_32_spikes);
        this.body_main.addChild(this.arm_left_1);
        this.arm_left_1.addChild(this.arm_left_1_spikes);
        this.arm_left_2.addChild(this.arm_left_pincer_2);
        this.leg_l_22.addChild(this.leg_l_22_spikes);
        this.leg_l_31.addChild(this.leg_l_31_spikes);
        this.arm_left_2.addChild(this.arm_left_pincer_1);
        this.arm_r_2.addChild(this.arm_r_pincer_2);
        this.body_main.addChild(this.arm_r_1);
        this.leg_r_22.addChild(this.leg_r_22_spikes);
        this.arm_r_2.addChild(this.arm_r_pincer_1);
        this.leg_r_31.addChild(this.leg_r_32);
        this.leg_r_31.addChild(this.leg_r_31_spikes);
        this.leg_r_12.addChild(this.leg_r_12_spikes);
        this.arm_r_2.addChild(this.arm_r_2_spikes);
        this.leg_l_11.addChild(this.leg_l_11_spikes);
        this.body_main.addChild(this.shape69);
        this.body_main.addChild(this.leg_r_11);
        this.leg_l_21.addChild(this.leg_l_22);
        this.body_main.addChild(this.body_spikes);
        this.body_main.addChild(this.leg_l_31);
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body_main, body_spikes, arm_left_1, arm_r_1, leg_l_11, leg_l_21, leg_l_31, leg_r_11,
        leg_r_21, leg_r_31, head, shape69, arm_left_1_spikes, arm_left_2, arm_left_2_spikes, arm_left_pincer_1,
        arm_left_pincer_2, arm_r_1_spikes, arm_r_2, arm_r_2_spikes, arm_r_pincer_1, arm_r_pincer_2, leg_l_11_spikes,
        leg_l_12, leg_l_12_spikes, leg_l_21_spikes, leg_l_22, leg_l_22_spikes, leg_l_31_spikes, leg_l_32, leg_l_32_spikes,
        leg_r_11_spikes, leg_r_12, leg_r_12_spikes, leg_r_21_spikes, leg_r_22, leg_r_22_spikes, leg_r_31_spikes,
        leg_r_32, leg_r_32_spikes
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        animator.update(entityIn);

        animator.setAnimation(EntityKingCrab.EAT_RIGHT);
        animator.startKeyframe(16);
        this.rotate(animator, arm_r_1, 26.09F, 13.04F, -49.56F);
        this.rotate(animator, arm_r_2,  0, 5.22F,  54.78F);
        animator.endKeyframe();
        animator.startKeyframe(8);
        this.rotate(animator, arm_r_1, 26.09F, 13.04F, -49.56F);
        this.rotate(animator, arm_r_2,  0, 5.22F,  54.78F);
        this.rotate(animator, arm_r_pincer_1, 0, 13.04F, 0);
        animator.endKeyframe();
        animator.startKeyframe(16);
        this.rotate(animator, arm_r_1, -88.70F, 2.61F, 39.13F);
        this.rotate(animator, arm_r_2,  0, -13.04F, -30F);
        this.rotate(animator, arm_r_pincer_1, 0, 13.04F, 0);
        animator.endKeyframe();
        animator.resetKeyframe(16);

        animator.setAnimation(EntityKingCrab.EAT_LEFT);
        animator.startKeyframe(16);
        this.rotate(animator, arm_left_1, 26.09F, -13.04F, 49.56F);
        this.rotate(animator, arm_left_2, 0, -5.22F, -54.78F);
        animator.endKeyframe();
        animator.startKeyframe(8);
        this.rotate(animator, arm_left_1, 26.09F, -13.04F, 49.56F);
        this.rotate(animator, arm_left_2,  0, -5.22F,  -54.78F);
        this.rotate(animator, arm_left_pincer_1, 0, -13.04F, 0);
        animator.endKeyframe();
        animator.startKeyframe(16);
        this.rotate(animator, arm_left_1, -88.70F, -2.61F, -39.13F);
        this.rotate(animator, arm_left_2,  0, 13.04F, 30F);
        this.rotate(animator, arm_left_pincer_1, 0, -13.04F, 0);
        animator.endKeyframe();
        animator.resetKeyframe(16);

        animator.setAnimation(EntityKingCrab.EAT_BOTH);
        animator.startKeyframe(16);
        this.rotate(animator, arm_left_1, 26.09F, -13.04F,  49.56F);
        this.rotate(animator, arm_left_2,  0, -5.22F,  -54.78F);
        animator.endKeyframe();
        animator.startKeyframe(8);
        this.rotate(animator, arm_left_1, 26.09F, -13.04F, 49.56F);
        this.rotate(animator, arm_left_2,  0, -5.22F,  -54.78F);
        this.rotate(animator, arm_left_pincer_1, 0, -13.04F, 0);
        animator.endKeyframe();
        animator.startKeyframe(16);
        this.rotate(animator, arm_left_1, -88.70F, -2.61F, -39.13F);
        this.rotate(animator, arm_left_2,  0, 13.04F, 30F);
        this.rotate(animator, arm_left_pincer_1, 0, -13.04F, 0);
        this.rotate(animator, arm_r_1, 26.09F, 13.04F, -49.56F);
        this.rotate(animator, arm_r_2,  0, 5.22F,  54.78F);
        animator.endKeyframe();
        animator.startKeyframe(8);
        this.rotate(animator, arm_r_1, 26.09F, 13.04F, -49.56F);
        this.rotate(animator, arm_r_2,  0, 5.22F,  54.78F);
        this.rotate(animator, arm_r_pincer_1, 0, 13.04F, 0);
        animator.endKeyframe();
        animator.startKeyframe(16);
        this.rotate(animator, arm_r_1, -88.70F, 2.61F, 39.13F);
        this.rotate(animator, arm_r_2,  0, -13.04F, -30F);
        this.rotate(animator, arm_r_pincer_1, 0, 13.04F, 0);
        animator.endKeyframe();
        animator.resetKeyframe(16);
    }

    public void setupAnim(EntityKingCrab tarantula, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();
        animate(tarantula);

        limbSwing = ageInTicks;
        limbSwingAmount = Math.min(0.4F, limbSwingAmount);
        float globalSpeed = 1.2f;
        //float globalSpeed = limbSwing * 0.0001F * 1.2f;
        //limbSwingAmount = 0.4F;
        float globalDegree = 1.4f;

        bob(body_main, 0.4F * 1.5f, 0.05F, false, ageInTicks / 20, 5);
        bob(leg_l_11, 0.4F * 1.5f, 0.05F, false, -ageInTicks / 20, 5);
        bob(leg_r_11, 0.4F * 1.5f, 0.05F, false, -ageInTicks / 20, 5);
        bob(leg_l_21, 0.4F * 1.5f, 0.05F, false, -ageInTicks / 20, 5);
        bob(leg_r_21, 0.4F * 1.5f, 0.05F, false, -ageInTicks / 20, 5);
        bob(leg_l_31, 0.4F * 1.5f, 0.05F, false, -ageInTicks / 20, 5);
        bob(leg_r_31, 0.4F * 1.5f, 0.05F, false, -ageInTicks / 20, 5);

        animateArthropodLeg(leg_l_11, leg_l_12, globalSpeed * 0.2F, globalDegree, 1, -limbSwing, limbSwingAmount);
        animateArthropodLeg(leg_l_21, leg_l_22, globalSpeed * 0.2F, globalDegree, 3, -limbSwing, limbSwingAmount);
        animateArthropodLeg(leg_l_31, leg_l_32, globalSpeed * 0.2F, globalDegree, 5, -limbSwing, limbSwingAmount);

        animateArthropodLeg(leg_r_11, leg_r_12, globalSpeed * 0.2F, globalDegree, 0, -limbSwing, limbSwingAmount);
        animateArthropodLeg(leg_r_21, leg_r_22, globalSpeed * 0.2F, globalDegree, 2, -limbSwing, limbSwingAmount);
        animateArthropodLeg(leg_r_31, leg_r_32, globalSpeed * 0.2F, globalDegree, 4, -limbSwing, limbSwingAmount);
    }

    private void animateArthropodLeg(AdvancedModelBox limb_1, AdvancedModelBox limb_2, float speed, float degree, int offset, float limbSwing, float limbSwingAmount) {
        swing(limb_1, speed, degree * 1.2f, false, offset, 0.1f, limbSwing, limbSwingAmount);
        flap(limb_1, speed, degree * 0.8f, true, offset + 1.5F, 0.2f, limbSwing, limbSwingAmount);
        flap(limb_2, speed, degree * 1f, true, offset + 1.5F, 0f, limbSwing, limbSwingAmount);
    }
}
