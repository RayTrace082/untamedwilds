package untamedwilds.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INeedsPostUpdate;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class UntamedSpawnEggItem extends SpawnEggItem {

   private int currentSpecies;
   private final int speciesNumber;
   private boolean isCached;

   // TODO: Egg-Spawner interaction is screwed
   public UntamedSpawnEggItem(EntityType<?> typeIn, int species, int primaryColorIn, int secondaryColorIn, Properties builder) {
      super(typeIn, primaryColorIn, secondaryColorIn, builder);
      this.speciesNumber = species + 1;
      this.currentSpecies = 0;
      this.isCached = false;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
      tooltip.add(new TranslationTextComponent("item.untamedwilds.spawn_egg_info").mergeStyle(TextFormatting.GRAY));
      tooltip.add(new TranslationTextComponent("item.untamedwilds.spawn_egg_current",  this.getCurrentSpeciesNumber(stack)).mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));
   }

   private String getCurrentSpeciesNumber(ItemStack stack) {
      if (!this.isCached && stack.hasTag()) {
         if (stack.getTag().contains("EntityTag")) {
            this.currentSpecies = stack.getTag().getCompound("EntityTag").getInt("Variant");
            this.isCached = true;
         }
      }
      int i = this.currentSpecies - 1;
      return i >= 0 ? String.valueOf(i) : "Random";
   }

   public void increaseSpeciesNumber(int intIn) {
      //UntamedWilds.LOGGER.info(intIn % this.speciesNumber);
      this.currentSpecies = (intIn % this.speciesNumber);
      this.isCached = true;
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
      ItemStack itemstack = playerIn.getHeldItem(handIn);
      if (!worldIn.isRemote && playerIn.isSneaking()) {
         //UntamedWilds.LOGGER.info(this.currentSpecies);
         this.increaseSpeciesNumber(this.currentSpecies + 1);
         playerIn.sendStatusMessage(new TranslationTextComponent("item.untamedwilds.spawn_egg_change",  this.getCurrentSpeciesNumber(itemstack)), true);

         itemstack.setTag(new CompoundNBT());
         if (this.currentSpecies != 0) {
            CompoundNBT entityNBT = new CompoundNBT();
            entityNBT.putInt("Variant", this.currentSpecies - 1);
            itemstack.getTag().put("EntityTag", entityNBT);
         }
         return ActionResult.resultPass(itemstack);
      }

      BlockRayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
      if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
         return ActionResult.resultPass(itemstack);
      } else if (!(worldIn instanceof ServerWorld)) {
         return ActionResult.resultSuccess(itemstack);
      } else {
         BlockPos blockpos = raytraceresult.getPos();
         if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
            return ActionResult.resultPass(itemstack);
         } else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.getFace(), itemstack)) {
            EntityType<?> entitytype = this.getType(itemstack.getTag());
            Entity spawn = entitytype.spawn((ServerWorld)worldIn, itemstack, playerIn, blockpos, SpawnReason.SPAWN_EGG, false, false);
            if (spawn == null) {
               return ActionResult.resultPass(itemstack);
            }
            if (spawn instanceof ComplexMob) {
               ((ComplexMob) spawn).chooseSkinForSpecies((ComplexMob)spawn, true);
            }
            if (spawn instanceof INeedsPostUpdate) {
               ((INeedsPostUpdate) spawn).updateAttributes();
            }
            if (!playerIn.abilities.isCreativeMode) {
               itemstack.shrink(1);
            }
            playerIn.addStat(Stats.ITEM_USED.get(this));
            return ActionResult.resultConsume(itemstack);
         } else {
            return ActionResult.resultFail(itemstack);
         }
      }
   }

   @Override
   public ActionResultType onItemUse(ItemUseContext context) {
      World world = context.getWorld();
      if (!(world instanceof ServerWorld)) {
         return ActionResultType.SUCCESS;
      } else {
         ItemStack itemstack = context.getItem();
         BlockPos blockpos = context.getPos();
         Direction direction = context.getFace();
         BlockState blockstate = world.getBlockState(blockpos);
         if (blockstate.isIn(Blocks.SPAWNER)) {
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity instanceof MobSpawnerTileEntity) {
               AbstractSpawner abstractspawner = ((MobSpawnerTileEntity)tileentity).getSpawnerBaseLogic();
               EntityType<?> entitytype1 = this.getType(itemstack.getTag());
               abstractspawner.setEntityType(entitytype1);
               CompoundNBT entityNBT = new CompoundNBT();
               abstractspawner.write(entityNBT);
               UntamedWilds.LOGGER.info(entityNBT);

               ListNBT listnbt = new ListNBT();
               CompoundNBT compoundnbt = new CompoundNBT();
               CompoundNBT entityNBT_2 = new CompoundNBT();
               entityNBT_2.putString("id", entitytype1.getRegistryName().toString());
               entityNBT_2.putInt("Variant", this.currentSpecies - 1);
               compoundnbt.put("Entity", entityNBT_2);
               compoundnbt.putInt("Weight", 1);
               listnbt.add(compoundnbt);

               entityNBT.getList("SpawnPotentials", 10).clear();
               entityNBT.getList("SpawnPotentials", 10).add(listnbt);
               UntamedWilds.LOGGER.info(entityNBT);

               abstractspawner.read(entityNBT);
               tileentity.markDirty();
               world.notifyBlockUpdate(blockpos, blockstate, blockstate, 3);
               itemstack.shrink(1);
               return ActionResultType.CONSUME;
            }
         }

         BlockPos blockpos1;
         if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
            blockpos1 = blockpos;
         } else {
            blockpos1 = blockpos.offset(direction);
         }

         EntityType<?> entitytype = this.getType(itemstack.getTag());
         Entity spawn = entitytype.spawn((ServerWorld)world, itemstack, context.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
         if (spawn instanceof ComplexMob) {
            ((ComplexMob) spawn).chooseSkinForSpecies((ComplexMob)spawn, true);
         }
         itemstack.shrink(1);

         return ActionResultType.CONSUME;
      }
   }
}
