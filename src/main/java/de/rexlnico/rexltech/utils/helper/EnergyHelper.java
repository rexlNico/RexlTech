package de.rexlnico.rexltech.utils.helper;

import net.minecraft.util.text.StringTextComponent;

public class EnergyHelper {

    public static String getEnergyString(int number) {

        if (number >= 1000000000) {
            return number / 1000000000 + "G FE";
        } else if (number >= 1000000) {
            return number / 1000000 + "M FE";
        } else if (number >= 1000) {
            return number / 1000 + "k FE";
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

    public static StringTextComponent getEnergyStorageTextComponent(int storage, int maxStorage) {
        return new StringTextComponent(getEnergyStorageString(storage, maxStorage));
    }

    public static StringTextComponent getFluidStorageTextComponent(int storage, int maxStorage) {
        return new StringTextComponent(getFluidStorageString(storage, maxStorage));
    }

}
