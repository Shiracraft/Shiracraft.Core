package mc.shiracraft.core.registry;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class RegistryHandler {

    /**
     * Method will instruct all registries to register themselves onto the eventbus.
     * @param eventBus Forge event bus
     */
    public static void registerAll(IEventBus eventBus) {
        ItemRegistry.register(eventBus);
        BlockRegistry.register(eventBus);
        CreativeTabsRegistry.register(eventBus);
        CommandArgumentRegistry.register(eventBus);
        LootModifierRegistry.register(eventBus);
    }

    public static void registerConfigs() {
        ConfigRegistry.registerConfigs();
    }

    public static void onCommandRegister(RegisterCommandsEvent event) {
        CommandRegistry.registerCommands(event.getDispatcher());
    }
}
