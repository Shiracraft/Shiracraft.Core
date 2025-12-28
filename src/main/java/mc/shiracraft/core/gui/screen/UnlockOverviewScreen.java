package mc.shiracraft.core.gui.screen;

import mc.shiracraft.core.registry.ConfigRegistry;
import mc.shiracraft.core.unlock.Unlock;
import mc.shiracraft.core.unlock.UnlockCategory;
import mc.shiracraft.core.world.data.UnlockData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnlockOverviewScreen extends Screen {

    private static final int BACKGROUND_COLOR = 0xC0101010;
    private static final int TITLE_COLOR = 0xFFFFFF;
    private static final int UNLOCKED_COLOR = 0x55FF55;
    private static final int LOCKED_COLOR = 0xFF5555;
    private static final int CATEGORY_COLOR = 0xFFAA00;

    private final Player player;

    public UnlockOverviewScreen(Player player) {
        super(Component.literal("Unlock Overview"));
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Render dark background
        guiGraphics.fill(0, 0, this.width, this.height, BACKGROUND_COLOR);

        // Render title
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 10, TITLE_COLOR);

        // Render unlock information
        renderUnlockInfo(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderUnlockInfo(GuiGraphics guiGraphics) {
        var unlockData = UnlockData.get();
        var unlockTree = unlockData.getUnlockTree(player);
        var allUnlocks = ConfigRegistry.UNLOCK_CONFIG.getAll();

        // Group unlocks by category
        Map<UnlockCategory, List<Unlock>> unlocksByCategory = allUnlocks.stream()
                .collect(Collectors.groupingBy(Unlock::getCategory));

        int y = 30;
        int x = 20;

        // Display unlocks grouped by category
        for (UnlockCategory category : UnlockCategory.values()) {
            List<Unlock> categoryUnlocks = unlocksByCategory.get(category);

            if (categoryUnlocks != null && !categoryUnlocks.isEmpty()) {
                // Draw category header
                guiGraphics.drawString(this.font, category.getDisplayName() + ":", x, y, CATEGORY_COLOR);
                y += 15;

                // Draw unlocks
                for (Unlock unlock : categoryUnlocks) {
                    boolean isUnlocked = unlockTree.isUnlocked(unlock.getName());
                    int color = isUnlocked ? UNLOCKED_COLOR : LOCKED_COLOR;
                    String status = isUnlocked ? "✓" : "✗";

                    guiGraphics.drawString(this.font, "  " + status + " " + unlock.getName(), x, y, color);
                    y += 12;
                }

                y += 10; // Extra space between categories
            }
        }

        // Display progress
        long unlockedCount = allUnlocks.stream()
                .filter(unlock -> unlockTree.isUnlocked(unlock.getName()))
                .count();
        long totalCount = allUnlocks.size();
        double percentage = totalCount > 0 ? (unlockedCount * 100.0 / totalCount) : 0;

        String progressText = String.format("Progress: %d/%d (%.1f%%)", unlockedCount, totalCount, percentage);
        guiGraphics.drawString(this.font, progressText, x, this.height - 30, TITLE_COLOR);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
