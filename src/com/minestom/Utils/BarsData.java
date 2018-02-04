package com.minestom.Utils;

import java.util.List;

public class BarsData {

    private List<String> nameFrames;
    private List<String> commands;
    private int namePeriod;
    private String countdownTime;
    private String color;
    private String style;
    private boolean announcerEnabled;
    private String announcerTime;
    private String barKeyName;

    public BarsData() {}

    public BarsData(String barKeyName, List<String> nameFrames, List<String> commands, int namePeriod, String countdownTime, String color,
                    String style, boolean announcerEnabled, String announcerTime) {
        this.barKeyName = barKeyName;
        this.nameFrames = nameFrames;
        this.commands = commands;
        this.namePeriod = namePeriod;
        this.countdownTime = countdownTime;
        this.color = color;
        this.style = style;
        this.announcerEnabled = announcerEnabled;
        this.announcerTime = announcerTime;
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
}
