package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.items;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.NinjaOni2;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.item.NinjaItem;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.Ninja;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.Teams;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kageoi implements NinjaItem {
    @Override
    public int slot() {
        return 21;
    }

    @Override
    public Material type() {
        return Material.HEART_OF_THE_SEA;
    }

    @Override
    public HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "影追玉";
    }

    @Override
    public void execute(Ninja ninja) {
        Player player = ninja.getPlayer();

        List<Player> glowPlayers = new ArrayList<>();

        //光らせるプレイヤーの設定
        for (Ninja nin : NinjaOni2.getNinjas()) {
            if (nin.getTeam() == Teams.PLAYER) {
                if(!glowPlayers.contains(nin.getPlayer())) {
                    glowPlayers.add(nin.getPlayer());
                }
            }
        }

        new BukkitRunnable() {

            int count = 8;

            @Override
            public void run() {
                if(count < 0) {
                    this.cancel();
                } else {
                    for (Player p : glowPlayers) {

                        PacketContainer glowPacket = NinjaOni2.getInstance().getProtocol().createPacket(PacketType.Play.Server.ENTITY_METADATA);
                        glowPacket.getIntegers().write(0, p.getEntityId()); //光らせるプレイヤーのID
                        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
                        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
                        WrappedDataWatcher.Serializer serializer2 = WrappedDataWatcher.Registry.get(Integer.class);
                        watcher.setEntity(p); //光らせるプレイヤーを指定
                        watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page

                        glowPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created

                        try {
                            NinjaOni2.getInstance().getProtocol().sendServerPacket(player, glowPacket);
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                count--;
            }

        }.runTaskTimer(NinjaOni2.getInstance(), 0L, 20L);
    }

    @Override
    public NinjaItemType ninjaItemType() {
        return NinjaItemType.ONI_ITEM;
    }
}
