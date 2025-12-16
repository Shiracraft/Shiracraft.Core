package mc.shiracraft.core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

public class SlotMachineConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue MOD_UNLOCK_CHANCE_VALUE;
    public static final ForgeConfigSpec.DoubleValue DOUBLE_COIN_CONFIG_VALUE;
    public static final ForgeConfigSpec.DoubleValue RETURN_COIN_CONFIG_VALUE;
//
//    public static final ForgeConfigSpec.ConfigValue<List<String>> TECHNICAL_MOD_IDS_VALUE;

    static {
        BUILDER.push("Chance configuration");

        MOD_UNLOCK_CHANCE_VALUE = BUILDER.defineInRange(
                "modUnlockChance",
                0.0001,
                0.0,
                1.0
        );

        DOUBLE_COIN_CONFIG_VALUE = BUILDER.defineInRange(
                "doubleCoinChance",
                0.001,
                0.0,
                1.0
        );

        RETURN_COIN_CONFIG_VALUE = BUILDER.defineInRange(
                "returnCoinChance",
                0.001,
                0.0,
                1.0
        );

        BUILDER.push("Technical mod section");

//        TECHNICAL_MOD_IDS_VALUE = BUILDER.define("technicalModIds", List.of("shiracraft.tech"));

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void register(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, SPEC);
    }
}
