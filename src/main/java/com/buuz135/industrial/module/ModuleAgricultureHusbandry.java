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
import com.buuz135.industrial.block.agriculturehusbandry.*;
import com.buuz135.industrial.registry.IFRegistries;
import com.buuz135.industrial.utils.Reference;
import com.buuz135.industrial.utils.apihandlers.plant.*;
import com.hrznstudio.titanium.module.RegistryHelper;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.tuple.Pair;

public class ModuleAgricultureHusbandry implements IModule {

    public static AdvancedTitaniumTab TAB_AG_HUS = new AdvancedTitaniumTab(Reference.MOD_ID + "_ag_hus", true);

    public static Pair<Block, BlockEntityType<?>> PLANT_GATHERER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("plant_gatherer", PlantGathererBlock::new);
    public static Pair<Block, BlockEntityType<?>> SEWER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("sewer", SewerBlock::new);
    public static Pair<Block, BlockEntityType<?>> SEWAGE_COMPOSTER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("sewage_composter", SewageComposterBlock::new);
    public static Pair<Block, BlockEntityType<?>> PLANT_FERTILIZER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("plant_fertilizer", PlantFertilizerBlock::new);
    public static Pair<Block, BlockEntityType<?>> PLANT_SOWER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("plant_sower", PlantSowerBlock::new);
    public static Pair<Block, BlockEntityType<?>> SLAUGHTER_FACTORY = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("mob_slaughter_factory", SlaughterFactoryBlock::new);
    public static Pair<Block, BlockEntityType<?>> ANIMAL_RANCHER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("animal_rancher", AnimalRancherBlock::new);
    public static Pair<Block, BlockEntityType<?>> ANIMAL_FEEDER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("animal_feeder", AnimalFeederBlock::new);
    public static Pair<Block, BlockEntityType<?>> ANIMAL_BABY_SEPARATOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("animal_baby_separator", AnimalBabySeparatorBlock::new);
    public static Pair<Block, BlockEntityType<?>> MOB_CRUSHER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("mob_crusher", MobCrusherBlock::new);
    public static Pair<Block, BlockEntityType<?>> HYDROPONIC_BED = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("hydroponic_bed", HydroponicBedBlock::new);
    public static Pair<Block, BlockEntityType<?>> MOB_DUPLICATOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("mob_duplicator", MobDuplicatorBlock::new);
    public static Pair<Block, BlockEntityType<?>> WITHER_BUILDER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("wither_builder", WitherBuilderBlock::new);

    @Override
    public void generateFeatures(RegistryHelper registryHelper) {

        registryHelper.registerGeneric(IFRegistries.PLANT_RECOLLECTABLE,"blockcropplant", BlockCropPlantRecollectable::new);
        registryHelper.registerGeneric(IFRegistries.PLANT_RECOLLECTABLE,"blocknetherwart", BlockNetherWartRecollectable::new);
        registryHelper.registerGeneric(IFRegistries.PLANT_RECOLLECTABLE,"blocksugarandcactus", DoubleTallPlantRecollectable::new);
        registryHelper.registerGeneric(IFRegistries.PLANT_RECOLLECTABLE,"blockpumpkingandmelon", PumpkinMelonPlantRecollectable::new);
        registryHelper.registerGeneric(IFRegistries.PLANT_RECOLLECTABLE,"tree", TreePlantRecollectable::new);
        registryHelper.registerGeneric(IFRegistries.PLANT_RECOLLECTABLE,"chorus_fruit", ChorusFruitRecollectable::new);
        registryHelper.registerGeneric(IFRegistries.PLANT_RECOLLECTABLE,"bamboo", BambooPlantRecollectable::new);
        registryHelper.registerGeneric(IFRegistries.PLANT_RECOLLECTABLE,"kelp", KelpPlantRecollectable::new);
        registryHelper.registerGeneric(IFRegistries.PLANT_RECOLLECTABLE,"sweetberries", SweetBerriesPlantRecollectable::new);
        TAB_AG_HUS.addIconStack(() -> new ItemStack(PLANT_SOWER.getLeft()));
    }
}
