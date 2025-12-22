package mc.shiracraft.core.casino.randomisation;

import com.google.gson.annotations.Expose;
import mc.shiracraft.core.unlock.UnlockCategory;

public class RollChances {
    @Expose public UnlockCategory category;
    @Expose public Double unlockChance;
    @Expose public Double refundChance;
    @Expose public Double doubleCoinsChance;
    @Expose public Double pityTokenChance;
}
