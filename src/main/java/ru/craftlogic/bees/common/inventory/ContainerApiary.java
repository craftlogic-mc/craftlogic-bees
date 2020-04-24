package ru.craftlogic.bees.common.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import ru.craftlogic.api.inventory.ContainerBase;
import ru.craftlogic.bees.common.tileentity.TileEntityApiary;
import ru.craftlogic.bees.common.tileentity.TileEntityApiary.ApiaryField;

public class ContainerApiary extends ContainerBase<TileEntityApiary> {
    public ContainerApiary(InventoryPlayer playerInv, TileEntityApiary apiary) {
        super(apiary);
        this.addSlotToContainer(new SlotHoneycomb(apiary, 0, 70, 14));
        this.addSlotToContainer(new SlotHoneycomb(apiary, 1, 90, 14));
        this.addSlotToContainer(new SlotHoneycomb(apiary, 2, 60, 34));
        this.addSlotToContainer(new SlotHoneycomb(apiary, 3, 80, 34));
        this.addSlotToContainer(new SlotHoneycomb(apiary, 4, 100, 34));
        this.addSlotToContainer(new SlotHoneycomb(apiary, 5, 70, 54));
        this.addSlotToContainer(new SlotHoneycomb(apiary, 6, 90, 54));
        this.addSlotToContainer(new SlotLure(apiary, 7, 26, 34));
        this.addSlotToContainer(new SlotBeeQueen(apiary, 8, 134, 14));
        this.addSlotToContainer(new SlotBeeQueen(apiary, 9, 134, 34));
        this.addSlotToContainer(new SlotBeeQueen(apiary, 10, 134, 54));
        this.addPlayerSlots(playerInv, 8, 84);
    }

    public int getBees() {
        return this.inventory.getField(ApiaryField.BEES);
    }

    public int getMaxBees() {
        return this.inventory.getField(ApiaryField.MAX_BEES);
    }

    public int getWork() {
        return this.inventory.getField(ApiaryField.WORK);
    }

    public int getRange() {
        return this.inventory.getField(ApiaryField.RANGE);
    }
}
