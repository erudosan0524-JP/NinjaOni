package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public interface ItemBuilder {
    ItemBuilder type(Material m);
    ItemBuilder name(String name);
    ItemBuilder amount(int amount);
    ItemBuilder enchant(Enchantment enchantment, int level);

    ItemStack create();

}
