package live.chanakancloud.serverguard.processor;

import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import org.bukkit.event.Event;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import live.chanakancloud.serverguard.Anticheat;
import live.chanakancloud.serverguard.player.PlayerData;
import live.chanakancloud.serverguard.processor.impl.PacketProcessor;
import lombok.NonNull;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * A processor is used to process certain data such as from packets, events, etc.
 * <p>
 * All processors can handle packets and bukkit events
 *
 * @see PacketProcessor for an example
 * @see NMSPacket
 * @see Event
 * @author Braydon
 */
public class Processor extends PacketListenerDynamic implements Listener {
    protected final PlayerData playerData;

    public Processor(@NonNull PlayerData playerData) {
        super(PacketEventPriority.NORMAL);
        this.playerData = playerData;
        Anticheat.INSTANCE.getPacketEvents().getEventManager().registerListener(this);
        Bukkit.getPluginManager().registerEvents(this, Anticheat.INSTANCE);
    }
}