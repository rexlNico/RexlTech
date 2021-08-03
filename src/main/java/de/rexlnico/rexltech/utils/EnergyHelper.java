package de.rexlnico.rexltech.utils;

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

}
