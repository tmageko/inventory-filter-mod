package com.tanya.inventoryfilter.registry;

import com.tanya.inventoryfilter.InventoryFilterMod;
import com.tanya.inventoryfilter.container.FilterContainer;
import com.tanya.inventoryfilter.item.FilterItem;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, InventoryFilterMod.MODID);

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, InventoryFilterMod.MODID);

    // Предмет фильтра
    public static final RegistryObject<Item> FILTER_ITEM = ITEMS.register("filter",
            () -> new FilterItem(new Item.Properties()));

    // Контейнер для GUI
    public static final RegistryObject<MenuType<FilterContainer>> FILTER_CONTAINER =
            MENUS.register("filter",
                    () -> IForgeMenuType.create((windowId, inv, data) ->
                            new FilterContainer(windowId, inv, inv.player))
            );
}