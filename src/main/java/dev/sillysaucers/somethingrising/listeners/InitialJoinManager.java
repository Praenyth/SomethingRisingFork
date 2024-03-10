package dev.sillysaucers.somethingrising.listeners;

import dev.sillysaucers.somethingrising.GamePeriod;
import dev.sillysaucers.somethingrising.SomethingRising;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class InitialJoinManager implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (SomethingRising.CURRENT_STATUS == GamePeriod.LOBBY) {

            event.getPlayer().setGameMode(GameMode.ADVENTURE);

        } else {

            event.getPlayer().setGameMode(GameMode.SPECTATOR);

        }
    }

}
