package dev.sillysaucers.somethingrising;

import dev.sillysaucers.somethingrising.commands.RisingCommand;
import dev.sillysaucers.somethingrising.listeners.HeightLimit;
import dev.sillysaucers.somethingrising.listeners.InitialJoinManager;
import dev.sillysaucers.somethingrising.listeners.PlayerElimination;
import dev.sillysaucers.somethingrising.runnables.BlockRisingRunnable;
import dev.sillysaucers.somethingrising.runnables.BorderClosingPeriodRunnable;
import dev.sillysaucers.somethingrising.runnables.StarterPeriodRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SomethingRising extends JavaPlugin {

    public static GamePeriod CURRENT_STATUS = GamePeriod.LOBBY;
    public static StarterPeriodRunnable STARTER_PRE_EVENT;
    public static BorderClosingPeriodRunnable BORDER_PRE_EVENT;
    public static BlockRisingRunnable GAME;


    public static List<UUID> alivePlayers = new ArrayList<>();

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new PlayerElimination(), this);
        getServer().getPluginManager().registerEvents(new InitialJoinManager(), this);
        getServer().getPluginManager().registerEvents(new HeightLimit(), this);
        GAME = new BlockRisingRunnable(this, getServer().getWorlds().get(0), 20);
        STARTER_PRE_EVENT = new StarterPeriodRunnable(this);
        BORDER_PRE_EVENT = new BorderClosingPeriodRunnable(getServer().getWorlds().get(0));
        RisingCommand.init(this);

    }

    @Override
    public void onDisable() {

    }

}

