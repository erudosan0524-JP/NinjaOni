package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Ninja;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTask extends BukkitRunnable {

    private final int PACKAGE_TIME = 30;
    private final int PACKAGE_RANGE = 10;

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
            int lockedCount = NinjaOni2.countLockedPlayer();

            if(count != MAX_COUNT && count % PACKAGE_TIME == 0) {
                //マネパケ（マネーパッケージ）の処理
                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    List<Location> locs = new ArrayList<>();
                    Location loc = player.getLocation();

                    for(int x = 1; x <= PACKAGE_RANGE; x++) {
                        for(int z = 1; z <= PACKAGE_RANGE; z++) {
                            locs.add(loc.clone().add(x, 0, z));
                        }
                    }

                    for(int x = -1; x >= -PACKAGE_RANGE; x--) {
                        for(int z = -1; z >= -PACKAGE_RANGE; z--) {
                            locs.add(loc.clone().add(x,0,z));
                        }
                    }

                    Collections.shuffle(locs);

                    Entity entity = locs.get(0).getWorld().spawnEntity(locs.get(0), EntityType.ARMOR_STAND);
                    ArmorStand stand = (ArmorStand) entity;
                    stand.setBasePlate(false);
                    stand.setInvisible(true);
                    stand.setSmall(true);
                    stand.setCollidable(false);
                    stand.setCanPickupItems(false);
                    stand.setInvulnerable(true);
                    stand.setCustomName("money");
                    stand.setHelmet(ItemManager.getMoney());

                }

            }

            if (count == 0 || oniCount == 0 || playerCount == 0 || lockedCount == playerCount) {
                String subTitle = "";

                if(oniCount > playerCount || lockedCount == playerCount) {
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

                for(Player player : Bukkit.getOnlinePlayers()) {
                    bar.removePlayer(player);
                }
                bar.removeAll();

                this.cancel();
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