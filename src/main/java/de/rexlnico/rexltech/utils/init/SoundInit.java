package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class SoundInit {

    public static final SoundEvent JETPACK = new SoundEvent(new ResourceLocation(RexlTech.MODID + "jetpack"));
    public static final SoundEvent CRUSHER = new SoundEvent(new ResourceLocation(RexlTech.MODID + "crusher"));
    public static final SoundEvent COAL_GENERATOR = new SoundEvent(new ResourceLocation(RexlTech.MODID + "coal_generator"));

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        registry.register(JETPACK.setRegistryName("jetpack"));
        registry.register(CRUSHER.setRegistryName("crusher"));
        registry.register(COAL_GENERATOR.setRegistryName("coal_generator"));
    }

}
