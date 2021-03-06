package dev.rachamon.rachamonpixelmonshowdown.configs;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Arrays;
import java.util.List;

/**
 * The type Language config.
 */
@ConfigSerializable
public class LanguageConfig {
    @Setting(value = "general", comment = "General Battle Messages")
    private final GeneralLanguageBattle generalLanguageBattle = new GeneralLanguageBattle();

    /**
     * The type General language battle.
     */
    @ConfigSerializable
    public static class GeneralLanguageBattle {
        /**
         * The Prefix.
         */
        @Setting(value = "prefix", comment = "Prefix for chat message")
        protected String prefix = "&8[&c&lPixelmonShowdown&8]&7 ";

        /**
         * The Hover text.
         */
        @Setting(value = "hover-text", comment = "hover text display")
        protected String hoverText = "&8[&a&lHOVER&8]&r";

        /**
         * The Banned header.
         */
        @Setting(value = "banned-header", comment = "banned title")
        protected String bannedHeader = "&4&lBanned&7";

        /**
         * The Yes text.
         */
        @Setting(value = "yes-text", comment = "yes text")
        protected String yesText = "&a&lYes&7";

        /**
         * The No text.
         */
        @Setting(value = "no-text", comment = "no text")
        protected String noText = "&c&lNo&7";

        /**
         * The Team double text.
         */
        @Setting(value = "team-double-text", comment = "team double text")
        protected String teamDoubleText = "&5&lDouble&7";

        /**
         * The Team single.
         */
        @Setting(value = "team-single-text", comment = "team single text")
        protected String teamSingle = "&6&lSingle&7";

        /**
         * The Team double error.
         */
        @Setting(value = "team-double-error", comment = "error when player has only one pokemon when battle type is double.")
        protected String teamDoubleError = "&cThis is double team, please put more than one pokemon in your party.";


        /**
         * The Not in battle.
         */
        @Setting(value = "not-in-battle", comment = "error when no in a battle")
        protected String notInBattle = "&cYou or your opponent is not in a battle.";

        /**
         * The Successfully draw.
         */
        @Setting(value = "successfully-draw", comment = "draw successfully message")
        protected String successfullyDraw = "&aYou have successfully draw with your opponent. no point will be lose.";

        /**
         * The Player ask for draw.
         */
        @Setting(value = "player-ask-for-draw", comment = "message when player ask for a draw")
        protected String playerAskForDraw = "&7You have request a draw to an opponent.";

        /**
         * The Opponent ask for draw.
         */
        @Setting(value = "opponent-ask-for-draw", comment = "message when player has ask an opponent for a draw")
        protected String opponentAskForDraw = "&7Your opponent has asked you for a draw, would you like to accept?";

        /**
         * The Opponent decline.
         */
        @Setting(comment = "when opponent decline draw", value = "opponent-decline")
        protected String opponentDecline = "&cYour opponent has decline";

        /**
         * The You decline.
         */
        @Setting(comment = "when you decline draw", value = "opponent-decline")
        protected String youDecline = "&7You have declined your opponent proposal";

        /**
         * The Win message.
         */
        @Setting(value = "win-message", comment = "win message after the player has won.")
        protected String winMessage = "&aYou have won player &a&l{player}&7, current elo &7&l{current}&l &8-> &a&l{new-elo}";

        /**
         * The Lose message.
         */
        @Setting(value = "lose-message", comment = "lose message after the player has lost.")
        protected String loseMessage = "&cYou have lost against player &a&l{player}&7, current elo &7&l{current}&l &8-> &c&l{new-elo}";

        /**
         * The Starting message.
         */
        @Setting(value = "starting-message", comment = "message before matching starting")
        protected String startingMessage = "&7The match will start in &a&l{time}&7 seconds, get ready!";

        /**
         * The Already in battle.
         */
        @Setting(value = "already-in-battle", comment = "one player is already in battle")
        protected String alreadyInBattle = "A participant is already in battle! Battle cancelled.";

        /**
         * The Your team has fainted.
         */
        @Setting(value = "your-team-has-fainted", comment = "some pokemon has been fainted")
        protected String yourTeamHasFainted = "&cOne of your pokemon has been fained.";

        /**
         * The Participant team has fainted.
         */
        @Setting(value = "participant-team-has-fainted", comment = "some pokemon has been fainted")
        protected String participantTeamHasFainted = "&cOne of your participant pokemon has been fained.";

        /**
         * The Your team not same.
         */
        @Setting(value = "your-team-not-same", comment = "team party not same")
        protected String yourTeamNotSame = "&cYour party is not the same as what you queued with!";

        /**
         * The Participant team not same.
         */
        @Setting(value = "participant-team-not-same", comment = "team party not same")
        protected String participantTeamNotSame = "&cA participant's team was found ineligible! Battle cancelled.";

        /**
         * The Your team not validated.
         */
        @Setting(value = "your-team-not-validated", comment = "team party not validated rules")
        protected String yourTeamNotValidated = "&cYour party does not follow the formats rules!";

