package jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.listener;

import jp.ne.sakura.erudoblog.ninjaoni.ninjaoni.NinjaOni;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    public JoinQuitListener(NinjaOni plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        NinjaOni.addNinjaPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        NinjaOni.getNinjaPlayers().removeIf(np -> np.getPlayer().getName().equals(player.getName()));
    }
}