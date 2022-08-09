package untamedwilds.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class EntityDataHolderClient {

    public final Map<Integer, Map<String, SoundEvent>> sounds;
    public final HashMap<Integer, String> species_data;

    public EntityDataHolderClient(Map<Integer, Map<String, SoundEvent>> p_i232114_3_, HashMap<Integer, String> attack) {
        this.sounds = p_i232114_3_;
        this.species_data = attack;
    }

    /*@Nullable
    public SoundEvent getSoundsWithAlt(int i, String sound_id, SoundEvent alt_sound) {
        SoundEvent event = this.getSounds(i, sound_id);
        if (event == null)
            return alt_sound;
        return event;
    }

    @Nullable
    public SoundEvent getSounds(int i, String sound_id) {
        UntamedWilds.LOGGER.info(this.sounds.toString());
        if (this.sounds.containsKey(i) && this.sounds.get(i).containsKey(sound_id)) {
            return this.sounds.get(i).get(sound_id);
        }
        if (this.sounds.get(-1).containsKey(sound_id)) {
            return this.sounds.get(-1).get(sound_id);
        }
        return null;
    }*/

    public String getSpeciesName(int i) {
        return this.species_data.get(i);
    }

    public int getNumberOfSpecies() {
        return this.species_data.size();
    }

    public void addSpeciesName(int id, String name) {
        if (!this.species_data.containsKey(id)) {
            this.species_data.put(id, name);
        }
    }

    public void addSoundData(int id, String sound_type, ResourceLocation sound) {
        if (!this.sounds.containsKey(id)) {
            this.sounds.put(id, new HashMap<>());
        }
        this.sounds.get(id).put(sound_type, new SoundEvent(sound));
    }

    /*@Nullable
    public SoundEvent getSound(int id, String sound_type) {
        UntamedWilds.LOGGER.info(this.sounds.toString());
        if (this.sounds.containsKey(id) && this.sounds.get(id).containsKey(sound_type)) {
            return this.sounds.get(id).get(sound_type);
        }
        if (this.sounds.containsKey(-1) && this.sounds.get(-1).containsKey(sound_type)) {
            return this.sounds.get(-1).get(sound_type);
        }
        return null;
    }*/
}