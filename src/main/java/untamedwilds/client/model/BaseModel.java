package untamedwilds.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import untamedwilds.UntamedWilds;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This class handles some common features from Model files
public abstract class BaseModel<T extends Entity> extends AdvancedEntityModel<T> {

    protected static final Map<String, Iterable<AdvancedModelBox>> ALL_PART_MAP = new HashMap<>();

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        Class clazz = getClass();
        String identifier = clazz.getName();
        Iterable<AdvancedModelBox> boxes;
        UntamedWilds.LOGGER.info("BRUH: First test");
        if (ALL_PART_MAP.get(identifier) == null) {
            List<AdvancedModelBox> rendererList = getFieldNameWithListDataType(clazz);
            UntamedWilds.LOGGER.info("BRUH: Third test " + rendererList);

            /*try {
                for (Field f : clazz.getDeclaredFields()) {
                    if(f.getType().equals(AdvancedModelBox.class)){
                        rendererList.add((AdvancedModelBox) f.get(null));
                    }
                    UntamedWilds.LOGGER.info("BRUH: Adding " + f.get(null) + " " + f.getType().getName());
                }
            }
            catch (Exception except) {
                UntamedWilds.LOGGER.warn(except);
            }*/
            ALL_PART_MAP.put(identifier, ImmutableList.copyOf(rendererList));
            UntamedWilds.LOGGER.info("BRUH " + getClass() + " " + ImmutableList.copyOf(rendererList));
        }
        boxes = ALL_PART_MAP.get(identifier);
        return boxes;
    }

    private static List<AdvancedModelBox> getFieldNameWithListDataType(Class<?> clazz){
        List<AdvancedModelBox> result = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for(Field f : fields){
            UntamedWilds.LOGGER.info("BRUH: 2nd test " + fields);

            f.setAccessible(true);
            UntamedWilds.LOGGER.info("BRUH: 3nd test " + fields);

            // use equals to compare the data type.
            if(f.getType().equals(AdvancedModelBox.class)){
                Class<AdvancedModelBox> type = AdvancedModelBox.class;
                UntamedWilds.LOGGER.info("BRUH: 4th test " + f.getName());
                try {
                    UntamedWilds.LOGGER.info("BRUH " + f.get(type));
                    result.add((AdvancedModelBox) f.get(type));
                } catch (IllegalAccessException e) {
                    UntamedWilds.LOGGER.info(f.getType());
                    UntamedWilds.LOGGER.info("BRUH");
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
