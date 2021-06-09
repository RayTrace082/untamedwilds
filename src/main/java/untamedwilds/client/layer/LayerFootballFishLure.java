package untamedwilds.client.layer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;

@OnlyIn(Dist.CLIENT)
public class LayerFootballFishLure<T extends Entity, M extends EntityModel<T>> extends AbstractEyesLayer<T, M> {

    private final RenderType TEXTURE = RenderType.getEyes(new ResourceLocation(UntamedWilds.MOD_ID, "textures/entity/football_fish/glint.png"));

    public LayerFootballFishLure(IEntityRenderer<T, M> rendererIn) {
        super(rendererIn);
    }

    public RenderType getRenderType() {
        return TEXTURE;
    }
}
