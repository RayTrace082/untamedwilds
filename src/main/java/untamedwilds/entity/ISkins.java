package untamedwilds.entity;

/**
 * Interface reserved for mobs which make use of Skins, defined as a List of skins
 * This interface should not be used alongside ISpecies, as both make use of the "Variant" parameter to define variants
 */

@Deprecated
public interface ISkins {

    //List<ResourceLocation> TEXTURES = new ArrayList<>();

    int getSkinNumber();

    /*static void registerTextures(int count, ISkins clazz, String path) {
        for(int i = 1; i < count + 1; i++)
            clazz.TEXTURES.add(new ResourceLocation(UntamedWilds.MOD_ID, String.format("textures/entity/" + path + "_%d.png", i)));
    }*/
}
