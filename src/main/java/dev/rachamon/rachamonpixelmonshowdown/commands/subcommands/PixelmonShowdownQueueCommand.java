package dev.rachamon.rachamonpixelmonshowdown.commands.subcommands;

import dev.rachamon.api.sponge.implement.command.*;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.commands.elements.LeagueInteractionCommandElement;
import dev.rachamon.rachamonpixelmonshowdown.commands.elements.LeagueNameCommandElement;
import dev.rachamon.rachamonpixelmonshowdown.utils.ChatUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * The type Pixelmon showdown queue command.
 */
@ICommandDescription("Queue Section")
@ICommandAliases({"queue"})
@ICommandPermission("rachamonpixelmonshowdown.command.user.queue")
public class PixelmonShowdownQueueCommand implements IPlayerCommand, IParameterizedCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {

        Optional<String> league = args.getOne("league");
        Optional<String> interaction = args.getOne("interaction");

        if (!league.isPresent() || !interaction.isPresent()) {
            return CommandResult.empty();
        }


        try {
            if (interaction.get().equalsIgnoreCase("enter")) {
                RachamonPixelmonShowdown.getInstance().getMatchMakingManager().enterQueue(league.get(), source);
            } else if (interaction.get().equalsIgnoreCase("leave")) {
                RachamonPixelmonShowdown.getInstance().getMatchMakingManager().leaveQueue(league.get(), source);
            } else if (interaction.get().equalsIgnoreCase("stats")) {
                RachamonPixelmonShowdown.getInstance().getMatchMakingManager().leagueStats(league.get(), source);
            } else if (interaction.get().equalsIgnoreCase("leaderboard")) {
                RachamonPixelmonShowdown.getInstance().getMatchMakingManager().leagueLeaderboard(league.get(), source);
            } else if (interaction.get().equalsIgnoreCase("rules")) {
                RachamonPixelmonShowdown.getInstance().getMatchMakingManager().leagueRules(league.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtil.sendMessage(source, e.getMessage());
        }


        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{new LeagueNameCommandElement(Text.of("league")), new LeagueInteractionCommandElement(Text.of("interaction"))};
    }
}
