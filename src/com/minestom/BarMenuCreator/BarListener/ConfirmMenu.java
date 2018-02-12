package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.Utils.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.DataHandler.BossBarHandler;
import com.minestom.Utils.MessageUtil;
import com.minestom.DataHandler.PlayerEditingData;
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
            BossBarHandler bossBarHandler = editingData.getBossBarHandler();
            BossBarManager barManager = bossBarHandler.getBossBarManager();
            FileConfiguration configuration = plugin.getConfig();

            int slot = event.getRawSlot();

            if (slot == 1 && item.hasItemMeta()) {
                String barName = editingData.getBarKeyName();
                String section = "Bars." + barName;

                if (editingData.isDeleting()) {

                    barManager.removeBar(player);
                    editingData.setConfirm(false);

                    plugin.getUtilities().removePlayerEditing(player);

                    configuration.set(section, null);
                    plugin.saveConfig();

                    player.closeInventory();
                    MessageUtil.sendMessage(player, "You have successfully deleted the bar!");
                }

                if (editingData.isCanceling()) {

                    barManager.removeBar(player);
                    editingData.setConfirm(false);

                    plugin.getUtilities().removePlayerEditing(player);
                    bossBarHandler.stopAnimatedTitle();

                    player.closeInventory();
                    MessageUtil.sendMessage(player, "You have cancelled the bar creation!");
                }

                if (editingData.isSaving()) {

                    MessageUtil.sendMessage(player, "Saving the bar... Please wait...");

                    configuration.set(section + ".DisplayName.Frames", bossBarHandler.getNameFrames());
                    configuration.set(section + ".DisplayName.Period", bossBarHandler.getNamePeriod());
                    configuration.set(section + ".Time", bossBarHandler.getCountdownTime());
                    configuration.set(section + ".Color", bossBarHandler.getColor());
                    configuration.set(section + ".Style", bossBarHandler.getStyle());
                    configuration.set(section + ".Commands", bossBarHandler.getCommands());
                    configuration.set(section + ".AnnouncerMode.Enabled", bossBarHandler.isAnnouncerEnabled());
                    configuration.set(section + ".AnnouncerMode.Time", bossBarHandler.getAnnouncerTime());
                    bossBarHandler.setInitialTime(plugin.getUtilities().timeToSeconds(bossBarHandler.getCountdownTime()));
                    plugin.saveConfig();

                    plugin.loadBars();
                    barManager.removeBar(player);
                    editingData.setConfirm(false);

                    plugin.getUtilities().removePlayerEditing(player);
                    bossBarHandler.stopAnimatedTitle();

                    if (bossBarHandler.isAnnouncerEnabled()) {
                        String timeFormat = bossBarHandler.getAnnouncerTime();
                        plugin.getAnnouncerTimer().put(barName, plugin.getUtilities().timeToSeconds(timeFormat));
                    }

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
                Bukkit.getScheduler().runTaskLater(plugin, () -> BossbarMenuMaker.createConfimMenu(player), 1L);
            }
        }
    }
}
