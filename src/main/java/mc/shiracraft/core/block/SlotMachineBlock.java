package mc.shiracraft.core.block;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.block.entity.SlotMachineBlockEntity;
import mc.shiracraft.core.unlock.UnlockCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SlotMachineBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SlotMachineBlockEntity) {
                // TODO: Implement slot machine logic here and trigger animations/sounds as needed
                UnlockCategory category = state.getValue(CATEGORY);
                Core.LOGGER.info("Slot Machine interacted by {} at {} with category: {}",
                    player.getName().getString(), pos, category);
                player.sendSystemMessage(Component.literal("Slot Machine activated! Category: " + category.name()));
            }
        }
        return InteractionResult.SUCCESS;
    }
}
