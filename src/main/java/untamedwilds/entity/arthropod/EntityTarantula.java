package untamedwilds.entity.arthropod;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartAvoidGoal;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartSwimGoal;
import untamedwilds.entity.ai.target.DontThreadOnMeTarget;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityTarantula extends ComplexMob implements ISpecies {

    private static final String BREEDING = "EARLY_SUMMER";
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();

    public int aggroProgress;
    //public int webProgress;

    public EntityTarantula(EntityType<? extends EntityTarantula> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 2.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .createMutableAttribute(Attributes.ARMOR, 0D);
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
        this.targetSelector.addGoal(3, new DontThreadOnMeTarget<>(this, LivingEntity.class, true));
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
            this.setAngry(this.getAttackTarget() != null);
        }
        if (this.world.isRemote && this.isAngry() && this.aggroProgress < 40) {
            this.aggroProgress++;
        } else if (this.world.isRemote && !this.isAngry() && this.aggroProgress > 0) {
            this.aggroProgress--;
        }
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    /* Breeding conditions for the Tarantula are:
     * A nearby Tarantula of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && !this.isSleeping() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth()) {
            List<EntityTarantula> list = this.world.getEntitiesWithinAABB(EntityTarantula.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
            list.removeIf(input -> (input.getGender() == this.getGender()) || (input.getVariant() != this.getVariant()) || input.getGrowingAge() != 0);
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
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        EntityUtils.dropEggs(this, "egg_tarantula_" + getRawSpeciesName(this.getVariant()).toLowerCase(), 4);
        return null;
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);

        if (itemstack.getItem() == Items.GLASS_BOTTLE && this.isAlive()) {
            EntityUtils.turnEntityIntoItem(this, "bottle_tarantula_" + getRawSpeciesName(this.getVariant()).toLowerCase());
            itemstack.shrink(1);
            return ActionResultType.func_233537_a_(this.world.isRemote);
        }
        return super.func_230254_b_(player, hand);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 80, 0));
            }
            return true;
        }
        return false;
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

    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == Items.CHICKEN; }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET) {
            return this.rand.nextInt(SpeciesTarantula.values().length);
        }
        return SpeciesTarantula.getSpeciesByBiome(biome);
    }

    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.tarantula_" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesTarantula.values()[i].name().toLowerCase(); }

    public enum SpeciesTarantula implements IStringSerializable {

        BLACK			(0, 0.8F, 3, Biome.Category.SAVANNA, Biome.Category.JUNGLE),
        BLACK_AND_WHITE	(1, 0.8F, 2, Biome.Category.MESA),
        COBALT			(2, 0.8F, 1, Biome.Category.JUNGLE),
        KING			(3, 1.0F, 3, Biome.Category.SAVANNA),
        RED_KNEE		(4, 0.8F, 2, Biome.Category.MESA),
        REGALIS			(5, 0.8F, 1, Biome.Category.JUNGLE),
        ROSE			(6, 0.8F, 3, Biome.Category.MESA),
        TIGER			(7, 0.8F, 2, Biome.Category.JUNGLE);

        public Float scale;
        public int species;
        public int rolls;
        public Biome.Category[] spawnBiomes;

        SpeciesTarantula(int species, Float scale, int rolls, Biome.Category... biomes) {
            this.species = species;
            this.scale = scale;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.tarantula." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesTarantula> types = new ArrayList<>();
            for (SpeciesTarantula type : values()) {
                for(Biome.Category biomeTypes : type.spawnBiomes) {
                    if(biome.getCategory() == biomeTypes){
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
