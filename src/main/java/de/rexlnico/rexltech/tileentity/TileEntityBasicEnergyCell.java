package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.utils.init.TileEntityInit;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;

public class TileEntityBasicEnergyCell extends BaseTileEntityEnergyCell {

    public TileEntityBasicEnergyCell() {
        super(TileEntityInit.BASIC_ENERGY_CELL.get(), new SideConfiguration[]{SideConfiguration.INPUT, SideConfiguration.OUTPUT, SideConfiguration.IN_AND_OUT}, 100000, 500);
    }


    @Override
    public void onTick() {
    }

}
