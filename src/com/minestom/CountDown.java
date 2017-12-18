package com.minestom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class CountDown extends BukkitRunnable {

    private BossbarTimer plugin;

    public CountDown(BossbarTimer plugin) {
        this.plugin = plugin;
    }


    @Override
    public void run() {
        Map<String, Double> timer = plugin.getTimer();
        for (Map.Entry<String, Double> entry : timer.entrySet()) {
            String barName = entry.getKey();
            double timeLeft = entry.getValue();
            BossBarManager bossBar = plugin.getBarManagerMap().get(barName);
            if (timeLeft == 0) {
                timer.remove(barName);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    bossBar.removeBar(player);
                }
                for (String command : plugin.getConfig().getStringList("Bars." + barName + ".Commands")) {
                    plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            } else {
                timer.put(barName, timeLeft - 1);
                bossBar.setBarProgress(timeLeft - 1, plugin.getInitialTime().get(barName));
                bossBar.setBarName(plugin.getConfig().getString("Bars." + barName + ".DisplayName").replace("{time}",
                        plugin.getUtilities().format((long) timeLeft - 1)));
            }
        }
    }
}
