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
        rollChances.clear();

        RollChances otherChances = new RollChances();
        otherChances.category = UnlockCategory.OTHER;
        otherChances.unlockChance = 0.05;
        otherChances.doubleCoinsChance = 0.10;
        otherChances.refundChance = 0.15;
        otherChances.pityTokenChance = 0.20;
        rollChances.add(otherChances);

        RollChances technicalChances = new RollChances();
        technicalChances.category = UnlockCategory.TECHNICAL_MOD;
        technicalChances.unlockChance = 0.05;
        technicalChances.doubleCoinsChance = 0.10;
        technicalChances.refundChance = 0.15;
        technicalChances.pityTokenChance = 0.20;
        rollChances.add(technicalChances);

        RollChances magicalChances = new RollChances();
        magicalChances.category = UnlockCategory.MAGICAL_MOD;
        magicalChances.unlockChance = 0.05;
        magicalChances.doubleCoinsChance = 0.10;
        magicalChances.refundChance = 0.15;
        magicalChances.pityTokenChance = 0.20;
        rollChances.add(magicalChances);

        RollChances foodChances = new RollChances();
        foodChances.category = UnlockCategory.FOOD_MOD;
        foodChances.unlockChance = 0.05;
        foodChances.doubleCoinsChance = 0.10;
        foodChances.refundChance = 0.15;
        foodChances.pityTokenChance = 0.20;
        rollChances.add(foodChances);
    }
}
