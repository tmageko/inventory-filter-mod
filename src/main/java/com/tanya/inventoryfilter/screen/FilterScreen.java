package com.tanya.inventoryfilter.screen;

import com.tanya.inventoryfilter.InventoryFilterMod;
import com.tanya.inventoryfilter.container.FilterContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FilterScreen extends AbstractContainerScreen<FilterContainer> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(InventoryFilterMod.MODID, "textures/gui/filter.png");

    public FilterScreen(FilterContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);

        // Размер точно по координатам слотов в FilterContainer:
        // 9 слотов * 18px + 8px отступ с каждой стороны = 176
        // хотбар y=108 + 18px высота + 8px отступ снизу = 134
        this.imageWidth = 176;
        this.imageHeight = 136;

        // Заголовок над слотами фильтра (y=18 слоты, значит 6px сверху)
        this.titleLabelX = 8;
        this.titleLabelY = 6;

        // Надпись "Инвентарь" над инвентарём (y=50 слоты, значит 40px)
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 40;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 176, 136);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}