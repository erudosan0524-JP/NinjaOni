package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import org.bukkit.event.Listener;

public class NinjaPlayerListener implements Listener {
    private NinjaOni plugin;

    public NinjaPlayerListener(NinjaOni plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
