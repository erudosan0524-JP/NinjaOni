package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2;

/* ゲームにまつわる処理の根幹を担うクラス*/
public class Game {
    public enum GameState {
        INGAME, //ゲーム中
        COUNTDOWN, //カウントダウン中
        NONE //何もしていない
    }

    public enum Teams {
        ONI("oni","§c", "§f"), //鬼チーム
        PLAYER("player", "§9", "§f"), //プレイヤーチーム
        LOCKEDPLAYER("lockedplayer", "",""), //捕まっているプレイヤーチーム
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
}
