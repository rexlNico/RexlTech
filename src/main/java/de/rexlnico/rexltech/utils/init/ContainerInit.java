package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.container.CoalGeneratorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RexlTech.MODID);

    public static final RegistryObject<ContainerType<CoalGeneratorContainer>> COAL_GENERATOR_CONTAINER = CONTAINERS.register("coal_generator", () -> IForgeContainerType.create(((windowId, inv, data) ->{
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new CoalGeneratorContainer(windowId, world, pos, inv, inv.player);
    })));

}
