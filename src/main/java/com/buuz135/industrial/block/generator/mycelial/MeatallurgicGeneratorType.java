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

package com.buuz135.industrial.block.generator.mycelial;

import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.plugin.jei.generator.MycelialGeneratorRecipe;
import com.buuz135.industrial.utils.IndustrialTags;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import io.github.fabricators_of_create.porting_lib.util.INBTSerializable;
import me.alphamode.forgetags.Tags;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MeatallurgicGeneratorType implements IMycelialGeneratorType{

    @Override
    public String getName() {
        return "meatallurgic";
    }

    @Override
    public Input[] getInputs() {
        return new Input[]{Input.TANK, Input.SLOT};
    }

    @Override
    public List<BiPredicate<ItemStack, Integer>> getSlotInputPredicates() {
        return Arrays.asList(null, (stack, slot) -> stack.is(Tags.Items.INGOTS));
    }

    @Override
    public List<Predicate<FluidStack>> getTankInputPredicates() {
        return Arrays.asList(fluidStack -> fluidStack.getFluid().isSame(ModuleCore.MEAT.getSourceFluid()), null);
    }

    @Override
    public boolean canStart(INBTSerializable<CompoundTag>[] inputs) {
        return inputs.length >= 2 && inputs[0] instanceof SidedFluidTankComponent && inputs[1] instanceof SidedInventoryComponent && ((SidedFluidTankComponent<?>) inputs[0]).getFluidAmount() >= 250 && ((SidedInventoryComponent<?>) inputs[1]).getStackInSlot(0).getCount() > 0;
    }

    @Override
    public Pair<Integer, Integer> getTimeAndPowerGeneration(INBTSerializable<CompoundTag>[] inputs) {
        if (inputs.length >= 2 && inputs[0] instanceof SidedFluidTankComponent<?> fluidTankComponent && inputs[1] instanceof SidedInventoryComponent inventoryComponent){
            if (fluidTankComponent.getFluidAmount() >= 250*81 && inventoryComponent.getStackInSlot(0).getCount() > 0){
                Transaction transaction = Transaction.openOuter();
                fluidTankComponent.extract(fluidTankComponent.getResource(), 250*81, transaction);
                inventoryComponent.getStackInSlot(0).shrink(1);
                return Pair.of(20*20, 200);
            }
        }
        return Pair.of(0,80);
    }

    @Override
    public DyeColor[] getInputColors() {
        return new DyeColor[]{DyeColor.BROWN, DyeColor.YELLOW};
    }

    @Override
    public Item getDisplay() {
        return Items.GOLD_INGOT;
    }

    @Override
    public int getSlotSize() {
        return 64;
    }

    @Override
    public List<MycelialGeneratorRecipe> getRecipes() {
        return Collections.singletonList(new MycelialGeneratorRecipe(Arrays.asList(new ArrayList<>(), Collections.singletonList(Ingredient.of(Tags.Items.INGOTS))), Arrays.asList(Collections.singletonList(new FluidStack(ModuleCore.MEAT.getSourceFluid(), 250)), Collections.emptyList()), 20*20, 100));
    }

    @Override
    public ShapedRecipeBuilder addIngredients(ShapedRecipeBuilder recipeBuilder) {
        recipeBuilder = recipeBuilder.define('B', Tags.Items.INGOTS)
                .define('C', ModuleCore.MEAT.getBucketFluid())
                .define('M', IndustrialTags.Items.MACHINE_FRAME_SUPREME);
        return recipeBuilder;
    }
}
