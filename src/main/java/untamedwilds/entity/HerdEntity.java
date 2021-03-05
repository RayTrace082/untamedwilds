package untamedwilds.entity;

import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class HerdEntity {
    private int maxHerdSize;
    private float radius = 8.0F;
    private boolean openToCombine;
    private ComplexMob leader;
    private final Random rand;
    public final List<ComplexMob> creatureList = new ArrayList<>();

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
            creature.herd.setLeader(creature);
            creature.herd.setOpenToCombine(false);
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

        if (this.getLeader().ticksExisted % 10 == 0) {
            List<ComplexMob> toRemove = new ArrayList<>();
            if (this.isOpenToCombine()) {
                List<ComplexMob> list = this.getLeader().getEntityWorld().getEntitiesWithinAABB(ComplexMob.class, this.getLeader().getBoundingBox().grow(16.0D, 12.0D, 16.0D));
                for (ComplexMob creature : list) {
                    if (!this.containsCreature(creature) && creature.herd != null && canCombineHerds(this, creature.herd)) {
                        int netSize = this.creatureList.size() + creature.herd.creatureList.size();
                        if (!creature.herd.isOpenToCombine() && creature.getClass().equals(this.getLeader().getClass()) && netSize <= this.getMaxSize() && netSize <= creature.herd.getMaxSize()) {
                            combineHerds(this, creature.herd);
                        }
                    }
                }
            }

            ComplexMob creature;
            for (ComplexMob complexMob : this.creatureList) {
                creature = complexMob;
                if (creature instanceof IPackEntity) {
                    IPackEntity packCreature = (IPackEntity)creature;
                    if (packCreature.shouldLeavePack()) {
                        toRemove.add(creature);
                        continue;
                    }
                }
                if (creature.isAlive() && creature.getDistanceSq(this.leader) <= 1024.0D) {
                    if (creature != this.leader) {
                        if (creature.getDistanceSq(this.leader) <= (double) (this.radius * this.radius)) {
                            Vector3d vec = this.leader.getLookVec();
                            creature.getLookController().setLookPosition(creature.getPosX() + vec.x, creature.getPosY() + vec.y, creature.getPosZ() + vec.z, 6.0F, 85.0F);
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
        ComplexMob creature;
        for (Iterator<ComplexMob> i$ = herd2.creatureList.iterator(); i$.hasNext(); creature.herd = herd1) {
            creature = i$.next();
        }
    }
}