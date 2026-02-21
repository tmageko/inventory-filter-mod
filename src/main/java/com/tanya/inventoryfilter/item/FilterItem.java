package com.tanya.inventoryfilter.item;

import com.tanya.inventoryfilter.container.FilterContainer;
import com.tanya.inventoryfilter.util.FilterHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FilterItem extends Item {

    public FilterItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;

        // Проверяем каждые 20 тиков (1 секунда)
        if (player.tickCount % 20 != 0) return;

        // Получаем список предметов для фильтрации из NBT самого фильтра
        List<ItemStack> filterItems = FilterHelper.getFilterItems(stack);
        if (filterItems.isEmpty()) return;

        Inventory inv = player.getInventory();

        // Проверяем весь инвентарь (кроме слота с фильтром)
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (i == slotId) continue; // пропускаем сам фильтр

            ItemStack invStack = inv.getItem(i);
            if (invStack.isEmpty()) continue;

            // Проверяем, есть ли этот предмет в списке фильтра
            for (ItemStack filterStack : filterItems) {
                if (ItemStack.isSameItem(invStack, filterStack)) {
                    inv.setItem(i, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new FilterContainer(id, inv, p),
                    Component.translatable("container.inventoryfilter.filter")
            ));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        List<ItemStack> filterItems = FilterHelper.getFilterItems(stack);
        if (!filterItems.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.inventoryfilter.filter_items"));
            for (ItemStack item : filterItems) {
                tooltip.add(Component.literal(" - ").append(item.getHoverName()));
            }
        }
    }
}