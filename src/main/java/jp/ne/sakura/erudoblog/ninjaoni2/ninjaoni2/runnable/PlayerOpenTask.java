package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.Game;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.MessageManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.Ninja;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

//プレイヤー開放タスク
public class PlayerOpenTask extends BukkitRunnable {

    private final int MAX_COUNT;

    private int count = 0;
    private Ninja ninja; //シフトを押している忍者
    private Ninja locked; //解除対象

    public PlayerOpenTask(Ninja ninja, Ninja locked, int count) {
        this.MAX_COUNT = count * 20;
        this.count = count * 20;
        this.ninja = ninja;
        this.locked = locked;
    }

    @Override
    public void run() {
        if(count < 0 || !ninja.getPlayer().isSneaking() || ninja.getPlayer().getLocation().distance(locked.getPlayer().getLocation()) >= 4) {
            this.cancel();
            return;
        }

        if (ninja.getTeam() == Game.Teams.PLAYER) {

            if (locked != null) {
                if (count == 0) {
                    ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1);
                    ninja.getPlayer().sendTitle(ChatColor.RED + "解除完了", null, 10, 70, 2);
                    locked.getPlayer().sendTitle(ChatColor.RED + "解除完了", null, 10, 70, 2);
                    MessageManager.sendAll(ChatColor.RED + locked.getPlayer().getName() + ChatColor.WHITE + "が開放された！");
                    locked.setLocked(false);
                } else if (count % 20 == 0) {
                    String frame = ChatColor.RED + "◆";
                    StringBuilder sb = new StringBuilder();
                    for (int i = count / 20; i > 0; i--) {
                        sb.append(frame);
                    }
                    sb.append(ChatColor.RED + "解除中");
                    for (int i = count / 20; i > 0; i--) {
                        sb.append(frame);
                    }

                    ninja.getPlayer().sendTitle(sb.toString(), null, 10, 70, 2);
                    locked.getPlayer().sendTitle(sb.toString(), null, 10, 70, 2);
                } else {
                    ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 10);
                }
                count--;
            }
        }
    }
}
