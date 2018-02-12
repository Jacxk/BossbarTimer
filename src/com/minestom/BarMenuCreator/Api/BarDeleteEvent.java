package com.minestom.BarMenuCreator.Api;

import com.minestom.DataHandler.BossBarHandler;
import com.minestom.Utils.Utilities;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class BarDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Utilities utilities;
    private boolean announcer;
    private int namePeriod;
    private String announcerTime;
    private String countDownTime;
    private String keyName;
    private String color;
    private String style;
    private List<String> commands;
    private List<String> frames;

    public BarDeleteEvent(Utilities utilities, BossBarHandler bossBarHandler) {
        this.utilities = utilities;
        this.announcer = bossBarHandler.isAnnouncerEnabled();
        this.namePeriod = bossBarHandler.getNamePeriod();
        this.announcerTime = bossBarHandler.getAnnouncerTime();
        this.countDownTime = bossBarHandler.getCountdownTime();
        this.keyName = bossBarHandler.getBarKeyName();
        this.color = bossBarHandler.getColor();
        this.style = bossBarHandler.getStyle();
        this.commands = bossBarHandler.getCommands();
        this.frames = bossBarHandler.getNameFrames();
    }

    @Override
    public String toString() {
        return String.format("KeyName: %s, Announcer: %s, Name Frames: %s, Name Refresh Rate: %s ticks, Announce every: %s, " +
                        "CountDown Timer: %s, Color: %s, Style: %s, Commands: %s", keyName, announcer, frames.toString(),
                namePeriod, announcerTime, countDownTime, color, style, commands.toString());
    }

    public String getkeyName() {
        return keyName;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isAnnouncer() {
        return announcer;
    }

    public int getNamePeriod() {
        return namePeriod;
    }

    public String getAnnouncerTime() {
        return announcerTime;
    }

    public Long getAnnouncerTimeToMillis() {
        return utilities.timeToMillis(announcerTime);
    }

    public String getCountDownToString() {
        return countDownTime;
    }

    public Long getCountDownToMillis() {
        return utilities.timeToMillis(countDownTime);
    }

    public String getKeyName() {
        return keyName;
    }

    public String getColor() {
        return color;
    }

    public String getStyle() {
        return style;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getFrames() {
        return frames;
    }
}
