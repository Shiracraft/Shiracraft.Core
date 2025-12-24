package mc.shiracraft.core.casino;

import mc.shiracraft.core.Core;
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

    public static void rollSlots(Player player, UnlockCategory category) {
        final int SLOT_COINS_PER_ROLL = 1;
        List<ItemStack> deductedCoins = deductSlotCoins(player, SLOT_COINS_PER_ROLL);

        if (deductedCoins == null) {
            player.sendSystemMessage(Component.literal("You don't have enough slot coins to roll!"));
            return;
        }

        try {
            var rewardType = RollService.roll(category);

            if (rewardType == RewardType.UNLOCK) {
                var unlockRollSuccess = rollUnlocks(player, category);
                if (unlockRollSuccess) return;
                rewardType = RewardType.REFUND;
            }

            // TODO: Improve reward system and visualisation
            player.sendSystemMessage(Component.literal(String.format("You received a reward: %s", rewardType.name())));
        } catch (Exception e) {
            Core.LOGGER.error("Error rolling slots!", e);
            refundDeductedCoins(player, deductedCoins);
            throw e;
        }
    }

    private static void refundDeductedCoins(Player player, List<ItemStack> deductedCoins) {
        for (ItemStack coinStack : deductedCoins) {
            player.getInventory().placeItemBackInInventory(coinStack);
        }
    }

    /**
     * Attempts to deduct slot coins from the player's inventory.
     * @param player The player to deduct coins from
     * @param cost The number of slot coins to deduct
     * @return List of ItemStacks that were deducted, or null if the player didn't have enough coins
     */
    private static List<ItemStack> deductSlotCoins(Player player, int cost) {
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
            return null;
        }

        // Deduct from tracked stacks and store the deducted amounts
        List<ItemStack> deductedCoins = new ArrayList<>();
        int remaining = cost;
        for (ItemStack coinStack : coinStacks) {
            if (remaining <= 0) break;

            int toDeduct = Math.min(remaining, coinStack.getCount());
            coinStack.shrink(toDeduct);
            remaining -= toDeduct;

            // Store the deducted amount as a new ItemStack for potential refund
            deductedCoins.add(new ItemStack(slotCoinItem, toDeduct));
        }

        return deductedCoins;
    }

    /**
     * Rolls a random unlock from the given category and unlocks it for the player.
     * @param player The player to grant the unlock for
     * @param category The category to roll unlocks from
     * @return true if an unlock was granted, false otherwise
     */
    private static boolean rollUnlocks(Player player, UnlockCategory category) {
        var unlockData = UnlockData.get();
        var unlockTree = unlockData.getUnlockTree(player);
        var unlocks = ConfigRegistry.UNLOCK_CONFIG
                .getAll()
                .stream()
                .filter(unlock -> unlock.getCategory() == category
                        && !unlockTree.isUnlocked(unlock.getName()))
                .toList();

        if (unlocks.isEmpty()) return false;

        var random = new Random(RandomUtils.getSeed());
        var unlock = unlocks.get(random.nextInt(unlocks.size()));
        unlockData.unlock(player, unlock.getName());

        displayUnlockRewards(player, unlock);
        return true;
    }

    private static void displayUnlockRewards(Player player, Unlock unlock) {
        player.sendSystemMessage(Component.literal(String.format("You unlocked \"%s\"!", unlock.getName())));
    }
}
