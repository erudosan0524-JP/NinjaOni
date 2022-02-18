package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.commands.SubCommand;
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
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            System.out.println(p.getName());
            if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == getPlugin().getMyConfig().getWarpBlockType()) {
                NinjaPlayer ninja = new NinjaPlayer(p, PlayerStatus.ONI);
                NinjaOni.updateNinjaPlayer(ninja);
                p.sendMessage("ゲームに参戦しました");
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
