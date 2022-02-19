package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils;

import org.bukkit.ChatColor;

public enum PlayerStatus {
    ONI("鬼",ChatColor.RED.toString(), ChatColor.WHITE.toString()),
    PLAYER("逃走者", ChatColor.BLUE.toString(), ChatColor.WHITE.toString()),
    SPECTATOR("観戦者", ChatColor.GRAY.toString(), ChatColor.WHITE.toString()),
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
