package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.MessageManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Ninja;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

//プレイヤーと鬼に関するリスナー
public class NinjaOniListener implements Listener {

    private NinjaOni2 plugin;

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