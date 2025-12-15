package mc.shiracraft.core.registries;

import mc.shiracraft.core.Core;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabsRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Core.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SHIRACRAFT_TAB = CREATIVE_MODE_TABS.register("shiracraft_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetabs.shiracraft"))
            .icon(() -> new ItemStack(ItemRegistry.SLOT_COIN.get()))
            .displayItems((parameters, output) -> {
                // Add items to the creative tab
                output.accept(ItemRegistry.SLOT_COIN.get());
                output.accept(ItemRegistry.PITY_TOKEN.get());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
        Core.LOGGER.info("Creative Mode Tab Registry Initialized. Registered {} tab(s).", CREATIVE_MODE_TABS.getEntries().size());
    }
}
