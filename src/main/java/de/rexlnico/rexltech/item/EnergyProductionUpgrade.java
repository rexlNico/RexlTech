package de.rexlnico.rexltech.item;

public class EnergyProductionUpgrade extends BaseUpgradeItem {

    public EnergyProductionUpgrade() {
        super(new Properties().maxStackSize(4));
    }

    @Override
    public float getAdditionalEnergy() {
        return 1.1f;
    }

    @Override
    public float getAdditionalSpeed() {
        return 1.11f;
    }

}
