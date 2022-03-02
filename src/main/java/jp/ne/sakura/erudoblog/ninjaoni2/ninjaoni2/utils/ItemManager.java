package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class ItemManager {

    public static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }


    public static ItemStack createItem(Material material, String name, HashMap<Enchantment,Integer> enchants) {
        ItemStack item = new ItemStack(material);
        for(Enchantment enchant : enchants.keySet()) {
            int level = enchants.get(enchant);
            item.addEnchantment(enchant,level);
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItem(Material material,String name, Enchantment enchant, int level ) {
        ItemStack item = new ItemStack(material);
        item.addEnchantment(enchant,level);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getOniHelmet() {
        return createItem(Material.DIAMOND_HELMET, "鬼ヘルメット", Enchantment.BINDING_CURSE, 1);
    }

    public static ItemStack getOniChestplate() {
        return createItem(Material.DIAMOND_CHESTPLATE, "鬼チェストプレート", Enchantment.BINDING_CURSE, 1);
    }

    public static ItemStack getOniLeggings() {
        return createItem(Material.DIAMOND_LEGGINGS, "鬼レギンス", Enchantment.BINDING_CURSE, 1);
    }

    public static ItemStack getOniBoots() {
        return createItem(Material.DIAMOND_BOOTS, "鬼ブーツ", Enchantment.BINDING_CURSE, 1);
    }

    public static ItemStack getKunai() {
        return createItem(Material.ARROW, "クナイ");
    }

    public static ItemStack getKemuri() {
        return createItem(Material.FIREWORK_STAR, "煙玉");
    }

    public static ItemStack getKakure() {
        return createItem(Material.SLIME_BALL, "隠れ玉");
    }

    public static ItemStack getKageoi() {
        return createItem(Material.HEART_OF_THE_SEA, "影追玉");
    }
}
