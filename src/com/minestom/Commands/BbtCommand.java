package com.minestom.Commands;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarTimer;
import com.minestom.DataHandler.BossBarHandler;
import com.minestom.DataHandler.PlayerEditingData;
import com.minestom.Utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BbtCommand implements CommandExecutor {

    private BossBarTimer plugin;

    public BbtCommand(BossBarTimer plugin) {
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
            case "update":
                if (!sender.hasPermission("bossbartimer.update.check")) {
                    MessageUtil.sendMessage(sender, plugin.getConfig().getString("Messages.NoPermission"));
                    break;
                }
                if (sender instanceof Player) plugin.getUpdate().sendUpdateMessage((Player) sender);
                else plugin.getUpdate().sendUpdateMessage();
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
        }
    }

    private void stopParameter(CommandSender sender, Integer argsLength, String[] args) {
        if (argsLength == 1) {
            MessageUtil.sendMessage(sender, "You need to specify a bar!");
            return;
        }
        String barName = args[1];
        BossBarHandler bossBarHandler = plugin.getBarDataMap().get(barName);

        if (!bossBarHandler.isRunning()) {
            MessageUtil.sendMessage(sender, "That bar is not active!");
            return;
        }
        bossBarHandler.stop(false);

        MessageUtil.sendMessage(sender, "The bar timer has been stopped!");
    }

    private void startParameter(CommandSender sender, Integer argsLength, String[] args) {
        if (argsLength == 1) {
            MessageUtil.sendMessage(sender, "You need to specify a bar!");
            return;
        }

        String barName = args[1];
        BossBarHandler bossBarHandler = plugin.getBarDataMap().get(barName);

        if (!plugin.getConfig().isConfigurationSection("Bars." + barName)) {
            MessageUtil.sendMessage(sender, "That bar is not available try another one!");
            return;
        }
        if (bossBarHandler.isRunning()) {
            MessageUtil.sendMessage(sender, "That bar is already active!");
            return;
        }

        bossBarHandler.start();

        MessageUtil.sendMessage(sender, "The bar &e" + barName + " &7has been started!");
    }
}
