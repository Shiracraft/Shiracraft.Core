package mc.shiracraft.core.unlock.restriction;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.unlock.UnlockTree;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Core.MOD_ID)
public class RestrictionManager {

    public static UnlockTree UNLOCK_TREE;

    public static UnlockTree getUnlockTree(Player player) {
        // TODO: Implement getting unlock tree from world data
        return UNLOCK_TREE == null
                ? new UnlockTree(player.getUUID())
                : UNLOCK_TREE;
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (!event.isCancelable()) return;

        Player player = event.getEntity();
        UnlockTree researchTree = getUnlockTree(player);

        Item usedItem = event.getItemStack().getItem();

        String restrictedBy = researchTree.restrictedBy(usedItem, RestrictionType.USABILITY);

        if (restrictedBy == null)
            return; // Doesn't restrict usability of this item, so stop here.

        if (event.getSide() == LogicalSide.CLIENT) {
            player.sendSystemMessage(Component.literal(String.format("You cannot use %s right now. You need to unlock \"%s\" first!", event.getItemStack().getDisplayName().getString(), restrictedBy)));
            Core.LOGGER.warn("Item {} is restricted by {}", usedItem.getDescriptionId(), restrictedBy);
        }

        event.setCanceled(true);
    }

}
