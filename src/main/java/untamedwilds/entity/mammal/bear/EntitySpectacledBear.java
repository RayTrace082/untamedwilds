package untamedwilds.entity.mammal.bear;

import net.minecraft.entity.AgeableEntity;
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
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.unique.BearRaidChestsGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModLootTables;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySpectacledBear extends AbstractBear {

    private static final ResourceLocation TEXTURE = new ResourceLocation("untamedwilds:textures/entity/bear/spectacled.png");
    private static final float SIZE = 0.8f;
    private static final String BREEDING = "LATE_WET";
    private static final int GESTATION = 7 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 7 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 1;

    public EntitySpectacledBear(EntityType<? extends AbstractBear> type, World worldIn) {
        super(type, worldIn);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.3D, false, 1));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > 7));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new BearRaidChestsGoal(this, 120));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof EntitySpectacledBear)));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) < 6));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 24.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .createMutableAttribute(Attributes.ARMOR, 4D);
    }

    /* Crepuscular: Active between 17:00 and 7:00 */
    public boolean isActive() {
        super.isActive();
        long time = this.world.getDayTime();
        return time < 1000 || time > 11000;
    }

    /* Breeding conditions for the Spectacled Bear are:
     * Temperate Biome (T between 0.2 and 0.7)
     * No other entities nearby */
    public boolean wantsToBreed() {
        super.wantsToBreed();
        if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this) && this.getHunger() >= 80) {
            if (ConfigGamerules.hardcoreBreeding.get()) {
                List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                float i = this.world.getBiome(this.getPosition()).getTemperature(this.getPosition());
                return i >= 0.2 && i <= 0.7 && list.size() < 3;
            }
            return true;
        }
        return false;
    }

    @Nullable
    public EntitySpectacledBear func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        return create_offspring(new EntitySpectacledBear(ModEntity.SPECTACLED_BEAR, this.world));
    }


    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTables.BEAR_LOOT_SPECTACLED;
    }
    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.APPLE; }
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    public ResourceLocation getTexture() { return TEXTURE; }
    protected int getOffspring() { return 1; }
}