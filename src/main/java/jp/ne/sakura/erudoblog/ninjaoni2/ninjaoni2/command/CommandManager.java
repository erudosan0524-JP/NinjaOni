package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.SubCommand;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.subcommands.Help;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.subcommands.Start;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.command.commands.subcommands.Warp;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager implements CommandExecutor {
    private NinjaOni2 plugin;
    private final String mainCommand = "ninja";
    private final List<SubCommand> commands = new ArrayList<>();

    public CommandManager(NinjaOni2 plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        if(plugin.getCommand(mainCommand) == null) {
            System.out.println("Command null");
            return;
        }

        plugin.getCommand(mainCommand).setExecutor(this);

        this.commands.add(new Help(plugin));
        this.commands.add(new Start(plugin));
        this.commands.add(new Warp(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getLogger().info("Only players can run this command!");
            return true;
        }

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase(mainCommand)) {
            if(args.length == 0) {
                player.sendMessage("Command length equals ZERO");
                return true;
            }

            //<command> args[0] args[1] args[2]...

            SubCommand target = this.get(args[0]);

            if(Objects.isNull(target)) {
                player.sendMessage("Invalid Command");
                return true;
            }

            ArrayList<String> arrayList = new ArrayList<>();

            //<command> <subcommand> args[0] args[1]...

            arrayList.addAll(Arrays.asList(args));
            arrayList.remove(0); //index 0 はサブコマンド本体

            try {
                target.onCommand(player, arrayList.toArray(new String[arrayList.size()]));
            } catch(Exception e) {
                player.sendMessage("Command Error");

                e.printStackTrace();
            }

        }

        return true;
    }

    private SubCommand get(String name) {
        Iterator<SubCommand> subcommands = this.commands.iterator();

        while(subcommands.hasNext()) {
            SubCommand sc = (SubCommand) subcommands.next();

            if(sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases;
            int var6 = (aliases = sc.aliases()).length;

            for(int var5 = 0; var5 < var6; var5++) {
                String alias = aliases[var5];
                if(name.equalsIgnoreCase(alias)) {
                    return sc;
                }
            }
        }

        return null;
    }
}
