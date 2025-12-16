package mc.shiracraft.core.unlock.types;

import com.google.gson.annotations.Expose;
import mc.shiracraft.core.unlock.Unlock;
import mc.shiracraft.core.unlock.UnlockCategory;
import mc.shiracraft.core.unlock.restriction.RestrictionType;
import mc.shiracraft.core.unlock.restriction.Restrictions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * Copied from Team Iskallia's Vault Hunters research system.
 * <br/>
 * <a href="https://github.com/Iskallia/Vault-public-S1/blob/master/src/main/java/iskallia/vault/research/type/CustomResearch.java">
 * View on GitHub
 * </a>
 */
public class CustomUnlock extends Unlock {
    @Expose
    protected Map<String, Restrictions> itemRestrictions;
    @Expose
    protected Map<String, Restrictions> blockRestrictions;
    @Expose
    protected Map<String, Restrictions> entityRestrictions;

    public CustomUnlock(String name, UnlockCategory category) {
        super(name, category);
        this.itemRestrictions = new HashMap<>();
        this.blockRestrictions = new HashMap<>();
        this.entityRestrictions = new HashMap<>();
    }

    public Map<String, Restrictions> getItemRestrictions() {
        return itemRestrictions;
    }

    public Map<String, Restrictions> getBlockRestrictions() {
        return blockRestrictions;
    }

    public Map<String, Restrictions> getEntityRestrictions() {
        return entityRestrictions;
    }

    @Override
    public boolean restricts(Item item, RestrictionType restrictionType) {
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName == null) return false;
        String sid = registryName.getNamespace() + ":" + registryName.getPath();
        Restrictions restrictions = itemRestrictions.get(sid);
        if (restrictions == null) return false;
        return restrictions.restricts(restrictionType);
    }

    @Override
    public boolean restricts(Block block, RestrictionType restrictionType) {
        ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(block);
        if (registryName == null) return false;
        String sid = registryName.getNamespace() + ":" + registryName.getPath();
        Restrictions restrictions = blockRestrictions.get(sid);
        if (restrictions == null) return false;
        return restrictions.restricts(restrictionType);
    }

    @Override
    public boolean restricts(EntityType<?> entityType, RestrictionType restrictionType) {
        ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (registryName == null) return false;
        String sid = registryName.getNamespace() + ":" + registryName.getPath();
        Restrictions restrictions = entityRestrictions.get(sid);
        if (restrictions == null) return false;
        return restrictions.restricts(restrictionType);
    }
}
