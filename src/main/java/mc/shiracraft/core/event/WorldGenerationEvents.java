package mc.shiracraft.core.event;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.world.SlotMachineSpawnGenerator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles world generation events for placing slot machines at spawn.
 * TODO: Temporary world gen for testing, needs to be replaced or removed in the final product.
 */
@Mod.EventBusSubscriber(modid = Core.MOD_ID)
public class WorldGenerationEvents {

    /**
     * Called when a level is loaded. This is where we generate slot machines at spawn.
     * This event fires once when a world is first created/loaded.
     */
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        // Only run on server side and only for the Overworld
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        // Only generate in the Overworld
        if (!serverLevel.dimension().equals(Level.OVERWORLD)) {
            return;
        }

        Core.LOGGER.info("Overworld loaded, checking for slot machine generation...");

        // Get the slot machine generator and attempt to generate
        SlotMachineSpawnGenerator generator = SlotMachineSpawnGenerator.get(serverLevel);
        generator.generateSlotMachines(serverLevel);
    }
}

