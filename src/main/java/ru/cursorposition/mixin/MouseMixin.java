package ru.cursorposition.mixin;

import net.minecraft.client.Mouse;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.cursorposition.CursorPosition;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void synchronizeInventoryCursor(CallbackInfo ci) {
        CursorPosition.beforeMouseTick(MinecraftClient.getInstance());
    }
}
