package mc.shiracraft.core;

import com.mojang.logging.LogUtils;
import mc.shiracraft.core.registry.RegistryHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(Core.MOD_ID)
public class Core {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "shiracraft";

    public Core(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        RegistryHandler.registerConfigs();
        RegistryHandler.registerAll(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(RegistryHandler::onCommandRegister);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public static String sId(String name) {
        return MOD_ID + ":" + name;
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
