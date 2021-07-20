package de.rexlnico.rexltech.itemgroup;

import de.rexlnico.rexltech.utils.init.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MachineGroup  extends ItemGroup {

    public static final MachineGroup MACHINEGROUP = new MachineGroup(ItemGroup.GROUPS.length, "rexltech_machines");

    public MachineGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemInit.COAL_GENERATOR.get());
    }

}
