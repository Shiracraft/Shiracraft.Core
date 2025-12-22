package mc.shiracraft.core.config;

import com.google.gson.annotations.Expose;
import mc.shiracraft.core.casino.randomisation.RollChances;
import mc.shiracraft.core.unlock.UnlockCategory;

import java.util.ArrayList;
import java.util.List;

public class RollChanceConfig extends Config {
    @Expose
    public List<RollChances> rollChances;

    public RollChanceConfig() {
        rollChances = new ArrayList<>();
    }

    public RollChances getChancesByCategory(UnlockCategory category) {
        return rollChances.stream()
                .filter(chances -> chances.category == category)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getName() {
        return "roll_chances";
    }

    @Override
    protected void reset() {

    }
}
