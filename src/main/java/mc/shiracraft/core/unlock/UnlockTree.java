package mc.shiracraft.core.unlock;

import mc.shiracraft.core.registry.ConfigRegistry;
import mc.shiracraft.core.unlock.restriction.RestrictionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class UnlockTree implements INBTSerializable<CompoundTag> {

    protected UUID playerUUID;
    protected List<String> unlockedItems = new LinkedList<>();

    public UnlockTree(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void unlock(String unlockName) {
        unlockedItems.add(unlockName);
    }

    public void restrict(String unlockName) {
        unlockedItems.remove(unlockName);
    }

    public String restrictedBy(Item item, RestrictionType restrictionType) {
        for (Unlock unlock : ConfigRegistry.UNLOCK_CONFIG.getAll()) {
            if (unlockedItems.contains(unlock.getName())) continue;
            if (unlock.restricts(item, restrictionType)) return unlock.getName();
        }
        return null;
    }

    public String restrictedBy(Block block, RestrictionType restrictionType) {
        for (Unlock unlock : ConfigRegistry.UNLOCK_CONFIG.getAll()) {
            if (unlockedItems.contains(unlock.getName())) continue;
            if (unlock.restricts(block, restrictionType)) return unlock.getName();
        }
        return null;
    }

    public String restrictedBy(EntityType<?> entityType, RestrictionType restrictionType) {
        for (Unlock unlock : ConfigRegistry.UNLOCK_CONFIG.getAll()) {
            if (unlockedItems.contains(unlock.getName())) continue;
            if (unlock.restricts(entityType, restrictionType)) return unlock.getName();
        }
        return null;
    }

    public void reset() {
        unlockedItems.clear();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("PlayerUUID", this.playerUUID);

        ListTag unlocksListTag = new ListTag();
        for (String unlockName : unlockedItems) {
            unlocksListTag.add(StringTag.valueOf(unlockName));
        }
        nbt.put("Unlocks", unlocksListTag);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.playerUUID = nbt.getUUID("PlayerUUID");
        this.unlockedItems.clear();

        ListTag unlocksListTag = nbt.getList("Unlocks", Tag.TAG_STRING);
        for (int i = 0; i < unlocksListTag.size(); i++) {
            String unlockName = unlocksListTag.getString(i);
            if (!unlockName.isEmpty()) {
                this.unlockedItems.add(unlockName);
            }
        }
    }
}
