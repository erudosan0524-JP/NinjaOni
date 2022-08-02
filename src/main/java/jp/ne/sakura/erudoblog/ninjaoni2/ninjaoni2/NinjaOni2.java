package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2;

import dev.jorel.commandapi.CommandAPI;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NinjaOni2 extends JavaPlugin {


    @Override
    public void onEnable() {
        NinjaOniAPI.setInstance(this);
        NinjaOniAPI.getInstance().getGame().setup();

        //コマンドの設定
        CommandAPI.registerCommand(NinjaCommand.class);

        //リスナーの設定
        new JoinQuitListener(this);
        new NinjaMoveListener(this);
        new NinjaOniListener(this);
        new NinjaItemListener(this);
        new ShopListener(this);

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
