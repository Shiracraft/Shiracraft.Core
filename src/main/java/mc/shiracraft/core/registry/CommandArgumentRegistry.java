package mc.shiracraft.core.registry;

import mc.shiracraft.core.Core;
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

    public static void register(IEventBus eventBus) {
        COMMAND_ARGUMENTS.register(eventBus);
    }
}
