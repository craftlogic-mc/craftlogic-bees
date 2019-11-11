package ru.craftlogic.bees.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.craftlogic.bees.common.block.BlockApiary;
import ru.craftlogic.bees.common.block.BlockBeeHive;
import ru.craftlogic.bees.common.world.WorldGenerator;
import ru.craftlogic.common.block.BlockPlanks2;

import java.util.Random;

import static ru.craftlogic.api.CraftBlocks.registerBlockWithItem;

public class ProxyCommon {
    public static Block BEE_HIVE;
    public static BiMap<BlockPlanks.EnumType, Block> APIARY = HashBiMap.create(BlockPlanks.EnumType.values().length);
    public static BiMap<BlockPlanks2.PlanksType2, Block> APIARY2 = HashBiMap.create(BlockPlanks2.PlanksType2.values().length);

    public void preInit(FMLPreInitializationEvent event) {
        BEE_HIVE = registerBlockWithItem(new BlockBeeHive());

        for (BlockPlanks.EnumType plankType : BlockPlanks.EnumType.values()) {
            APIARY.put(plankType, registerBlockWithItem(new BlockApiary(plankType.getName())));
        }

        for (BlockPlanks2.PlanksType2 plankType : BlockPlanks2.PlanksType2.values()) {
            APIARY2.put(plankType, registerBlockWithItem(new BlockApiary(plankType.getName())));
        }

        GameRegistry.registerWorldGenerator(new WorldGenerator(), 0);
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}

    @SubscribeEvent
    protected void onChunkPopulate(PopulateChunkEvent.Post event) {
        World world = event.getWorld();
        Random rand = event.getRand();
        if (rand.nextFloat() <= 0.03) {
            int x = (event.getChunkX() << 4) + rand.nextInt(8);
            int z = (event.getChunkZ() << 4) + rand.nextInt(8);
            int y = world.getHeight(x, z);
            WorldGenerator.HIVE_GENERATOR.generate(world, rand, new BlockPos(x, y, z));
        }
    }
}
