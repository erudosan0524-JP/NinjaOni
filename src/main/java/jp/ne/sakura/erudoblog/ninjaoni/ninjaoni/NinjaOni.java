package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.Config;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.GameState;
import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils.NinjaPlayer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class NinjaOni extends JavaPlugin {

    @Getter
    @Setter
    private static NinjaOni instance;

    private static List<NinjaPlayer> ninja = new ArrayList<>();

    @Getter
    @Setter
    private GameState gameState;

    @Getter
    private Config myConfig;



    @Override
    public void onEnable() {
        setInstance(this);

        //ゲーム状態のセットアップ
        gameState = GameState.NONE;

        //コンフィグのセットアップ
        myConfig = new Config(getInstance());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
}
