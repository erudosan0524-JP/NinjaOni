package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.Ninja;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Shukuchi implements NinjaItem {

    @Override
    public int slot() {
        return 22;
    }

    @Override
    public Material type() {
        return Material.GOLD_NUGGET;
    }

    @Override
    public HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "縮地";
    }

    @Override
    public void execute(Ninja ninja) {
        Player player = ninja.getPlayer();

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20,255),true);

        new BukkitRunnable() {

            int count = 3;

            @Override
            public void run() {
                if(count < 0) {
                    this.cancel();
                }else if(count == 0) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 5), true);
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST,1,1f);
                } else {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                }
                count--;

            }
        }.runTaskTimer(NinjaOni2.getInstance(), 0L, 20L);
    }

    @Override
    public NinjaItemType ninjaItemType() {
        return NinjaItemType.ONI_ITEM;
    }
}
