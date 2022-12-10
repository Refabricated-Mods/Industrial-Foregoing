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

package com.buuz135.industrial.proxy;

import com.buuz135.industrial.api.straw.StrawHandler;
import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.registry.IFRegistries;
import com.buuz135.industrial.utils.Reference;
import com.buuz135.industrial.utils.apihandlers.straw.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;


public class StrawRegistry {

    public static void register() {

        registerStrawHandler("water", new WaterStrawHandler());
        registerStrawHandler("lava", new LavaStrawHandler());
        registerStrawHandler("milk", new MilkStrawHandler());
        registerStrawHandler("essence", new EssenceStrawHandler());
        registerStrawHandler("biofuel", new PotionStrawHandler(() -> ModuleCore.BIOFUEL.getSourceFluid())
                .addPotion(MobEffects.MOVEMENT_SPEED, 800, 0)
                .addPotion(MobEffects.DIG_SPEED, 800, 0));
        registerStrawHandler("sludge", new PotionStrawHandler(() -> ModuleCore.SLUDGE.getSourceFluid())
                .addPotion(MobEffects.WITHER, 600, 0)
                .addPotion(MobEffects.BLINDNESS, 1000, 0)
                .addPotion(MobEffects.MOVEMENT_SLOWDOWN, 1200, 1));
        registerStrawHandler("sewage", new PotionStrawHandler(() -> ModuleCore.SEWAGE.getSourceFluid())
                .addPotion(MobEffects.CONFUSION, 1200, 0)
                .addPotion(MobEffects.MOVEMENT_SLOWDOWN, 1200, 0));
        registerStrawHandler("meat", new PotionStrawHandler(() -> ModuleCore.MEAT.getSourceFluid())
                .addPotion(MobEffects.ABSORPTION, 100, 2)
                .addPotion(MobEffects.SATURATION, 300, 2));
        /*registerStrawHandler("protein", new PotionStrawHandler(PROTEIN)
                .addPotion(Effects.ABSORPTION, 100, 3)
                .addPotion(Effects.SATURATION, 300, 3));*/
        registerStrawHandler("latex", new PotionStrawHandler(() -> ModuleCore.LATEX.getSourceFluid())
                .addPotion(MobEffects.POISON, 1000, 2)
                .addPotion(MobEffects.MOVEMENT_SLOWDOWN, 1000, 2));
    }

    private static void registerStrawHandler(String name, StrawHandler handler){
        Registry.register(IFRegistries.STRAW_HANDLER, new ResourceLocation(Reference.MOD_ID, name), handler);
    }
}