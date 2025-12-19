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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ModUnlock extends Unlock {

    @Expose
    protected Set<String> modIds;
    @Expose
    protected Restrictions restrictions;

    public ModUnlock(String name, UnlockCategory category, String... modIds) {
        super(name, category);
        this.modIds = new HashSet<>();
        this.restrictions = Restrictions.forMods();

        Collections.addAll(this.modIds, modIds);
    }

    public Set<String> getModIds() {
        return modIds;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public ModUnlock withRestrictions(
            boolean hittability,
            boolean entityInteractability,
            boolean blockInteractability,
            boolean usability,
            boolean craftability
    ) {
        this.restrictions.set(RestrictionType.HITTABILITY, hittability);
        this.restrictions.set(RestrictionType.ENTITY_INTERACTABILITY, entityInteractability);
        this.restrictions.set(RestrictionType.BLOCK_INTERACTABILITY, blockInteractability);
        this.restrictions.set(RestrictionType.USABILITY, usability);
        this.restrictions.set(RestrictionType.CRAFTABILITY, craftability);
        return this;
    }

    @Override
    public boolean restricts(Item item, RestrictionType restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) return false;
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName == null) return false;
        return modIds.contains(registryName.getNamespace());
    }

    @Override
    public boolean restricts(Block block, RestrictionType restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) return false;
        ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(block);
        if (registryName == null) return false;
        return modIds.contains(registryName.getNamespace());
    }

    @Override
    public boolean restricts(EntityType<?> entityType, RestrictionType restrictionType) {
        if (!this.restrictions.restricts(restrictionType)) return false;
        ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (registryName == null) return false;
        return modIds.contains(registryName.getNamespace());
    }
}
