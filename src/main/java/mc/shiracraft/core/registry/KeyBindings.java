package mc.shiracraft.core.registry;

import com.mojang.blaze3d.platform.InputConstants;
import mc.shiracraft.core.Core;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final String KEY_CATEGORY = "key.category." + Core.MOD_ID;

    public static final KeyMapping OPEN_UNLOCK_SCREEN = new KeyMapping(
            "key." + Core.MOD_ID + ".open_unlock_screen",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            KEY_CATEGORY
    );
}

