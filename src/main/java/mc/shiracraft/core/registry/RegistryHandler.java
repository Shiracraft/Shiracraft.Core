package mc.shiracraft.core.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public final class RegistryHandler {

    /**
     * Method will instruct all registries to register themselves onto the eventbus.
     * @param eventBus Forge event bus
     */
    public static void registerAll(IEventBus eventBus) {
        ItemRegistry.register(eventBus);
        CreativeTabsRegistry.register(eventBus);
    }

}
