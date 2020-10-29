package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelSunfish;
import untamedwilds.entity.fish.Sunfish;

import javax.annotation.Nonnull;

public class RendererSunfish extends MobRenderer<Sunfish, EntityModel<Sunfish>> {

    private final ResourceLocation SUNFISH = new ResourceLocation("untamedwilds:textures/entity/sunfish/sunfish.png");
    private final ResourceLocation SOUTHERN = new ResourceLocation("untamedwilds:textures/entity/sunfish/southern.png");

    public RendererSunfish(EntityRendererManager rendermanager, ModelSunfish model, float shadowsize) {
        super(rendermanager, model, shadowsize);
    }

    protected void preRenderCallback(Sunfish entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.6F;
        f += (entity.getMobSize() * 0.3f);
        f *= entity.getRenderScale();
        f *= (Sunfish.SpeciesSunfish.values()[entity.getSpecies()].scale);
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getEntityTexture(@Nonnull Sunfish entity) {
        switch (entity.getSpecies()) {
            default:
            case 0: return SUNFISH;
            case 1: return SOUTHERN;
        }
    }
}
