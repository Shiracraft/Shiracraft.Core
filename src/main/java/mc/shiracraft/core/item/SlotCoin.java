package mc.shiracraft.core.item;

import net.minecraft.world.item.Item;

public class SlotCoin extends Item {
    public static final String ITEM_NAME = "slot_coin";

    private static final Properties PROPERTIES = new Properties()
            .stacksTo(64);

    public SlotCoin() {
        super(PROPERTIES);
    }
}
