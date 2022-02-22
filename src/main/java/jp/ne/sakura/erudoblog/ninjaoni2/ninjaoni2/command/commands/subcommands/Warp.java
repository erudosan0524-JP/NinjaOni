package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.SubCommand;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.Ninja;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.NinjaOni;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.NinjaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Warp extends SubCommand {
    public Warp(NinjaOni2 plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            System.out.println(p.getName());
            if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == getPlugin().getMyConfig().getWarpBlockTypeOni()) {
                p.getInventory().setHelmet(ItemManager.getOniHelmet());
                p.getInventory().setChestplate(ItemManager.getOniChestplate());
                p.getInventory().setLeggings(ItemManager.getOniLeggings());
                p.getInventory().setBoots(ItemManager.getOniBoots());
                NinjaOni2.updateNinjaPlayer(new NinjaOni(p, false, false));
                p.sendMessage("あなたは鬼になりました");
            } else if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == getPlugin().getMyConfig().getWarpBlockTypeSpec()) {
                NinjaOni2.updateNinjaPlayer(new Ninja(p, Teams.SPECTATOR));
                p.sendMessage("あなたは観戦者になりました");
            }
        }
    }

    @Override
    public String name() {
        return "wp";
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
