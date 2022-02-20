package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils;

import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class NinjaPlayer {

    private Player player;
    private boolean isClimbing; //壁を上っているか
    private boolean isLocked; //捕まっているか
    private Teams team; //所属チーム

    public NinjaPlayer(Player player, boolean isClimbing, boolean isLocked, Teams team) {
        this.player = player;
        this.isClimbing = isClimbing;
        this.isLocked = isLocked;
        this.team = team;
    }

    public NinjaPlayer(Player player, Teams team) {
        this(player,false,false,team);
    }
}
