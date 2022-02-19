package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import org.bukkit.event.Listener;

public class NinjaOniListener implements Listener {

    private NinjaOni plugin;

    public NinjaOniListener(NinjaOni plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
