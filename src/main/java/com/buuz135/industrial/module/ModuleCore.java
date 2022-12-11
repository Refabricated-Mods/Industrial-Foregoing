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

import com.buuz135.industrial.api.straw.StrawHandler;
import com.buuz135.industrial.block.MachineFrameBlock;
import com.buuz135.industrial.block.core.DarkGlassBlock;
import com.buuz135.industrial.block.core.DissolutionChamberBlock;
import com.buuz135.industrial.block.core.FluidExtractorBlock;
import com.buuz135.industrial.block.core.LatexProcessingUnitBlock;
import com.buuz135.industrial.block.core.tile.FluidExtractorTile;
import com.buuz135.industrial.fluid.OreFluidInstance;
import com.buuz135.industrial.item.FertilizerItem;
import com.buuz135.industrial.item.ItemStraw;
import com.buuz135.industrial.item.LaserLensItem;
import com.buuz135.industrial.item.RecipelessCustomItem;
import com.buuz135.industrial.item.addon.EfficiencyAddonItem;
import com.buuz135.industrial.item.addon.ProcessingAddonItem;
import com.buuz135.industrial.item.addon.RangeAddonItem;
import com.buuz135.industrial.item.addon.SpeedAddonItem;
import com.buuz135.industrial.proxy.StrawRegistry;
import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.fluid.TitaniumFluidInstance;
import com.hrznstudio.titanium.module.RegistryHelper;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import io.github.fabricators_of_create.porting_lib.util.FluidAttributes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.tuple.Pair;

public class ModuleCore implements IModule {

    public static Rarity PITY_RARITY;
    public static Rarity SIMPLE_RARITY;
    public static Rarity ADVANCED_RARITY;
    public static Rarity SUPREME_RARITY;

    public static AdvancedTitaniumTab TAB_CORE = new AdvancedTitaniumTab(new ResourceLocation(Reference.MOD_ID, "_core"), true);
    public static Item TINY_DRY_RUBBER;
    public static Item DRY_RUBBER;
    public static Item PLASTIC;
    public static Item FERTILIZER;
    public static Item PINK_SLIME_ITEM;
    public static Item PINK_SLIME_INGOT;
    public static Item STRAW;
    public static Block PITY;
    public static Block SIMPLE;
    public static Block ADVANCED;
    public static Block SUPREME;
    public static Pair<Block, BlockEntityType<?>> FLUID_EXTRACTOR;
    public static Pair<Block, BlockEntityType<?>> LATEX_PROCESSING;
    public static Pair<Block, BlockEntityType<?>> DISSOLUTION_CHAMBER;
    public static Item[] RANGE_ADDONS = new Item[12];
    public static Item[] LASER_LENS = new Item[DyeColor.values().length];
    public static Item SPEED_ADDON_1;
    public static Item SPEED_ADDON_2;
    public static Item EFFICIENCY_ADDON_1;
    public static Item EFFICIENCY_ADDON_2;
    public static Item PROCESSING_ADDON_1;
    public static Item PROCESSING_ADDON_2;
    public static Block DARK_GLASS;

    public static TitaniumFluidInstance LATEX;
    public static TitaniumFluidInstance MEAT;
    public static TitaniumFluidInstance SEWAGE;
    public static TitaniumFluidInstance ESSENCE;
    public static TitaniumFluidInstance SLUDGE;
    public static TitaniumFluidInstance PINK_SLIME;
    public static TitaniumFluidInstance BIOFUEL;
    public static TitaniumFluidInstance ETHER;
    public static OreFluidInstance RAW_ORE_MEAT;
    public static OreFluidInstance FERMENTED_ORE_MEAT;

    public static Item IRON_GEAR;
    public static Item GOLD_GEAR;
    public static Item DIAMOND_GEAR;


