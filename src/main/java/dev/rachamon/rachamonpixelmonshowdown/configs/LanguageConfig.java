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

        @Setting(value = "hover-text", comment = "hover text display")
        protected String hoverText = "&8[&a&lHOVER&8]&r";

        @Setting(value = "banned-header", comment = "banned title")
        protected String bannedHeader = "&4&lBanned&7";

        @Setting(value = "yes-text", comment = "yes text")
        protected String yesText = "&a&lYes&7";

        @Setting(value = "no-text", comment = "no text")
        protected String noText = "&c&lNo&7";

        @Setting(value = "team-double-text", comment = "team double text")
        protected String teamDoubleText = "&5&lDouble&7";

        @Setting(value = "team-single-text", comment = "team single text")
        protected String teamSingle = "&6&lSingle&7";

        @Setting(value = "team-double-error", comment = "error when player has only one pokemon when battle type is double.")
        protected String teamDoubleError = "&cThis is double team, please put more than one pokemon in your party.";


        @Setting(value = "not-in-battle", comment = "error when no in a battle")
        protected String notInBattle = "&cYou or your opponent is not in a battle.";

        @Setting(value = "successfully-draw", comment = "draw successfully message")
        protected String successfullyDraw = "&aYou have successfully draw with your opponent. no point will be lose.";

        @Setting(value = "player-ask-for-draw", comment = "message when player ask for a draw")
        protected String playerAskForDraw = "&7You have request a draw to an opponent.";

        @Setting(value = "opponent-ask-for-draw", comment = "message when player has ask an opponent for a draw")
        protected String opponentAskForDraw = "&7Your opponent has asked you for a draw, would you like to accept?";

        @Setting(comment = "when opponent decline draw", value = "opponent-decline")
        protected String opponentDecline = "&cYour opponent has decline";

        /**
         * The Click to view.
         */
        @Setting(comment = "question click to view.", value = "click-to-view")
        protected String clickToView = "&aClick to View";

        /**
         * The Click to answer.
         */
        @Setting(comment = "question click to answer.", value = "click-to-answer")
        protected String clickToAnswer = "&aClick to Answer";

        /**
         * The Must be player.
         */
        @Setting(comment = "question click to view.", value = "must-be-player")
        protected String mustBePlayer = "&cYou must be player to answer this question";

        /**
         * The Already responded.
         */
        @Setting(comment = "question click to view.", value = "already-responded")
        protected String alreadyResponded = "&cYou have already responded to that question!";

        @Setting(value = "accept-text", comment = "accept button")
        protected String acceptText = "&8[&aAccept&8]";

        @Setting(value = "decline-text", comment = "decline button")
        protected String declineText = "&8[&aDecline&8]";
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
        protected String leagueLeaderboardValue = "&c{rank}&7. &a&l{player}&r &aelo&8: &a&l{elo}&r.";

        /**
         * The League stats value.
         */
        @Setting(value = "league-stats-value", comment = "stats value info")
        protected List<String> leagueStatsValue = Arrays.asList("", "&2&lElo&8: &a{league-name}", "&2&lElo: &8: &a{elo}", "&2&lWin-rate: &8: &a{win-rate}", "&2&lWins: &8: &a{wins}", "&2&lLoses: &8: &a{loses}", "");

        @Setting(value = "league-battle-rules", comment = "battle rules value info")
        protected List<String> leagueBattleRules = Arrays.asList(
                "",
                "&2&lBattle Type&8: &a{type}",
                "&2&lLevel Capacity&8: &a{level-capacity}",
                "&2&lRaise level to capacity&8: &a{raise-cap}",
                "&2&lTeam Preview&8: &a{team-preview}",
                "&2&lTurn Time&8: &a{turn-time}",
                "&2&lPokemon banned&8: {hover-pokemon-banned}",
                "&2&lMove banned&8: {hover-move-banned}",
                "&2&lAbility banned&8: {hover-ability-banned}",
                "&2&lItem banned&8: {hover-item-banned}",
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

        public List<String> getLeagueBattleRules() {
            return leagueBattleRules;
        }

        public String getHoverText() {
            return hoverText;
        }

        public String getBannedHeader() {
            return bannedHeader;
        }

        public String getYesText() {
            return yesText;
        }

        public String getNoText() {
            return noText;
        }

        public String getTeamDoubleText() {
            return teamDoubleText;
        }

        public String getTeamSingle() {
            return teamSingle;
        }

        public String getTeamDoubleError() {
            return teamDoubleError;
        }

        public String getPlayerAskForDraw() {
            return playerAskForDraw;
        }

        public String getOpponentAskForDraw() {
            return opponentAskForDraw;
        }

        public String getAcceptText() {
            return acceptText;
        }

        public String getDeclineText() {
            return declineText;
        }

        public String getNotInBattle() {
            return notInBattle;
        }

        public String getSuccessfullyDraw() {
            return successfullyDraw;
        }

        public String getClickToView() {
            return clickToView;
        }

        public String getClickToAnswer() {
            return clickToAnswer;
        }

        public String getMustBePlayer() {
            return mustBePlayer;
        }

        public String getAlreadyResponded() {
            return alreadyResponded;
        }

        public String getOpponentDecline() {
            return opponentDecline;
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