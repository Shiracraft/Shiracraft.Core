package mc.shiracraft.core.unlock;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum UnlockCategory implements StringRepresentable {
    TECHNICAL_MOD("technical_mod", "Technical Mods"),
    MAGICAL_MOD("magical_mod", "Magical Mods"),
    FOOD_MOD("food_mod", "Food Mods"),
    OTHER("other", "Other");

    private final String name;
    private final String displayName;

    UnlockCategory(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
