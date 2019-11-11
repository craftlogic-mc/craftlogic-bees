package ru.craftlogic.bees.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import ru.craftlogic.api.block.Updatable;
import ru.craftlogic.api.inventory.InventoryHolder;
import ru.craftlogic.api.inventory.manager.InventoryManager;
import ru.craftlogic.api.inventory.manager.ListInventoryManager;
import ru.craftlogic.api.tile.TileEntityBase;

public class TileEntityApiary extends TileEntityBase implements Updatable, InventoryHolder {
    private final NonNullList<ItemStack> frames = NonNullList.withSize(3, ItemStack.EMPTY);
    private BeeFamily family = null;

    public TileEntityApiary(World world, IBlockState state) {
        super(world, state);
    }

    @Override
    public void update() {

    }

    @Override
    public InventoryManager getInventoryManager() {
        return new ListInventoryManager(frames);
    }

    public static class BeeFamily {

    }
}
