package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.client.model.ModelGiantClam;
import untamedwilds.entity.mollusk.GiantClam;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class RendererGiantClam extends MobRenderer<GiantClam, EntityModel<GiantClam>> {

    private static final ModelGiantClam GIANT_CLAM_MODEL = new ModelGiantClam();

    private static final ResourceLocation DERASA         = new ResourceLocation("untamedwilds:textures/entity/giant_clam/derasa.png");
    private static final ResourceLocation GIGAS         = new ResourceLocation("untamedwilds:textures/entity/giant_clam/gigas.png");
    private static final ResourceLocation MAXIMA        = new ResourceLocation("untamedwilds:textures/entity/giant_clam/maxima.png");
    private static final ResourceLocation SQUAMOSA      = new ResourceLocation("untamedwilds:textures/entity/giant_clam/squamosa.png");

    public RendererGiantClam(EntityRendererManager renderManager) {
        super(renderManager, GIANT_CLAM_MODEL, 1F);
    }

    @Override
    protected void preRenderCallback(GiantClam entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = GiantClam.SpeciesGiantClam.values()[entity.getSpecies()].scale;
        f += (entity.getMobSize() * 0.25f);
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = GiantClam.SpeciesGiantClam.values()[entity.getSpecies()].scale * 0.8f;
    }

    public ResourceLocation getEntityTexture(@Nonnull GiantClam entity) {
        switch (entity.getSpecies()) {
            default:
            case 0: return DERASA;
            case 1: return GIGAS;
            case 2: return MAXIMA;
            case 3: return SQUAMOSA;
        }
    }
}
