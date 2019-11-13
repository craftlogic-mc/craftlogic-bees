package ru.craftlogic.bees.common;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.craftlogic.bees.common.world.WorldGenerator;

import java.util.Random;

public class ProxyCommon {
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerWorldGenerator(new WorldGenerator(), 0);
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}

    @SubscribeEvent
    public void onChunkPopulate(PopulateChunkEvent.Post event) {
        World world = event.getWorld();
        Random rand = event.getRand();
        if (rand.nextFloat() <= 0.03) {
            int x = (event.getChunkX() << 4) + rand.nextInt(8);
            int z = (event.getChunkZ() << 4) + rand.nextInt(8);
            int y = world.getHeight(x, z);
            WorldGenerator.HIVE_GENERATOR.generate(world, rand, new BlockPos(x, y, z).down());
        }
    }
}
