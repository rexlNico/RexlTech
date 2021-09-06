package de.rexlnico.rexltech;

import com.google.common.collect.ImmutableMap;
import de.rexlnico.rexltech.client.HudHandler;
import de.rexlnico.rexltech.config.RexlTechClientConfig;
import de.rexlnico.rexltech.config.RexlTechGeneratorConfig;
import de.rexlnico.rexltech.config.RexlTechOreConfig;
import de.rexlnico.rexltech.screen.CoalGeneratorScreen;
import de.rexlnico.rexltech.screen.CrusherScreen;
import de.rexlnico.rexltech.screen.LatexExtractorScreen;
import de.rexlnico.rexltech.utils.handler.InputHandler;
import de.rexlnico.rexltech.utils.handler.ItemCaptureHandler;
import de.rexlnico.rexltech.utils.handler.KeyinputHandler;
import de.rexlnico.rexltech.utils.init.*;
import de.rexlnico.rexltech.utils.networking.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.AxeItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RexlTech.MODID)
public class RexlTech {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "rexltech";

    public RexlTech() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::setupClient);

        FluidInit.FLUIDS.register(bus);
        BlockInit.BLOCKS.register(bus);
        ContainerInit.CONTAINERS.register(bus);
        TileEntityInit.TILE_ENTITIES.register(bus);
        ItemInit.ITEMS.register(bus);
        RecipeInit.RECIPE_SERIALIZER.register(bus);

        bus.register(new SoundInit());

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, EventManager::addOres);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RexlTechOreConfig.SPEC, "rexltech-oregen.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RexlTechGeneratorConfig.SPEC, "rexltech-renerator.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RexlTechClientConfig.SPEC, "rexltech-clientConfig.toml");

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventManager.class);
        MinecraftForge.EVENT_BUS.register(ItemCaptureHandler.INSTANCE);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        MinecraftForge.EVENT_BUS.register(new InputHandler());
        AxeItem.BLOCK_STRIPPING_MAP = new ImmutableMap.Builder<Block, Block>().putAll(AxeItem.BLOCK_STRIPPING_MAP)
                .put(BlockInit.RUBBER_LOG.get(), BlockInit.STRIPPED_RUBBER_LOG.get())
                .put(BlockInit.RUBBER_WOOD.get(), BlockInit.STRIPPED_RUBBER_WOOD.get())
                .build();
    }

    private void setupClient(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyinputHandler());
        MinecraftForge.EVENT_BUS.register(new HudHandler());
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (minecraft, screen) -> new RexlTechClientConfig(screen));
        ScreenManager.registerFactory(ContainerInit.COAL_GENERATOR_CONTAINER.get(), CoalGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.CRUSHER_CONTAINER.get(), CrusherScreen::new);
        ScreenManager.registerFactory(ContainerInit.LATEX_EXTRACTOR_CONTAINER.get(), LatexExtractorScreen::new);
        RenderTypeLookup.setRenderLayer(BlockInit.BASIC_ENERGY_CABLE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.RUBBER_LEAVES.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.RUBBER_SAPLING.get(), RenderType.getCutout());
    }


}
