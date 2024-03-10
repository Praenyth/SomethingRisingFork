package dev.sillysaucers.somethingrising.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class InitialBorderManager implements Listener {

    public boolean firstPlayerJoined = false;

    @EventHandler
    public void onFirstPlayerJoin(PlayerJoinEvent event) {

        if (!firstPlayerJoined) {

            event.getPlayer().getWorld().getWorldBorder().setSize(10);

        }

    }

}
