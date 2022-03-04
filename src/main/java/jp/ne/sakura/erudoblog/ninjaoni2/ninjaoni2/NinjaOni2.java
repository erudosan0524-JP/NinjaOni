package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.CommandManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener.*;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable.CountDownTask;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable.GameTask;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable.MovementTask;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Config;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ItemManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.Ninja;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
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

import static jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams.*;

public final class NinjaOni2 extends JavaPlugin {

    @Getter
    private static NinjaOni2 instance;

    @Getter
    @Setter
    private GameState gameState;

    @Getter
    private Config myConfig;

    @Getter
    private CommandManager command;

    @Getter
    private ProtocolManager protocol;

    @Getter
    private static final List<Ninja> ninjas = new ArrayList<>();

    //スコアボード
    private ScoreboardManager sm;
    private Scoreboard board;
    private static Team oni;
    private static Team pl;
    private static Team lockedpl;
    private static Team spectator;

    @Override
    public void onEnable() {
        instance = this;

        //ゲーム状態の設定
        gameState = GameState.NONE;

        //ProtocolLibの設定
        protocol = ProtocolLibrary.getProtocolManager();
        initPacketListener();

        //チームの設定
        initTeams();

        //コンフィグの設定
        myConfig = new Config(getInstance());

        //コマンドの設定
        command = new CommandManager(getInstance());
        command.setup();

        //リスナーの設定
        new JoinQuitListener(getInstance());
        new NinjaMoveListener(getInstance());
        new NinjaOniListener(getInstance());
        new NinjaItemListener(getInstance());

    }

    @Override
    public void onDisable() {
        for (String name : oni.getEntries()) {
            oni.removeEntry(name);
        }
        for (String name : pl.getEntries()) {
            pl.removeEntry(name);
        }
        for (String name : spectator.getEntries()) {
            spectator.removeEntry(name);
        }

        Bukkit.getScheduler().cancelTasks(getInstance());
    }

    public void gameStart(int countdownTime, int gameTime) {
        setGameState(GameState.COUNTDOWN);

        //チームの設定
        for (Ninja ninja : ninjas) {
            if (ninja.getTeam() == SPECTATOR) {
                //観戦者チームにいた場合
                addPlayerToTeam(ninja.getPlayer(), SPECTATOR);
                ninja.getPlayer().setGameMode(GameMode.SPECTATOR);
            } else if (ninja.getTeam() == ONI) {
                //鬼チームにいた場合
                Player player = ninja.getPlayer();
                addPlayerToTeam(ninja.getPlayer(), ONI);
                Location loc = getMyConfig().getTPLocationOni().clone();
                loc.setWorld(player.getWorld());
                player.teleport(loc);
            } else {
                //プレイヤーの場合
                ninja.setTeam(Teams.PLAYER);
                Player player = ninja.getPlayer();
                addPlayerToTeam(ninja.getPlayer(), PLAYER);
                Location loc = getMyConfig().getTPLocationPlayer().clone();
                loc.setWorld(player.getWorld());
                player.teleport(loc);
            }
        }

        //鬼の見た目の設定
        for(Ninja ninja : ninjas) {
            if(ninja.getTeam() == ONI) {

            }
        }

        //インベントリの設定
        for(Ninja ninja : ninjas) {
            ninja.getPlayer().getInventory().clear(); //インベントリ初期化

            PlayerInventory inv = ninja.getPlayer().getInventory();
            if(ninja.getTeam() != ONI) {
                ItemStack kemuri = ItemManager.getKemuri();
                ItemStack kakure = ItemManager.getKakure();

                kemuri.setAmount(64);
                kakure.setAmount(64);

                inv.setItem(20, kakure);
                inv.setItem(21, kemuri);
            } else {
                inv.setHelmet(ItemManager.getOniHelmet());
                inv.setChestplate(ItemManager.getOniChestplate());
                inv.setLeggings(ItemManager.getOniLeggings());
                inv.setBoots(ItemManager.getOniBoots());

                ItemStack item = ItemManager.getKunai();
                item.setAmount(64);
                inv.setItem(20,item);

                ItemStack kageoi = ItemManager.getKageoi();
                kageoi.setAmount(64);
                inv.setItem(21, kageoi);
            }
        }

        //Taskの実行
        new CountDownTask(countdownTime).runTaskTimer(this, 0L, 20L);
        new GameTask(gameTime).runTaskTimer(this, 0L, 20L);
        new MovementTask().runTaskTimer(this, 0L, 20L);
    }

