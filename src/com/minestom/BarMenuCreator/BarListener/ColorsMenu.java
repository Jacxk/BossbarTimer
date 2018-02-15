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

public class ColorsMenu implements Listener {

    private BossBarTimer plugin;

    public ColorsMenu(BossBarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {

        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();
        if (inventoryName.equals("Choose a color") && slotType != InventoryType.SlotType.OUTSIDE) {

            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
            BossBarHandler bossBarHandler = editingData.getBossBarHandler();

            int slot = event.getRawSlot();

            if (slot != 8 && item.hasItemMeta()) {
                String color = ChatColor.stripColor(item.getItemMeta().getDisplayName());
                bossBarHandler.setBarColor(color);
                bossBarHandler.setColor(color);
            }
            if (slot == 8 && item.hasItemMeta()) {
                BossbarMenuMaker.createEditMenu(player, plugin);
            }
        }
    }
}
