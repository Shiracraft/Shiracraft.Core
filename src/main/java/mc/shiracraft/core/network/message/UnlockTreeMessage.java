package mc.shiracraft.core.network.message;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.unlock.UnlockTree;
import mc.shiracraft.core.unlock.restriction.RestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class UnlockTreeMessage {
    public UnlockTree unlockTree;
    public UUID uuid;

    public UnlockTreeMessage(UnlockTree unlockTree, UUID uuid) {
        this.unlockTree = unlockTree;
        this.uuid = uuid;
    }

    public static void encode(UnlockTreeMessage message, FriendlyByteBuf buffer) {
        buffer.writeUUID(message.uuid);
        var nbt = message.unlockTree.serializeNBT();
        buffer.writeNbt(nbt);
    }

    public static UnlockTreeMessage decode(FriendlyByteBuf buffer) {
        UUID uuid = buffer.readUUID();
        var nbt = buffer.readNbt();

        if (nbt == null) {
            throw new IllegalStateException("Received null NBT data for UnlockTree");
        }

        UnlockTree unlockTree = new UnlockTree(uuid);
        unlockTree.deserializeNBT(nbt);

        return new UnlockTreeMessage(unlockTree, uuid);
    }

    public static void handle(UnlockTreeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            // This message should only be received on the client side
            if (context.getDirection().getReceptionSide().isClient()) {
                handleClient(message);
            } else {
                Core.LOGGER.warn("UnlockTreeMessage received on server side - this shouldn't happen!");
            }
        });

        context.setPacketHandled(true);
    }

    private static void handleClient(UnlockTreeMessage message) {
        RestrictionManager.UNLOCK_TREE = message.unlockTree;
    }
}
