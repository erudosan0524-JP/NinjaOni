package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.Ninja;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.NinjaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

//ゲーム中の動きに関するタスク
public class MovementTask extends BukkitRunnable {

    private final NinjaOni2 plugin = NinjaOni2.getInstance();

    private int count = 0;

    @Override
    public void run() {
        if(plugin.getGameState() != GameState.INGAME) {
            return;
        }

        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(!NinjaOni2.containsNinja(player)) return;

            Ninja ninja = NinjaOni2.getNinjaPlayer(player);

            if(ninja instanceof NinjaPlayer) {
                NinjaPlayer ninjaPlayer = (NinjaPlayer) ninja;
                if(ninjaPlayer.isLocked()) {
                    ninjaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20 * 2, 1));
                }
            }
        }
    }
}
