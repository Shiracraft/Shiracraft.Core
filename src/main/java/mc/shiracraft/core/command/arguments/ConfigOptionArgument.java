package mc.shiracraft.core.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ConfigOptionArgument implements ArgumentType<String> {

    // In the future this might need to be replaced with reflection on the config registry and the .getName() method of each config.
    private static final Collection<String> EXAMPLES = Arrays.asList("unlock", "rollchance", "all");
    private static final DynamicCommandExceptionType INVALID_CONFIG = new DynamicCommandExceptionType(
            config -> Component.literal("Invalid config option: " + config)
    );

    public static ConfigOptionArgument options() {
        return new ConfigOptionArgument();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.readString().toLowerCase();

        if (!isValidOption(input)) {
            throw INVALID_CONFIG.create(input);
        }

        return input;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String remaining = builder.getRemaining().toLowerCase();

        for (String option : Arrays.asList("unlock", "rollchance", "all")) {
            if (option.startsWith(remaining)) {
                builder.suggest(option);
            }
        }

        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    private boolean isValidOption(String option) {
        return option.equals("unlock") || option.equals("rollchance") || option.equals("all");
    }
}

