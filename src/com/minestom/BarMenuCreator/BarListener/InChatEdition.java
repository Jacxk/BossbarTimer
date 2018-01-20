package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.PlayerEditingData;
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

public class InChatEdition implements Listener {

    private BossbarTimer plugin;

    public InChatEdition(BossbarTimer plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Utilities utilities = plugin.getUtilities();
        Player player = event.getPlayer();

        if (!utilities.getPlayerEditingDataMap().containsKey(player)) return;

        PlayerEditingData editingData = utilities.getEditingData(player);
        String message = event.getMessage();

        if (editingData.isAddingCmd()) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                editingData.setAddingCmd(false);
                editingData.setEditing(true);

                BossbarMenuMaker.createAvancedMenu(player, plugin);
                return;
            }

            List<String> lore = new ArrayList<>();
            for (String cmds : editingData.getBarValue("Commands").split(", ")) {
                if (editingData.getBarValue("Commands").isEmpty()) continue;
                lore.add(cmds.replaceAll("[\\[\\]]", ""));
            }
            lore.add(message);

            editingData.setAddingCmd(false);
            editingData.addBarValue("Commands", lore.toString());
            BossbarMenuMaker.createAvancedMenu(player, plugin);
        }
        if (editingData.isEditingName()) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                editingData.setEditingName(false);
                editingData.setEditing(true);
                BossbarMenuMaker.createEditMenu(player, plugin);
                return;
            }

            List<String> frames = new ArrayList<>();
            for (String cmds : editingData.getBarValue("DisplayName").split(", ")) {
                if (editingData.getBarValue("DisplayName").isEmpty()) continue;
                frames.add(cmds.replaceAll("[\\[\\]]", ""));
            }
            frames.add(message);

            editingData.setEditingName(false);
            editingData.setEditing(true);
            editingData.addBarValue("DisplayName", frames.toString());
            BossbarMenuMaker.createEditMenu(player, plugin);

            utilities.setFrames(frames);
        }
        if (editingData.isEditTimer()) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                editingData.setEditTimer(false);
                editingData.setEditing(true);
                BossbarMenuMaker.createEditMenu(player, plugin);
                return;
            }
            editingData.addBarValue("Time", message);
            editingData.setEditTimer(false);
            editingData.setEditing(true);
            BossbarMenuMaker.createAvancedMenu(player, plugin);
        }
        if (editingData.isCreateBar()) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                BossbarMenuMaker.createMainMenu(player);
                editingData.setCreateBar(false);
                return;
            }
            for (String bars : plugin.getBarManagerMap().keySet()) {
                if (message.equals(bars)) {
                    MessageUtil.sendMessage(player, "There is a bar with that name! Try another name!");
                    return;
                }
            }
            editingData.addBarValue("DisplayName", "[&cExample &fText, &fExample &cText]");
            editingData.addBarValue("Period", "10");
            editingData.addBarValue("Time", "0s");
            editingData.addBarValue("Color", "White");
            editingData.addBarValue("Style", "Solid");
            editingData.addBarValue("Commands", "[say test, say another test]");
            editingData.addBarValue("AnnouncerModeEnabled", "false");
            editingData.addBarValue("AnnouncerModeTime", "none");

            editingData.setBarKeyName(message.replace(" ", "_"));
            plugin.getBarManagerMap().put(editingData.getBarKeyName(), new BossBarManager(plugin));
            BossBarManager barManager = plugin.getBarManagerMap().get(editingData.getBarKeyName());

            barManager.createBar(" ", "WHITE", "SOLID");
            barManager.setFinished(false);
            barManager.addPlayer(player);

            utilities.setFrames(Arrays.asList("&cExample &fText", "&fExample &cText"));
            utilities.setPeriod(20);
            utilities.animateText(barManager);

            BossbarMenuMaker.createEditMenu(player, plugin);
            editingData.setCreateBar(false);
            editingData.setEditing(true);
        }
        if (editingData.isAnnouncerTime()) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                editingData.setAnnouncerTime(false);
                editingData.setEditing(true);
                BossbarMenuMaker.createEditMenu(player, plugin);
                return;
            }
            editingData.addBarValue("AnnouncerModeTime", message);
            editingData.setAnnouncerTime(false);
            editingData.setEditing(true);
            BossbarMenuMaker.createAvancedMenu(player, plugin);
        }
        if (editingData.isEditPeriod()) {
            event.setCancelled(true);
            BossBarManager barManager = plugin.getBarManagerMap().get(editingData.getBarKeyName());

            if (message.equalsIgnoreCase("cancel")) {
                editingData.setEditPeriod(false);
                editingData.setEditing(true);
                BossbarMenuMaker.createEditMenu(player, plugin);
                return;
            }
            if (!StringUtils.isNumeric(message)) {
                MessageUtil.sendMessage(player, "You need to enter the time in a number.");
                return;
            }
            editingData.addBarValue("Period", message);
            editingData.setEditPeriod(false);
            editingData.setEditing(true);
            BossbarMenuMaker.createEditMenu(player, plugin);

            List<String> frames = new ArrayList<>();

            for (String cmds : editingData.getBarValue("DisplayName").split(", ")) {
                if (editingData.getBarValue("DisplayName").isEmpty()) continue;
                frames.add(cmds.replaceAll("[\\[\\]]", ""));
            }

            Bukkit.getScheduler().cancelTask(utilities.getTaskId());
            utilities.setPeriod(Long.parseLong(editingData.getBarValue("Period")));
            utilities.setFrames(frames);
            utilities.animateText(barManager);
        }
    }
}
