package de.rexlnico.rexltech.utils.item;

import de.rexlnico.rexltech.utils.tileentity.CustomItemEnergyStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public abstract class EnergyItem extends Item {

    private final int energyCapacity;
    private final int input;
    private final int output;

    public EnergyItem(Properties properties, int capacity, int input, int output) {
        super(properties);
        this.energyCapacity = capacity;
        this.input = input;
        this.output = output;
    }

    public EnergyItem(Properties properties, int capacity, int transfer) {
        super(properties);
        this.energyCapacity = capacity;
        this.input = transfer;
        this.output = transfer;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CapabilityProviderEnergyItem(stack, energyCapacity, input, output);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        StringTextComponent textComponent = new StringTextComponent(getEnergyStored(stack) + " FE / " + energyCapacity + " FE");
        textComponent.setStyle(Style.EMPTY.setColor(Color.fromHex("#cc0099")));
        tooltip.add(textComponent);
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
        return stack.getCapability(CapabilityEnergy.ENERGY)
                .map(cap -> ((CustomItemEnergyStorage) cap).extractEnergyInternal(maxExtract, simulate))
                .orElse(0);
    }

    public int getEnergyStored(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY)
                .map(IEnergyStorage::getEnergyStored)
                .orElse(0);
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY, null)
                .map(cap -> {
                    double maxAmount = cap.getMaxEnergyStored();
                    double energyDif = maxAmount - cap.getEnergyStored();
                    return energyDif / maxAmount;
                })
                .orElse(super.getDurabilityForDisplay(stack));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        if (!this.isInGroup(group)) {
            return;
        }

        ItemStack charged = new ItemStack(this);
        charged.getOrCreateTag().putDouble("Energy", energyCapacity);
        items.add(charged);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null && player.world != null) {
            float[] color = new float[]{204, 0, 153};
            return MathHelper.rgb(color[0] / 255F, color[1] / 255F, color[2] / 255F);
        }
        return super.getRGBDurabilityForDisplay(stack);
    }

}
