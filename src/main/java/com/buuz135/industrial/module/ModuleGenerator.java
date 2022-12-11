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
import com.buuz135.industrial.block.generator.*;
import com.buuz135.industrial.block.generator.mycelial.IMycelialGeneratorType;
import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.module.RegistryHelper;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ModuleGenerator implements IModule {

    public static AdvancedTitaniumTab TAB_GENERATOR = new AdvancedTitaniumTab(new ResourceLocation(Reference.MOD_ID, "generator"), true);

    public static Pair<Block, BlockEntityType<?>> PITIFUL_GENERATOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("pitiful_generator", PitifulGeneratorBlock::new);
    public static Pair<Block, BlockEntityType<?>> BIOREACTOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("bioreactor", BioReactorBlock::new);
    public static Pair<Block, BlockEntityType<?>> BIOFUEL_GENERATOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("biofuel_generator", BiofuelGeneratorBlock::new);
    public static List<Pair<Block, BlockEntityType<?>>> MYCELIAL_GENERATORS = new ArrayList<>();
    public static Pair<Block, BlockEntityType<?>> MYCELIAL_REACTOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("mycelial_reactor", MycelialReactorBlock::new);

    @Override
    public void generateFeatures(RegistryHelper helper) {
        for (IMycelialGeneratorType type : IMycelialGeneratorType.TYPES) {
            MYCELIAL_GENERATORS.add(helper.registerBlockWithTile("mycelial_" + type.getName(), () -> new MycelialGeneratorBlock(type)));
        }
        TAB_GENERATOR.addIconStack(() -> new ItemStack(PITIFUL_GENERATOR.getLeft()));
    }
}
