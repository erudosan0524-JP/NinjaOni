package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.CommandManager;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener.JoinQuitListener;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener.NinjaMoveListener;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener.NinjaOniListener;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener.NinjaPlayerListener;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.runnable.CountDown;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.runnable.GameTask;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public final class NinjaOni extends JavaPlugin {

    @Getter
    private static NinjaOni instance;

    private static List<NinjaPlayer> ninja = new ArrayList<>();

    @Getter
    @Setter
    private GameState gameState;

    @Getter
    private Config myConfig;

    @Getter
    private CommandManager command;

    private ScoreboardManager sm;
    private Scoreboard board;

    private Team oni,player,spectator;

    @Override
    public void onEnable() {
        instance = this;

        //ゲーム状態のセットアップ
        gameState = GameState.NONE;

        //コンフィグのセットアップ
        myConfig = new Config(getInstance());

        //コマンドのセットアップ
        command = new CommandManager(getInstance());
        command.setup();

        //チームのセットアップ
        setupTeams();

        //リスナーのセットアップ
        new NinjaMoveListener(getInstance());
        new JoinQuitListener(getInstance());
        new NinjaOniListener(getInstance());
        new NinjaPlayerListener(getInstance());
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(getInstance());
        ninja.removeAll(ninja);
    }


    private void setupTeams() {
        sm = Bukkit.getServer().getScoreboardManager();
        board = sm.getMainScoreboard();

        this.oni = board.registerNewTeam(PlayerStatus.ONI.getTeamName());
        oni.setPrefix(PlayerStatus.ONI.getPrefix());
        oni.setSuffix(PlayerStatus.ONI.getSuffix());
        oni.setAllowFriendlyFire(false);

        this.player = board.registerNewTeam(PlayerStatus.PLAYER.getTeamName());
        player.setPrefix(PlayerStatus.PLAYER.getPrefix());
        player.setSuffix(PlayerStatus.PLAYER.getSuffix());
        player.setAllowFriendlyFire(false);

        this.spectator = board.registerNewTeam(PlayerStatus.SPECTATOR.getTeamName());
        spectator.setPrefix(PlayerStatus.SPECTATOR.getPrefix());
        spectator.setSuffix(PlayerStatus.SPECTATOR.getSuffix());
        spectator.setAllowFriendlyFire(false);
    }

    //ゲームスタート時の処理
    public void gameStart(int countdownTime, int gameTime) {
        for(NinjaPlayer np : NinjaOni.getNinjaPlayers()) {
            if(np.getStatus() == PlayerStatus.ONI) {
                System.out.println(np.getPlayer().getName() + "add Oni team");
                oni.addEntry(np.getPlayer().getName());
            } else if(np.getStatus() == PlayerStatus.PLAYER) {
                System.out.println(np.getPlayer().getName() + "add Player team");
                player.addEntry(np.getPlayer().getName());
            } else if(np.getStatus() == PlayerStatus.SPECTATOR) {
                System.out.println(np.getPlayer().getName() + "add Spectator team");
                spectator.addEntry(np.getPlayer().getName());
            }
        }

        gameState = GameState.COUNTDOWN;

        for (NinjaPlayer ninja : ninja) {
            if (ninja.getStatus() == PlayerStatus.SPECTATOR) {
                //スペクテイターモードの人
                ninja.setStatus(PlayerStatus.SPECTATOR);
                ninja.getPlayer().setGameMode(GameMode.SPECTATOR);

            } else if (ninja.getStatus() == PlayerStatus.ONI) {
                //鬼の人
                Player player = ninja.getPlayer();
                Location loc = getMyConfig().getTPLocationOni();
                loc.setWorld(player.getWorld());
                player.teleport(getMyConfig().getTPLocationOni());
            } else {
                //通常プレイヤーの人
                ninja.setStatus(PlayerStatus.PLAYER);
                Player player = ninja.getPlayer();
                Location loc = getMyConfig().getTPLocationPlayer();
                loc.setWorld(player.getWorld());
                player.teleport(getMyConfig().getTPLocationPlayer());
            }
        }

        new CountDown(countdownTime).runTaskTimer(this, 0L, 20L);
        new GameTask(gameTime).runTaskTimer(this, 0L, 20L);
    }

    //ゲーム終了時の処理
    public void gameEnd() {
        for(String name : oni.getEntries()) {
            oni.removeEntry(name);
        }
        for(String name : player.getEntries()) {
            player.removeEntry(name);
        }
        for(String name : spectator.getEntries()) {
            spectator.removeEntry(name);
        }

        setGameState(GameState.NONE);

        for (NinjaPlayer np : getNinjaPlayers()) {
            Player player = np.getPlayer();
            updateNinjaPlayer(player, PlayerStatus.NONE);
        }
    }

    //存在しない場合null, 存在する場合NinjaPlayer
    public static NinjaPlayer contains(Player player) {
        NinjaPlayer np = null;

        for (NinjaPlayer p : ninja) {
            if (p.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                np = p;
            }
        }

        return np;
    }

    public static void addNinjaPlayer(Player player) {
        ninja.add(new NinjaPlayer(player));
    }

    public static void addNinjaPlayer(NinjaPlayer player) {
        ninja.add(player);
    }

    public static void updateNinjaPlayer(Player player, PlayerStatus status) {
        if (contains(player) != null) {
            NinjaPlayer np = contains(player);
            np.setPlayer(player);
            np.setStatus(status);
        } else {
            addNinjaPlayer(new NinjaPlayer(player,status));
        }
    }

    public static NinjaPlayer getNinjaPlayer(Player player) {
        NinjaPlayer result = null;
        if (contains(player) != null) {
            result = contains(player);
        }
        return result;
    }

    public static List<NinjaPlayer> getNinjaPlayers() {
        return ninja;
    }
}
