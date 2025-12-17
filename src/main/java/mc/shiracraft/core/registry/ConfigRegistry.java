package mc.shiracraft.core.registry;

import mc.shiracraft.core.config.ModUnlockConfig;

public class ConfigRegistry {
    public static ModUnlockConfig MOD_UNLOCK_CONFIG;

    public static void registerConfigs() {
        MOD_UNLOCK_CONFIG = new ModUnlockConfig().readConfig();
    }
}
