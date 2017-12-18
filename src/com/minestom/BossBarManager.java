package com.minestom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarManager {

    private BossbarTimer plugin;

    public BossBarManager(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    private BossBar bar = Bukkit.createBossBar(null, BarColor.RED, BarStyle.SEGMENTED_20);

    public void addPlayer(Player player, String name) {
        if (name != null) {
            bar.setTitle(plugin.getConfig().getString("Bars." + name + ".DisplayName"));
            bar.setColor(BarColor.valueOf(plugin.getConfig().getString("Bars." + name + ".Color").toUpperCase()));
            bar.setStyle(BarStyle.valueOf(plugin.getConfig().getString("Bars." + name + ".Style").toUpperCase()));
        }
        if (!bar.getPlayers().contains(player) && bar != null) {
            bar.addPlayer(player);
        }
    }

    public void setBarProgress(double progress, double initialTime) {
        double barProgress = initialTime / progress;
        bar.setProgress(1 / barProgress);
    }

    public void removeBar(Player player) {
        if (bar.getPlayers().contains(player))
            bar.removePlayer(player);
    }

    public void setBarName(String text) {
        bar.setTitle(ChatColor.translateAlternateColorCodes('&', text));
    }
}
