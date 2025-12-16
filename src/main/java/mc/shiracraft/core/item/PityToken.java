package mc.shiracraft.core.item;

import net.minecraft.world.item.Item;

public class PityToken extends Item {
    public static final String ITEM_NAME = "pity_token";

    private static final Properties PROPERTIES = new Properties()
            .stacksTo(64);

    public PityToken() {
        super(PROPERTIES);
    }
}
