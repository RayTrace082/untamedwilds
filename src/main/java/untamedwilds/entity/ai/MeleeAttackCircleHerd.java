package untamedwilds.entity.ai;

import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.Effects;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;

public class MeleeAttackCircleHerd extends Goal {
    protected final ComplexMob attacker;
    protected int attackTick;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private Path path;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private float extraReach;
    private long field_220720_k;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;
    private byte invert = 1;
    private float offset = 0;
    private boolean isJumper;

    public MeleeAttackCircleHerd(ComplexMob entityIn, double speedIn, boolean useLongMemory) {
        this(entityIn, speedIn, useLongMemory, 0);
    }

    public MeleeAttackCircleHerd(ComplexMob entityIn, double speedIn, boolean useLongMemory, float reach) {
        this(entityIn, speedIn, useLongMemory, reach, false);
    }

    public MeleeAttackCircleHerd(ComplexMob entityIn, double speedIn, boolean useLongMemory, float reach, boolean isJumper) {
        this.attacker = entityIn;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.extraReach = reach;
        this.isJumper = isJumper;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (this.attacker.isChild()) {
            return false;
        }
        long i = this.attacker.world.getGameTime();
        if (i - this.field_220720_k < 20L) {
            return false;
        } else {
            this.field_220720_k = i;
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (canPenalize) {
                    if (--this.delayCounter <= 0) {
                        this.path = this.attacker.getNavigator().getPathToEntity(livingentity, 0);
                        this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
                this.path = this.attacker.getNavigator().getPathToEntity(livingentity, 0);
                return this.path != null;
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        LivingEntity livingentity = this.attacker.getAttackTarget();
        if (livingentity == null || (this.attacker.getAir() < 40 && !this.attacker.canBreatheUnderwater()) || !livingentity.isAlive()) {
            return false;
        } else if (!this.longMemory) {
            return !this.attacker.getNavigator().noPath();
        } else if (!this.attacker.isWithinHomeDistanceFromPosition(livingentity.getPosition())) {
            return false;
        }
        else {
            return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative();
        }
    }

    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
        this.attacker.setAggroed(true);
        this.delayCounter = 0;
    }

    public void resetTask() {
        LivingEntity livingentity = this.attacker.getAttackTarget();
        if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
            this.attacker.setAttackTarget(null);
        }
        this.attacker.setAggroed(false);
        this.attacker.getNavigator().clearPath();
    }

    public void tick() {
        LivingEntity livingentity = this.attacker.getAttackTarget();
        if (this.attacker.ticksExisted % 200 == 0) {
            this.offset = this.attacker.getRNG().nextInt(10);
        }
        if (this.attacker.herd.creatureList.size() == 1 || (this.attacker.ticksExisted % 200 < 61 && this.attacker.getAttackTarget().getRevengeTarget() != this.attacker)) {
            this.attacker.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
            double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getBoundingBox().minY, livingentity.getPosZ());
            --this.delayCounter;

            if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getPosX();
                this.targetY = livingentity.getBoundingBox().minY;
                this.targetZ = livingentity.getPosZ();
                this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                if (this.canPenalize) {
                    this.delayCounter += failedPathFindingPenalty;
                    if (this.attacker.getNavigator().getPath() != null) {
                        net.minecraft.pathfinding.PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                        if (finalPathPoint != null && livingentity.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    } else {
                        failedPathFindingPenalty += 10;
                    }
                }
                if (d0 > 1024.0D) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0D) {
                    this.delayCounter += 5;
                }

                if (!this.attacker.getNavigator().tryMoveToEntityLiving(livingentity, this.speedTowardsTarget)) {
                    this.delayCounter += 15;
                }
            }

            this.attackTick = Math.max(this.attackTick - 1, 0);
            this.checkAndPerformAttack(livingentity, d0);
        }
        else if (this.attacker.herd != null) {
            if (this.attacker.getAttackTarget() != null) {
                double x = this.attacker.getAttackTarget().getPosX() + Math.cos(this.offset + this.attacker.ticksExisted / 40F) * 6;
                double z = this.attacker.getAttackTarget().getPosZ() + Math.sin(this.offset + this.attacker.ticksExisted / 40F) * 6;
                this.attacker.getNavigator().tryMoveToXYZ(x, this.attacker.getAttackTarget().getPosY(), z, 1.2F);
            }
        }

