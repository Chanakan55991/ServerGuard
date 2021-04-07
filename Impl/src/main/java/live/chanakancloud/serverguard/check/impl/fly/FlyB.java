package live.chanakancloud.serverguard.check.impl.fly;

import cc.funkemunky.api.utils.PlayerUtils;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import live.chanakancloud.serverguard.api.check.CheckInfo;
import live.chanakancloud.serverguard.api.check.CheckType;
import live.chanakancloud.serverguard.check.Check;
import live.chanakancloud.serverguard.common.MovementData;
import live.chanakancloud.serverguard.player.PlayerData;
import live.chanakancloud.serverguard.util.Util;
import java.util.Optional;
import lombok.NonNull;

@CheckInfo(name = "Fly (B)", type = CheckType.FLY, experimental = true)
public class FlyB extends Check {
    private static final double LIMIT = 1.83;

    private double lastGroundY;
	public FlyB(@NonNull PlayerData playerData) throws ClassNotFoundException {
		super(playerData);
	}
	@Override
	public void handle(MovementData movementData, long timestamp) {
		
		
		if (Util.isTeleporting(movementData) || Util.isNearGround(movementData.from)
				|| Util.isNearGround(movementData.to) || Util.isNearClimbable(player) || Util.isNearWater(player)) {
			lastGroundY = movementData.to.getY();
			return;
		}
		 

        if (movementData.to.getY() <= movementData.from.getY())
            return;

        double difference = movementData.to.getY() - lastGroundY;

        AtomicReference<Double> limit = new AtomicReference<>(LIMIT);

        Optional.ofNullable(player.getVelocity())
                .map(Vector::getY)
                .ifPresent(velocity -> limit.updateAndGet(v -> v + Math.abs(velocity * 4)));

        int jumpAmplifier = PlayerUtils.getPotionEffectLevel(player, PotionEffectType.JUMP);

        if (jumpAmplifier > 0)
            limit.updateAndGet(v -> v + Math.pow(jumpAmplifier + 4.2, 2D) / 16D);

        if (difference > limit.get()) {
        	flag(String.format("%s, %s", Math.ceil(difference - 1.1), limit.get()));
        }
	}
	
}