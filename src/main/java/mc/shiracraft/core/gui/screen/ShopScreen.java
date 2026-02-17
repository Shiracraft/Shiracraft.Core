package mc.shiracraft.core.gui.screen;

import mc.shiracraft.core.registry.ConfigRegistry;
import mc.shiracraft.core.registry.ItemRegistry;
import mc.shiracraft.core.unlock.Unlock;
import mc.shiracraft.core.unlock.UnlockCategory;
import mc.shiracraft.core.unlock.types.ModUnlock;
import mc.shiracraft.core.world.data.UnlockData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fixed-size shop window with an inner scrollable/pannable canvas (similar to the advancements screen).
 * This is a visual-only MVP: it renders categories (tabs), mod "cards" on the canvas, and hover tooltips.
 * Purchase logic and networking will be added later.
 */
public class ShopScreen extends Screen {

    // Desired window sizing relative to the screen.
    // Kept conservative so it doesn't overtake the entire screen on small displays.
    private static final float WINDOW_W_FRAC = 0.70f;
    private static final float WINDOW_H_FRAC = 0.65f;

    private static final int WINDOW_W_MIN = 276;
    private static final int WINDOW_H_MIN = 196;

    // Hard upper bounds so it stays readable and doesn't get absurdly large.
    private static final int WINDOW_W_MAX = 460;
    private static final int WINDOW_H_MAX = 320;

    // Tabs: make them big enough for 16x16 item icons + padding
    private static final int TAB_W = 32;
    private static final int TAB_H = 32;
    private static final int TAB_GAP = 4;

    // Layout constants
    private static final int WINDOW_PAD = 8;
    private static final int HEADER_H = 46; // title + tabs
    private static final int FOOTER_H = 22; // pity token line

    // Placeholder pricing (until you add prices to config)
    private static final int DEFAULT_PRICE = 500;

    private static final int BACKDROP_COLOR = 0xB0101010;
    private static final int WINDOW_BG = 0xFFEEEEEE;
    private static final int WINDOW_BORDER = 0xFF1E1E1E;

    private static final int CANVAS_BG = 0xFFF7F7F7;
    private static final int CANVAS_BORDER = 0xFF6A6A6A;

    private static final int CARD_SIZE = 42;
    private static final int CARD_ICON_SIZE = 16;

    private static final int CARD_BORDER_DEFAULT = 0xFF4B4B4B;
    private static final int CARD_BORDER_UNLOCKABLE = 0xFFFFDD55;
    private static final int CARD_BORDER_UNLOCKED = 0xFF55FF55;

    private static final int TEXT_DARK = 0xFF202020;
    private static final int TEXT_MUTED = 0xFF555555;

    // Inset scissor by 1px so content disappears before touching the border
    private static final int SCISSOR_INSET = 1;

    private final Player player;

    private UnlockCategory activeCategory = UnlockCategory.TECHNICAL_MOD;

    /** Pannable canvas state. */
    private double viewX = 0;
    private double viewY = 0;
    private boolean draggingCanvas = false;
    private double dragStartMouseX;
    private double dragStartMouseY;
    private double dragStartViewX;
    private double dragStartViewY;

    /** Cached cards for the current category. */
    private final List<ShopCard> cards = new ArrayList<>();

    // Cached pity tokens (updated each render)
    private int pityTokenCount = 0;

    public ShopScreen(Player player) {
        super(Component.literal("Shop Screen"));
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();
        rebuildCards();
    }

    private void rebuildCards() {
        cards.clear();

        var unlockData = UnlockData.get();
        var unlockTree = unlockData.getUnlockTree(player);

        List<Unlock> allUnlocks = ConfigRegistry.UNLOCK_CONFIG.getAll();
        if (allUnlocks == null) return;

        // Only show unlocks in the active tab/category
        List<Unlock> unlocks = allUnlocks.stream()
                .filter(u -> u != null && u.getCategory() == activeCategory)
                .toList();

        // Grid layout (on the inner canvas)
        int cols = 6;
        int startX = 18;
        int startY = 18;
        int spacingX = 56;
        int spacingY = 56;

        for (int i = 0; i < unlocks.size(); i++) {
            Unlock unlock = unlocks.get(i);
            int col = i % cols;
            int row = i / cols;

            int x = startX + col * spacingX;
            int y = startY + row * spacingY;

            boolean unlocked = unlockTree.isUnlocked(unlock.getName());
            boolean unlockable = !unlocked;

            int price = DEFAULT_PRICE;
            boolean affordable = unlocked || pityTokenCount >= price;

            cards.add(new ShopCard(unlock, x, y, unlocked, unlockable, affordable, price));
        }
    }

    private int getWindowW() {
        return Mth.clamp(Math.round(this.width * WINDOW_W_FRAC), WINDOW_W_MIN, WINDOW_W_MAX);
    }

