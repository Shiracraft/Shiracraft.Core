package mc.shiracraft.core.registry;

import mc.shiracraft.core.config.SlotMachineConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class RegistryHandler {

    /**
     * Method will instruct all registries to register themselves onto the eventbus.
     * @param eventBus Forge event bus
     */
    public static void registerAll(IEventBus eventBus) {
        ItemRegistry.register(eventBus);
        CreativeTabsRegistry.register(eventBus);
    }

    public static void registerConfigs(FMLJavaModLoadingContext context) {
        SlotMachineConfig.register(context);
    }
}
