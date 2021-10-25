package untamedwilds.entity.arthropod;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartAvoidGoal;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartSwimGoal;
import untamedwilds.entity.ai.target.DontThreadOnMeTarget;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityTarantula extends ComplexMob implements ISpecies, INewSkins {

    public int aggroProgress;
    //public int webProgress;

    public EntityTarantula(EntityType<? extends EntityTarantula> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 2.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .createMutableAttribute(Attributes.ARMOR, 0D);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > 8));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) < 4));
        this.targetSelector.addGoal(3, new DontThreadOnMeTarget<>(this, LivingEntity.class, true));
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
            this.setAngry(this.getAttackTarget() != null);
        }
        if (this.world.isRemote && this.isAngry() && this.aggroProgress < 40) {
            this.aggroProgress++;
        } else if (this.world.isRemote && !this.isAngry() && this.aggroProgress > 0) {
            this.aggroProgress--;
        }
    }

    /* Breeding conditions for the Tarantula are:
     * A nearby Tarantula of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && !this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityTarantula> list = this.world.getEntitiesWithinAABB(EntityTarantula.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
            list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
            if (list.size() >= 1) {
                this.setGrowingAge(this.getAdulthoodTime());
                list.get(0).setGrowingAge(this.getAdulthoodTime());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        EntityUtils.dropEggs(this, "egg_tarantula", this.getOffspring());
        return null;
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);

        if (itemstack.getItem() == Items.GLASS_BOTTLE && this.isAlive()) {
            EntityUtils.turnEntityIntoItem(this, "bottle_tarantula");
            itemstack.shrink(1);
            return ActionResultType.func_233537_a_(this.world.isRemote);
        }
        return super.func_230254_b_(player, hand);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 80, 0));
            }
            return true;
        }
        return false;
    }

    public boolean isPotionApplicable(EffectInstance potionEffectIn) {
        return potionEffectIn.getPotion() != Effects.POISON && super.isPotionApplicable(potionEffectIn);
    }
}
