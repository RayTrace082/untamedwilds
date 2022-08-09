package untamedwilds.client.layer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;

@OnlyIn(Dist.CLIENT)
public class LayerFootballFishLure<T extends Entity, M extends EntityModel<T>> extends EyesLayer<T, M> {

    private final RenderType TEXTURE = RenderType.eyes(new ResourceLocation(UntamedWilds.MOD_ID, "textures/entity/football_fish/glint.png"));

    public LayerFootballFishLure(RenderLayerParent<T, M> rendererIn) {
        super(rendererIn);
    }

    public RenderType renderType() {
        return TEXTURE;
    }
}
