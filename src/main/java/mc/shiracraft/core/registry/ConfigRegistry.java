package mc.shiracraft.core.registry;

import mc.shiracraft.core.config.UnlockConfig;

public class ConfigRegistry {
    public static UnlockConfig UNLOCK_CONFIG;

    public static void registerConfigs() {
        UNLOCK_CONFIG = new UnlockConfig().readConfig();
    }
}
