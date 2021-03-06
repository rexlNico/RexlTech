package de.rexlnico.rexltech.utils.item;

import de.rexlnico.rexltech.utils.helper.EnergyHelper;
import de.rexlnico.rexltech.utils.tileentity.CustomItemEnergyStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
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

public abstract class EnergyArmorItem extends ArmorItem {

    private final int energyCapacity;
    private final int input;
    private final int output;

    public EnergyArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn, int capacity, int input, int output) {
        super(materialIn, slot, builderIn);
        this.energyCapacity = capacity;
        this.input = input;
        this.output = output;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CapabilityProviderEnergyItem(stack, energyCapacity, input, output);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(getEnergyText(stack));
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
        if (!this.isInGroup(group)) {
            return;
        }

        ItemStack charged = new ItemStack(this);
        charged.getOrCreateTag().putDouble("Energy", energyCapacity);
        items.add(charged);
    }

    public int getEnergyCapacity() {
        return energyCapacity;
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

    public StringTextComponent getEnergyText(ItemStack stack) {
        StringTextComponent textComponent = EnergyHelper.getEnergyStorageTextComponent(getEnergyStored(stack), energyCapacity);
        textComponent.setStyle(Style.EMPTY.setColor(Color.fromHex("#cc0099")));
        return textComponent;
    }

}
