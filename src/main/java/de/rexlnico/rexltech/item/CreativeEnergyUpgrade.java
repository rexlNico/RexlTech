package de.rexlnico.rexltech.item;

public class CreativeEnergyUpgrade extends BaseUpgradeItem {

    public CreativeEnergyUpgrade() {
        super(new Properties().maxStackSize(1));
    }

    @Override
    public float getAdditionalEnergy() {
        return -1f;
    }

    @Override
    public float getAdditionalSpeed() {
        return 0f;
    }

}
