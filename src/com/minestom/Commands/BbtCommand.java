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
        if (!command.getName().equalsIgnoreCase("bossbartimer") || !(sender instanceof Player)) {
            return true;
        }
        Integer argsLength = args.length;
        Player player = (Player) sender;

        if (argsLength == 0) {
            if (plugin.editing.contains(player)) {
                BossbarInterface.createEditMenu(player, plugin);
            } else BossbarInterface.createMainMenu(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            if (argsLength == 1) {
                player.sendMessage(colorMessage("&8----------------------------"));
                player.sendMessage(colorMessage("&c&l        BossBarTimer"));
                player.sendMessage(colorMessage(""));
                player.sendMessage(colorMessage("&8 - &a/bbt start <name>"));
                player.sendMessage(colorMessage("&8 - &a/bbt stop <name>"));
                player.sendMessage(colorMessage("&8 - &a/bbt create <name> <time> <color> <style> <DisplayName>"));
                player.sendMessage(colorMessage("&8 - &a/bbt reload"));
                player.sendMessage(colorMessage(""));
                player.sendMessage(colorMessage("&7TIP: Use 'Tab' to autocomplete some arguments, such as <color>, <style> and <name>"));
                player.sendMessage(colorMessage(""));
                player.sendMessage(colorMessage("&8----------------------------"));
            }
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (argsLength == 1) {
                plugin.reloadConfig();
                player.sendMessage(colorMessage("&cBossBarTimer > &7The configuration file has been reloaded!"));
                for (String barName : plugin.getConfig().getConfigurationSection("Bars").getKeys(false)) {
                    if (!plugin.getBarManagerMap().containsKey(barName)) {
                        plugin.getBarManagerMap().put(barName, new BossBarManager(plugin));
                    }
                }
            }
        }

        if (args[0].equalsIgnoreCase("stop")) {
            if (argsLength == 1) {
                player.sendMessage(colorMessage("&cBossBarTimer > &7You need to specify a bar!"));
                return true;
            }
            String barName = args[1];
            BossBarManager bossBar = plugin.getBarManagerMap().get(barName);
            if (!plugin.getTimer().containsKey(barName)) {
                player.sendMessage(colorMessage("&cBossBarTimer > &7That bar is not active!"));
                return true;
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                bossBar.removeBar(onlinePlayer);
            }
            plugin.getTimer().remove(barName);
            bossBar.setFinished(false);
            player.sendMessage(colorMessage("&cBossBarTimer > &7The bar timer has been stopped!"));
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (argsLength == 1 || argsLength == 2 || argsLength == 3 || argsLength == 4) {
                player.sendMessage(colorMessage("&cBossBarTimer > &7Wrong usage! Use: &a/bbt create <name> <time> <color> <style> <DisplayName>"));
                return true;
            }
            String barName = args[1];
            String time = args[2];
            String color = args[3];
            String style = args[4];
            StringBuilder displayName = new StringBuilder();
            for (int i = 5; i < argsLength; i++) {
                displayName.append(args[i] + " ");
            }
            plugin.getConfig().set("Bars." + barName + ".DisplayName", displayName.toString().trim());
            plugin.getConfig().set("Bars." + barName + ".Time", time);
            plugin.getConfig().set("Bars." + barName + ".Color", color.toUpperCase());
            plugin.getConfig().set("Bars." + barName + ".Style", style.toUpperCase());
            plugin.saveConfig();
            plugin.getBarManagerMap().put(barName, plugin.getBarManager());
            player.sendMessage(colorMessage("&cBossBarTimer > &7A new bar timer has been created!"));
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (argsLength == 1) {
                player.sendMessage(colorMessage("&cBossBarTimer > &7You need to specify a bar!"));
                return true;
            }
            String barName = args[1];
            if (!plugin.getConfig().isConfigurationSection("Bars." + barName)) {
                player.sendMessage(colorMessage("&cBossBarTimer > &7That bar is not available try another one!"));
                return true;
            }
            if (plugin.getTimer().containsKey(barName)) {
                player.sendMessage(colorMessage("&cBossBarTimer > &7That bar is already active!"));
                return true;
            }
            String timeFormat = plugin.getConfig().getString("Bars." + barName + ".Time");
            plugin.getUtilities().formatTime(barName, timeFormat);
            BossBarManager bossBar = plugin.getBarManagerMap().get(barName);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(onlinePlayer);
            }
            bossBar.setBarColor(plugin.getConfig().getString("Bars." + barName + ".Color").toUpperCase());
            bossBar.setBarStyle(plugin.getConfig().getString("Bars." + barName + ".Style").toUpperCase());
            bossBar.setFinished(false);

            player.sendMessage(colorMessage("&cBossBarTimer > &7The bar &e" + barName + " &7has been started!"));
            plugin.getUtilities().animateText(plugin.getConfig().getStringList("Bars." + barName + ".DisplayName.Frames"),
                    plugin.getConfig().getLong("Bars." + barName + ".DisplayName.Period"), bossBar);
        }

        return true;
    }


}
