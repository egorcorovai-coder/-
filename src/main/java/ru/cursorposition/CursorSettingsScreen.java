package ru.cursorposition;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class CursorSettingsScreen extends Screen {
    private final Screen parent;
    private boolean picking;
    public CursorSettingsScreen(Screen parent) { super(Text.literal("Позиция курсора 1.0.2")); this.parent = parent; }
    private Text modeText() {
        return Text.literal("Режим: " + ("LAST".equals(CursorPosition.CONFIG.mode) ? "последняя позиция" : "выбранная точка"));
    }
    @Override protected void init() {
        addDrawableChild(ButtonWidget.builder(modeText(), b -> {
            CursorPosition.CONFIG.mode = "LAST".equals(CursorPosition.CONFIG.mode) ? "FIXED" : "LAST";
            CursorPosition.save(); b.setMessage(modeText());
        }).dimensions(width / 2 - 130, height / 2 - 40, 260, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Выбрать точку на экране"), b -> picking = true)
            .dimensions(width / 2 - 130, height / 2 - 14, 260, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("По центру"), b -> {
            CursorPosition.CONFIG.mode = "FIXED"; CursorPosition.CONFIG.x = 0.5; CursorPosition.CONFIG.y = 0.5;
            CursorPosition.save(); clearAndInit();
        }).dimensions(width / 2 - 130, height / 2 + 12, 260, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Готово"), b -> close())
            .dimensions(width / 2 - 130, height / 2 + 48, 260, 20).build());
    }
    @Override public boolean mouseClicked(double x, double y, int button) {
        if (picking) {
            if (button == 0) {
                CursorPosition.CONFIG.mode = "FIXED";
                CursorPosition.CONFIG.x = Math.max(0, Math.min(1, x / width));
                CursorPosition.CONFIG.y = Math.max(0, Math.min(1, y / height));
                CursorPosition.save(); picking = false; clearAndInit();
            }
            return true;
        }
        return super.mouseClicked(x, y, button);
    }
    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (picking) {
            context.fill(0, 0, width, height, 0x99000000);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("Нажмите в нужной точке. Esc — отмена"), width / 2, 15, 0xFFFFFF);
            context.fill(mouseX - 7, mouseY, mouseX + 8, mouseY + 1, 0xFFFFFFFF);
            context.fill(mouseX, mouseY - 7, mouseX + 1, mouseY + 8, 0xFFFFFFFF);
        } else {
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 70, 0xFFFFFF);
        }
    }
    @Override public void close() {
        if (picking) { picking = false; return; }
        client.setScreen(parent);
    }
}
