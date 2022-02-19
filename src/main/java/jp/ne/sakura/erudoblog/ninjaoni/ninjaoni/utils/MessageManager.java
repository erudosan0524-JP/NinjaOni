package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageManager {
    public static void sendMessageAll(String message) {
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public static void sendTitleAll(String title, String subtitle, int fadein, int stay, int fadeout) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendTitle(title, subtitle, fadein, stay, fadeout);
        }
    }
}
