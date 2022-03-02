package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.Ninja;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

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
            if (NinjaOni2.containsNinja(player)) {

                Ninja ninja = NinjaOni2.getNinjaPlayer(player);

                //クナイ
                if (ninja.getTeam() == Teams.ONI) {
                    if (item.getType() == ItemManager.getKunai().getType()) {
                        if (inv.contains(ItemManager.getKunai().getType())) {
                            HashMap<Integer, ? extends ItemStack> indexs = inv.all(ItemManager.getKunai().getType());
                            for (int key : indexs.keySet()) {
                                if (key >= 0 && key <= 8) {
                                    int amount = inv.getItem(key).getAmount();
                                    if (amount > 1) {
                                        inv.getItem(key).setAmount(inv.getItem(key).getAmount() - 1);
                                    } else {
                                        inv.remove(inv.getItem(key));
                                    }
                                }

                            }

                        }


                        Vector vec = player.getEyeLocation().getDirection().multiply(1.6);
                        player.launchProjectile(Arrow.class, vec);
                    }

                    if(item.getType() == ItemManager.getKageoi().getType()) {
                        if (inv.contains(ItemManager.getKageoi().getType())) {
                            HashMap<Integer, ? extends ItemStack> indexs = inv.all(ItemManager.getKageoi().getType());
                            for (int key : indexs.keySet()) {
                                if (key >= 0 && key <= 8) {
                                    int amount = inv.getItem(key).getAmount();
                                    if (amount > 1) {
                                        inv.getItem(key).setAmount(inv.getItem(key).getAmount() - 1);
                                    } else {
                                        inv.remove(inv.getItem(key));
                                    }
                                }

                            }

                        }

                        //影追玉の処理
                        PacketContainer packet = plugin.getProtocol().createPacket(PacketType.Play.Server.ENTITY_EFFECT);
                        packet.getBytes().write(0, (byte) 24);
                        packet.getIntegers().write(1,1);

                        try {
                            plugin.getProtocol().sendServerPacket(ninja.getPlayer(), packet);
                        } catch (InvocationTargetException invocationTargetException) {
                            invocationTargetException.printStackTrace();
                        }

                    }
                }

                if (ninja.getTeam() == Teams.PLAYER) {
                    //煙玉
                    if (item.getType() == ItemManager.getKemuri().getType()) {
                        if (inv.contains(ItemManager.getKemuri().getType())) {
                            HashMap<Integer, ? extends ItemStack> indexs = inv.all(ItemManager.getKemuri().getType());
                            for (int key : indexs.keySet()) {
                                if (key >= 0 && key <= 8) {
                                    int amount = inv.getItem(key).getAmount();
                                    if (amount > 1) {
                                        inv.getItem(key).setAmount(inv.getItem(key).getAmount() - 1);
                                    } else {
                                        inv.remove(inv.getItem(key));
                                    }
                                }

                            }

                        }

                        Slime slime = (Slime) player.getWorld().spawnEntity(player.getLocation(), EntityType.SLIME);
                        slime.setSize(1);
                        slime.setCanPickupItems(false);
                        slime.setInvisible(true);

                        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 1);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 2, 0));

                        new BukkitRunnable() {

                            int count = 0;

                            @Override
                            public void run() {
                                if (count >= 40) {
                                    this.cancel();
                                } else {
                                    Location location1 = slime.getEyeLocation();
                                    Location location2 = slime.getEyeLocation();
                                    Location location3 = slime.getEyeLocation();
                                    int particles = 30;
                                    float radius = 0.7f;
                                    for (int i = 0; i < particles; i++) {
                                        double angle, x, z;

                                        angle = 2 * Math.PI * i / particles;
                                        x = Math.cos(angle) * radius;
                                        z = Math.sin(angle) * radius;

                                        location1.add(x, 0, z);
                                        location2.add(x, -0.66, z);
                                        location3.add(x, -1.33, z);

                                        slime.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location1, 5, 0, 0, 0);
                                        slime.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location2, 5, 0, 0, 0);
                                        slime.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location3, 5, 0, 0, 0);

                                        location1.subtract(x, 0, z);
                                        location2.subtract(x, -0.66, z);
                                        location3.subtract(x, -1.33, z);

                                    }
                                    count++;
                                }
                            }
                        }.runTaskTimer(plugin, 0, 1);
                    }

                    //隠れ玉
                    if (item.getType() == ItemManager.getKakure().getType()) {
                        if (inv.contains(ItemManager.getKakure().getType())) {
                            HashMap<Integer, ? extends ItemStack> indexs = inv.all(ItemManager.getKakure().getType());
                            for (int key : indexs.keySet()) {
                                if (key >= 0 && key <= 8) {
                                    int amount = inv.getItem(key).getAmount();
                                    if (amount > 1) {
                                        inv.getItem(key).setAmount(inv.getItem(key).getAmount() - 1);
                                    } else {
                                        inv.remove(inv.getItem(key));
                                    }
                                }

                            }

                        }

                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 10, 1));
                    }
                }
            }
        }

    }

    @EventHandler
    public void onClickInv(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) {
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        if (!(e.getClickedInventory() instanceof PlayerInventory)) {
            return;
        }

        if (plugin.getGameState() != GameState.INGAME) {
            return;
        }


        Player player = (Player) e.getWhoClicked();
        PlayerInventory inv = (PlayerInventory) e.getClickedInventory();

        if (NinjaOni2.containsNinja(player)) {
            Ninja ninja = NinjaOni2.getNinjaPlayer(player);

            if (ninja.getTeam() == Teams.PLAYER || ninja.getTeam() == Teams.ONI) {
                if (e.getCurrentItem().getType() == ItemManager.getKunai().getType() && e.getSlot() == 20) {
                    e.setCancelled(true);
                    inv.addItem(ItemManager.getKunai());
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.3F, 1);
                } else if (e.getCurrentItem().getType() == ItemManager.getKemuri().getType() && e.getSlot() == 21) {
                    e.setCancelled(true);
                    inv.addItem(ItemManager.getKemuri());
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.3F, 1);
                } else if (e.getCurrentItem().getType() == ItemManager.getKakure().getType() && e.getSlot() == 20) {
                    e.setCancelled(true);
                    inv.addItem(ItemManager.getKakure());
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.3F, 1);
                }
            }
        }
    }

    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent e) {
        if (plugin.getGameState() != GameState.INGAME) {
            return;
        }

        if (e.getItem().getItemStack().getType() == Material.ARROW && e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

}
