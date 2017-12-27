package com.minestom.BarInterface.BarListener;

import com.minestom.BarInterface.BossbarInterface;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ColorsMenu implements Listener {

    private BossbarTimer plugin;

    public ColorsMenu(BossbarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        BossBarManager barManager = plugin.getBarManager();

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();

        Map<String, String> values = plugin.getCreateBarValues().get(plugin.getBarKeyName().get(player));

        if (inventoryName.equals("Choose a color") && slotType != InventoryType.SlotType.OUTSIDE) {
            if (item == null) {
                return;
            }
            event.setCancelled(true);
            if (slot != 8 && item.hasItemMeta()) {
                String color = ChatColor.stripColor(item.getItemMeta().getDisplayName());
                barManager.setBarColor(color);
                values.put("Color", color);
                plugin.getCreateBarValues().put(plugin.getBarKeyName().get(player), values);
            }
            if (slot == 8 && item.hasItemMeta()) {
                BossbarInterface.createEditMenu(player, plugin);
            }
        }
    }
}
