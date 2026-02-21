package com.tanya.inventoryfilter.container;

import com.tanya.inventoryfilter.registry.ModRegistry;
import com.tanya.inventoryfilter.util.FilterHelper;
import com.tanya.inventoryfilter.item.FilterItem;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FilterContainer extends AbstractContainerMenu {

    private final Container filterInventory = new SimpleContainer(9);
    private final Player player;

    public FilterContainer(int id, Inventory playerInventory, Player player) {
        super(ModRegistry.FILTER_CONTAINER.get(), id);
        this.player = player;

        // Загружаем сохраненные предметы
        ItemStack filterStack = findFilterStack(player);
        if (!filterStack.isEmpty()) {
            List<ItemStack> savedItems = FilterHelper.getFilterItems(filterStack);
            for (int i = 0; i < savedItems.size() && i < 9; i++) {
                if (!savedItems.get(i).isEmpty()) {
                    ItemStack displayStack = savedItems.get(i).copy();
                    displayStack.setCount(1);
                    filterInventory.setItem(i, displayStack);
                }
            }
        }

        // Слоты фильтра (9 штук) — x = 8 + i*18, y = 18
        for (int i = 0; i < 9; i++) {
            final int slotIndex = i;
            this.addSlot(new Slot(filterInventory, i, 8 + i * 18, 18) {

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }

                @Override
                public boolean mayPickup(Player player) {
                    return false;
                }

                @Override
                public boolean isActive() {
                    return true;
                }

                @Override
                public void onTake(Player player, ItemStack stack) {
                }

                @Override
                public ItemStack safeTake(int p_150404_, int p_150405_, Player p_150406_) {
                    return ItemStack.EMPTY;
                }

                @Override
                public ItemStack safeInsert(ItemStack stack, int amount) {
                    return stack;
                }

                @Override
                public boolean allowModification(Player player) {
                    return false;
                }
            });
        }

        // Инвентарь игрока — x = 8 + col*18, y = 51 + row*18
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, 51 + row * 18));
            }
        }

        // Хотбар — x = 8 + col*18, y = 109
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 109));
        }
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId >= 0 && slotId < 9) {
            Slot slot = this.getSlot(slotId);
            ItemStack cursorStack = this.getCarried();

            if (cursorStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                ItemStack newStack = cursorStack.copy();
                newStack.setCount(1);
                slot.set(newStack);
            }

            saveFilterToNBT();
            return;
        }

        super.clicked(slotId, button, clickType, player);
    }

    private void saveFilterToNBT() {
        ItemStack filterStack = findFilterStack(player);
        if (!filterStack.isEmpty()) {
            FilterHelper.saveFilterFromContainer(filterStack, filterInventory);
        }
    }

    private ItemStack findFilterStack(Player player) {
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() instanceof FilterItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        saveFilterToNBT();
    }

    public Container getFilterInventory() {
        return filterInventory;
    }
}