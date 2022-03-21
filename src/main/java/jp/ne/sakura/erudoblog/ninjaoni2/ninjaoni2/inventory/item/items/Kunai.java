package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Ninja;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Kunai implements NinjaItem {
    @Override
    public int slot() {
        return 20;
    }

    @Override
    public Material type() {
        return Material.ARROW;
    }

    @Override
    public HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "クナイ";
    }

    @Override
    public void execute(Ninja ninja) {
        Player player = ninja.getPlayer();

        Vector vec = player.getEyeLocation().getDirection().multiply(1.6);
        player.launchProjectile(Arrow.class, vec);
    }

    @Override
    public NinjaItemType ninjaItemType() {
        return NinjaItemType.ONI_ITEM;
    }
}
