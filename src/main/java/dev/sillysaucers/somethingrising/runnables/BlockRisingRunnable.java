package dev.sillysaucers.somethingrising.runnables;

import dev.sillysaucers.somethingrising.GamePeriod;
import dev.sillysaucers.somethingrising.SomethingRising;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class BlockRisingRunnable extends BukkitRunnable {

    public static int originalTicks;
    private static int Y_LEVEL = -64;
    public int lavaHeight = 318;
    private boolean runOnce = false;
    private Plugin plugin;
    private World world;
    private int ticksPerRise;
    private Material block = Material.LAVA;

    private int finalBorderTime = 300;

    public BlockRisingRunnable(Plugin plugin, World world, int ticks) {
        this.plugin = plugin;
        this.world = world;
        this.ticksPerRise = ticks;
        originalTicks = ticks;
    }

    public static void resetyLevel() {
        Y_LEVEL = -64;
    }

    public void setyLevel(int yLevel) {
        Y_LEVEL = yLevel;
    }

    public void setTicksPerRise(int ticks) {
        originalTicks = ticks;
    }

    public void setBlock(Material material) {
        block = material;
    }

    public void setFinalBorderTime(int seconds) {
        finalBorderTime = seconds;
    }

    public void setLavaHeightLimit(int heightLimit) {
        lavaHeight = heightLimit;
    }

    public void startLavaRise() {
        this.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public void run() {

        if (!runOnce) {
            world.getWorldBorder().setSize(20, TimeUnit.SECONDS, finalBorderTime);
            for (Player pl :
                    Bukkit.getOnlinePlayers()) {
                pl.sendMessage(ChatColor.YELLOW + "Lava will now rise. If you die beyond this point, you're eliminated. " + ChatColor.LIGHT_PURPLE + "Good luck :3");
            }
            SomethingRising.CURRENT_STATUS = GamePeriod.ACTIVE;
            runOnce = true;
        }

        if (SomethingRising.CURRENT_STATUS == GamePeriod.ENDED) {
            cancel();
        }

        ticksPerRise--;
        if (ticksPerRise <= 0) {
            ticksPerRise = originalTicks;
            if (Y_LEVEL > lavaHeight) {
                cancel();
            } else {
                for (int x = -50; x < 50; x++) {
                    for (int z = -50; z < 50; z++) {
                        world.getBlockAt(x, Y_LEVEL, z).setType(block, false);
                    }
                }
                Y_LEVEL++;
            }
        }

    }

}
