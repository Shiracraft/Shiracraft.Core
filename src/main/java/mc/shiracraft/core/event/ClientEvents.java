package mc.shiracraft.core.event;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.gui.screen.UnlockOverviewScreen;
import mc.shiracraft.core.registry.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Core.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents
{
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.OPEN_UNLOCK_SCREEN);
    }
}

@Mod.EventBusSubscriber(modid = Core.MOD_ID, value = Dist.CLIENT)
class ClientForgeEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getInstance();

            // Check if the unlock screen key is pressed
            while (KeyBindings.OPEN_UNLOCK_SCREEN.consumeClick()) {
                if (minecraft.player != null && minecraft.screen == null) {
                    minecraft.setScreen(new UnlockOverviewScreen(minecraft.player));
                }
            }
        }
    }
}