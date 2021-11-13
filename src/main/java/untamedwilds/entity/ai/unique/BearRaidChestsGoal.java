package untamedwilds.entity.ai.unique;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import untamedwilds.entity.mammal.EntityBear;

import java.util.EnumSet;

public class BearRaidChestsGoal extends Goal {
    private IInventory targetInventory;
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
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.taskOwner.isTamed() || !this.taskOwner.isOnGround() || this.taskOwner.getHunger() > 60 || this.taskOwner.getRNG().nextInt(this.executionChance) != 0 || this.taskOwner.getAttackTarget() != null) {
            return false;
        }
        BlockPos pos = this.taskOwner.getPosition();

        this.targetPos = getNearbyInventories(pos);
        return this.targetPos != null;
    }

    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ((double)this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1, (double)this.targetPos.getZ() + 0.5D, 1f);
        super.startExecuting();
    }

    public void resetTask() {
        super.resetTask();
    }

    public void tick() {
        //double distance = this.taskOwner.getDistance(this.targetInventory.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
        if (this.targetPos != null && this.taskOwner.getDistanceSq(targetPos.getX(), targetPos.getY(), targetPos.getZ()) < 4) {
            this.taskOwner.getLookController().setLookPosition(this.targetPos.getX(), this.targetPos.getY() + 1.5F, this.targetPos.getZ(), 10f, (float)this.taskOwner.getVerticalFaceSpeed());
            this.taskOwner.getNavigator().clearPath();
            this.taskOwner.setSitting(true);
            this.searchCooldown--;
            if (this.taskOwner.world.getTileEntity(targetPos) instanceof ChestTileEntity) {
                ChestTileEntity chest = (ChestTileEntity) this.taskOwner.world.getTileEntity(targetPos);
                this.taskOwner.world.addBlockEvent(this.targetPos, chest.getBlockState().getBlock(), 1, 1);
            }
            if (this.searchCooldown == 0) {
                this.searchCooldown = 100;
                this.continueTask = stealItem();
            }
        }
        super.tick();
    }

    public boolean shouldContinueExecuting() {
        if (this.taskOwner.getHunger() >= 60 || this.targetInventory.isEmpty()) {
            this.taskOwner.setSitting(false);
            if (this.taskOwner.world.getTileEntity(targetPos) instanceof ChestTileEntity) {
                ChestTileEntity chest = (ChestTileEntity) this.taskOwner.world.getTileEntity(targetPos);
                this.taskOwner.world.addBlockEvent(this.targetPos, chest.getBlockState().getBlock(), 1, 0);
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
            if (this.targetInventory instanceof ISidedInventory) {
                ISidedInventory isidedinventory = (ISidedInventory) this.targetInventory;
                int[] aint = isidedinventory.getSlotsForFace(enumfacing);

                for (int i : aint) {
                    ItemStack itemstack = this.targetInventory.getStackInSlot(i);

                    if (!itemstack.isEmpty() && canExtractItemFromSlot(this.targetInventory, itemstack, i, enumfacing))
                    {
                        ItemStack itemstack1 = itemstack.copy();
                        this.targetInventory.setInventorySlotContents(i, ItemStack.EMPTY);
                        if (itemstack1.getItem().isFood()) {
                            this.taskOwner.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1, 1);
                            this.taskOwner.addHunger((itemstack1.getItem().getFood().getHealing() * 10 * itemstack1.getCount()));
                            for(Pair<EffectInstance, Float> pair : itemstack1.getItem().getFood().getEffects()) {
                                if (pair.getFirst() != null && this.taskOwner.world.rand.nextFloat() < pair.getSecond()) {
                                    this.taskOwner.addPotionEffect(new EffectInstance(pair.getFirst()));
                                }
                            }
                            return false;
                        }
                        if (itemstack1.getItem().hasEffect(itemstack1)) {
                            this.taskOwner.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1, 1);
                            this.taskOwner.addHunger(10);
                            for(EffectInstance effectinstance : PotionUtils.getEffectsFromStack(itemstack1)) {
                                if (effectinstance.getPotion().isInstant()) {
                                    effectinstance.getPotion().affectEntity(this.taskOwner, this.taskOwner, this.taskOwner, effectinstance.getAmplifier(), 1.0D);
                                } else {
                                    this.taskOwner.addPotionEffect(new EffectInstance(effectinstance));
                                }
                            }
                            return false;
                        }
                        this.taskOwner.entityDropItem(itemstack, 0.2f);
                        return true;
                    }
                }
            } else {
                int j = this.targetInventory.getSizeInventory();

                for (int k = 0; k < j; ++k) {
                    ItemStack itemstack = this.targetInventory.getStackInSlot(k);

                    if (!itemstack.isEmpty() && canExtractItemFromSlot(this.targetInventory, itemstack, k, enumfacing))
                    {
                        ItemStack itemstack1 = itemstack.copy();
                        this.targetInventory.setInventorySlotContents(k, ItemStack.EMPTY);
                        this.taskOwner.setAnimation(EntityBear.ATTACK_SWIPE);
                        if (itemstack1.getItem().isFood()) {
                            this.taskOwner.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1, 1);
                            this.taskOwner.addHunger((itemstack1.getItem().getFood().getHealing() * 10 * itemstack1.getCount()));
                            for(Pair<EffectInstance, Float> pair : itemstack1.getItem().getFood().getEffects()) {
                                if (pair.getFirst() != null && this.taskOwner.world.rand.nextFloat() < pair.getSecond()) {
                                    this.taskOwner.addPotionEffect(new EffectInstance(pair.getFirst()));
                                }
                            }
                            return false;
                        }
                        if (itemstack1.getItem().hasEffect(itemstack1)) {
                            this.taskOwner.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1, 1);
                            this.taskOwner.addHunger(10);
                            for(EffectInstance effectinstance : PotionUtils.getEffectsFromStack(itemstack1)) {
                                if (effectinstance.getPotion().isInstant()) {
                                    effectinstance.getPotion().affectEntity(this.taskOwner, this.taskOwner, this.taskOwner, effectinstance.getAmplifier(), 1.0D);
                                } else {
                                    this.taskOwner.addPotionEffect(new EffectInstance(effectinstance));
                                }
                            }
                            return false;
                        }
                        if (!(itemstack1.getItem().isFood()) || !(itemstack1.getItem().hasEffect(itemstack1))) {
                            this.taskOwner.entityDropItem(itemstack, 0.2f);
                        }
                        else {
                            this.taskOwner.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1, 1);
                            return false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static IInventory getInventoryAtPosition(World worldIn, BlockPos pos) {
        IInventory iinventory = null;
        BlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();

        if (block.hasTileEntity(state)) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof IInventory) {
                iinventory = (IInventory)tileentity;

                /*if (iinventory instanceof ChestTileEntity && block instanceof ChestBlock) {
                    iinventory = (IInventory) ((ChestBlock)block).getContainer(worldIn.getBlockState(pos), worldIn, pos);
                }*/
            }
        }

        return iinventory;
    }

    private static boolean isInventoryEmpty(IInventory inventoryIn, Direction side) {
        if (inventoryIn instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int i : aint) {
                if (!isidedinventory.getStackInSlot(i).isEmpty()) {
                    return false;
                }
            }
        }
        else {
            int j = inventoryIn.getSizeInventory();

            for (int k = 0; k < j; ++k) {
                if (!inventoryIn.getStackInSlot(k).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, Direction side) {
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side);
    }

    private BlockPos getNearbyInventories(BlockPos roomCenter) {
        int X = 15;
        int Y = 3;
        //List<BlockPos> inventories = new ArrayList<>();
        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(roomCenter.add(-X, -Y, -X), roomCenter.add(X, Y, X))) {
            if (this.taskOwner.world.getTileEntity(blockpos) != null && getInventoryAtPosition(this.taskOwner.world, blockpos) != null) {
                if (!isInventoryEmpty(getInventoryAtPosition(this.taskOwner.world, blockpos), Direction.UP)) {
                    this.targetInventory = getInventoryAtPosition(this.taskOwner.world, blockpos);
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
        return inventories.get(this.taskOwner.getRNG().nextInt(Math.max(inventories.size() - 1, 1)));*/
    }
}