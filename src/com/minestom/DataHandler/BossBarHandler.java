package com.minestom.DataHandler;

import com.minestom.BarMenuCreator.Api.BarEndEvent;
import com.minestom.BarMenuCreator.Api.BarStartEvent;
import com.minestom.BossbarTimer;
import com.minestom.Utils.BossBarManager;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.Utilities;
import org.bukkit.Bukkit;
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
    private BossBarManager bossBarManager;
    private long currentTime;
    private int framesTask;
    private int countDownTask;
    private BossbarTimer plugin;
    private Utilities utilities;
    private boolean running;

    public BossBarHandler(BossbarTimer plugin) {
        this.plugin = plugin;
        this.bossBarManager = new BossBarManager();
    }

    public BossBarHandler(BossbarTimer plugin, String barKeyName, List<String> nameFrames, List<String> commands, int namePeriod, String countdownTime, String color,
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
        this.bossBarManager = new BossBarManager();
        this.initialTime = initialTime;
        this.currentTime = utilities.timeToSeconds(countdownTime);
        this.running = false;
    }

    public void start() {
        if (plugin.debug) MessageUtil.sendDebugMessage("Stating countdown...");
        setRunning(true);
        setCurrentTime(utilities.timeToSeconds(countdownTime));

        getBossBarManager().setBarColor(getColor());
        getBossBarManager().setBarStyle(getStyle());

        Bukkit.getOnlinePlayers().forEach(getBossBarManager()::addPlayer);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (currentTime == 0) stop(true);
                else {
                    currentTime--;
                    bossBarManager.setBarProgress(currentTime, getInitialTime());
                }
                countDownTask = this.getTaskId();
            }
        }.runTaskTimer(plugin, 0L, 20L);

        startAnimatedTitle();

        BarStartEvent barStartEvent = new BarStartEvent(plugin.getUtilities(), this);
        Bukkit.getServer().getPluginManager().callEvent(barStartEvent);

    }

    public void startAnimatedTitle() {
        if (plugin.debug) MessageUtil.sendDebugMessage("Stating Animation...");
        new BukkitRunnable() {
            int i = -1;

            @Override
            public void run() {
                if (nameFrames.isEmpty()) nameFrames.addAll(Arrays.asList("&cExample &fText", "&fExample &cText"));
                if (i <= nameFrames.size()) i++;
                if (i >= nameFrames.size()) i = 0;
                bossBarManager.setBarName(nameFrames.get(i).replace("{time}", utilities.format(getCurrentTime())));
                framesTask = this.getTaskId();
            }
        }.runTaskTimer(plugin, 0L, namePeriod);
    }

    public void stop(boolean b) {
        Bukkit.getOnlinePlayers().forEach(getBossBarManager()::removeBar);
        setRunning(false);

        bossBarManager.setBarProgress(1, 1);
        if (b) utilities.executeCommand(getCommands());

        stopAnimatedTitle();
        Bukkit.getScheduler().cancelTask(countDownTask);

        BarEndEvent barEndEvent = new BarEndEvent(utilities, BossBarHandler.this);
        Bukkit.getServer().getPluginManager().callEvent(barEndEvent);

        if (plugin.debug) MessageUtil.sendDebugMessage("Stopping countdown...");
    }

    public void stopAnimatedTitle() {
        if (plugin.debug) MessageUtil.sendDebugMessage("Stopping Animation...");
        Bukkit.getScheduler().cancelTask(framesTask);
    }

    public String getBarKeyName() {
        return barKeyName;
    }

    public void setBarKeyName(String barKeyName) {
        this.barKeyName = barKeyName;
    }

    public List<String> getNameFrames() {
        return nameFrames;
    }

    public void setNameFrames(List<String> nameFrames) {
        this.nameFrames = nameFrames;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public int getNamePeriod() {
        return namePeriod;
    }

    public void setNamePeriod(int namePeriod) {
        this.namePeriod = namePeriod;
    }

    public String getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(String countdownTime) {
        this.countdownTime = countdownTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isAnnouncerEnabled() {
        return announcerEnabled;
    }

    public void setAnnouncerEnabled(boolean announcerEnabled) {
        this.announcerEnabled = announcerEnabled;
    }

    public String getAnnouncerTime() {
        return announcerTime;
    }

    public void setAnnouncerTime(String announcerTime) {
        this.announcerTime = announcerTime;
    }

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }
}
