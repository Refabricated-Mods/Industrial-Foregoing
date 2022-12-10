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
package com.buuz135.industrial.block.agriculturehusbandry.tile;

import com.buuz135.industrial.IndustrialForegoing;
import com.buuz135.industrial.block.tile.IndustrialAreaWorkingTile;
import com.buuz135.industrial.block.tile.RangeManager;
import com.buuz135.industrial.config.machine.agriculturehusbandry.MobCrusherConfig;
import com.buuz135.industrial.item.addon.RangeAddonItem;
import com.buuz135.industrial.module.ModuleAgricultureHusbandry;
import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.utils.IndustrialTags;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.screen.addon.StateButtonInfo;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.item.AugmentWrapper;
import com.hrznstudio.titanium.util.LangUtil;
import dev.cafeteria.fakeplayerapi.server.FakeServerPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MobCrusherTile extends IndustrialAreaWorkingTile<MobCrusherTile> {

    @Save
    private SidedInventoryComponent<MobCrusherTile> output;
    @Save
    private SidedFluidTankComponent<MobCrusherTile> tank;
    @Save
    private boolean dropXP;
    private ButtonComponent buttonComponent;

    public MobCrusherTile(BlockPos blockPos, BlockState blockState) {
        super(ModuleAgricultureHusbandry.MOB_CRUSHER, RangeManager.RangeType.BEHIND, true, MobCrusherConfig.powerPerOperation, blockPos, blockState);
        this.dropXP = true;
        this.addTank(tank = (SidedFluidTankComponent<MobCrusherTile>) new SidedFluidTankComponent<MobCrusherTile>("essence", MobCrusherConfig.tankSize, 43, 20, 0).
                setColor(DyeColor.LIME).
                setTankAction(FluidTankComponent.Action.DRAIN).
                setComponentHarness(this).
                setValidator(fluidStack -> fluidStack.getFluid().is(IndustrialTags.Fluids.EXPERIENCE))
        );
        this.addInventory(output = (SidedInventoryComponent<MobCrusherTile>) new SidedInventoryComponent<MobCrusherTile>("output", 64, 22, 6 * 3, 1).
                setColor(DyeColor.ORANGE).
                setRange(6, 3).
                setInputFilter((stack, integer) -> false).
                setComponentHarness(this)
        );
        this.addButton(buttonComponent = new ButtonComponent(154 - 18 * 2, 84, 14, 14) {
            @Override
            @Environment(EnvType.CLIENT)
            public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
                return Collections.singletonList(() -> new StateButtonAddon(this,
                        new StateButtonInfo(0, AssetTypes.BUTTON_SIDENESS_ENABLED, ChatFormatting.GOLD + LangUtil.getString("tooltip.industrialforegoing.mob_crusher.produce"), "tooltip.industrialforegoing.mob_crusher.produce_extra"),
                        new StateButtonInfo(1, AssetTypes.BUTTON_SIDENESS_DISABLED, ChatFormatting.GOLD + LangUtil.getString("tooltip.industrialforegoing.mob_crusher.consume"), "tooltip.industrialforegoing.mob_crusher.consume_extra_1", "tooltip.industrialforegoing.mob_crusher.consume_extra_2")) {
                    @Override
                    public int getState() {
                        return dropXP ? 0 : 1;
                    }
                });
            }
        }.setPredicate((playerEntity, compoundNBT) -> {
            this.dropXP = !this.dropXP;
            markForUpdate();
        }));
    }

    @Override
    public WorkAction work() {
        if (hasEnergy(MobCrusherConfig.powerPerOperation)) {
            List<Mob> mobs = this.level.getEntitiesOfClass(Mob.class, getWorkingArea().bounds()).stream().filter(mobEntity -> !(mobEntity instanceof Animal &&
            mobEntity.isBaby()) && !mobEntity.isInvulnerable() && !(mobEntity instanceof WitherBoss && ((WitherBoss) mobEntity).getInvulnerableTicks() > 0)).filter(LivingEntity::isAlive).collect(Collectors.toList());
            if (mobs.size() > 0) {
                Mob entity = mobs.get(0);
                FakeServerPlayer player = IndustrialForegoing.getFakePlayer(level, "mob_crusher");
                if (entity.getType().is(IndustrialTags.EntityTypes.MOB_CRUSHER_INSTANT_KILL_BLACKLIST)){
                    return damage(entity, player);
                } else {
                    return instantKill(entity, player);
                }
            }
        }
        return new WorkAction(1, 0);
    }

    private WorkAction instantKill(Mob entity, FakeServerPlayer player){
        int experience = entity.getExperienceReward(player);
        int looting = 0;
        if (!dropXP) {
            looting = this.level.random.nextInt(4);
            ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
            EnchantmentHelper.setEnchantments(Collections.singletonMap(Enchantments.MOB_LOOTING, looting), sword);
            player.setItemInHand(InteractionHand.MAIN_HAND, sword);
        }
        //Loot from table
        DamageSource source = DamageSource.playerAttack(player);
        LootTable table = this.level.getServer().getLootTables().get(entity.getLootTable());
        LootContext.Builder context = new LootContext.Builder((ServerLevel) this.level)
                .withRandom(this.level.random)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                .withParameter(LootContextParams.ORIGIN, new Vec3(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ()))
                .withParameter(LootContextParams.KILLER_ENTITY, player)
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, player);
        table.getRandomItems(context.create(LootContextParamSets.ENTITY)).forEach(stack -> ItemHandlerHelper.insertItem(this.output, stack, false));
        List<ItemEntity> extra = new ArrayList<>();
        //Drop special items
        if (entity.captureDrops() == null) entity.captureDrops(new ArrayList<>());
        entity.dropCustomDeathLoot(source, looting, true);
        if (entity.captureDrops() != null) {
            extra.addAll(entity.captureDrops());
        }
        ForgeHooks.onLivingDrops(entity, source, extra, looting, true);
        extra.forEach(itemEntity -> {
            ItemHandlerHelper.insertItem(this.output, itemEntity.getItem(), false);
            itemEntity.remove(Entity.RemovalReason.KILLED);
        });
        if (dropXP) {
            Transaction transaction = Transaction.openOuter();
            this.tank.insert(FluidVariant.of(ModuleCore.ESSENCE.getSourceFluid()), experience * 20 * 81L, transaction);
            transaction.commit();
        }
        entity.setHealth(0);
        entity.remove(Entity.RemovalReason.KILLED);
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        return new WorkAction(0.1f, MobCrusherConfig.powerPerOperation);
    }

    private WorkAction damage(Mob entity, FakeServerPlayer player){
        entity.hurt((DamageSource.playerAttack(player)).setMagic(), MobCrusherConfig.attackDamage);
        return new WorkAction(0.1f, MobCrusherConfig.powerPerOperation);
    }



    public VoxelShape getWorkingArea() {
        return new RangeManager(this.worldPosition, this.getFacingDirection(), RangeManager.RangeType.BEHIND){
            @Override
            public AABB getBox() {
                return super.getBox().expandTowards(0,2, 0);
            }
        }.get(hasAugmentInstalled(RangeAddonItem.RANGE) ? ((int) AugmentWrapper.getType(getInstalledAugments(RangeAddonItem.RANGE).get(0), RangeAddonItem.RANGE) + 1) : 0);
    }

    @Override
    public MobCrusherTile getSelf() {
        return this;
    }

    @Override
    protected EnergyStorageComponent<MobCrusherTile> createEnergyStorage() {
        return new EnergyStorageComponent<>(MobCrusherConfig.maxStoredPower, 10, 20);
    }

    @Override
    public int getMaxProgress() {
        return MobCrusherConfig.maxProgress;
    }
}
