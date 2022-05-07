package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.*;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


public class MovementTask extends BukkitRunnable {

    private final NinjaOni2 plugin = NinjaOniAPI.INSTANCE.getPlugin();

    @Override
    public void run() {
        if(NinjaOniAPI.getInstance().getGame().getGameState() == Game.GameState.NONE) {
            this.cancel();
        }

        if(NinjaOniAPI.getInstance().getGame().getGameState() != Game.GameState.INGAME) {
            return;
        }

        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(!NinjaManager.getInstance().containsNinja(player)) return;

            Ninja ninja = NinjaManager.getInstance().getNinjaPlayer(player);

            if(ninja.getTeam() == Game.Teams.PLAYER) {
                if(ninja.isLocked()) { //捕まっている時の処理
                    if(ninja.getHp() > 0) {
                        ninja.decHP();
                        ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.ENTITY_PLAYER_HURT, 0.3F, 1);
                        ninja.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20 * 2, 4));
                        ninja.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 2, -100), true);
                        ninja.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 2, 3));
                    } else {
                        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                            p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5F,1);
                        }

                        MessageManager.sendAll(ChatColor.RED + ninja.getPlayer().getName() + "が脱落した");
                        ninja.setTeam(Game.Teams.SPECTATOR);
                        NinjaManager.getInstance().updateNinjaPlayer(ninja);
                        NinjaOniAPI.getInstance().getGame().addEntry(ninja.getPlayer(), Game.Teams.SPECTATOR);
                        ninja.getPlayer().setGameMode(GameMode.SPECTATOR);
                    }
                }
            }
        }
    }
}
