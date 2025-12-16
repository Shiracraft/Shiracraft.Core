package mc.shiracraft.core.unlock.restriction;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

/**
 * Initially developed by Team Iskallia for the Vault Hunters modpack.
 * <br/>
 * <a href="https://github.com/Iskallia/Vault-public-S1/blob/master/src/main/java/iskallia/vault/research/Restrictions.java">
 * View on GitHub
 * </a>
 */
public class Restrictions {

    @Expose
    protected Map<RestrictionType, Boolean> restricts;

    private Restrictions() {
        this.restricts = new HashMap<>();
    }

    public Restrictions set(RestrictionType type, boolean restricts) {
        this.restricts.put(type, restricts);
        return this;
    }

    public boolean restricts(RestrictionType type) {
        return this.restricts.getOrDefault(type, false);
    }

    public static Restrictions forMods() {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(RestrictionType.USABILITY, false);
        restrictions.restricts.put(RestrictionType.CRAFTABILITY, false);
        restrictions.restricts.put(RestrictionType.HITTABILITY, false);
        restrictions.restricts.put(RestrictionType.BLOCK_INTERACTABILITY, false);
        restrictions.restricts.put(RestrictionType.ENTITY_INTERACTABILITY, false);
        return restrictions;
    }

    public static Restrictions forItems(boolean restricted) {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(RestrictionType.USABILITY, restricted);
        restrictions.restricts.put(RestrictionType.CRAFTABILITY, restricted);
        restrictions.restricts.put(RestrictionType.HITTABILITY, restricted);
        return restrictions;
    }

    public static Restrictions forBlocks(boolean restricted) {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(RestrictionType.HITTABILITY, restricted);
        restrictions.restricts.put(RestrictionType.BLOCK_INTERACTABILITY, restricted);
        return restrictions;
    }

    public static Restrictions forEntities(boolean restricted) {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(RestrictionType.HITTABILITY, restricted);
        restrictions.restricts.put(RestrictionType.ENTITY_INTERACTABILITY, restricted);
        return restrictions;
    }
}