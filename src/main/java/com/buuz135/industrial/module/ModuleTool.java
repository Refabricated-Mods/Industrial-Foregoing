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

package com.buuz135.industrial.module;

import com.buuz135.industrial.entity.InfinityLauncherProjectileEntity;
import com.buuz135.industrial.entity.InfinityNukeEntity;
import com.buuz135.industrial.entity.InfinityTridentEntity;
import com.buuz135.industrial.item.MeatFeederItem;
import com.buuz135.industrial.item.MobEssenceToolItem;
import com.buuz135.industrial.item.MobImprisonmentToolItem;
import com.buuz135.industrial.item.infinity.item.*;
import com.buuz135.industrial.utils.BlockUtils;
import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.capability.CapabilityItemStackHolder;
import com.hrznstudio.titanium.itemstack.ItemStackHarness;
import com.hrznstudio.titanium.itemstack.ItemStackHarnessRegistry;
import com.hrznstudio.titanium.module.RegistryHelper;
import com.hrznstudio.titanium.network.IButtonHandler;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class ModuleTool implements IModule {

    public static final SoundEvent NUKE_EXPLOSION = new SoundEvent(new ResourceLocation(Reference.MOD_ID, "nuke_explosion"));
    public static final SoundEvent NUKE_ARMING = new SoundEvent(new ResourceLocation(Reference.MOD_ID, "nuke_arming"));
    public static AdvancedTitaniumTab TAB_TOOL = new AdvancedTitaniumTab(new ResourceLocation(Reference.MOD_ID, "tool"), true);

    public static Item MEAT_FEEDER;
    public static Item MOB_IMPRISONMENT_TOOL;
    public static Item INFINITY_DRILL;
    public static Item MOB_ESSENCE_TOOL;
    public static Item INFINITY_SAW;
    public static Item INFINITY_HAMMER;
    public static Item INFINITY_TRIDENT;
    public static Item INFINITY_BACKPACK;
    public static Item INFINITY_LAUNCHER;
    public static SoundEvent NUKE_CHARGING;

    public static EntityType<?> TRIDENT_ENTITY_TYPE;
    public static EntityType<?> INFINITY_LAUNCHER_PROJECTILE_ENTITY_TYPE;

    public static Item INFINITY_NUKE;
    public static EntityType<?> INFINITY_NUKE_ENTITY_TYPE;

    @Override
    public void generateFeatures(RegistryHelper registryHelper) {
        MEAT_FEEDER  = registryHelper.registerGeneric(Registry.ITEM, "meat_feeder", () -> new MeatFeederItem(TAB_TOOL.getTab()));
        MOB_IMPRISONMENT_TOOL  = registryHelper.registerGeneric(Registry.ITEM, "mob_imprisonment_tool", () -> new MobImprisonmentToolItem(TAB_TOOL.getTab()));
        INFINITY_DRILL  = registryHelper.registerGeneric(Registry.ITEM, "infinity_drill", () -> new ItemInfinityDrill(TAB_TOOL.getTab()));
        //features.add(Feature.builder("mob_essence_tool").content(Registry.ITEM, MOB_ESSENCE_TOOL = new MobEssenceToolItem(TAB_TOOL.getTab())));
        INFINITY_SAW  = registryHelper.registerGeneric(Registry.ITEM, "infinity_saw", () -> new ItemInfinitySaw(TAB_TOOL.getTab()));
        INFINITY_HAMMER  = registryHelper.registerGeneric(Registry.ITEM, "infinity_hammer", () -> new ItemInfinityHammer(TAB_TOOL.getTab()));
        INFINITY_TRIDENT  = registryHelper.registerGeneric(Registry.ITEM, "infinity_trident", () -> new ItemInfinityTrident(TAB_TOOL.getTab()));
        TRIDENT_ENTITY_TYPE =  registryHelper.registerEntityType("trident_entity", () ->  EntityType.Builder.<InfinityTridentEntity>of(InfinityTridentEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)
                .setShouldReceiveVelocityUpdates(true)
                .setCustomClientFactory((spawnEntity, world) -> new InfinityTridentEntity((EntityType<? extends InfinityTridentEntity>) TRIDENT_ENTITY_TYPE, world)).clientTrackingRange(4).updateInterval(20).build("trident_entity"));
        INFINITY_BACKPACK  = registryHelper.registerGeneric(Registry.ITEM, "infinity_backpack", ItemInfinityBackpack::new);
        INFINITY_LAUNCHER  = registryHelper.registerGeneric(Registry.ITEM, "infinity_launcher", () -> new ItemInfinityLauncher(TAB_TOOL.getTab()));
        INFINITY_LAUNCHER_PROJECTILE_ENTITY_TYPE = registryHelper.registerEntityType("launcher_projectile_entity",  () -> EntityType.Builder.<InfinityLauncherProjectileEntity>of(InfinityLauncherProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)
                .setShouldReceiveVelocityUpdates(true)
                .setCustomClientFactory((spawnEntity, world) -> new InfinityLauncherProjectileEntity((EntityType<? extends InfinityLauncherProjectileEntity>) INFINITY_LAUNCHER_PROJECTILE_ENTITY_TYPE, world)).clientTrackingRange(4).updateInterval(20).build("launcher_projectile_entity"));
        INFINITY_NUKE  = registryHelper.registerGeneric(Registry.ITEM, "infinity_nuke", () -> new ItemInfinityNuke(TAB_TOOL.getTab()));
        INFINITY_NUKE_ENTITY_TYPE = registryHelper.registerEntityType("infinity_nuke", () -> EntityType.Builder.<InfinityNukeEntity>of(InfinityNukeEntity::new, MobCategory.MISC).sized(0.5F, 1.5F)
                .setShouldReceiveVelocityUpdates(true)
                .setCustomClientFactory((spawnEntity, world) -> new InfinityNukeEntity((EntityType<? extends InfinityNukeEntity>) INFINITY_NUKE_ENTITY_TYPE, world)).fireImmune().clientTrackingRange(8).updateInterval(20).build("infinity_nuke"));
        NUKE_CHARGING = registryHelper.registerGeneric(Registry.SOUND_EVENT, "nuke_charging", () -> new SoundEvent(new ResourceLocation(Reference.MOD_ID, "nuke_charging")));
        TAB_TOOL.addIconStack(() -> new ItemStack(INFINITY_DRILL));
        ItemStackHarnessRegistry.register(() -> INFINITY_SAW, stack -> new ItemStackHarness(stack, null, (IButtonHandler) stack.getItem(), EnergyStorage.ITEM, FluidStorage.ITEM, CapabilityItemStackHolder.ITEM));
        ItemStackHarnessRegistry.register(() -> INFINITY_DRILL, stack -> new ItemStackHarness(stack, null, (IButtonHandler) stack.getItem(), EnergyStorage.ITEM, FluidStorage.ITEM, CapabilityItemStackHolder.ITEM));
        ItemStackHarnessRegistry.register(() -> INFINITY_HAMMER, stack -> new ItemStackHarness(stack, null, (IButtonHandler) stack.getItem(), EnergyStorage.ITEM, FluidStorage.ITEM, CapabilityItemStackHolder.ITEM));
        ItemStackHarnessRegistry.register(() -> INFINITY_TRIDENT, stack -> new ItemStackHarness(stack, null, (IButtonHandler) stack.getItem(), EnergyStorage.ITEM, FluidStorage.ITEM, CapabilityItemStackHolder.ITEM));
        ItemStackHarnessRegistry.register(() -> INFINITY_TRIDENT, stack -> new ItemStackHarness(stack, null, (IButtonHandler) stack.getItem(), EnergyStorage.ITEM, FluidStorage.ITEM, CapabilityItemStackHolder.ITEM));
        ItemStackHarnessRegistry.register(() -> INFINITY_BACKPACK, stack -> new ItemStackHarness(stack, null, (IButtonHandler) stack.getItem(), EnergyStorage.ITEM, FluidStorage.ITEM, CapabilityItemStackHolder.ITEM));
        ItemStackHarnessRegistry.register(() -> INFINITY_LAUNCHER, stack -> new ItemStackHarness(stack, null, (IButtonHandler) stack.getItem(), EnergyStorage.ITEM, FluidStorage.ITEM, CapabilityItemStackHolder.ITEM));
        ItemStackHarnessRegistry.register(() -> INFINITY_NUKE, stack -> new ItemStackHarness(stack, null, (IButtonHandler) stack.getItem(), EnergyStorage.ITEM, FluidStorage.ITEM, CapabilityItemStackHolder.ITEM));
        Registry.register(Registry.SOUND_EVENT, NUKE_EXPLOSION.getLocation(), NUKE_EXPLOSION);
        Registry.register(Registry.SOUND_EVENT, NUKE_ARMING.getLocation(), NUKE_ARMING);
        BlockEvents.BLOCK_BREAK.register((breakEvent -> {
            if (breakEvent.getPlayer().getMainHandItem().getItem() == INFINITY_SAW && BlockUtils.isLog((Level) breakEvent.getWorld(), breakEvent.getPos())){
                breakEvent.setCanceled(true);
                breakEvent.getPlayer().getMainHandItem().mineBlock((Level) breakEvent.getWorld(), breakEvent.getState(), breakEvent.getPos(), breakEvent.getPlayer());
            }
        }));

    }
}
