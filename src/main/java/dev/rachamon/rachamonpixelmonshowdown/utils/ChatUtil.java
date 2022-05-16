package dev.rachamon.rachamonpixelmonshowdown.utils;

import dev.rachamon.api.sponge.util.TextUtil;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import org.spongepowered.api.entity.living.player.Player;

/**
 * The type Chat util.
 */
public class ChatUtil {


    /**
     * Send message.
     *
     * @param player  the player
     * @param message the message
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(TextUtil.toText(RachamonPixelmonShowdown
                .getInstance()
                .getLanguage()
                .getGeneralLanguageBattle()
                .getPrefix() + message));
    }

}
