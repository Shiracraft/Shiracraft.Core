package mc.shiracraft.core.world.data;

import mc.shiracraft.core.unlock.Unlock;
import mc.shiracraft.core.unlock.UnlockTree;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UnlockData extends SavedData {
    private final Map<UUID, UnlockTree> unlockTreeMap = new HashMap<>();

    public UnlockTree getUnlockTree(Player player) {
        return getUnlockTree(player.getUUID());
    }

    public UnlockTree getUnlockTree(UUID uuid) {
        return this.unlockTreeMap.computeIfAbsent(uuid, UnlockTree::new);
    }

    public UnlockData unlock(Player player, Unlock unlock) {
        UnlockTree unlockTree = getUnlockTree(player);
        unlockTree.unlock(unlock.getName());

        setDirty();
        return this;
    }

    public UnlockData resetUnlockTree(Player player) {
        UnlockTree unlockTree = getUnlockTree(player);
        unlockTree.reset();

        setDirty();
        return this;
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag compoundTag) {
        CompoundTag unlockDataTag = new CompoundTag();

        for (Map.Entry<UUID, UnlockTree> entry : unlockTreeMap.entrySet()) {
            UUID playerUUID = entry.getKey();
            UnlockTree unlockTree = entry.getValue();

            CompoundTag treeTag = unlockTree.serializeNBT();
            unlockDataTag.put(playerUUID.toString(), treeTag);
        }

        compoundTag.put("UnlockData", unlockDataTag);
        return compoundTag;
    }

    public static UnlockData load(CompoundTag compoundTag) {
        UnlockData data = new UnlockData();

        CompoundTag unlockDataTag = compoundTag.getCompound("UnlockData");

        for (String uuidString : unlockDataTag.getAllKeys()) {
            UUID playerUUID = UUID.fromString(uuidString);
            CompoundTag treeTag = unlockDataTag.getCompound(uuidString);

            UnlockTree unlockTree = new UnlockTree(playerUUID);
            unlockTree.deserializeNBT(treeTag);
            data.unlockTreeMap.put(playerUUID, unlockTree);
        }

        return data;
    }

    public static UnlockData get(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(
                        UnlockData::load,
                        UnlockData::new,
                        "unlock_data"
                );
    }
}
