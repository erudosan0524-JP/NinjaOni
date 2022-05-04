package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.SubCommand;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class Help extends SubCommand {

    public Help(NinjaOni2 plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        player.sendMessage(
                "===================",
                "HELP",
                "===================");

        WorldBorder border = player.getWorld().getWorldBorder();
        player.sendMessage("Size: " + border.getSize());
    }

    @Override
    public String name() {
        return "help";
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