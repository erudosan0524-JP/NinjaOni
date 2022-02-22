package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.ninja;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class NinjaPlayer extends Ninja {

    @Getter
    @Setter
    private boolean isLocked; //捕まっているか

    public NinjaPlayer(Player player, boolean isClimbing, boolean isLocked) {
        super(player, isClimbing, Teams.PLAYER);
        this.isLocked = isLocked;
    }




}
