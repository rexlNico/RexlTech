package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.block.BaseMachineBlock;
import de.rexlnico.rexltech.item.recipe.LatexExtractorRecipe;
import de.rexlnico.rexltech.utils.helper.RecipeHelper;
import de.rexlnico.rexltech.utils.init.FluidInit;
import de.rexlnico.rexltech.utils.init.TileEntityInit;
import de.rexlnico.rexltech.utils.networking.FluidTankUpdatePacket;
import de.rexlnico.rexltech.utils.networking.PacketHandler;
import de.rexlnico.rexltech.utils.tileentity.CustomFluidStorage;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.HashMap;

public class TileEntityLatexExtractor extends BaseTileEntity {

    public static HashMap<DimensionType, HashMap<ChunkPos, HashMap<BlockPos, FluidExtractionProgress>>> EXTRACTION = new HashMap<>();

    private boolean active = false;
    public CustomFluidStorage latexTank = new CustomFluidStorage(this, 8000);
    private int workTime = 0;

    public TileEntityLatexExtractor() {
        super(TileEntityInit.LATEX_EXTRACTOR.get(), SideConfiguration.OUTPUT);
        latexTank.input = false;
        sendTankUpdate();
    }

    @Override
    public void onTick() {
        BlockPos block = getBlockPosInFront();
        if (!world.isAirBlock(block)) {
            LatexExtractorRecipe recipe = RecipeHelper.getLatexExtractorRecipe(this);
            if (recipe != null && latexTank.getCapacity() >= latexTank.getFluidAmount() + recipe.getLatexProduction()) {
                if (!active) {
                    active = true;
                    changeBurningState(true);
                }
                FluidExtractionProgress extractionProgress = EXTRACTION.computeIfAbsent(this.world.getDimensionType(), dimensionType -> new HashMap<>()).computeIfAbsent(this.world.getChunkAt(block).getPos(), chunkPos -> new HashMap<>()).computeIfAbsent(block, pos1 -> new FluidExtractionProgress(this.world));
                latexTank.fillForced(new FluidStack(FluidInit.LATEX_FLUID.get(), recipe.getLatexProduction()), IFluidHandler.FluidAction.EXECUTE);
                PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new FluidTankUpdatePacket(pos, latexTank.getFluid()));
                if (workTime % (recipe.getWorkTime() / 7) == 0) {
                    extractionProgress.addProgress(1);
                }
                if (extractionProgress.getProgress() > 7) {
                    extractionProgress.setProgress(-1);
                    world.setBlockState(block, recipe.getResult());
                    workTime = 0;
                }
                workTime++;
            } else {
                if (active) {
                    active = false;
                    changeBurningState(false);
                }
            }
        } else {
            if (active) {
                active = false;
                changeBurningState(false);
            }
        }
    }

    @Override
    public void setTanksStack(int id, FluidStack stack) {
        latexTank.setFluid(stack);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("tank", latexTank.writeToNBT(new CompoundNBT()));
        compound.putInt("work_time", workTime);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        latexTank.readFromNBT(nbt.getCompound("tank"));
        workTime = nbt.getInt("work_time");
        super.read(state, nbt);
        sendTankUpdate();
    }

    public void sendTankUpdate() {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new FluidTankUpdatePacket(pos, latexTank.getFluid()));
    }

    public BlockPos getBlockPosInFront() {
        return pos.offset(world.getBlockState(pos).get(BaseMachineBlock.FACING));
    }

    @Override
    public int getLightValue() {
        return 0;
    }

    public static class FluidExtractionProgress {

        private int progress;
        private int breakID;

        public FluidExtractionProgress(World world) {
            this.progress = 0;
            this.breakID = world.rand.nextInt();
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public void addProgress(int progress) {
            this.progress += progress;
        }

        public int getBreakID() {
            return breakID;
        }

    }
}
