package com.minestom;

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
        Map<String, Double> timer = plugin.getTimer();
        if (timer.isEmpty()) return;
        for (Map.Entry<String, Double> entry : timer.entrySet()) {
            String barName = entry.getKey();
            double timeLeft = entry.getValue();
            BossBarManager bossBar = plugin.getBarManagerMap().get(barName);
            if (timeLeft == 0) {
                if (!barName.contains("-Announcer")) {
                    bossBar.setBarProgress(1, 1);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        bossBar.removeBar(player);
                    }
                    utilities.executeCommand(plugin.getConfig().getStringList("Bars." + barName + ".Commands"));
                    if (plugin.getConfig().getString("Bars." + barName + ".AnnouncerMode.Enabled") != null &&
                            plugin.getConfig().getString("Bars." + barName + ".AnnouncerMode.Enabled").equalsIgnoreCase("True")) {
                        utilities.formatTime(barName + "-Announcer", plugin.getConfig().getString("Bars." + barName + ".AnnouncerMode.Time"));
                    }
                    Bukkit.getScheduler().cancelTask(utilities.getTaskId());
                    timer.remove(barName);
                } else {
                    String name = barName.replace("-Announcer", "");
                    BossBarManager barManager = plugin.getBarManagerMap().get(name);

                    timer.remove(barName);

                    String timeFormat = plugin.getConfig().getString("Bars." + name + ".Time");
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

                    plugin.getInitialTime().put(name, time);

                    barManager.setBarColor(plugin.getConfig().getString("Bars." + name + ".Color").toUpperCase());
                    barManager.setBarStyle(plugin.getConfig().getString("Bars." + name + ".Style").toUpperCase());

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        barManager.addPlayer(player);
                    }
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
