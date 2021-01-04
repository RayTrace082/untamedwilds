package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.entity.reptile.EntitySoftshellTurtle;

@OnlyIn(Dist.CLIENT)
public class ModelTurtleSoftshell extends AdvancedEntityModel<EntitySoftshellTurtle> {
    public AdvancedModelBox main_body;
    public AdvancedModelBox body_shell;
    public AdvancedModelBox hand_right;
    public AdvancedModelBox leg_right;
    public AdvancedModelBox neck;
    public AdvancedModelBox hand_left;
    public AdvancedModelBox leg_left;
    public AdvancedModelBox body_tail_long;
    public AdvancedModelBox body_tail_short;
    public AdvancedModelBox main_head;
    public AdvancedModelBox head_nose;

    public ModelTurtleSoftshell() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.neck = new AdvancedModelBox(this, 18, 0);
        this.neck.setRotationPoint(0.0F, 0.0F, -0.01F);
        this.neck.addBox(-1.0F, -1.0F, -4.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(neck, 0.0F, 0.0F, 0.0F);
        this.leg_left = new AdvancedModelBox(this, 26, 14);
        this.leg_left.mirror = true;
        this.leg_left.setRotationPoint(3.0F, 0.51F, 2.0F);
        this.leg_left.addBox(-1.0F, -0.5F, -1.0F, 5, 1, 3, 0.0F);
        this.setRotateAngle(leg_left, 0.0F, -1.0927506446736497F, 0.0F);
        this.body_tail_long = new AdvancedModelBox(this, 0, 20);
        this.body_tail_long.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.body_tail_long.addBox(-0.5F, 0.0F, 0.0F, 1, 1, 6, 0.0F);
        this.body_tail_short = new AdvancedModelBox(this, 8, 20);
        this.body_tail_short.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.body_tail_short.addBox(-0.5F, 0.0F, 0.0F, 1, 1, 4, 0.0F);
        this.main_head = new AdvancedModelBox(this, 30, 0);
        this.main_head.setRotationPoint(0.0F, -0.2F, -3.41F);
        this.main_head.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 4, 0.0F);
        this.setRotateAngle(main_head, 0.0F, 0.0F, 0.0F);
        this.body_shell = new AdvancedModelBox(this, 0, 9);
        this.body_shell.setRotationPoint(0.0F, -0.4F, 0.0F);
        this.body_shell.addBox(-4.0F, -1.0F, -5.0F, 8, 1, 10, 0.0F);
        this.setRotateAngle(body_shell, -0.045553093477052F, 0.0F, 0.0F);
        this.leg_right = new AdvancedModelBox(this, 26, 14);
        this.leg_right.setRotationPoint(-3.0F, 0.51F, 2.0F);
        this.leg_right.addBox(-4.0F, -0.5F, -1.0F, 5, 1, 3, 0.0F);
        this.setRotateAngle(leg_right, 0.0F, 1.0927506446736497F, 0.0F);
        this.hand_right = new AdvancedModelBox(this, 26, 8);
        this.hand_right.setRotationPoint(-2.0F, 0.7F, -3.0F);
        this.hand_right.addBox(-4.0F, -0.5F, -2.0F, 5, 1, 3, 0.0F);
        this.setRotateAngle(hand_right, 0.0F, -0.3490658503988659F, 0.0F);
        this.head_nose = new AdvancedModelBox(this, 26, 0);
        this.head_nose.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.head_nose.addBox(-1F, -0.5F, -1.0F, 2, 1, 2, 0.0F);
        this.setRotateAngle(head_nose, 0.091106186954104F, 0.0F, 0.0F);
        this.main_body = new AdvancedModelBox(this, 0, 0);
        this.main_body.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.main_body.addBox(-3.0F, -2.0F, -4.0F, 6, 3, 6, 0.0F);
        this.hand_left = new AdvancedModelBox(this, 26, 8);
        this.hand_left.mirror = true;
        this.hand_left.setRotationPoint(2.0F, 0.7F, -3.0F);
        this.hand_left.addBox(-1.0F, -0.5F, -2.0F, 5, 1, 3, 0.0F);
        this.setRotateAngle(hand_left, 0.0F, 0.3490658503988659F, 0.0F);
        this.main_body.addChild(this.neck);
        this.main_body.addChild(this.leg_left);
        this.main_body.addChild(this.body_tail_long);
        this.main_body.addChild(this.body_tail_short);
        this.neck.addChild(this.main_head);
        this.main_body.addChild(this.body_shell);
        this.main_body.addChild(this.leg_right);
        this.main_body.addChild(this.hand_right);
        this.main_head.addChild(this.head_nose);
        this.main_body.addChild(this.hand_left);
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.main_body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(    main_body, hand_left, hand_right, leg_left, leg_right, neck, main_head
        );
    }

    public void setRotationAngles(EntitySoftshellTurtle turtle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetToDefaultPose();

        limbSwing = ageInTicks;

        float limbSwingConstant = 0.5f;
        float globalSpeed = 1.2f;
        float globalDegree = 0.6f;
        if (limbSwingAmount > 0.5) {
            limbSwingAmount = 0.5F;
        }

        // Basking animation (it just raises the head up)
        if (turtle.baskProgress != 0) {
            progressPosition(neck, turtle.baskProgress, 0.0F, 0.0F, -4.01F, 100);
            progressRotation(neck, turtle.baskProgress, -0.5009094953223726F, 0, 0.0F, 100);
            progressRotation(main_head, turtle.baskProgress, 0.36425021489121656F, 0, 0.0F, 100);
        }
        if (turtle.isInWater() && turtle.isAirBorne) {
            this.setRotateAngle(main_body, (float) (turtle.getMotion().getY() * -30 * Math.PI / 180F), 0, 0);
        }

        this.main_body.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F), (float) (1F + Math.sin(ageInTicks / 16) * 0.06F));
        walk(neck, 0.4F, 0.03F, false, 2.8f, 0.1F, ageInTicks / 16, 0.1F);
        bob(main_head, 0.4F, 0.03F, false, ageInTicks / 16, 2);

        swing(hand_left, globalSpeed, globalDegree * 1.4f, false, 0, 0.8f, limbSwing / 2, limbSwingAmount);
        swing(leg_left, globalSpeed, globalDegree * 1.2f, false, 0.8F, 0.1f, limbSwing / 2, limbSwingAmount);
        swing(hand_right, globalSpeed, globalDegree * 1.4f, false, 1.6F, 0.8f, limbSwing / 2, limbSwingAmount);
        swing(leg_right, globalSpeed, globalDegree * 1.2f, false, 2.4F, 0.1f, limbSwing / 2, limbSwingAmount);
        if (turtle.isInWater()) {
            flap(hand_left, globalSpeed, globalDegree * 1.4f, false, 0, 0.1f, limbSwing / 2, limbSwingAmount);
            flap(leg_left, globalSpeed, globalDegree * 1.2f, false, 0.8F, 0.1f, limbSwing / 2, limbSwingAmount);
            flap(hand_right, globalSpeed, globalDegree * 1.4f, false, 1.6F, 0.1f, limbSwing / 2, limbSwingAmount);
            flap(leg_right, globalSpeed, globalDegree * 1.2f, false, 2.4F, 0.1f, limbSwing / 2, limbSwingAmount);

            flap(main_body, globalSpeed / 2, globalDegree * 1.2f, false, 0, 0.1f, limbSwing / 2, limbSwingAmount);
            swing(main_body, globalSpeed / 2, globalDegree * 1.2f, false, 0.8F, 0.1f, limbSwing / 3, limbSwingAmount);
        }
    }
}
