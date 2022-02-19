package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.utils;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class Config {
    private Plugin plugin;
    private FileConfiguration config;

    @Getter
    private int countdownTime, gameTime;

    @Getter
    private Material warpBlockTypeOni, warpBlockTypeSpec;

    @Getter
    private Location TPLocation;

    public Config(Plugin plugin) {
        this.plugin = plugin;

        load();
    }

    private void load() {
        plugin.saveDefaultConfig();

        if (Objects.nonNull(config)) {
            reload();
        }

        config = plugin.getConfig();

        countdownTime = config.getInt("countdown-time");
        gameTime = config.getInt("game-time");

        String temp = config.getString("warp-block-type-oni");
        warpBlockTypeOni = Material.valueOf(temp);
        temp = config.getString("warp-block-type-spec");
        warpBlockTypeSpec = Material.valueOf(temp);

        double x = config.getDouble("tp-location.x");
        double y = config.getDouble(("tp-location.y"));
        double z = config.getDouble(("tp-location.z"));
        TPLocation = new Location(null, x,y,z);

    }

    public void reload() {
        plugin.reloadConfig();
    }
}
