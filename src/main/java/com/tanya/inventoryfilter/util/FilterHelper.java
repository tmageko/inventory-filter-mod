package com.tanya.inventoryfilter.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper {

    private static final String FILTER_TAG = "FilterItems";
    private static final String SLOT_TAG = "Slot";

    public static List<ItemStack> getFilterItems(ItemStack filterStack) {
        List<ItemStack> items = new ArrayList<>();
        CompoundTag tag = filterStack.getOrCreateTag();

        if (tag.contains(FILTER_TAG)) {
            ListTag listTag = tag.getList(FILTER_TAG, Tag.TAG_COMPOUND); // ← используем Tag.TAG_COMPOUND
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag itemTag = listTag.getCompound(i);
                ItemStack stack = ItemStack.of(itemTag);
                if (!stack.isEmpty()) {
                    items.add(stack);
                }
            }
        }

        return items;
    }

    public static void setFilterItems(ItemStack filterStack, List<ItemStack> items) {
        ListTag listTag = new ListTag();

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                stack.save(itemTag);
                itemTag.putInt(SLOT_TAG, i);
                listTag.add(itemTag);
            }
        }

        filterStack.getOrCreateTag().put(FILTER_TAG, listTag);
    }

    public static void saveFilterFromContainer(ItemStack filterStack, net.minecraft.world.Container container) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                items.add(stack.copy());
            }
        }
        setFilterItems(filterStack, items);
    }
}