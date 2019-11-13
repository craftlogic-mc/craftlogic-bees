package ru.craftlogic.bees.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.craftlogic.api.BeesAPI;
import ru.craftlogic.bees.common.ProxyCommon;

public class ProxyClient extends ProxyCommon {
    public static TextureAtlasSprite BEE_SPRITE;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        BEE_SPRITE = event.getMap().registerSprite(new ResourceLocation(BeesAPI.MOD_ID, "particles/bee"));
        event.getMap().registerSprite(new ResourceLocation(BeesAPI.MOD_ID, "items/slots/honeycomb"));
        event.getMap().registerSprite(new ResourceLocation(BeesAPI.MOD_ID, "items/slots/lure"));
    }
}
