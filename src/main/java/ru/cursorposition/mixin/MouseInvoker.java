package ru.cursorposition.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mouse.class)
public interface MouseInvoker {
    @Invoker("onCursorPos")
    void cursorposition$onCursorPos(long window, double x, double y);
}
