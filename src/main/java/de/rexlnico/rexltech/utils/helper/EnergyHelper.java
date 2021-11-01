package de.rexlnico.rexltech.utils.helper;

import net.minecraft.util.text.StringTextComponent;

public class EnergyHelper {

    public static String getEnergyString(float number) {

        if (number >= 1000000000) {
            return Math.round(number / 100000000F) / 10F + "G FE";
        } else if (number >= 1000000) {
            return Math.round(number / 100000F) / 10F + "M FE";
        } else if (number >= 1000) {
            return Math.round(number / 100F) / 10F + "k FE";
        } else {
            return number + " FE";
        }
    }

    public static String getFluidString(int number) {
        if (number >= 1000000) {
            return number / 1000 + "k mb";
        } else {
            return number + "mb";
        }
    }

    public static String getEnergyStorageString(int storage, int maxStorage) {
        return getEnergyString(storage) + " / " + getEnergyString(maxStorage);
    }

    public static String getFluidStorageString(int storage, int maxStorage) {
        return getFluidString(storage) + " / " + getFluidString(maxStorage);
    }

    public static StringTextComponent getEnergyUsageTextComponent(int usage) {
        return new StringTextComponent(getEnergyString(usage) + "/tick");
    }

    public static StringTextComponent getEnergyStorageTextComponent(int storage, int maxStorage) {
        return new StringTextComponent(getEnergyStorageString(storage, maxStorage));
    }

    public static StringTextComponent getFluidStorageTextComponent(int storage, int maxStorage) {
        return new StringTextComponent(getFluidStorageString(storage, maxStorage));
    }

}
