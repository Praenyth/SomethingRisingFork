package dev.sillysaucers.somethingrising;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.StaticArgument;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class RisingUtils {

    public static Command.Builder<CommandSender> generateCommand(CommandManager<CommandSender> manager, String argument) {
        return Command.<CommandSender>newBuilder(
                        "rising",
                        manager.createDefaultCommandMeta()
                ).argument(StaticArgument.of(argument))
                .permission("something.rising.admin");
    }

    public static String displayTimer(int sec) {
        int hours = (int) TimeUnit.SECONDS.toHours(sec);
        int minutes = (int) (TimeUnit.SECONDS.toMinutes(sec) - TimeUnit.HOURS.toMinutes(hours));
        int seconds = (int) (TimeUnit.SECONDS.toSeconds(sec) - TimeUnit.MINUTES.toSeconds(minutes));
        if (seconds < 0) {
            seconds = 0;
        }
        if (hours == 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public static TextComponent getColoredTimer(String str, int secondsLeft, int startingSeconds) {
        // Calculate the percentage of time elapsed
        double percentElapsed = 1.0 - ((double) secondsLeft / startingSeconds);

        // Calculate the RGB values based on the percentage elapsed
        int red, green;
        if (percentElapsed < 0.5) {
            green = 255;
            red = (int) (percentElapsed * 2 * 255);
        } else {
            green = (int) ((1 - percentElapsed) * 2 * 255);
            red = 255;
        }

        // Construct the hex color code for the text color
        String colorCode = String.format("#%02x%02x00", red, green);
        TextComponent component = new TextComponent(str);
        component.setColor(ChatColor.of(colorCode));
        return component;
    }
}
