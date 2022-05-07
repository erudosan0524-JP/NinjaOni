package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.*;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.SubCommand;
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
            getPlugin().getLogger().info(p.getName());
            if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == NinjaOniAPI.getInstance().getMyConfig().getWarpBlockTypeOni()) {
                NinjaManager.getInstance().updateNinjaPlayer(new Ninja(p, Game.Teams.ONI));
                p.sendMessage("あなたは鬼になりました");
            } else if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == NinjaOniAPI.getInstance().getMyConfig().getWarpBlockTypeSpec()) {
                NinjaManager.getInstance().updateNinjaPlayer(new Ninja(p, Game.Teams.SPECTATOR));
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
