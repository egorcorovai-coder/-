package ru.cursorposition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.LoggerFactory;

public final class CursorPosition {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("cursorposition.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static class Config {
        public String mode = "FIXED";
        public double x = 0.5, y = 0.5;
    }
    public static final Config CONFIG = load();
    private static double lastX = 0.5, lastY = 0.5;
    private static Config load() {
        try {
            if (Files.exists(FILE)) {
                Config c = GSON.fromJson(Files.readString(FILE), Config.class);
                if (c != null && ("FIXED".equals(c.mode) || "LAST".equals(c.mode))) {
                    c.x = clamp(c.x); c.y = clamp(c.y); return c;
                }
            }
        } catch (Exception e) { LoggerFactory.getLogger("cursorposition").warn("Cannot read cursor settings", e); }
        return new Config();
    }
    private static double clamp(double n) { return Double.isFinite(n) ? Math.max(0, Math.min(1, n)) : 0.5; }
    public static void save() {
        try { Files.createDirectories(FILE.getParent()); Files.writeString(FILE, GSON.toJson(CONFIG)); }
        catch (Exception e) { LoggerFactory.getLogger("cursorposition").warn("Cannot save cursor settings", e); }
    }
    public static void remember(MinecraftClient client) {
        int[] w = new int[1], h = new int[1];
        GLFW.glfwGetWindowSize(client.getWindow().getHandle(), w, h);
        if (w[0] > 0 && h[0] > 0) {
            lastX = clamp(client.mouse.getX() / w[0]); lastY = clamp(client.mouse.getY() / h[0]);
        }
    }
    public static void restore(MinecraftClient client) {
        int[] w = new int[1], h = new int[1];
        long handle = client.getWindow().getHandle();
        GLFW.glfwGetWindowSize(handle, w, h);
        double x = "LAST".equals(CONFIG.mode) ? lastX : CONFIG.x;
        double y = "LAST".equals(CONFIG.mode) ? lastY : CONFIG.y;
        GLFW.glfwSetCursorPos(handle, x * Math.max(0, w[0] - 1), y * Math.max(0, h[0] - 1));
    }
}
