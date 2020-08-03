package untamedwilds.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.DolphinLookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.pathfinding.WalkAndSwimNodeProcessor;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class ComplexMobAmphibious extends ComplexMobTerrestrial {
    public float buoyancy = 1;
    protected boolean isAmphibious;
    protected boolean isLandNavigator;
    private boolean swimmingUp;
    //protected final SwimmerPathNavigator waterNavigator;
    //protected final GroundPathNavigator groundNavigator;

    public ComplexMobAmphibious(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new ComplexMobAmphibious.MoveHelperController(this);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.lookController = new DolphinLookController(this, 10);
        this.stepHeight = 1.0F;
        //this.waterNavigator = new SwimmerPathNavigator(this, worldIn);
        //this.groundNavigator = new GroundPathNavigator(this, worldIn);
    }

    public boolean wantsToLeaveWater() { return true; }

    public boolean wantsToEnterWater() { return false; }

    public float getWaterSlowDown() {
        return 1;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    private boolean isPathOnHighGround() {
        if (this.navigator != null && this.navigator.getPath() != null && this.navigator.getPath().getFinalPathPoint() != null) {
            BlockPos target = new BlockPos(this.navigator.getPath().getFinalPathPoint().x, this.navigator.getPath().getFinalPathPoint().y, this.navigator.getPath().getFinalPathPoint().z);
            BlockPos amphibious = new BlockPos(this);
            return !(target.getY() >= amphibious.getY());
        }
        return false;
    }

    @Override
    public void travel(Vec3d movement) {
        if (this.isSitting()) {
            super.travel(Vec3d.ZERO);
            return;
        }
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(this.getAIMoveSpeed(), movement);
            this.move(MoverType.SELF, this.getMotion());
            Vec3d motion = this.getMotion();
            Vec3d motion_2 = motion.scale(0.7);
            this.setMotion(motion_2.getX(), motion.getY(), motion_2.getZ());
        } else {
            super.travel(movement);
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double deltaX = this.getPosX() - this.prevPosX;
        double deltaZ = this.getPosZ() - this.prevPosZ;
        double deltaY = this.getPosY() - this.prevPosY;
        float delta = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 4.0F;
        if (delta > 1.0F) {
            delta = 1.0F;
        }
        this.limbSwingAmount += (delta - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_TURTLE_SWIM;
    }

    @Override
    public Vec3d getPositionVector() {
        return new Vec3d(this.getPosX(), this.getPosY() + 0.5D, this.getPosZ());
    }

    static class AmphibiousNavigator extends SwimmerPathNavigator {

        AmphibiousNavigator(ComplexMobAmphibious turtle, World p_i48815_2_) {
            super(turtle, p_i48815_2_);
        }

        protected boolean canNavigate() {
            return true;
        }

        protected PathFinder getPathFinder(int p_179679_1_) {
            this.nodeProcessor = new WalkAndSwimNodeProcessor();
            return new PathFinder(this.nodeProcessor, p_179679_1_);
        }

        public boolean canEntityStandOnPos(BlockPos pos) {
             return !this.world.getBlockState(pos.down()).isAir();
        }
    }

    static class MoveHelperController extends MovementController {
        private final ComplexMobAmphibious entity;

        public MoveHelperController(ComplexMobAmphibious entity) {
            super(entity);
            this.entity = entity;
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO && !this.entity.getNavigator().noPath()) {
                double d0 = this.posX - this.entity.getPosX();
                double d1 = this.posY - this.entity.getPosY();
                double d2 = this.posZ - this.entity.getPosZ();
                double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 90.0F);
                this.entity.renderYawOffset = this.entity.rotationYaw;
                float f1 = (float)(this.speed * this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                this.entity.setAIMoveSpeed(MathHelper.lerp(0.125F, this.entity.getAIMoveSpeed(), f1));
                this.entity.setMotion(this.entity.getMotion().add(0.0D, (double)this.entity.getAIMoveSpeed() * d1 * 0.1D, 0.0D));
                if (this.entity.isInWater()) {
                    float f2 = -((float)(MathHelper.atan2(d1, MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double)(180F / (float)Math.PI)));
                    f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                    this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, f2, 5.0F);
                }
            } else {
                if (this.entity.getAttackTarget() == null && this.entity.getNavigator().noPath()) {
                    //this.setMotion(this.getMotion().add(0.0D, -0.005D, 0.0D));
                    //this.entity.setMotion(0.0D, this.entity.buoyancy - 1, 0.0D);
                    this.entity.setMotion(this.entity.getMotion().add(0.0D, this.entity.buoyancy - 1, 0.0D));
                }
                this.entity.setAIMoveSpeed(0.0F);
            }
        }
    }
}