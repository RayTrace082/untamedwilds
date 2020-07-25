package untamedwilds.entity.reptile;

import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAmphibious;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.AmphibiousRandomSwimGoal;
import untamedwilds.entity.ai.AmphibiousTransition;
import untamedwilds.entity.ai.SmartAvoidGoal;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntitySoftshellTurtle extends ComplexMobAmphibious implements ISpecies {

    private static final String BREEDING = "EARLY_SUMMER";
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();
    public int baskProgress;

    public EntitySoftshellTurtle(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 1;
        this.ecoLevel = 4;
        this.buoyancy = 0.998F;
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2F);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1D, 1.1D, input -> this.getEcoLevel(input) > 4));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 120));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, false, input -> this.getEcoLevel(input) < 4));
    }

    public boolean wantsToLeaveWater() { return this.world.getDayTime() > 5000 && this.world.getDayTime() < 7000; }

    public boolean wantsToEnterWater() { return !(this.world.getDayTime() > 5000 && this.world.getDayTime() < 7000); }

    public boolean isPushedByWater() {
        return false;
    }

    public void onDeath(DamageSource cause) {
        if (cause == DamageSource.ANVIL && !this.isChild()) {
            // TODO: Advancement trigger here
            ItemEntity entityitem = this.entityDropItem(new ItemStack(ModItems.FOOD_TURTLE_SOUP.get()), 0.2F);
            entityitem.getItem().setCount(1);
        }
        super.onDeath(cause);
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
        if (this.world.isRemote && !this.isInWater() && this.baskProgress < 100) {
            this.baskProgress++;
        } else if (this.world.isRemote && this.isInWater() && this.baskProgress > 0) {
            this.baskProgress--;
        }
    }

    /* Breeding conditions for the softshell_turtle are:
     * A nearby softshell_turtle of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && !this.isSleeping() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth()) {
            List<EntitySoftshellTurtle> list = this.world.getEntitiesWithinAABB(EntitySoftshellTurtle.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
            list.removeIf(input -> (input.getGender() == this.getGender()) || (input.getSpecies() != this.getSpecies()) || input.getGrowingAge() != 0);
            if (list.size() >= 1) {
                this.setGrowingAge(GROWING);
                list.get(0).setGrowingAge(GROWING);
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) {
        ItemEntity entityitem = this.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":egg_softshell_turtle_" + this.getRawSpeciesName().toLowerCase()))), 0.2F);
        entityitem.getItem().setCount(1 + this.rand.nextInt(4));
        return null;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);

        if (!this.world.isRemote && !this.isBreedingItem(itemstack) && itemstack.isEmpty() && this.isAlive()) {
            ItemEntity entityitem = this.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":softshell_turtle_" + this.getRawSpeciesName().toLowerCase()))), 0.2F);
            entityitem.setMotion((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F, this.rand.nextFloat() * 0.05F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
            entityitem.getItem().setTag(this.writeEntityToNBT(this));
            if (this.hasCustomName()) {
                entityitem.getItem().setDisplayName(this.getCustomName());
            }
            this.remove();
            return true;
        }
        return super.processInteract(player, hand);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = this.isChild() ? SoundEvents.ENTITY_TURTLE_SHAMBLE_BABY : SoundEvents.ENTITY_TURTLE_SHAMBLE;
        this.playSound(soundevent, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_TURTLE_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_TURTLE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_TURTLE_DEATH;
    }

    public String getBreedingSeason() {
        return BREEDING;
    }
    public int getAdulthoodTime() { return GROWING; }

    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == Items.COD; }

    @Override
    public int setSpeciesByBiome(Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET) {
            return this.rand.nextInt(SpeciesSoftshellTurtle.values().length);
        }
        return SpeciesSoftshellTurtle.getSpeciesByBiome(biome);
    }
    public String getSpeciesName() { return new TranslationTextComponent("item.untamedwilds.softshell_turtle_" + this.getRawSpeciesName()).getUnformattedComponentText(); }
    public String getRawSpeciesName() { return SpeciesSoftshellTurtle.values()[this.getSpecies()].name().toLowerCase(); }

    public enum SpeciesSoftshellTurtle implements IStringSerializable {

        BLACK			(0, 1.0F, 2, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.JUNGLE),
        CHINESE	        (1, 0.8F, 3, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.JUNGLE),
        FLAPSHELL		(2, 1.0F, 2, BiomeDictionary.Type.SWAMP),
        NILE			(3, 1.1F, 2, BiomeDictionary.Type.SAVANNA),
        PEACOCK			(4, 0.9F, 1, BiomeDictionary.Type.JUNGLE),
        PIG_NOSE		(5, 0.8F, 2, BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.SWAMP),
        SPINY		    (6, 0.7F, 3, BiomeDictionary.Type.SWAMP);

        public Float sizeMult;
        public int species;
        public int rolls;
        public BiomeDictionary.Type[] spawnBiomes;

        SpeciesSoftshellTurtle(int species, Float smult, int rolls, BiomeDictionary.Type... biomes) {
            this.species = species;
            this.sizeMult = smult;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getName() {
            return I18n.format("entity.softshell_turtle." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesSoftshellTurtle> types = new ArrayList<>();
            if (biome.getDefaultTemperature() < 0.8F) {
                return 99;
            }
            for (SpeciesSoftshellTurtle type : values()) {
                for(BiomeDictionary.Type biomeTypes : type.spawnBiomes) {
                    if(BiomeDictionary.hasType(biome, biomeTypes)){
                        for (int i=0; i < type.rolls; i++) {
                            types.add(type);
                        }
                    }
                }
            }
            if (types.isEmpty()) {
                return 99;
            } else {
                return types.get(new Random().nextInt(types.size())).getSpecies();
            }
        }
    }
}
