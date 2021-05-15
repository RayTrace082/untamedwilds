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
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.FishBreachGoal;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityArowana extends ComplexMobAquatic implements ISpecies, INewSkins {

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

    public static void processSkins() {
        for (int i = 0; i < SpeciesArowana.values().length; i++) {
            EntityUtils.buildSkinArrays("arowana", SpeciesArowana.values()[i].name().toLowerCase(), i, EntityArowana.TEXTURES_COMMON, EntityArowana.TEXTURES_RARE);
        }
        UntamedWilds.LOGGER.info(EntityArowana.TEXTURES_COMMON);
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

        // Coerces the Arowana to stay at the surface
        if (this.getAttackTarget() == null && this.isInWater() && this.ticksExisted % 10 == 0) {
            if (world.hasWater(this.getPosition().up().up())) {
                this.setMotion(this.getMotion().add(0, 0.1F, 0));
            }
        }
        super.livingTick();
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_arowana_" + getRawSpeciesName(this.getVariant()).toLowerCase(), itemstack);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }
        return super.func_230254_b_(player, hand);
    }

    /* Breeding conditions for the Arowana are:
     * A nearby Arowana of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityArowana> list = this.world.getEntitiesWithinAABB(EntityArowana.class, this.getBoundingBox().grow(12.0D, 8.0D, 12.0D));
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
        EntityUtils.dropEggs(this, "egg_arowana_" + getRawSpeciesName(this.getVariant()).toLowerCase(), 4);
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
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

    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.arowana_" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesArowana.values()[i].name().toLowerCase(); }
    public ResourceLocation getTexture() {
        if (this.getSkin() > 99) {
            return EntityArowana.TEXTURES_RARE.get(this.getVariant()).get(this.getSkin() - 100);
        }
        return EntityArowana.TEXTURES_COMMON.get(this.getVariant()).get(this.getSkin());
    }

    public enum SpeciesArowana implements IStringSerializable {

        BLACK		(0, 0.8F, 2, Biome.Category.JUNGLE),
        GREEN		(1, 1.1F, 2, Biome.Category.SWAMP, Biome.Category.JUNGLE),
        JARDINI		(2, 1F, 1, Biome.Category.SWAMP),
        SILVER    	(3, 1F, 2, Biome.Category.JUNGLE);

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
