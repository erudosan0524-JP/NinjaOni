package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.commands.SubCommand;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.MessageManager;
import org.bukkit.entity.Player;

public class Reload extends SubCommand {

    public Reload(NinjaOni plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        getPlugin().getMyConfig().reload();
        MessageManager.sendMessageAll("リロードが完了しました");
    }

    @Override
    public String name() {
        return "reload";
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