package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items.Kageoi;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items.Kakure;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items.Kemuri;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items.Kunai;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ItemManager {

    @Getter
    public final List<NinjaItem> ninjaItems = new ArrayList<>();
    @Getter
    public final List<ItemStack> items = new ArrayList<>();

    public ItemManager() {
        ninjaItems.add(new Kageoi());
        ninjaItems.add(new Kakure());
        ninjaItems.add(new Kemuri());
        ninjaItems.add(new Kunai());
    }

    public ItemStack getItem(NinjaItem ninjaItem) {
        ItemStack item = new ItemStack(ninjaItem.type());

        if(ninjaItem.enchants() != null) {
            for(Enchantment enchant : ninjaItem.enchants().keySet()) {
                int level = ninjaItem.enchants().get(enchant);
                item.addEnchantment(enchant,level);
            }
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ninjaItem.name());
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

    @SuppressWarnings("deprecation")
    public static ItemStack createPlayerHead(String player_name, String item_name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD,1);

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName(item_name);
        meta.setOwner(player_name);

        head.setItemMeta(meta);
        return head;
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

    public static ItemStack getMoney() {
       return createPlayerHead("MrSnowDK", "お金");
    }
}
