package dev.sillysaucers.somethingrising.runnables;

import dev.sillysaucers.somethingrising.RisingUtils;
import dev.sillysaucers.somethingrising.SomethingRising;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static dev.sillysaucers.somethingrising.RisingUtils.getColoredTimer;

public class StarterPeriodRunnable extends BukkitRunnable {

    private final int initialTime = 1200;
    Plugin plugin;
    private boolean runOnce = false;
    private int timeLeft = 1200;
    private World world;
    private double worldBorderRadius = 500;

    public StarterPeriodRunnable(Plugin plugin) {
        this.plugin = plugin;
    }

    public void startFromStarter(Plugin plugin, World world) {
        this.world = world;
        this.runTaskTimer(plugin, 0, 20);
    }

    public void setTimeLeft(int seconds) {
        timeLeft = seconds;
    }

    public void setWorldBorderRadius(double radius) {
        worldBorderRadius = radius;
    }

    @Override
    public void run() {

        if (!runOnce) {

            world.getWorldBorder().setSize(worldBorderRadius);
            runOnce = true;

        }

        timeLeft--;
        for (Player pl :
                Bukkit.getOnlinePlayers()) {
            pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, getColoredTimer(RisingUtils.displayTimer(timeLeft), timeLeft, initialTime));
        }
        if (timeLeft <= 0) {
            SomethingRising.BORDER_PRE_EVENT.startBorderClose(plugin);
            cancel();
        }
    }

}
