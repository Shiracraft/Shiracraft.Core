package mc.shiracraft.core.unlock;

import mc.shiracraft.core.registry.ItemRegistry;
import mc.shiracraft.core.world.data.UnlockData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Server-side-ish domain logic for purchasing unlocks.
 * <p>
 * The GUI should only detect the click and delegate here.
 */
public final class UnlockPurchaseService {

    private UnlockPurchaseService() {
    }

    public enum PurchaseResult {
        SUCCESS,
        ALREADY_UNLOCKED,
        NOT_ENOUGH_TOKENS,
        INVALID_REQUEST
    }

    /**
     * Attempts to purchase an {@link Unlock} for a player by taking pity tokens and unlocking it.
     *
     * @param player the player purchasing
     * @param unlock the unlock to purchase
     * @param price  the token price
     * @return result enum
     */
    public static PurchaseResult tryPurchase(Player player, Unlock unlock, int price) {
        if (player == null || unlock == null) return PurchaseResult.INVALID_REQUEST;
        if (price <= 0) return PurchaseResult.INVALID_REQUEST;

        // This must run on the logical server. Running on the client would only update a local copy
        // and the server (commands, restrictions, persistence) would never see it.
        if (player.getServer() == null) {
            return PurchaseResult.INVALID_REQUEST;
        }

        var unlockData = UnlockData.get();
        var unlockTree = unlockData.getUnlockTree(player);

        if (unlockTree.isUnlocked(unlock.getName())) {
            return PurchaseResult.ALREADY_UNLOCKED;
        }

        int available = countItem(player, ItemRegistry.PITY_TOKEN.get().getDefaultInstance());
        if (available < price) {
            return PurchaseResult.NOT_ENOUGH_TOKENS;
        }

        // Use vanilla removal so both server state and client sync behave like a normal item consume.
        player.getInventory().clearOrCountMatchingItems(
                stack -> ItemStack.isSameItemSameTags(stack, ItemRegistry.PITY_TOKEN.get().getDefaultInstance()),
                price,
                player.inventoryMenu.getCraftSlots()
        );

        player.getInventory().setChanged();
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.inventoryMenu.broadcastChanges();
        }

        unlockData.unlock(player, unlock.getName());

        player.sendSystemMessage(Component.literal("Unlocked \"" + unlock.getName() + "\"!"));
        return PurchaseResult.SUCCESS;
    }

    private static int countItem(Player player, ItemStack match) {
        int total = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && ItemStack.isSameItemSameTags(stack, match)) {
                total += stack.getCount();
            }
        }
        return total;
    }
}
