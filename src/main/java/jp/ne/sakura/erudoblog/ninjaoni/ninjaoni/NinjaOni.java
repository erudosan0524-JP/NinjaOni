package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.command.CommandManager;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener.JoinQuitListener;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener.NinjaMoveListener;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.runnable.CountDown;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.runnable.GameTask;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

    @Getter
    private TeamManager teamManager;

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
        teamManager.load();

        //リスナーのセットアップ
        new NinjaMoveListener(getInstance());
        new JoinQuitListener(getInstance());
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(getInstance());
        ninja.removeAll(ninja);
    }

    //ゲームスタート時の処理
    public void gameStart(int countdownTime, int gameTime) {
        teamManager.load();
        gameState = GameState.COUNTDOWN;

        for (NinjaPlayer ninja : ninja) {
            if (ninja.getStatus() == PlayerStatus.SPECTATOR) {
                //スペクテイターモードの人
                ninja.setStatus(PlayerStatus.SPECTATOR);
                ninja.getPlayer().setGameMode(GameMode.SPECTATOR);
            } else if(ninja.getStatus() == PlayerStatus.ONI){
                //鬼の人
                Player player = ninja.getPlayer();

            } else {
                //通常プレイヤーの人
                ninja.setStatus(PlayerStatus.PLAYER);
                Player player = ninja.getPlayer();
            }
        }

        new CountDown(countdownTime).runTaskTimer(this, 0L, 20L);
        new GameTask(gameTime).runTaskTimer(this, 0L, 20L);
    }

    //ゲーム終了時の処理
    public void gameEnd() {

    }

    //存在しない場合-1, 存在する場合そのインデックス値
    public static int contains(Player player) {
        int result = -1;

        for(int i = 0; i < ninja.size(); i++) {
            NinjaPlayer np = ninja.get(i);
            if(np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                result = i;
            }
        }

        return result;
    }

    public static void addNinjaPlayer(Player player) {
        if(contains(player) < 0) {
            ninja.add(new NinjaPlayer(player));
        }
    }

    public static void updateNinjaPlayer(NinjaPlayer np) {
        if(contains(np.getPlayer()) >= 0) {
            ninja.remove(contains(np.getPlayer()));

            ninja.add(np);
        }
    }

    public static NinjaPlayer getNinjaPlayer(Player player) {
        NinjaPlayer result = null;
        if(contains(player) >= 0) {
            result = ninja.get(contains(player));
        }
        return result;
    }

    public static List<NinjaPlayer> getNinjaPlayers() {
        return ninja;
    }
}
