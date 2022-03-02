package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {

    public static void sendAll(String message) {
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendMessage(ChatColor.DARK_GRAY + "> " + ChatColor.WHITE + message);
        }
    }
}
