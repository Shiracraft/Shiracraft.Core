package mc.shiracraft.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import mc.shiracraft.core.casino.SlotMachine;
import mc.shiracraft.core.command.arguments.UnlockCategoryArgument;
import mc.shiracraft.core.unlock.UnlockCategory;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.argument;

public class SlotsCommand extends Command {
    private static final String CategoryArgumentIdentifier = "category";

    @Override
    public String getName() {
        return "slots";
    }

    @Override
    public CommandPermissionLevel getPermissionLevel() {
        return CommandPermissionLevel.MODERATOR;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(argument(CategoryArgumentIdentifier, UnlockCategoryArgument.options())
                .executes(this::rollSlots)
        );
    }

    private int rollSlots(CommandContext<CommandSourceStack> context) {
        if (!context.getSource().isPlayer()) return 0;

        var player = context.getSource().getPlayer();
        var unlockCategory = context.getArgument(CategoryArgumentIdentifier, UnlockCategory.class);
        SlotMachine.rollSlots(player, unlockCategory);
        return 1;
    }
}
