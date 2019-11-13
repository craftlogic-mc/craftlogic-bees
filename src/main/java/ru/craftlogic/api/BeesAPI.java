package ru.craftlogic.api;

import net.minecraftforge.fml.relauncher.Side;
import ru.craftlogic.api.network.AdvancedNetwork;

public class BeesAPI {
    public static final String MOD_ID = CraftAPI.MOD_ID + "-bees";
    public static final String VERSION = "0.2.0-BETA";
    public static final AdvancedNetwork NETWORK = new AdvancedNetwork(MOD_ID);

    private static boolean init, postInit;

    public static void init(Side side) {
        if (init) {
            throw new IllegalStateException("API has already been initialized!");
        }
        init = true;

        NETWORK.openChannel();
        BeesMaterials.init(side);
        BeesBlocks.init(side);
        BeesSounds.init(side);
        BeesItems.init(side);
    }

    public static void postInit(Side side) {
        if (postInit) {
            throw new IllegalStateException("API has already been post-initialized!");
        }
        postInit = true;
    }
}
