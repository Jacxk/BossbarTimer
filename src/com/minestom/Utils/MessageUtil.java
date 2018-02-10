package com.minestom.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {
    private static String prefix = "&cBossBarTimer > &7";

    public static void sendMessage(Player player, String message) {
        sendMessage(player, message, true);
    }

    public static void sendMessage(CommandSender sender, String message) {
        String fullMessage = "";

        fullMessage += prefix;

        fullMessage += message;
        sender.sendMessage(colorMessage(fullMessage));
    }

    public static void sendMessage(Player player, String message, boolean usePrefix) {
        String fullMessage = "";

        if (usePrefix) {
            fullMessage += prefix;
        }

        fullMessage += message;

        player.sendMessage(colorMessage(fullMessage));
    }

    public static void sendDebugMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("bossbartimer.debug")) player.sendMessage(colorMessage("&cBBT Debug > " + message));
        });
        Bukkit.getLogger().info(message);
    }

    public static void sendMessages(Player player, String[] messages) {
        sendMessages(player, messages, true);
    }

    public static void sendMessages(Player player, String[] messages, boolean usePrefix) {
        for (String message : messages) {
            sendMessage(player, message, usePrefix);
        }
    }

    public static void sendMessages(CommandSender sender, String[] messages, boolean prefix) {
        for (String message : messages) {
            if (!prefix) sender.sendMessage(colorMessage(message));
            else sendMessage(sender, message);
        }
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    private static String colorMessage(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