        /**
         * The Participant team not validated.
         */
        @Setting(value = "participant-team-not-validated", comment = "team party not validated rules")
        protected String participantTeamNotValidated = "&cA participant's team did not follow the format's rules! Battle cancelled";

        /**
         * The You already in battle.
         */
        @Setting(value = "you-already-in-battle", comment = "already in battle")
        protected String youAlreadyInBattle = "&cYou are already in battle!";

        /**
         * The Participant already in battle.
         */
        @Setting(value = "participant-already-in-battle", comment = "already in battle")
        protected String participantAlreadyInBattle = "&cA participant is already in battle! Battle cancelled.";

        /**
         * The Player not found battle.
         */
        @Setting(value = "player-not-found-battle", comment = "player not found")
        protected String playerNotFoundBattle = "&cPlayer not found, A player might be disconnected! Battle cancelled.";

        /**
         * The Player enter queue.
         */
        @Setting(value = "player-enter-queue", comment = "when player enter the queue")
        protected String playerEnterQueue = "&aYou have entered league {league} queue.";

        /**
         * The Player enter queue announcement.
         */
        @Setting(value = "player-enter-queue-announcement", comment = "when player enter the queue announcement")
        protected String playerEnterQueueAnnouncement = "&7Player &a&l{player}&7 has entered &a&l{league}&7 League. You can join by type &2/showdown q {league} enter&7.";

        /**
         * The Invalid team validated.
         */
        @Setting(value = "invalid-team-validated", comment = "player team is invalid")
        protected String invalidTeamValidated = "&cYour team &4&l{validate}&c does not valid. please use correct team format.";
        /**
         * The Invalid team size.
         */
        @Setting(value = "invalid-team-size", comment = "player invalid team size")
        protected String invalidTeamSize = "&cYour team size is too big. allowed &4&l{allowed}&c pokemons";

        /**
         * The Player leave queue.
         */
        @Setting(value = "player-leave-queue", comment = "when player leave the queue")
        protected String playerLeaveQueue = "&aYou have leaved league {league} queue.";

        /**
         * The Not in queue.
         */
        @Setting(value = "not-in-queue", comment = "player is not in a queue")
        protected String notInQueue = "&cYou are not in a queue.";

        /**
         * The League not found.
         */
        @Setting(value = "league-not-found", comment = "league not exists.")
        protected String leagueNotFound = "&aLeague name doesn't exists.";

        /**
         * The League leaderboard value.
         */
        @Setting(value = "league-leaderboard-value", comment = "leaderboard value info")
        protected String leagueLeaderboardValue = "&4&l{rank}&7. &a&l{player}&7: &aelo &8-> &a&l{elo}&r &8(&5Win Rate&8: &a&l{win-rate} &6%&8)";

        /**
         * The League stats value.
         */
        @Setting(value = "league-stats-value", comment = "stats value info")
        protected List<String> leagueStatsValue = Arrays.asList("", "&2&nLeague&r &8-> &a{league-name}", "&2&nElo&r &8-> &a{elo}", "&2&nWin Rate&r &8-> &a{win-rate} &6%", "&2&nWins&r &8-> &a{wins}", "&2&nLoses&r &8-> &a{loses}", "");

        /**
         * The League battle rules.
         */
        @Setting(value = "league-battle-rules", comment = "battle rules value info")
        protected List<String> leagueBattleRules = Arrays.asList(
                "",
                "&2Battle Type &8-> &a{type}",
                "&2Level Capacity &8-> &a{level-capacity}",
                "&2Raise level to capacity &8-> &a{raise-cap}",
                "&2Team Preview &8-> &a{team-preview}",
                "&2Turn Time &8-> &a{turn-time}",
                "&2Pokemon banned &8-> {hover-pokemon-banned}",
                "&2Move banned &8-> {hover-move-banned}",
                "&2Ability banned &8-> {hover-ability-banned}",
                "&2Item banned &8-> {hover-item-banned}",
                ""

        );

        /**
         * Gets prefix.
         *
         * @return the prefix
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * Gets win message.
         *
         * @return the win message
         */
        public String getWinMessage() {
            return winMessage;
        }

        /**
         * Gets lose message.
         *
         * @return the lose message
         */
        public String getLoseMessage() {
            return loseMessage;
        }

        /**
         * Gets starting message.
         *
         * @return the starting message
         */
        public String getStartingMessage() {
            return startingMessage;
        }

        /**
         * Gets already in battle.
         *
         * @return the already in battle
         */
        public String getAlreadyInBattle() {
            return alreadyInBattle;
        }

        /**
         * Gets your team has fainted.
         *
         * @return the your team has fainted
         */
        public String getYourTeamHasFainted() {
            return yourTeamHasFainted;
        }

        /**
         * Gets participant team has fainted.
         *
         * @return the participant team has fainted
         */
        public String getParticipantTeamHasFainted() {
            return participantTeamHasFainted;
        }

