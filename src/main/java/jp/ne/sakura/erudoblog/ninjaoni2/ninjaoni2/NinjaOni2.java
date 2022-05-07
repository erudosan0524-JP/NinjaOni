package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.CommandManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener.*;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable.CountDownTask;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable.GameTask;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable.MovementTask;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Config;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.ItemManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

import static jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.Game.Teams.*;

public final class NinjaOni2 extends JavaPlugin {


    @Override
    public void onEnable() {
        NinjaOniAPI.setInstance(this);
        NinjaOniAPI.getInstance().getGame().setup();

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
