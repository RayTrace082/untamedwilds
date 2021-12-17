package untamedwilds.util;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import untamedwilds.UntamedWilds;

import java.util.ArrayList;
import java.util.List;

public class BiomeDataHolder {

    private final List<String> conditions;
    private final ArrayList<BiomeTestHolder> testerList;

    public BiomeDataHolder(List<String> biomeIn) {
        this.conditions = biomeIn;
        this.testerList = new ArrayList<>();
        for (String condition : this.conditions) {
            String key = condition;
            if (condition.contains("|"))
                key = condition.split("\\|")[1];
            ConditionTypes type = getTypeOfCondition(condition);
            ConditionModifiers modifier = getModifierFromString(condition);
            BiomeTestHolder testHolder = new BiomeTestHolder(key, type, modifier);
            testerList.add(testHolder);
        }
    }

    public boolean canSpawnInBiome(RegistryKey<Biome> biomekey, Biome biome) {
        for (BiomeTestHolder test : this.testerList) {
            if (!test.isValidBiome(biomekey, biome)) {
                return false;
            }
        }
        return true;
    }

    public static ConditionModifiers getModifierFromString(String strIn) {
        if (strIn.contains("!"))
            return ConditionModifiers.INVERTED;
        else if (strIn.contains("#"))
            return ConditionModifiers.PRIORITY;
        return ConditionModifiers.NONE;
    }

    public static ConditionTypes getTypeOfCondition(String strIn) {
        String clean = strIn.replaceAll("[!#]","");
        if (clean.contains("|")) {
            String str = clean.split("\\|")[0];
            UntamedWilds.LOGGER.info(str);
            switch (str) {
                case "category":
                    return ConditionTypes.BIOME_CATEGORY;
                case "dictionary":
                    return ConditionTypes.FORGE_DICTIONARY;
                case "resource":
                    return ConditionTypes.REGISTRY_NAME;
            }
        }
        return ConditionTypes.BIOME_CATEGORY;
    }

    public enum ConditionTypes implements IStringSerializable {
        BIOME_CATEGORY ("Category"),
        FORGE_DICTIONARY ("Dictionary"),
        REGISTRY_NAME ("Resource Location");

        public String type;

        ConditionTypes(String type) {
            this.type = type;
        }

        @Override
        public String getString() {
            return this.type;
        }
    }

    public enum ConditionModifiers implements IStringSerializable {
        NONE (" "),
        INVERTED (" Inverted "),
        PRIORITY (" Priority ");

        public String type;

        ConditionModifiers(String type) {
            this.type = type;
        }

        @Override
        public String getString() {
            return this.type;
        }
    }

    public static class BiomeTestHolder {

        private final String key;
        private final ConditionTypes type;
        private final ConditionModifiers modifier;

        public BiomeTestHolder(String key, ConditionTypes typeIn, ConditionModifiers modifierIn) {
            this.key = key;
            this.type = typeIn;
            this.modifier = modifierIn;
        }

        public boolean isValidBiome(RegistryKey<Biome> biomekey, Biome biome) {
            boolean result = false;
            switch (this.type) {
                case BIOME_CATEGORY:
                    result = biome.getCategory() == Biome.Category.byName(this.key);
                    break;
                case FORGE_DICTIONARY:
                    result = BiomeDictionary.hasType(biomekey, BiomeDictionary.Type.getType(this.key));
                    break;
                case REGISTRY_NAME:
                    UntamedWilds.LOGGER.info(biome.getRegistryName() + " " + new ResourceLocation(this.key));
                    result = biome.getRegistryName().equals(new ResourceLocation(this.key));
                    break;
            }
            if (this.modifier == ConditionModifiers.INVERTED) {
                return !result;
            }
            return result;
        }
    }
}