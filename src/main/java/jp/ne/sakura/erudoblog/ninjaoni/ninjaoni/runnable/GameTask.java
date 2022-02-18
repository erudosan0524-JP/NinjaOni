package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.runnable;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.NinjaPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {
    private int count;
    private NinjaOni plugin;

    public GameTask(int count) {
        this.plugin = NinjaOni.getInstance();

        if(count > 0) {
            this.count = count;
        } else {
            this.count = plugin.getMyConfig().getGameTime();
        }
    }

    @Override
    public void run() {
        if(plugin.getGameState() == GameState.INGAME) {
            if (count < 0) {
                this.cancel();
            }

            if (count == 0) {
                plugin.setGameState(GameState.NONE);
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle("GAME OVER!", null, 10, 70, 2);
                }
            } else {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(NinjaOni.getNinjaPlayer(player) != null) {
                        NinjaPlayer ninja = NinjaOni.getNinjaPlayer(player);
                        StringBuilder sb = new StringBuilder();
                        sb.append("残り時間:");
                        sb.append(count);
                        sb.append(" | ");
                        sb.append("残りHP: ");
                        sb.append(ninja.getHp());

                        TextComponent component = new TextComponent();
                        component.setText(sb.toString());

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                    }
                }

                count--;
            }
        }
    }
}
