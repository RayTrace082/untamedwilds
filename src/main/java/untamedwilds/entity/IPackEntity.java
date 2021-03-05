package untamedwilds.entity;

public interface IPackEntity {

    static void initPack(ComplexMob entity) {
        entity.herd = new HerdEntity(entity, ((IPackEntity)entity).getMaxPackSize());
    }

    int getMaxPackSize();

    default boolean shouldLeavePack() {
        return false;
    }
}
