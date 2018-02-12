package com.minestom.Runnables;

import com.minestom.BossbarTimer;
import com.minestom.DataHandler.BossBarHandler;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.Utilities;
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

            BossBarHandler bossBarHandler = plugin.getBarDataMap().get(barName);

            if (time == 0) {
                long announcerTime = utilities.timeToSeconds(bossBarHandler.getAnnouncerTime());

                announcer.put(barName, announcerTime);
                bossBarHandler.start();

                if (plugin.debug) MessageUtil.sendDebugMessage("Announcing " + barName + "\nTime: " + time);
            } else announcer.put(barName, time - 1);
        }
    }
}
