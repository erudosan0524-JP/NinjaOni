package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.subcommands;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class SpawnShop extends SubCommand {

    public SpawnShop(NinjaOni2 plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Location loc = player.getLocation();

        if(args.length > 0) {
            Villager villager = (Villager) player.getWorld().spawnEntity(loc, EntityType.VILLAGER);
            villager.setAI(false);
            villager.setAdult();
            villager.setCanPickupItems(false);
            villager.setCustomNameVisible(true);
            villager.setGravity(true);
            villager.setSilent(true);
            villager.setInvulnerable(true);
            villager.setRotation(player.getLocation().getYaw(), player.getLocation().getPitch());

            if(args[0].equalsIgnoreCase("oni")) {
                villager.setCustomName(ChatColor.GREEN + "鬼専用ショップ");
            } else if (args[0].equalsIgnoreCase("player")){
                villager.setCustomName(ChatColor.GREEN + "プレイヤー専用ショップ");
            }
        }
    }

    @Override
    public String name() {
        return "spawnshop";
    }

    @Override
    public String info() {
        return "";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
