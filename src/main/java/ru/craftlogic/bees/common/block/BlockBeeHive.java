package ru.craftlogic.bees.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.craftlogic.api.BeesAPI;
import ru.craftlogic.api.BeesItems;
import ru.craftlogic.api.BeesMaterials;
import ru.craftlogic.api.block.BlockNarrow;
import ru.craftlogic.api.model.ModelManager;
import ru.craftlogic.api.model.ModelRegistrar;
import ru.craftlogic.api.world.Location;
import ru.craftlogic.bees.CraftBees;
import ru.craftlogic.bees.common.item.ItemHoneycomb;
import ru.craftlogic.bees.common.item.ItemScoop;

import javax.annotation.Nullable;

public class BlockBeeHive extends BlockNarrow implements ModelRegistrar {
    public static PropertyBool INHABITED = PropertyBool.create("inhabited");

    public BlockBeeHive() {
        super(BeesMaterials.WAX, "bee_hive", 1.5F, CraftBees.TAB);
        setSoundType(SoundType.PLANT);
        setHarvestLevel("scoop", 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(INHABITED, (meta & 8) > 0);
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
            new ModelResourceLocation(BeesAPI.MOD_ID + ":bee_hive", "normal")
        );
    }

    @Override
    protected IProperty[] getProperties() {
        return new IProperty[] {INHABITED};
    }

    @Override
    protected void harvestBlock(Location location, IBlockState state, EntityPlayer player, @Nullable TileEntity tile, ItemStack item) {
        super.harvestBlock(location, state, player, tile, item);
        ItemStack heldItem = player.getHeldItemMainhand();
        if (heldItem.getItem() instanceof ItemScoop) {
            int bees = ItemScoop.getBees(heldItem);
            int maxBees = ItemScoop.getMaxBees(heldItem);
            if (bees < maxBees) {
                World world = location.getWorld();
                int b = Math.min(1 + world.rand.nextInt(3), maxBees - bees);
                ItemScoop.setBees(heldItem, bees + b);
            }
        }
    }

    @Override
    public void addDrops(Location location, NonNullList<ItemStack> items, int fortune) {
        World world = location.getWorld();
        for (int i = 0; i < 1/* + world.rand.nextInt(2)*/; i++) {
            ItemStack comb = new ItemStack(BeesItems.HONEYCOMB);
            comb.setTagCompound(ItemHoneycomb.setSlots(new NBTTagCompound(), slot -> world.rand.nextInt(2)));
            items.add(comb);
        }
    }
}
