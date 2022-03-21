package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Ninja;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;

public interface NinjaItem {
    //設置スロット
    int slot();

    //Material
    Material type();

    //付与エンチャント
    HashMap<Enchantment,Integer> enchants();

    //アイテム名
    String name();

    //アイテム実行処理
    void execute(Ninja ninja);

    NinjaItemType ninjaItemType();

    enum NinjaItemType {
        ONI_ITEM,PLAYER_ITEM
    }
}
