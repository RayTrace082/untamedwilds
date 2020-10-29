package untamedwilds.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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

    // Spawn particles throughout the entity. Features safe casting of worldIn
    public static <T extends IParticleData> void spawnParticlesOnEntity(World worldIn, LivingEntity entityIn, T particle, int count, int iter) {
        if (worldIn.isRemote) {
            return;
        }
        for (int i = 0; i < iter;  i++) {
            ((ServerWorld)worldIn).spawnParticle(particle, entityIn.getPosX(), entityIn.getPosY() + (double)entityIn.getHeight() / 1.5D, entityIn.getPosZ(), count, entityIn.getWidth() / 4.0F, entityIn.getHeight() / 4.0F, entityIn.getWidth() / 4.0F, 0.05D);
        }
    }
}
