package mc.shiracraft.core.registry;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.block.SlotMachineBlock;
import mc.shiracraft.core.item.MelonSoda;
import mc.shiracraft.core.item.PityToken;
import mc.shiracraft.core.item.SlotCoin;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Core.MOD_ID);

    public static RegistryObject<Item> SLOT_COIN = ITEMS.register(SlotCoin.ITEM_NAME, SlotCoin::new);
    public static RegistryObject<Item> PITY_TOKEN = ITEMS.register(PityToken.ITEM_NAME, PityToken::new);
    public static RegistryObject<Item> MELON_SODA = ITEMS.register(MelonSoda.ITEM_NAME, MelonSoda::new);

    // Block Items
    public static RegistryObject<Item> SLOT_MACHINE_BLOCK_ITEM = ITEMS.register(
            SlotMachineBlock.BLOCK_NAME,
            () -> new BlockItem(BlockRegistry.SLOT_MACHINE_BLOCK.get(), new Item.Properties())
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        Core.LOGGER.info("Item Registry Initialized. Registered {} Item(s).", ITEMS.getEntries().size());
    }
}
