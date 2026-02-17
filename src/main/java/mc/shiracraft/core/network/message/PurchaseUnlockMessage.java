package mc.shiracraft.core.network.message;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.registry.ConfigRegistry;
import mc.shiracraft.core.unlock.Unlock;
import mc.shiracraft.core.unlock.UnlockPurchaseService;
import mc.shiracraft.core.network.ShiracraftNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client -> Server request to purchase an unlock.
 */
public class PurchaseUnlockMessage {

    private final String unlockName;
    private final int price;

    public PurchaseUnlockMessage(String unlockName, int price) {
        this.unlockName = unlockName;
        this.price = price;
    }

    public static void encode(PurchaseUnlockMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.unlockName);
        buf.writeVarInt(message.price);
    }

    public static PurchaseUnlockMessage decode(FriendlyByteBuf buf) {
        String unlockName = buf.readUtf(256);
        int price = buf.readVarInt();
        return new PurchaseUnlockMessage(unlockName, price);
    }

    public static void handle(PurchaseUnlockMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            var sender = context.getSender();
            if (sender == null) return;

            // Only accept on server reception.
            if (context.getDirection().getReceptionSide().isClient()) {
                Core.LOGGER.warn("PurchaseUnlockMessage received on client side - ignoring");
                return;
            }

            Unlock unlock = ConfigRegistry.UNLOCK_CONFIG.getAll().stream()
                    .filter(u -> u != null && u.getName().equals(message.unlockName))
                    .findFirst()
                    .orElse(null);

            UnlockPurchaseService.PurchaseResult result = UnlockPurchaseService.tryPurchase(sender, unlock, message.price);

            ShiracraftNetwork.CHANNEL.send(
                    net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> sender),
                    new PurchaseUnlockResultMessage(message.unlockName, result)
            );

            // Still keep a simple failure message server-side.
            if (result != UnlockPurchaseService.PurchaseResult.SUCCESS) {
                sender.sendSystemMessage(net.minecraft.network.chat.Component.literal("Purchase failed: " + result));
            }
        });
        context.setPacketHandled(true);
    }
}
