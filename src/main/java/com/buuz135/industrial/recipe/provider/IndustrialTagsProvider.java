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

package com.buuz135.industrial.recipe.provider;

import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.utils.IndustrialTags;
import com.buuz135.industrial.utils.Reference;
import me.alphamode.forgetags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class IndustrialTagsProvider {

    public static class Blocks extends FabricTagProvider.BlockTagProvider {

        public Blocks(FabricDataGenerator generatorIn, String modid) {
            super(generatorIn);
        }

        @Override
        protected void generateTags() {
            tag(IndustrialTags.Blocks.MACHINE_FRAME_PITY).add(ModuleCore.PITY);
            tag(IndustrialTags.Blocks.MACHINE_FRAME_SIMPLE).add(ModuleCore.SIMPLE);
            tag(IndustrialTags.Blocks.MACHINE_FRAME_ADVANCED).add(ModuleCore.ADVANCED);
            tag(IndustrialTags.Blocks.MACHINE_FRAME_SUPREME).add(ModuleCore.SUPREME);

            TagAppender<Block> tTagAppender = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);

            Registry.BLOCK.stream().filter(block -> block.getRegistryName().getNamespace().equals(Reference.MOD_ID)).forEach(tTagAppender::add);
        }
    }

    public static class Items extends FabricTagProvider.ItemTagProvider {

        public Items(FabricDataGenerator generatorIn, String modid) {
            super(generatorIn, new Blocks(generatorIn, modid));
        }

        @Override
        protected void generateTags() {
            this.copy(IndustrialTags.Blocks.MACHINE_FRAME_PITY,  IndustrialTags.Items.MACHINE_FRAME_PITY);
            this.copy(IndustrialTags.Blocks.MACHINE_FRAME_SIMPLE,  IndustrialTags.Items.MACHINE_FRAME_SIMPLE);
            this.copy(IndustrialTags.Blocks.MACHINE_FRAME_ADVANCED,  IndustrialTags.Items.MACHINE_FRAME_ADVANCED);
            this.copy(IndustrialTags.Blocks.MACHINE_FRAME_SUPREME, IndustrialTags.Items.MACHINE_FRAME_SUPREME);

            tag(IndustrialTags.Items.PLASTIC).add(ModuleCore.PLASTIC);
            tag(IndustrialTags.Items.SLUDGE_OUTPUT).add(net.minecraft.world.item.Items.DIRT, net.minecraft.world.item.Items.CLAY, net.minecraft.world.item.Items.GRAVEL, net.minecraft.world.item.Items.SAND, net.minecraft.world.item.Items.RED_SAND, net.minecraft.world.item.Items.SOUL_SAND);
            tag(Tags.Items.SLIMEBALLS).add(ModuleCore.PINK_SLIME_ITEM);
        }
    }

}
