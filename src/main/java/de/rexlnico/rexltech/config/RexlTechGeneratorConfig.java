package de.rexlnico.rexltech.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RexlTechGeneratorConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> coal_generator_production;
    public static final ForgeConfigSpec.ConfigValue<Integer> coal_generator_storage;
    public static final ForgeConfigSpec.ConfigValue<Integer> coal_generator_output;

    static {
        BUILDER.push("RexlTech Ore Config");

        coal_generator_production = BUILDER.comment("Coal Generator generation FE/t. Default value is 40").define("Coal Generator generation", 40);
        coal_generator_storage = BUILDER.comment("Coal Generator storage FE. Default value is 100000").define("Coal Generator storage", 100000);
        coal_generator_output = BUILDER.comment("Coal Generator output FE/t. Default value is 1000").define("Coal Generator output", 1000);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
