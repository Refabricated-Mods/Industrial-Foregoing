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
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class DissolutionChamberRecipe extends SerializableRecipe {

    public static GenericSerializer<DissolutionChamberRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(Reference.MOD_ID, "dissolution_chamber"), DissolutionChamberRecipe.class);
    public static List<DissolutionChamberRecipe> RECIPES = new ArrayList<>();


    public Ingredient.Value[] input;
    public FluidStack inputFluid;
    public int processingTime;
    public ItemStack output;
    public FluidStack outputFluid;

    public DissolutionChamberRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public DissolutionChamberRecipe(ResourceLocation resourceLocation, Ingredient.Value[] input, FluidStack inputFluid, int processingTime, ItemStack output, FluidStack outputFluid) {
        super(resourceLocation);
        this.input = input;
        this.inputFluid = inputFluid;
        this.processingTime = processingTime;
        this.output = output;
        this.output.getItem().onCraftedBy(this.output, null, null);
        this.outputFluid = outputFluid;
        RECIPES.add(this);
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return false;
    }

    public boolean matches(Storage<ItemVariant> handler, FluidTankComponent tank) {
        if (input == null || tank == null || inputFluid == null) return false;
        List<ItemStack> handlerItems = new ArrayList<>();
        for (StorageView<ItemVariant> view : handler.iterable(Transaction.openOuter())){
            if (!view.isResourceBlank()) handlerItems.add(view.getResource().toStack((int) view.getAmount()));
        }
        for (Ingredient.Value iItemList : input) {
            boolean found = false;
            for (ItemStack stack : iItemList.getItems()) {
                int i = 0;
                for (; i < handlerItems.size(); i++) {
                    if (handlerItems.get(i).sameItem(stack)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    handlerItems.remove(i);
                    break;
                }
            }
            if (!found) return false;
        }
        return handlerItems.size() == 0 && tank.simulateExtract(inputFluid.getType(), inputFluid.getAmount(), null) == inputFluid.getAmount();
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
        return output;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }
}
