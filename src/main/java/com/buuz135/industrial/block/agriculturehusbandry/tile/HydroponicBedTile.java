package com.buuz135.industrial.block.agriculturehusbandry.tile;

import com.buuz135.industrial.api.plant.PlantRecollectable;
import com.buuz135.industrial.block.tile.IndustrialAreaWorkingTile;
import com.buuz135.industrial.block.tile.IndustrialWorkingTile;
import com.buuz135.industrial.config.machine.agriculturehusbandry.AnimalRancherConfig;
import com.buuz135.industrial.config.machine.resourceproduction.HydroponicBedConfig;
import com.buuz135.industrial.module.ModuleAgricultureHusbandry;
import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.registry.IFRegistries;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class HydroponicBedTile extends IndustrialWorkingTile<HydroponicBedTile> {

    @Save
    private SidedFluidTankComponent<HydroponicBedTile> water;
    @Save
    private SidedFluidTankComponent<HydroponicBedTile> ether;
    @Save
    private ProgressBarComponent<HydroponicBedTile> etherBuffer;
    @Save
    private SidedInventoryComponent<HydroponicBedTile> output;

    public HydroponicBedTile(BlockPos blockPos, BlockState blockState) {
        super(ModuleAgricultureHusbandry.HYDROPONIC_BED, HydroponicBedConfig.powerPerOperation, blockPos, blockState);
        addTank(this.water = (SidedFluidTankComponent<HydroponicBedTile>) new SidedFluidTankComponent<HydroponicBedTile>("water", 1000 ,43, 20, 0)
                .setColor(DyeColor.BLUE)
                .setTankType(FluidTankComponent.Type.SMALL)
                .setTankAction(FluidTankComponent.Action.FILL)
                .setValidator(fluidStack -> fluidStack.getFluid().isSame(Fluids.WATER) ||  fluidStack.getFluid().isSame(Fluids.LAVA))
        );
        addTank(this.ether = (SidedFluidTankComponent<HydroponicBedTile>) new SidedFluidTankComponent<HydroponicBedTile>("ether", 10,43, 57, 1)
                .setColor(DyeColor.CYAN)
                .setTankType(FluidTankComponent.Type.SMALL)
                .setTankAction(FluidTankComponent.Action.FILL)
                .setValidator(fluidStack -> fluidStack.getFluid().isSame(ModuleCore.ETHER.getSourceFluid().get()))
        );
        addProgressBar(this.etherBuffer = new ProgressBarComponent<HydroponicBedTile>(63, 20, 200)
                .setColor(DyeColor.CYAN)
                .setCanReset(hydroponicBedTile -> false)
        );
        addInventory(this.output = (SidedInventoryComponent<HydroponicBedTile>) new SidedInventoryComponent<HydroponicBedTile>("output",79,22, 5*3, 2)
                .setColor(DyeColor.ORANGE)
                .setRange(5, 3)
                .setInputFilter((stack, integer) -> false)
        );
    }

    @Override
    public WorkAction work() {
        if (this.etherBuffer.getProgress() <= 0 && this.ether.getFluidAmount() > 0){
            this.ether.drainForced(1, IFluidHandler.FluidAction.EXECUTE);
            this.etherBuffer.setProgress(this.etherBuffer.getMaxProgress());
        }
        if (hasEnergy(1000)){
            BlockPos up = this.worldPosition.above();
            BlockState state = this.level.getBlockState(up);
            Block block = state.getBlock();
            if (!this.level.isEmptyBlock(up) && this.water.getFluidAmount() >= 10){
                if (block instanceof IPlantable && ((IPlantable) block).getPlantType(this.level, up) == PlantType.NETHER && !this.water.getFluid().getFluid().isSame(Fluids.LAVA))
                    return new WorkAction(1, 0);
                if (state.getBlock() instanceof BonemealableBlock){
                    BonemealableBlock growable = (BonemealableBlock) this.level.getBlockState(up).getBlock();
                    if (growable.isValidBonemealTarget(this.level, up, this.level.getBlockState(up), false)){
                        if (this.etherBuffer.getProgress() > 0){
                            growable.performBonemeal((ServerLevel) this.level, this.level.random, up, this.level.getBlockState(up));
                            this.etherBuffer.setProgress(this.etherBuffer.getProgress() - 1);
                        } else {
                            for (int i = 0; i < 4; i++) {
                                this.level.getBlockState(up).randomTick((ServerLevel) this.level,up, this.level.random);
                            }
                        }
                        this.water.drainForced(10, IFluidHandler.FluidAction.EXECUTE);
                        return new WorkAction(1, HydroponicBedConfig.powerPerOperation);
                    } else if (this.etherBuffer.getProgress() > 0){
                        tryToHarvestAndReplant(this.level, up, state, this.output, this.etherBuffer, this);
                        return new WorkAction(1, HydroponicBedConfig.powerPerOperation);
                    }
                } else {
                    if (!tryToHarvestAndReplant(this.level, up, state, this.output, this.etherBuffer, this)){
                        if (this.etherBuffer.getProgress() > 0){
                            for (int i = 0; i < 10; i++) {
                                this.level.getBlockState(up).randomTick((ServerLevel) this.level,up, this.level.random);
                            }
                            this.etherBuffer.setProgress(this.etherBuffer.getProgress() - 1);
                        } else {
                            for (int i = 0; i < 4; i++) {
                                this.level.getBlockState(up).randomTick((ServerLevel) this.level,up, this.level.random);
                            }
                        }
                        this.water.drainForced(10, IFluidHandler.FluidAction.EXECUTE);
                    }
                    return new WorkAction(1, HydroponicBedConfig.powerPerOperation);
                }
            }
        }
        return new WorkAction(1,0);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, HydroponicBedTile blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        if (this.level.getGameTime() % 5 == 0) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockEntity tile = level.getBlockEntity(worldPosition.relative(direction));
                if (tile instanceof HydroponicBedTile) {
                    int difference = water.getFluidAmount() - ((HydroponicBedTile) tile).getWater().getFluidAmount();
                    if (difference > 0 && (water.getFluid().isFluidEqual(((HydroponicBedTile) tile).getWater().getFluid()) || ((HydroponicBedTile) tile).getWater().isEmpty())) {
                        if (difference <= 25) difference = difference / 2;
                        else difference = 25;
                        if (water.getFluidAmount() >= difference) {
                            water.drainForced(((HydroponicBedTile) tile).getWater().fill(new FluidStack(Fluids.WATER, water.drainForced(difference, IFluidHandler.FluidAction.SIMULATE).getAmount()), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                    difference = ether.getFluidAmount() - ((HydroponicBedTile) tile).getEther().getFluidAmount();
                    if (difference > 0) {
                        difference = 1;
                        if (ether.getFluidAmount() >= difference) {
                            ether.drainForced(((HydroponicBedTile) tile).getEther().fill(new FluidStack(ModuleCore.ETHER.getSourceFluid().get(), ether.drainForced(difference, IFluidHandler.FluidAction.SIMULATE).getAmount()), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                    difference = getEnergyStorage().getAmount() - ((HydroponicBedTile) tile).getEnergyStorage().getAmount();
                    if (difference > 0) {
                        if (difference <= 1000 && difference > 1) difference = difference / 2;
                        if (difference > 1000) difference = 1000;
                        if (getEnergyStorage().getAmount() >= difference) {
                            getEnergyStorage().extractEnergy(((HydroponicBedTile) tile).getEnergyStorage().receiveEnergy(difference, false), false);
                        }
                    }
                }
            }
        }
    }

    public SidedFluidTankComponent<HydroponicBedTile> getWater() {
        return water;
    }

    public SidedFluidTankComponent<HydroponicBedTile> getEther() {
        return ether;
    }

    public static boolean tryToHarvestAndReplant(Level level, BlockPos up, BlockState state, IItemHandler output, ProgressBarComponent<?> etherBuffer, IndustrialWorkingTile tile){
        Optional<PlantRecollectable> optional = IFRegistries.PLANT_RECOLLECTABLE.stream().filter(plantRecollectable -> plantRecollectable.canBeHarvested(level, up, state)).findFirst();
        if (optional.isPresent()) {
            List<ItemStack> drops = optional.get().doHarvestOperation(level, up, state);
            if (level.isEmptyBlock(up)){
                for (ItemStack drop : drops) {
                    if (drop.getItem() instanceof IPlantable){
                        level.setBlockAndUpdate(up, ((IPlantable) drop.getItem()).getPlant(level, up));
                        drop.shrink(1);
                        break;
                    } else if (drop.getItem() instanceof BlockItem && ((BlockItem) drop.getItem()).getBlock() instanceof IPlantable){
                        level.setBlockAndUpdate(up, ((IPlantable) ((BlockItem) drop.getItem()).getBlock()).getPlant(level, up));
                        drop.shrink(1);
                        break;
                    }
                }
            }
            drops.forEach(stack -> ItemHandlerHelper.insertItem(output, stack, false));
            if (tile instanceof IndustrialAreaWorkingTile<?> && optional.get().shouldCheckNextPlant(level, up, level.getBlockState(up))) {
                ((IndustrialAreaWorkingTile<?>) tile).increasePointer();
            }
            etherBuffer.setProgress(etherBuffer.getProgress() - 1);
            return true;
        }
        return false;
    }

    @Override
    public int getMaxProgress() {
        return HydroponicBedConfig.maxProgress;
    }

    @Nonnull
    @Override
    public HydroponicBedTile getSelf() {
        return this;
    }

    @Override
    protected EnergyStorageComponent<HydroponicBedTile> createEnergyStorage() {
        return new EnergyStorageComponent<>(HydroponicBedConfig.maxStoredPower, 10, 20);
    }
}