    @Override
    public void generateFeatures(RegistryHelper helper) {
        PITY_RARITY = Rarity.create("pity", ChatFormatting.GREEN);
        SIMPLE_RARITY = Rarity.create("simple", ChatFormatting.AQUA);
        ADVANCED_RARITY = Rarity.create("advanced", ChatFormatting.LIGHT_PURPLE);
        SUPREME_RARITY = Rarity.create("supreme", ChatFormatting.GOLD);
        PITY = helper.registerBlockWithItem("machine_frame_pity", () ->  new MachineFrameBlock(PITY_RARITY, TAB_CORE.getTab()), (block) -> () -> new MachineFrameBlock.MachineFrameItem(block.get(), PITY_RARITY, TAB_CORE.getTab()));
        SIMPLE = helper.registerBlockWithItem("machine_frame_simple", () ->  new MachineFrameBlock(SIMPLE_RARITY, TAB_CORE.getTab()),  (block) -> () -> new MachineFrameBlock.MachineFrameItem(block.get(), SIMPLE_RARITY, TAB_CORE.getTab()));
        ADVANCED = helper.registerBlockWithItem("machine_frame_advanced", () ->  new MachineFrameBlock(ADVANCED_RARITY, TAB_CORE.getTab()),  (block) -> () -> new MachineFrameBlock.MachineFrameItem(block.get(), ADVANCED_RARITY, TAB_CORE.getTab()));
        SUPREME = helper.registerBlockWithItem("machine_frame_supreme", () ->  new MachineFrameBlock(SUPREME_RARITY, TAB_CORE.getTab()),  (block) -> () -> new MachineFrameBlock.MachineFrameItem(block.get(), SUPREME_RARITY, TAB_CORE.getTab()));
        ServerTickEvents.END_WORLD_TICK.register((world -> {
            if (world.getGameTime() % 40 == 0 && FluidExtractorTile.EXTRACTION.containsKey(world.dimensionType())){
                FluidExtractorTile.EXTRACTION.get(world.dimensionType()).values().forEach(blockPosFluidExtractionProgressHashMap -> blockPosFluidExtractionProgressHashMap.keySet().forEach(pos -> world.destroyBlockProgress(blockPosFluidExtractionProgressHashMap.get(pos).getBreakID(), pos, blockPosFluidExtractionProgressHashMap.get(pos).getProgress())));
            }
        }));
        StrawRegistry.register();
        for (int i = 0; i < RANGE_ADDONS.length; i++) {
            int finalI = i;
            RANGE_ADDONS[i] = helper.registerGeneric(Registry.ITEM, "range_addon" + i, () -> new RangeAddonItem(finalI, TAB_CORE.getTab()));
        }
        TAB_CORE.addIconStack(() -> new ItemStack(PLASTIC));
        for (DyeColor value : DyeColor.values()) {
            LASER_LENS[value.getId()] = helper.registerGeneric(Registry.ITEM, "laser_lens" + value.getId(), () -> new LaserLensItem(value.getId()));
        }
        TINY_DRY_RUBBER = helper.registerGeneric(Registry.ITEM, "tinydryrubber", () -> new RecipelessCustomItem("tinydryrubber",TAB_CORE.getTab()));
        DRY_RUBBER = helper.registerGeneric(Registry.ITEM, "dryrubber", () -> new RecipelessCustomItem("dryrubber", TAB_CORE.getTab()));
        PLASTIC = helper.registerGeneric(Registry.ITEM, "plastic", () -> new RecipelessCustomItem("plastic", TAB_CORE.getTab()));
        FERTILIZER = helper.registerGeneric(Registry.ITEM, "fertilizer", () -> new FertilizerItem(TAB_CORE.getTab()));
        PINK_SLIME_ITEM = helper.registerGeneric(Registry.ITEM, "pink_slime", () -> new RecipelessCustomItem("pink_slime", TAB_CORE.getTab()));
        PINK_SLIME_INGOT = helper.registerGeneric(Registry.ITEM, "pink_slime_ingot", () -> new RecipelessCustomItem("pink_slime_ingot", TAB_CORE.getTab()));
        STRAW = helper.registerGeneric(Registry.ITEM, "straw", () -> new ItemStraw(TAB_CORE.getTab()));
        FLUID_EXTRACTOR = helper.registerBlockWithTile("fluid_extractor", FluidExtractorBlock::new);
        LATEX_PROCESSING = helper.registerBlockWithTile("latex_processing_unit", LatexProcessingUnitBlock::new);
        DISSOLUTION_CHAMBER = helper.registerBlockWithTile("dissolution_chamber", DissolutionChamberBlock::new);
        SPEED_ADDON_1 = helper.registerGeneric(Registry.ITEM, "speed_addon_1", () -> new SpeedAddonItem(1, TAB_CORE.getTab()));
        SPEED_ADDON_2 = helper.registerGeneric(Registry.ITEM, "speed_addon_2", () -> new SpeedAddonItem(2, TAB_CORE.getTab()));
        EFFICIENCY_ADDON_1 = helper.registerGeneric(Registry.ITEM, "efficiency_addon_1", () -> new EfficiencyAddonItem(1, TAB_CORE.getTab()));
        EFFICIENCY_ADDON_2 = helper.registerGeneric(Registry.ITEM, "efficiency_addon_2", () -> new EfficiencyAddonItem(2, TAB_CORE.getTab()));
        PROCESSING_ADDON_1 = helper.registerGeneric(Registry.ITEM, "processing_addon_1", () -> new ProcessingAddonItem(1, TAB_CORE.getTab()));
        PROCESSING_ADDON_2 = helper.registerGeneric(Registry.ITEM, "processing_addon_2", () -> new ProcessingAddonItem(2, TAB_CORE.getTab()));
        DARK_GLASS = helper.registerBlockWithItem("dark_glass", DarkGlassBlock::new);

        LATEX = new TitaniumFluidInstance(helper, "latex", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/latex_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/latex_flow")),  TAB_CORE.getTab());
        MEAT =  new TitaniumFluidInstance(helper, "meat", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/meat_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/meat_flow")),  TAB_CORE.getTab());
        SEWAGE = new TitaniumFluidInstance(helper, "sewage", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/sewage_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/sewage_flow")),  TAB_CORE.getTab());
        ESSENCE =  new TitaniumFluidInstance(helper, "essence", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/essence_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/essence_flow")),  TAB_CORE.getTab());
        SLUDGE =  new TitaniumFluidInstance(helper, "sludge", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/sludge_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/sludge_flow")),  TAB_CORE.getTab());
        PINK_SLIME = new TitaniumFluidInstance(helper, "pink_slime", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/pink_slime_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/pink_slime_flow")),  TAB_CORE.getTab());
        BIOFUEL = new TitaniumFluidInstance(helper, "biofuel", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/biofuel_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/biofuel_flow")),  TAB_CORE.getTab());
        ETHER =  new TitaniumFluidInstance(helper, "ether_gas", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/ether_gas_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/ether_gas_flow")).gaseous(),  TAB_CORE.getTab());
        RAW_ORE_MEAT = new OreFluidInstance(helper, "raw_ore_meat", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/raw_ore_meat_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/raw_ore_meat_flow")),  TAB_CORE.getTab());
        FERMENTED_ORE_MEAT =new OreFluidInstance(helper, "fermented_ore_meat", FluidAttributes.builder(new ResourceLocation(Reference.MOD_ID, "blocks/fluids/ether_gas_still"), new ResourceLocation(Reference.MOD_ID, "blocks/fluids/ether_gas_flow")),  TAB_CORE.getTab());

        IRON_GEAR = helper.registerGeneric(Registry.ITEM, "iron_gear", () -> new Item(new Item.Properties().tab(TAB_CORE.getTab())));
        GOLD_GEAR = helper.registerGeneric(Registry.ITEM, "gold_gear", () -> new Item(new Item.Properties().tab(TAB_CORE.getTab())));
        DIAMOND_GEAR = helper.registerGeneric(Registry.ITEM, "diamond_gear", () -> new Item(new Item.Properties().tab(TAB_CORE.getTab())));
    }

    /*@Environment(EnvType.CLIENT)
    public void textureStitch(TextureStitchEvent.Pre event) {
        //event.addSprite(LATEX.getSourceFluid().getAttributes().getFlowingTexture()); ??
        //event.addSprite(LATEX.getSourceFluid().getAttributes().getStillTexture());
    }
    @Environment(EnvType.CLIENT)
    public void onClient(){
        EventManager.mod(TextureStitchEvent.Pre.class).process(this::textureStitch).subscribe();
    }*/
}
