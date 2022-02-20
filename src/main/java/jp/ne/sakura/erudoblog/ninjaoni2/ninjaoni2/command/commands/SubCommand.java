package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class SubCommand {
    @Getter
    private NinjaOni2 plugin;

    /*
     *  <command> <subcommand> args[0] args[1] ...
     */
    public SubCommand(NinjaOni2 plugin) {
        this.plugin = plugin;
    }


    /**
     * @param player コマンドが実行されたプレイヤー
     * @param args コマンドのエイリアス
     */
    public abstract void onCommand(Player player, String[] args);


    /**
     * @return サブコマンドの名前
     */
    public abstract String name();


    /**
     * @return サブコマンドの詳細
     */
    public abstract String info();


    /**
     * @return args[]の部分
     */
    public abstract String[] aliases();
}
