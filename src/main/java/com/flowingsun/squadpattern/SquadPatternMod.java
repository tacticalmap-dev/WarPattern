package com.flowingsun.squadpattern;

import com.flowingsun.squadpattern.config.SquadConfig;
import com.flowingsun.squadpattern.match.SquadMatchService;
import com.flowingsun.squadpattern.net.SquadNetwork;
import com.flowingsun.squadpattern.vp.VictoryMatchManager;
import com.flowingsun.squadpattern.vp.VictoryPointProtection;
import com.flowingsun.squadpattern.vp.VictoryPointRuntime;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Main Forge mod entry point.
 * Wires registries, config specs, network channel, and runtime services.
 */
@Mod(SquadPatternMod.MOD_ID)
public class SquadPatternMod {
    public static final String MOD_ID = "squadpattern";

    public SquadPatternMod() {
        // Register game content first so objects are available before gameplay systems boot.
        VictoryPointRuntime.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCreativeTab);

        // Expose both server/common and client-side config trees.
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, SquadConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, SquadConfig.CLIENT_SPEC);

        SquadNetwork.init();

        // Runtime services are Forge-event driven.
        MinecraftForge.EVENT_BUS.register(SquadMatchService.INSTANCE);
        MinecraftForge.EVENT_BUS.register(VictoryMatchManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new VictoryPointProtection());
    }

    private void onCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(VictoryPointRuntime.VICTORY_POINT_ITEM.get());
            event.accept(VictoryPointRuntime.NORMAL_POINT_ITEM.get());
            event.accept(VictoryPointRuntime.AMMO_POINT_ITEM.get());
            event.accept(VictoryPointRuntime.OIL_POINT_ITEM.get());
        }
    }
}
