package mc.shiracraft.core.registry;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.block.SlotMachineBlock;
import mc.shiracraft.core.block.entity.SlotMachineBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Core.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Core.MOD_ID);

    public static final RegistryObject<Block> SLOT_MACHINE_BLOCK = BLOCKS.register(SlotMachineBlock.BLOCK_NAME, SlotMachineBlock::new);

    public static final RegistryObject<BlockEntityType<SlotMachineBlockEntity>> SLOT_MACHINE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            SlotMachineBlock.BLOCK_NAME,
            () -> BlockEntityType.Builder.of(SlotMachineBlockEntity::new, SLOT_MACHINE_BLOCK.get()).build(null)
    );

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        Core.LOGGER.info("Block Registry Initialized. Registered {} Block(s) and {} Block Entit(ies).", BLOCKS.getEntries().size(), BLOCK_ENTITIES.getEntries().size());
    }
}
