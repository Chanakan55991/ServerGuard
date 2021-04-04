package live.chanakancloud.serverguard.api.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Location;

import live.chanakancloud.serverguard.api.check.CheckInfo;

/**
 * A violation is created and stored in a player's playerdata when they are flagged by a check.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
public class Violation {
    // Check Data
    private final CheckInfo checkInfo;
    private final String[] data;
    private final int violations;

    // Player Data
    private final Location location;
    private final int ping;

    // Server Data
    private final double tps;

    private final long timestamp; // The timestamp the violation occurred
}