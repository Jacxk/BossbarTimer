package com.minestom.BarInterface.BarListener;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossbarTimer;
import com.minestom.Utils.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainMenu implements Listener {

    private BossbarTimer plugin;

    public MainMenu(BossbarTimer plugin) {
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
        if (inventoryName.equals("BossbarTimer") && slotType != InventoryType.SlotType.OUTSIDE
                && inventory.getType() == InventoryType.HOPPER) {
            if (item == null || !item.hasItemMeta()) {
                return;
            }
            event.setCancelled(true);
            if (slot == 1) {
                player.closeInventory();
                plugin.setCreatingBar(player);
                MessageUtil.sendMessage(player, "&aEnter the bar name in the chat. Use &eCancel &ato cancel.");
            }
            if (slot == 3) {
                BossbarInterface.createEditBarsMenu(player, plugin);
            }
        }
    }
}
