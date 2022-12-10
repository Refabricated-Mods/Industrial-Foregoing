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
package com.buuz135.industrial.recipe;

import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

public class StoneWorkGenerateRecipe extends SerializableRecipe {

    public static GenericSerializer<StoneWorkGenerateRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(Reference.MOD_ID, "stonework_generate"), StoneWorkGenerateRecipe.class);
    public static List<StoneWorkGenerateRecipe> RECIPES = new ArrayList<>();

    static {
        new StoneWorkGenerateRecipe(new ResourceLocation(Reference.MOD_ID, "cobblestone"),new ItemStack(Blocks.COBBLESTONE), FluidConstants.BLOCK, FluidConstants.BLOCK, 0, 0);
        new StoneWorkGenerateRecipe(new ResourceLocation(Reference.MOD_ID, "netherrack"),new ItemStack(Blocks.NETHERRACK), 250 * 81, 400 * 81, 250 * 81, 200 * 81);
        new StoneWorkGenerateRecipe(new ResourceLocation(Reference.MOD_ID, "obsidian"),new ItemStack(Blocks.OBSIDIAN), FluidConstants.BLOCK, FluidConstants.BLOCK, 0, FluidConstants.BLOCK);
        new StoneWorkGenerateRecipe(new ResourceLocation(Reference.MOD_ID, "granite"),new ItemStack(Blocks.GRANITE), 200 * 81, 200 * 81, 200 * 81, 200 * 81);
        new StoneWorkGenerateRecipe(new ResourceLocation(Reference.MOD_ID, "diorite"),new ItemStack(Blocks.DIORITE), 200 * 81, 250 * 81, 200 * 81, 250 * 81);
        new StoneWorkGenerateRecipe(new ResourceLocation(Reference.MOD_ID, "andesite"),new ItemStack(Blocks.ANDESITE), 300 * 81, 300 * 81, 300 * 81, 300 * 81);
    }

    public ItemStack output;
    public long waterNeed;
    public long lavaNeed;
    public long waterConsume;
    public long lavaConsume;

    public StoneWorkGenerateRecipe(ResourceLocation resourceLocation, ItemStack output, long waterNeed, long lavaNeed, long waterConsume, long lavaConsume) {
        super(resourceLocation);
        this.output = output;
        this.waterNeed = waterNeed;
        this.lavaNeed = lavaNeed;
        this.waterConsume = waterConsume;
        this.lavaConsume = lavaConsume;
        RECIPES.add(this);
    }

    public StoneWorkGenerateRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(Container inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }

    public boolean canIncrease(FluidTank fluidTank, FluidTank fluidTank2){
        return fluidTank.getFluidAmount() >= waterNeed && fluidTank2.getFluidAmount() >= lavaNeed;
    }

    public void consume(FluidTankComponent fluidTank, FluidTankComponent fluidTank2){
        Transaction transaction = Transaction.openOuter();
        fluidTank.extract(FluidVariant.of(Fluids.WATER), waterConsume, transaction);
        fluidTank2.extract(FluidVariant.of(Fluids.LAVA), lavaConsume, transaction);
        transaction.commit();
    }

}
