package untamedwilds.entity.reptile;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartAvoidGoal;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartSwimGoal;
import untamedwilds.entity.ai.target.HuntMobTarget;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntitySnake extends ComplexMobTerrestrial implements ISpecies {
    //TODO: Move Snakes away from ComplexMobTerrestrial, they don't need Animations

    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();
    private static final String BREEDING = "LATE_SUMMER";
    public static Animation ANIMATION_TONGUE;

    public EntitySnake(EntityType<? extends ComplexMobTerrestrial> type, World worldIn) {
        super(type, worldIn);
        ANIMATION_TONGUE = Animation.create(10);
        this.ecoLevel = 2;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.entityCollisionReduction = 1F;
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(6.0D);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> this.getEcoLevel(input) > 8));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, false, input -> this.getEcoLevel(input) < 4));
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
            if (this.ticksExisted % 120 == 0) {
                this.setAnimation(ANIMATION_TONGUE);
            }
            this.setAngry(this.getAttackTarget() != null);
        }
        /*if (this.world.isRemote && this.isAngry() && this.aggroProgress < 40) {
            this.aggroProgress++;
        } else if (this.world.isRemote && !this.isAngry() && this.aggroProgress > 0) {
            this.aggroProgress--;
        }*/
    }

    /* Breeding conditions for the Snake are:
     * A nearby Snake of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && !this.isSleeping() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth()) {
            List<EntitySnake> list = this.world.getEntitiesWithinAABB(EntitySnake.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
            list.removeIf(input -> (input.getGender() == this.getGender()) || (input.getSpecies() != this.getSpecies()) || input.getGrowingAge() != 0);
            if (list.size() >= 1) {
                this.setGrowingAge(GROWING);
                list.get(0).setGrowingAge(GROWING);
                return true;
            }
        }
        return false;
    }

    /*protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            if (this.isRattler()) {
                return ModSounds.ENTITY_SNAKE_RATTLE;
            }
            return ModSounds.ENTITY_SNAKE_HISS;
        }
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.ENTITY_SNAKE_HISS;
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_SNAKE_HISS;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return ModLootTable.FROG_LOOT;
    }*/

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) {
        //ItemEntity entityitem = this.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":egg_tarantula_" + this.getRawSpeciesName().toLowerCase()))), 0.2F);
        //entityitem.getItem().setCount(1 + this.rand.nextInt(3));
        return null;
    }

    public String getBreedingSeason() {
        return BREEDING;
    }
    public int getAdulthoodTime() { return GROWING; }

    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == Items.RABBIT; } //TODO: Rat meat from Rats mod?

    @Override
    public int setSpeciesByBiome(Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET) {
            return this.rand.nextInt(EntitySnake.SpeciesSnake.values().length);
        }
        return EntitySnake.SpeciesSnake.getSpeciesByBiome(biome);
    }

    public String getSpeciesName() { return new TranslationTextComponent("item.untamedwilds.snake_" + this.getRawSpeciesName()).getUnformattedComponentText(); }
    public String getRawSpeciesName() { return EntitySnake.SpeciesSnake.values()[this.getSpecies()].name().toLowerCase(); }

    public boolean isRattler() { return SpeciesSnake.values()[this.getSpecies()].isRattler(); }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_TONGUE};
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag && SpeciesSnake.values()[this.getSpecies()].getVenomTier() > 0) {
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 200, SpeciesSnake.values()[this.getSpecies()].getVenomTier() - 1));
            }
            return true;
        }
        return flag;
    }

    public enum SpeciesSnake implements IStringSerializable {

        ADDER			(0, 0.7F,	1,	false, 3, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS),
        BALL_PYTHON		(1, 1.0F,	0,	false, 2, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.SPARSE),
        BLACK_MAMBA		(2, 1.3F,	2,	false, 2, BiomeDictionary.Type.SAVANNA),
        CARPET_PYTHON	(3, 1.1F,	0,	false, 1, BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.LUSH),
        CORAL			(4, 0.7F,	2,	false, 3, BiomeDictionary.Type.MESA, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DRY),
        CORAL_BLUE		(5, 1.1F,	1,	false, 1, BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.LUSH),
        CORN			(6, 0.9F,	0,	false, 3, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.PLAINS),
        EMERALD			(7, 0.9F,	0,	false, 2, BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.LUSH),
        GRASS_SNAKE		(8, 0.8F,	0,	false, 3, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST),
        GRAY_KINGSNAKE	(9, 0.7F,	0,	false, 3, BiomeDictionary.Type.MESA),
        RATTLESNAKE		(10,0.7F,	1,	true,  2, BiomeDictionary.Type.MESA, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SPARSE),
        SWAMP_MOCCASIN	(11,0.7F,	1,	false, 2, BiomeDictionary.Type.SWAMP);


        public Float scale;
        public int species;
        public int rolls;
        public int venomTier;
        public boolean rattler;
        public BiomeDictionary.Type[] spawnBiomes;

        SpeciesSnake(int species, Float scale, int venomTier, boolean rattler, int rolls, BiomeDictionary.Type... biomes) {
            this.species = species;
            this.scale = scale;
            this.rolls = rolls;
            this.venomTier = venomTier;
            this.rattler = rattler;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getName() {
            return I18n.format("entity.tarantula." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesSnake> types = new ArrayList<>();
            if (biome.getDefaultTemperature() < 0.8F) {
                return 99;
            }
            for (SpeciesSnake type : values()) {
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

        public int getVenomTier() {
            return venomTier;
        }

        public boolean isRattler() {
            return this.rattler;
        }
    }
}