package com.minestom.Commands;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BbtCommand implements CommandExecutor {

    private BossbarTimer plugin;

    public BbtCommand(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    private String colorMessage(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bossbartimer")) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (plugin.editing.contains(player)) {
                    BossbarInterface.createEditMenu(player, plugin);
                } else BossbarInterface.createMainMenu(player);
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 1) {
                    sender.sendMessage(colorMessage("&8----------------------------"));
                    sender.sendMessage(colorMessage("&c&l        BossBarTimer"));
                    sender.sendMessage(colorMessage(""));
                    sender.sendMessage(colorMessage("&8 - &a/bbt start <name>"));
                    sender.sendMessage(colorMessage("&8 - &a/bbt stop <name>"));
                    sender.sendMessage(colorMessage("&8 - &a/bbt create <name> <time> <color> <style> <DisplayName>"));
                    sender.sendMessage(colorMessage("&8 - &a/bbt reload"));
                    sender.sendMessage(colorMessage(""));
                    sender.sendMessage(colorMessage("&7TIP: Use 'Tab' to autocomplete some arguments, such as <color>, <style> and <name>"));
                    sender.sendMessage(colorMessage(""));
                    sender.sendMessage(colorMessage("&8----------------------------"));
                }
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (args.length == 1) {
                    plugin.reloadConfig();
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7The configuration file has been reloaded!"));
                    for (String barName : plugin.getConfig().getConfigurationSection("Bars").getKeys(false)) {
                        if (!plugin.getBarManagerMap().containsKey(barName)) {
                            plugin.getBarManagerMap().put(barName, new BossBarManager(plugin));
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("stop")) {
                if (args.length == 1) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7You need to specify a bar!"));
                    return true;
                }
                String barName = args[1];
                BossBarManager bossBar = plugin.getBarManagerMap().get(barName);
                if (!plugin.getTimer().containsKey(barName)) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7That bar is not active!"));
                    return true;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    bossBar.removeBar(player);
                }
                plugin.getTimer().remove(barName);
                bossBar.setFinished(false);
                sender.sendMessage(colorMessage("&cBossBarTimer > &7The bar timer has been stopped!"));
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1 || args.length == 2 || args.length == 3 || args.length == 4) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7Wrong usage! Use: &a/bbt create <name> <time> <color> <style> <DisplayName>"));
                    return true;
                }
                String barName = args[1];
                String time = args[2];
                String color = args[3];
                String style = args[4];
                StringBuilder displayName = new StringBuilder();
                for (int i = 5; i < args.length; i++) {
                    displayName.append(args[i] + " ");
                }
                plugin.getConfig().set("Bars." + barName + ".DisplayName", displayName.toString().trim());
                plugin.getConfig().set("Bars." + barName + ".Time", time);
                plugin.getConfig().set("Bars." + barName + ".Color", color.toUpperCase());
                plugin.getConfig().set("Bars." + barName + ".Style", style.toUpperCase());
                plugin.saveConfig();
                plugin.getBarManagerMap().put(barName, plugin.getBarManager());
                sender.sendMessage(colorMessage("&cBossBarTimer > &7A new bar timer has been created!"));
            }
            if (args[0].equalsIgnoreCase("start")) {
                if (args.length == 1) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7You need to specify a bar!"));
                    return true;
                }
                String barName = args[1];
                if (!plugin.getConfig().isConfigurationSection("Bars." + barName)) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7That bar is not available try another one!"));
                    return true;
                }
                if (plugin.getTimer().containsKey(barName)) {
                    sender.sendMessage(colorMessage("&cBossBarTimer > &7That bar is already active!"));
                    return true;
                }
                String timeFormat = plugin.getConfig().getString("Bars." + barName + ".Time");
                plugin.getUtilities().formatTime(barName, timeFormat);
                BossBarManager bossBar = plugin.getBarManagerMap().get(barName);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    bossBar.addPlayer(player);
                }
                bossBar.setBarColor(plugin.getConfig().getString("Bars." + barName + ".Color").toUpperCase());
                bossBar.setBarStyle(plugin.getConfig().getString("Bars." + barName + ".Style").toUpperCase());
                bossBar.setFinished(false);

                sender.sendMessage(colorMessage("&cBossBarTimer > &7The bar &e" + barName + " &7has been started!"));
                plugin.getUtilities().animateText(plugin.getConfig().getStringList("Bars." + barName + ".DisplayName.Frames"),
                        plugin.getConfig().getLong("Bars." + barName + ".DisplayName.Period"), bossBar);
            }
        }
        return true;
    }


}
