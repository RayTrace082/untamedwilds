package untamedwilds.entity;

/**
 * Interface reserved for mobs which need to have their attributes updated after spawning
 * Allows mobs which have defined Species to assign attributes based on these traits
 */

public interface INeedsPostUpdate {

    void updateAttributes();
}
