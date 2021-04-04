package live.chanakancloud.serverguard.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Location;

import live.chanakancloud.serverguard.processor.impl.MovementProcessor;

import java.util.List;

/**
 * This object stores data based on a player's movement.
 *
 * @author Braydon
 * @see MovementProcessor
 */
@AllArgsConstructor @Getter
public class MovementData {
    public final Location from, to;
    private final List<MovementType> movementTypes;

    public boolean isPositionLook() {
        return isPosition() && isPositionLook();
    }

    public boolean isAscending() {
        return to.getY() > from.getY();
    }

    public boolean isDescending() {
        return !isAscending();
    }

    public boolean isPosition() {
        return movementTypes.contains(MovementType.X) || movementTypes.contains(MovementType.Y) || movementTypes.contains(MovementType.Z);
    }

    public boolean isLook() {
        return movementTypes.contains(MovementType.YAW) || movementTypes.contains(MovementType.PITCH);
    }

    public enum MovementType {
        X, Z, Y, YAW, PITCH
    }
}