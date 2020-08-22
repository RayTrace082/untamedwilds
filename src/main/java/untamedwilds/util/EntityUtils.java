package untamedwilds.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Items;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

// A series of functions that are not meant to be limited to ComplexMobs
public abstract class EntityUtils {

    // Destroy the boat of the selected entity (if it exists)
    public static boolean destroyBoat(World worldIn, LivingEntity entityIn) {
        if (entityIn.getRidingEntity() != null && entityIn.getRidingEntity() instanceof BoatEntity) {
            BoatEntity boat = (BoatEntity) entityIn.getRidingEntity();
            boat.remove();
            if (worldIn.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                for(int j = 0; j < 3; ++j) {
                    boat.entityDropItem(boat.getBoatType().asPlank());
                }
                for(int k = 0; k < 2; ++k) {
                    boat.entityDropItem(Items.STICK);
                }
            }
            return true;
        }
        return false;
    }
}
