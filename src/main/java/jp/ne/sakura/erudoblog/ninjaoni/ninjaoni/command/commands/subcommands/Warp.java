package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.commands.SubCommand;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.NinjaPlayer;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.PlayerStatus;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Warp extends SubCommand {
    public Warp(NinjaOni plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if(args[0].equals("oni")) {
            for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                System.out.println(p.getName());
                if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == getPlugin().getMyConfig().getWarpBlockTypeOni()) {
                    p.getInventory().setHelmet(ItemManager.getOniHelmet());
                    p.getInventory().setChestplate(ItemManager.getOniChestplate());
                    p.getInventory().setLeggings(ItemManager.getOniLeggings());
                    p.getInventory().setBoots(ItemManager.getOniBoots());
                    NinjaOni.updateNinjaPlayer(p, PlayerStatus.ONI);
                    p.sendMessage("あなたは鬼になりました");
                }
            }
        } else if(args[0].equals("spectator")) {
            for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                System.out.println(p.getName());
                if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == getPlugin().getMyConfig().getWarpBlockTypeSpec()) {
                    NinjaOni.updateNinjaPlayer(p, PlayerStatus.SPECTATOR);
                    p.sendMessage("あなたは観戦者になりました");
                }
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
