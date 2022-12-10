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

import com.buuz135.industrial.api.conveyor.ConveyorUpgradeFactory;
import com.buuz135.industrial.api.transporter.TransporterTypeFactory;
import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.module.ModuleTool;
import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.annotation.MaterialReference;
import com.hrznstudio.titanium.api.IRecipeProvider;
import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.recipe.generator.TitaniumRecipeProvider;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapedRecipeBuilder;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapelessRecipeBuilder;
import com.hrznstudio.titanium.util.NonNullLazy;
import me.alphamode.forgetags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class IndustrialRecipeProvider extends TitaniumRecipeProvider {

    private final NonNullLazy<List<Block>> blocks;

    public IndustrialRecipeProvider(FabricDataGenerator generatorIn, NonNullLazy<List<Block>> blocks) {
        super(generatorIn);
        this.blocks = blocks;
    }

    @Override
    public void register(Consumer<FinishedRecipe> consumer) {
        this.blocks.get().stream().filter(block -> block instanceof BasicBlock).map(block -> (BasicBlock) block).forEach(blockBase -> blockBase.registerRecipe(consumer));
        //TRANSPORT
        ConveyorUpgradeFactory.FACTORIES.forEach(conveyorUpgradeFactory -> conveyorUpgradeFactory.registerRecipe(consumer));
        TransporterTypeFactory.FACTORIES.forEach(typeFactory -> typeFactory.registerRecipe(consumer));
        //TOOL
        ((IRecipeProvider)ModuleTool.MOB_IMPRISONMENT_TOOL).registerRecipe(consumer);
        ((IRecipeProvider) ModuleTool.MEAT_FEEDER).registerRecipe(consumer);
        ((IRecipeProvider) ModuleTool.INFINITY_DRILL).registerRecipe(consumer);
        ((IRecipeProvider) ModuleTool.INFINITY_SAW).registerRecipe(consumer);
        ((IRecipeProvider) ModuleTool.INFINITY_HAMMER).registerRecipe(consumer);
        ((IRecipeProvider) ModuleTool.INFINITY_TRIDENT).registerRecipe(consumer);
        ((IRecipeProvider) ModuleTool.INFINITY_BACKPACK).registerRecipe(consumer);
        ((IRecipeProvider) ModuleTool.INFINITY_LAUNCHER).registerRecipe(consumer);
        ((IRecipeProvider) ModuleTool.INFINITY_NUKE).registerRecipe(consumer);
        //CORE
        ((IRecipeProvider)ModuleCore.STRAW).registerRecipe(consumer);
        for (Item rangeAddon : ModuleCore.RANGE_ADDONS) {
            ((IRecipeProvider) rangeAddon).registerRecipe(consumer);
        }
        ((IRecipeProvider) ModuleCore.SPEED_ADDON_1).registerRecipe(consumer);
        ((IRecipeProvider) ModuleCore.SPEED_ADDON_2).registerRecipe(consumer);
        ((IRecipeProvider) ModuleCore.EFFICIENCY_ADDON_1).registerRecipe(consumer);
        ((IRecipeProvider) ModuleCore.EFFICIENCY_ADDON_2).registerRecipe(consumer);
        ((IRecipeProvider) ModuleCore.PROCESSING_ADDON_1).registerRecipe(consumer);
        ((IRecipeProvider) ModuleCore.PROCESSING_ADDON_2).registerRecipe(consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(ModuleCore.DRY_RUBBER).requires(ModuleCore.TINY_DRY_RUBBER, 9).save(consumer);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModuleCore.DRY_RUBBER), ModuleCore.PLASTIC, 0.3f, 200).unlockedBy("has_plastic", this.has(ModuleCore.DRY_RUBBER)).save(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(ModuleCore.PITY)
                .pattern("WIW").pattern("IRI").pattern("WIW")
                .define('W', ItemTags.LOGS)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .save(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(ModuleCore.IRON_GEAR)
                .pattern(" P ").pattern("P P").pattern(" P ")
                .define('P', Items.IRON_INGOT)
                .save(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(ModuleCore.GOLD_GEAR)
                .pattern(" P ").pattern("P P").pattern(" P ")
                .define('P', Items.GOLD_INGOT)
                .save(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(ModuleCore.DIAMOND_GEAR)
                .pattern(" P ").pattern("P P").pattern(" P ")
                .define('P', Items.DIAMOND)
                .save(consumer);
        for (Item laserLen : ModuleCore.LASER_LENS) {
            ((IRecipeProvider)laserLen).registerRecipe(consumer);
        }
        for (DyeColor value : DyeColor.values()) {
            TitaniumShapelessRecipeBuilder.shapelessRecipe(ModuleCore.LASER_LENS[value.getId()])
                    .requires(Ingredient.of(Arrays.stream(ModuleCore.LASER_LENS).map(itemRegistryObject -> new ItemStack(itemRegistryObject)).collect(Collectors.toList()).stream()))
                    .requires(value.getTag())
                    .save(consumer, new ResourceLocation(Reference.MOD_ID, "laser_lens_"+value.getSerializedName()+ "_recolor"));
        }
    }
}
