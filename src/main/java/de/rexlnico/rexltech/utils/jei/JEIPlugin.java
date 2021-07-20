package de.rexlnico.rexltech.utils.jei;

import de.rexlnico.rexltech.RexlTech;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEIPlugin implements IModPlugin {


    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(RexlTech.MODID, "jei");
    }



}
