package com.tanya.inventoryfilter;

import com.tanya.inventoryfilter.registry.ModRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(InventoryFilterMod.MODID)
public class InventoryFilterMod {

    public static final String MODID = "inventoryfilter";

    public InventoryFilterMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Регистрируем все deferred registers
        ModRegistry.ITEMS.register(modEventBus);
        ModRegistry.MENUS.register(modEventBus);

        // 👇 ДОБАВЛЯЕМ ЭТУ СТРОКУ 👇
        modEventBus.addListener(this::addCreative);

        // Регистрируем клиентские настройки
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> modEventBus.addListener(ModClient::clientSetup));

        MinecraftForge.EVENT_BUS.register(this);
    }

    // 👇 ДОБАВЛЯЕМ ЭТОТ МЕТОД В КОНЕЦ КЛАССА 👇
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModRegistry.FILTER_ITEM.get());
        }
    }
}