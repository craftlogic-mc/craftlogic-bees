package ru.craftlogic.bees.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.craftlogic.bees.common.block.BlockBeeHive;
import ru.craftlogic.bees.common.block.BlockBeeHouse;

import static ru.craftlogic.api.CraftBlocks.registerBlockWithItem;

public class ProxyCommon {
    public static Block BEE_HIVE;
    public static BiMap<BlockPlanks.EnumType, Block> BEE_HOUSE = HashBiMap.create(BlockPlanks.EnumType.values().length);

    public void preInit(FMLPreInitializationEvent event) {
        BEE_HIVE = registerBlockWithItem(new BlockBeeHive());

        for (BlockPlanks.EnumType plankType : BlockPlanks.EnumType.values()) {
            BEE_HOUSE.put(plankType, registerBlockWithItem(new BlockBeeHouse(plankType)));
        }
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}
}
