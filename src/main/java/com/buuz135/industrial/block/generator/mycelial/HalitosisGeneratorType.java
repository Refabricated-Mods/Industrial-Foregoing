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

import com.buuz135.industrial.plugin.jei.generator.MycelialGeneratorRecipe;
import com.buuz135.industrial.utils.IndustrialTags;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import io.github.fabricators_of_create.porting_lib.util.INBTSerializable;
import net.minecraft.world.level.block.Blocks;
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

public class HalitosisGeneratorType implements IMycelialGeneratorType{
    @Override
    public String getName() {
        return "halitosis";
    }

    @Override
    public Input[] getInputs() {
        return new Input[]{Input.SLOT};
    }

    @Override
    public List<BiPredicate<ItemStack, Integer>> getSlotInputPredicates() {
        return Arrays.asList((stack, integer) -> stack.getItem() == Items.DRAGON_BREATH);
    }

    @Override
    public List<Predicate<FluidStack>> getTankInputPredicates() {
        return new ArrayList<>();
    }

    @Override
    public boolean canStart(INBTSerializable<CompoundTag>[] inputs) {
        return inputs.length > 0 && inputs[0] instanceof SidedInventoryComponent && ((SidedInventoryComponent<?>) inputs[0]).getStackInSlot(0).getCount() > 0;
    }

    @Override
    public Pair<Integer, Integer> getTimeAndPowerGeneration(INBTSerializable<CompoundTag>[] inputs) {
        if (inputs.length > 0 && inputs[0] instanceof SidedInventoryComponent && ((SidedInventoryComponent<?>) inputs[0]).getStackInSlot(0).getCount() > 0){
            ItemStack stack = ((SidedInventoryComponent<?>) inputs[0]).getStackInSlot(0).copy();
            ((SidedInventoryComponent<?>) inputs[0]).getStackInSlot(0).shrink(1);
            return calculate(stack);
        }
        return Pair.of(0,80);
    }

    @Override
    public DyeColor[] getInputColors() {
        return new DyeColor[]{DyeColor.MAGENTA};
    }

    @Override
    public Item getDisplay() {
        return Items.DRAGON_BREATH;
    }

    @Override
    public int getSlotSize() {
        return 64;
    }

    @Override
    public List<MycelialGeneratorRecipe> getRecipes() {
        List<MycelialGeneratorRecipe> recipes = new ArrayList<>();
        for (Item item : new Item[]{Items.DRAGON_BREATH}) {
            ItemStack stack = new ItemStack(item);
            Pair<Integer, Integer> power = calculate(stack);
            recipes.add(new MycelialGeneratorRecipe(Collections.singletonList(Collections.singletonList(Ingredient.of(stack))), new ArrayList<>(), power.getLeft(), power.getRight()));
        }
        return recipes;
    }

    private Pair<Integer, Integer> calculate(ItemStack stack){
        return Pair.of(10*60*20, 40);
    }

    @Override
    public ShapedRecipeBuilder addIngredients(ShapedRecipeBuilder recipeBuilder) {
        recipeBuilder = recipeBuilder.define('B', Blocks.PURPUR_BLOCK)
                .define('C', Blocks.END_ROD)
                .define('M', IndustrialTags.Items.MACHINE_FRAME_SUPREME);
        return recipeBuilder;
    }

}
