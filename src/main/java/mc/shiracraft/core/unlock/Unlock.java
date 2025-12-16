package mc.shiracraft.core.unlock;

import com.google.gson.annotations.Expose;
import mc.shiracraft.core.unlock.restriction.RestrictionType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * Inspired by the Vault Hunters modpack research system. Modified to fit Shiracraft's Casino mechanic.
 */
public abstract class Unlock {

    @Expose protected String name;
    @Expose protected UnlockCategory category;

    public Unlock(String name, UnlockCategory category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public abstract boolean restricts(Item item, RestrictionType restrictionType);

    public abstract boolean restricts(Block block, RestrictionType restrictionType);

    public abstract boolean restricts(EntityType<?> entityType, RestrictionType restrictionType);
}
