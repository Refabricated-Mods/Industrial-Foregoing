package com.buuz135.industrial;

import com.buuz135.industrial.recipe.provider.IndustrialRecipeProvider;
import com.buuz135.industrial.recipe.provider.IndustrialSerializableProvider;
import com.buuz135.industrial.recipe.provider.IndustrialTagsProvider;
import com.buuz135.industrial.utils.Reference;
import com.buuz135.industrial.utils.data.IndustrialBlockstateProvider;
import com.buuz135.industrial.utils.data.IndustrialModelProvider;
import com.hrznstudio.titanium.datagenerator.loot.TitaniumLootTableProvider;
import com.hrznstudio.titanium.datagenerator.model.BlockItemModelGeneratorProvider;
import com.hrznstudio.titanium.util.NonNullLazy;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IndustrialForegoingDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        NonNullLazy<List<Block>> blocksToProcess = NonNullLazy.of(() ->
                Registry.BLOCK
                        .stream()
                        .filter(basicBlock -> Optional.ofNullable(basicBlock.getRegistryName())
                                .map(ResourceLocation::getNamespace)
                                .filter(Reference.MOD_ID::equalsIgnoreCase)
                                .isPresent())
                        .collect(Collectors.toList())
        );
        fabricDataGenerator.addProvider(d -> new IndustrialTagsProvider.Blocks(d, Reference.MOD_ID));
        fabricDataGenerator.addProvider(d -> new IndustrialTagsProvider.Items(d, Reference.MOD_ID));
        fabricDataGenerator.addProvider(d -> new IndustrialRecipeProvider(d, blocksToProcess));
        fabricDataGenerator.addProvider(d -> new IndustrialSerializableProvider(d, Reference.MOD_ID));
        fabricDataGenerator.addProvider(d -> new TitaniumLootTableProvider(d, blocksToProcess));
        fabricDataGenerator.addProvider(d -> new BlockItemModelGeneratorProvider(d, Reference.MOD_ID, blocksToProcess));
        //fabricDataGenerator.addProvider(d -> new IndustrialBlockstateProvider(d, d, blocksToProcess));
        //fabricDataGenerator.addProvider(d -> new IndustrialModelProvider(d, d));
    }
}
