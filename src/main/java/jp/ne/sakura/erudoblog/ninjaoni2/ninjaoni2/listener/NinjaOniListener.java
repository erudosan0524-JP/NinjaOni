package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.Ninja;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.NinjaOni;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.NinjaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

//プレイヤーと鬼に関するリスナー
public class NinjaOniListener implements Listener {

    private NinjaOni2 plugin;
    private final double RELEASE_RANGE = 3.0; //シフト押したときに開放できる範囲

    public NinjaOniListener(NinjaOni2 plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (plugin.getGameState() != GameState.INGAME) {
            return;
        }

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player player = (Player) e.getEntity();

            if (!NinjaOni2.containsNinja(damager) && !NinjaOni2.containsNinja(player)) {
                return;
            }

            Ninja damagerNinja = NinjaOni2.getNinjaPlayer(damager);
            Ninja playerNinja = NinjaOni2.getNinjaPlayer(player);

            if (!(damagerNinja instanceof NinjaOni)) {
                return;
            }

            if (!(playerNinja instanceof NinjaPlayer)) {
                return;
            }

            NinjaOni damagerNin = (NinjaOni) damagerNinja;
            NinjaPlayer playerNin = (NinjaPlayer) playerNinja;

            if(!playerNin.isLocked()) {
                //捕まった時の処理
                e.setCancelled(true);
                playerNin.setLocked(true);

            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();


    }
}