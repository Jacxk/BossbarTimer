package com.minestom;

import com.minestom.DataHandler.BossBarHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private BossBarTimer plugin;

    public JoinListener(BossBarTimer plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (BossBarHandler bossBarHandler : plugin.getBarDataMap().values()) {
            if (!bossBarHandler.isRunning()) continue;
            bossBarHandler.addPlayer(player);
        }
        sendUpdate(player);
    }

    private void sendUpdate(Player player) {
        if (player.hasPermission("bossbartimer.update.notification"))
            plugin.getUpdate().sendUpdateMessage(player);
    }

}
