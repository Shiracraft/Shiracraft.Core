package mc.shiracraft.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mc.shiracraft.core.command.arguments.UnlockNameArgument;
import mc.shiracraft.core.registry.ConfigRegistry;
import mc.shiracraft.core.world.data.UnlockData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class UnlocksCommand extends Command {

    private static final String UnlockNameArgumentIdentifier = "unlock_name";
    private static final String PlayerArgumentIdentifier = "player";

    @Override
    public String getName() {
        return "unlocks";
    }

    @Override
    public CommandPermissionLevel getPermissionLevel() {
        return CommandPermissionLevel.MODERATOR;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        // TODO: Test restructure of command
        builder.then(argument(PlayerArgumentIdentifier, EntityArgument.players())
                .then(literal("unlock")
                        .then(argument(UnlockNameArgumentIdentifier, UnlockNameArgument.options())
                                .executes(this::unlock)
                        )
                )
                .then(literal("restrict")
                        .then(argument(UnlockNameArgumentIdentifier, UnlockNameArgument.options())
                                .executes(this::restrict)
                        )
                )
                .then(literal("reset")
                        .executes(this::reset)
                )
        );
    }

    private int unlock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!context.getSource().isPlayer()) return 0;

        var unlockName = getUnlockName(context);
        var players = getPlayers(context);
        var unlockData = UnlockData.get();

        players.forEach(player -> {
            unlockData.unlock(player, unlockName);
            player.sendSystemMessage(Component.literal(String.format("You unlocked \"%s\"!", unlockName)));
        });
        return 1;
    }

    private int restrict(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!context.getSource().isPlayer()) return 0;

        var unlockName = getUnlockName(context);
        var players = getPlayers(context);
        var unlockData = UnlockData.get();

        players.forEach(player -> {
            unlockData.restrict(player, unlockName);
            player.sendSystemMessage(Component.literal(String.format("Your access to \"%s\" has been restricted!", unlockName)));
        });
        return 1;
    }

    private int reset(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!context.getSource().isPlayer()) return 0;

        var players = getPlayers(context);
        var unlockData = UnlockData.get();

        players.forEach(player -> {
            unlockData.resetUnlockTree(player);
            player.sendSystemMessage(Component.literal("Your research tree has been reset!"));
        });

        return 1;
    }

    private List<ServerPlayer> getPlayers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var playerArgument = context.getArgument(PlayerArgumentIdentifier, EntitySelector.class);
        return playerArgument.findPlayers(context.getSource());
    }

    private String getUnlockName(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var unlockName = context.getArgument(UnlockNameArgumentIdentifier, String.class);

        if (ConfigRegistry.UNLOCK_CONFIG.getAll().stream().noneMatch(unlock -> unlock.getName().equals(unlockName))) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
        }

        return unlockName;
    }
}
