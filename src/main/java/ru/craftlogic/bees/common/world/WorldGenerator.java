package ru.craftlogic.bees.common.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenerator implements IWorldGenerator {
    public static final WorldGenBeeHive HIVE_GENERATOR = new WorldGenBeeHive();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator generator, IChunkProvider provider) {

    }
}
