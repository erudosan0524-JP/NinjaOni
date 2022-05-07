package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.Ninja;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Choyaku implements NinjaItem {
    @Override
    public int slot() {
        return 23;
    }

    @Override
    public Material type() {
        return Material.PRISMARINE_SHARD;
    }

    @Override
    public HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "跳躍";
    }

    @Override
    public void execute(Ninja ninja) {
        Player player = ninja.getPlayer();
        Location loc = player.getLocation().clone();
        Vector direction = loc.getDirection();

        Vector vec = direction.normalize().multiply(1.6).setY(1.5);
        player.setVelocity(vec);
    }

    @Override
    public NinjaItemType ninjaItemType() {
        return NinjaItemType.ONI_ITEM;
    }
}
