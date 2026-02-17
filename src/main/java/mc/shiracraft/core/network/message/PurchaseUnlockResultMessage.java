package mc.shiracraft.core.network.message;

import mc.shiracraft.core.gui.screen.ShopScreen;
import mc.shiracraft.core.unlock.UnlockPurchaseService;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Server -> Client response for a purchase attempt.
 */
public class PurchaseUnlockResultMessage {

    private final String unlockName;
    private final UnlockPurchaseService.PurchaseResult result;

    public PurchaseUnlockResultMessage(String unlockName, UnlockPurchaseService.PurchaseResult result) {
        this.unlockName = unlockName;
        this.result = result;
    }

    public static void encode(PurchaseUnlockResultMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.unlockName);
        buf.writeVarInt(message.result.ordinal());
    }

    public static PurchaseUnlockResultMessage decode(FriendlyByteBuf buf) {
        String unlockName = buf.readUtf(256);
        int ordinal = buf.readVarInt();
        UnlockPurchaseService.PurchaseResult[] values = UnlockPurchaseService.PurchaseResult.values();
        UnlockPurchaseService.PurchaseResult result = ordinal >= 0 && ordinal < values.length
                ? values[ordinal]
                : UnlockPurchaseService.PurchaseResult.INVALID_REQUEST;
        return new PurchaseUnlockResultMessage(unlockName, result);
    }

    public static void handle(PurchaseUnlockResultMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            var mc = Minecraft.getInstance();
            if (mc.screen instanceof ShopScreen shopScreen) {
                shopScreen.onServerResponse();
            }
        });
        context.setPacketHandled(true);
    }
}

