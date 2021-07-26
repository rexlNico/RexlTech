package de.rexlnico.rexltech.item;

import net.minecraft.item.Item;

public class SpeedUpgrade extends BaseUpgradeItem {

    public SpeedUpgrade() {
        super(new Item.Properties().maxStackSize(16));
    }

    @Override
    public float getAdditionalEnergy() {
        return 1.25f;
    }

    @Override
    public float getAdditionalSpeed() {
        return 1.2f;
    }

}
