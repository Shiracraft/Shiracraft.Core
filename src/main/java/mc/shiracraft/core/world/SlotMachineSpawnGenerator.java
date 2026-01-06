package mc.shiracraft.core.world;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.registry.BlockRegistry;
import mc.shiracraft.core.unlock.UnlockCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;

import static mc.shiracraft.core.block.SlotMachineBlock.CATEGORY;

/**
 * Handles the generation of slot machines at world spawn.
 * Ensures one slot machine per category is generated on the surface near spawn.
 */
public class SlotMachineSpawnGenerator extends SavedData {
    private static final String DATA_NAME = Core.MOD_ID + "_slot_machine_spawn";
    private boolean hasGenerated = false;

    public SlotMachineSpawnGenerator() {
    }

    public static SlotMachineSpawnGenerator get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                SlotMachineSpawnGenerator::load,
                SlotMachineSpawnGenerator::new,
                DATA_NAME
        );
    }

    public static SlotMachineSpawnGenerator load(net.minecraft.nbt.CompoundTag tag) {
        SlotMachineSpawnGenerator data = new SlotMachineSpawnGenerator();
        data.hasGenerated = tag.getBoolean("HasGenerated");
        return data;
    }

    @Nonnull
    @Override
    public net.minecraft.nbt.CompoundTag save(net.minecraft.nbt.CompoundTag tag) {
        tag.putBoolean("HasGenerated", hasGenerated);
        return tag;
    }

    /**
     * Generates slot machines at world spawn if not already generated.
     * Places one slot machine per category in a line near spawn.
     */
    public void generateSlotMachines(ServerLevel level) {
        if (hasGenerated) {
            Core.LOGGER.info("Slot machines already generated at spawn, skipping...");
            return;
        }

        Core.LOGGER.info("Generating slot machines at world spawn...");

        BlockPos spawnPos = level.getSharedSpawnPos();
        UnlockCategory[] categories = UnlockCategory.values();

        // Place slot machines in a row, spacing them 3 blocks apart
        for (int i = 0; i < categories.length; i++) {
            UnlockCategory category = categories[i];

            // Offset each machine: first one at spawn, then 3 blocks east for each next one
            BlockPos machinePos = spawnPos.offset(i * 3, 0, 0);

            // Find the surface level at this position
            BlockPos surfacePos = findSurfaceLevel(level, machinePos);

            if (surfacePos != null) {
                placeSlotMachine(level, surfacePos, category);
                Core.LOGGER.info("Placed {} slot machine at {}", category.getDisplayName(), surfacePos);
            } else {
                Core.LOGGER.warn("Could not find suitable surface for {} slot machine at {}", category.getDisplayName(), machinePos);
            }
        }

        hasGenerated = true;
        setDirty();
        Core.LOGGER.info("Finished generating slot machines at spawn");
    }

    /**
     * Finds the surface level at the given x,z coordinates.
     * Searches downward from max build height and upward from min build height.
     */
    private BlockPos findSurfaceLevel(ServerLevel level, BlockPos startPos) {
        int x = startPos.getX();
        int z = startPos.getZ();

        // Start from a reasonable height and search downward for the first solid block
        int maxY = level.getMaxBuildHeight() - 1;
        int minY = level.getMinBuildHeight();

        for (int y = maxY; y >= minY; y--) {
            BlockPos checkPos = new BlockPos(x, y, z);
            BlockState blockState = level.getBlockState(checkPos);
            BlockPos abovePos = checkPos.above();
            BlockState aboveState = level.getBlockState(abovePos);

            // Found a solid block with air above it - this is the surface
            if (!blockState.isAir() && blockState.isSolidRender(level, checkPos) && aboveState.isAir()) {
                return abovePos; // Return the air block position where we'll place the machine
            }
        }

        return null;
    }

    /**
     * Places a slot machine block with the specified category at the given position.
     * Also spawns an invisible armor stand with the category name above it.
     */
    private void placeSlotMachine(ServerLevel level, BlockPos pos, UnlockCategory category) {
        BlockState slotMachineState = BlockRegistry.SLOT_MACHINE_BLOCK.get()
                .defaultBlockState()
                .setValue(CATEGORY, category);

        // Clear any existing block
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        // Place the slot machine
        level.setBlock(pos, slotMachineState, 3);

        // Spawn an invisible armor stand with the category name above the slot machine
        spawnCategoryLabel(level, pos, category);
    }

    /**
     * Spawns an invisible armor stand with the category name as a label.
     * The armor stand is placed 2 blocks above the slot machine for visibility.
     */
    private void spawnCategoryLabel(ServerLevel level, BlockPos machinePos, UnlockCategory category) {
        // Create the armor stand
        ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, level);

        // Position it 2 blocks above the slot machine (centered)
        double x = machinePos.getX() + 0.5;
        double y = machinePos.getY() + 0.5;
        double z = machinePos.getZ() + 0.5;
        armorStand.setPos(x, y, z);

        // Make it invisible
        armorStand.setInvisible(true);

        // Set the custom name (category display name) with gold color and bold
        armorStand.setCustomName(Component.literal("§6§l" + category.getDisplayName()));
        armorStand.setCustomNameVisible(true);

        // Make it invulnerable and prevent it from being affected by physics
        armorStand.setInvulnerable(true);
        armorStand.setNoGravity(true);
        armorStand.setSilent(true);

        // Disable all visual elements
        armorStand.setShowArms(false);
        armorStand.setNoBasePlate(true);

        // Add the armor stand to the world
        level.addFreshEntity(armorStand);

        Core.LOGGER.info("Spawned label armor stand for {} at ({}, {}, {})",
                category.getDisplayName(), x, y, z);
    }
}

