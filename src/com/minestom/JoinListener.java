package com.minestom;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class JoinListener implements Listener {

    private BossbarTimer plugin;

    public JoinListener(BossbarTimer plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Map<String, Double> timer = plugin.getTimer();
        for (Map.Entry<String, Double> entry : timer.entrySet()) {
            if (!entry.getKey().contains("-Announcer")) {
                BossBarManager barManager = plugin.getBarManagerMap().get(entry.getKey());
                barManager.addPlayer(player);
            }
        }
    }
}