package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.NinjaPlayer;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.PlayerStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

//「鬼がプレイヤーを捕まえる」という機能に関するリスナー
public class NinjaOniListener implements Listener {

    private NinjaOni plugin;

    private static final List<Player> blockPlayers = new ArrayList<>(); //捕まったプレイヤーが入るリスト
    private final double RELEASE_RANGE = 3.0; //シフト押したときに開放できる範囲

    public NinjaOniListener(NinjaOni plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player)) {
            return;
        }

        if(!(e.getEntity() instanceof Player)) {
            return;
        }

        if(plugin.getGameState() != GameState.INGAME) {
            return;
        }

        Player damager = (Player) e.getDamager();
        Player player = (Player) e.getEntity();

        if(NinjaOni.contains(damager) == null) {
            return;
        }

        if(NinjaOni.contains(player) == null) {
            return;
        }

        NinjaPlayer damagerNinja = NinjaOni.getNinjaPlayer(damager);
        NinjaPlayer playerNinja = NinjaOni.getNinjaPlayer(player);

        if(damagerNinja.getStatus() == PlayerStatus.ONI && playerNinja.getStatus() == PlayerStatus.PLAYER) { //ダメージャーが鬼かつプレイヤーが逃げる側
            e.setCancelled(true);

            if(!blockPlayers.contains(player)) {
                blockPlayers.add(player);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if(NinjaOni.contains(player) == null) {
            return;
        }

        NinjaPlayer playerNinja = NinjaOni.getNinjaPlayer(player);

        if(blockPlayers.contains(player)) {
            e.setCancelled(true);
        }

        if(playerNinja.getStatus() == PlayerStatus.PLAYER) {
            if(player.isSneaking()) {
                Location loc = player.getLocation();

                for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if(loc.distance(p.getLocation()) <= RELEASE_RANGE) {

                        players.add(p);
                    }
                }


            }
        }
    }
}
