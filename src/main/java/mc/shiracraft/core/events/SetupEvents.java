package mc.shiracraft.core.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

public class SetupEvents {
    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void setupCommon(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public static void setupDedicatedServer(final FMLDedicatedServerSetupEvent event) {

    }
}
