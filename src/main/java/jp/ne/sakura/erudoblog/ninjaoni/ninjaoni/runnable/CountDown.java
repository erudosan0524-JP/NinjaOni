package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.runnable;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CountDown extends BukkitRunnable {
    private int count;
    private NinjaOni plugin;
    private final String symbol_left = "&e>";
    private final String symbol_right = "&e<";

    public CountDown(int count) {
        this.plugin = NinjaOni.getInstance();

        if(count > 0) {
            this.count = count;
        } else {
            this.count = plugin.getMyConfig().getCountdownTime();
        }
    }

    @Override
    public void run() {
        if(plugin.getGameState() == GameState.COUNTDOWN) {
            if(count < 0) {
                this.cancel();
            }

            if (count == 0) {
                plugin.setGameState(GameState.INGAME);
                MessageManager.sendTitleAll("GAME START!", null, 10, 70, 2);
            } else {
                StringBuilder sb = new StringBuilder();
                for(int i=0; i < count; i++) {
                    sb.append(ChatColor.translateAlternateColorCodes('&',symbol_left));
                }

                sb.append(count);

                for(int i=0; i < count; i++) {
                    sb.append(ChatColor.translateAlternateColorCodes('&',symbol_right));
                }

                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle(sb.toString(), null, 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1);
                }
            }
            count--;
        }
    }
}
