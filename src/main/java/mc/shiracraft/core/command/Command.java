package mc.shiracraft.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public abstract class Command {
    public abstract String getName();
    public abstract CommandPermissionLevel getPermissionLevel();
    public abstract void build(LiteralArgumentBuilder<CommandSourceStack> builder);
}
