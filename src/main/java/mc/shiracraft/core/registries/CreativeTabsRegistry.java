package mc.shiracraft.core.registries;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.creative.ShiracraftCreativeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabsRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Core.MOD_ID);

    // TODO: Fix registry freezing upon opening creative tabs
    public static final RegistryObject<CreativeModeTab> SHIRACRAFT_TAB = CREATIVE_TABS.register("shiracraft_tab", ShiracraftCreativeTab::new);

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
