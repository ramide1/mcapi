package com.ramide1.mcapi;

import java.io.File;
import java.io.FileWriter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

public class App extends JavaPlugin {
    String pluginName = "Minecraft API";
    File config;
    File staticFilesDir;
    File indexFile;

    @Override
    public void onEnable() {
        config = new File(getDataFolder(), "config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
        }
        staticFilesDir = new File(getDataFolder(), "web");
        if (!staticFilesDir.exists()) {
            staticFilesDir.mkdirs();
        }
        if (staticFilesDir.list().length == 0) {
            indexFile = new File(staticFilesDir, "index.html");
            try {
                if (indexFile.createNewFile()) {
                    FileWriter writer = new FileWriter(indexFile);
                    writer.write("<html><body><h1>Minecraft API enabled!</h1></body></html>");
                    writer.close();
                }
            } catch (Exception e) {
                getLogger().info(ChatColor.RED + "Error creating index file!");
            }
        }
        Web.enable(this, staticFilesDir);
        getLogger().info(ChatColor.GREEN + pluginName + " has been enabled!");
    }

    @Override
    public void onDisable() {
        Web.disable();
        getLogger().info(ChatColor.GREEN + pluginName + " has been disabled!");
    }
}