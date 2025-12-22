package mc.shiracraft.core.casino;

import mc.shiracraft.core.casino.randomisation.RollService;
import mc.shiracraft.core.casino.reward.RewardType;
import mc.shiracraft.core.registry.ConfigRegistry;
import mc.shiracraft.core.registry.ItemRegistry;
import mc.shiracraft.core.unlock.Unlock;
import mc.shiracraft.core.unlock.UnlockCategory;
import mc.shiracraft.core.util.RandomUtils;
import mc.shiracraft.core.world.data.UnlockData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlotMachine {

    // TODO: Call upon slot machine interaction
    public static void rollSlots(Player player, UnlockCategory category) {
        final int SLOT_COINS_PER_ROLL = 1;
        if (!deductSlotCoins(player, SLOT_COINS_PER_ROLL)) {
            player.sendSystemMessage(Component.literal("You don't have enough slot coins to roll!"));
            return;
        }

        try {
            var rewardType = RollService.roll(category);

            if (rewardType == RewardType.UNLOCK) {
                rollUnlocks(player, category);
                return;
            }

            // TODO: Improve reward system and visualisation
            player.sendSystemMessage(Component.literal(String.format("You received a reward: %s", rewardType.name())));
        } catch (Exception e) {
            refundDeductedCoins(player, SLOT_COINS_PER_ROLL);
            throw e;
        }
    }

    private static void refundDeductedCoins(Player player, int cost) {
        player.getInventory().add(cost, ItemRegistry.SLOT_COIN.get().getDefaultInstance());
    }

    /**
     * Attempts to deduct slot coins from the player's inventory.
     * @param player The player to deduct coins from
     * @param cost The number of slot coins to deduct
     * @return true if the player had enough coins and they were deducted, false otherwise
     */
    private static boolean deductSlotCoins(Player player, int cost) {
        var inventory = player.getInventory();
        Item slotCoinItem = ItemRegistry.SLOT_COIN.get();

        // Single pass: find and track all coin stacks
        List<ItemStack> coinStacks = new ArrayList<>();
        int totalCoins = 0;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            var itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty() && itemStack.getItem() == slotCoinItem) {
                coinStacks.add(itemStack);
                totalCoins += itemStack.getCount();
            }
        }

        // Check if player has enough coins
        if (totalCoins < cost) {
            return false;
        }

        // Deduct from tracked stacks only
        int remaining = cost;
        for (ItemStack coinStack : coinStacks) {
            if (remaining <= 0) break;

            int toDeduct = Math.min(remaining, coinStack.getCount());
            coinStack.shrink(toDeduct);
            remaining -= toDeduct;
        }

        return true;
    }

    private static void rollUnlocks(Player player, UnlockCategory category) {
        var unlockData = UnlockData.get();
        var unlockTree = unlockData.getUnlockTree(player);
        var unlocks = ConfigRegistry.UNLOCK_CONFIG
                .getAll()
                .stream()
                .filter(unlock -> unlock.getCategory() == category
                        && !unlockTree.isUnlocked(unlock.getName()))
                .toList();

        var random = new Random(RandomUtils.getSeed());
        var unlock = unlocks.get(random.nextInt(unlocks.size()));
        unlockData.unlock(player, unlock.getName());

        displayUnlockRewards(player, unlock);
    }

    private static void displayUnlockRewards(Player player, Unlock unlock) {
        player.sendSystemMessage(Component.literal(String.format("You unlocked \"%s\"!", unlock.getName())));
    }
}
