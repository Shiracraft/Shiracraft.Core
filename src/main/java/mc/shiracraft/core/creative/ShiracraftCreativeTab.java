package mc.shiracraft.core.creative;

import mc.shiracraft.core.items.SlotCoin;
import mc.shiracraft.core.registries.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;

public class ShiracraftCreativeTab extends CreativeModeTab {
    private static final CreativeModeTab.Builder BUILDER = CreativeModeTab.builder()
            .icon(() -> new SlotCoin().getDefaultInstance())
            .displayItems((parameters, output) ->
                    ItemRegistry.ITEMS.getEntries().forEach(item ->
                            output.accept(item.get())
                    )
            );

    public ShiracraftCreativeTab() {
        super(BUILDER);
    }
}
