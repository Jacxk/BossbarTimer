package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.PlayerEditingData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConfirmMenu implements Listener {

    private BossbarTimer plugin;

    public ConfirmMenu(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        InventoryType.SlotType slotType = event.getSlotType();
        Inventory inventory = event.getClickedInventory();
        String inventoryName = event.getView().getTopInventory().getTitle();

        if (inventoryName.equals("Confirm...") && slotType != InventoryType.SlotType.OUTSIDE
                && inventory.getType() == InventoryType.HOPPER) {

            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
            BossBarManager barManager = plugin.getBarManagerMap().get(editingData.getBarKeyName());
            FileConfiguration configuration = plugin.getConfig();

            int slot = event.getRawSlot();

            if (slot == 1 && item.hasItemMeta()) {
                String barName = editingData.getBarKeyName();
                String section = "Bars." + barName;

                if (editingData.isDeleting()) {

                    barManager.removeBar(player);
                    barManager.setFinished(true);
                    editingData.setConfirm(false);

                    plugin.getBarManagerMap().remove(editingData.getBarKeyName());
                    plugin.getUtilities().removePlayerEditing(player);

                    configuration.set(section, null);
                    plugin.saveConfig();

                    player.closeInventory();
                    MessageUtil.sendMessage(player, "You have successfully deleted the bar!");
                }

                if (editingData.isCanceling()) {

                    barManager.removeBar(player);
                    barManager.setFinished(true);
                    editingData.setConfirm(false);

                    plugin.getUtilities().removePlayerEditing(player);

                    player.closeInventory();
                    MessageUtil.sendMessage(player, "You have cancelled the bar creation!");
                }

                if (editingData.isSaving()) {

                    MessageUtil.sendMessage(player, "Saving the bar... Please wait...");

                    List<String> commands = new ArrayList<>();
                    for (String cmds : editingData.getBarValue("Commands").split(", ")) {
                        if (editingData.getBarValue("Commands").isEmpty()) continue;
                        commands.add(cmds.replaceAll("[\\[\\]]", ""));
                    }

                    List<String> displayName = new ArrayList<>();
                    for (String frames : editingData.getBarValue("DisplayName").split(", ")) {
                        if (editingData.getBarValue("DisplayName").isEmpty()) continue;
                        displayName.add(frames.replaceAll("[\\[\\]]", ""));
                    }

                    configuration.set(section + ".DisplayName.Frames", displayName);
                    configuration.set(section + ".DisplayName.Period", Long.valueOf(editingData.getBarValue("Period")));
                    configuration.set(section + ".Time", editingData.getBarValue("Time"));
                    configuration.set(section + ".Color", editingData.getBarValue("Color"));
                    configuration.set(section + ".Style", editingData.getBarValue("Style"));
                    configuration.set(section + ".Commands", commands);
                    configuration.set(section + ".AnnouncerMode.Enabled", editingData.getBarValue("AnnouncerModeEnabled"));
                    configuration.set(section + ".AnnouncerMode.Time", editingData.getBarValue("AnnouncerModeTime"));
                    plugin.saveConfig();

                    String enabled = editingData.getBarValue("AnnouncerModeEnabled");
                    if (enabled != null && enabled.equalsIgnoreCase("true")) {
                        String timeFormat = editingData.getBarValue("AnnouncerModeTime");
                        plugin.getUtilities().formatTime(barName + "-Announcer", timeFormat);
                    } else if (enabled != null && enabled.equalsIgnoreCase("false")) {
                        plugin.getTimer().remove(barName + "-Announcer");
                    }

                    plugin.getBarManagerMap().put(barName, new BossBarManager(plugin));
                    barManager.removeBar(player);
                    barManager.setFinished(true);
                    editingData.setConfirm(false);

                    plugin.getUtilities().removePlayerEditing(player);

                    player.closeInventory();
                    MessageUtil.sendMessage(player, "The bar has been successfully saved!");
                }
            }
            if (slot == 3 && item.hasItemMeta()) {
                if (editingData.isCanceling()) {

                    editingData.setConfirm(false);
                    editingData.setCanceling(false);
                    editingData.setEditing(true);

                    BossbarMenuMaker.createEditMenu(player, plugin);

                }
                if (editingData.isSaving()) {

                    editingData.setConfirm(false);
                    editingData.setSaving(false);
                    editingData.setEditing(true);

                    BossbarMenuMaker.createEditMenu(player, plugin);

                }
                if (editingData.isDeleting()) {

                    editingData.setConfirm(false);
                    editingData.setDeleting(false);
                    editingData.setEditing(true);

                    BossbarMenuMaker.createEditBarsMenu(player, plugin);

                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        String inventoryName = event.getView().getTopInventory().getTitle();
        Inventory inventory = event.getInventory();

        if ((event.getPlayer() instanceof Player)) {
            Player player = (Player) event.getPlayer();
            PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);

            if (player != null && plugin.getUtilities().getPlayerEditingDataMap().containsKey(player) && editingData.isConfirm()
                    && inventoryName.equals("Confirm...") && inventory.getType() == InventoryType.HOPPER) {
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        BossbarMenuMaker.createConfimMenu(player);
                    }
                }, 1L);
            }
        }
    }
}
