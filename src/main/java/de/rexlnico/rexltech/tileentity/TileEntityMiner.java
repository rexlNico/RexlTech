package de.rexlnico.rexltech.tileentity;

import com.mojang.authlib.GameProfile;
import de.rexlnico.rexltech.utils.handler.ItemCaptureHandler;
import de.rexlnico.rexltech.utils.helper.FakePlayer;
import de.rexlnico.rexltech.utils.init.ItemInit;
import de.rexlnico.rexltech.utils.init.TileEntityInit;
import de.rexlnico.rexltech.utils.tileentity.CustomEnergyStorage;
import de.rexlnico.rexltech.utils.tileentity.CustomItemStackUpgradesHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntityMiner extends TileEntity implements ITickableTileEntity {

    private final static ItemStack genericDigger = new ItemStack(Items.DIAMOND_PICKAXE, 1);
    private ItemStack diggerTool;

    public List<TileEntityItemOutput> outputs = new ArrayList<>();
    public boolean formed = false;
    public CustomItemStackUpgradesHandler itemStackHandler = new CustomItemStackUpgradesHandler(this, 4, ItemInit.SPEED_UPGRADE.get(), ItemInit.CREATIVE_ENERGY_UPGRADE.get());
    public ArrayList<ItemStack> extraStacksHandler = new ArrayList<>();
    private LazyOptional<IItemHandler> lazyOptional = LazyOptional.of(() -> itemStackHandler);
    public CustomEnergyStorage energyStorage = new CustomEnergyStorage(this, 10000000, 2500000, 0);
    private LazyOptional<IEnergyStorage> lazyOptionalEnergy = LazyOptional.of(() -> energyStorage);
    private int progress = 0;
    private int maxProgress = 200;
    private GameProfile owner = null;
    private FakePlayer fakePlayer;

    public TileEntityMiner() {
        super(TileEntityInit.MINER_BLOCK.get());
    }

    public void setOwner(GameProfile gameProfile) {
        owner = gameProfile;
    }

    public boolean operate() {
        if (!extraStacksHandler.isEmpty()) {
            List<ItemStack> remove = new ArrayList<>();
            for (ItemStack stack : extraStacksHandler) {
                if (sendItemToOutput(stack)) {
                    remove.add(stack);
                }
            }
            extraStacksHandler.removeAll(remove);
            return false;
        }
        if (owner == null) return false;
        boolean success = false;
        Direction side = Direction.NORTH;
        BlockPos offset = getPos().offset(side);
        World world = this.world;
        TileEntity tileEntity = world.getTileEntity(offset);
        if (world.isAirBlock(offset)) return false;
        if (fakePlayer == null) {
            fakePlayer = new FakePlayer((ServerWorld) world, owner, "miner");
        }
        if (diggerTool == null) {
            diggerTool = genericDigger.copy();
            //Apply enchantments from upgrades
        }

        BlockState blockState = world.getBlockState(offset);
        if (blockState.getMaterial().isLiquid()) return false;
        float hardness = blockState.getPlayerRelativeBlockHardness(fakePlayer, world, offset);
        if (hardness == 0) return false;

        fakePlayer.setHeldItem(Hand.MAIN_HAND, diggerTool.copy());
        fakePlayer.setLocationEdge(offset, side);
        ItemCaptureHandler.startCapturing();
        success = fakePlayer.interactionManager.tryHarvestBlock(offset);
        LinkedList<ItemStack> stacks = ItemCaptureHandler.stopCapturing();
        for (ItemStack stack : stacks) {
            if (!sendItemToOutput(stack)) {
                extraStacksHandler.add(stack);
            }
        }
        fakePlayer.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
        PlayerInventory inventory = fakePlayer.inventory;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getCount() > 0)
                if (!sendItemToOutput(stack)) {
                    extraStacksHandler.add(stack);
                }
        }

        fakePlayer.clearInventory();
        return true;
    }

    public boolean sendItemToOutput(ItemStack stack) {

        return false;
    }

    @Override
    public void tick() {
        if (world.isRemote) return;

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) return lazyOptionalEnergy.cast();
        return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return lazyOptional.cast();
        if (cap == CapabilityEnergy.ENERGY) return lazyOptionalEnergy.cast();
        return super.getCapability(cap);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("Formed", formed);
        if (!outputs.isEmpty() && formed) {
            compound.put("Outputs", outputs.stream().map(tileEntity -> NBTUtil.writeBlockPos(tileEntity.getPos())).collect(Collectors.toCollection(ListNBT::new)));
        }
        compound.put("Inventory", itemStackHandler.serializeNBT());
        compound.put("Energy", energyStorage.serializeNBT());
        compound.putInt("Progress", progress);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        formed = nbt.getBoolean("Formed");
        if (nbt.contains("Outputs") && formed)
            nbt.getList("Outputs", Constants.NBT.TAG_COMPOUND).forEach(inbt -> outputs.add((TileEntityItemOutput) world.getTileEntity(NBTUtil.readBlockPos((CompoundNBT) inbt))));
        itemStackHandler.deserializeNBT(nbt.getCompound("Inventory"));
        energyStorage.deserializeNBT(nbt.getCompound("Energy"));
        progress = nbt.getInt("Progress");
        super.read(state, nbt);
    }

}
