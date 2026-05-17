package com.spectatormod.events;

import com.spectatormod.SpectatorMod;
import com.spectatormod.config.SpectatorConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = SpectatorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeathEventHandler {

    // Players pending spectator mode after respawn (mode 2)
    private static final Set<UUID> pendingSpectator = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!SpectatorConfig.ENABLED.get()) return;
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer victim)) return;

        GameType gameType = victim.gameMode.getGameModeForPlayer();
        if (gameType != GameType.SURVIVAL && gameType != GameType.ADVENTURE) return;

        DamageSource source = event.getSource();
        Entity killer = source.getEntity();
        if (!(killer instanceof ServerPlayer)) return;

        int mode = SpectatorConfig.MODE.get();

        if (mode == 1) {
            event.setCanceled(true);
            victim.setHealth(victim.getMaxHealth());
            victim.setGameMode(GameType.SPECTATOR);
            SpectatorMod.LOGGER.info(
                "Player {} killed by {} - mode 1: switched to spectator instantly.",
                victim.getName().getString(), killer.getName().getString()
            );
        } else {
            pendingSpectator.add(victim.getUUID());
            SpectatorMod.LOGGER.info(
                "Player {} killed by {} - mode 2: will respawn as spectator.",
                victim.getName().getString(), killer.getName().getString()
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!SpectatorConfig.ENABLED.get()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (pendingSpectator.remove(player.getUUID())) {
            player.setGameMode(GameType.SPECTATOR);
            SpectatorMod.LOGGER.info(
                "Player {} respawned as spectator (mode 2).",
                player.getName().getString()
            );
        }
    }
}
