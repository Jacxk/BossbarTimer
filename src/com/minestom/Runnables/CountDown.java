package com.minestom.Runnables;

import com.minestom.DataHandler.PlayerEditingData;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CountDown extends BukkitRunnable {

    private Utilities utilities;

    public CountDown(Utilities utilities) {
        this.utilities = utilities;
    }

    @Override
    public void run() {
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
