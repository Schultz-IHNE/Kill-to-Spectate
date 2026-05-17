package com.spectatormod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpectatorConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.IntValue MODE;
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("general");

        ENABLED = BUILDER
            .comment("If true, players killed by other players are switched to spectator mode.")
            .define("enabled", true);

        MODE = BUILDER
            .comment(
                "Spectator mode behavior.",
                "1 = death is cancelled, items do not drop, player instantly becomes spectator.",
                "2 = normal death (items drop, death screen), player respawns as spectator."
            )
            .defineInRange("mode", 2, 1, 2);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
