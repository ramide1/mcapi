package com.ramide1.mcapi;

import org.bukkit.Bukkit;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.io.File;

public class Web {
    private static Javalin webApp;

    public static void enable(App plugin, File staticFilesDir) {
        Integer port = plugin.getConfig().getInt("Config.port", 80);
        String password = plugin.getConfig().getString("Config.password", "password");
        webApp = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = staticFilesDir.getAbsolutePath();
                staticFiles.location = Location.EXTERNAL;
                staticFiles.precompress = false;
                staticFiles.aliasCheck = null;
            });
            config.showJavalinBanner = false;
        }).start(port);
        webApp.post("/command", ctx -> {
            JsonResponse jsonResponse = new JsonResponse("Error", true);
            if (!password.equals(ctx.formParam("password"))) {
                jsonResponse.setResponse("Incorrect password");
            } else {
                try {
                    String command = ctx.formParam("command");
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                    });
                    jsonResponse.setResponse("Command success");
                    jsonResponse.setError(false);
                } catch (Exception e) {
                    jsonResponse.setResponse(e.getMessage());
                }
            }
            ctx.json(jsonResponse);
        });
        webApp.post("/players/count", ctx -> {
            JsonResponse jsonResponse = new JsonResponse("Error", true);
            if (!password.equals(ctx.formParam("password"))) {
                jsonResponse.setResponse("Incorrect password");
            } else {
                try {
                    jsonResponse.setResponse(Integer.toString(Bukkit.getOnlinePlayers().size()));
                    jsonResponse.setError(false);
                } catch (Exception e) {
                    jsonResponse.setResponse(e.getMessage());
                }
            }
            ctx.json(jsonResponse);
        });
        webApp.post("/players/list", ctx -> {
            JsonResponse jsonResponse = new JsonResponse("Error", true);
            if (!password.equals(ctx.formParam("password"))) {
                jsonResponse.setResponse("Incorrect password");
            } else {
                try {
                    jsonResponse.setResponse(Bukkit.getOnlinePlayers().toString());
                    jsonResponse.setError(false);
                } catch (Exception e) {
                    jsonResponse.setResponse(e.getMessage());
                }
            }
            ctx.json(jsonResponse);
        });
    }

    public static void disable() {
        if (webApp != null) {
            webApp.stop();
        }
    }
}