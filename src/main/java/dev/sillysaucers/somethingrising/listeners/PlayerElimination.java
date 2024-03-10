package dev.sillysaucers.somethingrising.listeners;

import dev.sillysaucers.somethingrising.GamePeriod;
import dev.sillysaucers.somethingrising.SomethingRising;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerElimination implements Listener {

    public static void handleVictory() {
        Player winner = Bukkit.getPlayer(SomethingRising.alivePlayers.get(0));
        for (Player pl :
                Bukkit.getOnlinePlayers()) {
            pl.sendTitle(ChatColor.GREEN + winner.getName() + " has won!", "", 10, 70, 20);
            pl.playSound(pl.getLocation(), Sound.ENTITY_ALLAY_DEATH, 1, 1);
            pl.setGameMode(GameMode.SPECTATOR);
            SomethingRising.CURRENT_STATUS = GamePeriod.ENDED;
        }
    }

    public static void handlePlayerDeath(PlayerDeathEvent event) {
        switch (SomethingRising.CURRENT_STATUS) {
            case LOBBY:
            case ENDED:
            case STARTER:
            case BORDER:
                event.setDeathMessage("");
                break;
            case ACTIVE:
                Player player = event.getEntity();
                event.setKeepInventory(true);
                player.setGameMode(GameMode.SPECTATOR);
                SomethingRising.alivePlayers.remove(player.getUniqueId());
                if (SomethingRising.alivePlayers.size() == 1) {
                    handleVictory();
                } else {
                    event.setDeathMessage(
                            ChatColor.RED + player.getName() +
                                    " has been eliminated. (" +
                                    SomethingRising.alivePlayers.size() +
                                    "/" +
                                    Bukkit.getOnlinePlayers().size() + ")"
                    );
                }
        }
    }

    public static void handlePlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        switch (SomethingRising.CURRENT_STATUS) {

            case STARTER:
            case BORDER:
            case ACTIVE:
                if (SomethingRising.alivePlayers.contains(player.getUniqueId())) {

                    SomethingRising.alivePlayers.remove(player.getUniqueId());

                    for (Player pl :
                            Bukkit.getOnlinePlayers()) {

                        pl.sendMessage(ChatColor.RED + player.getName() + " has left!");

                    }

                    if (SomethingRising.alivePlayers.size() == 1) {
                        handleVictory();
                    }
                }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        switch (SomethingRising.CURRENT_STATUS) {
            case LOBBY:
            case STARTER:
            case BORDER:
            case ENDED:
                break;
            case ACTIVE:
                if (SomethingRising.alivePlayers.contains(player.getUniqueId())) {
                    handlePlayerDeath(event);
                } else {
                    event.setDeathMessage("");
                }
                break;
        }

    }


    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {

            switch (SomethingRising.CURRENT_STATUS) {

                case STARTER:
                case ENDED:
                case LOBBY:
                    event.setCancelled(true);
                    break;

            }

        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        handlePlayerDisconnect(event);
    }

}
