package untamedwilds.entity.mammal;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.*;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModItems;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityAardvark extends ComplexMobTerrestrial implements ISpecies {

    private static final float SIZE = 1.0f;
    private static final String BREEDING = "LATE_SPRING";
    private static final int GESTATION = 7 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 5;

    public static Animation WORK_DIG;

    public EntityAardvark(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.turn_speed = 0.8F;
        WORK_DIG = Animation.create(76);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, 100, false, true));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.6D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> this.getEcoLevel(input) > 4));
        this.goalSelector.addGoal(3, new GotoSleepGoal(this, 1));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, 120, 0, false));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.18D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 24.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 8.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .createMutableAttribute(Attributes.ARMOR, 0D);
    }

    /* Nocturnal: Active between 19:00 and 10:00 */
    public boolean isActive() {
        super.isActive();
        long time = this.world.getDayTime();
        return time > 13000 || time < 4000;
    }

    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            return !this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this) && this.getHunger() >= 80;
        }
        return false;
    }

    @Override
    public void livingTick() {
        if (!this.world.isRemote) {
            this.setAngry(this.getAttackTarget() != null);
            if (this.ticksExisted % 600 == 0) {
                if (this.wantsToBreed()) {
                    this.setInLove(null);
                }
            }
            if (this.world.getGameTime() % 1000 == 0) {
                this.addHunger(-10);
                if (!this.isStarving()) {
                    this.heal(1.0F);
                }
            }
            int i = this.rand.nextInt(3000);
            if (i == 13 && !this.isInWater() && this.isNotMoving() && this.canMove() && this.getAnimation() == NO_ANIMATION) {
                this.setSitting(true);
            }
            if (i == 14 && this.isSitting()) {
                this.setSitting(false);
            }
            if (i > 2980 && !this.isInWater() && this.getHunger() < 60 && this.canMove() && this.getAnimation() == NO_ANIMATION) {
                if (this.world.getBlockState(this.getPosition().down()).getHarvestTool() == ToolType.SHOVEL) {
                    this.setAnimation(WORK_DIG);
                }
            }
            if (this.getAnimation() == WORK_DIG && this.getAnimationTick() % 8 == 0) {
                ((ServerWorld)this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, this.world.getBlockState(this.getPosition().down())), this.getPosX(), this.getPosY(), this.getPosZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);
                this.playSound(SoundEvents.ITEM_SHOVEL_FLATTEN, 0.8F, 0.6F);
                if (this.getAnimationTick() == 64 && this.rand.nextInt(10) == 0) {
                    this.entityDropItem(new ItemStack(ModItems.VEGETABLE_AARDVARK_CUCUMBER.get()), 0.2F);
                }
            }
        }
        super.livingTick();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_PIG_DEATH; }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, WORK_DIG};
    }

    @Nullable
    public EntityAardvark func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        return create_offspring(new EntityAardvark(ModEntity.AARDVARK, this.world));
    }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET) {
            return this.rand.nextInt(EntityAardvark.SpeciesAardvark.values().length);
        }
        return EntityAardvark.SpeciesAardvark.getSpeciesByBiome(biome);
    }


    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.aardvark_" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesAardvark.values()[i].name().toLowerCase(); }

    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == ModItems.VEGETABLE_AARDVARK_CUCUMBER.get(); }
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    protected int getOffspring() { return 1; }

    // Species available, referenced to properly distribute Aardvarks in the world. if any is ever added
    public enum SpeciesAardvark implements IStringSerializable {

        COMMON		(0, 1, Biome.Category.SAVANNA);

        public EntityType<? extends EntityAardvark> type;
        public int rarity;
        public Biome.Category[] spawnBiomes;
        public int species;

        SpeciesAardvark(int species, int rolls, Biome.Category... biomes) {
            this.species = species;
            this.rarity = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        @Override
        public String getString() {
            return "why would you do this?";
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<EntityAardvark.SpeciesAardvark> types = new ArrayList<>();
            for (EntityAardvark.SpeciesAardvark type : values()) {
                for(Biome.Category biomeTypes : type.spawnBiomes) {
                    if(biome.getCategory() == biomeTypes){
                        for (int i=0; i < type.rarity; i++) {
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
