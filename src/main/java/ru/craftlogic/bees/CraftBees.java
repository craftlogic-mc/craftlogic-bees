package ru.craftlogic.bees;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.craftlogic.api.CraftAPI;
import ru.craftlogic.api.network.AdvancedNetwork;
import ru.craftlogic.api.util.CreativeTab;
import ru.craftlogic.bees.common.ProxyCommon;

@Mod(modid = CraftBees.MOD_ID, version = CraftBees.VERSION, dependencies = "required-after:" + CraftAPI.MOD_ID)
public class CraftBees {
    public static final String MOD_ID = CraftAPI.MOD_ID + "-bees";
    public static final String VERSION = "0.2.0-BETA";

    public static final CreativeTab TAB = CreativeTab.registerTab(MOD_ID, tab -> new ItemStack(ProxyCommon.BEE_HIVE));

    @SidedProxy(clientSide = "ru.craftlogic.bees.client.ProxyClient", serverSide = "ru.craftlogic.bees.common.ProxyCommon")
    public static ProxyCommon PROXY;
    public static final AdvancedNetwork NETWORK = new AdvancedNetwork(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PROXY);
        MinecraftForge.TERRAIN_GEN_BUS.register(PROXY);
        PROXY.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NETWORK.openChannel();
        PROXY.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit(event);
    }
}
