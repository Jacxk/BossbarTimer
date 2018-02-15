package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarTimer;
import com.minestom.DataHandler.BossBarHandler;
import com.minestom.DataHandler.PlayerEditingData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class StylesMenu implements Listener {

    private BossBarTimer plugin;

    public StylesMenu(BossBarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();
        if (inventoryName.equals("Choose a style") && slotType != InventoryType.SlotType.OUTSIDE) {
            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();


            if (slot != 8 && item.hasItemMeta()) {
                PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
                BossBarHandler bossBarHandler = editingData.getBossBarHandler();
                String style = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ", "_");

                bossBarHandler.setBarStyle(style);
                bossBarHandler.setStyle(style);
            }
            if (slot == 8 && item.hasItemMeta()) {
                BossbarMenuMaker.createEditMenu(player, plugin);
            }
        }
    }
}
