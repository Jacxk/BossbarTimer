package com.minestom.Updater;

import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URL;

public class AutoDownload {

    private Plugin plugin;

    public AutoDownload(Plugin plugin) {
        this.plugin = plugin;
    }

    public void autoDownload(int id) {

        plugin.getLogger().info("&7Getting the latest version of &c&l" + plugin.getDescription().getName() + "&7...");
        plugin.getLogger().info("&7Downloading...");

        String downloadURL = "https://api.spiget.org/v2/resources/" + id + "/download";
        File dir = new File(plugin.getDataFolder().getAbsolutePath());
        String pluginFolder = dir.getParentFile().getAbsolutePath() + "/" + plugin.getDescription().getName() + ".jar";

        try {
            FileUtils.copyURLToFile(new URL(downloadURL), new File(pluginFolder), 10000, 10000);
            plugin.getLogger().info("&7Downloaded the latest version of &c&lTimedFly&7!");
            plugin.getLogger().info("&7The changes will take effect in the next server restart!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


