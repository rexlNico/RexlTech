package de.rexlnico.rexltech.itemgroup;

import de.rexlnico.rexltech.utils.init.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ResourcesGroup extends ItemGroup {

    public static final ResourcesGroup RESOURCES_GROUP = new ResourcesGroup(ItemGroup.GROUPS.length, "rexltech_resources");

    public ResourcesGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemInit.COPPER_INGOT.get());
    }

}
