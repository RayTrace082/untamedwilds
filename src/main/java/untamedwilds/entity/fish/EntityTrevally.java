package untamedwilds.entity.fish;

import net.minecraft.client.resources.I18n;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.FishReturnToSchoolGoal;
import untamedwilds.entity.ai.FishWanderAsSchoolGoal;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityTrevally extends ComplexMobAquatic implements ISpecies, IPackEntity, INewSkins {

    private static final String BREEDING = "MID_SUMMER";
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();

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
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new FishWanderAsSchoolGoal(this));
        this.goalSelector.addGoal(4, new FishReturnToSchoolGoal(this));
    }

    public static void processSkins() {
        for (int i = 0; i < SpeciesTrevally.values().length; i++) {
            EntityUtils.buildSkinArrays("trevally", SpeciesTrevally.values()[i].name().toLowerCase(), i, TEXTURES_COMMON, TEXTURES_RARE);
        }
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
            if (player.isCreative() && itemstack.isEmpty()) {
                for (int i = 0; i < this.herd.creatureList.size(); ++i) {
                    ComplexMob creature = this.herd.creatureList.get(i);
                    creature.addPotionEffect(new EffectInstance(Effects.GLOWING, 80, 0));
                }
            }
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_trevally_" + getRawSpeciesName(this.getVariant()).toLowerCase(), itemstack);
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
            list.removeIf(input -> (input.getGender() == this.getGender()) || input.getGrowingAge() != 0);
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
        EntityUtils.dropEggs(this, "egg_trevally_" + getRawSpeciesName(this.getVariant()).toLowerCase(), 4);
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }
    public int getAdulthoodTime() { return GROWING; }
    public String getBreedingSeason() { return BREEDING; }

    public int getMaxPackSize() {
        return SpeciesTrevally.values()[this.getVariant()].schoolSize;
    }

    public boolean shouldLeavePack() {
        return this.rand.nextInt(120) == 0;
    }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (biomekey.equals(Biomes.COLD_OCEAN) || biomekey.equals(Biomes.DEEP_COLD_OCEAN) || biomekey.equals(Biomes.FROZEN_OCEAN) || biomekey.equals(Biomes.DEEP_FROZEN_OCEAN)) {
            return 99;
        }
        if (reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET || ConfigGamerules.randomSpecies.get()) {
            return this.rand.nextInt(EntityTrevally.SpeciesTrevally.values().length);
        }
        return EntityTrevally.SpeciesTrevally.getSpeciesByBiome(biome);
    }

    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.trevally_" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesTrevally.values()[i].name().toLowerCase(); }

    public enum SpeciesTrevally implements IStringSerializable {

        BIGEYE		(0, 0.8F, 8, 2, Biome.Category.OCEAN),
        BLUESPOTTED	(1, 0.8F, 8, 2, Biome.Category.OCEAN),
        GIANT    	(2, 1.5F, 1, 1, Biome.Category.OCEAN),
        GOLDEN		(3, 1F, 6, 2, Biome.Category.OCEAN),
        JACK    	(4, 1F, 8, 3, Biome.Category.OCEAN);

        public Float scale;
        public int species;
        public int rolls;
        public int schoolSize;
        public Biome.Category[] spawnBiomes;

        SpeciesTrevally(int species, Float scale, int schoolSize, int rolls, Biome.Category... biomes) {
            this.species = species;
            this.scale = scale;
            this.schoolSize = schoolSize;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.trevally." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<EntityTrevally.SpeciesTrevally> types = new ArrayList<>();

            for (EntityTrevally.SpeciesTrevally type : values()) {
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
