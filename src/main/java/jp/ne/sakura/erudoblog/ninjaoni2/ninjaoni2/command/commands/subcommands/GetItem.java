package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.SubCommand;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items.Kageoi;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items.Kakure;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items.Kemuri;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items.Kunai;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetItem extends SubCommand {

    public GetItem(NinjaOni2 plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if(args.length == 1) {
            switch (args[0]) {
                case "kunai":
                    player.sendMessage("クナイをインベントリに追加しました");
                    player.getInventory().addItem(getPlugin().getItemManager().getItem(new Kunai()));
                    break;
                case "kakure":
                    player.sendMessage("隠れ玉をインベントリに追加しました");
                    player.getInventory().addItem(getPlugin().getItemManager().getItem(new Kakure()));
                    break;
                case "kemuri":
                    player.sendMessage("煙玉をインベントリに追加しました");
                    player.getInventory().addItem(getPlugin().getItemManager().getItem(new Kemuri()));
                    break;
                case "kageoi":
                    player.sendMessage("影追玉をインベントリに追加しました");
                    player.getInventory().addItem(getPlugin().getItemManager().getItem(new Kageoi()));
                    break;
            }
        } else {
            player.getInventory().addItem(new ItemStack(Material.AIR));
        }
    }

    @Override
    public String name() {
        return "getitem";
    }

    @Override
    public String info() {
        return "";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
