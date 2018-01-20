package com.minestom.Commands;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.PlayerEditingData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BbtCommand implements CommandExecutor {

    private BossbarTimer plugin;

    public BbtCommand(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("bossbartimer")) {
            return true;
        }

        Integer argsLength = args.length;
        if (argsLength == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
            if (plugin.getUtilities().getPlayerEditingDataMap().containsKey(player) && editingData.isEditing()) {
                BossbarMenuMaker.createEditMenu(player, plugin);
            } else BossbarMenuMaker.createMainMenu(player);
            return true;
        }

        switch (args[0]) {
            case "help":
                helpParameter(sender, argsLength);
                break;
            case "reload":
                reloadParameter(sender, argsLength);
                break;
            case "stop":
                stopParameter(sender, argsLength, args);
                break;
            case "create":
                if (!(sender instanceof Player)) {
                    MessageUtil.sendMessage(sender, "Only players can use this command");
                    return true;
                }
                Player player = (Player) sender;
                createParameter(player, argsLength, args);
                break;
            case "start":
                startParameter(sender, argsLength, args);
                break;
            default:
                helpParameter(sender, argsLength);
                break;
        }
        return true;
    }

    private void helpParameter(CommandSender sender, Integer argsLength) {
        if (argsLength == 1) {
            String[] messages = {
                    "&8----------------------------",
                    "&c&l        BossBarTimer",
                    "",
                    "&8 - &a/bbt start <name>",
                    "&8 - &a/bbt stop <name>",
                    // "&8 - &a/bbt create <name> <time> <color> <style> <DisplayName>", // currently not working
                    "&8 - &a/bbt reload",
                    "",
                    "&7TIP: Use 'Tab' to autocomplete some arguments, such as <color>, <style> and <name>",
                    "",
                    "&7https://github.com/Jacxk/BossbarTimer/wiki",
                    "",
                    "&8----------------------------"
            };
            MessageUtil.sendMessages(sender, messages, false);
        }
    }

    private void reloadParameter(CommandSender sender, Integer argsLength) {
        if (argsLength == 1) {
            plugin.reloadConfig();
            MessageUtil.sendMessage(sender, "The configuration file has been reloaded!");
            for (String barName : plugin.getConfig().getConfigurationSection("Bars").getKeys(false)) {
                if (!plugin.getBarManagerMap().containsKey(barName)) {
                    plugin.getBarManagerMap().put(barName, new BossBarManager(plugin));
                }
            }
            Bukkit.getScheduler().cancelTask(plugin.getUtilities().getTaskId());
        }
    }

    private void stopParameter(CommandSender sender, Integer argsLength, String[] args) {
        if (argsLength == 1) {
            MessageUtil.sendMessage(sender, "You need to specify a bar!");
            return;
        }
        String barName = args[1];
        BossBarManager bossBar = plugin.getBarManagerMap().get(barName);
        if (!plugin.getTimer().containsKey(barName)) {
            MessageUtil.sendMessage(sender, "That bar is not active!");
            return;
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            bossBar.removeBar(onlinePlayer);
        }
        plugin.getTimer().remove(barName);
        bossBar.setFinished(false);
        Bukkit.getScheduler().cancelTask(plugin.getUtilities().getTaskId());
        MessageUtil.sendMessage(sender, "The bar timer has been stopped!");
    }

    private void createParameter(Player player, Integer argsLength, String[] args) {
        if (argsLength <= 4) {
            MessageUtil.sendMessage(player, "Wrong usage! Use: &a/bbt create <name> <time> <color> <style> <DisplayName>");
            return;
        }
        String barName = args[1];
        String time = args[2];
        String color = args[3];
        String style = args[4];
        StringBuilder displayName = new StringBuilder();
        for (int i = 5; i < argsLength; i++) {
            displayName.append(args[i]).append(" ");
        }
        plugin.getConfig().set("Bars." + barName + ".DisplayName", displayName.toString().trim());
        plugin.getConfig().set("Bars." + barName + ".Time", time);
        plugin.getConfig().set("Bars." + barName + ".Color", color.toUpperCase());
        plugin.getConfig().set("Bars." + barName + ".Style", style.toUpperCase());
        plugin.saveConfig();
        plugin.getBarManagerMap().put(barName, plugin.getBarManager());
        MessageUtil.sendMessage(player, "A new bar timer has been created!");
    }

    private void startParameter(CommandSender sender, Integer argsLength, String[] args) {
        Bukkit.getScheduler().cancelTask(plugin.getUtilities().getTaskId());
        if (argsLength == 1) {
            MessageUtil.sendMessage(sender, "You need to specify a bar!");
            return;
        }

        String barName = args[1];

        if (!plugin.getConfig().isConfigurationSection("Bars." + barName)) {
            MessageUtil.sendMessage(sender, "That bar is not available try another one!");
            return;
        }
        if (plugin.getTimer().containsKey(barName)) {
            MessageUtil.sendMessage(sender, "That bar is already active!");
            return;
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

        MessageUtil.sendMessage(sender, "The bar &e" + barName + " &7has been started!");
        plugin.getUtilities().setFrames(plugin.getConfig().getStringList("Bars." + barName + ".DisplayName.Frames"));
        plugin.getUtilities().setPeriod(plugin.getConfig().getLong("Bars." + barName + ".DisplayName.Period"));
        plugin.getUtilities().animateText(bossBar);
    }
}
