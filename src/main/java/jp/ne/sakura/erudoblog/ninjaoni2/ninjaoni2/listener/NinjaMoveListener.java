package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.Ninja;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.NinjaPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class NinjaMoveListener implements Listener {

    private NinjaOni2 plugin;

    public NinjaMoveListener(NinjaOni2 plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //忍者着地
    @EventHandler
    public void onDamageEntity(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getEntity();

        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);

            if (player.isSneaking()) {
                if (NinjaOni2.getInstance().getGameState() != GameState.INGAME) {
                    return;
                }

                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1);
                player.spawnParticle(Particle.CRIT_MAGIC, player.getLocation().subtract(0, 0.3, 0), 20);
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 3));
            }
        }
    }

    //壁ジャンプ
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (NinjaOni2.getInstance().getGameState() != GameState.INGAME) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = e.getPlayer();

            Block wxblock = player.getWorld().getBlockAt(player.getLocation().subtract(1.05, 0, 0));
            Block nxblock = player.getWorld().getBlockAt(player.getLocation().subtract(0, 0, 1.05));
            Block exblock = player.getWorld().getBlockAt(player.getLocation().subtract(-1.05, 0, 0));
            Block sxblock = player.getWorld().getBlockAt(player.getLocation().subtract(0, 0, -1.05));

            double angle = player.getLocation().getYaw();
            double yaw = Util.normalAbsoluteAngleDegrees(angle);
            double velox = player.getVelocity().getX();
            double veloy = player.getVelocity().getY();
            double veloz = player.getVelocity().getZ();

            if (!wxblock.getType().equals(Material.AIR)) {
                if (yaw >= 50 && yaw <= 130) {
                    if (velox <= -0.01 && velox >= -1.0 && veloz <= -0.01 && veloz >= -1.0) {
                        Vector vector = player.getLocation().getDirection().multiply(0.5).setX(velox * -4.25).setY(0.525).setZ(veloz * 5.25);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(1, 0, 0);
                        player.playSound(location, Sound.BLOCK_ANCIENT_DEBRIS_STEP, 1, 1);
                        player.spawnParticle(Particle.CRIT_MAGIC, location, 20);
                    } else if (velox <= -0.01 && velox >= -1.0 && veloz <= 1.0 && veloz >= 0.01) {
                        Vector vector = player.getLocation().getDirection().multiply(0.5).setX(velox * -4.25).setY(0.525).setZ(veloz * 5.25);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(1, 0, 0);
                        player.playSound(location, Sound.BLOCK_ANCIENT_DEBRIS_STEP, 1, 1);
                        player.spawnParticle(Particle.CRIT_MAGIC, location, 20);
                    }
                }
            } else if (!nxblock.getType().equals(Material.AIR)) {
                if (yaw >= 140 && yaw <= 220) {
                    if (velox <= 1.0 && velox >= 0.01 && veloz <= -0.01 && veloz >= -1.0) {
                        Vector vector = player.getLocation().getDirection().multiply(0.5).setX(velox * 5.25).setY(0.525).setZ(veloz * -4.25);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(0, 0, 1);
                        player.playSound(location, Sound.BLOCK_ANCIENT_DEBRIS_STEP, 1, 1);
                        player.spawnParticle(Particle.CRIT_MAGIC, location, 20);
                    } else if (velox <= -0.01 && velox >= -1.0 && veloz <= -0.01 && veloz >= -1.0) {
                        Vector vector = player.getLocation().getDirection().multiply(0.5).setX(velox * 5.25).setY(0.525).setZ(veloz * -4.25);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(0, 0, 1);
                        player.playSound(location, Sound.BLOCK_ANCIENT_DEBRIS_STEP, 1, 1);
                        player.spawnParticle(Particle.CRIT_MAGIC, location, 20);
                    }
                }
            } else if (!exblock.getType().equals(Material.AIR)) {
                if (yaw >= 230 && yaw <= 310) {
                    if (velox <= 1.0 && velox >= 0.01 && veloz <= 1.0 && veloz >= 0.01) {
                        Vector vector = player.getLocation().getDirection().multiply(0.5).setX(velox * -4.25).setY(0.525).setZ(veloz * 5.25);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(-1, 0, 0);
                        player.playSound(location, Sound.BLOCK_ANCIENT_DEBRIS_STEP, 1, 1);
                        player.spawnParticle(Particle.CRIT_MAGIC, location, 20);
                    } else if (velox <= 1.0 && velox >= 0.01 && veloz <= -0.01 && veloz >= -1.0) {
                        Vector vector = player.getLocation().getDirection().multiply(0.5).setX(velox * -4.25).setY(0.525).setZ(veloz * 5.25);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(-1, 0, 0);
                        player.playSound(location, Sound.BLOCK_ANCIENT_DEBRIS_STEP, 1, 1);
                        player.spawnParticle(Particle.CRIT_MAGIC, location, 20);
                    }
                }
            } else if (!sxblock.getType().equals(Material.AIR)) {
                if (yaw >= 320 && yaw <= 360 || yaw >= 0 && yaw <= 40) {
                    if (velox <= -0.01 && velox >= -1.0 && veloz <= 1.0 && veloz >= 0.01) {
                        Vector vector = player.getLocation().getDirection().multiply(0.5).setX(velox * 5.25).setY(0.525).setZ(veloz * -4.25);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(0, 0, -1);
                        player.playSound(location, Sound.BLOCK_ANCIENT_DEBRIS_STEP, 1, 1);
                        player.spawnParticle(Particle.CRIT_MAGIC, location, 20);
                    } else if (velox <= 1.0 && velox >= 0.01 && veloz <= 1.0 && veloz >= 0.01) {
                        Vector vector = player.getLocation().getDirection().multiply(0.5).setX(velox * 5.25).setY(0.525).setZ(veloz * -4.25);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(0, 0, -1);
                        player.playSound(location, Sound.BLOCK_ANCIENT_DEBRIS_STEP, 1, 1);
                        player.spawnParticle(Particle.CRIT_MAGIC, location, 20);
                    }
                }
            }
        }
    }

    //壁よじ登り
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (NinjaOni2.getInstance().getGameState() != GameState.INGAME) {
            return;
        }

        Player player = e.getPlayer();

        if (NinjaOni2.getNinjaPlayer(player) == null) {
            System.out.println("Ninja is null");
            return;
        }

        if (!player.isSneaking()) {
            return;
        }

        Ninja np = NinjaOni2.getNinjaPlayer(player);

        //player is on ground
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            np.setClimbing(false);
        }

        Block wxblock = player.getWorld().getBlockAt(player.getLocation().subtract(1.05, 0, 0));
        Block nxblock = player.getWorld().getBlockAt(player.getLocation().subtract(0, 0, 1.05));
        Block exblock = player.getWorld().getBlockAt(player.getLocation().subtract(-1.05, 0, 0));
        Block sxblock = player.getWorld().getBlockAt(player.getLocation().subtract(0, 0, -1.05));

        double angle = player.getLocation().getYaw();
        double yaw = Util.normalAbsoluteAngleDegrees(angle);
        double velox = player.getVelocity().getX();
        double veloy = player.getVelocity().getY();
        double veloz = player.getVelocity().getZ();

        if (!wxblock.getType().equals(Material.AIR)) {
            if (yaw >= 50 && yaw <= 130) {
                if (veloy >= 0.23) {
                    if (!np.isClimbing()) {
                        Vector vector = player.getLocation().getDirection().setY(0.45).setX(0).setZ(0);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(1, 0, 0);
                        player.playSound(location, Sound.BLOCK_LADDER_STEP, 1, 1);
                        np.setClimbing(true);
                    }
                }


            }
        } else if (!nxblock.getType().equals(Material.AIR)) {
            if (yaw >= 140 && yaw <= 220) {
                if (veloy >= 0.23) {
                    if (!np.isClimbing()) {
                        Vector vector = player.getLocation().getDirection().setY(0.45).setX(0).setZ(0);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(1, 0, 0);
                        player.playSound(location, Sound.BLOCK_LADDER_STEP, 1, 1);
                        np.setClimbing(true);
                    }

                }

            }
        } else if (!exblock.getType().equals(Material.AIR)) {
            if (yaw >= 230 && yaw <= 310) {
                if (veloy >= 0.23) {
                    if (!np.isClimbing()) {
                        Vector vector = player.getLocation().getDirection().setY(0.45).setX(0).setZ(0);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(1, 0, 0);
                        player.playSound(location, Sound.BLOCK_LADDER_STEP, 1, 1);
                        np.setClimbing(true);
                    }

                }
            }
        } else if (!sxblock.getType().equals(Material.AIR)) {
            if (yaw >= 320 && yaw <= 360 || yaw >= 0 && yaw <= 40) {
                if (veloy >= 0.23) {
                    if (!np.isClimbing()) {
                        Vector vector = player.getLocation().getDirection().setY(0.45).setX(0).setZ(0);
                        player.setVelocity(vector);
                        Location location = player.getLocation().subtract(1, 0, 0);
                        player.playSound(location, Sound.BLOCK_LADDER_STEP, 1, 1);
                        np.setClimbing(true);
                    }

                }
            }
        }
    }
}

class Util {
    /**
     * Normalizes an angle to an absolute angle.
     * The normalized angle will be in the range from 0 to 360, where 360
     * itself is not included.
     *
     * @param angle the angle to normalize
     * @return the normalized angle that will be in the range of [0,360[
     */
    public static double normalAbsoluteAngleDegrees(double angle) {
        return (angle %= 360) >= 0 ? angle : (angle + 360);
    }

}