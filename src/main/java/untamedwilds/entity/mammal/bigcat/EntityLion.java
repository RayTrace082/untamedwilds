package untamedwilds.entity.mammal.bigcat;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.IPackEntity;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntPackMobTarget;
import untamedwilds.entity.ai.target.HurtPackByTargetGoal;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModLootTables;
import untamedwilds.util.EntityUtils;

import java.util.List;

public class EntityLion extends AbstractBigCat implements IPackEntity {

    private static final ResourceLocation TEXTURE = new ResourceLocation("untamedwilds:textures/entity/big_cat/lion_male.png");
    private static final ResourceLocation TEXTURE_FEMALE = new ResourceLocation("untamedwilds:textures/entity/big_cat/lion_female.png");
    private static final float SIZE = 1F;
    private static final String BREEDING = "ALL";
    private static final int GESTATION = 5 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 11 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 3;

    public EntityLion(EntityType<? extends AbstractBigCat> type, World worldIn) {
        super(type, worldIn);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, true));
        this.goalSelector.addGoal(2, new MeleeAttackCircleHerd(this, 2.3D, true, 1));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > 9));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtPackByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof EntityLion)));
        this.targetSelector.addGoal(3, new HuntPackMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) < 8));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 8.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.16D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 40.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .createMutableAttribute(Attributes.ARMOR, 0D);
    }

    /* Breeding conditions for the Lion are:
     * Warm Biome (T higher than 0.6)
     * No more than 3 other entities nearby */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this) && this.getHunger() >= 80) {
                if (ConfigGamerules.hardcoreBreeding.get()) {
                    List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                    float i = this.world.getBiome(this.getPosition()).getTemperature(this.getPosition());
                    return i >= 0.6 && list.size() < 6;
                }
                return true;
            }
        }
        return false;
    }

    public void livingTick() {
        if (this.herd == null) {
            IPackEntity.initPack(this);
        }
        else {
            this.herd.tick();
        }
        super.livingTick();
    }

    public EntityLion func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        EntityLion bear = new EntityLion(ModEntity.LION, this.world);
        bear.setVariant(this.getVariant());
        bear.setGender(this.rand.nextInt(2));
        bear.setMobSize(this.rand.nextFloat());
        return bear;
    }

    protected activityType getActivityType() {
        return activityType.DIURNAL;
    }
    protected ResourceLocation getLootTable() {
        return ModLootTables.BIGCAT_LOOT_LION;
    }
    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.BEEF; }
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    public ResourceLocation getTexture() {
        return this.isMale() ? TEXTURE : TEXTURE_FEMALE;
    }
    protected int getOffspring() { return 2; }
    public int getMaxPackSize() { return 8; }
}
