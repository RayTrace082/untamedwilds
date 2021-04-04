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
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModLootTables;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySunBear extends AbstractBear {

    private static final ResourceLocation TEXTURE = new ResourceLocation("untamedwilds:textures/entity/bear/sun.png");
    private static final float SIZE = 0.6f;
    private static final String BREEDING = "MID_WET";
    private static final int GESTATION = 4 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 4 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 4;

    public EntitySunBear(EntityType<? extends AbstractBear> type, World worldIn) {
        super(type, worldIn);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.3D, false, 1));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> this.getEcoLevel(input) > 5));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new GrazeGoal(this, 100));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof EntitySunBear)));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.13D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 24.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 15.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .createMutableAttribute(Attributes.ARMOR, 4D);
    }

    /* Diurnal: Active between 9:00 and 21:00 */
    public boolean isActive() {
        super.isActive();
        long time = this.world.getDayTime();
        return time > 3000 && time < 15000;
    }

    /* Breeding conditions for the Spectacled Bear are:
     * Hot Biome (T between 0.7 and 2)
     * No other entities nearby */
    public boolean wantsToBreed() {
        super.wantsToBreed();
        if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this) && this.getHunger() >= 80) {
            if (ConfigGamerules.hardcoreBreeding.get()) {
                List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                float i = this.world.getBiome(this.getPosition()).getTemperature(this.getPosition());
                return i >= 0.7 && i <= 2 && list.size() < 3;
            }
            return true;
        }
        return false;
    }

    @Nullable
    public EntitySunBear func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        return create_offspring(new EntitySunBear(ModEntity.SUN_BEAR, this.world));
    }

    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTables.BEAR_LOOT_SUN;
    }
    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.HONEYCOMB; }
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    public ResourceLocation getTexture() { return TEXTURE; }
    protected int getOffspring() { return 2; }
}