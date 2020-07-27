package untamedwilds.entity.ai.unique;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import untamedwilds.entity.ComplexMobTerrestrial;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class PandaEatBamboo extends Goal  {
    private ComplexMobTerrestrial taskOwner;
    private final Sorter sorter;
    private final int executionChance;
    private int distance;
    private ItemEntity targetItem;
    private Item targetItemStack;

    public PandaEatBamboo(ComplexMobTerrestrial creature, int chance, int distance) {
        this.taskOwner = creature;
        this.executionChance = chance;
        this.sorter = new Sorter(creature);
        this.distance = distance;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    public boolean shouldExecute() {
        if (this.taskOwner.getHunger() > 80 || this.taskOwner.isChild() || this.taskOwner.isSleeping()) {
            return false;
        }
        if (this.taskOwner.getRNG().nextInt(this.executionChance) != 0) {
            return false;
        }
        List<ItemEntity> list = this.taskOwner.world.getEntitiesWithinAABB(ItemEntity.class, this.getTargetableArea(distance));
        //UntamedWilds.LOGGER.info(list.get(0).getItem().getItem());
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

    private AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ(this.targetItem.getPosX(), this.targetItem.getPosY(), this.targetItem.getPosZ(), 1D);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.targetItem == null || !this.targetItem.isAlive()) {
            return false;
        } else if (this.taskOwner.isSitting()) {
            return false;
        }
        return !this.taskOwner.hasPath();
    }

    @Override
    public void tick() {
        double distance = Math.sqrt(Math.pow(this.taskOwner.getPosX() - this.targetItem.getPosX(), 2.0D) + Math.pow(this.taskOwner.getPosZ() - this.targetItem.getPosZ(), 2.0D));
        if (distance < 1.5D) {
            this.taskOwner.addHunger(10);
            this.targetItem.getItem().shrink(1);
            if (this.targetItem.getItem().getCount() == 0) {
                this.targetItem.remove();
            }
            this.taskOwner.setAnimation(taskOwner.getAnimationEat());
        }
        if(this.taskOwner.getNavigator().noPath()){
            resetTask();
        }
        this.taskOwner.getNavigator().tryMoveToXYZ(this.targetItem.getPosX(), this.targetItem.getPosY(), this.targetItem.getPosZ(), 1D);
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity entity;

        private Sorter(Entity entityIn)
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
