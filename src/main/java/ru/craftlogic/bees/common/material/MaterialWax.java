package ru.craftlogic.bees.common.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialWax extends Material {
    public MaterialWax() {
        super(MapColor.YELLOW);
        setRequiresTool();
        setNoPushMobility();
    }
}
