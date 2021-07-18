package untamedwilds.entity.reptile;

import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.item.ItemEntity;
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
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartMeleeAttackGoal;
import untamedwilds.entity.ai.SmartSwimGoal;
import untamedwilds.entity.ai.SmartWanderGoal;
import untamedwilds.entity.ai.unique.TortoiseHideInShellGoal;
import untamedwilds.init.ModItems;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityTortoise extends ComplexMobTerrestrial implements ISpecies, INewSkins {

    private static final String BREEDING = "EARLY_SUMMER";
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();

    public EntityTortoise(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 1;
        this.ticksToSit = 20;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.1D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 6.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2D)
                .createMutableAttribute(Attributes.ARMOR, 10D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new TortoiseHideInShellGoal<>(this, LivingEntity.class, 7, input -> getEcoLevel(input) > 4));
        this.goalSelector.addGoal(3, new SmartWanderGoal(this, 1.0D, 400, 0,true));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static void processSkins() {
        for (int i = 0; i < SpeciesTortoise.values().length; i++) {
            EntityUtils.buildSkinArrays("tortoise", SpeciesTortoise.values()[i].name().toLowerCase(), i, TEXTURES_COMMON, TEXTURES_RARE);
        }
    }

    public void onDeath(DamageSource cause) {
        if (cause == DamageSource.ANVIL && !this.isChild()) {
            // Advancement Trigger: "Unethical Soup"
            ItemEntity entityitem = this.entityDropItem(new ItemStack(ModItems.FOOD_TURTLE_SOUP.get()), 0.2F);
            if (entityitem != null) {
                entityitem.getItem().setCount(1);
            }
        }
        super.onDeath(cause);
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

    /* Breeding conditions for the tortoise are:
     * A nearby tortoise of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityTortoise> list = this.world.getEntitiesWithinAABB(EntityTortoise.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
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
        EntityUtils.dropEggs(this, "egg_tortoise_" + getRawSpeciesName(this.getVariant()).toLowerCase(), 5);
        return null;
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);

        if (itemstack.isEmpty() && this.isAlive()) {
            EntityUtils.turnEntityIntoItem(this, "tortoise_" + getRawSpeciesName(this.getVariant()).toLowerCase());
            return ActionResultType.func_233537_a_(this.world.isRemote);
        }
        return super.func_230254_b_(player, hand);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source != DamageSource.FALL && this.sitProgress > 0) {
            amount = amount * 0.2F;
        }
        return super.attackEntityFrom(source, amount);
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

    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == Items.COD; }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomeKey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || isArtificialSpawnReason(reason)) {
            return this.rand.nextInt(SpeciesTortoise.values().length);
        }
        return SpeciesTortoise.getSpeciesByBiome(biome);
    }

    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.tortoise_" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesTortoise.values()[i].name().toLowerCase(); }

    public enum SpeciesTortoise implements IStringSerializable {

        ASIAN_BOX	(0, 0.8F, 2, Biome.Category.FOREST, Biome.Category.JUNGLE),
        GOPHER	    (1, 1.0F, 3, Biome.Category.FOREST),
        LEOPARD		(2, 1.0F, 2, Biome.Category.SAVANNA, Biome.Category.MESA, Biome.Category.PLAINS),
        MARGINATED	(3, 0.9F, 2, Biome.Category.FOREST, Biome.Category.PLAINS),
        STAR		(4, 0.8F, 1, Biome.Category.PLAINS, Biome.Category.MESA),
        SULCATA		(5, 1.2F, 2, Biome.Category.SAVANNA);

        public Float scale;
        public int species;
        public int rolls;
        public Biome.Category[] spawnBiomes;

        SpeciesTortoise(int species, Float scale, int rolls, Biome.Category... biomes) {
            this.species = species;
            this.scale = scale;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.tortoise." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesTortoise> types = new ArrayList<>();
            /*if (biome.getDefaultTemperature() < 0.8F) {
                return 99;
            }*/
            for (SpeciesTortoise type : values()) {
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
