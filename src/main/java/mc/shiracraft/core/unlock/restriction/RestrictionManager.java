package mc.shiracraft.core.unlock.restriction;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.unlock.UnlockTree;
import mc.shiracraft.core.world.data.UnlockData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Core.MOD_ID)
public class RestrictionManager {

    public static UnlockTree UNLOCK_TREE;

    public static UnlockTree getUnlockTree(Player player) {
        var level = player.level();
        if (!level.isClientSide()) {
            return UNLOCK_TREE != null
                    ? UNLOCK_TREE
                    : new UnlockTree(player.getUUID());
        } else {
            return UnlockData.get()
                    .getUnlockTree(player);
        }
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (!event.isCancelable()) return;

        var player = event.getEntity();
        var researchTree = getUnlockTree(player);
        var usedItem = event.getItemStack().getItem();
        var restrictedBy = researchTree.restrictedBy(usedItem, RestrictionType.USABILITY);

        if (restrictedBy == null)
            return; // Doesn't restrict usability of this item, so stop here.

        if (event.getSide() == LogicalSide.CLIENT) {
            player.displayClientMessage(
                    Component.literal(
                            String.format("You cannot use %s right now. You need to unlock \"%s\" first!",
                                    event.getItemStack().getDisplayName().getString(),
                                    restrictedBy)
                    ), true);
        }

        event.setCanceled(true);
    }

}
