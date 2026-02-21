// ModClient.java
package com.tanya.inventoryfilter;

import com.tanya.inventoryfilter.registry.ModRegistry;
import com.tanya.inventoryfilter.screen.FilterScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModClient {

    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModRegistry.FILTER_CONTAINER.get(), FilterScreen::new);
        });
    }
}