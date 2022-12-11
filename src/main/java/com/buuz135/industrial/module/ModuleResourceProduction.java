/*
 * This file is part of Industrial Foregoing.
 *
 * Copyright 2021, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.buuz135.industrial.module;

import com.buuz135.industrial.IndustrialForegoing;
import com.buuz135.industrial.block.resourceproduction.*;
import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.module.RegistryHelper;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.tuple.Pair;

;

public class ModuleResourceProduction implements IModule {

    public static AdvancedTitaniumTab TAB_RESOURCE = new AdvancedTitaniumTab(new ResourceLocation(Reference.MOD_ID, "resource_production"), true);

    public static Pair<Block, BlockEntityType<?>> RESOURCEFUL_FURNACE = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("resourceful_furnace", ResourcefulFurnaceBlock::new);
    public static Pair<Block, BlockEntityType<?>> SLUDGE_REFINER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("sludge_refiner", SludgeRefinerBlock::new);
    public static Pair<Block, BlockEntityType<?>> WATER_CONDENSATOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("water_condensator", WaterCondensatorBlock::new);
    public static Pair<Block, BlockEntityType<?>> MECHANICAL_DIRT = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("mechanical_dirt", MechanicalDirtBlock::new);
    public static Pair<Block, BlockEntityType<?>> BLOCK_PLACER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("block_placer", BlockPlacerBlock::new);
    public static Pair<Block, BlockEntityType<?>> BLOCK_BREAKER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("block_breaker", BlockBreakerBlock::new);
    public static Pair<Block, BlockEntityType<?>> FLUID_COLLECTOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("fluid_collector", FluidCollectorBlock::new);
    public static Pair<Block, BlockEntityType<?>> FLUID_PLACER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("fluid_placer", FluidPlacerBlock::new);
    public static Pair<Block, BlockEntityType<?>> DYE_MIXER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("dye_mixer", DyeMixerBlock::new);
    public static Pair<Block, BlockEntityType<?>> SPORES_RECREATOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("spores_recreator", SporesRecreatorBlock::new);
    public static Pair<Block, BlockEntityType<?>> MATERIAL_STONEWORK_FACTORY = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("material_stonework_factory", MaterialStoneWorkFactoryBlock::new);
    public static Pair<Block, BlockEntityType<?>> MARINE_FISHER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("marine_fisher", MarineFisherBlock::new);
    public static Pair<Block, BlockEntityType<?>> POTION_BREWER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("potion_brewer", PotionBrewerBlock::new);
    public static Pair<Block, BlockEntityType<?>> ORE_LASER_BASE = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("ore_laser_base", OreLaserBaseBlock::new);
    public static Pair<Block, BlockEntityType<?>> LASER_DRILL = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("laser_drill", LaserDrillBlock::new);
    public static Pair<Block, BlockEntityType<?>> FLUID_LASER_BASE = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("fluid_laser_base", FluidLaserBaseBlock::new);
    public static Pair<Block, BlockEntityType<?>> WASHING_FACTORY = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("washing_factory", WashingFactoryBlock::new);
    public static Pair<Block, BlockEntityType<?>> FERMENTATION_STATION = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("fermentation_station", FermentationStationBlock::new);
    public static Pair<Block, BlockEntityType<?>> FLUID_SIEVING_MACHINE = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("fluid_sieving_machine", FluidSievingMachineBlock::new);

    @Override
    public void generateFeatures(RegistryHelper registryHelper) {
        TAB_RESOURCE.addIconStack(() -> new ItemStack(WATER_CONDENSATOR.getLeft()));
    }

}
