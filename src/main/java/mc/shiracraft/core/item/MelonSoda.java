package mc.shiracraft.core.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.NotNull;

public class MelonSoda extends Item {
    public static final String ITEM_NAME = "melon_soda";
    private static final Properties PROPERTIES = new Properties()
            .food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.2f)
                    .build()
            );
    public MelonSoda() {
        super(PROPERTIES);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.DRINK;
    }
}
