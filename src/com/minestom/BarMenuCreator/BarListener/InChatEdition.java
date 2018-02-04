package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.BarsData;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.PlayerEditingData;
import com.minestom.Utils.Utilities;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

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
        BarsData barsData = editingData.getBarsData();
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
            for (String commandString : barsData.getCommands()) {
                if (barsData.getCommands() == null || barsData.getCommands().isEmpty()) break;
                lore.add(commandString);
            }
            lore.add(message);

            editingData.setAddingCmd(false);
            barsData.setCommands(lore);
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

            List<String> frames = new ArrayList<>(barsData.getNameFrames());
            frames.add(message);

            editingData.setEditingName(false);
            editingData.setEditing(true);
            barsData.setNameFrames(frames);
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
            barsData.setCountdownTime(message);
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
            for (String bars : plugin.getBarDataMap().keySet()) {
                if (message.equals(bars)) {
                    MessageUtil.sendMessage(player, "There is a bar with that name! Try another name!");
                    return;
                }
            }

            barsData.setBarKeyName(message.replace(" ", "_"));
            barsData.setNameFrames(Arrays.asList("&cExample &fText", "&fExample &cText"));
            barsData.setNamePeriod(20);
            barsData.setCountdownTime("1m 30s");
            barsData.setColor("WHITE");
            barsData.setStyle("SOLID");
            barsData.setCommands(Arrays.asList("say first command", "say second command"));
            barsData.setAnnouncerEnabled(false);
            barsData.setAnnouncerTime("none");

            editingData.setBarKeyName(message.replace(" ", "_"));
            plugin.getBarManagerMap().put(editingData.getBarsData(), new BossBarManager(plugin));
            BossBarManager barManager = plugin.getBarManagerMap().get(editingData.getBarsData());

            barManager.createBar(" ", "WHITE", "SOLID");
            barManager.addPlayer(player);

            utilities.setFrames(barsData.getNameFrames());
            utilities.setPeriod(barsData.getNamePeriod());
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
            barsData.setAnnouncerTime(message);
            editingData.setAnnouncerTime(false);
            editingData.setEditing(true);
            BossbarMenuMaker.createAvancedMenu(player, plugin);
        }
        if (editingData.isEditPeriod()) {
            event.setCancelled(true);
            BossBarManager barManager = plugin.getBarManagerMap().get(editingData.getBarsData());

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
            barsData.setNamePeriod(Integer.valueOf(message));
            editingData.setEditPeriod(false);
            editingData.setEditing(true);
            BossbarMenuMaker.createEditMenu(player, plugin);

            Bukkit.getScheduler().cancelTask(utilities.getTaskId());
            utilities.setPeriod(barsData.getNamePeriod());
            utilities.setFrames(barsData.getNameFrames());
            utilities.animateText(barManager);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;

        Utilities utilities = plugin.getUtilities();
        Player player = event.getPlayer();

        if (!utilities.getPlayerEditingDataMap().containsKey(player)) return;

        PlayerEditingData editingData = utilities.getEditingData(player);

        if (editingData.isAnnouncerTime() || editingData.isCreateBar() || editingData.isEditTimer() ||
                editingData.isEditingName() || editingData.isEditPeriod() || editingData.isAddingCmd())
            event.setCancelled(true);

    }
}
