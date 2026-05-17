package com.spectatormod.events;

import com.mojang.brigadier.CommandDispatcher;
import com.spectatormod.SpectatorMod;
import com.spectatormod.config.SpectatorConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpectatorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandHandler {

    private static final String MODE_INSTANT    = "instant";
    private static final String MODE_AFTERDEATH = "afterdeath";

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
            Commands.literal("spectatormod")
                .requires(source -> source.hasPermission(2))

                .then(Commands.literal("on")
                    .executes(ctx -> {
                        SpectatorConfig.ENABLED.set(true);
                        SpectatorConfig.SPEC.save();
                        ctx.getSource().sendSuccess(
                            () -> Component.literal("[SpectatorMod] Enabled."), true);
                        SpectatorMod.LOGGER.info("SpectatorMod enabled via command.");
                        return 1;
                    })
                )

                .then(Commands.literal("off")
                    .executes(ctx -> {
                        SpectatorConfig.ENABLED.set(false);
                        SpectatorConfig.SPEC.save();
                        ctx.getSource().sendSuccess(
                            () -> Component.literal("[SpectatorMod] Disabled."), true);
                        SpectatorMod.LOGGER.info("SpectatorMod disabled via command.");
                        return 1;
                    })
                )

                .then(Commands.literal("mode")
                    .then(Commands.literal(MODE_INSTANT)
                        .executes(ctx -> {
                            SpectatorConfig.MODE.set(1);
                            SpectatorConfig.SPEC.save();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal(
                                    "[SpectatorMod] Mode: instant — death cancelled, no item drop, player becomes spectator immediately."),
                                true);
                            SpectatorMod.LOGGER.info("SpectatorMod mode set to instant.");
                            return 1;
                        })
                    )
                    .then(Commands.literal(MODE_AFTERDEATH)
                        .executes(ctx -> {
                            SpectatorConfig.MODE.set(2);
                            SpectatorConfig.SPEC.save();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal(
                                    "[SpectatorMod] Mode: afterdeath — normal death with item drop, player respawns as spectator."),
                                true);
                            SpectatorMod.LOGGER.info("SpectatorMod mode set to afterdeath.");
                            return 1;
                        })
                    )
                )

                .then(Commands.literal("status")
                    .executes(ctx -> {
                        boolean enabled = SpectatorConfig.ENABLED.get();
                        String state = enabled ? "enabled" : "disabled";
                        String modeName = SpectatorConfig.MODE.get() == 1 ? MODE_INSTANT : MODE_AFTERDEATH;
                        ctx.getSource().sendSuccess(
                            () -> Component.literal("[SpectatorMod] " + state + ", mode: " + modeName + "."), false);
                        return 1;
                    })
                )
        );
    }
}
