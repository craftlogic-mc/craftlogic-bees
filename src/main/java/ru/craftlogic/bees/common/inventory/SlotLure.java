package ru.craftlogic.bees.common.inventory;

import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.craftlogic.api.BeesAPI;
import ru.craftlogic.bees.common.tileentity.TileEntityApiary;

public class SlotLure extends Slot {
    public SlotLure(TileEntityApiary apiary, int slotId, int x, int y) {
        super(apiary, slotId, x, y);
        setBackgroundName(BeesAPI.MOD_ID + ":items/slots/lure");
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() == Items.SUGAR;
    }
}
