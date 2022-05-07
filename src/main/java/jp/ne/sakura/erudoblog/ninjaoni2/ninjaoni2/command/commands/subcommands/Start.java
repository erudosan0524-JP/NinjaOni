package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOniAPI;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.SubCommand;
import org.bukkit.entity.Player;

public class Start extends SubCommand {

    public Start(NinjaOni2 plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        int countdownTime = 5;
        int gameTime = 300;

        if(args.length == 2) {
            try {
                countdownTime = Integer.parseInt(args[0]);
                gameTime = Integer.parseInt(args[1]);
            }catch(NumberFormatException e) {
                player.sendMessage("引数には数値を入力してください");
            }
        }

        NinjaOniAPI.getInstance().getGame().gameStart(countdownTime,gameTime);
    }

    @Override
    public String name() {
        return "start";
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
