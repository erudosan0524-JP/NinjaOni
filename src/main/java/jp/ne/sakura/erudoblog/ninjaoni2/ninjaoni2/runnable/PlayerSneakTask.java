package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.MessageManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.Ninja;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSneakTask extends BukkitRunnable {
    private final NinjaOni2 plugin = NinjaOni2.getInstance();

    private final int MAX_COUNT = 3 * 20;
    private int count = MAX_COUNT;

    @Override
    public void run() {
        if (plugin.getGameState() == GameState.NONE) {
            this.cancel();
        }

        if (plugin.getGameState() != GameState.INGAME) {
            return;
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!NinjaOni2.containsNinja(player)) return;

            Ninja ninja = NinjaOni2.getNinjaPlayer(player);
            if(ninja.getTeam() == Teams.PLAYER) {
                if(!ninja.isLocked()) {
                    if (ninja.getPlayer().isSneaking()) {
                        //ロック解除処理ここから
                        Ninja lockedNinja = null;

                        for (Ninja nin : NinjaOni2.getNinjas()) {
                            if (nin.getTeam() == Teams.PLAYER) {
                                if (nin.isLocked()) {
                                    if (ninja.getPlayer().getLocation().distance(nin.getPlayer().getLocation()) <= 3) {
                                        lockedNinja = nin;
                                        break;
                                    }
                                }
                            }
                        }

                        if (lockedNinja != null) {
                            if (count < 0) {
                                ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1);
                                ninja.getPlayer().sendTitle(ChatColor.RED + "解除完了", null, 10, 70, 2);
                                lockedNinja.getPlayer().sendTitle(ChatColor.RED + "解除完了", null, 10, 70, 2);
                                lockedNinja.setLocked(false);
                                count = MAX_COUNT;
                            } else if (count % 20 == 0) {
                                String frame = ChatColor.RED + "◆";
                                StringBuilder sb = new StringBuilder();
                                for (int i = count; i > 0; i--) {
                                    sb.append(frame);
                                }
                                sb.append(ChatColor.RED + "解除中");
                                for (int i = count; i > 0; i--) {
                                    sb.append(frame);
                                }

                                ninja.getPlayer().sendTitle(sb.toString(), null, 10, 70, 2);
                                lockedNinja.getPlayer().sendTitle(sb.toString(), null, 10, 70, 2);
                            } else {
                                ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 10);
                            }
                            count--;
                        }
                        //ロック解除処理ここまで

                    }
                }
            }
        }

    }
}
