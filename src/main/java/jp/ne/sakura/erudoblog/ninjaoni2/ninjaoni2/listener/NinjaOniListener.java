package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.MessageManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Ninja;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

//プレイヤーと鬼に関するリスナー
public class NinjaOniListener implements Listener {

    private NinjaOni2 plugin;

    private final Material[] GUIBlocks = {
            Material.CRAFTING_TABLE,
            Material.FURNACE,
            Material.FURNACE_MINECART,
            Material.ANVIL,
            Material.BREWING_STAND,
            Material.HOPPER,
            Material.HOPPER_MINECART,
            Material.BEACON,
            Material.ENCHANTING_TABLE,
            Material.CHEST,
            Material.CHEST_MINECART,
            Material.FLETCHING_TABLE,
            Material.ENDER_CHEST,
            Material.DISPENSER,
            Material.DROPPER,
            Material.SMOKER,
            Material.BLAST_FURNACE,
            Material.LOOM,
            Material.BARREL,
            Material.CARTOGRAPHY_TABLE,
            Material.GRINDSTONE,
            Material.SMITHING_TABLE,
            Material.STONECUTTER
    };

    public NinjaOniListener(NinjaOni2 plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (plugin.getGameState() != GameState.INGAME) {
            return;
        }

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) { //鬼が逃走者を殴った時
            Player damager = (Player) e.getDamager();
            Player player = (Player) e.getEntity();

            if (!NinjaOni2.containsNinja(damager) || !NinjaOni2.containsNinja(player)) {
                return;
            }

            Ninja damagerNinja = NinjaOni2.getNinjaPlayer(damager);
            Ninja playerNinja = NinjaOni2.getNinjaPlayer(player);

            if (damagerNinja.getTeam() != Teams.ONI) {
                return;
            }

            if (playerNinja.getTeam() != Teams.PLAYER) {
                return;
            }

            if(!playerNinja.isLocked()) {
                //捕まった時の処理
                e.setCancelled(true);
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.3F, 1);
                MessageManager.sendAll(ChatColor.RED + playerNinja.getPlayer().getName() + ChatColor.WHITE + "は" + ChatColor.DARK_AQUA + damagerNinja.getPlayer().getName() + ChatColor.WHITE + "に確保された！");
                playerNinja.setLocked(true);

            }
        }
    }

    //GUIが存在するブロックを開いた時キャンセル
    @EventHandler
    public void onClickGUIBlock(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            for(Material m : GUIBlocks) {
                if(block.getType() == m) {
                    e.setCancelled(true);
                }
            }
        }
    }

    //壁掛けが壊れないように
    @EventHandler
    public void onHangingBreak(HangingBreakEvent e) {
        e.setCancelled(true);
    }

}