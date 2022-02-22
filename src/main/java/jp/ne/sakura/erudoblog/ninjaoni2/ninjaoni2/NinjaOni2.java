package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.CommandManager;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener.JoinQuitListener;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener.NinjaMoveListener;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.listener.NinjaOniListener;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable.CountDownTask;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable.GameTask;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Config;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja.Ninja;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
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
    private static final List<Ninja> ninjas = new ArrayList<>();

    //スコアボード
    private ScoreboardManager sm;
    private Scoreboard board;
    private Team oni;
    private Team pl;
    private Team spectator;

    @Override
    public void onEnable() {
        instance = this;

        //ゲーム状態の設定
        gameState = GameState.NONE;

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


    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(getInstance());

    }

    public void gameStart(int countdownTime, int gameTime) {
        setGameState(GameState.COUNTDOWN);

        for(Ninja ninja : ninjas) {
            if(ninja.getTeam() == SPECTATOR) {
                //観戦者チームにいた場合
                addPlayerToTeam(ninja.getPlayer(), SPECTATOR);
                ninja.getPlayer().setGameMode(GameMode.SPECTATOR);
            }else if(ninja.getTeam() == ONI) {
                //鬼チームにいた場合
                Player player = ninja.getPlayer();
                addPlayerToTeam(ninja.getPlayer(), ONI);
                Location loc = getMyConfig().getTPLocationOni();
                loc.setWorld(player.getWorld());
                player.teleport(getMyConfig().getTPLocationOni());
            } else {
                //プレイヤーの場合
                ninja.setTeam(Teams.PLAYER);
                Player player = ninja.getPlayer();
                addPlayerToTeam(ninja.getPlayer(), PLAYER);
                Location loc = getMyConfig().getTPLocationPlayer();
                loc.setWorld(player.getWorld());
                player.teleport(getMyConfig().getTPLocationPlayer());
            }
        }

        new CountDownTask(countdownTime).runTaskTimer(this, 0L, 20L);
        new GameTask(gameTime).runTaskTimer(this, 0L, 20L);
    }

    public void gameEnd() {
        //チームから全員外す
        for(String name : oni.getEntries()) {
            oni.removeEntry(name);
        }
        for(String name : pl.getEntries()) {
            pl.removeEntry(name);
        }
        for(String name : spectator.getEntries()) {
            spectator.removeEntry(name);
        }

        //ゲーム状態変更
        setGameState(GameState.NONE);

        for(Ninja np : getNinjas()) {
            Ninja ninja = new Ninja(np.getPlayer(), NONE);
            updateNinjaPlayer(ninja);
        }
    }

    private void initTeams() {
        sm = Bukkit.getScoreboardManager();
        board = sm.getMainScoreboard();

        if(board.getTeam(ONI.getTeamName()) == null) {
            oni = board.registerNewTeam(ONI.getTeamName());
        } else {
            oni = board.getTeam(ONI.getTeamName());
        }
        oni.setPrefix(ONI.getPrefix());
        oni.setSuffix(ONI.getSuffix());
        oni.setDisplayName(ONI.getTeamName());
        oni.setAllowFriendlyFire(false);

        if(board.getTeam(PLAYER.getTeamName()) == null) {
            pl = board.registerNewTeam(PLAYER.getTeamName());
        } else {
            pl = board.getTeam(PLAYER.getTeamName());
        }
        pl.setPrefix(PLAYER.getPrefix());
        pl.setSuffix(PLAYER.getSuffix());
        pl.setDisplayName(PLAYER.getTeamName());
        pl.setAllowFriendlyFire(false);

        if(board.getTeam(SPECTATOR.getTeamName()) == null) {
            spectator = board.registerNewTeam(SPECTATOR.getTeamName());
        } else {
            spectator = board.getTeam(SPECTATOR.getTeamName());
        }
        spectator.setPrefix(SPECTATOR.getPrefix());
        spectator.setSuffix(SPECTATOR.getSuffix());
        spectator.setDisplayName(SPECTATOR.getTeamName());
        spectator.setAllowFriendlyFire(false);
    }

    public void addPlayerToTeam(Player player, Teams team) {
        switch(team) {
            case ONI:
                oni.addEntry(player.getName());
            case SPECTATOR:
                spectator.addEntry(player.getName());
            default:
                pl.addEntry(player.getName());
        }
    }

    public void removePlayerFromTeam(Player player, Teams team) {
        if(!containsTeam(player,team)) return;

        switch (team) {
            case ONI:
                oni.removeEntry(player.getName());
            case SPECTATOR:
                spectator.addEntry(player.getName());
            default:
                pl.removeEntry(player.getName());
        }
    }

    public boolean containsTeam(Player player, Teams team) {
        switch (team) {
            case ONI:
                return oni.hasEntry(player.getName());
            case SPECTATOR:
                return spectator.hasEntry(player.getName());
            default:
                return pl.hasEntry(player.getName());
        }
    }

    public static boolean containsNinja(Player player) {
        boolean result = false;

        for(Ninja np : ninjas) {
            if(np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                result = true;
            }
        }

        return result;
    }

    public static void addNinjaPlayer(Ninja ninja) {
        if(containsNinja(ninja.getPlayer())) {
            updateNinjaPlayer(ninja);
        } else {
            ninjas.add(ninja);
        }
    }

    public static void updateNinjaPlayer(Ninja ninja) {
        if(!containsNinja(ninja.getPlayer())) {
            return;
        }

        Ninja oldNinja = getNinjaPlayer(ninja.getPlayer());
        oldNinja.setClimbing(ninja.isClimbing());
        oldNinja.setTeam(ninja.getTeam());
    }

    public static Ninja getNinjaPlayer(Player player) {
        Ninja result = null;

        if(containsNinja(player)) {
            for(int i = 0; i < ninjas.size(); i++) {
                Ninja np = ninjas.get(i);
                if(np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    result = np;
                }
            }
        }

        return result;
    }

    public static int countNinja(Teams team) {
        int result = 0;
        for(Ninja ninja : ninjas) {
            if(ninja.getTeam() == team) {
                result++;
            }
        }

        return result;
    }



}
