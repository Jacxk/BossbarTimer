package com.minestom.Runnables;

import com.minestom.BarMenuCreator.Api.BarEndEvent;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.BarsData;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.PlayerEditingData;
import com.minestom.Utils.Utilities;
import org.bukkit.Bukkit;
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
            BossBarManager barManager = barsData.getBossBarManager();

            if (timeLeft == 0) {
                barManager.setBarProgress(1, 1);
                utilities.executeCommand(barsData.getCommands());
                utilities.stop(barsData);

                timer.remove(barName, timeLeft);
                BarEndEvent barEndEvent = new BarEndEvent(plugin.getUtilities(), plugin.getBarDataMap().get(barName));
                Bukkit.getServer().getPluginManager().callEvent(barEndEvent);

                if (plugin.debug) MessageUtil.sendDebugMessage("Count down ended... Bar name: " + barName);

            } else {
                timer.put(barName, timeLeft - 1);
                barManager.setBarProgress(timeLeft, barsData.getInitialTime());
            }
            if (plugin.debug)
                MessageUtil.sendDebugMessage("Countdown -> Bar name " + barName + "\nTime left: " + timeLeft);
        }
        Bukkit.getOnlinePlayers().forEach(player -> {
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
        });
    }

}
