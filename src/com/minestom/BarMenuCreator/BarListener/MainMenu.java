package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossbarTimer;
import com.minestom.Utils.MessageUtil;
import com.minestom.Utils.PlayerEditingData;
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
        Inventory inventory = event.getClickedInventory();
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();

        if (inventoryName.equals("BossbarTimer") && slotType != InventoryType.SlotType.OUTSIDE
                && inventory.getType() == InventoryType.HOPPER) {
            ItemStack item = event.getCurrentItem();
            if (item == null || !item.hasItemMeta()) return;
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            int slot = event.getRawSlot();
            if (slot == 1) {
                if (!player.hasPermission("bossbartimer.create")) {
                    MessageUtil.sendMessage(player, plugin.getConfig().getString("Messages.NoPermission"));
                    return;
                }
                plugin.getUtilities().addPlayerEditing(player);
                PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);

                player.closeInventory();
                editingData.setCreateBar(true);
                MessageUtil.sendMessage(player, "&aEnter the bar name in the chat. Use &eCancel &ato cancel.");

                if (plugin.debug) MessageUtil.sendDebugMessage("Clicked Slot: " + slot + "\nClicked Option: Create new bar");
            }
            if (slot == 3) {
                if (!player.hasPermission("bossbartimer.edit")) {
                    MessageUtil.sendMessage(player, plugin.getConfig().getString("Messages.NoPermission"));
                    return;
                }
                BossbarMenuMaker.createEditBarsMenu(player, plugin);

                if (plugin.debug) MessageUtil.sendDebugMessage("Clicked Slot: " + slot + "\nClicked Option: Edit current bar");
            }
        }
    }
}