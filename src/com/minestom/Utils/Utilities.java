package com.minestom.Utils;

import com.minestom.BossbarTimer;
import com.minestom.DataHandler.BossBarHandler;
import com.minestom.DataHandler.PlayerEditingData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {

    private BossbarTimer plugin;
    private Map<Player, PlayerEditingData> playerEditingDataMap = new HashMap<>();

    public Utilities(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    public String format(long input) {
        return formatDurationWords(input * 1000, true, true);
    }

    private String formatDurationWords(long durationMillis, boolean suppressLeadingZero, boolean suppressTrailingZero) {

        FileConfiguration configuration = plugin.getConfig();

        String days = configuration.getString("Messages.Format.Plural.Days");
        String hours = configuration.getString("Messages.Format.Plural.Hours");
        String minutes = configuration.getString("Messages.Format.Plural.Minutes");
        String seconds = configuration.getString("Messages.Format.Plural.Seconds");
        String day = configuration.getString("Messages.Format.Singular.Day");
        String hour = configuration.getString("Messages.Format.Singular.Hour");
        String minute = configuration.getString("Messages.Format.Singular.Minute");
        String second = configuration.getString("Messages.Format.Singular.Second");

        String duration = DurationFormatUtils.formatDuration(durationMillis, "d' " + days + " 'H' " + hours + " 'm' " + minutes + " 's' " + seconds + "'");
        String tmp;

        if (suppressLeadingZero) {
            duration = " " + duration;
            tmp = StringUtils.replaceOnce(duration, " 0 " + days, "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(tmp, " 0 " + hours, "");
                if (tmp.length() != duration.length()) {
                    tmp = StringUtils.replaceOnce(tmp, " 0 " + minutes, "");
                    duration = tmp;
                    if (tmp.length() != tmp.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 " + seconds, "");
                    }
                }
            }

            if (duration.length() != 0) {
                duration = duration.substring(1);
            }
        }

        if (suppressTrailingZero) {
            tmp = StringUtils.replaceOnce(duration, " 0 " + seconds, "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(tmp, " 0 " + minutes, "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(tmp, " 0 " + hours, "");
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 " + days, "");
                    }
                }
            }
        }

        duration = " " + duration;
        duration = StringUtils.replaceOnce(duration, " 1 " + seconds, " 1 " + second);
        duration = StringUtils.replaceOnce(duration, " 1 " + minutes, " 1 " + minute);
        duration = StringUtils.replaceOnce(duration, " 1 " + hours, " 1 " + hour);
        duration = StringUtils.replaceOnce(duration, " 1 " + days, " 1 " + day);
        return duration.trim();
    }

    public Long timeToMillis(String timeFormat) {
        return timeToSeconds(timeFormat) * 1000;
    }

    public Long timeToSeconds(String timeFormat) {
        String[] timeBefore = timeFormat.split(" ");
        long time = 0;

        for (String timeString : timeBefore) {
            if (timeString.contains("s")) {
                time += Long.parseLong(timeString.replaceAll("[a-zA-Z]", ""));
            }
            if (timeString.contains("m")) {
                time += Long.parseLong(timeString.replaceAll("[a-zA-Z]", "")) * 60;
            }
            if (timeString.contains("h")) {
                time += Long.parseLong(timeString.replaceAll("[a-zA-Z]", "")) * 3600;
            }
            if (timeString.contains("d")) {
                time += Long.parseLong(timeString.replaceAll("[a-zA-Z]", "")) * 86400;
            }
        }
        return time;
    }

    public void executeCommand(List<String> cmds) {
        for (String command : cmds) {
            if (command.contains("none") || command.isEmpty()) continue;
            if (!command.startsWith("[")) Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            if (command.startsWith("[message]")) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(ChatColor.translateAlternateColorCodes('&', command.replace("[message]", "")));
                }
            }
            if (command.startsWith("[title-subtitle]")) {
                String[] strings = command.replace("[title-subtitle]", "").split(";");
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (strings[1] == null) {
                        players.sendTitle(ChatColor.translateAlternateColorCodes('&', strings[0]), null, 10, 40, 10);
                        continue;
                    }
                    if (strings[2] == null) {
                        players.sendTitle(ChatColor.translateAlternateColorCodes('&', strings[0]),
                                ChatColor.translateAlternateColorCodes('&', strings[1]), 10, 40, 10);
                        continue;
                    }
                    players.sendTitle(ChatColor.translateAlternateColorCodes('&', strings[0]),
                            ChatColor.translateAlternateColorCodes('&', strings[1]), Integer.valueOf(strings[2]),
                            Integer.valueOf(strings[3]), Integer.valueOf(strings[4]));
                }
            }
            if (command.startsWith("[sound]")) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.playSound(players.getLocation(), Sound.valueOf(command.replace("[sound]", "").replace(" ", "").toUpperCase()), 1, 2);
                }
            }
            if (command.startsWith("[console]")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("[console]", ""));
            }
            if (command.startsWith("[player]")) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    Bukkit.getServer().dispatchCommand(players, command.replace("[player]", ""));
                }
            }
        }
    }

    public void addPlayerEditing(Player player) {
        playerEditingDataMap.put(player, new PlayerEditingData(new BossBarHandler(plugin)));
    }

    public void removePlayerEditing(Player player) {
        playerEditingDataMap.remove(player);
    }

    public Map<Player, PlayerEditingData> getPlayerEditingDataMap() {
        return playerEditingDataMap;
    }

    public PlayerEditingData getEditingData(Player player) {
        return playerEditingDataMap.get(player);
    }
}