    private int getWindowH() {
        return Mth.clamp(Math.round(this.height * WINDOW_H_FRAC), WINDOW_H_MIN, WINDOW_H_MAX);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // Backdrop (dim world)
        g.fill(0, 0, this.width, this.height, BACKDROP_COLOR);

        int windowW = getWindowW();
        int windowH = getWindowH();

        int left = (this.width - windowW) / 2;
        int top = (this.height - windowH) / 2;

        // Update pity token count every frame (cheap + keeps UI responsive)
        pityTokenCount = countItem(player, ItemRegistry.PITY_TOKEN.get().getDefaultInstance());

        // Window
        g.fill(left, top, left + windowW, top + windowH, WINDOW_BG);

        // Title
        g.drawString(this.font, this.title, left + WINDOW_PAD, top + 6, TEXT_DARK, false);

        // Tabs
        renderTabs(g, left, top, mouseX, mouseY);

        // Canvas viewport
        int canvasX = left + WINDOW_PAD;
        int canvasY = top + HEADER_H;
        int canvasW = windowW - (WINDOW_PAD * 2);
        int canvasH = windowH - HEADER_H - FOOTER_H;

        g.fill(canvasX, canvasY, canvasX + canvasW, canvasY + canvasH, CANVAS_BG);
        g.renderOutline(canvasX, canvasY, canvasW, canvasH, CANVAS_BORDER);

        // Clip to canvas (inset by 1px so cards don't overlap the border line)
        int scissorX1 = canvasX + SCISSOR_INSET;
        int scissorY1 = canvasY + SCISSOR_INSET;
        int scissorX2 = canvasX + canvasW - SCISSOR_INSET;
        int scissorY2 = canvasY + canvasH - SCISSOR_INSET;
        g.enableScissor(scissorX1, scissorY1, scissorX2, scissorY2);

        ShopCard hovered = null;
        for (ShopCard card : cards) {
            int cx = canvasX + (int) Math.round(card.canvasX - viewX);
            int cy = canvasY + (int) Math.round(card.canvasY - viewY);

            renderCard(g, card, cx, cy);

            if (isInside(mouseX, mouseY, cx, cy, CARD_SIZE, CARD_SIZE)) {
                hovered = card;
            }
        }

        g.disableScissor();

        // Footer
        g.drawString(this.font, "Pity Tokens: " + pityTokenCount, left + WINDOW_PAD, top + windowH - 14, TEXT_MUTED, false);

        // Draw window border LAST so it stays on top of children (fixes cards overlapping border)
        g.renderOutline(left, top, windowW, windowH, WINDOW_BORDER);

        // Hover tooltip
        if (hovered != null) {
            renderHover(g, hovered, mouseX, mouseY);
        }

        super.render(g, mouseX, mouseY, partialTick);
    }

    private void renderTabs(GuiGraphics g, int left, int top, int mouseX, int mouseY) {
        int x = left + WINDOW_PAD;
        int y = top + 18;

        for (UnlockCategory cat : UnlockCategory.values()) {
            boolean active = cat == activeCategory;

            int bg = active ? 0xFFDADADA : 0xFFC7C7C7;
            int border = active ? 0xFF2A2A2A : 0xFF5A5A5A;

            g.fill(x, y, x + TAB_W, y + TAB_H, bg);
            g.renderOutline(x, y, TAB_W, TAB_H, border);

            // Category icon: slot coin (centered)
            ItemStack icon = new ItemStack(ItemRegistry.SLOT_COIN.get());
            g.renderItem(icon, x + (TAB_W - 16) / 2, y + (TAB_H - 16) / 2);

            if (isInside(mouseX, mouseY, x, y, TAB_W, TAB_H)) {
                g.renderTooltip(this.font, Component.literal(cat.getDisplayName()), mouseX, mouseY);
            }

            x += TAB_W + TAB_GAP;
        }
    }

    private void renderCard(GuiGraphics g, ShopCard card, int x, int y) {
        int border = CARD_BORDER_DEFAULT;
        if (card.unlocked) border = CARD_BORDER_UNLOCKED;
        else if (card.unlockable) border = CARD_BORDER_UNLOCKABLE;

        // Base
        g.fill(x, y, x + CARD_SIZE, y + CARD_SIZE, 0xFFFFFFFF);
        g.renderOutline(x, y, CARD_SIZE, CARD_SIZE, border);

        // Icon (centered) - configured per unlock (defaults to empty map)
        int iconX = x + (CARD_SIZE - CARD_ICON_SIZE) / 2;
        int iconY = y + (CARD_SIZE - CARD_ICON_SIZE) / 2;
        g.renderItem(card.unlock.getIconStack(), iconX, iconY);

        // Unlocked inner outline
        if (card.unlocked) {
            g.renderOutline(x + 1, y + 1, CARD_SIZE - 2, CARD_SIZE - 2, 0xFF2CD82C);
        }

        // Price tag (bottom-right) for locked items, since the name is only in the hover UI now
        if (!card.unlocked) {
            String priceText = String.valueOf(card.price);
            int textW = this.font.width(priceText);
            int px = x + CARD_SIZE - 3 - textW;
            int py = y + CARD_SIZE - 10;
            g.drawString(this.font, priceText, px, py, 0xFF3A3A3A, false);
        }

        // Gray out cards that the user can't afford (and aren't already unlocked)
        if (!card.affordable) {
            g.fill(x + 1, y + 1, x + CARD_SIZE - 1, y + CARD_SIZE - 1, 0xAA808080);
        }
    }

