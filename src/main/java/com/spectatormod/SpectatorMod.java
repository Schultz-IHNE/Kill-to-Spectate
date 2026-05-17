package com.spectatormod;

import com.mojang.logging.LogUtils;
import com.spectatormod.config.SpectatorConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(SpectatorMod.MOD_ID)
public class SpectatorMod {

    public static final String MOD_ID = "spectatormod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SpectatorMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SpectatorConfig.SPEC);
        LOGGER.info("SpectatorMod loaded.");
    }
}
