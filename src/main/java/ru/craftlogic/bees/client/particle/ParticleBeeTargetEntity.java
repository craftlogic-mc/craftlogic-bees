package ru.craftlogic.bees.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.craftlogic.bees.client.ProxyClient;

public class ParticleBeeTargetEntity extends ParticleBee {
	private final Vec3d origin;
	private final Entity entity;

	public ParticleBeeTargetEntity(World world, Vec3d origin, Entity entity, int color) {
		super(world, origin.x, origin.y, origin.z, 0.0, 0.0, 0.0);
		this.origin = origin;
		this.entity = entity;

		setParticleTexture(ProxyClient.BEE_SPRITE);

		motionX = (entity.posX - posX) * 0.015;
		motionY = (entity.posY + 1.62F - posY) * 0.015;
		motionZ = (entity.posZ - posZ) * 0.015;

		particleRed = (color >> 16 & 255) / 255.0F;
		particleGreen = (color >> 8 & 255) / 255.0F;
		particleBlue = (color & 255) / 255.0F;

		setSize(0.1F, 0.1F);
		particleScale *= 0.2F;
		particleMaxAge = (int) (80.0 / (Math.random() * 0.8 + 0.2));

		motionX *= 0.9;
		motionY *= 0.9;
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

		if (particleAge < particleMaxAge * 0.5) {
			// fly near the entity
			motionX = (entity.posX - posX) * 0.09;
			motionX = (motionX + 0.2 * (-0.5 + rand.nextFloat())) / 2;
			motionY = (entity.posY + 1.62F - posY) * 0.03;
			motionY = (motionY + 0.4 * (-0.5 + rand.nextFloat())) / 4;
			motionZ = (entity.posZ - posZ) * 0.09;
			motionZ = (motionZ + 0.2 * (-0.5 + rand.nextFloat())) / 2;
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
