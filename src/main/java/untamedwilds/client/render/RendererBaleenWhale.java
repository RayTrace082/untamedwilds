package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelBaleenWhale;
import untamedwilds.entity.mammal.EntityBaleenWhale;

public class RendererBaleenWhale extends MobRenderer<EntityBaleenWhale, EntityModel<EntityBaleenWhale>> {

    private static final ModelBaleenWhale WHALE_MODEL = new ModelBaleenWhale();

    public RendererBaleenWhale(EntityRendererProvider.Context rendermanager) {
        super(rendermanager, WHALE_MODEL, 0.2F);
    }
    @Override
    protected void scale(EntityBaleenWhale entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityBaleenWhale entity) {
        return entity.getTexture();
    }
}
