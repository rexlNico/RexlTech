package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.container.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RexlTech.MODID);

    public static final RegistryObject<ContainerType<CoalGeneratorContainer>> COAL_GENERATOR_CONTAINER = CONTAINERS.register("coal_generator", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new CoalGeneratorContainer(windowId, world, pos, inv, inv.player);
    })));

    public static final RegistryObject<ContainerType<CrusherContainer>> CRUSHER_CONTAINER = CONTAINERS.register("crusher", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new CrusherContainer(windowId, world, pos, inv, inv.player);
    })));

    public static final RegistryObject<ContainerType<LatexExtractorContainer>> LATEX_EXTRACTOR_CONTAINER = CONTAINERS.register("latex_extractor", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new LatexExtractorContainer(windowId, world, pos, inv, inv.player);
    })));

    public static final RegistryObject<ContainerType<SmelterContainer>> SMELTER_CONTAINER = CONTAINERS.register("smelter", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new SmelterContainer(windowId, world, pos, inv, inv.player);
    })));

    public static final RegistryObject<ContainerType<MinerContainer>> MINER_CONTAINER = CONTAINERS.register("miner", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new MinerContainer(windowId, world, pos, inv, inv.player);
    })));

}
