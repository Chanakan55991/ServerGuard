package live.chanakancloud.serverguard.check;

import org.bukkit.event.Event;
import live.chanakancloud.serverguard.Anticheat;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import live.chanakancloud.serverguard.Anticheat;
import live.chanakancloud.serverguard.api.check.CheckInfo;
import live.chanakancloud.serverguard.api.event.PlayerCheatEvent;
import live.chanakancloud.serverguard.api.event.PlayerPunishEvent;
import live.chanakancloud.serverguard.api.player.Violation;
import live.chanakancloud.serverguard.common.MovementData;
import live.chanakancloud.serverguard.meta.MetadataManager;
import live.chanakancloud.serverguard.player.PlayerData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The purpose of a check is to detect specific cheats. Each check is capable of handling packets, movement, and attacking.
 * Each check object contains player data, the player, and information for the check.
 *
 * @author Braydon
 * @see PlayerData
 * @see Player
 * @see CheckInfo
 */
@Getter
public class Check {
    protected final PlayerData playerData;
    protected final Player player;
    protected double vl;
    private final CheckInfo checkInfo;

    @Getter(AccessLevel.NONE) private int violations;

    public Check(@NonNull PlayerData playerData) throws ClassNotFoundException {
        this.playerData = playerData;
        if (!getClass().isAnnotationPresent(CheckInfo.class))
            throw new ClassNotFoundException("Check is missing @CheckInfo annotation");
        player = playerData.getBukkitPlayer();
        checkInfo = getClass().getAnnotation(CheckInfo.class);
    }

    /**
     * This method is fired when the player sends or receives a packet.
     *
     * @param packetId the id of the packet
     * @param nmsPacket the nms packet
     * @param packet the raw nms packet
     * @param timestamp the timestamp the packet was handled
     * @see PacketType
     * @see NMSPacket
     */
    public void handle(byte packetId, @NonNull NMSPacket nmsPacket, @NonNull Object packet, long timestamp) {}

    /**
     * This method is fired when the player moves.
     *
     * @param movementData the data of the movement
     * @param timestamp the timestamp the movement was handled
     * @see Location
     * @see MovementData
     */
    public void handle(MovementData movementData, long timestamp) {}
    
    public void handle(Event e) {}

    /**
     * This method is used to debug the check with the given data.
     *
     * @param data the data to include in the debug
     */
    protected final void debug(String... data) {
        if (data.length < 1)
            throw new IllegalArgumentException("Cannot debug with no data");
        String message = ChatColor.stripColor(String.join(", ", data)).trim();
        for (Player debugger : Bukkit.getOnlinePlayers()) {
            PlayerData debuggerPlayerData = PlayerData.get(debugger);
            if (!debuggerPlayerData.isDebugging(playerData, getClass()))
                continue;
            debugger.sendMessage("§8[§cDEBUG§8] §7" + message);
        }
    }

    /**
     * This method is used to flag the player with the given data.
     * <p>
     * When a player is flagged, all online staff members are alerted with the check they flagged and the data
     *
     * @param data the optional data to include in the flag
     */
    protected final void flag(String... data) {
        violations++;

        Violation violation = new Violation(
                checkInfo,
                data,
                violations,
                player.getLocation(),
                playerData.packetProcessor.ping,
                Anticheat.INSTANCE.getRecentTps()[0],
                System.currentTimeMillis()
        );
        playerData.addViolation(violation);

        PlayerCheatEvent playerCheatEvent = new PlayerCheatEvent(player, violation);
        Bukkit.getScheduler().runTask(Anticheat.INSTANCE, () -> Bukkit.getPluginManager().callEvent(playerCheatEvent));
        if (playerCheatEvent.isCancelled())
            return;

        String message = ChatColor.stripColor(String.join(", ", data)).trim();
        String checkName = (checkInfo.experimental() ? "§7§o*" : "§f") + checkInfo.name();
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (!staff.hasPermission("anticheat.alert"))
                continue;
            staff.sendMessage("§8[§6§lAC§8] §f" + player.getName() + " §7flagged " + checkName + " §c(x" + violations + ")" +
                    (message.isEmpty() ? "" : " §7[" + message + "]"));
        }
        if (violations >= checkInfo.maxVl() && checkInfo.ban() && !checkInfo.experimental() && !playerData.isBanned()) {
            playerData.setBanned(true);

            String metadataJson = MetadataManager.getMetadataJson(playerData);
            Anticheat.INSTANCE.getLogger().info(player.getName() + " was banned for cheating (" + checkInfo.name() + "): " + metadataJson);

            Bukkit.getPluginManager().callEvent(new PlayerPunishEvent(playerCheatEvent));
            Bukkit.getScheduler().runTask(Anticheat.INSTANCE, () -> player.kickPlayer("[AC] Unfair Advantage"));
        }
    }
}