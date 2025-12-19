package mc.shiracraft.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mc.shiracraft.core.command.arguments.UnlockNameArgument;
import mc.shiracraft.core.registry.ConfigRegistry;
import mc.shiracraft.core.unlock.restriction.RestrictionManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class UnlocksCommand extends Command {

    private static final String UnlockNameArgumentIdentifier = "unlock_name";

    @Override
    public String getName() {
        return "unlocks";
    }

    @Override
    public CommandPermissionLevel getPermissionLevel() {
        return CommandPermissionLevel.EVERYONE;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(literal("unlock")
                        .then(argument("player", EntityArgument.players())
                                .then(argument(UnlockNameArgumentIdentifier, UnlockNameArgument.options())
                                        .executes(this::unlock)
                                )
                        )
                )
                .then(literal("restrict")
                        .then(argument("player", EntityArgument.players())
                                .then(argument(UnlockNameArgumentIdentifier, UnlockNameArgument.options())
                                        .executes(this::restrict))
                        )
                );
    }

    private int unlock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!context.getSource().isPlayer()) return 0;

        var unlockName = getUnlockName(context);
        var player = context.getSource().getPlayer();
        var unlockTree = RestrictionManager.getUnlockTree(player);
        unlockTree.unlock(unlockName);

        player.sendSystemMessage(Component.literal(String.format("You unlocked \"%s\"!", unlockName)));

        return 0;
    }

    private int restrict(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!context.getSource().isPlayer()) return 0;

        var unlockName = getUnlockName(context);
        var player = context.getSource().getPlayer();
        var unlockTree = RestrictionManager.getUnlockTree(player);
        unlockTree.restrict(unlockName);

        player.sendSystemMessage(Component.literal(String.format("Your access to \"%s\" has been restricted!", unlockName)));
        return 0;
    }

    private String getUnlockName(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var unlockName = context.getArgument(UnlockNameArgumentIdentifier, String.class);

        if (ConfigRegistry.UNLOCK_CONFIG.getAll().stream().noneMatch(unlock -> unlock.getName().equals(unlockName))) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
        }

        return unlockName;
    }
}
