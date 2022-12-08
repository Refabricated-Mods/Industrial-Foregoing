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

package com.buuz135.industrial.api.conveyor;

import com.buuz135.industrial.api.IBlockContainer;
import com.buuz135.industrial.api.conveyor.gui.IGuiComponent;
import io.github.fabricators_of_create.porting_lib.util.INBTSerializable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class ConveyorUpgrade implements INBTSerializable<CompoundTag> {

    private IBlockContainer container;
    private ConveyorUpgradeFactory factory;
    private Direction side;

    public ConveyorUpgrade(IBlockContainer container, ConveyorUpgradeFactory factory, Direction side) {
        this.container = container;
        this.factory = factory;
        this.side = side;
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }

    public boolean onUpgradeActivated(Player player, InteractionHand hand) {
        return false;
    }

    public Collection<ItemStack> getDrops() {
        return Collections.singleton(new ItemStack(this.getFactory().getUpgradeItem(), 1));
    }

    public IBlockContainer getContainer() {
        return container;
    }

    public Level getWorld() {
        return getContainer().getBlockWorld();
    }

    public BlockPos getPos() {
        return getContainer().getBlockPosition();
    }

    public ConveyorUpgradeFactory getFactory() {
        return factory;
    }

    public Direction getSide() {
        return side;
    }

    public void update() {

    }

    public void handleEntity(Entity entity) {

    }

    public void onUpgradeRemoved() {

    }

    public int getRedstoneOutput() {
        return 0;
    }

    public VoxelShape getBoundingBox() {
        return Shapes.empty();
    }

    public boolean hasGui() {
        return false;
    }

    public void handleButtonInteraction(int buttonId, CompoundTag compound) {

    }

    public void addComponentsToGui(List<IGuiComponent> componentList) {
    }

    public boolean ignoresCollision(){
        return false;
    }
}