        // TODO: Clean this shit up
        BlockPos testpos = this.attacker.getPosition().add(Math.cos(Math.toRadians(this.attacker.rotationYaw + 90)) * 1.2, 0, Math.sin(Math.toRadians(this.attacker.rotationYaw + 90)) * 1.2);
        if (this.isJumper && this.attacker.isOnGround() && this.attacker.getEntityWorld().getBlockState(testpos.down()).isAir() && this.attacker.getEntityWorld().getBlockState(testpos.down(2)).isAir() && this.attacker.getEntitySenses().canSee(livingentity)) { // TODO: "isJumper" param
            BlockPos targetpos = this.attacker.getPosition().add(Math.cos(Math.toRadians(this.attacker.rotationYaw + 90)) * 5, 0, Math.sin(Math.toRadians(this.attacker.rotationYaw + 90)) * 5);
            if (new Vector3d(targetpos.getX(), targetpos.getY(), targetpos.getZ()).distanceTo(livingentity.getPositionVec()) < this.attacker.getPositionVec().distanceTo(livingentity.getPositionVec())) {
                this.attacker.getEntityWorld().setBlockState(targetpos, Blocks.TORCH.getDefaultState());
                Random rand = this.attacker.getRNG();
                for (int i = 0; i < 4; i++) {
                    targetpos.add(rand.nextInt(2) - 1, rand.nextInt(2) - 1, rand.nextInt(2) - 1);
                    if (this.attacker.getNavigator().canEntityStandOnPos(targetpos)) {
                        Optional<Vector3d> jump_vec = this.calculateOptimalJumpVector(this.attacker, Vector3d.copyCentered(targetpos));
                        if (jump_vec.isPresent()) {
                            double d1 = jump_vec.get().length();
                            double d2 = 1 + d1 + (this.attacker.isPotionActive(Effects.JUMP_BOOST) ? (double)(0.1F * (float)(this.attacker.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1)) : 0.0D);
                            this.attacker.setMotion(jump_vec.get().x * d2 / d1, jump_vec.get().y, jump_vec.get().z * d2 / d1);
                            this.attacker.getNavigator().clearPath();
                        }
                    }
                }
            }
        }
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (this.attacker.canEntityBeSeen(enemy) && distToEnemySqr <= d0 && this.attackTick <= 0) {
            this.attackTick = 20;
            this.attacker.attackEntityAsMob(enemy);
        }

    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth() + this.extraReach);
    }

    // Start of the code gore
    private Optional<Vector3d> calculateOptimalJumpVector(CreatureEntity p_147657_, Vector3d p_147658_) {
        Optional<Vector3d> optional = Optional.empty();

        for(int i = 20; i < 55; i += 5) {
            //for(int i = 65; i < 85; i += 5) {
            Optional<Vector3d> optional1 = this.calculateJumpVectorForAngle(p_147657_, p_147658_, i);
            if (!optional.isPresent() || optional1.isPresent() && optional1.get().lengthSquared() < optional.get().lengthSquared()) {
                optional = optional1;
            }
        }

        return optional;
    }

    private Optional<Vector3d> calculateJumpVectorForAngle(CreatureEntity p_147660_, Vector3d p_147661_, int p_147662_) {
        Vector3d Vector3d = p_147660_.getPositionVec();
        Vector3d Vector3d1 = (new Vector3d(p_147661_.x - Vector3d.x, 0.0D, p_147661_.z - Vector3d.z)).normalize().scale(0.5D);
        p_147661_ = p_147661_.subtract(Vector3d1);
        Vector3d Vector3d2 = p_147661_.subtract(Vector3d);
        float f = (float)p_147662_ * (float)Math.PI / 180.0F;
        double d0 = Math.atan2(Vector3d2.z, Vector3d2.x);
        double d1 = Vector3d2.subtract(0.0D, Vector3d2.y, 0.0D).lengthSquared();
        double d2 = Math.sqrt(d1);
        double d3 = Vector3d2.y;
        double d4 = Math.sin((double)(2.0F * f));
        double d5 = 0.08D;
        double d6 = Math.pow(Math.cos((double)f), 2.0D);
        double d7 = Math.sin((double)f);
        double d8 = Math.cos((double)f);
        double d9 = Math.sin(d0);
        double d10 = Math.cos(d0);
        double d11 = d1 * 0.08D / (d2 * d4 - 2.0D * d3 * d6);
        if (d11 < 0.0D) {
            return Optional.empty();
        } else {
            double d12 = Math.sqrt(d11);
            if (d12 > 4) {
                return Optional.empty();
            } else {
                double d13 = d12 * d8;
                double d14 = d12 * d7;
                int i = (int) (Math.ceil(d2 / d13) * 2);
                double d15 = 0.0D;
                Vector3d Vector3d3 = null;

                for(int j = 0; j < i - 1; ++j) {
                    d15 += d2 / (double)i;
                    double d16 = d7 / d8 * d15 - Math.pow(d15, 2.0D) * 0.08D / (2.0D * d11 * Math.pow(d8, 2.0D));
                    double d17 = d15 * d10;
                    double d18 = d15 * d9;
                    Vector3d Vector3d4 = new Vector3d(Vector3d.x + d17, Vector3d.y + d16, Vector3d.z + d18);
                    if (Vector3d3 != null && !this.isClearTransition(Vector3d3, Vector3d4)) {
                        return Optional.empty();
                    }

                    Vector3d3 = Vector3d4;
                }

                return Optional.of((new Vector3d(d13 * d10, d14, d13 * d9)).scale((double)0.95F));
            }
        }
    }

    private boolean isClearTransition(Vector3d p_147665_, Vector3d p_147666_) {
        Vector3d Vector3d = p_147666_.subtract(p_147665_);
        double d0 = Math.min(this.attacker.getWidth(), this.attacker.getHeight());
        int i = (int) Math.ceil(Vector3d.length() / d0);
        Vector3d Vector3d1 = Vector3d.normalize();
        Vector3d Vector3d2 = p_147665_;

        for(int j = 0; j < i; ++j) {
            Vector3d2 = j == i - 1 ? p_147666_ : Vector3d2.add(Vector3d1.scale(d0 * (double)0.9F));
            AxisAlignedBB aabb = makeBoundingBox(Vector3d2.x, Vector3d2.y, Vector3d2.z);
            if (!this.attacker.getEntityWorld().checkNoEntityCollision(this.attacker, VoxelShapes.create(aabb))) {
                return false;
            }
        }

        return true;
    }

    public AxisAlignedBB makeBoundingBox(double p_20385_, double p_20386_, double p_20387_) {
        float f = this.attacker.getWidth() / 2.0F;
        float f1 = this.attacker.getHeight();
        return new AxisAlignedBB(p_20385_ - (double)f, p_20386_, p_20387_ - (double)f, p_20385_ + (double)f, p_20386_ + (double)f1, p_20387_ + (double)f);
    }
}