package ru.craftlogic.bees.common.tileentity;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.craftlogic.api.CraftAPI;
import ru.craftlogic.api.block.Updatable;
import ru.craftlogic.api.block.holders.ScreenHolder;
import ru.craftlogic.api.inventory.InventoryHolder;
import ru.craftlogic.api.inventory.manager.InventoryManager;
import ru.craftlogic.api.inventory.manager.ListInventoryManager;
import ru.craftlogic.api.tile.TileEntityBase;
import ru.craftlogic.bees.client.particle.ParticleBeeRoundTrip;
import ru.craftlogic.bees.client.screen.ScreenApiary;
import ru.craftlogic.bees.common.block.BlockApiary;
import ru.craftlogic.bees.common.inventory.ContainerApiary;
import ru.craftlogic.bees.common.item.ItemHoneycomb;
import ru.craftlogic.bees.common.item.ItemScoop;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Vector;

public class TileEntityApiary extends TileEntityBase implements Updatable, InventoryHolder, ScreenHolder {
    private final NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);
    private int bees = 0;
    private int work = 0;
    private int range = 5;
    private Vector<BlockPos> flowers = new Vector<>();

    public TileEntityApiary(World world, IBlockState state) {
        super(world, state);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (ticksExisted % 200 == 0) {
                findFlowers();
            }
            if (bees > 0 && !flowers.isEmpty()) {
                work += bees;
                if (work >= 10_000) {
                    work = 0;
                    for (int i = 0; i < 7; i++) {
                        ItemStack comb = items.get(i);
                        if (!comb.isEmpty() && comb.getItem() instanceof ItemHoneycomb) {
                            NBTTagCompound compound = comb.getTagCompound();
                            if (compound == null) {
                                compound = new NBTTagCompound();
                                comb.setTagCompound(compound);
                            }
                            if (!compound.hasKey("slots", 11)) {
                                compound.setIntArray("slots", new int[7]);
                            }
                            int[] slots = compound.getIntArray("slots");
                            Vector<Integer> free = new Vector<>();
                            for (int j = 0; j < slots.length; j++) {
                                int slot = slots[j];
                                if (slot == 0) {
                                    free.add(j);
                                }
                            }
                            if (free.isEmpty()) {
                                if (world.rand.nextFloat() <= 0.05F) {
                                    int j = world.rand.nextInt(7);
                                    slots[j] = 2;
                                    break;
                                }
                            } else {
                                int j = free.get(world.rand.nextInt(free.size()));
                                slots[j] = 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void findFlowers() {
        flowers.clear();
        BlockPos start = pos.add(-range, -range, -range);
        BlockPos end = pos.add(range, range, range);

        for (BlockPos.MutableBlockPos p : BlockPos.getAllInBoxMutable(start, end)) {
            if (isFlower(world.getBlockState(p).getActualState(world, p))) {
                flowers.add(p.toImmutable());
            }
        }
    }

    private boolean shouldRenderParticle(int particleSetting, Random random) {
        if (particleSetting == 2) { // minimal
            return random.nextInt(10) == 0;
        } else if (particleSetting == 1) { // decreased
            return random.nextInt(3) != 0;
        } else { // all
            return true;
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot >= 0 && slot < 7) {
            return stack.getItem() instanceof ItemHoneycomb;
        }
        if (slot == 7){
            return stack.getItem() == Items.SUGAR;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(Random random) {
        Minecraft mc = Minecraft.getMinecraft();
        int particleSetting = mc.gameSettings.particleSetting;
        if (!getState().getValue(BlockApiary.CLOSED) && shouldRenderParticle(particleSetting, random)) {
            ParticleManager particleManager = mc.effectRenderer;

            BlockPos start = pos.add(-range, -range, -range);
            BlockPos end = pos.add(range, range, range);

            Vec3d entrance;
            switch (getState().getValue(BlockApiary.FACING)) {
                default:
                case NORTH:
                    entrance = new Vec3d(pos).add(8.5 / 16.0, 5.5 / 16.0, 2.0 / 16.0);
                    break;
                case SOUTH:
                    entrance = new Vec3d(pos).add(8.5 / 16.0, 5.5 / 16.0, 1 - 2.0 / 16.0);
                    break;
                case WEST:
                    entrance = new Vec3d(pos).add(2.0 / 16.0, 5.5 / 16.0, 8.5 / 16.0);
                    break;
                case EAST:
                    entrance = new Vec3d(pos).add(1 - 2.0 / 16.0, 5.5 / 16.0, 8.5 / 16.0);
                    break;
            }

            for (BlockPos.MutableBlockPos p : BlockPos.getAllInBoxMutable(start, end)) {
                if (random.nextFloat() <= ((float) bees / 25) && isFlower(world.getBlockState(p).getActualState(world, p))) {
                    particleManager.addEffect(new ParticleBeeRoundTrip(world, entrance, p.toImmutable(), 0xFFFFFF));
                }
            }
        }
    }

    public boolean isFlower(IBlockState state) {
        if (state.getBlock() instanceof BlockFlower) {
            return true;
        }
        if (state.getBlock() instanceof BlockDoublePlant) {
            BlockDoublePlant.EnumPlantType type = state.getValue(BlockDoublePlant.VARIANT);
            return type != BlockDoublePlant.EnumPlantType.FERN && type != BlockDoublePlant.EnumPlantType.GRASS;
        }
        if (state.getBlock() instanceof BlockFlowerPot) {
            BlockFlowerPot.EnumFlowerType type = state.getValue(BlockFlowerPot.CONTENTS);
            switch (type) {
                case POPPY:
                case BLUE_ORCHID:
                case ALLIUM:
                case HOUSTONIA:
                case RED_TULIP:
                case ORANGE_TULIP:
                case WHITE_TULIP:
                case PINK_TULIP:
                case OXEYE_DAISY:
                case DANDELION:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, RayTraceResult target) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            if (heldItem.getItem() instanceof ItemScoop) {
                int bees = ItemScoop.getBees(heldItem);
                if (bees > 0) {
                    this.bees += bees;
                    ItemScoop.setBees(heldItem, 0);
                    markDirty();
                    markForUpdate();
                }
            } else {
                CraftAPI.showScreen(this, player);
            }
        }
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        ItemStackHelper.loadAllItems(compound, items);
        bees = compound.getInteger("bees");
        work = compound.getInteger("work");
        range = compound.getInteger("range");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, items);
        compound.setInteger("bees", bees);
        compound.setInteger("work", work);
        compound.setInteger("range", range);
        return compound;
    }

    @Nonnull
    @Override
    protected NBTTagCompound writeToPacket(NBTTagCompound compound) {
        compound = super.writeToPacket(compound);
        compound.setInteger("bees", bees);
        compound.setInteger("work", work);
        compound.setInteger("range", range);
        return compound;
    }

    @Override
    protected void readFromPacket(NBTTagCompound compound) {
        super.readFromPacket(compound);
        bees = compound.getInteger("bees");
        work = compound.getInteger("work");
        range = compound.getInteger("range");
    }

    /*public static void addBeeHiveFX(IBeeHousing housing, IBeeGenome genome, List<BlockPos> flowerPositions) {
        World world = housing.getWorldObj();
        if (!shouldSpawnParticle(world)) {
            return;
        }

        ParticleManager effectRenderer = Minecraft.getMinecraft().effectRenderer;

        Vec3d particleStart = housing.getBeeFXCoordinates();

        // Avoid rendering bee particles that are too far away, they're very small.
        // At 32+ distance, have no bee particles. Make more particles up close.
        BlockPos playerPosition = Minecraft.getMinecraft().player.getPosition();
        double playerDistanceSq = playerPosition.distanceSqToCenter(particleStart.x, particleStart.y, particleStart.z);
        if (world.rand.nextInt(1024) < playerDistanceSq) {
            return;
        }

        int color = genome.getPrimary().getSpriteColour(0);

        int randomInt = world.rand.nextInt(100);

        if (housing instanceof IHiveTile) {
            if (((IHiveTile) housing).isAngry() || randomInt >= 85) {
                List<EntityLivingBase> entitiesInRange = AlleleEffect.getEntitiesInRange(genome, housing, EntityLivingBase.class);
                if (!entitiesInRange.isEmpty()) {
                    EntityLivingBase entity = entitiesInRange.get(world.rand.nextInt(entitiesInRange.size()));
                    Particle particle = new ParticleBeeTargetEntity(world, particleStart, entity, color);
                    effectRenderer.addEffect(particle);
                    return;
                }
            }
        }

        if (randomInt < 75 && !flowerPositions.isEmpty()) {
            BlockPos destination = flowerPositions.get(world.rand.nextInt(flowerPositions.size()));
            Particle particle = new ParticleBeeRoundTrip(world, particleStart, destination, color);
            effectRenderer.addEffect(particle);
        } else {
            Vec3i area = AlleleEffect.getModifiedArea(genome, housing);
            Vec3i offset = housing.getCoordinates().add(-area.getX() / 2, -area.getY() / 4, -area.getZ() / 2);
            BlockPos destination = VectUtil.getRandomPositionInArea(world.rand, area).add(offset);
            Particle particle = new ParticleBeeExplore(world, particleStart, destination, color);
            effectRenderer.addEffect(particle);
        }
    }*/

    @Override
    public InventoryManager getInventoryManager() {
        return new ListInventoryManager(items);
    }

    @Override
    public ContainerApiary createContainer(EntityPlayer player, int subId) {
        return new ContainerApiary(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer createScreen(EntityPlayer player, int subId) {
        return new ScreenApiary(player.inventory, this, createContainer(player, subId));
    }
}
