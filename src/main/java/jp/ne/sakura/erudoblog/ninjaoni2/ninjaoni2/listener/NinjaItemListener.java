package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.NinjaInventory;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.MessageManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.Ninja;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.Teams;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

//アイテム処理に関するリスナー
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

        if (plugin.getGameState() == GameState.NONE) {
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
                        if (ninja.getMoney() > 0) {
                            ninjaInventory.purchaseItem(ninja, itemManager.getItem(ninjaItem));

                            inv.addItem(itemManager.getItem(ninjaItem));
                        }
                    }
                } else if (ninjaItem.ninjaItemType() == NinjaItem.NinjaItemType.PLAYER_ITEM && ninja.getTeam() == Teams.PLAYER) {
                    if (item.getType() == ninjaItem.type() && e.getSlot() == ninjaItem.slot()) {
                        e.setCancelled(true);

                        if (ninja.getMoney() > 0) {
                            ninjaInventory.purchaseItem(ninja, itemManager.getItem(ninjaItem));
                            inv.addItem(itemManager.getItem(ninjaItem));
                        }
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

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (plugin.getGameState() != GameState.INGAME) {
            return;
        }

        //クナイの処理
        if (e.getEntity() instanceof Arrow) {
            if (e.getHitEntity() == null) {
                e.getEntity().remove();
                e.setCancelled(true);
            } else {
                Arrow arrow = (Arrow) e.getEntity();
                Player player = (Player) e.getHitEntity();

                if(arrow.getShooter() == null) {
                    return;
                }

                if(!(arrow.getShooter() instanceof Player)) {
                    return;
                }

                if(!NinjaOni2.containsNinja(player)) {
                    return;
                }

                Player shooter = (Player) arrow.getShooter();
                Ninja ninja =NinjaOni2.getNinjaPlayer(player);

                if(ninja.getTeam() != Teams.PLAYER) {
                    return;
                }

                if(!ninja.isLocked()) {
                    e.setCancelled(true);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.3F, 1);
                    MessageManager.sendAll(ChatColor.RED + ninja.getPlayer().getName() + ChatColor.WHITE + "は" + ChatColor.DARK_AQUA + shooter.getName() + ChatColor.WHITE + "に確保された！");
                    ninja.setLocked(true);
                }
            }
        }

        //粘着玉の処理
        if(e.getEntity() instanceof Snowball) {
            if (e.getHitEntity() == null) {
                e.getEntity().remove();
                e.setCancelled(true);
            } else {
                Snowball snowball = (Snowball) e.getEntity();
                Player player = (Player) e.getHitEntity();

                if(snowball.getShooter() == null) {
                    return;
                }

                if(!(snowball.getShooter() instanceof Player)) {
                    return;
                }

                if(!NinjaOni2.containsNinja(player)) {
                    return;
                }

                Player shooter = (Player) snowball.getShooter();
                Ninja ninja =NinjaOni2.getNinjaPlayer(player);

                if(ninja.getTeam() != Teams.ONI) {
                    return;
                }

                shooter.playSound(shooter.getLocation(),Sound.BLOCK_SLIME_BLOCK_STEP, 0.8f,1f);
                player.playSound(player.getLocation(),Sound.BLOCK_SLIME_BLOCK_STEP, 0.8f,1f);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 4),true);
            }
        }
    }

}
