package untamedwilds.entity.ai.unique;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.phys.AABB;
import untamedwilds.entity.fish.EntityCatfish;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class CatfishGarbageBinGoal extends Goal {

    private final EntityCatfish taskOwner;
    private final ClosestSorter sorter;
    private final int distance;
    private ItemEntity targetItem;
    private final int executionChance;

    public CatfishGarbageBinGoal(EntityCatfish entityIn, int distance) {
        this(entityIn, distance, 100);
    }

    public CatfishGarbageBinGoal(EntityCatfish entityIn, int distance, int chance) {
        this.taskOwner = entityIn;
        this.sorter = new ClosestSorter(entityIn);
        this.distance = distance;
        this.executionChance = chance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    public boolean canUse() {
        if (this.taskOwner.isBaby() || this.taskOwner.isSleeping()) {
            return false;
        }
        if (this.taskOwner.getRandom().nextInt(this.executionChance) != 0) {
            return false;
        }
        List<ItemEntity> list = this.taskOwner.level.getEntitiesOfClass(ItemEntity.class, this.getTargettableArea(distance));

        list.removeIf((ItemEntity item) -> !item.getItem().isEdible());
        if (!list.isEmpty()) {
            list.sort(this.sorter);
            this.targetItem = list.get(0);
            return true;
        }
        return false;
    }

    private AABB getTargettableArea(double targetDistance) {
        return this.taskOwner.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void start() {
        this.taskOwner.getNavigation().moveTo(this.targetItem.getX(), this.targetItem.getY(), this.targetItem.getZ(), 1D);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.targetItem == null || !this.targetItem.isAlive()) {
            return false;
        } else if (!this.taskOwner.canMove()) {
            return false;
        }
        return !this.taskOwner.isPathFinding();
    }

    @Override
    public void tick() {
        double distance = Math.sqrt(Math.pow(this.taskOwner.getX() - this.targetItem.getX(), 2.0D) + Math.pow(this.taskOwner.getZ() - this.targetItem.getZ(), 2.0D));
        if (distance < 1.5D) {
            this.targetItem.getItem().shrink(1);
        }
        if(this.taskOwner.getNavigation().isDone()){
            stop();
        }
        this.taskOwner.getNavigation().moveTo(this.targetItem.getX(), this.targetItem.getY(), this.targetItem.getZ(), 1D);
    }

    public static class ClosestSorter implements Comparator<Entity> {
        private final Entity entity;

        private ClosestSorter(Entity entityIn)
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
