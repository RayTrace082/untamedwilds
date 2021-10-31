package untamedwilds.entity.fish;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.MeleeAttackCircle;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.target.HuntWeakerTarget;
import untamedwilds.entity.ai.unique.SharkSwimmingGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityShark extends ComplexMobAquatic implements ISpecies, IAnimatedEntity, INeedsPostUpdate, INewSkins {

    public static Animation ATTACK_THRASH;
    private int animationTick;
    private Animation currentAnimation;

    public EntityShark(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        ATTACK_THRASH = Animation.create(15);
        this.experienceValue = 10;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 12.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.8D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 50.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackCircle(this, 2.3D, false, 2));
        this.goalSelector.addGoal(3, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new SharkSwimmingGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntWeakerTarget<>(this, LivingEntity.class, true));
    }

    public void livingTick() {
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.world.isRemote) {
            this.setAngry(this.getAttackTarget() != null);
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.setGrowingAge(this.getPregnancyTime());
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }
        super.livingTick();
    }

    /* Breeding conditions for the Shark are:
     * A nearby Shark of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityShark> list = this.world.getEntitiesWithinAABB(EntityShark.class, this.getBoundingBox().grow(12.0D, 8.0D, 12.0D));
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
        return create_offspring(new EntityShark(ModEntity.SHARK, this.world));
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (reason == SpawnReason.SPAWN_EGG || ConfigGamerules.randomSpecies.get()) {
            return this.rand.nextInt(EntityShark.SpeciesShark.values().length);
        }
        // TODO: Needed because I can't parse individual biomes from .json
        return EntityShark.SpeciesShark.getSpeciesByBiome(biomekey);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            this.setAnimation(ATTACK_THRASH);
        }
        return flag;
    }

    public int getAnimationTick() { return animationTick; }
    public void setAnimationTick(int tick) { animationTick = tick; }
    public Animation getAnimation() { return currentAnimation; }
    public void setAnimation(Animation animation) { currentAnimation = animation; }
    public Animation[] getAnimations() { return new Animation[]{NO_ANIMATION, ATTACK_THRASH}; }

    // Flags Parameters
    public boolean hasShortFins() {
        return getEntityData(this.getType()).getFlags(this.getVariant(), "shortFins") == 1;
    }
    public boolean isBottomDweller() { return getEntityData(this.getType()).getFlags(this.getVariant(), "bottomDweller") == 1; }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getHealth());
        this.setHealth(this.getMaxHealth());
    }

    public enum SpeciesShark implements IStringSerializable {

        BIGEYE	    (0, 1, Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN),
        BLUNTNOSE	(1, 2, Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN),
        BULL    	(2, 4, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN),
        GOBLIN  	(3, 1, Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN),
        GREAT_WHITE	(4, 2, Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN),
        GREENLAND	(5, 1, Biomes.DEEP_COLD_OCEAN, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN),
        HAMMERHEAD	(6, 2, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN),
        LEMON   	(7, 1, Biomes.WARM_OCEAN),
        MAKO    	(8, 4, Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_WARM_OCEAN),
        TIGER	    (9, 2, Biomes.WARM_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN);

        public int species;
        public int rolls;
        public RegistryKey<Biome>[] spawnBiomes;

        @SafeVarargs
        SpeciesShark(int species, int rolls, RegistryKey<Biome>... biomes) {
            this.species = species;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.shark." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(RegistryKey<Biome> biomekey) {
            List<EntityShark.SpeciesShark> types = new ArrayList<>();

            for (EntityShark.SpeciesShark type : values()) {
                for(RegistryKey<Biome> biomeTypes : type.spawnBiomes) {
                    if(biomekey.equals(biomeTypes)){
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
