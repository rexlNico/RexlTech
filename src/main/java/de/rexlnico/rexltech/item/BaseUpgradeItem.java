package de.rexlnico.rexltech.item;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.itemgroup.UpgradeGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BaseUpgradeItem extends Item {

    public BaseUpgradeItem(Properties properties) {
        super(properties.group(UpgradeGroup.UPGRADEGROUP));
    }

    public abstract float getAdditionalEnergy();

    public abstract float getAdditionalSpeed();

    public static int getEnergyUsage(int baseUsage, ItemStack stack) {
        if (!(stack.getItem() instanceof BaseUpgradeItem)) return baseUsage;
        if (((BaseUpgradeItem) stack.getItem()).getAdditionalEnergy() == 0) return baseUsage;
        if (((BaseUpgradeItem) stack.getItem()).getAdditionalEnergy() == -1) return 0;
        if (stack == null || stack.isEmpty()) return baseUsage;
        return (int) (baseUsage * Math.pow(((BaseUpgradeItem) stack.getItem()).getAdditionalEnergy(), stack.getCount()));
    }

    public static int getTickTimeRemove(int baseRemove, ItemStack stack) {
        if (!(stack.getItem() instanceof BaseUpgradeItem)) return baseRemove;
        if (((BaseUpgradeItem) stack.getItem()).getAdditionalSpeed() == 0) return baseRemove;
        if (stack == null || stack.isEmpty()) return baseRemove;
        return (int) (baseRemove * Math.pow(((BaseUpgradeItem) stack.getItem()).getAdditionalSpeed(), stack.getCount()));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        TranslationTextComponent component = new TranslationTextComponent("description." + getRegistryName().toString().replace(RexlTech.MODID + ":", ""));
        component.setStyle(Style.EMPTY.setColor(Color.fromHex("#00bfff")));
        tooltip.add(component);
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
