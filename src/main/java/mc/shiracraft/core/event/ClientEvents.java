package mc.shiracraft.core.event;

import mc.shiracraft.core.Core;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@Mod.EventBusSubscriber(modid = Core.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents
{
}