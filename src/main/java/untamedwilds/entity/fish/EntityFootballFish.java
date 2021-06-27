package untamedwilds.entity.fish;

import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.SmartMeleeAttackGoal;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityFootballFish extends ComplexMobAquatic implements ISpecies, INewSkins, INeedsPostUpdate {

    private static final String BREEDING = "EARLY_AUTUMN";
    private static final int GROWING = 12 * ConfigGamerules.cycleLength.get();

    public EntityFootballFish(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 2;
    }

    protected void registerData() {
        super.registerData();
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.42D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 8.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 8.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .createMutableAttribute(Attributes.ARMOR, 2D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SmartMeleeAttackGoal(this, 1.8D, false, 2));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new SwimGoal(this, 4));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new HuntMobTarget<>(this, LivingEntity.class, true, false, input -> getEcoLevel(input) < 5));
    }

    public static void processSkins() {
        for (int i = 0; i < SpeciesFootballFish.values().length; i++) {
            EntityUtils.buildSkinArrays("football_fish", SpeciesFootballFish.values()[i].name().toLowerCase(), i, TEXTURES_COMMON, TEXTURES_RARE);
        }
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
    }

    /* Breeding conditions for the Football Fish are:
     * Be really deep in the ocean (16 blocks at least), and that's it */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
            BlockPos.Mutable blockPos = new BlockPos.Mutable();
            for (int i = 0; i <= 16; i++) {
                BlockState state = world.getBlockState(blockPos.setPos(this.getPosX(), this.getPosY() + i, this.getPosZ()));
                if (!state.getFluidState().isTagged(FluidTags.WATER)) {
                    return false;
                }
            }
            this.setGrowingAge(GROWING);
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        EntityUtils.dropEggs(this, "egg_football_fish_" + getRawSpeciesName(this.getVariant()).toLowerCase(), 4);
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }
    public int getAdulthoodTime() { return GROWING; }
    public String getBreedingSeason() { return BREEDING; }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (biomekey.equals(Biomes.DEEP_LUKEWARM_OCEAN) || biomekey.equals(Biomes.DEEP_OCEAN) || biomekey.equals(Biomes.DEEP_COLD_OCEAN)) {
            return this.rand.nextInt(SpeciesFootballFish.values().length);
        }
        if (isArtificialSpawnReason(reason)) {
            return this.rand.nextInt(SpeciesFootballFish.values().length);
        }
        return 99;
    }

    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.football_fish_" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesFootballFish.values()[i].name().toLowerCase(); }

    @Override
    public void updateAttributes() {
        // All Football Fish are female, for the purpose of the mod, males do not exist
        this.setGender(1);
    }

    public enum SpeciesFootballFish implements IStringSerializable {

        ATLANTIC    	(0, 1F, 1, Biome.Category.OCEAN);

        public Float scale;
        public int species;
        public int rolls;
        public Biome.Category[] spawnBiomes;

        SpeciesFootballFish(int species, Float scale, int rolls, Biome.Category... biomes) {
            this.species = species;
            this.scale = scale;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.football_fish." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesFootballFish> types = new ArrayList<>();

            for (SpeciesFootballFish type : values()) {
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
