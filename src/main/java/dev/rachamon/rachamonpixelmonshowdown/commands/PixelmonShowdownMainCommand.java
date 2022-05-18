package dev.rachamon.rachamonpixelmonshowdown.commands;

import dev.rachamon.api.sponge.implement.command.*;
import dev.rachamon.rachamonpixelmonshowdown.commands.subcommands.PixelmonShowdownDrawCommand;
import dev.rachamon.rachamonpixelmonshowdown.commands.subcommands.PixelmonShowdownQueueCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

import javax.annotation.Nonnull;

/**
 * The type Texture tokens main command.
 */
@ICommandChildren({PixelmonShowdownQueueCommand.class, PixelmonShowdownDrawCommand.class})
@ICommandAliases({"rachamonpixelmonshowdown", "showdown"})
@ICommandHelpText(title = "Main Pixelmon Showdown Help", command = "help")
@ICommandPermission("rachamonpixelmonshowdown.command.base")
public class PixelmonShowdownMainCommand implements ICommand {
    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext args) throws CommandException {
        return CommandResult.success();
    }
}
