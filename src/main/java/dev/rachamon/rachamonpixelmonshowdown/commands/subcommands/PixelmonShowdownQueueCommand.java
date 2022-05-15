package dev.rachamon.rachamonpixelmonshowdown.commands.subcommands;

import dev.rachamon.api.sponge.implement.command.*;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;

import javax.annotation.Nonnull;
import java.util.Optional;

@ICommandDescription("Queue Section")
@ICommandAliases({"queue"})
@ICommandPermission("rachamonpixelmonshowdown.command.user.queue")
public class PixelmonShowdownQueueCommand implements IPlayerCommand, IParameterizedCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {

        Optional<String> league = args.getOne("league");

        if (!league.isPresent()) {
            return CommandResult.empty();
        }

        try {
            RachamonPixelmonShowdown
                    .getInstance()
                    .getMatchMakingManager()
                    .enterQueue(league.get(), (EntityPlayerMP) source);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{};
    }
}
