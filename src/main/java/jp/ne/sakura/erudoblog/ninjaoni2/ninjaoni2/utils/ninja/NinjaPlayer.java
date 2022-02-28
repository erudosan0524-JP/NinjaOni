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

    @Getter
    @Setter
    private int hp;

    @Getter
    @Setter
    private int kakureTime; //隠れられる時間

    public NinjaPlayer(Player player, boolean isClimbing, boolean isLocked) {
        super(player, isClimbing, Teams.PLAYER);
        this.isLocked = isLocked;
        this.hp = 40;
        this.kakureTime = 5;
    }




}
