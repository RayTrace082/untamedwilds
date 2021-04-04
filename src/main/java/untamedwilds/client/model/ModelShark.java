package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import untamedwilds.entity.fish.EntityShark;

public class ModelShark extends AdvancedEntityModel<EntityShark> {

    private final AdvancedModelBox body_main;
    private final AdvancedModelBox head_snout;
    private final AdvancedModelBox body_tail_1;
    private final AdvancedModelBox fin_dorsal;
    private final AdvancedModelBox fin_right;
    private final AdvancedModelBox fin_left;
    private final AdvancedModelBox head_face_1;
    private final AdvancedModelBox head_jaw;
    private final AdvancedModelBox head_face_teeth;
    private final AdvancedModelBox head_hammer;
    private final AdvancedModelBox head_jaw_teeth;
    private final AdvancedModelBox body_tail_2;
    private final AdvancedModelBox fin_pelvic_left;
    private final AdvancedModelBox fin_pelvic_right;
    private final AdvancedModelBox body_tail_3;
    private final AdvancedModelBox fin_what_top;
    private final AdvancedModelBox fin_what_bottom;
    private final AdvancedModelBox fin_caudal;
    private final AdvancedModelBox fin_caudal_2;
    private final AdvancedModelBox head_nose;

    private final ModelAnimator animator;
    private float pitchBuffer = 0;

