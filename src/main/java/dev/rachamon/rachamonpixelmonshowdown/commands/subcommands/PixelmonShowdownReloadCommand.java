package dev.rachamon.rachamonpixelmonshowdown.commands.subcommands;

import dev.rachamon.api.sponge.implement.command.*;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;

import javax.annotation.Nonnull;

/**
 * The type Pixelmon showdown reload command.
 */
@ICommandDescription("reload config, languages, leagues")
@ICommandAliases({"reload"})
@ICommandPermission("rachamonpixelmonshowdown.command.admin.reload")
public class PixelmonShowdownReloadCommand implements ICommand, IParameterizedCommand {

    @Nonnull
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext args) throws CommandException {


        try {
            RachamonPixelmonShowdown.getInstance().getPluginManager().reload();
        } catch (Exception e) {
            e.printStackTrace();
            RachamonPixelmonShowdown.getInstance().getLogger().debug(e.getMessage());
            return CommandResult.empty();
        }

        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{};
    }

}
