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
package com.buuz135.industrial;

import com.buuz135.industrial.module.*;
import com.buuz135.industrial.proxy.CommonProxy;
import com.buuz135.industrial.proxy.client.ClientProxy;
import com.buuz135.industrial.proxy.client.render.TransporterTESR;
import com.buuz135.industrial.proxy.network.*;
import com.buuz135.industrial.recipe.*;
import com.buuz135.industrial.registry.IFRegistries;
import com.buuz135.industrial.utils.IFFakePlayer;
import com.buuz135.industrial.utils.Reference;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.network.locator.PlayerInventoryFinder;
import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardGiver;
import com.hrznstudio.titanium.reward.RewardManager;
import dev.cafeteria.fakeplayerapi.server.FakePlayerBuilder;
import dev.cafeteria.fakeplayerapi.server.FakeServerPlayer;
import io.github.tropheusj.milk.Milk;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.NewRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class IndustrialForegoing extends ModuleController {

    private static CommonProxy proxy;
    private static final Map<ServerLevel, Map<String, FakeServerPlayer>> FAKE_PLAYER_MAP = new HashMap<>();
    private static final FakePlayerBuilder FAKE_PLAYER_BUILDER = new FakePlayerBuilder(new ResourceLocation(Reference.MOD_ID, "fake_player"), (IFFakePlayer::new));
    public static NetworkHandler NETWORK = new NetworkHandler(Reference.MOD_ID);
    public static Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);
    public static IndustrialForegoing INSTANCE;

    public IndustrialForegoing() {
        super(Reference.MOD_ID);
        proxy = new CommonProxy();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> EventManager.mod(FMLClientSetupEvent.class).process(fmlClientSetupEvent -> new ClientProxy().run()).subscribe());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> EventManager.mod(ModelRegistryEvent.class).process(modelRegistryEvent -> ForgeModelBakery.addSpecialModel(new ResourceLocation(Reference.MOD_ID, "block/catears"))).subscribe());
        EventManager.mod(FMLCommonSetupEvent.class).process(fmlCommonSetupEvent -> proxy.run()).subscribe();
        EventManager.forge(ServerStartingEvent.class).process(fmlServerStartingEvent -> worldFakePlayer.clear()).subscribe();

        /*
        EventManager.forge(ItemTooltipEvent.class).process(itemTooltipEvent -> ForgeRegistries.ITEMS.tags().getReverseTag(itemTooltipEvent.getItemStack().getItem()).ifPresent(itemIReverseTag -> {
            itemIReverseTag.getTagKeys().forEach(itemTagKey -> itemTooltipEvent.getToolTip().add(new TextComponent(itemTagKey.location().toString())));
        })).subscribe();*/
        RewardGiver giver = RewardManager.get().getGiver(UUID.fromString("d28b7061-fb92-4064-90fb-7e02b95a72a6"), "Buuz135");
        try {
            giver.addReward(new Reward(new ResourceLocation(Reference.MOD_ID, "cat_ears"), new URL("https://raw.githubusercontent.com/Buuz135/Industrial-Foregoing/master/contributors.json"), () -> dist -> {
            }, new String[]{"normal", "cat", "spooky", "snowy"}));
        } catch (MalformedURLException e) {
            LOGGER.catching(e);
        }
        LaserDrillRarity.init();
        PlayerInventoryFinder.init();
    }

    public static FakeServerPlayer getFakePlayer(Level world, String name) {
        if (world instanceof ServerLevel serverLevel) {
            return FAKE_PLAYER_MAP.computeIfAbsent(serverLevel, s->  new HashMap<>()).computeIfAbsent(name, n -> FAKE_PLAYER_BUILDER.create(world.getServer(), serverLevel, name));
        }
        return null;
    }

    public static FakeServerPlayer getFakePlayer(Level world, BlockPos pos, String name) {
        FakeServerPlayer player = getFakePlayer(world, name);
        if (player != null) player.absMoveTo(pos.getX(), pos.getY(), pos.getZ(), 90, 90);
        return player;
    }

    @Override
    public void onPreInit() {
        super.onPreInit();
        NETWORK.registerMessage(ConveyorButtonInteractMessage.class);
        NETWORK.registerMessage(ConveyorSplittingSyncEntityMessage.class);
        NETWORK.registerMessage(SpecialParticleMessage.class);
        NETWORK.registerMessage(BackpackSyncMessage.class);
        NETWORK.registerMessage(BackpackOpenMessage.class);
        NETWORK.registerMessage(BackpackOpenedMessage.class);
        NETWORK.registerMessage(TransporterSyncMessage.class);
        NETWORK.registerMessage(TransporterButtonInteractMessage.class);
        NETWORK.registerMessage(PlungerPlayerHitMessage.class);
    }

    @Override
    protected void initModules() {
        INSTANCE = this;
        IFRegistries.init();
        getRegistries().registerGenericRecipeSerializers(FluidExtractorRecipe.SERIALIZER, DissolutionChamberRecipe.SERIALIZER, LaserDrillOreRecipe.SERIALIZER, LaserDrillFluidRecipe.SERIALIZER, StoneWorkGenerateRecipe.SERIALIZER, CrusherRecipe.SERIALIZER);
        Milk.enableMilkFluid();
        new ModuleCore().generateFeatures(getRegistries());
        new ModuleTool().generateFeatures(getRegistries());
        new ModuleTransportStorage().generateFeatures(getRegistries());
        new ModuleGenerator().generateFeatures(getRegistries());
        new ModuleAgricultureHusbandry().generateFeatures(getRegistries());
        new ModuleResourceProduction().generateFeatures(getRegistries());
        new ModuleMisc().generateFeatures(getRegistries());
    }

    @Environment(EnvType.CLIENT)
    private void initClient(){
        EventManager.mod(TextureStitchEvent.Pre.class).process(pre -> {
            if (pre.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)){
                pre.addSprite(TransporterTESR.TEXTURE);
            }
        }).subscribe();
        EventManager.mod(ModelBakeEvent.class).process(event -> {
            ClientProxy.ears_baked = event.getModelRegistry().get(new ResourceLocation(Reference.MOD_ID, "block/catears"));
        }).subscribe();
    }
}
