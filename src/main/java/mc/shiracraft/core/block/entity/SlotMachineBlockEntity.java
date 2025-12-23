package mc.shiracraft.core.block.entity;

import mc.shiracraft.core.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SlotMachineBlockEntity extends BlockEntity {

    public SlotMachineBlockEntity(BlockPos position, BlockState state) {
        this(BlockRegistry.SLOT_MACHINE_BLOCK_ENTITY.get(), position, state);
    }

    public SlotMachineBlockEntity(BlockEntityType<?> entityType, BlockPos position, BlockState state) {
        super(entityType, position, state);
    }


}
