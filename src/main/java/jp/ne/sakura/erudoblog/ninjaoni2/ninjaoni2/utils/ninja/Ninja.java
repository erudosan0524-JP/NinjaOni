package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class Ninja {

    private Player player;
    private boolean isClimbing; //壁を上っているか
    private Teams team; //所属チーム

    public Ninja(Player player, boolean isClimbing, Teams team) {
        this.player = player;
        this.isClimbing = isClimbing;
        this.team = team;
    }

    public Ninja(Player player, Teams team) {
        this(player,false,team);
    }
}
