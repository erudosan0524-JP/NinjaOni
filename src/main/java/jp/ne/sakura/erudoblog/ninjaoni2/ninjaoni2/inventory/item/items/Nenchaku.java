package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Ninja;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Snow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Nenchaku implements NinjaItem {
    @Override
    public int slot() {
        return 22;
    }

    @Override
    public Material type() {
        return Material.SNOWBALL;
    }

    @Override
    public HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "粘着玉";
    }

    @Override
    public void execute(Ninja ninja) {
        Player player = ninja.getPlayer();
        Location loc = player.getEyeLocation().clone();
        Vector direction = loc.getDirection();

        Vector vec = direction.normalize().multiply(1.5);
        player.launchProjectile(Snowball.class, vec);
    }


    @Override
    public NinjaItemType ninjaItemType() {
        return NinjaItemType.PLAYER_ITEM;
    }
}
