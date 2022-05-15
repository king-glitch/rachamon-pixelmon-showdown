package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.services.QueueService;
import dev.rachamon.rachamonpixelmonshowdown.structures.PlayerEloProfile;
import dev.rachamon.rachamonpixelmonshowdown.utils.ChatUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RachamonPixelmonShowdownMatchMakingManager {

    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();
    private final static RachamonPixelmonShowdownQueueManager queueManager = RachamonPixelmonShowdown
            .getInstance()
            .getQueueManager();
    private static boolean isRunning = false;
    private static Task matchMaker;

    public static void runTask() {
        if (RachamonPixelmonShowdownMatchMakingManager.isRunning) {
            return;
        }

        RachamonPixelmonShowdownMatchMakingManager.isRunning = true;
        matchMaker = Task
                .builder()
                .execute(RachamonPixelmonShowdownMatchMakingManager::matchMake)
                .interval(RachamonPixelmonShowdown
                        .getInstance()
                        .getConfig()
                        .getRoot()
                        .getQueueManagementCategorySetting()
                        .getMatchMakerTimer(), TimeUnit.SECONDS)
                .submit(RachamonPixelmonShowdown.getInstance());
    }

    private static void matchMake() {
        AtomicBoolean continueMatching = new AtomicBoolean(false);

        queueManager.getQueue().forEach((k, v) -> {
            if (v.getInQueue().size() >= 2) {
                RachamonPixelmonShowdownMatchMakingManager.findMatch(v);
                continueMatching.set(true);
            }
        });

        if (!continueMatching.get()) {
            stopTask();
        }
    }

    public static void startPreBattle(UUID playerUuid1, UUID playerUuid2, RachamonPixelmonShowdownRuleManager ruleManager) {
        Optional<Player> player1 = Sponge.getServer().getPlayer(playerUuid1);
        Optional<Player> player2 = Sponge.getServer().getPlayer(playerUuid2);

        if (!player1.isPresent() || !player2.isPresent()) {
            return;
        }

        int prepareTime = RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getRoot()
                .getQueueManagementCategorySetting()
                .getBattlePreparationTime();

        String preMatchMessage = RachamonPixelmonShowdown
                .getInstance()
                .getLanguage()
                .getGeneralLanguageBattle()
                .getStartingMessage()
                .replaceAll("\\{time}", prepareTime + "");

        ChatUtil.sendMessage(player1.get(), preMatchMessage);
        ChatUtil.sendMessage(player2.get(), preMatchMessage);

        EntityPlayerMP participant1 = (EntityPlayerMP) player1.get();
        EntityPlayerMP participant2 = (EntityPlayerMP) player2.get();
        Pokemon[] playerParty1 = Pixelmon.storageManager.getParty(participant1).getAll();
        Pokemon[] playerParty2 = Pixelmon.storageManager.getParty(participant2).getAll();
        ArrayList<Pokemon> playerPokemonList1 = new ArrayList<>(Arrays.asList((Pokemon[]) Arrays
                .stream(playerParty1)
                .filter(Objects::nonNull)
                .toArray()));
        ArrayList<Pokemon> playerPokemonList2 = new ArrayList<>(Arrays.asList((Pokemon[]) Arrays
                .stream(playerParty2)
                .filter(Objects::nonNull)
                .toArray()));

        if (ruleManager.getLeague().getBattleRule().isEnableTeamPreview()) {
            // TODO: add chat team preview
        }

        Task start = Task.builder().execute(() -> {
            RachamonPixelmonShowdownMatchMakingManager.startBattle(playerUuid1, playerPokemonList1, null, playerUuid2, playerPokemonList2, null, ruleManager);
        }).delay(prepareTime, TimeUnit.SECONDS).submit(RachamonPixelmonShowdown.getInstance());

    }

    public static void startBattle(UUID uuid1, ArrayList<Pokemon> playerOnePokemons, Pokemon playerOneStarterPokemon, UUID uuid2, ArrayList<Pokemon> playerTwoPokemons, Pokemon playerTwoStarterPokemon, RachamonPixelmonShowdownRuleManager ruleManager) {

        QueueService queueService = queueManager.findQueue(ruleManager.getLeagueName());
        queueService.addPlayerInMatch(uuid1);
        queueService.addPlayerInMatch(uuid2);

        Optional<Player> playerOne = Sponge.getServer().getPlayer(uuid1);
        Optional<Player> playerTwo = Sponge.getServer().getPlayer(uuid2);

        if (!playerOne.isPresent() || !playerTwo.isPresent()) {

            String playerNotFound = RachamonPixelmonShowdown
                    .getInstance()
                    .getLanguage()
                    .getGeneralLanguageBattle()
                    .getPlayerNotFoundBattle();

            playerOne.ifPresent(player -> ChatUtil.sendMessage(player, playerNotFound));
            playerTwo.ifPresent(player -> ChatUtil.sendMessage(player, playerNotFound));

            queueService.removePlayerInMatch(uuid1);
            queueService.removePlayerInMatch(uuid2);

            return;
        }

        EntityPlayerMP participant1 = (EntityPlayerMP) playerOne.get();
        EntityPlayerMP participant2 = (EntityPlayerMP) playerTwo.get();


        if (BattleRegistry.getBattle(participant1) != null || BattleRegistry.getBattle(participant2) != null) {
            String text = RachamonPixelmonShowdown
                    .getInstance()
                    .getLanguage()
                    .getGeneralLanguageBattle()
                    .getAlreadyInBattle();

            ChatUtil.sendMessage(playerOne.get(), text);
            ChatUtil.sendMessage(playerTwo.get(), text);

            queueService.removePlayerInMatch(uuid1);
            queueService.removePlayerInMatch(uuid2);

            return;

        }

        Pokemon[] playerOnePokemonParty = Pixelmon.storageManager.getParty(participant1).getAll();
        Pokemon[] playerTwoPokemonParty = Pixelmon.storageManager.getParty(participant1).getAll();


        ArrayList<Pokemon> playerOnePokemonsList = new ArrayList<>(Arrays.asList((Pokemon[]) Arrays
                .stream(playerOnePokemonParty)
                .filter(Objects::nonNull)
                .toArray()));
        ArrayList<Pokemon> playerTwoPokemonsList = new ArrayList<>(Arrays.asList((Pokemon[]) Arrays
                .stream(playerTwoPokemonParty)
                .filter(Objects::nonNull)
                .toArray()));

        boolean playerOneSameParty = RachamonPixelmonShowdownMatchMakingManager.isPartySame(playerOnePokemons, playerOnePokemonsList);
        boolean playerTwoSameParty = RachamonPixelmonShowdownMatchMakingManager.isPartySame(playerTwoPokemons, playerTwoPokemonsList);


        if (!playerOneSameParty || !playerTwoSameParty) {

            if (!playerOneSameParty) {
                ChatUtil.sendMessage(playerOne.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getYourTeamNotSame());
                ChatUtil.sendMessage(playerTwo.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getParticipantTeamNotSame());
            }

            if (!playerTwoSameParty) {
                ChatUtil.sendMessage(playerTwo.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getYourTeamNotSame());
                ChatUtil.sendMessage(playerOne.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getParticipantTeamNotSame());
            }

            queueService.removePlayerInMatch(uuid1);
            queueService.removePlayerInMatch(uuid2);

        }

        boolean playerOneTeamFainted = playerOnePokemons.stream().anyMatch(pokemon -> pokemon.getHealth() == 0);
        boolean playerTwoTeamFainted = playerTwoPokemons.stream().anyMatch(pokemon -> pokemon.getHealth() == 0);

        if (playerOneTeamFainted || playerTwoTeamFainted) {
            if (playerOneTeamFainted) {
                ChatUtil.sendMessage(playerOne.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getYourTeamHasFainted());
                ChatUtil.sendMessage(playerTwo.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getParticipantTeamHasFainted());
            }

            if (playerTwoTeamFainted) {
                ChatUtil.sendMessage(playerTwo.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getYourTeamHasFainted());
                ChatUtil.sendMessage(playerOne.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getParticipantTeamHasFainted());
            }

            queueService.removePlayerInMatch(uuid1);
            queueService.removePlayerInMatch(uuid2);

            return;
        }

        boolean playerOneValidated = ruleManager.getBattleRules().validateTeam(playerOnePokemonsList) == null;
        boolean playerTwoValidated = ruleManager.getBattleRules().validateTeam(playerTwoPokemonsList) == null;

        if (!playerOneValidated || !playerTwoValidated) {
            if (playerOneValidated) {
                ChatUtil.sendMessage(playerOne.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getYourTeamNotValidated());
                ChatUtil.sendMessage(playerTwo.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getParticipantTeamNotValidated());
            }

            if (playerTwoValidated) {
                ChatUtil.sendMessage(playerTwo.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getYourTeamNotValidated());
                ChatUtil.sendMessage(playerOne.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getParticipantTeamNotValidated());
            }

            queueService.removePlayerInMatch(uuid1);
            queueService.removePlayerInMatch(uuid2);

            return;
        }

        boolean playerOneInBattle = BattleRegistry.getBattle(participant1) != null;
        boolean playerTwoInBattle = BattleRegistry.getBattle(participant2) != null;

        if (playerOneInBattle || playerTwoInBattle) {
            if (playerOneInBattle) {
                ChatUtil.sendMessage(playerOne.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getYouAlreadyInBattle());
                ChatUtil.sendMessage(playerTwo.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getParticipantAlreadyInBattle());
            }

            if (playerTwoInBattle) {
                ChatUtil.sendMessage(playerTwo.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getYouAlreadyInBattle());
                ChatUtil.sendMessage(playerOne.get(), RachamonPixelmonShowdown
                        .getInstance()
                        .getLanguage()
                        .getGeneralLanguageBattle()
                        .getParticipantAlreadyInBattle());
            }

            queueService.removePlayerInMatch(uuid1);
            queueService.removePlayerInMatch(uuid2);

            return;
        }
        EntityPixelmon participantOneStarter;
        EntityPixelmon participantTwoStarter;
        if (playerOneStarterPokemon == null) {
            participantOneStarter = Pixelmon.storageManager
                    .getParty(participant1)
                    .getAndSendOutFirstAblePokemon(participant1);
        } else {
            participantOneStarter = playerOneStarterPokemon.getOrSpawnPixelmon(participant1);
        }

        if (playerTwoStarterPokemon == null) {
            participantTwoStarter = Pixelmon.storageManager
                    .getParty(participant2)
                    .getAndSendOutFirstAblePokemon(participant2);
        } else {
            participantTwoStarter = playerTwoStarterPokemon.getOrSpawnPixelmon(participant2);
        }

        PlayerParticipant[] playerOneParticipant = {new PlayerParticipant(participant1, participantOneStarter)};
        PlayerParticipant[] playerTwoParticipant = {new PlayerParticipant(participant2, participantTwoStarter)};

        BattleRegistry.startBattle(playerOneParticipant, playerTwoParticipant, ruleManager.getBattleRules());

    }

    private static boolean isPartySame(ArrayList<Pokemon> party1, ArrayList<Pokemon> party2) {
        if (party1.size() != party2.size()) {
            return false;
        }

        for (Pokemon pokemon : party1) {
            if (!party2.contains(pokemon)) {
                return false;
            }
        }

        return true;
    }

    public static void findMatch(QueueService queue) {
        ArrayList<UUID> toRemove = new ArrayList<>();
        ArrayList<UUID> inQueue = queue.getInQueue();
        RachamonPixelmonShowdownEloManager eloManager = queue.getEloManager();

        for (UUID uuid : inQueue) {

            if (toRemove.contains(uuid)) {
                continue;
            }

            int lowestMatchValue = -1;
            PlayerEloProfile playerProfile = eloManager.getProfileData(uuid);
            UUID opponent = null;

            for (UUID uuid2 : inQueue) {
                if (toRemove.contains(uuid2)) {
                    continue;
                }

                if (uuid.equals(uuid2)) {
                    continue;
                }

                PlayerEloProfile opponentProfile = eloManager.getProfileData(uuid2);
                int matchValue = Math.abs(playerProfile.getElo() - opponentProfile.getElo());

                if (matchValue <= RachamonPixelmonShowdown
                        .getInstance()
                        .getConfig()
                        .getRoot()
                        .getQueueManagementCategorySetting()
                        .getMatchThresholdValue() && (matchValue <= lowestMatchValue || lowestMatchValue == -1)) {

                    lowestMatchValue = matchValue;
                    opponent = uuid2;
                }

            }

            if (opponent != null) {
                toRemove.add(opponent);
                toRemove.add(uuid);
                RachamonPixelmonShowdownMatchMakingManager.startPreBattle(uuid, opponent, queue.getRuleManager());

            }
        }

        for (UUID uuid : toRemove) {
            queue.addPlayerInPreMatch(uuid);
        }

    }

    public static void stopTask() {
        if (!RachamonPixelmonShowdownMatchMakingManager.isRunning) {
            return;
        }

        RachamonPixelmonShowdownMatchMakingManager.isRunning = false;
        matchMaker.cancel();

    }

    public void leaveQueue(String leagueName, Player player) throws Exception {

        QueueService queueService = RachamonPixelmonShowdownMatchMakingManager.queueManager.getPlayerInQueue(player.getUniqueId());

        if (queueService == null) {
            throw new Exception(RachamonPixelmonShowdown
                    .getInstance()
                    .getLanguage()
                    .getGeneralLanguageBattle()
                    .getNotInQueue());
        }


        queueService.removePlayerInQueue(player.getUniqueId());


        ChatUtil.sendMessage(player, RachamonPixelmonShowdown
                .getInstance()
                .getLanguage()
                .getGeneralLanguageBattle()
                .getPlayerLeaveQueue()
                .replaceAll("\\{league}", leagueName));

    }

    public void leagueStats() {
    }

    public void leagueLeaderboard() {
    }

    public void leagueRules() {
    }

    public void enterQueue(String leagueName, Player player) throws Exception {

        if (RachamonPixelmonShowdownMatchMakingManager.queueManager.isPlayerInMatch(player.getUniqueId()) || RachamonPixelmonShowdownMatchMakingManager.queueManager.isPlayerInPreMatch(player.getUniqueId())) {
            throw new Exception(RachamonPixelmonShowdown
                    .getInstance()
                    .getLanguage()
                    .getGeneralLanguageBattle()
                    .getYouAlreadyInBattle());
        }


        EntityPlayerMP participant = (EntityPlayerMP) player;

        Pokemon[] party = (Pokemon[]) Arrays
                .stream(Pixelmon.storageManager.getParty(participant).getAll())
                .filter(Objects::nonNull)
                .toArray();
        ArrayList<Pokemon> pokemonArrayList = new ArrayList<>(Arrays.asList(party));

        QueueService queueService = this.plugin.getQueueManager().findQueue(leagueName);

        if (queueService == null) {
            throw new Exception(RachamonPixelmonShowdown
                    .getInstance()
                    .getLanguage()
                    .getGeneralLanguageBattle()
                    .getLeagueNotFound());
        }

        BattleRules rules = queueService.getRuleManager().getBattleRules();

        boolean isTeamValidate = rules.validateTeam(pokemonArrayList) == null;

        if (!isTeamValidate) {
            return;
        }

        queueService.addPlayerInQueue(participant.getUniqueID());

        RachamonPixelmonShowdownMatchMakingManager.runTask();

        ChatUtil.sendMessage(player, RachamonPixelmonShowdown
                .getInstance()
                .getLanguage()
                .getGeneralLanguageBattle()
                .getPlayerEnterQueue()
                .replaceAll("\\{league}", leagueName));
    }

}
