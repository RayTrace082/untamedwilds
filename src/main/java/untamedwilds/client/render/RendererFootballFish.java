package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.layer.LayerFootballFishLure;
import untamedwilds.client.model.ModelFootballFish;
import untamedwilds.entity.fish.EntityFootballFish;

import javax.annotation.Nonnull;

public class RendererFootballFish extends MobRenderer<EntityFootballFish, SegmentedModel<EntityFootballFish>> {

    private static final ModelFootballFish FOOTBALL_FISH_MODEL = new ModelFootballFish();

    public RendererFootballFish(EntityRendererManager rendermanager) {
        super(rendermanager, FOOTBALL_FISH_MODEL, 0.6F);
        this.addLayer(new LayerFootballFishLure<>(this));
    }

    protected void preRenderCallback(EntityFootballFish entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * 0.8F;
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityFootballFish entity) {
        return entity.getTexture();
    }
}
