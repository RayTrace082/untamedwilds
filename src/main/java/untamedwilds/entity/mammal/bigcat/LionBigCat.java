package untamedwilds.entity.mammal.bigcat;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModLootTables;

import java.util.List;

public class LionBigCat extends AbstractBigCat {

    private static final ResourceLocation TEXTURE = new ResourceLocation("untamedwilds:textures/entity/big_cat/lion_male.png");
    private static final ResourceLocation TEXTURE_FEMALE = new ResourceLocation("untamedwilds:textures/entity/big_cat/lion_female.png");
    private static final float SIZE = 1F;
    private static final String BREEDING = "ALL";
    private static final int GESTATION = 5 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 11 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 3;

    public LionBigCat(EntityType<? extends AbstractBigCat> type, World worldIn) {
        super(type, worldIn);
        this.ecoLevel = 8;
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, true));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.3D, false, 1));
        this.goalSelector.addGoal(3, new SmartFollowOwnerGoal(this, 2.3D, 16.0F, 3.0F));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> this.getEcoLevel(input) > 9));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D, true));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof LionBigCat)));
        this.targetSelector.addGoal(2, new SmartOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, false, input -> this.getEcoLevel(input) < 8));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.16D);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    /* Diurnal: Active between 7:00 and 22:00 */
    public boolean isActive() {
        super.isActive();
        long time = this.world.getDayTime();
        return time > 1000 && time < 16000;
    }

    /* Breeding conditions for the Lion are:
     * Warm Biome (T higher than 0.6)
     * No more than 3 other entities nearby */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth() && this.getHunger() >= 80) {
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

    public void breed() {
        for (int i = 0; i <= 1 + this.rand.nextInt(3); i++) {
            LionBigCat child = this.createChild(this);
            if (child != null) {
                child.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
                if (this.getOwner() != null) {
                    child.setTamedBy((PlayerEntity) this.getOwner());
                }
                this.world.addEntity(child);
            }
        }
    }

    public LionBigCat createChild(AgeableEntity ageable) {
        LionBigCat bear = new LionBigCat(ModEntity.LION, this.world);
        bear.setSpecies(this.getSpecies());
        bear.setGender(this.rand.nextInt(2));
        bear.setMobSize(this.rand.nextFloat());
        return bear;
    }

    protected ResourceLocation getLootTable() {
        return ModLootTables.BIGCAT_LOOT_LION;
    }
    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.BEEF; } // TODO: Replace with Zebra meat if Zebras are ever added
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    public ResourceLocation getTexture() {
        return this.isMale() ? TEXTURE : TEXTURE_FEMALE;
    }
}
