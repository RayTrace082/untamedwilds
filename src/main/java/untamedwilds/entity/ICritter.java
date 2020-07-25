package untamedwilds.entity;

/**
 * Interface reserved for critters, mobs which randomly spawn/despawn in the world
 * Critters use traditional vanilla spawning, and have methods to prevent their despawning
 */

public interface ICritter {
    boolean preventDespawn();
    boolean isNoDespawnRequired();
}