    public void gameEnd() {
        //チームから全員外す
        for (String name : oni.getEntries()) {
            oni.removeEntry(name);
        }
        for (String name : pl.getEntries()) {
            pl.removeEntry(name);
        }
        for (String name : spectator.getEntries()) {
            spectator.removeEntry(name);
        }

        //ゲーム状態変更
        setGameState(GameState.NONE);

        //全員のゲームモード変更
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.setGameMode(GameMode.ADVENTURE);
        }

        for (Ninja np : getNinjas()) {
            np.setHp(60);
            np.setLocked(false);
            np.setTeam(NONE);
        }
    }

    private void initPacketListener() {
    }

    private void initTeams() {
        sm = Bukkit.getScoreboardManager();
        board = sm.getMainScoreboard();

        if (board.getTeam(ONI.getTeamName()) == null) {
            oni = board.registerNewTeam(ONI.getTeamName());
        } else {
            oni = board.getTeam(ONI.getTeamName());
        }
        oni.setPrefix(ONI.getPrefix());
        oni.setSuffix(ONI.getSuffix());
        oni.setDisplayName(ONI.getTeamName());
        oni.setAllowFriendlyFire(false);
        oni.setColor(ChatColor.DARK_AQUA);
        oni.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

        if (board.getTeam(PLAYER.getTeamName()) == null) {
            pl = board.registerNewTeam(PLAYER.getTeamName());
        } else {
            pl = board.getTeam(PLAYER.getTeamName());
        }
        pl.setPrefix(PLAYER.getPrefix());
        pl.setSuffix(PLAYER.getSuffix());
        pl.setDisplayName(PLAYER.getTeamName());
        pl.setAllowFriendlyFire(false);
        pl.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

        if(board.getTeam(LOCKEDPLAYER.getTeamName()) == null) {
            lockedpl = board.registerNewTeam(LOCKEDPLAYER.getTeamName());
        } else {
            lockedpl = board.getTeam(LOCKEDPLAYER.getTeamName());
        }
        lockedpl.setPrefix(LOCKEDPLAYER.getPrefix());
        lockedpl.setSuffix(LOCKEDPLAYER.getSuffix());
        lockedpl.setDisplayName(LOCKEDPLAYER.getTeamName());
        lockedpl.setAllowFriendlyFire(false);
        lockedpl.setColor(ChatColor.RED);

        if (board.getTeam(SPECTATOR.getTeamName()) == null) {
            spectator = board.registerNewTeam(SPECTATOR.getTeamName());
        } else {
            spectator = board.getTeam(SPECTATOR.getTeamName());
        }
        spectator.setPrefix(SPECTATOR.getPrefix());
        spectator.setSuffix(SPECTATOR.getSuffix());
        spectator.setDisplayName(SPECTATOR.getTeamName());
        spectator.setAllowFriendlyFire(false);
        spectator.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
    }

    public static void addPlayerToTeam(Player player, Teams team) {
        switch (team) {
            case ONI:
                oni.addEntry(player.getName());
                break;
            case SPECTATOR:
                spectator.addEntry(player.getName());
                break;
            case LOCKEDPLAYER:
                lockedpl.addEntry(player.getName());
                break;
            default:
                pl.addEntry(player.getName());
                break;
        }
    }

    public static boolean containsNinja(Player player) {
        boolean result = false;

        for (Ninja np : ninjas) {
            if (np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                result = true;
            }
        }

        return result;
    }

    public static void addNinjaPlayer(Ninja ninja) {
        if (containsNinja(ninja.getPlayer())) {
            updateNinjaPlayer(ninja);
        } else {
            ninjas.add(ninja);
        }
    }

    public static void updateNinjaPlayer(Ninja ninja) {
        if (!containsNinja(ninja.getPlayer())) {
            return;
        }

        Ninja oldNinja = getNinjaPlayer(ninja.getPlayer());
        oldNinja.setClimbing(ninja.isClimbing());
        oldNinja.setTeam(ninja.getTeam());
        NinjaOni2.addPlayerToTeam(ninja.getPlayer(), ninja.getTeam());
    }

    public static Ninja getNinjaPlayer(Player player) {
        Ninja result = null;

        if (containsNinja(player)) {
            for (int i = 0; i < ninjas.size(); i++) {
                Ninja np = ninjas.get(i);
                if (np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    result = np;
                }

            }
        }

        return result;
    }

    public static int countNinja(Teams team) {
        int result = 0;
        for (Ninja ninja : ninjas) {
            if (ninja.getTeam() == team) {
                result++;
            }
        }

        return result;
    }


}
