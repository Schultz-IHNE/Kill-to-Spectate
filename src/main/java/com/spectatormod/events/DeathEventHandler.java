package com.spectatormod.events;

import com.spectatormod.SpectatorMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpectatorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeathEventHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        if (!(event.getEntity() instanceof ServerPlayer victim)) return;

        GameType gameType = victim.gameMode.getGameModeForPlayer();
        if (gameType != GameType.SURVIVAL && gameType != GameType.ADVENTURE) return;

        DamageSource source = event.getSource();
        Entity killer = source.getEntity();
        if (!(killer instanceof ServerPlayer)) return;

        event.setCanceled(true);
        victim.setHealth(victim.getMaxHealth());
        victim.setGameMode(GameType.SPECTATOR);

        SpectatorMod.LOGGER.info(
            "Player {} was killed by {} - switched to spectator mode.",
            victim.getName().getString(),
            killer.getName().getString()
        );
    }
}
