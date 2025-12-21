package net.lonk.agartha.worldgen.dimension;

import net.lonk.agartha.AgarthaMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.OptionalLong;

public class ModDimensions {
    public static final ResourceKey<LevelStem> AGARTHA_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(AgarthaMod.MODID, "agartha"));
    public static final ResourceKey<Level> AGARTHA_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(AgarthaMod.MODID, "agartha"));
    public static final ResourceKey<DimensionType> AGARTHA_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(AgarthaMod.MODID, "agartha_type"));

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(AGARTHA_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkyLight
                true, // hasCeiling
                false, // ultraWarm
                true,  // natural
                1.0, // coordinateScale
                false, // bedWorks
                false, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                1.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)
        ));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        // 1. Define your biome mapping using Climate Parameters
        // Climate.parameters(temp, humidity, continentalness, erosion, depth, weirdness, offset)
        // Values range from -1.0 to 1.0
        MultiNoiseBiomeSource biomeSource = MultiNoiseBiomeSource.createFromList(
                new Climate.ParameterList<>(java.util.List.of(
                        // --- CEILING BIOMES (Negative Depth) ---
                        com.mojang.datafixers.util.Pair.of(
                                // Temp, Humid, Cont, Erosion, DEPTH, Weird, Offset
                                Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, -1F, 0.0F, 0.0F),
                                biomeRegistry.getOrThrow(Biomes.DRIPSTONE_CAVES)
                        ),
                        com.mojang.datafixers.util.Pair.of(
                                Climate.parameters(0.5F, 0.5F, 0.0F, 0.0F, -1F, 0.0F, 0.0F),
                                biomeRegistry.getOrThrow(Biomes.LUSH_CAVES)
                        ),

                        // --- GROUND BIOMES (Positive Depth) ---
                        com.mojang.datafixers.util.Pair.of(
                                Climate.parameters(-0.5F, 0.0F, 0.0F, 0.0F, 1F, 0.0F, 0.0F),
                                biomeRegistry.getOrThrow(Biomes.FROZEN_PEAKS)
                        ),
                        com.mojang.datafixers.util.Pair.of(
                                Climate.parameters(0.5F, 0.2F, 0.0F, 0.0F, 1F, 0.0F, 0.0F),
                                biomeRegistry.getOrThrow(Biomes.MEADOW)
                        )
                ))
        );

        // 2. Create the generator using the MultiNoise source
        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                biomeSource,
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.CAVES)
        );

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.AGARTHA_DIM_TYPE), noiseBasedChunkGenerator);
        context.register(AGARTHA_KEY, stem);
    }
}
