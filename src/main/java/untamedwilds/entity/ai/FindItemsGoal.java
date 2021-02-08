package untamedwilds.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.math.AxisAlignedBB;
import untamedwilds.entity.ComplexMobTerrestrial;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class FindItemsGoal extends Goal  {

    private final ComplexMobTerrestrial taskOwner;
    private final ClosestSorter sorter;
    private final int distance;
    private ItemEntity targetItem;
    private Food targetItemStack;
    private final int executionChance;
    private final boolean hyperCarnivore;
    private final boolean hyperHerbivore;

    public FindItemsGoal(ComplexMobTerrestrial creature, int distance) {
        this(creature, distance, 100, false, false);
    }

    public FindItemsGoal(ComplexMobTerrestrial creature, int distance, boolean hyperCarnivore) {
        this(creature, distance, 100, hyperCarnivore, false);
    }

    public FindItemsGoal(ComplexMobTerrestrial creature, int distance, int chance, boolean hyperCarnivore, boolean hyperHerbivore) {
        this.taskOwner = creature;
        this.sorter = new ClosestSorter(creature);
        this.distance = distance;
        this.executionChance = chance;
        this.hyperCarnivore = hyperCarnivore;
        this.hyperHerbivore = hyperHerbivore;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Flag.TARGET));
    }

    public boolean shouldExecute() {
        if (this.taskOwner.getHunger() > 80 || this.taskOwner.isChild() || this.taskOwner.isSleeping()) {
            return false;
        }
        if (this.taskOwner.getRNG().nextInt(this.executionChance) != 0) {
            return false;
        }
        List<ItemEntity> list = this.taskOwner.world.getEntitiesWithinAABB(ItemEntity.class, this.getTargettableArea(distance));

        list.removeIf((ItemEntity item) -> !item.getItem().isFood());
        if (!list.isEmpty()) {
            if (this.hyperCarnivore) {
                list.removeIf((ItemEntity item) -> !isMeat(item.getItem().getItem()));
            } else if (this.hyperHerbivore) {
                list.removeIf((ItemEntity item) -> isMeat(item.getItem().getItem()));
            }
            if (!list.isEmpty()) {
                list.sort(this.sorter);
                this.targetItem = list.get(0);
                this.targetItemStack = this.targetItem.getItem().getItem().getFood();
                return true;
            }
        }
        return false;
    }

    private AxisAlignedBB getTargettableArea(double targetDistance) {
        return this.taskOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    private boolean isMeat(Item item) {
        if (item.getItem().getItem().getFood() == null) {
            return false;
        }
        return item.getItem().getItem().getFood().isMeat() || item.getItem().getItem().isIn(ItemTags.FISHES);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ(this.targetItem.getPosX(), this.targetItem.getPosY(), this.targetItem.getPosZ(), 1D);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.targetItem == null || !this.targetItem.isAlive()) {
            return false;
        } else if (!this.taskOwner.canMove()) {
            return false;
        }
        return !this.taskOwner.hasPath();
    }

    @Override
    public void tick() {
        double distance = Math.sqrt(Math.pow(this.taskOwner.getPosX() - this.targetItem.getPosX(), 2.0D) + Math.pow(this.taskOwner.getPosZ() - this.targetItem.getPosZ(), 2.0D));
        if (distance < 1.5D) {
            this.taskOwner.addHunger(Math.max(targetItemStack.getHealing() * 10, 10));
            this.targetItem.getItem().shrink(1);
            this.taskOwner.setAnimation(taskOwner.getAnimationEat());
        }
        if(this.taskOwner.getNavigator().noPath()){
            resetTask();
        }
        this.taskOwner.getNavigator().tryMoveToXYZ(this.targetItem.getPosX(), this.targetItem.getPosY(), this.targetItem.getPosZ(), 1D);
    }

    public static class ClosestSorter implements Comparator<Entity> {
        private final Entity entity;

        private ClosestSorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

        public int compare(Entity entity_1, Entity entity_2) {
            double dist_1 = this.entity.getDistanceSq(entity_1);
            double dist_2 = this.entity.getDistanceSq(entity_2);
            if (dist_1 < dist_2) {
                return -1;
            }
            else {
                return dist_1 > dist_2 ? 1 : 0;
            }
        }
    }
}
