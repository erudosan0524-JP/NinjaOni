package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Ninja;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        PlayerInventory inv = player.getInventory();
        ItemStack item = inv.getItemInMainHand();
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (NinjaOni2.containsNinja(player)) {

                Ninja ninja = NinjaOni2.getNinjaPlayer(player);

                if (ninja.getTeam() == Teams.ONI) {
                    //クナイ
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


                    //影追玉の処理
                    if (item.getType() == ItemManager.getKageoi().getType()) {
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

                        List<Player> glowPlayers = new ArrayList<>();

                        //光らせるプレイヤーの設定
                        for (Ninja nin : NinjaOni2.getNinjas()) {
                            if (nin.getTeam() == Teams.PLAYER) {
                                if(!glowPlayers.contains(nin.getPlayer())) {
                                    glowPlayers.add(nin.getPlayer());
                                }
                            }
                        }

                        new BukkitRunnable() {

                            int count = 10;

                            @Override
                            public void run() {
                                if(count < 0) {
                                    this.cancel();
                                } else {
                                    for (Player p : glowPlayers) {

                                        PacketContainer glowPacket = plugin.getProtocol().createPacket(PacketType.Play.Server.ENTITY_METADATA);
                                        glowPacket.getIntegers().write(0, p.getEntityId()); //光らせるプレイヤーのID
                                        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
                                        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
                                        WrappedDataWatcher.Serializer serializer2 = WrappedDataWatcher.Registry.get(Integer.class);
                                        watcher.setEntity(p); //光らせるプレイヤーを指定
                                        watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page

                                        glowPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created

                                        try {
                                            plugin.getProtocol().sendServerPacket(player, glowPacket);
                                        } catch (InvocationTargetException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }

                                count--;
                            }

                        }.runTaskTimer(plugin, 0L, 20L);
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
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 2, 1));

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
                                        location2.add(x, 2, z);
                                        location3.add(x, 5, z);

                                        slime.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location1, 5, 0, 0, 0);
                                        slime.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location2, 5, 0, 0, 0);
                                        slime.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location3, 5, 0, 0, 0);

                                        location1.subtract(x, 0, z);
                                        location2.subtract(x, 2, z);
                                        location3.subtract(x, 5, z);

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

                        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 1);
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

            if(ninja.getTeam() == Teams.PLAYER) {
                if (e.getCurrentItem().getType() == ItemManager.getKemuri().getType() && e.getSlot() == 21) {
                    e.setCancelled(true);
                    inv.addItem(ItemManager.getKemuri());
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.3F, 1);
                } else if (e.getCurrentItem().getType() == ItemManager.getKakure().getType() && e.getSlot() == 20) {
                    e.setCancelled(true);
                    inv.addItem(ItemManager.getKakure());
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.3F, 1);
                }
            }

            if(ninja.getTeam() == Teams.ONI) {
                if (e.getCurrentItem().getType() == ItemManager.getKunai().getType() && e.getSlot() == 20) {
                    e.setCancelled(true);
                    inv.addItem(ItemManager.getKunai());
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.3F, 1);
                } else if (e.getCurrentItem().getType() == ItemManager.getKageoi().getType() && e.getSlot() == 21) {
                    e.setCancelled(true);
                    inv.addItem(ItemManager.getKageoi());
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
