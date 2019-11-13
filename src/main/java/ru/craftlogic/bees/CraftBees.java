package ru.craftlogic.bees;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.craftlogic.api.BeesAPI;
import ru.craftlogic.api.BeesBlocks;
import ru.craftlogic.api.CraftAPI;
import ru.craftlogic.api.util.CreativeTab;
import ru.craftlogic.bees.common.ProxyCommon;

import static ru.craftlogic.api.BeesAPI.MOD_ID;
import static ru.craftlogic.api.BeesAPI.VERSION;

@Mod(modid = MOD_ID, version = VERSION, dependencies = "required-after:" + CraftAPI.MOD_ID)
public class CraftBees {
    public static final CreativeTab TAB = CreativeTab.registerTab(MOD_ID, tab -> new ItemStack(BeesBlocks.BEE_HIVE));

    @SidedProxy(clientSide = "ru.craftlogic.bees.client.ProxyClient", serverSide = "ru.craftlogic.bees.common.ProxyCommon")
    public static ProxyCommon PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BeesAPI.init(event.getSide());
        MinecraftForge.EVENT_BUS.register(PROXY);
        MinecraftForge.TERRAIN_GEN_BUS.register(PROXY);
        PROXY.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        BeesAPI.postInit(event.getSide());
        PROXY.postInit(event);
    }
}
