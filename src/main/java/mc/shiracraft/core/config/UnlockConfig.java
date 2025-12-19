package mc.shiracraft.core.config;

import com.google.gson.annotations.Expose;
import mc.shiracraft.core.unlock.Unlock;
import mc.shiracraft.core.unlock.UnlockCategory;
import mc.shiracraft.core.unlock.types.CustomUnlock;
import mc.shiracraft.core.unlock.types.ModUnlock;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class UnlockConfig extends Config {

    @Expose
    public List<ModUnlock> modUnlocks;
    @Expose
    public List<CustomUnlock> customUnlocks;

    public UnlockConfig() {
        modUnlocks = new LinkedList<>();
        customUnlocks = new LinkedList<>();
        modUnlocks.add(
                new ModUnlock("Minecraft", UnlockCategory.OTHER, "minecraft")
                        .withRestrictions(false, false, false, true, false)
        );
    }

    public List<Unlock> getAll() {
        List<Unlock> all = new LinkedList<>();
        all.addAll(modUnlocks);
        all.addAll(customUnlocks);
        return all;
    }

    public @Nullable Unlock getByName(String name) {
        for (var research : getAll()) {
            if (research.getName().equals(name)) {
                return research;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "unlocks";
    }

    @Override
    protected void reset() {

    }
}