package untamedwilds.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.controller.DolphinLookController;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class ComplexMobAmphibious extends ComplexMobTerrestrial {

    protected boolean isAmphibious;
    public float buoyancy = 1;
    //protected final SwimmerPathNavigator waterNavigator;
    //protected final GroundPathNavigator groundNavigator;

    public ComplexMobAmphibious(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        //this.moveController = new ComplexMobAmphibious.MoveHelperController(this);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.lookController = new DolphinLookController(this, 10);
        this.stepHeight = 1.0F;
        //this.waterNavigator = new SwimmerPathNavigator(this, worldIn);
        //this.groundNavigator = new GroundPathNavigator(this, worldIn);
    }

    public boolean wantsToLeaveWater() { return true; }

    public boolean wantsToEnterWater() { return false; }

    public float getWaterSlowDown() {
        return this.onGround ? 0.8F : 0.9F;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_TURTLE_SWIM;
    }

    @Override
    public Vec3d getPositionVector() {
        return new Vec3d(this.getPosX(), this.getPosY() + 0.5D, this.getPosZ());
    }
}