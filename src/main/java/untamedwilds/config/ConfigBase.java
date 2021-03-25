package untamedwilds.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigBase {
    private static final ForgeConfigSpec.Builder common_builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec common_config;

    public static final ConfigFeatureControl FEATURES;
    public static final ConfigGamerules GAMERULES;
    public static final ConfigMobControl MOBS;

    static {
        FEATURES = new ConfigFeatureControl(common_builder);
        GAMERULES = new ConfigGamerules(common_builder);
        MOBS = new ConfigMobControl(common_builder);

        common_config = common_builder.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        config.setConfig(configData);
    }
}
