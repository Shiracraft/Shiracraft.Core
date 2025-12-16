package mc.shiracraft.core.unlock.types;

import mc.shiracraft.core.unlock.Unlock;
import mc.shiracraft.core.unlock.UnlockCategory;
import mc.shiracraft.core.unlock.restriction.RestrictionType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModUnlock extends Unlock {

    public ModUnlock(String name, UnlockCategory category) {
        super(name, category);
    }

    @Override
    public boolean restricts(Item item, RestrictionType restrictionType) {
        return false;
    }

    @Override
    public boolean restricts(Block block, RestrictionType restrictionType) {
        return false;
    }

    @Override
    public boolean restricts(EntityType<?> entityType, RestrictionType restrictionType) {
        return false;
    }
}
