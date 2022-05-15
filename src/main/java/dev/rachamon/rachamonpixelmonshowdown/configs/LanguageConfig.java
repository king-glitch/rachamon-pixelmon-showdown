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

        @Setting(value = "already-in-battle", comment = "one player is already in battle")
        protected String alreadyInBattle = "A participant is already in battle! Battle cancelled.";

        @Setting(value = "your-team-has-fainted", comment = "some pokemon has been fainted")
        protected String yourTeamHasFainted = "&cOne of your pokemon has been fained.";

        @Setting(value = "participant-team-has-fainted", comment = "some pokemon has been fainted")
        protected String participantTeamHasFainted = "&cOne of your participant pokemon has been fained.";

        @Setting(value = "your-team-not-same", comment = "team party not same")
        protected String yourTeamNotSame = "&cYour party is not the same as what you queued with!";

        @Setting(value = "participant-team-not-same", comment = "team party not same")
        protected String participantTeamNotSame = "&cA participant's team was found ineligible! Battle cancelled.";

        @Setting(value = "your-team-not-validated", comment = "team party not validated rules")
        protected String yourTeamNotValidated = "&cYour party does not follow the formats rules!";

        @Setting(value = "participant-team-not-validated", comment = "team party not validated rules")
        protected String participantTeamNotValidated = "&cA participant's team did not follow the format's rules! Battle cancelled";

        @Setting(value = "you-already-in-battle", comment = "already in battle")
        protected String youAlreadyInBattle = "&cYou are already in battle!";

        @Setting(value = "participant-already-in-battle", comment = "already in battle")
        protected String participantAlreadyInBattle = "&cA participant is already in battle! Battle cancelled.";

        @Setting(value = "player-not-found-battle", comment = "player not found")
        protected String playerNotFoundBattle = "&cPlayer not found, A player might be disconnected! Battle cancelled.";

        @Setting(value = "player-enter-queue", comment = "when player enter the queue")
        protected String playerEnterQueue = "&aYou have entered league {league} queue.";

        @Setting(value = "player-leave-queue", comment = "when player leave the queue")
        protected String playerLeaveQueue = "&aYou have leaved league {league} queue.";

        @Setting(value = "not-in-queue", comment = "player is not in a queue")
        protected String notInQueue = "&aYou are not in a queue.";

        @Setting(value = "league-not-found", comment = "league not exists.")
        protected String leagueNotFound = "&aLeague name doesn't exists.";

        @Setting(value = "league-leaderboard-value", comment = "leaderboard value info")
        protected String leagueLeaderboardValue = "&c{rank}&7. &a&l{player}&r &aelo&8: &a&l{elo}&r.";

        @Setting(value = "league-stats-value", comment = "stats value info")
        protected String[] leagueStatsValue = new String[]{
                "",
                "Elo: {elo}",
                "Winrate: {win-rate}",
                "Wins: {wins}",
                "Loses: {loses}",
                "",
        };

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

        public String getAlreadyInBattle() {
            return alreadyInBattle;
        }

        public String getYourTeamHasFainted() {
            return yourTeamHasFainted;
        }

        public String getParticipantTeamHasFainted() {
            return participantTeamHasFainted;
        }

        public String getYourTeamNotSame() {
            return yourTeamNotSame;
        }

        public String getParticipantTeamNotSame() {
            return participantTeamNotSame;
        }

        public String getYourTeamNotValidated() {
            return yourTeamNotValidated;
        }

        public String getParticipantTeamNotValidated() {
            return participantTeamNotValidated;
        }

        public String getYouAlreadyInBattle() {
            return youAlreadyInBattle;
        }

        public String getParticipantAlreadyInBattle() {
            return participantAlreadyInBattle;
        }

        public String getPlayerNotFoundBattle() {
            return playerNotFoundBattle;
        }

        public String getPlayerEnterQueue() {
            return playerEnterQueue;
        }

        public String getPlayerLeaveQueue() {
            return playerLeaveQueue;
        }

        public String getNotInQueue() {
            return notInQueue;
        }

        public String getLeagueNotFound() {
            return leagueNotFound;
        }

        public String getLeagueLeaderboardValue() {
            return leagueLeaderboardValue;
        }

        public String[] getLeagueStatsValue() {
            return leagueStatsValue;
        }
    }

    public GeneralLanguageBattle getGeneralLanguageBattle() {
        return generalLanguageBattle;
    }
}