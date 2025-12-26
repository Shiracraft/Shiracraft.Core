package mc.shiracraft.core.registry;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.config.RollChanceConfig;
import mc.shiracraft.core.config.UnlockConfig;

public class ConfigRegistry {
    public static UnlockConfig UNLOCK_CONFIG;
    public static RollChanceConfig ROLL_CHANCE_CONFIG;

    public static void registerConfigs() {
        UNLOCK_CONFIG = new UnlockConfig().readConfig();
        ROLL_CHANCE_CONFIG = new RollChanceConfig().readConfig();
    }

    public static void reloadConfigs() {
        Core.LOGGER.info("Reloading configurations...");
        UNLOCK_CONFIG = new UnlockConfig().readConfig();
        ROLL_CHANCE_CONFIG = new RollChanceConfig().readConfig();
        Core.LOGGER.info("Configurations reloaded successfully!");
    }
}
