package untamedwilds.entity.mammal.bigcat;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModLootTables;

import java.util.List;

public class PumaBigCat extends AbstractBigCat {

    private static final ResourceLocation TEXTURE = new ResourceLocation("untamedwilds:textures/entity/big_cat/puma.png");
    private static final float SIZE = 0.7f;
    private static final String BREEDING = "ALL";
    private static final int GESTATION = 3 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 9 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 8;

    public PumaBigCat(EntityType<? extends AbstractBigCat> type, World worldIn) {
        super(type, worldIn);
        this.ecoLevel = 6;
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, true));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.3D, false, 1));
        this.goalSelector.addGoal(3, new SmartFollowOwnerGoal(this, 2.3D, 16.0F, 3.0F));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> this.getEcoLevel(input) > 6));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D, true));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof PumaBigCat)));
        this.targetSelector.addGoal(2, new SmartOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, false, input -> this.getEcoLevel(input) < 6));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.16D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .createMutableAttribute(Attributes.ARMOR, 0D);
    }

    /* Crepuscular: Active between 10:00 and 1:00 */
    public boolean isActive() {
        super.isActive();
        long time = this.world.getDayTime();
        return time > 4000 && time < 19000;
    }

    /* Breeding conditions for the Mountain Lion are:
     * Temperate Biome (T between 0.2 and 0.7)
     * No other entities nearby */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth() && this.getHunger() >= 80) {
                if (ConfigGamerules.hardcoreBreeding.get()) {
                    List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                    float i = this.world.getBiome(this.getPosition()).getTemperature(this.getPosition());
                    return i >= 0.2 && i <= 0.7 && list.size() < 3;
                }
                return true;
            }
        }
        return false;
    }

    public PumaBigCat func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        PumaBigCat bear = new PumaBigCat(ModEntity.PUMA, this.world);
        bear.setSpecies(this.getSpecies());
        bear.setGender(this.rand.nextInt(2));
        bear.setMobSize(this.rand.nextFloat());
        return bear;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_OCELOT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_OCELOT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_OCELOT_DEATH;
    }

    protected ResourceLocation getLootTable() {
        return ModLootTables.BIGCAT_LOOT_PUMA;
    }
    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.RABBIT; }
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    public ResourceLocation getTexture() { return TEXTURE; }
    protected int getOffspring() { return 3; }
}
