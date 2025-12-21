package mc.shiracraft.core.network.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class NetworkUtility {
    public static boolean runForPlayer(MinecraftServer server, UUID playerUUID, Consumer<Player> action) {
        if (server == null) {
            return false;
        }

        Player player = server.getPlayerList().getPlayer(playerUUID);

        if (player == null) {
            return false;
        }

        action.accept(player);
        return true;
    }
}
