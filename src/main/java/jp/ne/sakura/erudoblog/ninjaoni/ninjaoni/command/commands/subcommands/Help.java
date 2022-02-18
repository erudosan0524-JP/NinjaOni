package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.commands.SubCommand;
import org.bukkit.entity.Player;

public class Help extends SubCommand {

    public Help(NinjaOni plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        player.sendMessage(
                "===================",
                "HELP",
                "===================");
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