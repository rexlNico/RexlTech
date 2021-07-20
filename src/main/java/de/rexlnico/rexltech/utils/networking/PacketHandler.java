package de.rexlnico.rexltech.utils.networking;

import de.rexlnico.rexltech.RexlTech;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    private static int id = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RexlTech.MODID, "main"),
            () -> PROTOCOL_VERSION,
            s -> true,
            s -> true
    );

    public static void init() {
        INSTANCE.registerMessage(id++, SideConfigChangePacket.class, SideConfigChangePacket::toBytes, SideConfigChangePacket::new, SideConfigChangePacket::handle);
    }

}
