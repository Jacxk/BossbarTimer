package com.minestom;

import com.minestom.BarMenuCreator.Api.BarEndEvent;
import com.minestom.BarMenuCreator.Api.BarStartEvent;
import com.minestom.Utils.BarsData;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.PlayerEditingData;
import com.minestom.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class CountDown extends BukkitRunnable {

    private BossbarTimer plugin;
    private Utilities utilities;

    public CountDown(BossbarTimer plugin) {
        this.plugin = plugin;
        this.utilities = plugin.getUtilities();
    }

    @Override
    public void run() {
        Map<String, Long> timer = plugin.getTimer();
        if (timer.isEmpty()) return;
        for (Map.Entry<String, Long> entry : timer.entrySet()) {
            String barName = entry.getKey();
            long timeLeft = entry.getValue();
            BarsData barsData = plugin.getBarDataMap().get(barName);
            BossBarManager bossBar = plugin.getBarManagerMap().get(barsData);
            if (timeLeft == 0) {
                if (!barName.contains("-Announcer")) {
                    bossBar.setBarProgress(1, 1);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        bossBar.removeBar(player);
                    }
                    utilities.executeCommand(barsData.getCommands());
                    if (barsData.isAnnouncerEnabled()) {
                        utilities.formatTime(barName + "-Announcer", barsData.getAnnouncerTime());
                    }
                    Bukkit.getScheduler().cancelTask(utilities.getTaskId());
                    timer.remove(barName);
                    BarEndEvent barEndEvent = new BarEndEvent(plugin.getUtilities(), plugin.getBarDataMap().get(barName));
                    Bukkit.getServer().getPluginManager().callEvent(barEndEvent);
                } else {
                    String name = barName.replace("-Announcer", "");
                    BossBarManager barManager = plugin.getBarManagerMap().get(barsData);

                    timer.remove(barName);

                    String timeFormat = barsData.getCountdownTime();
                    long time = Long.parseLong(timeFormat.replaceAll("[a-zA-Z]", ""));

                    if (timeFormat.contains("s")) {
                        timer.put(name, time);
                    } else if (timeFormat.contains("m")) {
                        time *= 60;
                        timer.put(name, time);
                    } else if (timeFormat.contains("h")) {
                        time *= 3600;
                        timer.put(name, time);
                    } else timer.put(name, time);

                    plugin.getInitialTime().put(name, time);

                    barManager.setBarColor(barsData.getColor().toUpperCase());
                    barManager.setBarStyle(barsData.getStyle().toUpperCase());

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        barManager.addPlayer(player);
                    }
                    BarStartEvent barStartEvent = new BarStartEvent(plugin.getUtilities(), plugin.getBarDataMap().get(barName));
                    Bukkit.getServer().getPluginManager().callEvent(barStartEvent);
                }
            } else {
                timer.put(barName, timeLeft - 1);
                if (!barName.contains("-Announcer")) {
                    bossBar.setBarProgress(timeLeft - 1, plugin.getInitialTime().get(barName));
                    bossBar.setTimeleft(timeLeft - 1);
                }
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!utilities.getPlayerEditingDataMap().containsKey(player)) return;
            PlayerEditingData editingData = utilities.getEditingData(player);
            if (editingData.isEditingName()) {
                MessageUtil.sendTitle(player, "§aPlease enter the name", "§cin the chat", 0, 25, 0);
            }
            if (editingData.isEditTimer()) {
                MessageUtil.sendTitle(player, "§aPlease enter the time", "§cin the chat", 0, 25, 0);
            }
            if (editingData.isAnnouncerTime()) {
                MessageUtil.sendTitle(player, "§aPlease enter the time", "§cin the chat", 0, 25, 0);
            }
            if (editingData.isCreateBar()) {
                MessageUtil.sendTitle(player, "§aPlease enter the name", "§cin the chat", 0, 25, 0);
            }
            if (editingData.isEditPeriod()) {
                MessageUtil.sendTitle(player, "§aPlease enter the time", "§cin the chat", 0, 25, 0);
            }
        }
    }
}
