package untamedwilds.entity;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HerdEntity {
    private int maxHerdSize;
    private float radius = 8.0F;
    private boolean openToCombine;
    private ComplexMob leader;
    private final Random rand;
    public final List<ComplexMob> creatureList = new ArrayList<>();
    public double splitOffDistance = 1024D;

    public HerdEntity(ComplexMob creature, int maxSize) {
        this.openToCombine = true;
        this.rand = new Random();
        this.maxHerdSize = maxSize;
        this.setLeader(creature);
    }

    public void setLeader(ComplexMob creature) {
        this.leader = creature;
        if (!this.containsCreature(this.leader)) {
            this.addCreature(this.leader);
        }
    }

    public void chooseRandomLeader() {
        this.setLeader(this.creatureList.get(this.rand.nextInt(this.creatureList.size())));
    }

    public ComplexMob getLeader() {
        return this.leader;
    }

    public void addCreature(ComplexMob creature) {
        if (!this.creatureList.contains(creature)) {
            this.creatureList.add(creature);
        }
    }

    public boolean containsCreature(ComplexMob creature) {
        return this.creatureList.contains(creature);
    }

    public void removeCreature(HerdEntity herd, ComplexMob creature) {
        herd.creatureList.remove(creature);
        if (herd.creatureList.size() > 0 && herd.getLeader() == creature) {
            herd.chooseRandomLeader();
        }
        if (creature instanceof IPackEntity) {
            IPackEntity.initPack(creature);
            //creature.herd.setOpenToCombine(false);
        }
    }

    public void setMaxSize(int maxSchoolSize) {
        this.maxHerdSize = maxSchoolSize;
    }
    public int getMaxSize() {
        return this.maxHerdSize;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    public float getRadius() {
        return this.radius;
    }

    public void setOpenToCombine(boolean openToCombine) {
        this.openToCombine = openToCombine;
    }
    private boolean isOpenToCombine() {
        return this.openToCombine;
    }

    public void tick() {
        if (this.creatureList.size() == this.getMaxSize()) {
            this.setOpenToCombine(false);
        }
        else if (this.rand.nextInt(1800) == 0) {
            this.setOpenToCombine(!this.isOpenToCombine());
        }

        if (this.getLeader().tickCount % 10 == 0) {
            List<ComplexMob> toRemove = new ArrayList<>();
            if (this.isOpenToCombine()) {
                List<ComplexMob> list = this.getLeader().getLevel().getEntitiesOfClass(ComplexMob.class, this.getLeader().getBoundingBox().inflate(16.0D, 12.0D, 16.0D));
                for (ComplexMob creature : list) {
                    if (!this.containsCreature(creature) && creature.herd != null && canCombineHerds(this, creature.herd)) {
                        int netSize = this.creatureList.size() + creature.herd.creatureList.size();
                        if (creature.herd.isOpenToCombine() && creature.getClass().equals(this.getLeader().getClass()) && netSize <= this.getMaxSize() && netSize <= creature.herd.getMaxSize()) {
                            combineHerds(this, creature.herd);
                        }
                    }
                }
            }

            ComplexMob creature;
            for (ComplexMob complexMob : this.creatureList) {
                creature = complexMob;
                /*if (creature instanceof IPackEntity) {
                    IPackEntity packCreature = (IPackEntity)creature;
                    if (packCreature.shouldLeavePack()) {
                        toRemove.add(creature);
                        continue;
                    }
                }*/
                if (creature.isAlive() && creature.distanceToSqr(this.leader) <= this.splitOffDistance) {
                    if (creature != this.leader) {
                        if (creature.distanceToSqr(this.leader) <= (double) (this.radius * this.radius)) {
                            Vec3 vec = this.leader.getLookAngle();
                            creature.getLookControl().setLookAt(creature.getX() + vec.x, creature.getY() + vec.y, creature.getZ() + vec.z, 6.0F, 85.0F);
                        }
                    }
                } else {
                    toRemove.add(creature);
                }
            }

            for (ComplexMob mob : toRemove) {
                removeCreature(this, mob);
            }
        }
    }

    static boolean canCombineHerds(HerdEntity thisPack, HerdEntity otherPack) {
        return thisPack.creatureList.size() + otherPack.creatureList.size() <= thisPack.getMaxSize();
    }

    public static void combineHerds(HerdEntity herd1, HerdEntity herd2) {
        if (herd2.creatureList.size() > herd1.creatureList.size()) {
            herd1.setLeader(herd2.getLeader());
        }
        if (herd2.getMaxSize() < herd1.getMaxSize()) {
            herd1.setMaxSize(herd2.getMaxSize());
        }
        if (herd2.getRadius() < herd1.getRadius()) {
            herd1.setRadius(herd2.getRadius());
        }
        herd1.creatureList.addAll(herd2.creatureList);
    }
}