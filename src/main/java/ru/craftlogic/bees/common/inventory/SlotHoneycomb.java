package ru.craftlogic.bees.common.inventory;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.craftlogic.api.BeesAPI;
import ru.craftlogic.bees.common.item.ItemHoneycomb;
import ru.craftlogic.bees.common.tileentity.TileEntityApiary;

public class SlotHoneycomb extends Slot {
    public SlotHoneycomb(TileEntityApiary apiary, int slotId, int x, int y) {
        super(apiary, slotId, x, y);
        setBackgroundName(BeesAPI.MOD_ID + ":items/slots/honeycomb");
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemHoneycomb;
    }
}
