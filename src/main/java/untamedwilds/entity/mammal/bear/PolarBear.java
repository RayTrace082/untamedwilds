package untamedwilds.entity.mammal.bear;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.entity.ai.unique.BearRaidChestsGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModItems;
import untamedwilds.init.ModLootTables;

import java.util.List;

public class PolarBear extends AbstractBear {

    private static final ResourceLocation TEXTURE = new ResourceLocation("untamedwilds:textures/entity/bear/polar.png");
    private static final float SIZE = 1.1f;
    private static final String BREEDING = "LATE_SPRING";
    private static final int GESTATION = 10 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 10 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 1;

    public PolarBear(EntityType<? extends AbstractBear> type, World worldIn) {
        super(type, worldIn);
        this.ecoLevel = 8;
        this.swimSpeedMult = 1.4f;
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.3D, false, 1));
        this.goalSelector.addGoal(3, new SmartFollowOwnerGoal(this, 2.3D, 16.0F, 3.0F));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> this.getEcoLevel(input) > 9));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D, true));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new RaidCropsGoal(this));
        this.goalSelector.addGoal(4, new BearRaidChestsGoal(this, 120));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        //this.goalSelector.addGoal(7, new SmartLookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof PolarBear)));
        this.targetSelector.addGoal(2, new SmartOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, false, input -> this.getEcoLevel(input) < 7));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 8.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.17D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 24.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 45.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .createMutableAttribute(Attributes.ARMOR, 4D);
    }

    /* Diurnal: Active between 8:00 and 0:00 */
    public boolean isActive() {
        super.isActive();
        long time = this.world.getDayTime();
        return time < 13000 || time > 23000;
    }

    /* Breeding conditions for the Polar Bear are:
     * Frozen Biome (T between -1.0 and 0.1)
     * No other entities nearby */
    public boolean wantsToBreed() {
        super.wantsToBreed();
        if (ConfigGamerules.naturalBreeding.get() && !this.isSleeping() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth() && this.getHunger() >= 80) {
            if (ConfigGamerules.hardcoreBreeding.get()) {
                List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                float i = this.world.getBiome(this.getPosition()).getTemperature(this.getPosition());
                return i >= -1.0 && i <= 0.1 && list.size() < 3;
            }
            return true;
        }
        return false;
    }

    public void breed() {
        for (int i = 0; i <= 1 + this.rand.nextInt(1); i++) {
            PolarBear child = this.func_241840_a((ServerWorld) this.world, this);
            child.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
            if (this.getOwner() != null) {
                child.setTamedBy((PlayerEntity) this.getOwner());
            }
            this.world.addEntity(child);
        }
    }

    public PolarBear func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        PolarBear bear = new PolarBear(ModEntity.POLAR_BEAR, this.world);
        bear.setGender(this.rand.nextInt(2));
        bear.setMobSize(this.rand.nextFloat());
        bear.setGrowingAge(this.getAdulthoodTime() * -2);
        return bear;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTables.BEAR_LOOT_POLAR;
    }
    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == ModItems.MATERIAL_FAT.get(); } // TODO: If Seals ever get added, replace this with their meat
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    public ResourceLocation getTexture() { return TEXTURE; }
}
