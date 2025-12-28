package mc.shiracraft.core.casino.randomisation;

import mc.shiracraft.core.casino.reward.RewardType;
import mc.shiracraft.core.registry.ConfigRegistry;
import mc.shiracraft.core.unlock.UnlockCategory;
import mc.shiracraft.core.util.RandomUtils;

import java.util.Random;

public class RollService {
    public static RewardType roll(UnlockCategory category) {
        var random = new Random(RandomUtils.getSeed());

        var rollChances = getRollChances(category);
        var roll = random.nextDouble(0, 1);

        return determineRewardType(rollChances, roll);
    }

    private static RewardType determineRewardType(RollChances rollChances, double roll) {
        var cumulativeChance = 0.0;

        // With a cumulative approach, check each reward type in order of rarity. The rarest reward goes first.

        cumulativeChance += rollChances.unlockChance;
        if (roll < cumulativeChance) {
            return RewardType.UNLOCK;
        }

        cumulativeChance += rollChances.doubleCoinsChance;
        if (roll < cumulativeChance) {
            return RewardType.DOUBLE_COINS;
        }

        cumulativeChance += rollChances.refundChance;
        if (roll < cumulativeChance) {
            return RewardType.REFUND;
        }

        if (roll < 1.0) {
            return RewardType.PITY_TOKEN;
        }

        return RewardType.NONE;
    }

    private static RollChances getRollChances(UnlockCategory category) {
        return ConfigRegistry.ROLL_CHANCE_CONFIG.getChancesByCategory(category);
    }
}
