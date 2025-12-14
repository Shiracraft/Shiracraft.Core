package mc.shiracraft.core.registries;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.items.PityToken;
import mc.shiracraft.core.items.SlotCoin;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Core.MOD_ID);

    protected static RegistryObject<Item> SLOT_COIN = ITEMS.register(SlotCoin.ITEM_NAME, SlotCoin::new);
    protected static RegistryObject<Item> PITY_TOKEN = ITEMS.register(PityToken.ITEM_NAME, PityToken::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        Core.LOGGER.info("Item Registry Initialized. Registered {} Item(s).", ITEMS.getEntries().size());
    }
}
