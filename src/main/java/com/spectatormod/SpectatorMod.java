package com.spectatormod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(SpectatorMod.MOD_ID)
public class SpectatorMod {

    public static final String MOD_ID = "spectatormod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SpectatorMod() {
        LOGGER.info("SpectatorMod loaded.");
    }
}
