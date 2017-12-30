package com.minestom.BarInterface.BarListener;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NameTimeEdit implements Listener {

    private BossbarTimer plugin;

    public NameTimeEdit(BossbarTimer plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        String message = event.getMessage();

        Map<String, String> values = plugin.getCreateBarValues().get(plugin.getBarKeyName().get(player));
        BossBarManager barManager = plugin.getBarManagerMap().get(plugin.getBarKeyName().get(player));

        if (event.isCancelled()) return;

        if (plugin.containsAddingCmd(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeAddingCmd(player);
                plugin.setEditing(player);
                BossbarInterface.createAvancedMenu(player, plugin);
                return;
            }
            List<String> lore = new ArrayList<>();
            for (String cmds : values.get("Commands").split(", ")) {
                if (values.get("Commands").isEmpty()) continue;
                lore.add(cmds.replaceAll("[\\[\\]]", ""));
            }
            lore.add(message);
            plugin.removeAddingCmd(player);
            values.put("Commands", lore.toString());
            plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
            BossbarInterface.createAvancedMenu(player, plugin);
        }
        if (plugin.containsEditingName(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeEditingName(player);
                plugin.setEditing(player);
                BossbarInterface.createEditMenu(player, plugin);
                return;
            }

            List<String> frames = new ArrayList<>();
            for (String cmds : values.get("DisplayName").split(", ")) {
                if (values.get("DisplayName").isEmpty()) continue;
                frames.add(cmds.replaceAll("[\\[\\]]", ""));
            }
            frames.add(message);

            plugin.removeEditingName(player);
            plugin.setEditing(player);
            values.put("DisplayName", frames.toString());
            plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
            BossbarInterface.createEditMenu(player, plugin);

            plugin.getUtilities().cancel(frames, Long.parseLong(values.get("Period")), barManager);
            BukkitTask task = plugin.getUtilities().animateText(frames, Long.parseLong(values.get("Period")), barManager);
            task.cancel();
            BukkitTask task2 = plugin.getUtilities().animateText(frames, Long.parseLong(values.get("Period")), barManager);
        }
        if (plugin.containsEditTimer(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeEditTimer(player);
                plugin.setEditing(player);
                BossbarInterface.createEditMenu(player, plugin);
                return;
            }
            values.put("Time", message);
            plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
            plugin.removeEditTimer(player);
            plugin.setEditing(player);
            BossbarInterface.createAvancedMenu(player, plugin);
        }
        if (plugin.containsCreatingBar(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                BossbarInterface.createMainMenu(player);
                plugin.removeCreatingBar(player);
                return;
            }
            for (String bars : plugin.getBarManagerMap().keySet()) {
                if (message.equals(bars)) {
                    player.sendMessage("There is a bar with that name! Try another name!");
                    return;
                }
            }
            plugin.getBarValues().put("DisplayName", "[&cAnimated Text, &fAnimated Text]");
            plugin.getBarValues().put("Period", "10");
            plugin.getBarValues().put("Time", "0s");
            plugin.getBarValues().put("Color", "White");
            plugin.getBarValues().put("Style", "Solid");
            plugin.getBarValues().put("Commands", "[say test, say another test]");
            plugin.getBarValues().put("AnnouncerModeEnabled", "false");
            plugin.getBarValues().put("AnnouncerModeTime", "none");

            plugin.getCreateBarValues().put(message.replace(" ", "_"), plugin.getBarValues());
            plugin.getBarKeyName().put(player, message.replace(" ", "_"));
            plugin.removeCreatingBar(player);
            plugin.setEditing(player);
            BossbarInterface.createEditMenu(player, plugin);
            barManager.createBar("Title", "white", "solid");
            barManager.addPlayer(player);
        }
        if (plugin.containsAnnouncerTime(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeAnnouncerTime(player);
                plugin.setEditing(player);
                BossbarInterface.createEditMenu(player, plugin);
                return;
            }
            values.put("AnnouncerModeTime", message);
            plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
            plugin.removeAnnouncerTime(player);
            plugin.setEditing(player);
            BossbarInterface.createAvancedMenu(player, plugin);
        }
        if (plugin.containsEditPeriod(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeEditPeriod(player);
                plugin.setEditing(player);
                BossbarInterface.createEditMenu(player, plugin);
                return;
            }
            if (!StringUtils.isNumeric(message)) {
                player.sendMessage("You need to enter the time in a number.");
                return;
            }
            values.put("Period", message);
            plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
            plugin.removeEditPeriod(player);
            plugin.setEditing(player);
            BossbarInterface.createEditMenu(player, plugin);

            List<String> frames = new ArrayList<>();

            for (String cmds : values.get("DisplayName").split(", ")) {
                if (values.get("DisplayName").isEmpty()) continue;
                frames.add(cmds.replaceAll("[\\[\\]]", ""));
            }

            BukkitTask task = plugin.getUtilities().animateText(frames, Long.parseLong(values.get("Period")), barManager);
            task.cancel();
            BukkitTask task2 = plugin.getUtilities().animateText(frames, Long.parseLong(values.get("Period")), barManager);
        }
    }
}
