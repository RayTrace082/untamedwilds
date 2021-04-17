package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import untamedwilds.entity.mollusk.EntityGiantClam;

public class ModelGiantClam extends AdvancedEntityModel<EntityGiantClam> {

    public AdvancedModelBox mantle;
    public AdvancedModelBox shell_2;
    public AdvancedModelBox shell_1;

    public ModelGiantClam() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.shell_1 = new AdvancedModelBox(this, 0, 0);
        this.shell_1.setRotationPoint(0.0F, 1.0F, -3.0F);
        this.shell_1.addBox(-9.0F, -10.0F, -3.0F, 18, 12, 6, 0.0F);
        this.setRotateAngle(shell_1, 0.36425021489121656F, 0.0F, 0.0F);
        this.mantle = new AdvancedModelBox(this, 0, 36);
        this.mantle.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.mantle.addBox(-8.0F, -6.0F, -3.0F, 16, 8, 6, 0.0F);
        this.shell_2 = new AdvancedModelBox(this, 0, 18);
        this.shell_2.setRotationPoint(0.01F, 1.0F, 3.0F);
        this.shell_2.addBox(-9.0F, -10.0F, -3.0F, 18, 12, 6, 0.0F);
        this.setRotateAngle(shell_2, -0.36425021489121656F, 0.0F, 0.0F);
        this.mantle.addChild(this.shell_1);
        this.mantle.addChild(this.shell_2);
        updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.mantle);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.mantle, this.shell_1, this.shell_2);
    }

    public void setRotationAngles(EntityGiantClam clam, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();

        // Breathing Animation
        this.mantle.setScale((float) (1F + Math.sin(ageInTicks / 20) * 0.08F), (float) (1F + Math.sin(ageInTicks / 16) * 0.08F), 1.0F);
        walk(shell_1, 0.2f, 0.1f, true, 0.5F, 0f, ageInTicks / 20, 0.5F);
        walk(shell_2, 0.2f, 0.1f, false, 0.5F, 0f, ageInTicks / 20, 0.5F);

        // Opening/Closing Animation
        this.progressRotation(shell_1, clam.closeProgress, 0, 0, 0, 200);
        this.progressRotation(shell_2, clam.closeProgress, 0, 0, 0, 200);
    }
}
