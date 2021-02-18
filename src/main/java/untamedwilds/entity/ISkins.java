package untamedwilds.entity;

/**
 * Interface reserved for mobs which make use of Skins, defined as a List of skins
 * This interface should not be used alongside ISpecies, as both make use of "Species" to define variants
 * TODO: May be worth reworking everything into Skin lists?
 */

public interface ISkins {

    int getSkinNumber();

    //static void registerTextures(int i) { }
}
