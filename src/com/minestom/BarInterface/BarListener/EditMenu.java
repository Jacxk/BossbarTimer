package com.minestom.BarInterface.BarListener;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EditMenu implements Listener {

    private BossbarTimer plugin;

    public EditMenu(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        BossBarManager barManager = plugin.getBarManager();
        FileConfiguration configuration = plugin.getConfig();

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();
        if (inventoryName.equals("Edit Mode") && slotType != InventoryType.SlotType.OUTSIDE
                && inventory.getType() == InventoryType.HOPPER) {
            if (item == null) {
                return;
            }
            event.setCancelled(true);
            if (slot == 0 && item.hasItemMeta()) {
                BossbarInterface.createColorMenu(player);
                plugin.removeEditing(player);
                plugin.setColors(player);
            }
            if (slot == 1 && item.hasItemMeta()) {
                BossbarInterface.createStyleMenu(player);
                plugin.removeEditing(player);
                plugin.setStyles(player);
            }
            if (slot == 2 && item.hasItemMeta()) {
                player.closeInventory();
                plugin.removeEditing(player);
                plugin.setEditingName(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aEnter the display name of the bar in the chat. Use &eCancel &ato cancel."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7TIP: You can use '{time}' to show the time left!"));
            }
            if (slot == 3 && item.hasItemMeta()) {
                player.closeInventory();
                plugin.removeEditing(player);
                plugin.setEditTimer(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aEnter the time of the bar in the chat. Use &eCancel &ato cancel."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7TIP: You can use 's' for seconds, 'm' for minutes and 'h' for ours."));
            }
            if (slot == 4 && item.hasItemMeta()) {
                Map<Player, String> barKeyName = plugin.getBarKeyName();
                Map<String, Map<String, String>> createBar = plugin.getCreateBarValues();
                String barName = barKeyName.get(player);
                Map<String, String> values = createBar.get(plugin.getBarKeyName().get(player));
                String section = "Bars." + barName;

                player.sendMessage("Saving the bar... Please wait...");

                configuration.set(section + ".DisplayName", values.get("DisplayName"));
                configuration.set(section + ".Time", values.get("Time"));
                configuration.set(section + ".Color", values.get("Color"));
                configuration.set(section + ".Style", values.get("Style"));
                plugin.saveConfig();

                player.sendMessage("The bar has been successfully saved!");
                plugin.getBarManagerMap().put(barName, new BossBarManager(plugin));
                barManager.removeBar(player);

                createBar.remove(barName);
                barKeyName.remove(player);

                player.closeInventory();
            }
        }
    }

}
