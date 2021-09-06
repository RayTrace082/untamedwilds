package untamedwilds.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.entity.reptile.EntityAnaconda;

public class ModelAnaconda extends AdvancedEntityModel<EntityAnaconda> {

    public AdvancedModelBox body_5;
    public AdvancedModelBox body_6;
    public AdvancedModelBox body_4;
    public AdvancedModelBox body_7;
    public AdvancedModelBox body_8;
    public AdvancedModelBox body_9;
    public AdvancedModelBox body_10;
    public AdvancedModelBox body_11;
    public AdvancedModelBox body_12;
    public AdvancedModelBox body_13;
    public AdvancedModelBox body_3;
    public AdvancedModelBox body_2;
    public AdvancedModelBox body_1;
    public AdvancedModelBox head_main;
    public AdvancedModelBox body_head_top;
    public AdvancedModelBox body_head_top_1;
    public AdvancedModelBox body_snout_top;
    public AdvancedModelBox body_snout_top_1;
    private final ModelAnimator animator;

    private final AdvancedModelBox[] bodyParts;

    public ModelAnaconda() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.body_7 = new AdvancedModelBox(this, 0, 0);
        this.body_7.setRotationPoint(0.0F, 0.01F, 8.0F);
        this.body_7.addBox(-3.0F, -4.0F, 0.0F, 6, 5, 8, 0.0F);
        this.body_snout_top_1 = new AdvancedModelBox(this, 14, 23);
        this.body_snout_top_1.setRotationPoint(0.0F, -0.4F, -3.0F);
        this.body_snout_top_1.addBox(-2.5F, -0.5F, -4.0F, 5, 2, 4, 0.0F);
        this.body_head_top_1 = new AdvancedModelBox(this, 18, 13);
        this.body_head_top_1.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.body_head_top_1.addBox(-3.0F, -1.0F, -3.0F, 6, 2, 3, 0.0F);
        this.body_2 = new AdvancedModelBox(this, 28, 0);
        this.body_2.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.body_2.addBox(-2.5F, -3.0F, -8.0F, 5, 4, 8, 0.0F);
        this.body_9 = new AdvancedModelBox(this, 0, 0);
        this.body_9.setRotationPoint(0.0F, 0.01F, 8.0F);
        this.body_9.addBox(-3.0F, -4.0F, 0.0F, 6, 5, 8, 0.0F);
        this.body_4 = new AdvancedModelBox(this, 0, 0);
        this.body_4.setRotationPoint(0.0F, 0.01F, 0.0F);
        this.body_4.addBox(-3.0F, -4.0F, -8.0F, 6, 5, 8, 0.0F);
        this.body_8 = new AdvancedModelBox(this, 0, 0);
        this.body_8.setRotationPoint(0.0F, 0.01F, 8.0F);
        this.body_8.addBox(-3.0F, -4.0F, 0.0F, 6, 5, 8, 0.0F);
        this.body_10 = new AdvancedModelBox(this, 0, 0);
        this.body_10.setRotationPoint(0.0F, 0.01F, 8.0F);
        this.body_10.addBox(-3.0F, -4.0F, 0.0F, 6, 5, 8, 0.0F);
        this.body_5 = new AdvancedModelBox(this, 0, 0);
        this.body_5.setRotationPoint(0.0F, 23.01F, 6); // rotation Z was 16
        this.body_5.addBox(-3.0F, -4.0F, 0.0F, 6, 5, 8, 0.0F);
        this.head_main = new AdvancedModelBox(this, 0, 13);
        this.head_main.setRotationPoint(0.0F, -0.01F, -8.0F);
        this.head_main.addBox(-3.0F, -3.0F, -3.0F, 6, 4, 3, 0.0F);
        this.body_3 = new AdvancedModelBox(this, 0, 0);
        this.body_3.setRotationPoint(0.0F, 0.01F, -8.0F);
        this.body_3.addBox(-3.0F, -4.0F, -8.0F, 6, 5, 8, 0.0F);
        this.body_snout_top = new AdvancedModelBox(this, 0, 25);
        this.body_snout_top.setRotationPoint(0.0F, -0.4F, -3.0F);
        this.body_snout_top.addBox(-2.5F, -1.0F, -4.0F, 5, 2, 4, 0.0F);
        this.body_head_top = new AdvancedModelBox(this, 0, 20);
        this.body_head_top.setRotationPoint(0.0F, -1.5F, -3.0F);
        this.body_head_top.addBox(-3.0F, -1.5F, -3.0F, 6, 2, 3, 0.0F);
        this.body_6 = new AdvancedModelBox(this, 0, 0);
        this.body_6.setRotationPoint(0.0F, 0.01F, 8.0F);
        this.body_6.addBox(-3.0F, -4.0F, 0.0F, 6, 5, 8, 0.0F);
        this.body_1 = new AdvancedModelBox(this, 28, 0);
        this.body_1.setRotationPoint(0.0F, 0.01F, -8.0F);
        this.body_1.addBox(-2.5F, -3.0F, -8.0F, 5, 4, 8, 0.0F);
        this.body_11 = new AdvancedModelBox(this, 28, 0);
        this.body_11.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.body_11.addBox(-2.5F, -3.0F, 0.0F, 5, 4, 8, 0.0F);
        this.body_12 = new AdvancedModelBox(this, 38, 12);
        this.body_12.setRotationPoint(0.0F, 0.01F, 8.0F);
        this.body_12.addBox(-2.0F, -3.0F, 0.0F, 4, 4, 8, 0.0F);
        this.body_13 = new AdvancedModelBox(this, 24, 21);
        this.body_13.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.body_13.addBox(-1.5F, -2.0F, 0.0F, 3, 3, 8, 0.0F);
        this.body_6.addChild(this.body_7);
        this.body_head_top_1.addChild(this.body_snout_top_1);
        this.head_main.addChild(this.body_head_top_1);
        this.body_3.addChild(this.body_2);
        this.body_8.addChild(this.body_9);
        this.body_5.addChild(this.body_4);
        this.body_7.addChild(this.body_8);
        this.body_9.addChild(this.body_10);
        this.body_10.addChild(this.body_11);
        this.body_11.addChild(this.body_12);
        this.body_12.addChild(this.body_13);
        this.body_1.addChild(this.head_main);
        this.body_4.addChild(this.body_3);
        this.body_head_top.addChild(this.body_snout_top);
        this.head_main.addChild(this.body_head_top);
        this.body_5.addChild(this.body_6);
        this.body_2.addChild(this.body_1);

