package de.rexlnico.rexltech;

import de.rexlnico.rexltech.config.RexlTechGeneratorConfig;
import de.rexlnico.rexltech.config.RexlTechOreConfig;
import de.rexlnico.rexltech.utils.ClientSetup;
import de.rexlnico.rexltech.utils.init.*;
import de.rexlnico.rexltech.utils.networking.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("rexltech")
public class RexlTech {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "rexltech";

    public RexlTech() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        TileEntityInit.TILE_ENTITIES.register(bus);
        ContainerInit.CONTAINERS.register(bus);

        bus.addListener(ClientSetup::init);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, EventManager::addOres);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RexlTechOreConfig.SPEC, "rexltech-oregen.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RexlTechGeneratorConfig.SPEC, "rexltech-renerator.toml");

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventManager.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
    }
}
