package dev.sillysaucers.somethingrising.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.DoubleArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.parsers.MaterialArgument;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import dev.sillysaucers.somethingrising.GamePeriod;
import dev.sillysaucers.somethingrising.RisingUtils;
import dev.sillysaucers.somethingrising.SomethingRising;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class RisingCommand {

    public static void init(SomethingRising plugin) {
        try {

            // command manager
            CommandManager<CommandSender> manager = new BukkitCommandManager<>(
                    plugin,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );

            // something rising command
            manager.command(
                    RisingUtils.generateCommand(manager, "start")
                            .handler(context -> {
                                        if (context.getSender() instanceof Player) {
                                            switch (SomethingRising.CURRENT_STATUS) {
                                                case ACTIVE:
                                                case BORDER:
                                                case STARTER:
                                                    context.getSender().sendMessage(ChatColor.RED + "There is a game currently going on.");
                                                    break;
                                                case ENDED:
                                                    context.getSender().sendMessage(ChatColor.RED + "Please restart your server before starting another game.");
                                                    break;
                                                case LOBBY:
                                                    SomethingRising.CURRENT_STATUS = GamePeriod.STARTER;
                                                    SomethingRising.STARTER_PRE_EVENT.startFromStarter(plugin, ((Player) context.getSender()).getWorld());
                                                    for (Player pl : Bukkit.getOnlinePlayers()) {
                                                        if (context.getSender() instanceof Player) {
                                                            if (pl.hasPermission("something.rising.admin") && !pl.getName().equals(context.getSender().getName())) {
                                                                pl.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "[" + context.getSender().getName() + ": Started the minigame.]");
                                                            }
                                                        }
                                                        SomethingRising.alivePlayers.add(pl.getUniqueId());
                                                        pl.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
                                                        pl.setGameMode(GameMode.SURVIVAL);
                                                        pl.sendMessage(ChatColor.GREEN + "The starter period has begun!");
                                                        pl.playSound(pl.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
                                                    }
                                                    break;
                                            }
                                        }

                                    }
                            )
            );

            manager.command(
                    RisingUtils.generateCommand(manager, "setfinalbordertime")
                            .argument(IntegerArgument.builder("finalbordertime"))
                            .handler(
                                    context -> {
                                        switch (SomethingRising.CURRENT_STATUS) {
                                            case LOBBY:
                                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                                    if (context.getSender() instanceof Player) {
                                                        if (pl.hasPermission("something.rising.admin") && !pl.getName().equals(context.getSender().getName())) {
                                                            pl.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "[" + context.getSender().getName() + ": Set final border time to: " + context.get("finalbordertime") + ".]");
                                                        }
                                                    }
                                                }
                                                SomethingRising.GAME.setFinalBorderTime(context.get("finalbordertime"));
                                                context.getSender().sendMessage(ChatColor.GREEN + "The final border time is now: " + context.get("finalbordertime") + "!");
                                                break;
                                            case ENDED:
                                            case ACTIVE:
                                            case BORDER:
                                            case STARTER:
                                                context.getSender().sendMessage(ChatColor.RED + "You can't change that now!");
                                                break;
                                        }
                                    }
                            )
            );

            manager.command(
                    RisingUtils.generateCommand(manager, "setstarterborderradius")
                            .argument(DoubleArgument.builder("borderradius"))
                            .handler(
                                    context -> {
                                        switch (SomethingRising.CURRENT_STATUS) {
                                            case LOBBY:
                                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                                    if (context.getSender() instanceof Player) {
                                                        if (pl.hasPermission("something.rising.admin") && !pl.getName().equals(context.getSender().getName())) {
                                                            pl.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "[" + context.getSender().getName() + ": Set starter border to: " + context.get("borderradius") + ".]");
                                                        }
                                                    }
                                                }
                                                SomethingRising.STARTER_PRE_EVENT.setWorldBorderRadius(context.get("borderradius"));
                                                context.getSender().sendMessage(ChatColor.GREEN + "The starter border is now: " + context.get("borderradius") + "!");
                                                break;
                                            case ENDED:
                                            case ACTIVE:
                                            case BORDER:
                                            case STARTER:
                                                context.getSender().sendMessage(ChatColor.RED + "You can't change that now!");
                                                break;
                                        }
                                    }
                            )
            );

            manager.command(
                    RisingUtils.generateCommand(manager, "setblock")
                            .argument(MaterialArgument.builder("block"))
                            .handler(context -> {
                                switch (SomethingRising.CURRENT_STATUS) {
                                    case LOBBY:
                                        for (Player pl : Bukkit.getOnlinePlayers()) {
                                            if (context.getSender() instanceof Player) {
                                                if (pl.hasPermission("something.rising.admin") && !pl.getName().equals(context.getSender().getName())) {
                                                    pl.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "[" + context.getSender().getName() + ": Set block to " + context.get("block") + ".]");
                                                }
                                            }
                                        }
                                        SomethingRising.GAME.setBlock(context.get("block"));
                                        context.getSender().sendMessage(ChatColor.GREEN + "The block used in the block rising is now: " + context.get("block") + "!");
                                        break;
                                    case ENDED:
                                    case ACTIVE:
                                    case BORDER:
                                    case STARTER:
                                        context.getSender().sendMessage(ChatColor.RED + "You can't change that now!");
                                        break;
                                }
                            })
            );

            manager.command(
                    RisingUtils.generateCommand(manager, "setticksperrise")
                            .argument(IntegerArgument.builder("time"))
                            .handler(context -> {
                                        if (!(((int) context.get("time")) < 1 && ((int) context.get("time")) > 1200)) {
                                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                                if (context.getSender() instanceof Player) {
                                                    if (pl.hasPermission("something.rising.admin") && !pl.getName().equals(context.getSender().getName())) {
                                                        pl.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "[" + context.getSender().getName() + ": Set lava rise ticks to " + context.get("time") + ".]");
                                                    }
                                                }
                                            }
                                            SomethingRising.GAME.setTicksPerRise(context.get("time"));
                                            context.getSender().sendMessage(ChatColor.GREEN + "The ticks per lava rise has been set to " + context.get("time") + "!");
                                        } else {
                                            context.getSender().sendMessage(ChatColor.RED + "That value is way too high! Are you trying to make your games last a million years?");
                                        }
                                    }
                            )
            );

            manager.command(
                    RisingUtils.generateCommand(manager, "setbordercloseseconds")
                            .argument(IntegerArgument.builder("closeSeconds"))
                            .handler(context -> {
                                switch (SomethingRising.CURRENT_STATUS) {
                                    case LOBBY:
                                        for (Player pl : Bukkit.getOnlinePlayers()) {
                                            if (context.getSender() instanceof Player) {
                                                if (pl.hasPermission("something.rising.admin") && !pl.getName().equals(context.getSender().getName())) {
                                                    pl.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "[" + context.getSender().getName() + ": Set border close seconds to " + context.get("closeSeconds") + ".]");
                                                }
                                            }
                                        }
                                        SomethingRising.BORDER_PRE_EVENT.setBorderClosingSeconds(context.get("closeSeconds"));
                                        context.getSender().sendMessage(ChatColor.GREEN + "The seconds it takes for the border to close is now " + context.get("closeSeconds"));
                                        break;
                                    case ENDED:
                                    case ACTIVE:
                                    case BORDER:
                                    case STARTER:
                                        context.getSender().sendMessage(ChatColor.RED + "You can't change that now!");
                                        break;
                                }
                            })

            );

            manager.command(
                    RisingUtils.generateCommand(manager, "starterperiodtime")
                            .argument(IntegerArgument.builder("starterseconds"))
                            .handler(context -> {
                                switch (SomethingRising.CURRENT_STATUS) {
                                    case LOBBY:
                                        for (Player pl : Bukkit.getOnlinePlayers()) {
                                            if (context.getSender() instanceof Player) {
                                                if (pl.hasPermission("something.rising.admin") && !pl.getName().equals(context.getSender().getName())) {
                                                    pl.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "[" + context.getSender().getName() + ": Set the amount of time in the starter period to " + context.get("starterseconds") + ".]");
                                                }
                                            }
                                        }
                                        SomethingRising.STARTER_PRE_EVENT.setTimeLeft(context.get("starterseconds"));
                                        context.getSender().sendMessage(ChatColor.GREEN + "The starter period will now last " + context.get("starterseconds"));
                                        break;
                                    case ENDED:
                                    case ACTIVE:
                                    case BORDER:
                                    case STARTER:
                                        context.getSender().sendMessage(ChatColor.RED + "You can't change that now!");
                                        break;
                                }
                            })

            );

            manager.command(
                    RisingUtils.generateCommand(manager, "setlavaheight")
                            .argument(IntegerArgument.builder("lavaheight"))
                            .handler(
                                    context -> {
                                        switch (SomethingRising.CURRENT_STATUS) {
                                            case LOBBY:
                                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                                    if (context.getSender() instanceof Player) {
                                                        if (pl.hasPermission("something.rising.admin") && !pl.getName().equals(context.getSender().getName())) {
                                                            pl.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "[" + context.getSender().getName() + ": Set lava height to: " + context.get("lavaheight") + ".]");
                                                        }
                                                    }
                                                }
                                                SomethingRising.GAME.setLavaHeightLimit(context.get("lavaheight"));
                                                context.getSender().sendMessage(ChatColor.GREEN + "The lava height is now: " + context.get("lavaheight") + "!");
                                                break;
                                            case ENDED:
                                            case ACTIVE:
                                            case BORDER:
                                            case STARTER:
                                                context.getSender().sendMessage(ChatColor.RED + "You can't change that now!");
                                                break;
                                        }

                                    }
                            )
            );

            manager.command(
                    RisingUtils.generateCommand(manager, "revive")
                            .argument(PlayerArgument.builder("revivedPlayer"))
                            .handler(
                                    context -> {

                                        Player revivedPlayer = context.get("revivedPlayer");

                                        switch (SomethingRising.CURRENT_STATUS) {
                                            case LOBBY:
                                            case ENDED:
                                                context.getSender().sendMessage(ChatColor.RED + "This is not a period where you can revive people!");
                                                break;
                                            case STARTER:
                                            case BORDER:
                                            case ACTIVE:
                                                if (!SomethingRising.alivePlayers.contains(revivedPlayer.getUniqueId())) {
                                                    for (Player pl : Bukkit.getOnlinePlayers()) {
                                                        if (context.getSender() instanceof Player) {
                                                            if (pl.hasPermission("something.rising.admin") && !pl.getName().equals(context.getSender().getName())) {
                                                                pl.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "[" + context.getSender().getName() + ": Revived " + revivedPlayer.getName() + ".]");
                                                            }
                                                        }
                                                    }

                                                    if (context.getSender() instanceof Player) {

                                                        Player teleportTo = (Player) context.getSender();
                                                        revivedPlayer.teleport(teleportTo);

                                                    }

                                                    revivedPlayer.setGameMode(GameMode.SURVIVAL);

                                                    SomethingRising.alivePlayers.add(revivedPlayer.getUniqueId());

                                                    for (Player pl :
                                                            Bukkit.getOnlinePlayers()) {
                                                        pl.sendMessage(ChatColor.YELLOW + revivedPlayer.getDisplayName() + ChatColor.GREEN + " has been revived!");
                                                    }
                                                } else {
                                                    context.getSender().sendMessage(ChatColor.RED + "That player is still alive!");
                                                }
                                                break;
                                        }


                                    }
                            )
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
