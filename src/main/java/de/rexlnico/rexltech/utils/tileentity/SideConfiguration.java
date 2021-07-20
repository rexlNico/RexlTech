package de.rexlnico.rexltech.utils.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public enum SideConfiguration {

    NONE(false, false, 0, 0),
    INPUT(true, false, 89, 23),
    OUTPUT(false, true, 89, 23 + 17),
    IN_AND_OUT(true, true, 89, 23 + 17 + 17);

    public boolean input;
    public boolean output;
    public int textureX;
    public int textureY;

    SideConfiguration(boolean input, boolean output, int textureX, int textureY) {
        this.input = input;
        this.output = output;
        this.textureX = textureX;
        this.textureY = textureY;
    }

    public static SideConfiguration[] getDefaultSideConfig() {
        SideConfiguration[] back = new SideConfiguration[]{NONE, NONE, NONE, NONE, NONE, NONE};
        return back;
    }

    public static void writeToNBT(CompoundNBT compound, SideConfiguration[] sideConfig) {
        compound.putIntArray("side_configuration", new int[]{sideConfig[Direction.UP.ordinal()].ordinal(),
                sideConfig[Direction.DOWN.ordinal()].ordinal(),
                sideConfig[Direction.NORTH.ordinal()].ordinal(),
                sideConfig[Direction.SOUTH.ordinal()].ordinal(),
                sideConfig[Direction.EAST.ordinal()].ordinal(),
                sideConfig[Direction.WEST.ordinal()].ordinal()});
    }

    public static SideConfiguration[] readFromNBTConfig(CompoundNBT compound) {
        if (!compound.contains("side_configuration")) return getDefaultSideConfig();
        SideConfiguration[] config = new SideConfiguration[6];
        int[] intConfigs = compound.getIntArray("side_configuration");
        config[Direction.UP.ordinal()] = values()[intConfigs[0]];
        config[Direction.DOWN.ordinal()] = values()[intConfigs[1]];
        config[Direction.NORTH.ordinal()] = values()[intConfigs[2]];
        config[Direction.SOUTH.ordinal()] = values()[intConfigs[3]];
        config[Direction.EAST.ordinal()] = values()[intConfigs[4]];
        config[Direction.WEST.ordinal()] = values()[intConfigs[5]];
        return config;
    }

}
