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

public class LeagueDrawResponseCommandElement extends CommandElement {
    public LeagueDrawResponseCommandElement(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected Object parseValue(@Nonnull CommandSource source, @Nonnull CommandArgs args) throws ArgumentParseException {
        return args.next();
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, @Nonnull CommandArgs args, @Nonnull CommandContext context) {
        try {
            String next = args.next();
            return new ArrayList<>(Arrays.asList("yes", "no", "accept", "deny"))
                    .stream()
                    .filter(key -> key.toLowerCase().contains(next.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (ArgumentParseException ignored) {

        }

        return new ArrayList<>();
    }
}
