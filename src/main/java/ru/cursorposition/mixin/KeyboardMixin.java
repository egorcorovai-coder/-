package ru.cursorposition.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.cursorposition.CursorSettingsScreen;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void openSettings(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (window == client.getWindow().getHandle() && key == GLFW.GLFW_KEY_F8 && action == GLFW.GLFW_PRESS) {
            if (!(client.currentScreen instanceof CursorSettingsScreen)) client.setScreen(new CursorSettingsScreen(client.currentScreen));
            ci.cancel();
        }
    }
}
