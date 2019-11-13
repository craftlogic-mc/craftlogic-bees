package ru.craftlogic.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraftforge.fml.relauncher.Side;
import ru.craftlogic.bees.common.block.BlockApiary;
import ru.craftlogic.bees.common.block.BlockBeeHive;
import ru.craftlogic.common.block.BlockPlanks2;

import static ru.craftlogic.api.CraftBlocks.registerBlockWithItem;

public class BeesBlocks {
    public static Block BEE_HIVE;
    public static BiMap<BlockPlanks.EnumType, Block> APIARY = HashBiMap.create(BlockPlanks.EnumType.values().length);
    public static BiMap<BlockPlanks2.PlanksType2, Block> APIARY2 = HashBiMap.create(BlockPlanks2.PlanksType2.values().length);

    static void init(Side side) {
        BEE_HIVE = registerBlockWithItem(new BlockBeeHive());

        for (BlockPlanks.EnumType plankType : BlockPlanks.EnumType.values()) {
            APIARY.put(plankType, registerBlockWithItem(new BlockApiary(plankType.getName())));
        }

        for (BlockPlanks2.PlanksType2 plankType : BlockPlanks2.PlanksType2.values()) {
            APIARY2.put(plankType, registerBlockWithItem(new BlockApiary(plankType.getName())));
        }
    }
}