        this.bodyParts = new AdvancedModelBox[]{this.body_6, this.body_7, this.body_8, this.body_9, this.body_10, this.body_11, this.body_12, this.body_13};
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.body_5);
    }

    public void animate(IAnimatedEntity entity) {
        animator.update(entity);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
                head_main,
            body_head_top,
                    body_head_top_1, body_snout_top, body_snout_top_1,
                body_1,
                body_2,
                body_3,
                body_4,
                body_5,
                body_6,
                body_7,
                body_8,
                body_9,
                body_10,
                body_11,
                body_12,
                body_13
        );
    }

    public void setRotationAngles(EntityAnaconda anaconda, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(anaconda);
        limbSwing *= -1.2;
        float globalSpeed = 0.6f;
        float globalDegree = 2f;
        limbSwingAmount = 0.5F;

        // Pitch/Yaw handler
        if (anaconda.isInWater() && anaconda.isAirBorne) {
            this.setRotateAngle(body_5, anaconda.rotationPitch * ((float) Math.PI / 180F), 0, 0);
        }

        this.body_5.rotationPointX += (float)(Math.sin(limbSwing * -globalSpeed * 0.5) * (double)limbSwingAmount * (double)globalDegree * -4 - (double)(limbSwingAmount * globalDegree * -4));
        limbSwingAmount /= Math.max((double) anaconda.sitProgress / 6, 1);
        float partialTicks = ageInTicks - anaconda.ticksExisted;
        float renderYaw = (float)anaconda.getMovementOffsets(0, partialTicks)[0] ;
        this.body_6.rotateAngleY += MathHelper.clamp((float)anaconda.getMovementOffsets(6, partialTicks)[0] - renderYaw, -15, 15)  * 0.017453292F;
        this.body_8.rotateAngleY += MathHelper.clamp((float)anaconda.getMovementOffsets(12, partialTicks)[0] - renderYaw, -15, 15)  * 0.017453292F;
        this.body_10.rotateAngleY += MathHelper.clamp((float)anaconda.getMovementOffsets(18, partialTicks)[0] - renderYaw, -15, 15)  * 0.017453292F;
        this.body_12.rotateAngleY += MathHelper.clamp((float)anaconda.getMovementOffsets(24, partialTicks)[0] - renderYaw, -15, 15)  * 0.017453292F;
        swing(head_main, 0.5f * globalSpeed, 0.6f * globalDegree, false, -5.4f, 0, limbSwing, limbSwingAmount);
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
        swing(body_11, 0.5f * globalSpeed, 0.6f * globalDegree, false, 6.6f, 0, limbSwing, limbSwingAmount);
        swing(body_12, 0.5f * globalSpeed, 0.8f * globalDegree, false, 7f, 0, limbSwing, limbSwingAmount);
        swing(body_13, 0.5f * globalSpeed, 0.6f * globalDegree, false, 8.2f, 0, limbSwing, limbSwingAmount);
        if (anaconda.isAngry()) { // Angry/Stalking Animation
            body_1.rotateAngleX += (float) Math.toRadians(-18.26F);
            head_main.rotateAngleX += (float) Math.toRadians(18.26F);
            body_head_top.rotateAngleX += (float) Math.toRadians(-41.74F);
            body_head_top_1.rotateAngleX += (float) Math.toRadians(49.57F);
        }
        if (!anaconda.isInWater() && !anaconda.isChild() && anaconda.canMove()) {
            Vector3d position;
            float difference = 0F;
            int counter = 0;
            int parts = 0;
            for (EntityAnaconda.EntityAnacondaPart multipart : anaconda.anacondaParts) {
                if (counter > 0 && counter < 3 && multipart.getParent() == anaconda) {
                    position = multipart.getPositionVec().add(0, difference, 0);
                    BlockRayTraceResult rayTrace = anaconda.world.rayTraceBlocks(new RayTraceContext(position.add(0, 3, 0), position.add(0, -3, 0), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, null));
                    Vector3d vec3d = rayTrace.getHitVec();
                    difference = (float) (vec3d.getY() - position.getY());
                    float angle = (float) MathHelper.atan2(difference, 0);
                    float newZ = MathHelper.lerp(0.1F, anaconda.buffer[counter], this.bodyParts[counter].defaultRotationX + angle);
                    this.bodyParts[parts].rotateAngleX = newZ / 2;
                    this.bodyParts[parts + 1].rotateAngleX = newZ / 4;
                    this.bodyParts[parts + 2].rotateAngleX = -newZ / 4;
                    this.bodyParts[parts + 3].rotateAngleX = -newZ / 2;
                    anaconda.buffer[counter] = newZ;
                    parts += 4;
                }
                counter++;
            }
        }

        // Sitting Animation
        if (anaconda.sitProgress != 0) {
            this.progressPosition(body_5, anaconda.sitProgress, -4.0F, 23.0F, -3.0F, anaconda.ticksToSit);
            this.progressRotation(body_1, anaconda.sitProgress, (float) Math.toRadians(39.13F), (float) Math.toRadians(67.83F), (float) Math.toRadians(44.35F), anaconda.ticksToSit);
            this.progressRotation(body_2, anaconda.sitProgress,(float) Math.toRadians(-15.65F), (float) Math.toRadians(70.43F), 0, anaconda.ticksToSit);
            this.progressRotation(body_3, anaconda.sitProgress,0F, (float) Math.toRadians(75.65F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_4, anaconda.sitProgress,0F, (float) Math.toRadians(75.65F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_5, anaconda.sitProgress,0F, (float) Math.toRadians(67.83F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_6, anaconda.sitProgress,0F, (float) Math.toRadians(-57.39F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_7, anaconda.sitProgress,0F, (float) Math.toRadians(49.57F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_8, anaconda.sitProgress,0F, (float) Math.toRadians(57.39F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_9, anaconda.sitProgress,0F, (float) Math.toRadians(75.65F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_10, anaconda.sitProgress,0F, (float) Math.toRadians(62.61F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_11, anaconda.sitProgress,0F, (float) Math.toRadians(-49.57F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_12, anaconda.sitProgress,0F, (float) Math.toRadians(39.13F), 0F, anaconda.ticksToSit);
            this.progressRotation(body_13, anaconda.sitProgress,0F, (float) Math.toRadians(49.57F), 0F, anaconda.ticksToSit);
        }
    }
}