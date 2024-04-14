package com.ramide1.mcapi;

import org.bukkit.Bukkit;
import io.javalin.Javalin;

public class Web {
    private static Javalin webApp;

    public static void enable(App plugin) {
        Integer port = plugin.getConfig().getInt("Config.port", 80);
        String password = plugin.getConfig().getString("Config.password", "password");
        webApp = Javalin.create().start(port);
        webApp.get("/", ctx -> ctx.result("Minecraft API enabled!"));
        webApp.post("/command", ctx -> {
            if (!password.equals(ctx.formParam("password"))) {
                ctx.result("false");
            } else {
                String command = ctx.formParam("command");
                try {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                    });
                    ctx.result("true");
                } catch (Exception e) {
                    ctx.result("false");
                }
            }
        });
    }

    public static void disable() {
        if (webApp != null) {
            webApp.stop();
        }
    }
}