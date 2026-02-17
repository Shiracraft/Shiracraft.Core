package mc.shiracraft.core.unlock;

import com.google.gson.annotations.Expose;
import mc.shiracraft.core.unlock.restriction.RestrictionType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Inspired by the Vault Hunters modpack research system. Modified to fit Shiracraft's Casino mechanic.
 */
public abstract class Unlock {

    @Expose protected String name;
    @Expose protected UnlockCategory category;
    @Expose protected String icon;

    public Unlock(String name, UnlockCategory category) {
        this.name = name;
        this.category = category;
        this.icon = null;
    }

    /**
     * Fluent helper to set an icon id for this unlock.
     *
     * @param icon Item id in the form "namespace:path" (e.g. "minecraft:grass_block").
     */
    @SuppressWarnings("unchecked")
    public <T extends Unlock> T withIcon(String icon) {
        this.icon = icon;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public UnlockCategory getCategory() {
        return category;
    }

    /**
     * @return item stack for the configured icon, or an empty map if not configured or invalid.
     */
    public ItemStack getIconStack() {
        String iconId = this.icon;
        if (iconId == null || iconId.isBlank()) {
            return new ItemStack(Items.MAP);
        }
        ResourceLocation id = ResourceLocation.tryParse(iconId);
        if (id == null) return new ItemStack(Items.MAP);

        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null || item == Items.AIR) {
            return new ItemStack(Items.MAP);
        }
        return new ItemStack(item);
    }

    public abstract boolean restricts(Item item, RestrictionType restrictionType);

    public abstract boolean restricts(Block block, RestrictionType restrictionType);

    public abstract boolean restricts(EntityType<?> entityType, RestrictionType restrictionType);
}
