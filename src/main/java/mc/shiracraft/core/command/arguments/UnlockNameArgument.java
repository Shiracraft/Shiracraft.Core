package mc.shiracraft.core.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import mc.shiracraft.core.registry.ConfigRegistry;
import mc.shiracraft.core.unlock.Unlock;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UnlockNameArgument implements ArgumentType<String> {

    public static UnlockNameArgument options() {
        return new UnlockNameArgument();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public Collection<String> getExamples() {
        return ConfigRegistry.UNLOCK_CONFIG.getAll().stream()
                .map(Unlock::getName)
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Collection<String> unlockNames = ConfigRegistry.UNLOCK_CONFIG.getAll().stream()
                .map(Unlock::getName)
                .toList();

        for (String unlockName : unlockNames) {
            if (unlockName.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(unlockName);
            }
        }

        return builder.buildFuture();
    }
}
