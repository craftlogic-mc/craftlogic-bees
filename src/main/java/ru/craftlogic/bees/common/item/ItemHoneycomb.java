package ru.craftlogic.bees.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.craftlogic.api.block.Colored;
import ru.craftlogic.api.item.ItemBase;
import ru.craftlogic.api.model.ModelManager;
import ru.craftlogic.bees.CraftBees;
import ru.craftlogic.bees.client.model.ModelHoneycomb;

import java.util.function.IntUnaryOperator;

public class ItemHoneycomb extends ItemBase implements Colored {
    public ItemHoneycomb() {
        super("honeycomb", CraftBees.TAB);
        this.setMaxStackSize(1);
    }

    @Override
    public int getItemColor(ItemStack stack, int tint) {
        if (tint > 0) {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound != null && compound.hasKey("slots", 11)) {
                int[] slots = compound.getIntArray("slots");
                int type = slots[tint - 1];
                if (type == 1) { //honey
                    return 0xFBBF50;
                }
            }
        }
        return 0xFFFFFF;
    }

    public int getMaxSlots(ItemStack stack) {
        return 7;
    }

    public int getSlotsOccupied(ItemStack stack) {
        int occupied = 0;
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null && compound.hasKey("slots", 11)) {
            int[] slots = compound.getIntArray("slots");
            for (int slot : slots) {
                if (slot != 0) {
                    occupied += 1;
                }
            }
        }
        return occupied;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel(ModelManager modelManager) {
        ModelLoaderRegistry.registerLoader(ModelHoneycomb.Loader.INSTANCE);
        modelManager.registerItemModel(this, 0, ModelHoneycomb.LOCATION);
    }


    public static NBTTagCompound setSlots(NBTTagCompound compound, IntUnaryOperator setter) {
        int[] slots = compound.hasKey("slots", 11) ? compound.getIntArray("slots") : new int[7];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = setter.applyAsInt(slots[i]);
        }
        compound.setIntArray("slots", slots);
        return compound;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            ItemStack empty = new ItemStack(this);
            empty.setTagCompound(setSlots(new NBTTagCompound(), slot -> 0));
            ItemStack honey = new ItemStack(this);
            honey.setTagCompound(setSlots(new NBTTagCompound(), slot -> 1));
            ItemStack larvae = new ItemStack(this);
            larvae.setTagCompound(setSlots(new NBTTagCompound(), slot -> 2));
            items.add(empty);
            items.add(honey);
            items.add(larvae);
        }
    }
}
