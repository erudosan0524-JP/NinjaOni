package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils;

import org.bukkit.ChatColor;

public enum PlayerStatus {
    ONI("oni","§c", "§f"),
    PLAYER("player", "§9", "§f"),
    SPECTATOR("spectator", "§7", "§f"),
    NONE("","","");

    private String teamName;
    private String prefix;
    private String suffix;

    PlayerStatus(String teamName, String prefix, String suffix) {
        this.teamName = teamName;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
