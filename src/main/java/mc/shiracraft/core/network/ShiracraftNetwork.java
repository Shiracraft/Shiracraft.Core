package mc.shiracraft.core.network;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.network.message.UnlockTreeMessage;
import mc.shiracraft.core.network.message.PurchaseUnlockMessage;
import mc.shiracraft.core.network.message.PurchaseUnlockResultMessage;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ShiracraftNetwork {
    public static final String NETWORK_VERSION = "0.1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            Core.id("network"),
            () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION),
            version -> version.equals(NETWORK_VERSION)
    );

    private static int packetId = 0;

    private static int nextId() {
        return packetId++;
    }

    public static void register() {
        CHANNEL.messageBuilder(UnlockTreeMessage.class, nextId())
                .encoder(UnlockTreeMessage::encode)
                .decoder(UnlockTreeMessage::decode)
                .consumerMainThread(UnlockTreeMessage::handle)
                .add();

        CHANNEL.messageBuilder(PurchaseUnlockMessage.class, nextId())
                .encoder(PurchaseUnlockMessage::encode)
                .decoder(PurchaseUnlockMessage::decode)
                .consumerMainThread(PurchaseUnlockMessage::handle)
                .add();

        CHANNEL.messageBuilder(PurchaseUnlockResultMessage.class, nextId())
                .encoder(PurchaseUnlockResultMessage::encode)
                .decoder(PurchaseUnlockResultMessage::decode)
                .consumerMainThread(PurchaseUnlockResultMessage::handle)
                .add();

        Core.LOGGER.info("Network messages registered!");
    }
}
