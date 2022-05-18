package dev.rachamon.rachamonpixelmonshowdown.commands.subcommands;

import dev.rachamon.api.sponge.implement.command.*;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.commands.elements.LeagueDrawResponseCommandElement;
import dev.rachamon.rachamonpixelmonshowdown.utils.ChatUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * The type Pixelmon showdown queue command.
 */
@ICommandDescription("ask opponent to draw")
@ICommandAliases({"draw"})
@ICommandPermission("rachamonpixelmonshowdown.command.user.draw")
public class PixelmonShowdownDrawCommand implements IPlayerCommand, IParameterizedCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {

        Optional<String> response = args.getOne("response");
        try {
            if (!response.isPresent()) {
                RachamonPixelmonShowdown.getInstance().getMatchMakingManager().askForDraw(source);
            } else if (response.get().equalsIgnoreCase("yes") || response.get().equalsIgnoreCase("accept")) {
                RachamonPixelmonShowdown.getInstance().getMatchMakingManager().acceptedDraw(source);
            } else if (response.get().equalsIgnoreCase("no") || response.get().equalsIgnoreCase("deny")) {
                RachamonPixelmonShowdown.getInstance().getMatchMakingManager().deniedDraw(source);
            } else {
                return CommandResult.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtil.sendMessage(source, e.getMessage());
            return CommandResult.empty();
        }

        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{GenericArguments.optional(new LeagueDrawResponseCommandElement(Text.of("response")))};
    }
}
