package untamedwilds.entity.amphibian;

import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.init.ModItems;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityNewt extends ComplexMobAmphibious implements ISpecies, INewSkins {

    private static final String BREEDING = "EARLY_SUMMER";
    private static final int GROWING = 3 * ConfigGamerules.cycleLength.get();
    public int swimProgress;

    public EntityNewt(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new ComplexMobAquatic.MoveHelperController(this, 0.8F);
        this.swimSpeedMult = 3;
        this.buoyancy = 0.992F;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.14D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 8.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 2.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, (float)this.getAttributeValue(Attributes.FOLLOW_RANGE), 1D, 1.3D, input -> getEcoLevel(input) > 3));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 120));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, 120, 0, false));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static void processSkins() {
        for (int i = 0; i < SpeciesNewt.values().length; i++) {
            EntityUtils.buildSkinArrays("newt", SpeciesNewt.values()[i].name().toLowerCase(), i, TEXTURES_COMMON, TEXTURES_RARE);
        }
    }

    public boolean wantsToLeaveWater() { return !SpeciesNewt.values()[this.getVariant()].isAquatic; }

    public boolean wantsToEnterWater() { return SpeciesNewt.values()[this.getVariant()].isAquatic; }

    public boolean isPushedByWater() {
        return false;
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
        else {
            if (this.isInWater() && !this.isOnGround() && this.swimProgress < 20) {
                this.swimProgress++;
            } else if ((!this.isInWater() || this.isOnGround()) && this.swimProgress > 0) {
                this.swimProgress--;
            }
        }
    }

    /* Breeding conditions for the giant salamander are:
     * A nearby giant salamander of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityNewt> list = this.world.getEntitiesWithinAABB(EntityNewt.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setGrowingAge(GROWING);
                    list.get(0).setGrowingAge(GROWING);
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        EntityUtils.dropEggs(this, "egg_newt_" + getRawSpeciesName(this.getVariant()).toLowerCase(), 5);
        return null;
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_newt_" + getRawSpeciesName(this.getVariant()).toLowerCase(), itemstack);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }
        return super.func_230254_b_(player, hand);
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

    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == ModItems.MEAT_TURTLE_RAW.get(); }

    public float getModelScale() { return 0.8F; }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomeKey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || isArtificialSpawnReason(reason)) {
            return this.rand.nextInt(SpeciesNewt.values().length);
        }
        return SpeciesNewt.getSpeciesByBiome(biome);
    }

    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.newt" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesNewt.values()[i].name().toLowerCase(); }

    public enum SpeciesNewt implements IStringSerializable {

        COASTAL 		(0, 1.3F, 5, false, Biome.Category.FOREST, Biome.Category.TAIGA),
        CRESTED	        (1, 1.0F, 2, true, Biome.Category.RIVER, Biome.Category.EXTREME_HILLS),
        EASTERN			(2, 1.0F, 3, true, Biome.Category.SWAMP, Biome.Category.RIVER, Biome.Category.EXTREME_HILLS),
        FIRE_SALAMANDER	(3, 1.2F, 5, false, Biome.Category.FOREST, Biome.Category.TAIGA, Biome.Category.RIVER),
        RIBBED			(4, 0.9F, 3, true, Biome.Category.SWAMP, Biome.Category.RIVER, Biome.Category.EXTREME_HILLS),
        TIGER			(5, 1.2F, 5, false, Biome.Category.FOREST, Biome.Category.RIVER);

        public Float scale;
        public int species;
        public int rolls;
        public boolean isAquatic;
        public Biome.Category[] spawnBiomes;

        SpeciesNewt(int species, Float scale, int rolls, boolean isAquatic, Biome.Category... biomes) {
            this.species = species;
            this.scale = scale;
            this.rolls = rolls;
            this.isAquatic = isAquatic;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.newt." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesNewt> types = new ArrayList<>();
            for (SpeciesNewt type : values()) {
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
