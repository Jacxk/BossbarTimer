package com.minestom.Runnables;

import com.minestom.BossBarTimer;
import com.minestom.DataHandler.BossBarHandler;
import com.minestom.DataHandler.PlayerEditingData;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class Announcer extends BukkitRunnable {

    private BossBarTimer plugin;
    private Utilities utilities;

    public Announcer(BossBarTimer plugin) {
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
            } else if (time > 1) announcer.put(barName, time - 1);
        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!utilities.getPlayerEditingDataMap().containsKey(player)) return;
            PlayerEditingData editingData = utilities.getEditingData(player);
            if (editingData.isEditingName() || editingData.isCreateBar()) {
                MessageUtil.sendTitle(player, "§aPlease enter the name", "§cin the chat", 0, 25, 0);
            }
            if (editingData.isEditTimer() || editingData.isAnnouncerTime() || editingData.isEditPeriod()) {
                MessageUtil.sendTitle(player, "§aPlease enter the time", "§cin the chat", 0, 25, 0);
            }
        });
    }
}
