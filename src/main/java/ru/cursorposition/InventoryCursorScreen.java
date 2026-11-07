package ru.cursorposition;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.glfw.GLFW;

/** Inventory preview: input is consumed so selecting a slot never moves items. */
public final class InventoryCursorScreen extends HandledScreen<PlayerScreenHandler> {
    private final Screen parent;
    public InventoryCursorScreen(PlayerEntity player, Screen parent) {
        super(player.playerScreenHandler, player.getInventory(), Text.literal("Выбор слота"));
        titleX = 97;
        this.parent = parent;
    }
    @Override protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured,
            Identifier.ofVanilla("textures/gui/container/inventory.png"),
            x, y, 0f, 0f, backgroundWidth, backgroundHeight, 256, 256);
        InventoryScreen.drawEntity(context, x + 26, y + 8, x + 75, y + 78,
            30, 0.0625f, mouseX, mouseY, client.player);
    }
    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (Slot slot : handler.slots) {
                if (slot.inventory == client.player.getInventory()
                    && mouseX >= x + slot.x && mouseX < x + slot.x + 16
                    && mouseY >= y + slot.y && mouseY < y + slot.y + 16) {
                    CursorPosition.CONFIG.mode = "SLOT";
                    CursorPosition.CONFIG.slot = slot.getIndex();
                    CursorPosition.save();
                    close();
                    break;
                }
            }
        }
        return true;
    }
    @Override public boolean mouseReleased(double x, double y, int button) { return true; }
    @Override public boolean mouseDragged(double x, double y, int button, double dx, double dy) { return true; }
    @Override public boolean mouseScrolled(double x, double y, double horizontal, double vertical) { return true; }
    @Override public boolean keyPressed(int key, int scancode, int modifiers) {
        if (key == GLFW.GLFW_KEY_ESCAPE || client.options.inventoryKey.matchesKey(key, scancode)) close();
        return true;
    }
    @Override public boolean charTyped(char chr, int modifiers) { return true; }
    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, "Нажми на слот — курсор будет появляться в нём", width / 2, Math.max(4, y - 26), 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Esc — назад, предметы не перемещаются", width / 2, Math.max(15, y - 14), 0xAAAAAA);
        for (Slot slot : handler.slots) {
            if (slot.inventory == client.player.getInventory() && slot.getIndex() == CursorPosition.CONFIG.slot
                && "SLOT".equals(CursorPosition.CONFIG.mode)) {
                context.drawBorder(x + slot.x - 1, y + slot.y - 1, 18, 18, 0xFF00FF88);
            }
        }
    }
    @Override public void close() { MinecraftClient.getInstance().setScreen(parent); }
}
