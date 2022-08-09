package untamedwilds.entity.ai.unique;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import untamedwilds.entity.mammal.EntityBear;

import java.util.EnumSet;

public class BearRaidChestsGoal extends Goal {
    private Container targetInventory;
    private BlockPos targetPos;
    private final EntityBear taskOwner;
    private final int executionChance;
    private int searchCooldown;
    private boolean continueTask;

    public BearRaidChestsGoal(EntityBear entityIn, int chance) {
        this.taskOwner = entityIn;
        this.executionChance = chance;
        this.searchCooldown = 100;
        this.continueTask = true;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.taskOwner.isTame() || !this.taskOwner.isOnGround() || this.taskOwner.getHunger() > 60 || this.taskOwner.getRandom().nextInt(this.executionChance) != 0 || this.taskOwner.getTarget() != null) {
            return false;
        }
        BlockPos pos = this.taskOwner.blockPosition();

        this.targetPos = getNearbyInventories(pos);
        return this.targetPos != null;
    }

    public void start() {
        this.taskOwner.getNavigation().moveTo((double)this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1, (double)this.targetPos.getZ() + 0.5D, 1f);
        super.start();
    }

    public void stop() {
    }

    public void tick() {
        //double distance = this.taskOwner.getDistance(this.targetInventory.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
        if (this.targetPos != null && this.taskOwner.distanceToSqr(targetPos.getX(), targetPos.getY(), targetPos.getZ()) < 4) {
            this.taskOwner.getLookControl().setLookAt(this.targetPos.getX(), this.targetPos.getY() + 1.5F, this.targetPos.getZ(), 10f, (float)this.taskOwner.getMaxHeadXRot());
            this.taskOwner.getNavigation().stop();
            this.taskOwner.setSitting(true);
            this.searchCooldown--;
            if (this.taskOwner.level.getBlockEntity(targetPos) instanceof ChestBlockEntity) {
                ChestBlockEntity chest = (ChestBlockEntity) this.taskOwner.level.getBlockEntity(targetPos);
                this.taskOwner.level.blockEvent(this.targetPos, chest.getBlockState().getBlock(), 1, 1);
            }
            if (this.searchCooldown == 0) {
                this.searchCooldown = 100;
                this.continueTask = stealItem();
            }
        }
        super.tick();
    }

    public boolean canContinueToUse() {
        if (this.taskOwner.getHunger() >= 60 || this.targetInventory.isEmpty()) {
            this.taskOwner.setSitting(false);
            if (this.taskOwner.level.getBlockEntity(targetPos) instanceof ChestBlockEntity) {
                ChestBlockEntity chest = (ChestBlockEntity) this.taskOwner.level.getBlockEntity(targetPos);
                this.taskOwner.level.blockEvent(this.targetPos, chest.getBlockState().getBlock(), 1, 0);
            }
            return false;
        }
        return this.continueTask;
    }

    private boolean stealItem() {
        if (this.targetInventory != null) {
            Direction enumfacing = Direction.DOWN;

            if (isInventoryEmpty(this.targetInventory, enumfacing)) {
                return false;
            }
            if (this.targetInventory instanceof WorldlyContainer) {
                WorldlyContainer isidedinventory = (WorldlyContainer) this.targetInventory;
                int[] aint = isidedinventory.getSlotsForFace(enumfacing);

                for (int i : aint) {
                    ItemStack itemstack = this.targetInventory.getItem(i);

                    if (!itemstack.isEmpty() && canExtractItemFromSlot(this.targetInventory, itemstack, i, enumfacing))
                    {
                        ItemStack itemstack1 = itemstack.copy();
                        this.targetInventory.setItem(i, ItemStack.EMPTY);
                        if (itemstack1.getItem().isEdible()) {
                            this.taskOwner.playSound(SoundEvents.PLAYER_BURP, 1, 1);
                            this.taskOwner.addHunger((itemstack1.getItem().getFoodProperties().getNutrition() * 10 * itemstack1.getCount()));
                            for(Pair<MobEffectInstance, Float> pair : itemstack1.getItem().getFoodProperties().getEffects()) {
                                if (pair.getFirst() != null && this.taskOwner.level.random.nextFloat() < pair.getSecond()) {
                                    this.taskOwner.addEffect(new MobEffectInstance(pair.getFirst()));
                                }
                            }
                            return false;
                        }
                        if (!PotionUtils.getMobEffects(itemstack1).isEmpty()) {
                            this.taskOwner.playSound(SoundEvents.PLAYER_BURP, 1, 1);
                            this.taskOwner.addHunger(10);
                            for(MobEffectInstance effectinstance : PotionUtils.getMobEffects(itemstack1)) {
                                if (effectinstance.getEffect().isInstantenous()) {
                                    effectinstance.getEffect().applyInstantenousEffect(this.taskOwner, this.taskOwner, this.taskOwner, effectinstance.getAmplifier(), 1.0D);
                                } else {
                                    this.taskOwner.addEffect(new MobEffectInstance(effectinstance));
                                }
                            }
                            return false;
                        }
                        this.taskOwner.spawnAtLocation(itemstack, 0.2f);
                        return true;
                    }
                }
            } else {
                int j = this.targetInventory.getContainerSize();

                for (int k = 0; k < j; ++k) {
                    ItemStack itemstack = this.targetInventory.getItem(k);

                    if (!itemstack.isEmpty() && canExtractItemFromSlot(this.targetInventory, itemstack, k, enumfacing))
                    {
                        ItemStack itemstack1 = itemstack.copy();
                        this.targetInventory.setItem(k, ItemStack.EMPTY);
                        this.taskOwner.setAnimation(EntityBear.ATTACK_SWIPE);
                        if (itemstack1.getItem().isEdible()) {
                            this.taskOwner.playSound(SoundEvents.PLAYER_BURP, 1, 1);
                            this.taskOwner.addHunger((itemstack1.getItem().getFoodProperties().getNutrition() * 10 * itemstack1.getCount()));
                            for(Pair<MobEffectInstance, Float> pair : itemstack1.getItem().getFoodProperties().getEffects()) {
                                if (pair.getFirst() != null && this.taskOwner.level.random.nextFloat() < pair.getSecond()) {
                                    this.taskOwner.addEffect(new MobEffectInstance(pair.getFirst()));
                                }
                            }
                            return false;
                        }
                        if (!PotionUtils.getMobEffects(itemstack1).isEmpty()) {
                            this.taskOwner.playSound(SoundEvents.GENERIC_DRINK, 1, 1);
                            this.taskOwner.addHunger(10);
                            for(MobEffectInstance effectinstance : PotionUtils.getMobEffects(itemstack1)) {
                                if (effectinstance.getEffect().isInstantenous()) {
                                    effectinstance.getEffect().applyInstantenousEffect(this.taskOwner, this.taskOwner, this.taskOwner, effectinstance.getAmplifier(), 1.0D);
                                } else {
                                    this.taskOwner.addEffect(new MobEffectInstance(effectinstance));
                                }
                            }
                            return false;
                        }
                        if (!(itemstack1.getItem().isEdible()) || !PotionUtils.getMobEffects(itemstack).isEmpty()) {
                            this.taskOwner.spawnAtLocation(itemstack, 0.2f);
                        }
                        else {
                            this.taskOwner.playSound(SoundEvents.PLAYER_BURP, 1, 1);
                            return false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static Container getInventoryAtPosition(Level worldIn, BlockPos pos) {
        Container iinventory = null;
        BlockState state = worldIn.getBlockState(pos);

        if (worldIn.getBlockEntity(pos) != null) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);

            if (tileentity instanceof Container) {
                iinventory = (Container)tileentity;

                /*if (iinventory instanceof ChestTileEntity && block instanceof ChestBlock) {
                    iinventory = (Container) ((ChestBlock)block).getContainer(worldIn.getBlockState(pos), worldIn, pos);
                }*/
            }
        }

        return iinventory;
    }

    private static boolean isInventoryEmpty(Container inventoryIn, Direction side) {
        if (inventoryIn instanceof WorldlyContainer) {
            WorldlyContainer isidedinventory = (WorldlyContainer)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int i : aint) {
                if (!isidedinventory.getItem(i).isEmpty()) {
                    return false;
                }
            }
        }
        else {
            int j = inventoryIn.getContainerSize();

            for (int k = 0; k < j; ++k) {
                if (!inventoryIn.getItem(k).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean canExtractItemFromSlot(Container inventoryIn, ItemStack stack, int index, Direction side) {
        return !(inventoryIn instanceof WorldlyContainer) || ((WorldlyContainer)inventoryIn).canTakeItemThroughFace(index, stack, side);
    }

    private BlockPos getNearbyInventories(BlockPos roomCenter) {
        int X = 15;
        int Y = 3;
        //List<BlockPos> inventories = new ArrayList<>();
        for (BlockPos blockpos : BlockPos.betweenClosed(roomCenter.offset(-X, -Y, -X), roomCenter.offset(X, Y, X))) {
            if (this.taskOwner.level.getBlockEntity(blockpos) != null && getInventoryAtPosition(this.taskOwner.level, blockpos) != null) {
                if (!isInventoryEmpty(getInventoryAtPosition(this.taskOwner.level, blockpos), Direction.UP)) {
                    this.targetInventory = getInventoryAtPosition(this.taskOwner.level, blockpos);
                    return blockpos; // For some bizarre reason, the blockPos inside the array changes once it exits the loop,
                    // so yeah, returning the first inventory instead. The code remains commented just in case I ever figure this one out
                    //inventories.add((BlockPos)blockpos);
                }
            }
        }
        return null;
        /*if (inventories.isEmpty()) {
            return null;
        }
        return inventories.get(this.taskOwner.getRandom().nextInt(Math.max(inventories.size() - 1, 1)));*/
    }
}