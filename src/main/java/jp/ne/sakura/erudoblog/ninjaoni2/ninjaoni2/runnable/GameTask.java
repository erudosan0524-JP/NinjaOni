package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.NinjaPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {
    private int count;
    private NinjaOni2 plugin;

    public GameTask(int count) {
        this.plugin = NinjaOni2.getInstance();

        if (count > 0) {
            this.count = count;
        } else {
            this.count = plugin.getMyConfig().getGameTime();
        }
    }

    @Override
    public void run() {
        if (plugin.getGameState() == GameState.INGAME) {
            if (count < 0) {
                this.cancel();
            }

            if (count == 0) {
                plugin.gameEnd();
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle("GAME OVER!", null, 10, 70, 2);
                }
            } else {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (NinjaOni2.getNinjaPlayer(player) != null) {
                        NinjaPlayer ninja = NinjaOni2.getNinjaPlayer(player);
                        StringBuilder sb = new StringBuilder();
                        sb.append("残り時間:");
                        sb.append(count);
                        sb.append(" | ");
                        sb.append("残り逃走者: ");

                        TextComponent component = new TextComponent();
                        component.setText(sb.toString());

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                    }
                }
            }

            count--;
        }
    }
}