package mc.shiracraft.core.casino;

import com.mojang.logging.LogUtils;
import mc.shiracraft.core.casino.items.ItemRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import org.slf4j.Logger;

public class CasinoRegistryHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(IEventBus eventBus) {
        ItemRegistry.register(eventBus);

        LOGGER.info("Casino Registry Initialized. Registered {} items, {} blocks.", ItemRegistry.ITEMS.getEntries().size(), 0);
    }
}
