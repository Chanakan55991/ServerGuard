package live.chanakancloud.serverguard;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import live.chanakancloud.serverguard.command.AnticheatCommand;
import live.chanakancloud.serverguard.player.PlayerData;
import live.chanakancloud.serverguard.player.PlayerDataManager;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Braydon
 */
@Getter
public class Anticheat extends JavaPlugin {
    public static Anticheat INSTANCE;

    private PacketEvents packetEvents;
    private double[] recentTps;

    @Override
    public void onLoad() {
        INSTANCE = this;
        packetEvents = PacketEvents.create(this);
        packetEvents.load();
    }

    @Override
    public void onEnable() {
        packetEvents.init(new PacketEventsSettings().checkForUpdates(false));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            recentTps = packetEvents.getServerUtils().getRecentTPS();
        }, 0L, 20L);
        new PlayerDataManager(this);
        getCommand("anticheat").setExecutor(new AnticheatCommand());
    }

    @Override
    public void onDisable() {
        packetEvents.terminate();
        for (Player player : Bukkit.getOnlinePlayers())
            PlayerData.cleanup(player);
    }
}