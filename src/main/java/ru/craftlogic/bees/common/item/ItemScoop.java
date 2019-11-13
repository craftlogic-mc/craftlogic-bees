package ru.craftlogic.bees.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.craftlogic.api.item.ItemBase;
import ru.craftlogic.bees.CraftBees;

import javax.annotation.Nullable;
import java.util.List;

public class ItemScoop extends ItemBase {
    public ItemScoop() {
        super("scoop", CraftBees.TAB);
        setHarvestLevel("scoop", 1);
        setMaxStackSize(1);
    }

    public static int getMaxBees(ItemStack stack) {
        return 5;
    }

    public static int getBees(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null && compound.hasKey("bees")) {
            return compound.getInteger("bees");
        }
        return 0;
    }

    public static void setBees(ItemStack stack, int bees) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            compound = new NBTTagCompound();
            stack.setTagCompound(compound);
        }
        compound.setInteger("bees", bees);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getBees(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0 - (double) getBees(stack) / getMaxBees(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> lines, ITooltipFlag flag) {
        lines.add(String.format("Bees: %d/%d", getBees(stack) * 100, getMaxBees(stack) * 100));
    }
}
