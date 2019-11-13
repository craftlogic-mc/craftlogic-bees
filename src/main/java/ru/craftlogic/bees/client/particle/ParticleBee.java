package ru.craftlogic.bees.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class ParticleBee extends Particle {
    protected ParticleBee(World world, double x, double y, double z, double velX, double velY, double velZ) {
        super(world, x, y, z, velX, velY, velZ);
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float minU = 0;
        float maxU = 1;
        float minV = 0;
        float maxV = 1;

        if (this.particleTexture != null) {
            minU = particleTexture.getMinU();
            maxU = particleTexture.getMaxU();
            minV = particleTexture.getMinV();
            maxV = particleTexture.getMaxV();
        }

        float sp = 0.1F * particleScale;
        float x = (float) (prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float y = (float) (prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float z = (float) (prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);

        int light = this.getBrightnessForRender(partialTicks);
        int sky = light >> 16 & 65535;
        int block = light & 65535;

        buffer.pos(x - rotationX * sp - rotationXY * sp, y - rotationZ * sp, z - rotationYZ * sp - rotationXZ * sp).tex(maxU, maxV).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(sky, block).endVertex();
        buffer.pos(x - rotationX * sp + rotationXY * sp, y + rotationZ * sp, z - rotationYZ * sp + rotationXZ * sp).tex(maxU, minV).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(sky, block).endVertex();
        buffer.pos(x + rotationX * sp + rotationXY * sp, y + rotationZ * sp, z + rotationYZ * sp + rotationXZ * sp).tex(minU, minV).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(sky, block).endVertex();
        buffer.pos(x + rotationX * sp - rotationXY * sp, y - rotationZ * sp, z + rotationYZ * sp - rotationXZ * sp).tex(minU, maxV).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(sky, block).endVertex();
    }

    @Override
    public int getBrightnessForRender(float partialTicks) {
        return 0xF000F0;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}
