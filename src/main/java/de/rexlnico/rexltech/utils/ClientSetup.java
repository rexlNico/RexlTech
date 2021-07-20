package de.rexlnico.rexltech.utils;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.screen.CoalGeneratorScreen;
import de.rexlnico.rexltech.utils.init.ContainerInit;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RexlTech.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ContainerInit.COAL_GENERATOR_CONTAINER.get(), CoalGeneratorScreen::new);
    }

}
