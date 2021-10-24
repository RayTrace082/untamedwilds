package untamedwilds.entity.amphibian;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.SpeciesDataHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityNewt extends ComplexMobAmphibious implements ISpecies, INewSkins {

    public int swimProgress;

    public EntityNewt(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new ComplexMobAquatic.MoveHelperController(this, 0.8F);
        this.swimSpeedMult = 3;
        this.buoyancy = 0.992F;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.14D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 8.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 2.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, (float)this.getAttributeValue(Attributes.FOLLOW_RANGE), 1D, 1.3D, input -> getEcoLevel(input) > 3));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 120));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, 120, 0, false));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    private boolean isAquatic() {
        return ENTITY_DATA_HASH.get(this.getType()).getFlags(this.getVariant(), "isAquatic") == 1;
    }

    public boolean wantsToLeaveWater() { return !this.isAquatic(); }

    public boolean wantsToEnterWater() { return this.isAquatic(); }

    public boolean isPushedByWater() {
        return false;
    }

    public void livingTick() {
        super.livingTick();

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
        else {
            if (this.isInWater() && !this.isOnGround() && this.swimProgress < 20) {
                this.swimProgress++;
            } else if ((!this.isInWater() || this.isOnGround()) && this.swimProgress > 0) {
                this.swimProgress--;
            }
        }
    }

    /* Breeding conditions for the giant salamander are:
     * A nearby giant salamander of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityNewt> list = this.world.getEntitiesWithinAABB(EntityNewt.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setGrowingAge(this.getGrowingAge());
                    list.get(0).setGrowingAge(this.getGrowingAge());
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        EntityUtils.dropEggs(this, "egg_newt", this.getOffspring());
        return null;
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_newt", itemstack);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }
        return super.func_230254_b_(player, hand);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = this.isChild() ? SoundEvents.ENTITY_TURTLE_SHAMBLE_BABY : SoundEvents.ENTITY_TURTLE_SHAMBLE;
        this.playSound(soundevent, 0.15F, 1.0F);
    }

    public float getModelScale() { return 0.8F; }

    // TODO: Move this to ComplexMob.class
    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || isArtificialSpawnReason(reason)) {
            return this.rand.nextInt(ENTITY_DATA_HASH.get(this.getType()).getSpeciesData().size());
        }
        List<Integer> validTypes = new ArrayList<>();
        for (SpeciesDataHolder speciesDatum : ENTITY_DATA_HASH.get(this.getType()).getSpeciesData()) {
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
}
