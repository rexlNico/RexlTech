package de.rexlnico.rexltech.itemgroup;

import de.rexlnico.rexltech.utils.init.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class UpgradeGroup extends ItemGroup {

    public static final UpgradeGroup UPGRADEGROUP = new UpgradeGroup(ItemGroup.GROUPS.length, "rexltech_upgrades");

    public UpgradeGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemInit.SPEED_UPGRADE.get());
    }

}
