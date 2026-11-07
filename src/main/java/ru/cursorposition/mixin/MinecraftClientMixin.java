package ru.cursorposition.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.cursorposition.CursorPosition;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "setScreen", at = @At("HEAD"))
    private void rememberCursor(Screen next, CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        if (client.currentScreen instanceof HandledScreen<?> && next == null) CursorPosition.remember(client);
    }
    @Inject(method = "setScreen", at = @At("TAIL"))
    private void restoreCursor(Screen next, CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        if (next instanceof HandledScreen<?>) CursorPosition.restore(client);
    }
}
