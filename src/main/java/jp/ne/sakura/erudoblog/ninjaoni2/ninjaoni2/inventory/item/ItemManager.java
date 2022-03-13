package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

public final class ItemManager {

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

    public static ItemStack createSkull(String url) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);

        SkullMeta meta = (SkullMeta)  skull.getItemMeta();
        WrappedGameProfile profile = new WrappedGameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new WrappedSignedProperty("textures", url, null));
        Field profileField;

        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


        skull.setItemMeta(meta);
        return skull;
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
        ItemStack item = new NinjaItemBuilder(Material.DIAMOND_HELMET)
                .name("鬼ヘルメット")
                .amount(1)
                .enchant(Enchantment.BINDING_CURSE,1)
                .create();

        return item;
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

    public static ItemStack getMoney() {
       return createPlayerHead("MrSnowDK", "お金");
    }
}
