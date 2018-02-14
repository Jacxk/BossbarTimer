package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarTimer;
import com.minestom.DataHandler.BossBarHandler;
import com.minestom.DataHandler.PlayerEditingData;
import com.minestom.Utils.BossBarManager;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.Utilities;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InChatEdition implements Listener {

    private BossBarTimer plugin;

    public InChatEdition(BossBarTimer plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Utilities utilities = plugin.getUtilities();
        Player player = event.getPlayer();

        if (!utilities.getPlayerEditingDataMap().containsKey(player)) return;

        PlayerEditingData editingData = utilities.getEditingData(player);
        BossBarHandler bossBarHandler = editingData.getBossBarHandler();
        BossBarManager barManager = bossBarHandler.getBossBarManager();
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
            for (String commandString : bossBarHandler.getCommands()) {
                if (bossBarHandler.getCommands() == null || bossBarHandler.getCommands().isEmpty()) break;
                lore.add(commandString);
            }
            lore.add(message);

            editingData.setAddingCmd(false);
            bossBarHandler.setCommands(lore);
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

            List<String> frames = new ArrayList<>(bossBarHandler.getNameFrames());
            frames.add(message);

            editingData.setEditingName(false);
            editingData.setEditing(true);
            bossBarHandler.setNameFrames(frames);
            BossbarMenuMaker.createEditMenu(player, plugin);

            bossBarHandler.stopAnimatedTitle();
            bossBarHandler.startAnimatedTitle();
        }
        if (editingData.isEditTimer()) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                editingData.setEditTimer(false);
                editingData.setEditing(true);
                BossbarMenuMaker.createEditMenu(player, plugin);
                return;
            }
            bossBarHandler.setCountdownTime(message);
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

            bossBarHandler.setBarKeyName(message.replace(" ", "_"));
            bossBarHandler.setNameFrames(Arrays.asList("&cExample &fText", "&fExample &cText"));
            bossBarHandler.setNamePeriod(20);
            bossBarHandler.setCountdownTime("1m 30s");
            bossBarHandler.setCurrentTime(utilities.timeToSeconds("1m 30s"));
            bossBarHandler.setColor("WHITE");
            bossBarHandler.setStyle("SOLID");
            bossBarHandler.setCommands(Arrays.asList("say first command", "say second command"));
            bossBarHandler.setAnnouncerEnabled(false);
            bossBarHandler.setAnnouncerTime("none");

            editingData.setBarKeyName(message.replace(" ", "_"));

            barManager.createBar(" ", "WHITE", "SOLID");
            barManager.addPlayer(player);

            bossBarHandler.startAnimatedTitle();

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
            bossBarHandler.setAnnouncerTime(message);
            editingData.setAnnouncerTime(false);
            editingData.setEditing(true);
            BossbarMenuMaker.createAvancedMenu(player, plugin);
        }
        if (editingData.isEditPeriod()) {
            event.setCancelled(true);

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
            bossBarHandler.setNamePeriod(Integer.valueOf(message));
            editingData.setEditPeriod(false);
            editingData.setEditing(true);
            BossbarMenuMaker.createEditMenu(player, plugin);

            bossBarHandler.stopAnimatedTitle();
            bossBarHandler.startAnimatedTitle();
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
