package untamedwilds.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import untamedwilds.UntamedWilds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HerdEntity {
    private int maxSchoolSize = 10;
    private float radius = 8.0F;
    private boolean openToCombine;
    private ComplexMob leader;
    private final World world;
    public final List<ComplexMob> creatureList = new ArrayList<>();
    public Vector3d fleeLookVec = null;

    public HerdEntity(ComplexMob creature) {
        //this.openToCombine = creature.world.rand.nextInt(4) == 0;
        this.openToCombine = true;
        this.world = creature.world;
        this.setLeader(creature);
    }

    public void setLeader(ComplexMob creature) {
        this.leader = creature;
        if (!this.creatureList.contains(this.leader)) {
            this.addCreature(this.leader);
        }
    }

    public void chooseRandomLeader() {
        this.setLeader(this.creatureList.get(this.world.rand.nextInt(this.creatureList.size())));
    }

    public ComplexMob getLeader() {
        return this.leader;
    }

    public void addCreature(ComplexMob creature) {
        if (!this.creatureList.contains(creature)) {
            UntamedWilds.LOGGER.info("Adding new entity to Herd");
            this.creatureList.add(creature);
        }
    }

    public boolean containsCreature(ComplexMob creature) {
        return this.creatureList.contains(creature);
    }

    public void removeCreature(ComplexMob creature) {
        this.creatureList.remove(creature);
        if (this.creatureList.size() > 0 && this.getLeader() == creature) {
            this.chooseRandomLeader();
        }

        creature.initPack();
        creature.herd.setOpenToCombine(false);
        creature.followEntity = null;
        creature.targetVec = null;
    }

    public void setMaxSize(int maxSchoolSize) {
        this.maxSchoolSize = maxSchoolSize;
    }

    public int getMaxSize() {
        return this.getLeader().maxSchoolSize;
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

    public boolean isOpenToCombine() {
        return this.openToCombine;
    }

    public void tick() {
        if (!this.isOpenToCombine() && this.world.rand.nextInt(1500) == 0) {
            this.setOpenToCombine(true);
        } else if (this.isOpenToCombine() && this.world.rand.nextInt(1800) == 0) {
            this.setOpenToCombine(false);
        }

        if (this.isOpenToCombine()) {
            List<ComplexMob> list = this.world.getEntitiesWithinAABB(ComplexMob.class, this.getLeader().getBoundingBox().grow(16.0D, 12.0D, 16.0D));
            for (ComplexMob creature : list) {
                if (!this.containsCreature(creature) && creature.herd != null && creature.canCombineWith(this)) {
                    int netSize = this.creatureList.size() + creature.herd.creatureList.size();
                    if (creature.herd.isOpenToCombine() && creature.getClass().equals(this.getLeader().getClass()) && netSize <= this.getMaxSize() && netSize <= creature.herd.getMaxSize()) {
                        combineHerds(this, creature.herd);
                    }
                }
            }
        }

        //boolean nearLeader = false;
        //List<ComplexMob> leaderList = this.world.getEntitiesWithinAABB(ComplexMob.class, this.getLeader().getBoundingBox().grow(3));

        int i;
        ComplexMob creature;
        /*for (i = 0; i < leaderList.size(); ++i) {
            creature = leaderList.get(i);
            if (creature != this.leader && this.containsCreature(creature)) {
                nearLeader = true;
                break;
            }
        }*/

        for (i = 0; i < this.creatureList.size(); ++i) {
            creature = this.creatureList.get(i);
            MobEntity creature_entity = creature;
            if (creature_entity.isAlive() && creature_entity.getDistanceSq(this.leader) <= 1024.0D) {
                if (creature != this.leader) {
                    if (creature_entity.getDistanceSq(this.leader) <= (double)(this.radius * this.radius)/* && nearLeader*/) {
                        List<ComplexMob> list = this.world.getEntitiesWithinAABB(ComplexMob.class, creature_entity.getBoundingBox().grow(16.0D));
                        ComplexMob closestEntity = null;
                        double distSq = 1024.0D;
                        boolean hasNearby = false;

                        double dy;
                        for (ComplexMob thing : list) {
                            if (creature != thing && this.containsCreature(thing)) {
                                dy = creature_entity.getDistanceSq(thing);
                                float w = (creature_entity.getWidth() + thing.getWidth()) * 0.5F + 1.0F;
                                if (dy < (double) (w * w)) {
                                    hasNearby = true;
                                    break;
                                }

                                if (dy < distSq * distSq) {
                                    closestEntity = thing;
                                    distSq = dy;
                                }
                            }
                        }

                        if (!hasNearby && closestEntity != null) {
                            creature.followEntity = closestEntity;
                            creature.targetVec = null;
                        } else {
                            creature.followEntity = null;
                            if (this.leader.targetVec != null) {
                                if (creature.targetVec == null || this.world.rand.nextInt(10) == 0) {
                                    double dx = this.leader.targetVec.x - this.leader.getPosX() + (double)((this.world.rand.nextFloat() - 0.5F) * 1.1F);
                                    dy = this.leader.targetVec.y - this.leader.getPosY() + (double)((this.world.rand.nextFloat() - 0.5F) * 1.2F);
                                    double dz = this.leader.targetVec.z - this.leader.getPosZ() + (double)((this.world.rand.nextFloat() - 0.5F) * 1.1F);
                                    creature.targetVec = new Vector3d(creature_entity.getPosX() + dx, creature_entity.getPosY() + dy, creature_entity.getPosZ() + dz);
                                }
                            } else {
                                creature.targetVec = null;
                                Vector3d vec = this.leader.getLookVec();
                                creature_entity.getLookController().setLookPosition(creature_entity.getPosX() + vec.x, ((LivingEntity) creature).getPosY() + vec.y, ((LivingEntity) creature).getPosZ() + vec.z, 6.0F, 85.0F);
                            }
                        }
                    } else {
                        creature.followEntity = this.leader;
                        creature.targetVec = null;
                    }
                }
            } else {
                this.removeCreature(creature);
                --i;
            }
        }
    }

    public static HerdEntity combineHerds(HerdEntity herd1, HerdEntity herd2) {
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
        return herd1;
    }
}