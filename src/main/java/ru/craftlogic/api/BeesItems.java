package ru.craftlogic.api;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import ru.craftlogic.bees.common.item.ItemHoneycomb;
import ru.craftlogic.bees.common.item.ItemScoop;

import static ru.craftlogic.api.CraftItems.registerItem;

public class BeesItems {
    public static Item HONEYCOMB;
    public static Item SCOOP;

    static void init(Side side) {
        HONEYCOMB = registerItem(new ItemHoneycomb());
        SCOOP = registerItem(new ItemScoop());
    }
}
