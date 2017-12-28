package com.minestom.BarInterface.BarListener;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
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
import java.util.Map;

public class ConfirmMenu implements Listener {

    private BossbarTimer plugin;

    public ConfirmMenu(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        FileConfiguration configuration = plugin.getConfig();
        BossBarManager barManager = plugin.getBarManager();

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();
        Inventory inventory = event.getClickedInventory();

        if (inventoryName.equals("Confirm...") && slotType != InventoryType.SlotType.OUTSIDE
                && inventory.getType() == InventoryType.HOPPER) {
            if (item == null) {
                return;
            }
            event.setCancelled(true);
            if (slot == 1 && item.hasItemMeta()) {
                Map<Player, String> barKeyName = plugin.getBarKeyName();
                Map<String, Map<String, String>> createBar = plugin.getCreateBarValues();
                String barName = barKeyName.get(player);
                Map<String, String> values = createBar.get(plugin.getBarKeyName().get(player));
                String section = "Bars." + barName;

                if (plugin.containsDeleting(player)) {

                    barManager.removeBar(player);
                    barManager.setFinished(true);

                    createBar.remove(barName);
                    barKeyName.remove(player);

                    plugin.removeConfirm(player);
                    plugin.removeDeleting(player);

                    configuration.set(section, null);
                    plugin.saveConfig();

                    player.closeInventory();
                    plugin.getBarManagerMap().remove(barKeyName.get(player));
                    player.sendMessage("You have successfully deleted the bar!");
                }

                if (plugin.containsCanceling(player)) {

                    barManager.removeBar(player);
                    barManager.setFinished(true);

                    createBar.remove(barName);
                    barKeyName.remove(player);

                    plugin.removeConfirm(player);
                    plugin.removeCanceling(player);

                    player.closeInventory();
                    player.sendMessage("You have cancelled the bar creation!");
                }

                if (plugin.containsSaving(player)) {

                    player.sendMessage("Saving the bar... Please wait...");

                    List<String> lore = new ArrayList<>();

                    for (String cmds : values.get("Commands").split(", ")) {
                        if (values.get("Commands").isEmpty()) continue;
                        lore.add(cmds.replaceAll("[\\[\\]]", ""));
                    }

                    configuration.set(section + ".DisplayName", values.get("DisplayName"));
                    configuration.set(section + ".Time", values.get("Time"));
                    configuration.set(section + ".Color", values.get("Color"));
                    configuration.set(section + ".Style", values.get("Style"));
                    configuration.set(section + ".Commands", lore);
                    configuration.set(section + ".AnnouncerMode.Enabled", values.get("AnnouncerModeEnabled"));
                    configuration.set(section + ".AnnouncerMode.Time", values.get("AnnouncerModeTime"));
                    plugin.saveConfig();

                    String enabled = values.get("AnnouncerModeEnabled");
                    if (enabled != null && enabled.equalsIgnoreCase("true")) {
                        String timeFormat = values.get("AnnouncerModeTime");
                        plugin.getUtilities().formatTime(barName + "-Announcer", timeFormat);
                    } else if (enabled != null && enabled.equalsIgnoreCase("false")) {
                        plugin.getTimer().remove(barName + "-Announcer");
                    }

                    plugin.getBarManagerMap().put(barName, new BossBarManager(plugin));
                    barManager.removeBar(player);
                    barManager.setFinished(true);
                    plugin.removeEditing(player);

                    createBar.remove(barName);
                    barKeyName.remove(player);

                    plugin.removeConfirm(player);
                    plugin.removeSaving(player);

                    player.closeInventory();
                    player.sendMessage("The bar has been successfully saved!");
                }
            }
            if (slot == 3 && item.hasItemMeta()) {
                if (plugin.containsCanceling(player)) {

                    plugin.removeConfirm(player);
                    plugin.removeCanceling(player);
                    plugin.setEditing(player);
                    BossbarInterface.createEditMenu(player, plugin);

                }
                if (plugin.containsSaving(player)) {

                    plugin.removeConfirm(player);
                    plugin.removeSaving(player);
                    plugin.setEditing(player);
                    BossbarInterface.createEditMenu(player, plugin);

                }
                if (plugin.containsDeleting(player)) {

                    plugin.removeConfirm(player);
                    plugin.removeDeleting(player);
                    BossbarInterface.createEditBarsMenu(player, plugin);

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
            if (player != null && plugin.containsConfirm(player) && inventoryName.equals("Confirm...") && inventory.getType() == InventoryType.HOPPER) {
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        BossbarInterface.createConfimMenu(player);
                    }
                }, 1L);
            }
        }
    }
}
