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

//ゲーム中の動きに関するタスク
public class MovementTask extends BukkitRunnable {

    private final NinjaOni2 plugin = NinjaOni2.getInstance();

    private int count = 3;

    @Override
    public void run() {
        if(plugin.getGameState() == GameState.NONE) {
            this.cancel();
        }

        if(plugin.getGameState() != GameState.INGAME) {
            return;
        }

        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(!NinjaOni2.containsNinja(player)) return;

            Ninja ninja = NinjaOni2.getNinjaPlayer(player);

            if(ninja.getTeam() == Teams.PLAYER) {
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
                        ninja.setTeam(Teams.SPECTATOR);
                        NinjaOni2.updateNinjaPlayer(ninja);
                        NinjaOni2.getInstance().addPlayerToTeam(ninja.getPlayer(), Teams.SPECTATOR);
                        ninja.getPlayer().setGameMode(GameMode.SPECTATOR);
                    }
                } else {
                    if(ninja.getPlayer().isSneaking()) {
                        //ロック解除処理
                        Ninja lockedNinja = null;

                        for(Ninja nin : NinjaOni2.getNinjas()) {
                            if(nin.getTeam() == Teams.PLAYER) {
                                if(nin.isLocked()) {
                                    if(ninja.getPlayer().getLocation().distance(nin.getPlayer().getLocation()) <= 3) {
                                        lockedNinja = nin;
                                        break;
                                    }
                                }
                            }
                        }

                        if(lockedNinja == null) {
                            return;
                        }

                        if(count < 0) {
                            ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1);
                            ninja.getPlayer().sendTitle(ChatColor.RED + "解除完了",null,10, 70, 2);
                            lockedNinja.getPlayer().sendTitle(ChatColor.RED + "解除完了",null,10, 70, 2);
                            lockedNinja.setLocked(false);
                            count = 3;
                        } else {
                            String frame = ChatColor.RED + "◆";
                            StringBuilder sb = new StringBuilder();
                            for(int i = count; i > 0; i--) {
                                sb.append(frame);
                            }
                            sb.append(ChatColor.RED + "解除中");
                            for(int i = count; i > 0; i--) {
                                sb.append(frame);
                            }

                            ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 10);
                            ninja.getPlayer().sendTitle(sb.toString(),null,10, 70, 2);
                            lockedNinja.getPlayer().sendTitle(sb.toString(),null,10, 70, 2);
                        }

                        count--;
                    }
                }
            }
        }
    }
}
