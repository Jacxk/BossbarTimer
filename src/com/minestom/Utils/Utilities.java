package com.minestom.Utils;

import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Utilities {

    private BossbarTimer plugin;

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

    public void formatTime(String name, String timeFormat) {
        Map<String, Double> timer = plugin.getTimer();

        double time = Double.parseDouble(timeFormat.replaceAll("[a-zA-Z]", ""));
        if (timeFormat.contains("s")) {
            timer.put(name, time);
        } else if (timeFormat.contains("m")) {
            time = time * 60;
            timer.put(name, time);
        } else if (timeFormat.contains("h")) {
            time = time * 3600;
            timer.put(name, time);
        } else timer.put(name, time);
        if (!name.contains("-Announcer")) {
            plugin.getInitialTime().put(name, time);
        }
    }

    private int taskId;
    private List<String> frames;
    private long period;

    public BukkitTask animateText(BossBarManager barManager) {
        return new BukkitRunnable() {
            int i = -1;

            @Override
            public void run() {
                taskId = this.getTaskId();
                if (frames.isEmpty()) frames.addAll(Arrays.asList("&cExample &fText", "&fExample &cText"));
                if (i <= frames.size()) i++;
                if (i >= frames.size()) i = 0;
                barManager.setBarName(frames.get(i).replace("{time}",
                        plugin.getUtilities().format((long) barManager.getTimeleft())));
            }
        }.runTaskTimer(plugin, 0L, period);
    }

    public void setFrames(List<String> frames) {
        this.frames = frames;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
