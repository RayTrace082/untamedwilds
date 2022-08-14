package untamedwilds.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public interface INestingMob {

    // When using this Interface, use a variable to keep track of the egg-laying state

    /**
     * Method to check whether the mob needs to deposit its eggs, and build a nest, if none is available
     */
    boolean wantsToLayEggs();

    /**
     * Method to set the egg-laying status
     * @param status the new boolean
     */
    void setEggStatus(boolean status);

    /**
     * Assigns the Block instance which this entity will use as a nest, only blocks with an attached NestBlockEntity will be valid
     */
    Block getNestType();

    /**
     * Additional conditions to check whether a block is a valid nesting position or not, including conditions for it to be one
     * @param pos the BlockPos to be checked
     */
    boolean isValidNestBlock(BlockPos pos);
}
