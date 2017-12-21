package com.minestom.BarInterface.BarListener;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossbarTimer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EditMenu implements Listener {

    private BossbarTimer plugin;

    public EditMenu(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {

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

                if (event.getClick() == ClickType.SHIFT_LEFT) {

                    plugin.removeEditing(player);
                    plugin.setDeleting(player);
                    plugin.setConfirm(player);
                    BossbarInterface.createConfimMenu(player);

                }
                if (event.getClick() == ClickType.LEFT) {

                    plugin.removeEditing(player);
                    plugin.setSaving(player);
                    plugin.setConfirm(player);
                    BossbarInterface.createConfimMenu(player);

                }
            }
        }
    }

}
