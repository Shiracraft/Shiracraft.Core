package mc.shiracraft.core.block;

import mc.shiracraft.core.block.entity.SlotMachineBlockEntity;
import mc.shiracraft.core.casino.SlotMachine;
import mc.shiracraft.core.unlock.UnlockCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlotMachineBlock extends Block implements EntityBlock {
    public static final Properties PROPERTIES = Properties.of();
    public static final String BLOCK_NAME = "slot_machine";
    public static final EnumProperty<UnlockCategory> CATEGORY = EnumProperty.create("category", UnlockCategory.class);

    public SlotMachineBlock() {
        super(PROPERTIES);
        registerDefaultState(getStateDefinition().any().setValue(CATEGORY, UnlockCategory.TECHNICAL_MOD));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CATEGORY);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SlotMachineBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    // Warning is suppressed because the new useWithoutItem method doesn't exist in 1.20.1 yet.
    public @NotNull InteractionResult use(
            @NotNull BlockState state, Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit
    ) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SlotMachineBlockEntity) {
                UnlockCategory category = state.getValue(CATEGORY);
                SlotMachine.rollSlots(player, category);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
