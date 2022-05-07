package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils.Teams;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Data
public class Ninja {

    private Player player;
    private boolean isClimbing; //壁を上っているか
    private Teams team; //所属チーム
    private boolean isLocked; //捕まっているか
    private int hp;
    private int money;

    private List<NinjaItem> items;

    public Ninja(Player player, boolean isClimbing, boolean isLocked, int hp, Teams team, int money) {
        this.player = player;
        this.isClimbing = isClimbing;
        this.isLocked = isLocked;
        this.hp = hp;
        this.money = money;
        this.team = team;
        this.items = new ArrayList<>();
    }

    public Ninja(Player player, Teams team) {
        this(player,false,false, 60, team, 0);
    }

    public void decHP() {
        this.hp -= 1;
    }

    public void incMoney() {
        this.money += 1;
    }

    public void decMoney() {
        this.money -= 1;
    }

    public void setLocked(boolean b) {
        this.isLocked = b;

        if(b) {
            NinjaOni2.addPlayerToTeam(this.player, Teams.LOCKEDPLAYER);
        }else {
            NinjaOni2.addPlayerToTeam(this.player, Teams.PLAYER);
        }
    }

    public void addNinjaItem(NinjaItem item) {
        if(this.items.size() > 4) {
            this.items.add(item);
        }
    }
}
