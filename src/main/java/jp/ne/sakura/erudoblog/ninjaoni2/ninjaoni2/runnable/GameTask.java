package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.Ninja;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {
    private int count;
    private final int MAX_COUNT;
    private NinjaOni2 plugin;
    private BossBar bar;

    public GameTask(int count) {
        this.plugin = NinjaOni2.getInstance();

        if (count > 0) {
            this.count = count;
            this.MAX_COUNT = count;
        } else {
            this.count = plugin.getMyConfig().getGameTime();
            this.MAX_COUNT = plugin.getMyConfig().getGameTime();
        }

        this.bar = Bukkit.getServer().createBossBar("残り時間:" + this.MAX_COUNT, BarColor.BLUE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
    }

    @Override
    public void run() {
        if (plugin.getGameState() == GameState.INGAME) {
            int oniCount = NinjaOni2.countNinja(Teams.ONI);
            int playerCount = NinjaOni2.countNinja(Teams.PLAYER);

            if (count < 0) {
                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    this.bar.removePlayer(player);
                }
                this.bar.setVisible(false);
                this.bar.removeAll();

                this.cancel();
            }

            if (count == 0 || oniCount == 0 || playerCount == 0) {
                String subTitle = "";

                if(oniCount > playerCount) {
                    subTitle = "鬼の勝利！";
                } else if(oniCount < playerCount) {
                    subTitle = "プレイヤーの勝利";
                } else {
                    subTitle = "引き分け！";
                }

                plugin.gameEnd();
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle("GAME OVER!", subTitle, 10, 70, 2);
                }
            } else {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (NinjaOni2.getNinjaPlayer(player) != null) {
                        Ninja ninja = NinjaOni2.getNinjaPlayer(player);

                        bar.addPlayer(player);
                        bar.setVisible(true);
                        bar.setProgress((float) count / MAX_COUNT);
                        bar.setTitle("残り時間: " + count);

                        StringBuilder sb = new StringBuilder();
                        if(ninja.getTeam() == Teams.PLAYER) {
                            sb.append("残り逃走者: ");
                            sb.append(playerCount);
                            sb.append(" | ");
                            sb.append("残りHP: ");
                            sb.append(ninja.getHp());
                        }else if(ninja.getTeam() == Teams.ONI) {
                            sb.append("残り逃走者: ");
                            sb.append(playerCount);
                            sb.append(" | ");
                            sb.append("残り鬼： ");
                            sb.append(oniCount);
                        } else {
                            sb.append("残り逃走者: ");
                            sb.append(playerCount);
                            sb.append(" | ");
                            sb.append("残りHP: ");
                            sb.append(ninja.getHp());
                            sb.append(" | ");
                            sb.append("残り鬼： ");
                            sb.append(oniCount);
                        }


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