        /**
         * Gets your team not same.
         *
         * @return the your team not same
         */
        public String getYourTeamNotSame() {
            return yourTeamNotSame;
        }

        /**
         * Gets participant team not same.
         *
         * @return the participant team not same
         */
        public String getParticipantTeamNotSame() {
            return participantTeamNotSame;
        }

        /**
         * Gets your team not validated.
         *
         * @return the your team not validated
         */
        public String getYourTeamNotValidated() {
            return yourTeamNotValidated;
        }

        /**
         * Gets participant team not validated.
         *
         * @return the participant team not validated
         */
        public String getParticipantTeamNotValidated() {
            return participantTeamNotValidated;
        }

        /**
         * Gets you already in battle.
         *
         * @return the you already in battle
         */
        public String getYouAlreadyInBattle() {
            return youAlreadyInBattle;
        }

        /**
         * Gets participant already in battle.
         *
         * @return the participant already in battle
         */
        public String getParticipantAlreadyInBattle() {
            return participantAlreadyInBattle;
        }

        /**
         * Gets player not found battle.
         *
         * @return the player not found battle
         */
        public String getPlayerNotFoundBattle() {
            return playerNotFoundBattle;
        }

        /**
         * Gets player enter queue.
         *
         * @return the player enter queue
         */
        public String getPlayerEnterQueue() {
            return playerEnterQueue;
        }

        /**
         * Gets player leave queue.
         *
         * @return the player leave queue
         */
        public String getPlayerLeaveQueue() {
            return playerLeaveQueue;
        }

        /**
         * Gets not in queue.
         *
         * @return the not in queue
         */
        public String getNotInQueue() {
            return notInQueue;
        }

        /**
         * Gets league not found.
         *
         * @return the league not found
         */
        public String getLeagueNotFound() {
            return leagueNotFound;
        }

        /**
         * Gets league leaderboard value.
         *
         * @return the league leaderboard value
         */
        public String getLeagueLeaderboardValue() {
            return leagueLeaderboardValue;
        }

        /**
         * Get league stats value string [ ].
         *
         * @return the string [ ]
         */
        public List<String> getLeagueStatsValue() {
            return leagueStatsValue;
        }

        /**
         * Gets league battle rules.
         *
         * @return the league battle rules
         */
        public List<String> getLeagueBattleRules() {
            return leagueBattleRules;
        }

        /**
         * Gets hover text.
         *
         * @return the hover text
         */
        public String getHoverText() {
            return hoverText;
        }

        /**
         * Gets banned header.
         *
         * @return the banned header
         */
        public String getBannedHeader() {
            return bannedHeader;
        }

        /**
         * Gets yes text.
         *
         * @return the yes text
         */
        public String getYesText() {
            return yesText;
        }

        /**
         * Gets no text.
         *
         * @return the no text
         */
        public String getNoText() {
            return noText;
        }

        /**
         * Gets team double text.
         *
         * @return the team double text
         */
        public String getTeamDoubleText() {
            return teamDoubleText;
        }

        /**
         * Gets team single.
         *
         * @return the team single
         */
        public String getTeamSingle() {
            return teamSingle;
        }

        /**
         * Gets team double error.
         *
         * @return the team double error
         */
        public String getTeamDoubleError() {
            return teamDoubleError;
        }


        /**
         * Gets not in battle.
         *
         * @return the not in battle
         */
        public String getNotInBattle() {
            return notInBattle;
        }

        /**
         * Gets successfully draw.
         *
         * @return the successfully draw
         */
        public String getSuccessfullyDraw() {
            return successfullyDraw;
        }

        /**
         * Gets opponent decline.
         *
         * @return the opponent decline
         */
        public String getOpponentDecline() {
            return opponentDecline;
        }

        /**
         * Gets you decline.
         *
         * @return the you decline
         */
        public String getYouDecline() {
            return youDecline;
        }

        /**
         * Gets player ask for draw.
         *
         * @return the player ask for draw
         */
        public String getPlayerAskForDraw() {
            return playerAskForDraw;
        }

        /**
         * Gets opponent ask for draw.
         *
         * @return the opponent ask for draw
         */
        public String getOpponentAskForDraw() {
            return opponentAskForDraw;
        }

        /**
         * Gets invalid team validated.
         *
         * @return the invalid team validated
         */
        public String getInvalidTeamValidated() {
            return invalidTeamValidated;
        }

        /**
         * Gets invalid team size.
         *
         * @return the invalid team size
         */
        public String getInvalidTeamSize() {
            return invalidTeamSize;
        }

        /**
         * Gets player enter queue announcement.
         *
         * @return the player enter queue announcement
         */
        public String getPlayerEnterQueueAnnouncement() {
            return playerEnterQueueAnnouncement;
        }
    }

    /**
     * Gets general language battle.
     *
     * @return the general language battle
     */
    public GeneralLanguageBattle getGeneralLanguageBattle() {
        return generalLanguageBattle;
    }
}