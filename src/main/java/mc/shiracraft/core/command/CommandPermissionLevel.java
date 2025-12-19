package mc.shiracraft.core.command;

public enum CommandPermissionLevel {
    EVERYONE(0),
    MODERATOR(1),
    GAME_MASTER(2),
    ADMIN(3),
    OWNER(4);

    public final int value;

    CommandPermissionLevel(int i) {
        this.value = i;
    }
}
