package com.minestom.DataHandler;

import com.minestom.Api.BarEndEvent;
import com.minestom.Api.BarStartEvent;
import com.minestom.BossBarTimer;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class BossBarHandler {

    private List<String> nameFrames;
    private List<String> commands;
    private int namePeriod;
    private long initialTime;
    private boolean announcerEnabled;
    private String countdownTime;
    private String color;
    private String style;
    private String announcerTime;
    private String barKeyName;
    private long currentTime;
    private int framesTask;
    private int countDownTask;
    private BossBarTimer plugin;
    private Utilities utilities;
    private boolean running;
    private BossBar bar;

    public BossBarHandler(BossBarTimer plugin) {
        this.plugin = plugin;
        this.utilities = plugin.getUtilities();
        this.bar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
        this.running = false;
    }

    public BossBarHandler(BossBarTimer plugin, String barKeyName, List<String> nameFrames, List<String> commands, int namePeriod, String countdownTime, String color,
                          String style, boolean announcerEnabled, String announcerTime, long initialTime) {
        this.plugin = plugin;
        this.utilities = plugin.getUtilities();
        this.barKeyName = barKeyName;
        this.nameFrames = nameFrames;
        this.commands = commands;
        this.namePeriod = namePeriod;
        this.countdownTime = countdownTime;
        this.color = color;
        this.style = style;
        this.announcerEnabled = announcerEnabled;
        this.announcerTime = announcerTime;
        this.initialTime = initialTime;
        this.currentTime = utilities.timeToSeconds(countdownTime);
        this.bar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
        this.running = false;
    }

    public BossBarHandler start() {
        if (plugin.debug) MessageUtil.sendDebugMessage("Stating countdown...");
        setRunning(true);
        setCurrentTime(utilities.timeToSeconds(countdownTime));

        setBarColor(getColor());
        setBarStyle(getStyle());

        Bukkit.getOnlinePlayers().forEach(this::addPlayer);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (currentTime == 0) stop(true);
                else {
                    currentTime--;
                    setBarProgress(currentTime, getInitialTime());
                }
                countDownTask = this.getTaskId();
            }
        }.runTaskTimer(plugin, 0L, 20L);

        startAnimatedTitle();

        BarStartEvent barStartEvent = new BarStartEvent(plugin.getUtilities(), this);
        Bukkit.getServer().getPluginManager().callEvent(barStartEvent);

        return this;
    }

    public BossBarHandler startAnimatedTitle() {
        if (plugin.debug) MessageUtil.sendDebugMessage("Stating Animation...");
        new BukkitRunnable() {
            int i = -1;

            @Override
            public void run() {
                if (nameFrames.isEmpty()) nameFrames.addAll(Arrays.asList("&cExample &fText", "&fExample &cText"));
                if (i <= nameFrames.size()) i++;
                if (i >= nameFrames.size()) i = 0;
                setBarName(nameFrames.get(i).replace("{time}", utilities.format(getCurrentTime())));
                framesTask = this.getTaskId();
            }
        }.runTaskTimer(plugin, 0L, namePeriod);
        if (plugin.debug) MessageUtil.sendDebugMessage("Stopping countdown...");
        return this;
    }

    public void stop(boolean b) {
        Bukkit.getOnlinePlayers().forEach(this::removeBar);
        setRunning(false);

        setBarProgress(1, 1);
        if (b) utilities.executeCommand(getCommands());

        stopAnimatedTitle();
        Bukkit.getScheduler().cancelTask(countDownTask);

        BarEndEvent barEndEvent = new BarEndEvent(utilities, BossBarHandler.this);
        Bukkit.getServer().getPluginManager().callEvent(barEndEvent);

        if (plugin.debug) MessageUtil.sendDebugMessage("Stopping countdown...");
    }

    public BossBarHandler stopAnimatedTitle() {
        if (plugin.debug) MessageUtil.sendDebugMessage("Stopping Animation...");
        Bukkit.getScheduler().cancelTask(framesTask);
        return this;
    }

    public String getBarKeyName() {
        return barKeyName;
    }

    public BossBarHandler setBarKeyName(String barKeyName) {
        this.barKeyName = barKeyName;
        return this;
    }

    public List<String> getNameFrames() {
        return nameFrames;
    }

    public BossBarHandler setNameFrames(List<String> nameFrames) {
        this.nameFrames = nameFrames;
        return this;
    }

    public List<String> getCommands() {
        return commands;
    }

    public BossBarHandler setCommands(List<String> commands) {
        this.commands = commands;
        return this;
    }

    public int getNamePeriod() {
        return namePeriod;
    }

    public BossBarHandler setNamePeriod(int namePeriod) {
        this.namePeriod = namePeriod;
        return this;
    }

    public String getCountdownTime() {
        return countdownTime;
    }

    public BossBarHandler setCountdownTime(String countdownTime) {
        this.countdownTime = countdownTime;
        return this;
    }

    public String getColor() {
        return color;
    }

    public BossBarHandler setColor(String color) {
        this.color = color;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public BossBarHandler setStyle(String style) {
        this.style = style;
        return this;
    }

    public boolean isAnnouncerEnabled() {
        return announcerEnabled;
    }

    public BossBarHandler setAnnouncerEnabled(boolean announcerEnabled) {
        this.announcerEnabled = announcerEnabled;
        return this;
    }

    public String getAnnouncerTime() {
        return announcerTime;
    }

    public BossBarHandler setAnnouncerTime(String announcerTime) {
        this.announcerTime = announcerTime;
        return this;
    }

    private long getInitialTime() {
        return initialTime;
    }

    public BossBarHandler setInitialTime(long initialTime) {
        this.initialTime = initialTime;
        return this;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public BossBarHandler setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public BossBarHandler createBar(String title, String color, String style) {
        bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', title),
                BarColor.valueOf(color.toUpperCase()), BarStyle.valueOf(style.toUpperCase()));
        return this;
    }

    public BossBarHandler addPlayer(Player player) {
        if (!bar.getPlayers().contains(player) && bar != null) {
            bar.addPlayer(player);
        }
        return this;
    }

    public BossBarHandler setBarProgress(double progress, double initialTime) {
        double barProgress = initialTime / progress;
        bar.setProgress(1 / barProgress);
        return this;
    }

    public boolean containsBar(Player player) {
        return bar.getPlayers().contains(player);
    }

    public void removeBar(Player player) {
        bar.removePlayer(player);
    }

    public String getBarColor() {
        return bar.getColor().name();
    }

    public BossBarHandler setBarColor(String color) {
        bar.setColor(BarColor.valueOf(color.toUpperCase()));
        return this;
    }

    public String getBarStyle() {
        return bar.getStyle().name().replace("_", "");
    }

    public BossBarHandler setBarStyle(String style) {
        bar.setStyle(BarStyle.valueOf(style.toUpperCase()));
        return this;
    }

    public String getBarName() {
        return bar.getTitle();
    }

    public BossBarHandler setBarName(String text) {
        bar.setTitle(ChatColor.translateAlternateColorCodes('&', text));
        return this;
    }
}
