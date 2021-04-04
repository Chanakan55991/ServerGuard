package live.chanakancloud.serverguard.check.impl.speed;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import lombok.NonNull;
import live.chanakancloud.serverguard.check.Check;
import live.chanakancloud.serverguard.common.MovementData;
import live.chanakancloud.serverguard.player.PlayerData;
import live.chanakancloud.serverguard.api.check.CheckInfo;
import live.chanakancloud.serverguard.api.check.CheckType;

/**
 * This is just an example check to showcase the capabilities of the framework.
 *
 * @author Braydon
 */
@CheckInfo(name = "Speed (A)", type = CheckType.TEST)
public class SpeedA extends Check {
	private double lastDist;
	private boolean lastOnGround;
    public SpeedA(@NonNull PlayerData playerData) throws ClassNotFoundException {
        super(playerData);
    }

    @Override
    public void handle(MovementData movementData, long timestamp) {
        double distX = movementData.to.getX() - movementData.from.getX();
        double distZ = movementData.to.getZ() - movementData.from.getZ();
        double dist = (distX * distX) + (distZ * distZ);
        double lastDist = this.lastDist;
        this.lastDist = dist;
        
        boolean onGround = playerData.getBukkitPlayer().isOnGround();
        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = onGround;
        
        float friction = 0.91F;
        double shiftedLastDist = lastDist * friction;
        double equalness = dist - shiftedLastDist;
        
        debug("equalness: " + equalness);
    }
}
