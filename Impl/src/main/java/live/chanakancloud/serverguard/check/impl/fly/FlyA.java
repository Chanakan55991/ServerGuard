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

import live.chanakancloud.serverguard.common.MovementData;
import live.chanakancloud.serverguard.player.PlayerData;
import live.chanakancloud.serverguard.util.Util;

@CheckInfo(name = "Fly (A)", type = CheckType.FLY, experimental = true)
public class FlyA extends Check {
	public FlyA(@NonNull PlayerData playerData) throws ClassNotFoundException {
		super(playerData);
	}
	
	private boolean lastOnGround, lastLastOnGround;
	private double lastDeltaY;
	private boolean isJumping;
	private int nearLiquidTicks;
	public boolean halfMovement;
	public int halfMovementHistoryCounter = 0;
	

	@Override
	public void handle(MovementData movementData, long timestamp) {
		Vector velocity = playerData.getBukkitPlayer().getVelocity();
		double jumpVelocity = (double) 0.42F;
		this.isJumping = false;
		this.halfMovement = false;
		
		if (velocity.getY() > 0) {
			PotionEffect jumpPotion = player.getPotionEffect(PotionEffectType.JUMP);
			if (jumpPotion != null) {
				jumpVelocity += (double) ((float) jumpPotion.getAmplifier() + 1) * 0.1F;
			}
			if (player.getLocation().getBlock().getType() != Material.LADDER && Double.compare(velocity.getY(), jumpVelocity) == 0) {
				this.isJumping = true;
			}
		}
		
		if (Util.couldBeOnHalfblock(movementData.to) || Util.isNearBed(movementData.to)) {
			this.halfMovement = true;
			this.halfMovementHistoryCounter = 30;
		} else {
			if (this.halfMovementHistoryCounter > 0)
				this.halfMovementHistoryCounter--;
		}
		double deltaY = movementData.to.getY() - movementData.from.getY();

		double lastDeltaY = this.lastDeltaY;

		this.lastDeltaY = deltaY;

		boolean onGround = isNearGround(movementData.to);

		boolean lastOnGround = this.lastOnGround;
		this.lastOnGround = onGround;

		boolean lastLastOnGround = this.lastLastOnGround;
		this.lastLastOnGround = lastOnGround;

		double predictedDelta = (lastDeltaY - 0.08D) * 0.9800000190734863D;
		debug(Double.toString(Double.compare(velocity.getY(), jumpVelocity)));
		//debug(Double.toString(deltaY) + " " + Double.toString(predictedDelta));
		if (!(this.isJumping || movementData.from.getBlockY() < movementData.to.getBlockY() || Math.abs(movementData.to.getY() - movementData.from.getY()) >= 400 || VersionUtil.isFlying(playerData.getBukkitPlayer()) || VersionUtil.isRiptiding(playerData.getBukkitPlayer()) || VersionUtil.isSwimming(playerData.getBukkitPlayer()) || VersionUtil.isLevitationEffect(playerData.getPotionEffect(PotionEffectType.LEVITATION)) || playerData.getBukkitPlayer().getVehicle() != null || this.halfMovement || Util.isNearClimbable(playerData.getBukkitPlayer()))) {
			if (nearLiquidTicks <= 0) {
				if (!onGround && !lastOnGround && !lastLastOnGround && Math.abs(predictedDelta) >= 0.005D) {
					if (!isRoughlyEqual(deltaY, predictedDelta)) {
						flag("DeltaY: " + deltaY + " Predicted Delta: " +  predictedDelta);
					}
				}
			}
		}
	}
	//        if (deltaY >= maxDeltaY)
	//            flag(deltaY + ">=" + maxDeltaY);

	public boolean isRoughlyEqual(double d1, double d2) {
		return Math.abs(d1 - d2) < 0.001;
	}
	public boolean isNearGround(Location location) {
		double expand = 0.3;
		for (double x = -expand; x <= expand; x += expand) {
			for (double z = -expand; z <= expand; z += expand) {
				if (location.clone().add(x, -0.5001, z).getBlock().getType() != Material.AIR) {
					return true;
				}
			}
		}
		return false;
	}

	public void nearLiquidTick(Player player, Location from, Location to) {
		if (Util.isNearWater(player))
			this.nearLiquidTicks = 8;
		else {
			if (this.nearLiquidTicks > 0)
				this.nearLiquidTicks--;
			else
				this.nearLiquidTicks = 0;
		}
	}
}

