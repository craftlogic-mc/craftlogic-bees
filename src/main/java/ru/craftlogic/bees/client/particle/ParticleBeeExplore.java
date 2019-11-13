package ru.craftlogic.bees.client.particle;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.craftlogic.bees.client.ProxyClient;

public class ParticleBeeExplore extends ParticleBee {
	private final Vec3d origin;

	public ParticleBeeExplore(World world, Vec3d origin, BlockPos destination, int color) {
		super(world, origin.x, origin.y, origin.z, 0.0, 0.0, 0.0);
		this.origin = origin;

		setParticleTexture(ProxyClient.BEE_SPRITE);

		motionX = (destination.getX() + 0.5 - posX) * 0.015;
		motionY = (destination.getY() + 0.5 - posY) * 0.015;
		motionZ = (destination.getZ() + 0.5 - posZ) * 0.015;

		particleRed = (color >> 16 & 255) / 255F;
		particleGreen = (color >> 8 & 255) / 255F;
		particleBlue = (color & 255) / 255F;

		setSize(0.1F, 0.1F);
		particleScale *= 0.2F;
		particleMaxAge = (int) (80.0 / (Math.random() * 0.8 + 0.2));

		motionX *= 0.9;
		motionY *= 0.015;
		motionZ *= 0.9;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		move(motionX, motionY, motionZ);

		if (particleAge == particleMaxAge / 2) {
			motionX = (origin.x - posX) * 0.03;
			motionY = (origin.y - posY) * 0.03;
			motionZ = (origin.z - posZ) * 0.03;
		}

		if (particleAge < particleMaxAge * 0.25) {
			// venture out
			motionX *= 0.92 + 0.3 * rand.nextFloat();
			motionY = (motionY + 0.3 * (-0.5 + rand.nextFloat())) / 2;
			motionZ *= 0.92 + 0.3 * rand.nextFloat();
		} else if (particleAge < particleMaxAge * 0.5) {
			// slow down
			motionX *= 0.75 + 0.3 * rand.nextFloat();
			motionY = (motionY + 0.3 * (-0.5 + rand.nextFloat())) / 2;
			motionZ *= 0.75 + 0.3 * rand.nextFloat();
		} else if (particleAge < particleMaxAge * 0.75) {
			// venture back
			motionX *= 0.95;
			motionY = (origin.y - posY) * 0.03;
			motionY = (motionY + 0.2 * (-0.5 + rand.nextFloat())) / 2;
			motionZ *= 0.95;
		} else {
			// get to origin
			motionX = (origin.x - posX) * 0.03;
			motionY = (origin.y - posY) * 0.03;
			motionY = (motionY + 0.2 * (-0.5 + rand.nextFloat())) / 2;
			motionZ = (origin.z - posZ) * 0.03;
		}

		if (particleAge++ >= particleMaxAge) {
			setExpired();
		}
	}
}
