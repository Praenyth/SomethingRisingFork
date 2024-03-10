package dev.sillysaucers.somethingrising.runnables;

import dev.sillysaucers.somethingrising.GamePeriod;
import dev.sillysaucers.somethingrising.RisingUtils;
import dev.sillysaucers.somethingrising.SomethingRising;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

import static dev.sillysaucers.somethingrising.RisingUtils.getColoredTimer;

public class BorderClosingPeriodRunnable extends BukkitRunnable {

    private final int initialBorderClosingSeconds = 1200;
    private World world;
    private boolean startedBorderClosePhase = false;
    private int borderClosingSeconds = 1200;

    public BorderClosingPeriodRunnable(World world) {
        this.world = world;
    }

    public void startBorderClose(Plugin plugin) {
        this.runTaskTimer(plugin, 0, 20);
    }

    public void setBorderClosingSeconds(int seconds) {
        borderClosingSeconds = seconds;
    }

    @Override
    public void run() {
        if (!startedBorderClosePhase) {
            SomethingRising.CURRENT_STATUS = GamePeriod.BORDER;
            world.getWorldBorder().setSize(100, TimeUnit.SECONDS, borderClosingSeconds);
            for (Player pl :
                    Bukkit.getOnlinePlayers()) {
                pl.sendMessage(ChatColor.YELLOW + "The border will now close in! PVP is now enabled.");
            }
            startedBorderClosePhase = true;
        }
        borderClosingSeconds--;
        for (Player pl :
                Bukkit.getOnlinePlayers()) {
            pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, getColoredTimer(RisingUtils.displayTimer(borderClosingSeconds), borderClosingSeconds, initialBorderClosingSeconds));
        }
        if (borderClosingSeconds <= 0) {
            SomethingRising.GAME.startLavaRise();
            cancel();
        }

    }
}
