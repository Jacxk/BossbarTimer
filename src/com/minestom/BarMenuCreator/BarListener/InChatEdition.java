package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.Utilities;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InChatEdition implements Listener {

    private BossbarTimer plugin;

    public InChatEdition(BossbarTimer plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Utilities utilities = plugin.getUtilities();

        Player player = event.getPlayer();
        String message = event.getMessage();

        Map<String, String> values = plugin.getCreateBarValues().get(plugin.getBarKeyName().get(player));

        if (event.isCancelled()) return;

        if (plugin.containsAddingCmd(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeAddingCmd(player);
                plugin.setEditing(player);
                BossbarMenuMaker.createAvancedMenu(player, plugin);
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
            BossbarMenuMaker.createAvancedMenu(player, plugin);
        }
        if (plugin.containsEditingName(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeEditingName(player);
                plugin.setEditing(player);
                BossbarMenuMaker.createEditMenu(player, plugin);
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
            BossbarMenuMaker.createEditMenu(player, plugin);

            utilities.setFrames(frames);
        }
        if (plugin.containsEditTimer(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeEditTimer(player);
                plugin.setEditing(player);
                BossbarMenuMaker.createEditMenu(player, plugin);
                return;
            }
            values.put("Time", message);
            plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
            plugin.removeEditTimer(player);
            plugin.setEditing(player);
            BossbarMenuMaker.createAvancedMenu(player, plugin);
        }
        if (plugin.containsCreatingBar(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                BossbarMenuMaker.createMainMenu(player);
                plugin.removeCreatingBar(player);
                return;
            }
            for (String bars : plugin.getBarManagerMap().keySet()) {
                if (message.equals(bars)) {
                    MessageUtil.sendMessage(player, "There is a bar with that name! Try another name!");
                    return;
                }
            }
            plugin.getBarValues().put("DisplayName", "[&cExample &fText, &fExample &cText]");
            plugin.getBarValues().put("Period", "10");
            plugin.getBarValues().put("Time", "0s");
            plugin.getBarValues().put("Color", "White");
            plugin.getBarValues().put("Style", "Solid");
            plugin.getBarValues().put("Commands", "[say test, say another test]");
            plugin.getBarValues().put("AnnouncerModeEnabled", "false");
            plugin.getBarValues().put("AnnouncerModeTime", "none");

            plugin.getCreateBarValues().put(message.replace(" ", "_"), plugin.getBarValues());
            plugin.getBarKeyName().put(player, message.replace(" ", "_"));
            plugin.getBarManagerMap().put(plugin.getBarKeyName().get(player), new BossBarManager(plugin));
            BossBarManager barManager = plugin.getBarManagerMap().get(plugin.getBarKeyName().get(player));

            barManager.createBar(" ", "WHITE", "SOLID");
            barManager.setFinished(false);
            barManager.addPlayer(player);

            plugin.getUtilities().setFrames(Arrays.asList("&cExample &fText", "&fExample &cText"));
            plugin.getUtilities().setPeriod(20);
            plugin.getUtilities().animateText(barManager);

            BossbarMenuMaker.createEditMenu(player, plugin);
            plugin.removeCreatingBar(player);
            plugin.setEditing(player);
        }
        if (plugin.containsAnnouncerTime(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeAnnouncerTime(player);
                plugin.setEditing(player);
                BossbarMenuMaker.createEditMenu(player, plugin);
                return;
            }
            values.put("AnnouncerModeTime", message);
            plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
            plugin.removeAnnouncerTime(player);
            plugin.setEditing(player);
            BossbarMenuMaker.createAvancedMenu(player, plugin);
        }
        if (plugin.containsEditPeriod(player)) {
            event.setCancelled(true);
            BossBarManager barManager = plugin.getBarManagerMap().get(plugin.getBarKeyName().get(player));

            if (message.equalsIgnoreCase("cancel")) {
                plugin.removeEditPeriod(player);
                plugin.setEditing(player);
                BossbarMenuMaker.createEditMenu(player, plugin);
                return;
            }
            if (!StringUtils.isNumeric(message)) {
                MessageUtil.sendMessage(player, "You need to enter the time in a number.");
                return;
            }
            values.put("Period", message);
            plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
            plugin.removeEditPeriod(player);
            plugin.setEditing(player);
            BossbarMenuMaker.createEditMenu(player, plugin);

            List<String> frames = new ArrayList<>();

            for (String cmds : values.get("DisplayName").split(", ")) {
                if (values.get("DisplayName").isEmpty()) continue;
                frames.add(cmds.replaceAll("[\\[\\]]", ""));
            }

            Bukkit.getScheduler().cancelTask(utilities.getTaskId());
            utilities.setPeriod(Long.parseLong(values.get("Period")));
            utilities.setFrames(frames);
            utilities.animateText(barManager);
        }
    }
}
