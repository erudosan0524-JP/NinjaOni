package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils;

import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class NinjaPlayer {

    private static final int defaultHP = 100;

    private Player player;
    private int hp;
    private PlayerStatus status;
    private boolean isClimbing;

    public NinjaPlayer(Player player, int hp, PlayerStatus status) {
        this.player = player;
        this.hp = hp;
        this.status = status;
        this.isClimbing = false;
    }

    public NinjaPlayer(Player player, PlayerStatus status) {
        this(player,defaultHP, status);
    }

    public NinjaPlayer(Player player) {
        this(player,defaultHP,PlayerStatus.NONE);
    }

    //HPを1減らす関数
    public void decHP() {
        this.hp -= 1;
    }

}
