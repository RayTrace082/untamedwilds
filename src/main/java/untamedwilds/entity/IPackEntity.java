package untamedwilds.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

public interface IPackEntity {
    // Entities using this interface should have the following as methods

    public default void initPack(IPackEntity entity) {

    }

    // Soft cap for pack size
    int getMaxPackSize(IPackEntity entity);

}
