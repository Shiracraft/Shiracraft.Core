package mc.shiracraft.core.registry;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.command.arguments.ConfigOptionArgument;
import mc.shiracraft.core.command.arguments.UnlockCategoryArgument;
import mc.shiracraft.core.command.arguments.UnlockNameArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CommandArgumentRegistry {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENTS =
            DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, Core.MOD_ID);

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> UNLOCK_NAME_ARGUMENT =
            COMMAND_ARGUMENTS.register("unlock_name", () ->
                    ArgumentTypeInfos.registerByClass(UnlockNameArgument.class,
                            SingletonArgumentInfo.contextFree(UnlockNameArgument::options)));

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> UNLOCK_CATEGORY_ARGUMENT =
            COMMAND_ARGUMENTS.register("unlock_category", () ->
                    ArgumentTypeInfos.registerByClass(UnlockCategoryArgument.class,
                            SingletonArgumentInfo.contextFree(UnlockCategoryArgument::options)));

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> CONFIG_OPTION_ARGUMENT =
            COMMAND_ARGUMENTS.register("config_option", () ->
                    ArgumentTypeInfos.registerByClass(ConfigOptionArgument.class,
                            SingletonArgumentInfo.contextFree(ConfigOptionArgument::options)));

    public static void register(IEventBus eventBus) {
        COMMAND_ARGUMENTS.register(eventBus);
    }
}
