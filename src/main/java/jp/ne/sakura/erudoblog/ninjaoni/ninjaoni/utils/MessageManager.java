package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageManager {
    public static void sendMessageAll(String message) {
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }
}
