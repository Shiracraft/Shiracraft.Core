package mc.shiracraft.core.event;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.world.data.UnlockData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Core.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class ServerEvents {

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        MinecraftServer server = player.getServer();

        if (server == null) return;

        UnlockData.get().getUnlockTree(player).sync(server);
    }
}
