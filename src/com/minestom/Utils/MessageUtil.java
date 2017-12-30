package com.minestom.Utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil
{
    private static String prefix = "&cBossBarTimer > &7";
    private static char colorChar = '&';

    public static void sendMessage(Player player, String message)
    {
        sendMessage(player, message, true);
    }

    public static void sendMessage(Player player, String message, boolean usePrefix)
    {
        String fullMessage = "";

        if(usePrefix)
        {
            fullMessage += prefix;
        }

        fullMessage += message;

        player.sendMessage(colorMessage(fullMessage));
    }

    public static void sendMessages(Player player, String[] messages)
    {
        sendMessages(player, messages, true);
    }

    public static void sendMessages(Player player, String[] messages, boolean usePrefix)
    {
        for (String message: messages)
        {
            sendMessage(player, message, usePrefix);
        }
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut)
    {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    private static String colorMessage(String text) {
        return ChatColor.translateAlternateColorCodes(colorChar, text);
    }
}
