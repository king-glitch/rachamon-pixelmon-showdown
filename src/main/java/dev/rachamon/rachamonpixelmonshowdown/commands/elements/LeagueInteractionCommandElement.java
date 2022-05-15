package dev.rachamon.rachamonpixelmonshowdown.commands.elements;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LeagueInteractionCommandElement extends CommandElement {
    public LeagueInteractionCommandElement(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected Object parseValue(@Nonnull CommandSource source, @Nonnull CommandArgs args) throws ArgumentParseException {
        return null;
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, @Nonnull CommandArgs args, @Nonnull CommandContext context) {
        try {
            String next = args.next();
            return new ArrayList<>(Arrays.asList("stats", "enter", "leave", "leaderboard", "rules"))
                    .stream()
                    .filter(key -> key.toLowerCase().contains(next.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (ArgumentParseException ignored) {

        }

        return new ArrayList<>();
    }
}
