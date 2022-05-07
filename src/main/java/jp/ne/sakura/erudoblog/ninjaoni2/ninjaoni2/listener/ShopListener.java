package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopListener implements Listener {

    public ShopListener(NinjaOni2 plugin) {
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onInteractVillager(PlayerInteractEntityEvent e) {
        if(!(e.getRightClicked() instanceof Villager)) {
           return;
        }

        Villager villager = (Villager) e.getRightClicked();
        Player player = e.getPlayer();
        ItemManager im = new ItemManager();
        if(villager.getCustomName() != null) {
            String customName = ChatColor.stripColor(villager.getCustomName());
            if(customName.equals("鬼専用ショップ")) {
                Inventory inv = Bukkit.createInventory(null, 9, customName);

                List<ItemStack> items = new ArrayList<>();

                for(NinjaItem ni : im.getNinjaItems()) {
                    if(ni.ninjaItemType() == NinjaItem.NinjaItemType.ONI_ITEM) {
                        ItemStack is = im.getItem(ni);
                        items.add(is);
                    }
                }

                int slot = 0;
                for(ItemStack item : items) {
                    inv.setItem(slot, item);
                    slot+= 2;
                    if(slot > 10) {
                        break;
                    }
                }

                player.openInventory(inv);

            } else if(customName.equals("プレイヤー専用ショップ")) {
                Inventory inv = Bukkit.createInventory(null, 9, customName);

                List<ItemStack> items = new ArrayList<>();

                for(NinjaItem ni : im.getNinjaItems()) {
                    if(ni.ninjaItemType() == NinjaItem.NinjaItemType.PLAYER_ITEM) {
                        ItemStack is = im.getItem(ni);
                        items.add(is);
                    }
                }

                int slot = 0;
                for(ItemStack item : items) {
                    inv.setItem(slot, item);
                    slot+= 2;
                    if(slot > 10) {
                        break;
                    }
                }

                player.openInventory(inv);
            }
        }
    }
}
