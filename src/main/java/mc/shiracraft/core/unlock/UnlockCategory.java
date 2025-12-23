package mc.shiracraft.core.unlock;

import net.minecraft.util.StringRepresentable;

public enum UnlockCategory implements StringRepresentable {
    TECHNICAL_MOD("technical_mod"),
    MAGICAL_MOD("magical_mod"),
    FOOD_MOD("food_mod"),
    OTHER("other");

    private final String name;

    UnlockCategory(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
