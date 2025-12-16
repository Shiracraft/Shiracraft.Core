package mc.shiracraft.core.event;

import mc.shiracraft.core.Core;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {
    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        Core.LOGGER.info("Setting up client >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    public static void setupCommon(final FMLCommonSetupEvent event) {
        Core.LOGGER.info("Shiracraft.Core is being set-up...");
    }

    @SubscribeEvent
    public static void setupDedicatedServer(final FMLDedicatedServerSetupEvent event) {

    }
}
