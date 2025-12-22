package mc.shiracraft.core.unlock.restriction;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.unlock.UnlockTree;
import mc.shiracraft.core.world.data.UnlockData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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
    public static void onBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCancelable()) return;

        var player = event.getEntity();
        var unlockTree = getUnlockTree(player);

        String restrictedBy;

        var blockState = player.level().getBlockState(event.getPos());
        var itemStack = event.getItemStack();
        restrictedBy = unlockTree.restrictedBy(blockState.getBlock(), RestrictionType.BLOCK_INTERACTABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                showRestrictionMessage(player, getBlockNameFromState(blockState), restrictedBy, RestrictionType.BLOCK_INTERACTABILITY);
            }
            event.setCanceled(true);
            return;
        }

        if (itemStack == ItemStack.EMPTY) return;

        var item = itemStack.getItem();
        restrictedBy = unlockTree.restrictedBy(item, RestrictionType.USABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                showRestrictionMessage(player, itemStack.getDisplayName().getString(), restrictedBy, RestrictionType.USABILITY);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockHit(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.isCancelable()) return;

        var player = event.getEntity();
        var unlockTree = getUnlockTree(player);

        var blockState = player.level().getBlockState(event.getPos());

        String restrictedBy;

        restrictedBy = unlockTree.restrictedBy(blockState.getBlock(), RestrictionType.HITTABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                showRestrictionMessage(player, getBlockNameFromState(blockState), restrictedBy, RestrictionType.HITTABILITY);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteraction(PlayerInteractEvent.EntityInteract event) {
        if (!event.isCancelable()) return;

        var player = event.getEntity();
        var unlockTree = getUnlockTree(player);
        var entity = event.getTarget();

        String restrictedBy;

        restrictedBy = unlockTree.restrictedBy(entity.getType(), RestrictionType.ENTITY_INTERACTABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                showRestrictionMessage(player, getEntityName(entity), restrictedBy, RestrictionType.ENTITY_INTERACTABILITY);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        validateItemUsability(event);
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        validateItemUsability(event);
    }

    private static String getEntityName(Entity entity) {
        return entity.getType().getDescription().getString();
    }

    private static String getBlockNameFromState(BlockState blockState) {
        return blockState.getBlock().getName().getString();
    }

    private static void validateItemUsability(PlayerInteractEvent event) {
        if (!event.isCancelable()) return;

        var player = event.getEntity();
        var unlockTree = getUnlockTree(player);
        var usedItem = event.getItemStack().getItem();
        var restrictedBy = unlockTree.restrictedBy(usedItem, RestrictionType.USABILITY);

        if (restrictedBy == null)
            return; // Doesn't restrict usability of this item, so stop here.

        var itemName = event.getItemStack().getDisplayName().getString();

        if (event.getSide() == LogicalSide.CLIENT) {
            showRestrictionMessage(player, itemName, restrictedBy, RestrictionType.USABILITY);
        }

        event.setCanceled(true);
    }

    private static void showRestrictionMessage(Player player, String objectName, String restrictedBy, RestrictionType restrictionType) {
        // TODO: Support localization
        var targetAction = switch (restrictionType) {
            case USABILITY -> "use";
            case BLOCK_INTERACTABILITY, ENTITY_INTERACTABILITY -> "interact with";
            case CRAFTABILITY -> "craft";
            case HITTABILITY -> "hit";
        };

        player.displayClientMessage(
                Component.literal(
                        String.format("You cannot %s %s right now. You need to unlock \"%s\" first!",
                                targetAction,
                                objectName,
                                restrictedBy)
                ), true);
    }
}
