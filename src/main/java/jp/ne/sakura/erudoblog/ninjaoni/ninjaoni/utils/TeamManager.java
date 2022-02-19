package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private ScoreboardManager sm;
    private Scoreboard board;

    private Team oni;
    private Team player;
    private Team spectator;

    private NinjaOni plugin;

    public TeamManager(NinjaOni plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        sm = Bukkit.getServer().getScoreboardManager();
        board = sm.getNewScoreboard();

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

    public void load() {
        for(NinjaPlayer np : NinjaOni.getNinjaPlayers()) {
            if(np.getStatus() == PlayerStatus.ONI) {
                oni.addEntry(np.getPlayer().getName());
            } else if(np.getStatus() == PlayerStatus.PLAYER) {
                player.addEntry(np.getPlayer().getName());
            } else if(np.getStatus() == PlayerStatus.SPECTATOR) {
                spectator.addEntry(np.getPlayer().getName());
            }
        }
    }

    public void reset() {
        for(String name : oni.getEntries()) {
            oni.removeEntry(name);
        }
        for(String name : player.getEntries()) {
            player.removeEntry(name);
        }
        for(String name : spectator.getEntries()) {
            spectator.removeEntry(name);
        }
    }
}
