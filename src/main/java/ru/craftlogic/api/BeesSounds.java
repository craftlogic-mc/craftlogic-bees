package ru.craftlogic.api;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;

import static ru.craftlogic.api.CraftSounds.registerSound;

public class BeesSounds {
    public static SoundEvent BUZZ;

    static void init(Side side) {
        BUZZ = registerSound("buzz");
    }
}
