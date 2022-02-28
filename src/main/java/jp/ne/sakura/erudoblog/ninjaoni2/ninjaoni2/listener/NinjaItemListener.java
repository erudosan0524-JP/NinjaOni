package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ItemManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class NinjaItemListener implements Listener {

    private NinjaOni2 plugin;

    private final int invTime = 5;

    public NinjaItemListener(NinjaOni2 plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        Inventory inv = player.getInventory();
        ItemStack item = ((PlayerInventory) inv).getItemInMainHand();
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            //クナイ
            if (item.getType() == Material.ARROW) {
                if (inv.contains(ItemManager.getKunai())) {
                    int index = inv.first(ItemManager.getKunai());
                    if (index > -1) {
                        int amount = inv.getItem(index).getAmount();

                        if (amount > 1) {
                            inv.getItem(index).setAmount(inv.getItem(index).getAmount() - 1);
                        } else {
                            inv.remove(inv.getItem(index));
                        }
                    }
                }

                Vector vec = player.getEyeLocation().getDirection().multiply(1.4);
                player.launchProjectile(Arrow.class, vec);
            }

            //煙玉
            if (item.getType() == Material.FIREWORK_STAR) {
//                if(inv.contains(ItemManager.getKunai()) ){
//                    inv.remove(ItemManager.getKunai());
//                }


                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 1);

                new BukkitRunnable() {

                    int count = 0;

                    @Override
                    public void run() {
                        if(count >= 30) {
                            this.cancel();
                        } else {
                            Location location1 = player.getEyeLocation();
                            Location location2 = player.getEyeLocation();
                            Location location3 = player.getEyeLocation();
                            int particles = 30;
                            float radius = 0.7f;
                            for (int i = 0; i < particles; i++) {
                                double angle, x, z;

                                angle = 2 * Math.PI * i / particles;
                                x = Math.cos(angle) * radius;
                                z = Math.sin(angle) * radius;

                                location1.add(x, 0, z);
                                location2.add(x, -0.66, z);
                                location3.add(x, -0.33, z);

                                player.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location1, 5, 0, 0, 0);
                                player.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location2, 5, 0, 0, 0);
                                player.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location3, 5, 0, 0, 0);

                                location1.subtract(x, 0, z);
                                location2.subtract(x, -0.10, z);
                                location3.subtract(x, -0.20, z);

                            }
                            count++;
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }

            //隠れ玉
            if (item.getType() == Material.SLIME_BALL) {
                if (inv.contains(ItemManager.getKakure())) {
                    inv.remove(ItemManager.getKakure());
                }

                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 10, 1));
            }
        }

    }
}
