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
import com.buuz135.industrial.block.misc.*;
import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.module.RegistryHelper;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.tuple.Pair;


public class ModuleMisc implements IModule {

    public static AdvancedTitaniumTab TAB_MISC = new AdvancedTitaniumTab(new ResourceLocation(Reference.MOD_ID, "misc"), true);
    public static Pair<Block, BlockEntityType<?>> STASIS_CHAMBER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("stasis_chamber", StasisChamberBlock::new);
    public static Pair<Block, BlockEntityType<?>> MOB_DETECTOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("mob_detector", MobDetectorBlock::new);
    public static Pair<Block, BlockEntityType<?>> ENCHANTMENT_SORTER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("enchantment_sorter", EnchantmentSorterBlock::new);
    public static Pair<Block, BlockEntityType<?>> ENCHANTMENT_APPLICATOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("enchantment_applicator", EnchantmentApplicatorBlock::new);
    public static Pair<Block, BlockEntityType<?>> ENCHANTMENT_EXTRACTOR = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("enchantment_extractor", EnchantmentExtractorBlock::new);
    public static Pair<Block, BlockEntityType<?>> ENCHANTMENT_FACTORY = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("enchantment_factory", EnchantmentFactoryBlock::new);
    public static Pair<Block, BlockEntityType<?>> INFINITY_CHARGER = IndustrialForegoing.INSTANCE.getRegistries().registerBlockWithTile("infinity_charger", InfinityChargerBlock::new);

    @Override
    public void generateFeatures(RegistryHelper helper) {
        LivingEntityEvents.TICK.register((livingEntity -> {
            if (livingEntity instanceof Mob mob){
                if (mob.getPersistenData().contains("StasisChamberTime")){
                    long time = mob.getPersistentData().getLong("StasisChamberTime");
                    if (time + 50 <= mob.level.getGameTime()) {
                        mob.setNoAi(false);
                    }
                }
            }
        }));
        TAB_MISC.addIconStack(() -> new ItemStack(STASIS_CHAMBER.getLeft()));
    }
}
