package live.chanakancloud.serverguard.check.impl.fly;

import live.chanakancloud.serverguard.util.VersionUtil;
import lombok.NonNull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import live.chanakancloud.serverguard.api.check.CheckInfo;
import live.chanakancloud.serverguard.api.check.CheckType;
import live.chanakancloud.serverguard.check.Check;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.protobuf.Timestamp;

import live.chanakancloud.serverguard.common.MovementData;
import live.chanakancloud.serverguard.player.PlayerData;
import live.chanakancloud.serverguard.util.Util;

@CheckInfo(name = "Fly (A)", type = CheckType.FLY, experimental = true)
public class FlyA extends Check {
	public FlyA(@NonNull PlayerData playerData) throws ClassNotFoundException {
		super(playerData);
	}
    private static final double TOLERANCE = 0.005;

    private Double lastOffsetY;
    @Override
    public void handle(MovementData data, long timestamp) { 
    	
        if (player.getVelocity().getY() > 5 || Util.isTeleporting(data) || Util.isNearClimbable(player)) {
            lastOffsetY = null;
            return;
        }

        double offsetY = data.to.getY() - data.from.getY(); 

        if (!Util.isNearGround(data.from) && !Util.isNearGround(data.to)) { 
            if (lastOffsetY != null && !Util.isNearWater(player)) {
                double expectedOffsetY = (lastOffsetY - 0.08D) * 0.9800000190734863D;

                // We're going to ignore them if they're in an unloaded chunk
                if (offsetY + 0.09800000190734881 <= 0.001) {
                    lastOffsetY = null;
                    return;
                }

                double difference = Math.abs(expectedOffsetY - offsetY);

                // Since we don't have any direct calculation for vertical collision we'll just make it a bit more lenient
                int limit = Util.isUnderBlock(player) ? 3 : 1;

                if (difference > TOLERANCE) {
                    if (++vl > limit) {
                        flag(String.format("%s -> %s", offsetY, expectedOffsetY));
                    }
                } else {
                    vl = vl - 0.45;
                }
            }

            lastOffsetY = offsetY;
        } else {
            lastOffsetY = null;
        }
    }
}