    public ModelShark() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.head_hammer = new AdvancedModelBox(this, 0, 56);
        this.head_hammer.setRotationPoint(0.0F, -0.3F, -6.2F);
        this.head_hammer.addBox(-8.0F, -1.5F, -2.0F, 16, 3, 5, 0.0F);
        this.setRotateAngle(head_hammer, -0.091106186954104F, 0.0F, 0.0F);
        this.head_jaw = new AdvancedModelBox(this, 26, 23);
        this.head_jaw.setRotationPoint(0.0F, 2.0F, -6.5F);
        this.head_jaw.addBox(-3.0F, 0.0F, -5.0F, 6, 2, 5, 0.0F);
        this.setRotateAngle(head_jaw, -0.13665928043115597F, 0.0F, 0.0F);
        this.head_snout = new AdvancedModelBox(this, 0, 23);
        this.head_snout.setRotationPoint(0.0F, -0.9F, -7.0F);
        this.head_snout.addBox(-4.5F, -3.0F, -7.0F, 9, 7, 7, 0.0F);
        this.setRotateAngle(head_snout, 0.0911061869541028F, -5.270556841435265E-16F, 3.1003275537854504E-16F);
        this.head_jaw_teeth = new AdvancedModelBox(this, 26, 38);
        this.head_jaw_teeth.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.head_jaw_teeth.addBox(-3.0F, -1.0F, -5.0F, 6, 2, 5, 0.0F);
        this.head_jaw.scaleX = 1.1F;
        this.body_tail_1 = new AdvancedModelBox(this, 44, 4);
        this.body_tail_1.setRotationPoint(0.0F, -0.6F, 5.5F);
        this.body_tail_1.addBox(-3.0F, -3.0F, 0.0F, 6, 7, 10, 0.0F);
        this.setRotateAngle(body_tail_1, -0.136659280431156F, 0.0F, 0.0F);
        this.fin_left = new AdvancedModelBox(this, 50, 22);
        this.fin_left.mirror = true;
        this.fin_left.setRotationPoint(3.0F, 2.5F, -5.0F);
        this.fin_left.addBox(-0.5F, 0.0F, -2.0F, 1, 10, 6, 0.0F);
        this.setRotateAngle(fin_left, 0.31869712141416456F, 0.0F, -1.0927506446736497F);
        this.fin_dorsal = new AdvancedModelBox(this, 66, 22);
        this.fin_dorsal.setRotationPoint(0.0F, -3.3F, 0.0F);
        this.fin_dorsal.addBox(-0.5F, -9.0F, -2.0F, 1, 10, 6, 0.0F);
        this.setRotateAngle(fin_dorsal, -0.40980330836826856F, 0.0F, 0.0F);
        this.fin_what_bottom = new AdvancedModelBox(this, 0, 6);
        this.fin_what_bottom.setRotationPoint(0.0F, 1.5F, 5.0F);
        this.fin_what_bottom.addBox(-0.5F, 0.0F, -2.0F, 1, 3, 3, 0.0F);
        this.setRotateAngle(fin_what_bottom, 0.5009094953223726F, 0.0F, 0.0F);
        this.head_face_1 = new AdvancedModelBox(this, 0, 37);
        this.head_face_1.setRotationPoint(0.0F, 0.3F, -6.0F);
        this.head_face_1.addBox(-4.0F, -3.0F, -8.0F, 8, 5, 8, 0.0F);
        this.setRotateAngle(head_face_1, 0.045553093477051866F, 0.0F, 0.0F);
        this.head_nose = new AdvancedModelBox(this, 42, 49);
        this.head_nose.setRotationPoint(0.0F, -1.3F, -7.0F);
        this.head_nose.addBox(-2.5F, -1.5F, -12.0F, 5, 3, 12, 0.0F);
        this.setRotateAngle(head_nose, -0.18203784F, 0.0F, 0.0F);
        this.fin_caudal = new AdvancedModelBox(this, 96, 0);
        this.fin_caudal.setRotationPoint(0.0F, -1.1F, 5.4F);
        this.fin_caudal.addBox(-0.5F, -10.0F, -3.0F, 1, 11, 5, 0.0F);
        this.setRotateAngle(fin_caudal, -0.5918411493512771F, 0.0F, 0.0F);
        this.body_tail_2 = new AdvancedModelBox(this, 76, 6);
        this.body_tail_2.setRotationPoint(0.0F, 0.0F, 10.0F);
        this.body_tail_2.addBox(-2.0F, -2.5F, -0.5F, 4, 5, 10, 0.0F);
        this.setRotateAngle(body_tail_2, -0.045553093477052F, 0.0F, 0.0F);
        this.body_tail_3 = new AdvancedModelBox(this, 30, 0);
        this.body_tail_3.setRotationPoint(0.0F, 0.0F, 10.0F);
        this.body_tail_3.addBox(-1.5F, -2.0F, -0.5F, 3, 3, 8, 0.0F);
        this.setRotateAngle(body_tail_3, 0.136659280431156F, 0.0F, 0.0F);
        this.fin_what_top = new AdvancedModelBox(this, 0, 0);
        this.fin_what_top.setRotationPoint(0.0F, -1.5F, 3.0F);
        this.fin_what_top.addBox(-0.5F, -3.0F, -2.0F, 1, 3, 3, 0.0F);
        this.setRotateAngle(fin_what_top, -0.5009094953223726F, 0.0F, 0.0F);
        this.fin_caudal_2 = new AdvancedModelBox(this, 68, 0);
        this.fin_caudal_2.setRotationPoint(0.0F, 4.2F, 6.8F);
        this.fin_caudal_2.addBox(-0.5F, -4.0F, -5.0F, 1, 4, 7, 0.0F);
        this.setRotateAngle(fin_caudal_2, -0.7740535232594852F, 0.0F, 0.0F);
        this.fin_pelvic_right = new AdvancedModelBox(this, 36, 30);
        this.fin_pelvic_right.setRotationPoint(-2.3F, 3.0F, 7.7F);
        this.fin_pelvic_right.addBox(-0.5F, 0.0F, -0.8F, 1, 3, 3, 0.0F);
        this.setRotateAngle(fin_pelvic_right, 0.31869712141416456F, 0.0F, 1.0927506446736497F);
        this.body_main = new AdvancedModelBox(this, 0, 0);
        this.body_main.setRotationPoint(0.0F, 17.0F, -3.0F);
        this.body_main.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 14, 0.0F);
        this.setRotateAngle(body_main, 0.045553093477052F, 0.0F, 0.0F);
        this.fin_pelvic_left = new AdvancedModelBox(this, 36, 30);
        this.fin_pelvic_left.mirror = true;
        this.fin_pelvic_left.setRotationPoint(2.3F, 3.0F, 7.7F);
        this.fin_pelvic_left.addBox(-0.5F, 0.0F, -0.8F, 1, 3, 3, 0.0F);
        this.setRotateAngle(fin_pelvic_left, 0.31869712141416456F, 0.0F, -1.0927506446736497F);
        this.fin_right = new AdvancedModelBox(this, 50, 22);
        this.fin_right.setRotationPoint(-3.0F, 2.5F, -5.0F);
        this.fin_right.addBox(-0.5F, 0.0F, -2.0F, 1, 10, 6, 0.0F);
        this.setRotateAngle(fin_right, 0.31869712141416456F, 0.0F, 1.0927506446736497F);
        this.head_face_teeth = new AdvancedModelBox(this, 28, 46);
        this.head_face_teeth.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.head_face_teeth.addBox(-3.0F, -1.0F, -5.0F, 6, 2, 5, 0.0F);
        this.head_face_1.addChild(this.head_hammer);
        this.head_snout.addChild(this.head_jaw);
        this.body_main.addChild(this.head_snout);
        this.head_jaw.addChild(this.head_jaw_teeth);
        this.body_main.addChild(this.body_tail_1);
        this.body_main.addChild(this.fin_left);
        this.body_main.addChild(this.fin_dorsal);
        this.body_tail_2.addChild(this.fin_what_bottom);
        this.head_snout.addChild(this.head_face_1);
        this.body_tail_3.addChild(this.fin_caudal);
        this.body_tail_1.addChild(this.body_tail_2);
        this.body_tail_2.addChild(this.body_tail_3);
        this.body_tail_2.addChild(this.fin_what_top);
        this.body_tail_3.addChild(this.fin_caudal_2);
        this.body_tail_1.addChild(this.fin_pelvic_right);
        this.body_tail_1.addChild(this.fin_pelvic_left);
        this.body_main.addChild(this.fin_right);
        this.head_face_1.addChild(this.head_face_teeth);
        this.head_face_1.addChild(this.head_nose);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body_main);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
            body_main,
            head_snout,
            body_tail_1,
            fin_dorsal,
            fin_right,
            fin_left,
            head_face_1,
            head_jaw,
            head_face_teeth,
            head_hammer,
            head_jaw_teeth,
            body_tail_2,
            fin_pelvic_left,
            fin_pelvic_right,
            body_tail_3,
            fin_what_top,
            fin_what_bottom,
            fin_caudal,
            fin_caudal_2,
            head_nose
        );
    }

    private void animate(IAnimatedEntity entityIn) {
        this.resetToDefaultPose();
        EntityShark bear = (EntityShark) entityIn;
        animator.update(bear);

        animator.setAnimation(EntityShark.ATTACK_THRASH);
        animator.startKeyframe(5);
        this.rotate(animator, head_snout, -5.22F, 15.65F, -20.87F);
        this.rotate(animator, head_jaw, 57.39F, 0, 0);
        this.rotate(animator, head_face_1, -44.35F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        this.rotate(animator, head_snout, -5.22F, -5.22F, 10.43F);
        this.rotate(animator, head_jaw, 57.39F, 0, 0);
        this.rotate(animator, head_face_1, -44.35F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
    }

    public void setRotationAngles(EntityShark shark, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(shark);

        float globalSpeed = 0.6f;
        float globalDegree = 1f;
        float truePitch = limitAngle(this.pitchBuffer, headPitch, 0.05F);
        this.head_snout.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.08F), (float) (1F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);

        // Model Parameters
        boolean shortFins = shark.hasShortFins();
        this.fin_left.scaleY = shortFins ? 0.5F : 1;
        this.fin_right.scaleY = shortFins ? 0.5F : 1;
        this.fin_dorsal.scaleY = shortFins ? 0.5F : 1;

        if (shark.isInWater()) {
            this.setRotateAngle(body_main, netHeadYaw * ((float) Math.PI / 180F), truePitch * ((float) Math.PI / 180F), 0);
        }
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{head_snout, body_main, body_tail_1, body_tail_2, body_tail_3};
        chainSwing(bodyParts, globalSpeed * 0.8F, globalDegree * 0.8F, -5, limbSwing / 3, Math.max(0.3F, limbSwingAmount));
        this.pitchBuffer = truePitch;
    }

    protected float limitAngle(float sourceAngle, float targetAngle, float maximumChange) {
        float f = MathHelper.wrapDegrees(targetAngle - sourceAngle);
        if (f > maximumChange) {
            f = maximumChange;
        }
        if (f < -maximumChange) {
            f = -maximumChange;
        }
        float f1 = sourceAngle + f;
        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }
        return f1;
    }
}
