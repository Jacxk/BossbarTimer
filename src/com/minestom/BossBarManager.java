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

    private BossBar bar = Bukkit.createBossBar(null, BarColor.RED, BarStyle.SOLID);

    public void createBar(String title, String color, String style) {
        bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', title),
                BarColor.valueOf(color.toUpperCase()), BarStyle.valueOf(style.toUpperCase()));
    }

    public void addPlayer(Player player) {
        if (!bar.getPlayers().contains(player) && bar != null) {
            bar.addPlayer(player);
        }
    }

    public void setBarProgress(double progress, double initialTime) {
        double barProgress = initialTime / progress;
        bar.setProgress(1 / barProgress);
    }

    public boolean containsBar(Player player) {
        return bar.getPlayers().contains(player);
    }

    public void removeBar(Player player) {
        bar.removePlayer(player);
    }

    public String getBarColor() {
        return bar.getColor().name();
    }

    public void setBarColor(String color) {
        bar.setColor(BarColor.valueOf(color.toUpperCase()));
    }

    public String getBarStyle() {
        return bar.getStyle().name().replace("_", "");
    }

    public void setBarStyle(String style) {
        bar.setStyle(BarStyle.valueOf(style.toUpperCase()));
    }

    public void setBarName(String text) {
        bar.setTitle(ChatColor.translateAlternateColorCodes('&', text));
    }
}
