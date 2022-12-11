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

import com.buuz135.industrial.proxy.event.MeatFeederTickHandler;
import com.buuz135.industrial.utils.explosion.ExplosionTickHandler;
import com.hrznstudio.titanium.event.handler.EventManager;
import dev.cafeteria.fakeplayerapi.server.FakeServerPlayer;
import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.MountEntityCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.ArrayList;
import java.util.List;

public class CommonProxy {

    public static final String CONTRIBUTORS_FILE = "https://raw.githubusercontent.com/Buuz135/Industrial-Foregoing/master/contributors.json";
    public static List<String> CONTRIBUTORS = new ArrayList<>();

    public static DamageSource custom = new DamageSource("if_custom") {
        @Override
        public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            return new TranslatableComponent("text.industrialforegoing.chat.slaughter_kill", entityLivingBaseIn.getDisplayName(), ChatFormatting.RESET);
        }
    };

    public void run() {
        StrawRegistry.register();
        MountEntityCallback.EVENT.register(((mounted, mounting, b) -> {
            if (mounting instanceof FakeServerPlayer) return InteractionResult.FAIL;
            return InteractionResult.PASS;
        }));
        LivingEntityEvents.DROPS.register(((livingEntity, damageSource, collection) -> {
            if (damageSource.equals(custom)) {
                collection.clear();
                return true;
            }
            return false;
        }));

        EventManager.forge(LivingEvent.LivingUpdateEvent.class).process(MeatFeederTickHandler::onTick).subscribe();
        EventManager.forge(TickEvent.ServerTickEvent.class).process(ExplosionTickHandler::serverTick).subscribe();

    }

}
