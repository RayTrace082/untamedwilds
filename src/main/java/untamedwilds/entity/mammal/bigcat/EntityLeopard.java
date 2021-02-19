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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ISkins;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModLootTables;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityLeopard extends AbstractBigCat implements ISkins {

    public static final int SKIN_NUMBER = 3;
    private static final List<ResourceLocation> TEXTURES = new ArrayList<>();
    private static final float SIZE = 0.9f;
    private static final String BREEDING = "ALL";
    private static final int GESTATION = 4 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 10 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 5;

    public EntityLeopard(EntityType<? extends AbstractBigCat> type, World worldIn) {
        super(type, worldIn);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, true));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.3D, false, 1));
        this.goalSelector.addGoal(3, new SmartFollowOwnerGoal(this, 2.3D, 12.0F, 3.0F));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> this.getEcoLevel(input) > 6));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof EntityLeopard)));
        this.targetSelector.addGoal(2, new SmartOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, false, input -> this.getEcoLevel(input) < 5));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.16D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 35.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .createMutableAttribute(Attributes.ARMOR, 0D);
    }

    /* Diurnal: Active between 7:00 and 22:00 */
    public boolean isActive() {
        super.isActive();
        long time = this.world.getDayTime();
        return time > 1000 && time < 16000;
    }

    /* Breeding conditions for the Leopard are:
     * Warm Biome (T higher than 0.6)
     * No other entities nearby */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth() && this.getHunger() >= 80) {
                if (ConfigGamerules.hardcoreBreeding.get()) {
                    List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                    float i = this.world.getBiome(this.getPosition()).getTemperature(this.getPosition());
                    return i >= 0.6 && list.size() < 3;
                }
                return true;
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerTextures(int count) {
        for(int i = 1; i < count + 1; i++)
            EntityLeopard.TEXTURES.add(new ResourceLocation(UntamedWilds.MOD_ID, String.format("textures/entity/big_cat/leopard_%d.png", i)));
    }

    @Nullable
    public EntityLeopard func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        return create_offspring(new EntityLeopard(ModEntity.LEOPARD, this.world));
    }

    protected ResourceLocation getLootTable() {
        return ModLootTables.BIGCAT_LOOT_LEOPARD;
    }
    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.PORKCHOP; }
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    public ResourceLocation getTexture() { return TEXTURES.get(this.getVariant()); }
    public int getSkinNumber() { return SKIN_NUMBER; }
    protected int getOffspring() { return 2; }
}
