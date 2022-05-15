package dev.rachamon.rachamonpixelmonshowdown.commands.elements;

import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LeagueNameCommandElement extends CommandElement {

    public LeagueNameCommandElement(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected Object parseValue(@Nonnull CommandSource source, CommandArgs args) throws ArgumentParseException {
        return args.next();
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, CommandArgs args, @Nonnull CommandContext context) {
        Set<String> keys = RachamonPixelmonShowdown.getInstance().getLeague().getLeagues().keySet();
        try {
            String next = args.next();
            return keys
                    .stream()
                    .filter(key -> key.toLowerCase().contains(next.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (ArgumentParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(keys);
    }
}
