package mc.shiracraft.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import mc.shiracraft.core.Core;
import mc.shiracraft.core.command.arguments.ConfigOptionArgument;
import mc.shiracraft.core.config.RollChanceConfig;
import mc.shiracraft.core.config.UnlockConfig;
import mc.shiracraft.core.registry.ConfigRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ConfigsCommand extends Command {

    private static final String ConfigOptionArgumentIdentifier = "config_option";

    @Override
    public String getName() {
        return "configs";
    }

    @Override
    public CommandPermissionLevel getPermissionLevel() {
        return CommandPermissionLevel.MODERATOR;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(literal("reload")
                .then(argument(ConfigOptionArgumentIdentifier, ConfigOptionArgument.options())
                        .executes(this::reloadConfig)
                )
        );

        builder.then(literal("reset")
                .then(argument(ConfigOptionArgumentIdentifier, ConfigOptionArgument.options())
                        .executes(this::resetConfig)
                )
        );
    }

    private int reloadConfig(CommandContext<CommandSourceStack> context) {
        return executeConfigAction(context, "reload", "Reloading", "reloaded");
    }

    private int resetConfig(CommandContext<CommandSourceStack> context) {
        return executeConfigAction(context, "reset", "Resetting", "reset to defaults");
    }

    private int executeConfigAction(CommandContext<CommandSourceStack> context, String action,
                                     String actioningVerb, String completedVerb) {
        var source = context.getSource();
        var configOption = context.getArgument(ConfigOptionArgumentIdentifier, String.class);

        logConfigAction(source, action, configOption);
        source.sendSystemMessage(Component.literal("§e" + actioningVerb + " configuration(s)..."));

        try {
            performConfigAction(action, configOption);

            var configName = getConfigDisplayName(configOption);
            var message = String.format("§a%s configuration %s successfully!", configName, completedVerb);

            broadcastMessage(source, message);
            Core.LOGGER.info("{} configuration {} by {}", configName, completedVerb, getExecutorName(source));

            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("§cFailed to " + action + " configuration: " + e.getMessage()));
            Core.LOGGER.error("Failed to {} configuration", action, e);
            return 0;
        }
    }

    private void performConfigAction(String action, String configOption) {
        switch (action) {
            case "reload" -> performReload(configOption);
            case "reset" -> performReset(configOption);
        }
    }

    private void performReload(String configOption) {
        switch (configOption) {
            case "unlock" -> ConfigRegistry.UNLOCK_CONFIG = new UnlockConfig().readConfig();
            case "rollchance" -> ConfigRegistry.ROLL_CHANCE_CONFIG = new RollChanceConfig().readConfig();
            case "all" -> ConfigRegistry.reloadConfigs();
        }
    }

    private void performReset(String configOption) {
        switch (configOption) {
            case "unlock" -> {
                ConfigRegistry.UNLOCK_CONFIG = new UnlockConfig();
                ConfigRegistry.UNLOCK_CONFIG.generateConfig();
                ConfigRegistry.UNLOCK_CONFIG = ConfigRegistry.UNLOCK_CONFIG.readConfig();
            }
            case "rollchance" -> {
                ConfigRegistry.ROLL_CHANCE_CONFIG = new RollChanceConfig();
                ConfigRegistry.ROLL_CHANCE_CONFIG.generateConfig();
                ConfigRegistry.ROLL_CHANCE_CONFIG = ConfigRegistry.ROLL_CHANCE_CONFIG.readConfig();
            }
            case "all" -> {
                ConfigRegistry.UNLOCK_CONFIG = new UnlockConfig();
                ConfigRegistry.UNLOCK_CONFIG.generateConfig();
                ConfigRegistry.ROLL_CHANCE_CONFIG = new RollChanceConfig();
                ConfigRegistry.ROLL_CHANCE_CONFIG.generateConfig();
                ConfigRegistry.reloadConfigs();
            }
        }
    }

    private String getConfigDisplayName(String configOption) {
        return switch (configOption) {
            case "unlock" -> "Unlock";
            case "rollchance" -> "Roll Chance";
            case "all" -> "All";
            default -> configOption;
        };
    }

    private void logConfigAction(CommandSourceStack source, String action, String configOption) {
        var executor = getExecutorName(source);
        Core.LOGGER.info("Configuration {} action initiated by {} for: {}", action, executor, configOption);
    }

    private String getExecutorName(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            return player.getName().getString() + " (UUID: " + player.getUUID() + ")";
        }
        return "Server Console";
    }

    private void broadcastMessage(CommandSourceStack source, String message) {
        source.getServer().getPlayerList().getPlayers().forEach(player -> {
            if (player.hasPermissions(CommandPermissionLevel.MODERATOR.value)) {
                player.sendSystemMessage(Component.literal(message));
            }
        });

        // Don't broadcast to the sender if they're a player. This should be handled above.
        if (source.getEntity() instanceof ServerPlayer) return;

        source.sendSystemMessage(Component.literal(message));
    }
}
