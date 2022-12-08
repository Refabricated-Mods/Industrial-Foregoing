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

package com.buuz135.industrial.block.tile;


import javax.annotation.Nonnull;

import com.buuz135.industrial.capability.tile.BigEnergyHandler;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.GeneratorTile;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

public abstract class IndustrialGeneratorTile<T extends IndustrialGeneratorTile<T>> extends GeneratorTile<T> {

    public IndustrialGeneratorTile(Pair<Block, BlockEntityType<?>> basicTileBlock, BlockPos blockPos, BlockState blockState) {
        super((BasicTileBlock<T>) basicTileBlock.getLeft(), basicTileBlock.getRight(), blockPos, blockState);
    }

    @Override
    public InteractionResult onActivated(Player playerIn, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(playerIn, hand, facing, hitX, hitY, hitZ) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }
        openGui(playerIn);
        return InteractionResult.SUCCESS;
    }

    @Nonnull
    @Override
    protected EnergyStorageComponent<T> createEnergyStorage() {
        return new BigEnergyHandler<T>(getEnergyCapacity(), 0, getExtractingEnergy(), 10, 20) {
            @Override
            public void sync() {
                IndustrialGeneratorTile.this.syncObject(getEnergyStorage());
            }
        };
    }

}
