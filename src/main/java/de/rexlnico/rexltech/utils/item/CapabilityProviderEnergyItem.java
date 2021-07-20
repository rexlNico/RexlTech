package de.rexlnico.rexltech.utils.item;

import de.rexlnico.rexltech.utils.tileentity.CustomItemEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderEnergyItem implements ICapabilityProvider {

    public final CustomItemEnergyStorage storage;
    private final LazyOptional<CustomItemEnergyStorage> energyCapability;

    public CapabilityProviderEnergyItem(ItemStack stack, int capacity, int maxIn, int maxOur) {
        this.storage = new CustomItemEnergyStorage(capacity, maxIn, maxOur) {
            @Override
            public int getEnergyStored() {
                return stack.getOrCreateTag().getInt("Energy");
            }

            @Override
            public void setEnergyStored(int energy) {
                stack.getOrCreateTag().putInt("Energy", energy);
            }
        };
        this.energyCapability = LazyOptional.of(() -> this.storage);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return this.energyCapability.cast();
        }
        return LazyOptional.empty();
    }

}
