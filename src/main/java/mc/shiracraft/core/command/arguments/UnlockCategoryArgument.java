package mc.shiracraft.core.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import mc.shiracraft.core.unlock.UnlockCategory;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UnlockCategoryArgument implements ArgumentType<UnlockCategory> {

    private static final DynamicCommandExceptionType INVALID_CATEGORY = new DynamicCommandExceptionType(
            category -> Component.literal("Invalid unlock category: " + category)
    );

    public static UnlockCategoryArgument options() {
        return new UnlockCategoryArgument();
    }

    @Override
    public UnlockCategory parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.readString().toUpperCase();
        try {
            return UnlockCategory.valueOf(input);
        } catch (IllegalArgumentException e) {
            throw INVALID_CATEGORY.create(input);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String remaining = builder.getRemaining().toUpperCase();

        for (UnlockCategory category : UnlockCategory.values()) {
            String categoryName = category.name();
            if (categoryName.startsWith(remaining)) {
                builder.suggest(categoryName);
            }
        }

        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.stream(UnlockCategory.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
