package de.rexlnico.rexltech.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RexlTechOreConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> copper_veinSize;
    public static final ForgeConfigSpec.ConfigValue<Integer> copper_minHeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> copper_maxHeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> copper_amount;

    static {
        BUILDER.push("RexlTech Ore Config");

        copper_amount = BUILDER.comment("Copper Ore amount. Default value is 20 set to 0 to disable ore gen").define("Copper Ore amount", 20);
        copper_veinSize = BUILDER.comment("Copper Ore vein size. Default value is 12").define("Copper Ore vein size", 12);
        copper_minHeight = BUILDER.comment("Copper Ore min height. Default value is 15").define("Copper Ore min height", 15);
        copper_maxHeight = BUILDER.comment("Copper Ore max height. Default value is 55").define("Copper Ore max height", 55);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
