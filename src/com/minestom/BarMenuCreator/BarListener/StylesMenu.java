package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarManager;
import com.minestom.BossbarTimer;
import com.minestom.Utils.BarsData;
import com.minestom.Utils.PlayerEditingData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class StylesMenu implements Listener {

    private BossbarTimer plugin;

    public StylesMenu(BossbarTimer plugin) {
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
                BarsData barsData = editingData.getBarsData();
                BossBarManager barManager = plugin.getBarManagerMap().get(barsData);
                String style = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ", "_");

                barManager.setBarStyle(style);
                barsData.setStyle(style);
            }
            if (slot == 8 && item.hasItemMeta()) {
                BossbarMenuMaker.createEditMenu(player, plugin);
            }
        }
    }
}
