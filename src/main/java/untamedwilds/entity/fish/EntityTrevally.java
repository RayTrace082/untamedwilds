package untamedwilds.entity.fish;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.FishReturnToSchoolGoal;
import untamedwilds.entity.ai.FishWanderAsSchoolGoal;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.SpeciesDataHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityTrevally extends ComplexMobAquatic implements ISpecies, IPackEntity, INewSkins {

    public EntityTrevally(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 3;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.75D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 8.0D);
    }

    protected void registerGoals() {
        //super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new FishWanderAsSchoolGoal(this));
        this.goalSelector.addGoal(4, new FishReturnToSchoolGoal(this));
    }

    public void livingTick() {
        if (this.herd == null) {
            IPackEntity.initPack(this);
        }
        else {
            this.herd.tick();
        }
        if (!this.world.isRemote) {
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }
        super.livingTick();
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_trevally", itemstack);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }
        return super.func_230254_b_(player, hand);
    }

    /* Breeding conditions for the Trevally are:
     * A nearby Trevally of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityTrevally> list = this.world.getEntitiesWithinAABB(EntityTrevally.class, this.getBoundingBox().grow(12.0D, 8.0D, 12.0D));
            list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
            if (list.size() >= 1) {
                this.setGrowingAge(this.getGrowingAge());
                list.get(0).setGrowingAge(this.getGrowingAge());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        EntityUtils.dropEggs(this, "egg_trevally", this.getOffspring());
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }

    // Flags Parameters
    public int getMaxPackSize() {
        return EntityUtils.getPackSize(this.getType(), this.getVariant());
    }

    public boolean shouldLeavePack() {
        return this.rand.nextInt(120) == 0;
    }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (biomekey.equals(Biomes.COLD_OCEAN) || biomekey.equals(Biomes.DEEP_COLD_OCEAN) || biomekey.equals(Biomes.FROZEN_OCEAN) || biomekey.equals(Biomes.DEEP_FROZEN_OCEAN)) {
            return 99;
        }
        if (isArtificialSpawnReason(reason) || ConfigGamerules.randomSpecies.get()) {
            return this.rand.nextInt(ENTITY_DATA_HASH.get(this.getType()).getSpeciesData().size());
        }
        List<Integer> validTypes = new ArrayList<>();
        if (ComplexMob.ENTITY_DATA_HASH.containsKey(this.getType())) {
            for (SpeciesDataHolder speciesDatum : ComplexMob.ENTITY_DATA_HASH.get(this.getType()).getSpeciesData()) {
                for(Biome.Category biomeTypes : speciesDatum.getBiomeCategories()) {
                    if(biome.getCategory() == biomeTypes){
                        for (int i=0; i < speciesDatum.getRarity(); i++) {
                            validTypes.add(speciesDatum.getVariant());
                        }
                    }
                }
            }
            if (validTypes.isEmpty()) {
                return 99;
            } else {
                return validTypes.get(new Random().nextInt(validTypes.size()));
            }
        }
        return 99;
    }
}
