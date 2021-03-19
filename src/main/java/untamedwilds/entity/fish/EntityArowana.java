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
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.FishBreachGoal;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

;

public class EntityArowana extends ComplexMobAquatic implements ISpecies {

    private static final String BREEDING = "MID_SUMMER";
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();

    public EntityArowana(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 3;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.8D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 6.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new SwimGoal(this));
        this.goalSelector.addGoal(4, new FishBreachGoal(this, 400, true));
    }

    public void livingTick() {
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

        if (this.getAttackTarget() == null && this.isInWater() && this.ticksExisted % 10 == 0) {
            //double eyeHeight = (double) this.getEyeHeight() + 1.6F;
            //((ServerWorld)this.world).spawnParticle(ParticleTypes.FLAME, this.getPosX(), this.getPosY() + eyeHeight, this.getPosZ(), 1, 0, 0, 0, 0);
            if (world.hasWater(this.getPosition().up().up())) {
                UntamedWilds.LOGGER.info("JUMPING");
                this.setMotion(this.getMotion().add(0, 0.1F, 0)); // Coerces the Arowana to stay at the surface
            }
        }
        super.livingTick();
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_arowana_" + this.getRawSpeciesName().toLowerCase(), itemstack);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }
        return super.func_230254_b_(player, hand);
    }

    /* Breeding conditions for the Trevally are:
     * A nearby Trevally of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth()) {
            List<EntityArowana> list = this.world.getEntitiesWithinAABB(EntityArowana.class, this.getBoundingBox().grow(12.0D, 8.0D, 12.0D));
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
        EntityUtils.dropEggs(this, "egg_arowana_" + this.getRawSpeciesName().toLowerCase(), 4);
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
        if (reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET || ConfigGamerules.randomSpecies.get()) {
            return this.rand.nextInt(EntityArowana.SpeciesArowana.values().length);
        }
        return EntityArowana.SpeciesArowana.getSpeciesByBiome(biome);
    }

    public String getSpeciesName() { return new TranslationTextComponent("entity.untamedwilds.arowana_" + this.getRawSpeciesName()).getString(); }
    public String getRawSpeciesName() { return EntityArowana.SpeciesArowana.values()[this.getVariant()].name().toLowerCase(); }

    public enum SpeciesArowana implements IStringSerializable {

        BLACK		(0, 0.8F, 10, Biome.Category.JUNGLE),
        DRAGON	(1, 1.1F, 1, Biome.Category.SWAMP, Biome.Category.JUNGLE),
        GOLDEN    	(2, 1.1F,  1, Biome.Category.SWAMP, Biome.Category.JUNGLE),
        GREEN		(3, 1.1F, 8, Biome.Category.SWAMP, Biome.Category.JUNGLE),
        JARDINI		(3, 1F, 6, Biome.Category.SWAMP),
        SILVER    	(4, 1F, 10, Biome.Category.JUNGLE);

        public Float scale;
        public int species;
        public int rolls;
        public Biome.Category[] spawnBiomes;

        SpeciesArowana(int species, Float scale, int rolls, Biome.Category... biomes) {
            this.species = species;
            this.scale = scale;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.arowana." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<EntityArowana.SpeciesArowana> types = new ArrayList<>();

            for (EntityArowana.SpeciesArowana type : values()) {
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