    private void renderHover(GuiGraphics g, ShopCard card, int mouseX, int mouseY) {
        // Simple tooltip panel near the cursor
        int pad = 6;
        int w = 170;
        int h = 62;

        int x = mouseX + 12;
        int y = mouseY + 12;

        // Keep on-screen
        x = Mth.clamp(x, 6, this.width - w - 6);
        y = Mth.clamp(y, 6, this.height - h - 6);

        g.fill(x, y, x + w, y + h, 0xF0101010);
        g.renderOutline(x, y, w, h, 0xFF808080);

        // Icon (configured per unlock)
        g.renderItem(card.unlock.getIconStack(), x + pad, y + pad);

        // Text
        String name = card.unlock.getName();
        String desc = buildDescription(card.unlock);
        String price = "Price: " + card.price;

        int tx = x + pad + 20;
        int ty = y + pad;

        g.drawString(this.font, name, tx, ty, 0xFFFFFFFF, false);
        g.drawString(this.font, this.font.plainSubstrByWidth(desc, w - pad - 20), tx, ty + 12, 0xFFBDBDBD, false);

        int priceColor = card.affordable ? 0xFFFFDD55 : 0xFFFF7777;
        g.drawString(this.font, price, tx, ty + 24, priceColor, false);

        if (card.unlocked) {
            g.drawString(this.font, "Unlocked", tx, ty + 40, 0xFF55FF55, false);
        } else if (card.affordable) {
            g.drawString(this.font, "Available", tx, ty + 40, 0xFFFFDD55, false);
        } else {
            g.drawString(this.font, "Too expensive", tx, ty + 40, 0xFFFF7777, false);
        }
    }

    private static String buildDescription(Unlock unlock) {
        if (unlock instanceof ModUnlock modUnlock) {
            String first = modUnlock.getModIds().stream().filter(Objects::nonNull).findFirst().orElse("mod");
            return "Unlock mod: " + first;
        }
        return "Unlock";
    }

    private static boolean isInside(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && my >= y && mx < x + w && my < y + h;
    }

    private static int countItem(Player player, ItemStack match) {
        if (player == null) return 0;
        int total = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (ItemStack.isSameItemSameTags(stack, match)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        int windowW = getWindowW();
        int windowH = getWindowH();

        int left = (this.width - windowW) / 2;
        int top = (this.height - windowH) / 2;

        // Tabs click handling
        int tx = left + WINDOW_PAD;
        int ty = top + 18;
        for (UnlockCategory cat : UnlockCategory.values()) {
            if (isInside(mouseX, mouseY, tx, ty, TAB_W, TAB_H)) {
                activeCategory = cat;
                rebuildCards();
                return true;
            }
            tx += TAB_W + TAB_GAP;
        }

        // Start dragging canvas if clicking inside it
        int canvasX = left + WINDOW_PAD;
        int canvasY = top + HEADER_H;
        int canvasW = windowW - (WINDOW_PAD * 2);
        int canvasH = windowH - HEADER_H - FOOTER_H;

        if (isInside(mouseX, mouseY, canvasX, canvasY, canvasW, canvasH)) {
            draggingCanvas = true;
            dragStartMouseX = mouseX;
            dragStartMouseY = mouseY;
            dragStartViewX = viewX;
            dragStartViewY = viewY;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingCanvas && button == 0) {
            viewX = dragStartViewX - (mouseX - dragStartMouseX);
            viewY = dragStartViewY - (mouseY - dragStartMouseY);

            // Clamp some reasonable bounds (prevents getting totally lost)
            viewX = Mth.clamp(viewX, -2000, 2000);
            viewY = Mth.clamp(viewY, -2000, 2000);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) draggingCanvas = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        // Simple scroll = vertical pan
        viewY -= delta * 18.0;
        viewY = Mth.clamp(viewY, -2000, 2000);
        return true;
    }

    private record ShopCard(Unlock unlock, int canvasX, int canvasY, boolean unlocked, boolean unlockable, boolean affordable, int price) {
    }
}
