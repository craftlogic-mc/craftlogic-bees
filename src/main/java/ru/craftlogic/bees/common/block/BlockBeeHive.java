package ru.craftlogic.bees.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.craftlogic.api.block.BlockNarrow;
import ru.craftlogic.api.model.ModelManager;
import ru.craftlogic.api.model.ModelRegistrar;

public class BlockBeeHive extends BlockNarrow implements ModelRegistrar {
    public static PropertyBool INHABITED = PropertyBool.create("inhabited");

    public BlockBeeHive() {
        super(Material.GRASS, "bee_hive", 1.5F, CreativeTabs.DECORATIONS);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(INHABITED, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(INHABITED) ? 8 : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel(ModelManager modelManager) {
        super.registerModel(modelManager);
        modelManager.registerStateMapper(this, (state, mapper) ->
            new ModelResourceLocation("craftlogic:bee_hive", "normal")
        );
    }

    @Override
    protected IProperty[] getProperties() {
        return new IProperty[] {INHABITED};
    }
}
