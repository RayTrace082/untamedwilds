package untamedwilds.entity.mammal.bear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ISkins;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.unique.BearRaidChestsGoal;
import untamedwilds.init.ModLootTables;
import untamedwilds.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class EntityBlackBear extends AbstractBear implements ISkins {

    public static List<ResourceLocation> TEXTURES = new ArrayList<>();
    public static final int SKIN_NUMBER = 5;
    private static final float SIZE = 0.8f;
    private static final String BREEDING = "EARLY_SUMMER";
    private static final int GESTATION = 8 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 8 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 5;

    public EntityBlackBear(EntityType<? extends AbstractBear> type, World worldIn) {
        super(type, worldIn);
    }

    public static void registerTextures(int count) {
        for(int i = 1; i < count + 1; i++) {
            TEXTURES.add(new ResourceLocation(UntamedWilds.MOD_ID, String.format("textures/entity/bear/black_%d.png", i)));
        }
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.3D, false, 1));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > 6));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new BearRaidChestsGoal(this, 120));
        this.goalSelector.addGoal(6, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(7, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof EntityBlackBear)));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) <= 5));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 24D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .createMutableAttribute(Attributes.ARMOR, 4D);
    }

    /* Breeding conditions for the Black Bear are:
     * Temperate Biome (T between 0.2 and 0.7)
     * No other entities nearby */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this) && this.getHunger() >= 80) {
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

    protected ActivityType getActivityType() {
        return ActivityType.CREPUSCULAR;
    }
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTables.BEAR_LOOT_BLACK;
    }
    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.SWEET_BERRIES; }
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    public ResourceLocation getTexture() { return TEXTURES.get(this.getVariant()); }
    public int getSkinNumber() { return SKIN_NUMBER; }
    protected int getOffspring() { return 2; }
}