package net.lonk.agartha;

import com.mojang.logging.LogUtils;
import net.lonk.agartha.block.ModBlocks;
import net.lonk.agartha.entity.ModEntities;
import net.lonk.agartha.entity.custom.YakubEntity;
import net.lonk.agartha.item.ModItems;
import net.lonk.agartha.sound.ModSounds;
import net.lonk.agartha.sound.custom.YakubAmbientSound;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.Map;
import java.util.WeakHashMap;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AgarthaMod.MODID)
public class AgarthaMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "agartha";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final GameRules.Key<GameRules.IntegerValue> YAKUB_SPAWN_TIME_RULE = GameRules.register("yakubSpawnTime", GameRules.Category.MOBS, GameRules.IntegerValue.create(2400));
    public static final GameRules.Key<GameRules.IntegerValue> YAKUB_DESPAWN_TIME_RULE = GameRules.register("yakubDespawnTime", GameRules.Category.MOBS, GameRules.IntegerValue.create(6000));

    public AgarthaMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register the Deferred Registers
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        ModEntities.ENTITY_TYPES.register(modEventBus);
        modEventBus.register(ModEntities.class);

        ModSounds.SOUND_EVENTS.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Does common setup come from a land down under?");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.WHITE_MONSTER);
        }

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.YAKUB_SPAWN_EGG);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Does the server come from a land down under?");
    }
}
