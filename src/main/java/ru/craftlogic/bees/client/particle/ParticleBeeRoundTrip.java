package ru.craftlogic.bees.client.particle;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.craftlogic.bees.client.ProxyClient;

public class ParticleBeeRoundTrip extends ParticleBee {
	private final Vec3d origin;
	private final BlockPos destination;

	public ParticleBeeRoundTrip(World world, Vec3d origin, BlockPos destination, int color) {
		super(world, origin.x, origin.y, origin.z, 0.0, 0.0, 0.0);
		this.origin = origin;
		this.destination = destination;

		setParticleTexture(ProxyClient.BEE_SPRITE);

		motionX = (destination.getX() + 0.5 - this.posX) * 0.02 + 0.1 * rand.nextFloat();
		motionY = (destination.getY() + 0.5 - this.posY) * 0.015 + 0.1 * rand.nextFloat();
		motionZ = (destination.getZ() + 0.5 - this.posZ) * 0.02 + 0.1 * rand.nextFloat();

		particleRed = (color >> 16 & 255) / 255.0F;
		particleGreen = (color >> 8 & 255) / 255.0F;
		particleBlue = (color & 255) / 255.0F;

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
			motionX = (origin.x - posX) * 0.03 + 0.1 * rand.nextFloat();
			motionY = (origin.y - posY) * 0.03 + 0.1 * rand.nextFloat();
			motionZ = (origin.z - posZ) * 0.03 + 0.1 * rand.nextFloat();
		}
		if (particleAge < particleMaxAge * 0.25) {
			// venture out
			motionX *= 0.92 + 0.2 * rand.nextFloat();
			motionY = (motionY + 0.3 * (-0.5 + rand.nextFloat())) / 2;
			motionZ *= 0.92 + 0.2 * rand.nextFloat();
		} else if (particleAge < particleMaxAge * 0.5) {
			// get to flower destination
			motionX = (destination.getX() + 0.5 - posX) * 0.03;
			motionY = (destination.getY() + 0.5 - posY) * 0.1;
			motionY = (motionY + 0.2 * (-0.5 + rand.nextFloat())) / 2;
			motionZ = (destination.getZ() + 0.5 - posZ) * 0.03;
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
