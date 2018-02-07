package com.minestom.Commands;

import com.minestom.BarMenuCreator.Api.BarStartEvent;
import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.BarsData;
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
            if (!sender.hasPermission("bossbartimer.open")) {
                MessageUtil.sendMessage(sender, plugin.getConfig().getString("Messages.NoPermission"));
                return true;
            }
            Player player = (Player) sender;
            PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
            if (plugin.getUtilities().getPlayerEditingDataMap().containsKey(player) && editingData.isEditing()) {
                BossbarMenuMaker.createEditMenu(player, plugin);
            } else BossbarMenuMaker.createMainMenu(player);
            return true;
        }

        switch (args[0]) {
            case "help":
                if (!sender.hasPermission("bossbartimer.help")) {
                    MessageUtil.sendMessage(sender, plugin.getConfig().getString("Messages.NoPermission"));
                    break;
                }
                helpParameter(sender, argsLength);
                break;
            case "reload":
                if (!sender.hasPermission("bossbartimer.reload")) {
                    MessageUtil.sendMessage(sender, plugin.getConfig().getString("Messages.NoPermission"));
                    break;
                }
                reloadParameter(sender, argsLength);
                break;
            case "stop":
                if (!sender.hasPermission("bossbartimer.stop")) {
                    MessageUtil.sendMessage(sender, plugin.getConfig().getString("Messages.NoPermission"));
                    break;
                }
                stopParameter(sender, argsLength, args);
                break;
            case "start":
                if (!sender.hasPermission("bossbartimer.start")) {
                    MessageUtil.sendMessage(sender, plugin.getConfig().getString("Messages.NoPermission"));
                    break;
                }
                startParameter(sender, argsLength, args);
                break;
            case "debug":
                if (!sender.hasPermission("bossbartimer.debug")) {
                    MessageUtil.sendMessage(sender, plugin.getConfig().getString("Messages.NoPermission"));
                    break;
                }
                plugin.debug = !plugin.debug;
                MessageUtil.sendMessage(sender, "&7Debug mode " + (plugin.debug ? "enabled" : "disabled") + "...");
                break;
            default:
                if (!sender.hasPermission("bossbartimer.help")) {
                    MessageUtil.sendMessage(sender, plugin.getConfig().getString("Messages.NoPermission"));
                    break;
                }
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
                    "&8 - &a/bbt reload",
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
            plugin.getBarDataMap().clear();
            plugin.loadBars();
            Bukkit.getScheduler().cancelTask(plugin.getUtilities().getTaskId());
        }
    }

    private void stopParameter(CommandSender sender, Integer argsLength, String[] args) {
        if (argsLength == 1) {
            MessageUtil.sendMessage(sender, "You need to specify a bar!");
            return;
        }
        String barName = args[1];

        if (!plugin.getTimer().containsKey(barName)) {
            MessageUtil.sendMessage(sender, "That bar is not active!");
            return;
        }

        plugin.getTimer().remove(barName);
        plugin.getUtilities().stop(plugin.getBarDataMap().get(barName));

        MessageUtil.sendMessage(sender, "The bar timer has been stopped!");

        BarStartEvent barStartEvent = new BarStartEvent(plugin.getUtilities(), plugin.getBarDataMap().get(barName));
        Bukkit.getServer().getPluginManager().callEvent(barStartEvent);
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
        BarsData barsData = plugin.getBarDataMap().get(barName);

        String timeFormat = barsData.getCountdownTime();
        plugin.getTimer().put(barName, plugin.getUtilities().timeToSeconds(timeFormat));

        plugin.getUtilities().start(barsData);

        MessageUtil.sendMessage(sender, "The bar &e" + barName + " &7has been started!");

        BarStartEvent barStartEvent = new BarStartEvent(plugin.getUtilities(), plugin.getBarDataMap().get(barName));
        Bukkit.getServer().getPluginManager().callEvent(barStartEvent);
    }
}
