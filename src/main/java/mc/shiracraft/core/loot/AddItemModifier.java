package mc.shiracraft.core.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {

    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(instance -> codecStart(instance)
                    .and(ForgeRegistries.ITEMS.getCodec()
                            .fieldOf("item")
                            .forGetter(m -> m.item))
                    .and(Codec.INT
                            .optionalFieldOf("drop_amount", 1)
                            .forGetter(m -> m.dropAmount))
                    .and(Codec.BOOL
                            .optionalFieldOf("randomise_amount", false)
                            .forGetter(m -> m.randomiseAmount))
                    .apply(instance, AddItemModifier::new)
            )
    );

    private final Item item;
    private final int dropAmount;
    private final boolean randomiseAmount;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item) {
        this(conditionsIn, item, 1);
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, int dropAmount) {
        this(conditionsIn, item, dropAmount, false);
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, int dropAmount, boolean randomiseAmount) {
        super(conditionsIn);
        this.item = item;
        this.dropAmount = dropAmount;
        this.randomiseAmount = randomiseAmount;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Calculate the actual drop amount
        int amount = this.dropAmount;
        if (this.randomiseAmount && amount > 1) {
            // Generate a random amount between 1 and dropAmount (inclusive)
            amount = context.getRandom().nextInt(amount) + 1;
        }

        generatedLoot.add(new ItemStack(this.item, amount));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
