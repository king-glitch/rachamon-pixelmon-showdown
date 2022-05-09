package dev.rachamon.rachamonpixelmonshowdown.commands.subcommands;

import dev.rachamon.api.sponge.implement.command.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;

import javax.annotation.Nonnull;

@ICommandDescription("Queue Section")
@ICommandAliases({"queue"})
@ICommandPermission("rachamonpixelmonshowdown.command.user.queue")
public class PixelmonShowdownQueueCommand implements IPlayerCommand, IParameterizedCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {



        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{};
    }
}
