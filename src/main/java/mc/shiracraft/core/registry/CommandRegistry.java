package mc.shiracraft.core.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import mc.shiracraft.core.Core;
import mc.shiracraft.core.command.Command;
import mc.shiracraft.core.command.ConfigsCommand;
import mc.shiracraft.core.command.SlotsCommand;
import mc.shiracraft.core.command.UnlocksCommand;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Supplier;

import static net.minecraft.commands.Commands.literal;

public class CommandRegistry {
    public static UnlocksCommand UNLOCK_COMMAND;
    public static SlotsCommand SLOTS_COMMAND;
    public static ConfigsCommand CONFIGS_COMMAND;

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        UNLOCK_COMMAND = registerCommand(UnlocksCommand::new, dispatcher);
        SLOTS_COMMAND = registerCommand(SlotsCommand::new, dispatcher);
        CONFIGS_COMMAND = registerCommand(ConfigsCommand::new, dispatcher);
    }

    public static <T extends Command> T registerCommand(Supplier<T> supplier, CommandDispatcher<CommandSourceStack> dispatcher) {
        T command = supplier.get();

        LiteralArgumentBuilder<CommandSourceStack> builder = literal(command.getName());
        builder.requires((sender) -> sender.hasPermission(command.getPermissionLevel().value));
        command.build(builder);
        dispatcher.register(literal(Core.MOD_ID).then(builder));

        return command;
    }
}
