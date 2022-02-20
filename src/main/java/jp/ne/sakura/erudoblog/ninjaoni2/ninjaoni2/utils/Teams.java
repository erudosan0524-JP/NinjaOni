package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.utils;

public enum Teams {
    ONI("oni","§c", "§f"), //鬼チーム
    PLAYER("player", "§9", "§f"), //プレイヤーチーム
    SPECTATOR("spectator", "§7", "§f"), //観戦者チーム
    NONE("","",""); //どこにも所属していない

    private String teamName;
    private String prefix;
    private String suffix;

    Teams(String teamName, String prefix, String suffix) {
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
