package ru.craftlogic.bees.client.screen;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import ru.craftlogic.api.screen.ScreenWithInventory;
import ru.craftlogic.bees.common.inventory.ContainerApiary;
import ru.craftlogic.bees.common.tileentity.TileEntityApiary;

public class ScreenApiary extends ScreenWithInventory<ContainerApiary> {
    private InventoryPlayer playerInv;
    private final TileEntityApiary apiary;

    public ScreenApiary(InventoryPlayer playerInv, TileEntityApiary apiary, ContainerApiary container) {
        super(container);
        this.playerInv = playerInv;
        this.apiary = apiary;
    }

    @Override
    protected void init() {
        int x = getLocalX();
        int y = getLocalY();
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float deltaTime) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.bindTexture(BLANK_TEXTURE);
        this.drawTexturedRect(getLocalX(), getLocalY(), getWidth(), getHeight(), 0, 0);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float deltaTime) {
        this.drawCenteredText(this.apiary.getDisplayName(), getWidth() / 2, 9, 0x404040);
        this.drawText(this.playerInv.getDisplayName(), 8, getHeight() - 94, 0x404040);
        this.drawText(String.format("Bees: %d/%d", this.container.getBees() * 100, this.container.getMaxBees() * 100), 9, 9, 0x404040);
    }
}
