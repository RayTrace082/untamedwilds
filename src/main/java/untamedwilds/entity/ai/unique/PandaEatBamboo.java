package untamedwilds.entity.ai.unique;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import untamedwilds.entity.ComplexMobTerrestrial;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class PandaEatBamboo extends Goal {
    private final ComplexMobTerrestrial taskOwner;
    private final Sorter sorter;
    private final int executionChance;
    private final int distance;
    private ItemEntity targetItem;
    private Item targetItemStack;

    public PandaEatBamboo(ComplexMobTerrestrial creature, int chance, int distance) {
        this.taskOwner = creature;
        this.executionChance = chance;
        this.sorter = new Sorter(creature);
        this.distance = distance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    public boolean canUse() {
        if (this.taskOwner.getHunger() > 80 || this.taskOwner.isBaby() || this.taskOwner.isSleeping()) {
            return false;
        }
        if (this.taskOwner.getRandom().nextInt(this.executionChance) != 0) {
            return false;
        }
        List<ItemEntity> list = this.taskOwner.level.getEntitiesOfClass(ItemEntity.class, this.getTargetableArea(distance));
        list.removeIf((ItemEntity item) -> item.getItem().getItem().equals("bamboo")); // Stupid way to do this

        if (list.isEmpty()) {
            return false;
        }
        else {
            list.sort(this.sorter);
            this.targetItem = list.get(0);
            this.targetItemStack = this.targetItem.getItem().getItem();
            return true;
        }
    }

    private AABB getTargetableArea(double targetDistance) {
        return this.taskOwner.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    public void start() {
        this.taskOwner.getNavigation().moveTo(this.targetItem.getX(), this.targetItem.getY(), this.targetItem.getZ(), 1D);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.targetItem == null || !this.targetItem.isAlive()) {
            return false;
        } else if (this.taskOwner.isSitting()) {
            return false;
        }
        return !this.taskOwner.isPathFinding();
    }

    @Override
    public void tick() {
        double distance = Math.sqrt(Math.pow(this.taskOwner.getX() - this.targetItem.getX(), 2.0D) + Math.pow(this.taskOwner.getZ() - this.targetItem.getZ(), 2.0D));
        if (distance < 1.5D) {
            this.taskOwner.addHunger(10);
            this.targetItem.getItem().shrink(1);
            if (this.targetItem.getItem().getCount() == 0) {
                this.targetItem.discard();
            }
            this.taskOwner.setAnimation(taskOwner.getAnimationEat());
        }
        if(this.taskOwner.getNavigation().isDone()){
            stop();
        }
        this.taskOwner.getNavigation().moveTo(this.targetItem.getX(), this.targetItem.getY(), this.targetItem.getZ(), 1D);
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity entity;

        private Sorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

        public int compare(Entity entity_1, Entity entity_2) {
            double dist_1 = this.entity.distanceToSqr(entity_1);
            double dist_2 = this.entity.distanceToSqr(entity_2);

            if (dist_1 < dist_2) {
                return -1;
            }
            else {
                return dist_1 > dist_2 ? 1 : 0;
            }
        }
    }
}
