package de.rexlnico.rexltech.growable;

import de.rexlnico.rexltech.world.gen.ModConfiguredFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class RubberTree extends Tree {

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return ModConfiguredFeatures.RUBBER;
    }

}
