package com.minestom.Runnables;

import com.minestom.BarMenuCreator.Api.BarStartEvent;
import com.minestom.BossbarTimer;
import com.minestom.DataHandler.BarsData;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class Announcer extends BukkitRunnable {

    private BossbarTimer plugin;
    private Utilities utilities;

    public Announcer(BossbarTimer plugin) {
        this.plugin = plugin;
        this.utilities = plugin.getUtilities();
    }

    @Override
    public void run() {
        Map<String, Long> announcer = plugin.getAnnouncerTimer();
        for (Map.Entry<String, Long> entry : announcer.entrySet()) {
            String barName = entry.getKey();
            long time = entry.getValue();

            BarsData barsData = plugin.getBarDataMap().get(barName);

            if (time == 0) {
                long announcerTime = utilities.timeToSeconds(barsData.getAnnouncerTime());
                long countDownTime = utilities.timeToSeconds(barsData.getCountdownTime());

                announcer.put(barName, announcerTime);
                plugin.getTimer().put(barName, countDownTime);

                plugin.getUtilities().start(barsData);

                BarStartEvent barStartEvent = new BarStartEvent(plugin.getUtilities(), barsData);
                Bukkit.getServer().getPluginManager().callEvent(barStartEvent);

                if (plugin.debug) MessageUtil.sendDebugMessage("Announcing " + barName + "\nTime: " + time);
            } else announcer.put(barName, time - 1);

            if (plugin.debug) MessageUtil.sendDebugMessage("Announcer > Bar name " + barName + "\nTime to announce: " + time);
        }
    }
}
