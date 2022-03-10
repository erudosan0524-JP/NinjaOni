package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Ninja;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

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

            if (!ninja.isLocked()) {
                if (ninja.getPlayer().isSneaking()) {
                    if (ninja.getTeam() == Teams.PLAYER) {
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
                                for (int i = count / 20; i > 0; i--) {
                                    sb.append(frame);
                                }
                                sb.append(ChatColor.RED + "解除中");
                                for (int i = count / 20; i > 0; i--) {
                                    sb.append(frame);
                                }

                                ninja.getPlayer().sendTitle(sb.toString(), null, 10, 70, 2);
                                lockedNinja.getPlayer().sendTitle(sb.toString(), null, 10, 70, 2);
                            } else {
                                ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 10);
                            }
                            count--;
                        }
                    }

                    //お金取得処理ここから
                    for (Entity entity : player.getNearbyEntities(2, 0, 2)) {
                        if (entity instanceof ArmorStand) {
                            ArmorStand stand = (ArmorStand) entity;

                            if(stand.getCustomName() == null) {
                                return;
                            }

                            if (stand.getCustomName().equals("money")) {
                                if (count < 0) {
                                    Inventory inv = ninja.getPlayer().getInventory();
                                    if (inv.getItem(18) == null || inv.getItem(18).clone().getAmount() <= 0) {
                                        inv.setItem(18, ItemManager.getMoney());
                                    } else {
                                        int amount = inv.getItem(18).clone().getAmount();
                                        ItemStack item = ItemManager.getMoney().clone();
                                        item.setAmount(amount + 1);
                                        inv.setItem(18, item);
                                    }

                                    stand.remove();
                                    ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1);
                                    ninja.getPlayer().sendTitle(ChatColor.YELLOW + "取得完了", null, 10, 70, 2);
                                    ninja.incMoney();
                                    count = MAX_COUNT;
                                } else if (count % 20 == 0) {
                                    String frame = ChatColor.YELLOW + "◆";
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = count / 20; i > 0; i--) {
                                        sb.append(frame);
                                    }
                                    sb.append(ChatColor.RED + "取得中");
                                    for (int i = count / 20; i > 0; i--) {
                                        sb.append(frame);
                                    }

                                    ninja.getPlayer().sendTitle(sb.toString(), null, 10, 70, 2);
                                } else {
                                    ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 10);
                                }
                                count--;
                            }
                        }
                    }
                }
            }
        }
    }
}

