package ru.craftlogic.bees.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.craftlogic.bees.common.tileentity.TileEntityApiary;

public class SlotBeeQueen extends Slot {
    public SlotBeeQueen(TileEntityApiary apiary, int slotId, int x, int y) {
        super(apiary, slotId, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }
}
