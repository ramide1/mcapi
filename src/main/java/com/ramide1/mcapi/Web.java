package com.ramide1.mcapi;

import java.util.List;
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
                JsonResponse jsonResponse = new JsonResponse("Error", true);
                try {
                    CommandResponse commandResponse = new CommandResponse();
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.getServer().dispatchCommand(commandResponse, command);
                        List<String> messages = commandResponse.getMessages();
                        if (!messages.isEmpty()) {
                            jsonResponse.setResponse(String.join("\n", messages));
                            jsonResponse.setError(false);
                        } else {
                            jsonResponse.setResponse("No command response");
                        }
                        ctx.json(jsonResponse);
                    });
                } catch (Exception e) {
                    jsonResponse.setResponse(e.getMessage());
                    ctx.json(jsonResponse);
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