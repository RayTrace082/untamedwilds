package untamedwilds.entity;

import untamedwilds.util.EntityUtils;

public interface IPackEntity {

    static void initPack(ComplexMob entity) {
        entity.herd = new HerdEntity(entity, EntityUtils.getPackSize(entity.getType(), entity.getVariant()));
    }

    default boolean shouldLeavePack() {
        return false;
    }
}
