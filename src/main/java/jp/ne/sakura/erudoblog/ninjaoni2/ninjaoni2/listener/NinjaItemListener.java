package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.NinjaInventory;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Ninja;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NinjaItemListener implements Listener {

    private NinjaOni2 plugin;


    public NinjaItemListener(NinjaOni2 plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteractArmorStand(PlayerArmorStandManipulateEvent e) {
        if (plugin.getGameState() == GameState.INGAME) {
            e.setCancelled(true);

        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        PlayerInventory inv = player.getInventory();
        ItemStack item = inv.getItemInMainHand();
        NinjaInventory ninjaInventory = new NinjaInventory(inv);
        ItemManager itemManager = plugin.getItemManager();

        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (NinjaOni2.containsNinja(player)) {

                Ninja ninja = NinjaOni2.getNinjaPlayer(player);

                for (NinjaItem ninjaItem : itemManager.getNinjaItems()) {
                    if (ninjaItem.ninjaItemType() == NinjaItem.NinjaItemType.ONI_ITEM && ninja.getTeam() == Teams.ONI) {
                        if (item.getType() == ninjaItem.type()) {
                            ninjaInventory.decrementHolderItem(itemManager.getItem(ninjaItem));

                            ninjaItem.execute(ninja);
                        }
                    } else if (ninjaItem.ninjaItemType() == NinjaItem.NinjaItemType.PLAYER_ITEM && ninja.getTeam() == Teams.PLAYER) {
                        if (item.getType() == ninjaItem.type()) {
                            ninjaInventory.decrementHolderItem(itemManager.getItem(ninjaItem));

                            ninjaItem.execute(ninja);
                        }
                    }
                }

            }
        }

    }

    @EventHandler
    public void onClickInv(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) {
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        if (!(e.getClickedInventory() instanceof PlayerInventory)) {
            return;
        }

        if (plugin.getGameState() != GameState.INGAME) {
            return;
        }


        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        PlayerInventory inv = (PlayerInventory) e.getClickedInventory();
        NinjaInventory ninjaInventory = new NinjaInventory(inv);
        ItemManager itemManager = plugin.getItemManager();


        if (e.getCurrentItem().getType() == ItemManager.getMoney().getType() && e.getSlot() == 18) {
            e.setCancelled(true);
            return;
        }

        if (NinjaOni2.containsNinja(player)) {
            Ninja ninja = NinjaOni2.getNinjaPlayer(player);

            for (NinjaItem ninjaItem : itemManager.getNinjaItems()) {
                if (ninjaItem.ninjaItemType() == NinjaItem.NinjaItemType.ONI_ITEM && ninja.getTeam() == Teams.ONI) {
                    if (item.getType() == ninjaItem.type() && e.getSlot() == ninjaItem.slot()) {
                        e.setCancelled(true);
                        ninjaInventory.purchaseItem(ninja,itemManager.getItem(ninjaItem));

                        inv.addItem(itemManager.getItem(ninjaItem));
                    }
                } else if (ninjaItem.ninjaItemType() == NinjaItem.NinjaItemType.PLAYER_ITEM && ninja.getTeam() == Teams.PLAYER) {
                    if (item.getType() == ninjaItem.type() && e.getSlot() == ninjaItem.slot()) {
                        e.setCancelled(true);

                        ninjaInventory.purchaseItem(ninja,itemManager.getItem(ninjaItem));
                        inv.addItem(itemManager.getItem(ninjaItem));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemPickUp(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL) {
            ItemStack item = e.getItemDrop().getItemStack().clone();
            e.getItemDrop().remove();
            player.getInventory().addItem(item);
        }
    }

}
