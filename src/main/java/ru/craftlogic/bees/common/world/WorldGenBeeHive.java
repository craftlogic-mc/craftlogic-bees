package ru.craftlogic.bees.common.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import ru.craftlogic.api.BeesBlocks;

import java.util.Random;

public class WorldGenBeeHive extends WorldGenerator {
    private boolean isTreeBlock(IBlockState state, World world, BlockPos pos) {
        return state.getBlock().isLeaves(state, world, pos) || state.getBlock().isWood(world, pos);
    }

    private boolean isReplaceable(IBlockState state, World world, BlockPos pos) {
        return state.getBlock().isReplaceable(world, pos) && !state.getMaterial().isLiquid();
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        if (pos.getY() > 0 && world.getBiome(pos).getTempCategory() != Biome.TempCategory.COLD) {
            IBlockState state = world.getBlockState(pos);
            if (isTreeBlock(state, world, pos)) {
                final BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos(pos);
                do {
                    p.move(EnumFacing.DOWN);
                    state = world.getBlockState(p);
                } while (isTreeBlock(state, world, p));

                if (isReplaceable(state, world, p)) {
                    System.out.println("Generated bee hive at " + p);
                    world.setBlockState(p.toImmutable(), BeesBlocks.BEE_HIVE.getDefaultState(), 2);
                }
            }
        }
        return false;
    }
}
