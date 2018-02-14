package com.minestom.BarMenuCreator.BarListener;

import com.minestom.BarMenuCreator.BossbarMenuMaker;
import com.minestom.BossBarTimer;
import com.minestom.DataHandler.BossBarHandler;
import com.minestom.DataHandler.PlayerEditingData;
import com.minestom.Utils.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AvancedMenu implements Listener {

    private BossBarTimer plugin;

    public AvancedMenu(BossBarTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        String inventoryName = event.getView().getTopInventory().getTitle();
        InventoryType.SlotType slotType = event.getSlotType();
        if (inventoryName.equals("Advanced Settings") && slotType != InventoryType.SlotType.OUTSIDE
                && inventory.getType() == InventoryType.HOPPER) {
            ItemStack item = event.getCurrentItem();
            if (item == null || !item.hasItemMeta()) {
                return;
            }
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            PlayerEditingData editingData = plugin.getUtilities().getEditingData(player);
            BossBarHandler bossBarHandler = editingData.getBossBarHandler();

            int slot = event.getRawSlot();

            if (slot == 0) {
                player.closeInventory();
                editingData.setEditing(false);
                editingData.setEditTimer(true);
                MessageUtil.sendMessage(player, "&aEnter the time of the bar in the chat. Use &eCancel &ato cancel.");
                MessageUtil.sendMessage(player, "&7TIP: You can use 's' for seconds, 'm' for minutes and 'h' for ours.");
            }
            if (slot == 1) {
                List<String> lore = new ArrayList<>(bossBarHandler.getCommands());

                if (event.getClick() == ClickType.LEFT) {
                    editingData.setEditing(false);
                    editingData.setAddingCmd(true);
                    player.closeInventory();
                    MessageUtil.sendMessage(player, "&aEnter the command you wish to add in the chat. Use &eCancel &ato cancel.");
                    MessageUtil.sendMessage(player, "&7TIP: Do not use slash '/'. If the commands are 'none' delete them.");
                }
                if (event.getClick() == ClickType.RIGHT) {
                    if (!lore.isEmpty()) {
                        lore.remove(lore.size() - 1);
                        bossBarHandler.setCommands(lore);
                    }
                    if (lore.size() == 0) {
                        lore.clear();
                        bossBarHandler.setCommands(lore);
                    }
                    BossbarMenuMaker.createAvancedMenu(player, plugin);
                }
                if (event.getClick() == ClickType.SHIFT_LEFT) {
                    lore.clear();
                    bossBarHandler.setCommands(lore);
                    BossbarMenuMaker.createAvancedMenu(player, plugin);
                }

            }
            if (slot == 2) {
                if (event.getClick() == ClickType.LEFT) {

                    if (bossBarHandler.isAnnouncerEnabled()) bossBarHandler.setAnnouncerEnabled(false);
                    else bossBarHandler.setAnnouncerEnabled(true);

                    BossbarMenuMaker.createAvancedMenu(player, plugin);
                }
                if (event.getClick() == ClickType.RIGHT) {
                    editingData.setAnnouncerTime(true);
                    editingData.setEditing(false);
                    player.closeInventory();
                }
            }
            if (slot == 4) {
                BossbarMenuMaker.createEditMenu(player, plugin);
            }
        }
    }
}
