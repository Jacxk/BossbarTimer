package com.minestom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BossbarTimer extends JavaPlugin {

    private Map<String, Double> timer = new HashMap<>();
    private Map<String, Double> initialTime = new HashMap<>();
    private Map<String, BossBarManager> barManagerMap = new HashMap<>();
    private Utilities utilities;

    public Map<String, BossBarManager> getBarManagerMap() {
        return barManagerMap;
    }

    public Utilities getUtilities() {
        return utilities;
    }

    public Map<String, Double> getInitialTime() {
        return initialTime;
    }

    public Map<String, Double> getTimer() {
        return timer;
    }

    private String colorMessage(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public void onEnable() {
        utilities = new Utilities(this);
        BossBarManager barManager = new BossBarManager(this);
        BukkitTask task = new CountDown(this).runTaskTimer(this, 0L, 20L);
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }
        for (String name : getConfig().getConfigurationSection("Bars").getKeys(false)) {
            barManagerMap.put(name, barManager);
        }
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bossbartimer")) {
            if (args.length == 0) {
                sender.sendMessage(colorMessage("&8----------------------------"));
                sender.sendMessage(colorMessage("&c&l        BossBarTimer"));
                sender.sendMessage(colorMessage(""));
                sender.sendMessage(colorMessage("&8 - &a/bbt start <name>"));
                sender.sendMessage(colorMessage("&8 - &a/bbt stop <name>"));
                sender.sendMessage(colorMessage("&8 - &a/bbt create <name> <time> <color> <style> <DisplayName>"));
                sender.sendMessage(colorMessage("&8 - &a/bbt manual <name> <time> <DisplayName>"));
                sender.sendMessage(colorMessage("&8 - &a/bbt reload | Sort of not working"));
                sender.sendMessage(colorMessage(""));
                sender.sendMessage(colorMessage("&8----------------------------"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                sender.sendMessage(colorMessage("&cBossBarTimer > &7The configuration file has been reloaded!"));
            }
            if (args[0].equalsIgnoreCase("stop")) {
                String barName = args[1];
                BossBarManager bossBar = barManagerMap.get(barName);
                if (!timer.containsKey(barName)){
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7That bar is not active!"));
                    return true;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    bossBar.removeBar(player);
                }
                timer.remove(barName);
                sender.sendMessage(colorMessage("&cBossBarTimer > &7The bar timer has been stopped!"));
            }
            if (args[0].equalsIgnoreCase("create")) {
                String barName = args[1];
                String time = args[2];
                String color = args[3];
                String style = args[4];
                StringBuilder displayName = new StringBuilder();
                for (int i = 5; i < args.length; i++) {
                    displayName.append(args[i] + " ");
                }
                getConfig().set("Bars." + barName + ".DisplayName", displayName.toString().trim());
                getConfig().set("Bars." + barName + ".Time", time);
                getConfig().set("Bars." + barName + ".Color", color.toUpperCase());
                getConfig().set("Bars." + barName + ".Style", style.toUpperCase());
                saveConfig();
                sender.sendMessage(colorMessage("&cBossBarTimer > &7A new bar timer has been created!"));
            }
            if (args[0].equalsIgnoreCase("start")) {
                String barName = args[1];
                if (!getConfig().isConfigurationSection("Bars." + barName)) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7That bar is not available try another one!"));
                    return true;
                }
                if (timer.containsKey(barName)){
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7That bar is already active!"));
                    return true;
                }
                String timeFormat = getConfig().getString("Bars." + barName + ".Time");
                double time = Double.parseDouble(timeFormat.replaceAll("[a-zA-Z]", ""));
                if (timeFormat.contains("s")) {
                    timer.put(barName, time);
                } else if (timeFormat.contains("m")) {
                    time = time * 60;
                    timer.put(barName, time);
                } else if (timeFormat.contains("h")) {
                    time = time * 3600;
                    timer.put(barName, time);
                } else
                    timer.put(barName, time);
                initialTime.put(barName, time);
                BossBarManager bossBar = barManagerMap.get(barName);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    bossBar.addPlayer(player, barName);
                }
                sender.sendMessage(colorMessage("&cBossBarTimer > &7The bar &e" + barName + " &7has been started!"));
            }
        }
        return true;
    }
}
