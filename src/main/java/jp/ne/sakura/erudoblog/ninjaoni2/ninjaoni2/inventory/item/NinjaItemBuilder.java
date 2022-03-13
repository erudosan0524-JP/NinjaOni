package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/* Itemstackクラスを作成するクラス */
public class NinjaItemBuilder implements ItemBuilder {

    private ItemStack item;

    public NinjaItemBuilder(Material m) {
        this.item = new ItemStack(m);
    }

    @Override
    public ItemBuilder type(Material m) {
        item.setType(m);
        return this;
    }

    @Override
    public ItemBuilder name(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return this;
    }

    @Override
    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    @Override
    public ItemBuilder enchant(Enchantment enchantment, int level) {
        item.addEnchantment(enchantment,level);
        return this;
    }

    @Override
    public ItemStack create() {
        return this.item;
    }
}
