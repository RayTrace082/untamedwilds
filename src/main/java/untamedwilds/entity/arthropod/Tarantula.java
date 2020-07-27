package untamedwilds.entity.arthropod;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartAvoidGoal;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartSwimGoal;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModSounds;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tarantula extends ComplexMob implements ISpecies {

    private static final String BREEDING = "EARLY_SUMMER";
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();

    public int aggroProgress;
    public int webProgress;

    public Tarantula(EntityType<? extends Tarantula> type, World worldIn) {
        super(type, worldIn);
        this.ecoLevel = 3;
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> this.getEcoLevel(input) > 8));
        // this.goalSelector.addGoal(3, new TarantulaMakeWebs(this, 300));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, false, input -> this.getEcoLevel(input) < 4));
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            this.webProgress--;
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            this.setAngry(this.getAttackTarget() != null);
        }
        if (this.world.isRemote && this.isAngry() && this.aggroProgress < 40) {
            this.aggroProgress++;
        } else if (this.world.isRemote && !this.isAngry() && this.aggroProgress > 0) {
            this.aggroProgress--;
        }
    }

    /* Breeding conditions for the Tarantula are:
     * A nearby Tarantula of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && !this.isSleeping() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth()) {
            List<Tarantula> list = this.world.getEntitiesWithinAABB(Tarantula.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
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
        ItemEntity entityitem = this.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":egg_tarantula_" + this.getRawSpeciesName().toLowerCase()))), 0.2F);
        entityitem.getItem().setCount(1 + this.rand.nextInt(3));
        return null;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);

        if (!this.world.isRemote && !this.isBreedingItem(itemstack) && itemstack.isEmpty() && this.isAlive()) {
            ItemEntity entityitem = this.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":tarantula_" + this.getRawSpeciesName().toLowerCase()))), 0.2F);
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

    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 80, 0));
            }
            return true;
        }
        return flag;
    }

    public boolean isPotionApplicable(EffectInstance potionEffectIn) {
        return potionEffectIn.getPotion() != Effects.POISON && super.isPotionApplicable(potionEffectIn);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_TARANTULA_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    public String getBreedingSeason() {
        return BREEDING;
    }
    public int getAdulthoodTime() { return GROWING; }

    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == Items.CHICKEN; } //TODO: Make this some kind of meat

    @Override
    public int setSpeciesByBiome(Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET) {
            return this.rand.nextInt(SpeciesTarantula.values().length);
        }
        return SpeciesTarantula.getSpeciesByBiome(biome);
    }
    public String getSpeciesName() { return new TranslationTextComponent("item.untamedwilds.tarantula_" + this.getRawSpeciesName()).getUnformattedComponentText(); }
    public String getRawSpeciesName() { return SpeciesTarantula.values()[this.getSpecies()].name().toLowerCase(); }

    public enum SpeciesTarantula implements IStringSerializable {

        BLACK			(0, 0.8F, 3, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.JUNGLE),
        BLACK_AND_WHITE	(1, 0.8F, 2, BiomeDictionary.Type.SPARSE),
        COBALT			(2, 0.8F, 1, BiomeDictionary.Type.JUNGLE),
        KING			(3, 1.0F, 3, BiomeDictionary.Type.SAVANNA),
        RED_KNEE		(4, 0.8F, 2, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.MESA),
        REGALIS			(5, 0.8F, 1, BiomeDictionary.Type.JUNGLE),
        ROSE			(6, 0.8F, 3, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.MESA),
        TIGER			(7, 0.8F, 2, BiomeDictionary.Type.JUNGLE);

        public Float sizeMult;
        public int species;
        public int rolls;
        public BiomeDictionary.Type[] spawnBiomes;

        SpeciesTarantula(int species, Float smult, int rolls, BiomeDictionary.Type... biomes) {
            this.species = species;
            this.sizeMult = smult;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getName() {
            return I18n.format("entity.tarantula." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesTarantula> types = new ArrayList<>();
            if (biome.getDefaultTemperature() < 0.8F) {
                return 99;
            }
            for (SpeciesTarantula type : values()) {
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
