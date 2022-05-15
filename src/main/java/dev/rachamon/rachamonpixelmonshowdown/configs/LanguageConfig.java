package dev.rachamon.rachamonpixelmonshowdown.configs;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.Setting;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class LanguageConfig {
    @Setting(value = "general", comment = "General Battle Messages")
    private final GeneralLanguageBattle generalLanguageBattle = new GeneralLanguageBattle();

    @ConfigSerializable
    public static class GeneralLanguageBattle {
        @Setting(value = "prefix", comment = "Prefix for chat message")
        protected String prefix = "&8[PixelmonShowdown&8]&7 ";

        @Setting(value = "win-message", comment = "win message after the player has won.")
        protected String winMessage = "&aYou have won player &a&l{player}&7, current elo &7&l{current}&l &8-> &a&l{new-elo}";

        @Setting(value = "lose-message", comment = "lose message after the player has lost.")
        protected String loseMessage = "&cYou have lost against player &a&l{player}&7, current elo &7&l{current}&l &8-> &c&l{new-elo}";

        @Setting(value = "starting-message", comment = "message before matching starting")
        protected String startingMessage = "&7The match will start in &a&l{time}&7 seconds, get ready!";

        public String getPrefix() {
            return prefix;
        }

        public String getWinMessage() {
            return winMessage;
        }

        public String getLoseMessage() {
            return loseMessage;
        }

        public String getStartingMessage() {
            return startingMessage;
        }
    }

    public GeneralLanguageBattle getGeneralLanguageBattle() {
        return generalLanguageBattle;
    }
}