package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Data
public class Ninja {

    private Player player;
    private boolean isClimbing; //壁を上っているか
    private Teams team; //所属チーム
    private boolean isLocked; //捕まっているか
    private int hp;

    public Ninja(Player player, boolean isClimbing, boolean isLocked, int hp, Teams team) {
        this.player = player;
        this.isClimbing = isClimbing;
        this.isLocked = isLocked;
        this.hp = hp;
        this.team = team;
    }

    public Ninja(Player player, Teams team) {
        this(player,false,false, 40, team);
    }
}
