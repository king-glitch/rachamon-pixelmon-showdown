package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import dev.rachamon.api.sponge.util.TextUtil;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.configs.BattleLeagueConfig;
import dev.rachamon.rachamonpixelmonshowdown.services.QueueService;
import dev.rachamon.rachamonpixelmonshowdown.structures.PlayerEloProfile;
import dev.rachamon.rachamonpixelmonshowdown.utils.ChatUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The type Rachamon pixelmon showdown match making manager.
 */
public class RachamonPixelmonShowdownMatchMakingManager {

    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();
    private static boolean isRunning = false;
    private static Task matchMaker;

    /**
     * Run task.
     */
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

                        .getQueueManagementCategorySetting()
                        .getMatchMakerTimer(), TimeUnit.SECONDS)
                .submit(RachamonPixelmonShowdown.getInstance());
    }

    private static void matchMake() {
        AtomicBoolean continueMatching = new AtomicBoolean(false);

        RachamonPixelmonShowdown.getInstance().getQueueManager().getQueue().forEach((k, v) -> {
            if (v.getInQueue().size() >= 2) {
                RachamonPixelmonShowdownMatchMakingManager.findMatch(v);
                continueMatching.set(true);
            }
        });

        if (!continueMatching.get()) {
            stopTask();
        }
    }

    /**
     * Start pre battle.
     *
     * @param playerUuid1 the player uuid 1
     * @param playerUuid2 the player uuid 2
     * @param ruleManager the rule manager
     */
    public static void startPreBattle(UUID playerUuid1, UUID playerUuid2, RachamonPixelmonShowdownRuleManager ruleManager) {
        Optional<Player> player1 = Sponge.getServer().getPlayer(playerUuid1);
        Optional<Player> player2 = Sponge.getServer().getPlayer(playerUuid2);

        if (!player1.isPresent() || !player2.isPresent()) {
            return;
        }

        int prepareTime = RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getQueueManagementCategorySetting()
                .getBattlePreparationTime();
        int warmupTime = RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getQueueManagementCategorySetting()
                .getTeamPreviewTime();

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

        ArrayList<Pokemon> playerPokemonList1 = RachamonPixelmonShowdownMatchMakingManager.filterNotNullPokemonParty(playerParty1);
        ArrayList<Pokemon> playerPokemonList2 = RachamonPixelmonShowdownMatchMakingManager.filterNotNullPokemonParty(playerParty2);

        if (ruleManager.getLeague().getBattleRule().isEnableTeamPreview()) {
            Task.builder().execute(() -> {
                try {
                    PaginationList.Builder builder = PaginationList
                            .builder()
                            .title(TextUtil.toText("&6&l" + player2.get().getName() + " Party"))
                            .padding(TextUtil.toText("&8="));


                    List<Text> contents = new ArrayList<>();
                    int i = 1;

                    for (Pokemon pokemon : playerPokemonList2) {
                        contents.add(TextUtil.toText(i + ". " + pokemon.getSpecies()));
                        i++;
                    }

                    builder.contents(contents).sendTo(player1.get());
                    builder = PaginationList
                            .builder()
                            .title(TextUtil.toText("&6&l" + player1.get().getName() + " Party"))
                            .padding(TextUtil.toText("&8="));

                    contents.clear();

                    i = 1;
                    for (Pokemon pokemon : playerPokemonList1) {
                        contents.add(TextUtil.toText(i + ". " + pokemon.getSpecies()));
                        i++;
                    }

                    builder.contents(contents).sendTo(player2.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).delay(prepareTime - warmupTime, TimeUnit.SECONDS).submit(RachamonPixelmonShowdown.getInstance());


        }

        Task.builder().execute(() -> {
            try {
                RachamonPixelmonShowdownMatchMakingManager.startBattle(playerUuid1, playerPokemonList1, null, playerUuid2, playerPokemonList2, null, ruleManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).delay(prepareTime, TimeUnit.SECONDS).submit(RachamonPixelmonShowdown.getInstance());
    }

    private static ArrayList<Pokemon> filterNotNullPokemonParty(Pokemon[] pokemons) {
        ArrayList<Pokemon> playerPokemonList = new ArrayList<>();

        for (Pokemon pokemon : pokemons) {
            if (pokemon == null) {
                continue;
            }

            playerPokemonList.add(pokemon);
        }

        return playerPokemonList;
    }

    /**
     * Start battle.
     *
     * @param uuid1                   the uuid 1
     * @param playerOnePokemons       the player one pokemons
     * @param playerOneStarterPokemon the player one starter pokemon
     * @param uuid2                   the uuid 2
     * @param playerTwoPokemons       the player two pokemons
     * @param playerTwoStarterPokemon the player two starter pokemon
     * @param ruleManager             the rule manager
     * @throws Exception the exception
     */
    public static void startBattle(UUID uuid1, ArrayList<Pokemon> playerOnePokemons, Pokemon playerOneStarterPokemon, UUID uuid2, ArrayList<Pokemon> playerTwoPokemons, Pokemon playerTwoStarterPokemon, RachamonPixelmonShowdownRuleManager ruleManager) throws Exception {

        QueueService queueService = RachamonPixelmonShowdown
                .getInstance()
                .getQueueManager()
                .findQueue(ruleManager.getLeagueName());
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
        Pokemon[] playerTwoPokemonParty = Pixelmon.storageManager.getParty(participant2).getAll();

        ArrayList<Pokemon> playerOnePokemonsList = RachamonPixelmonShowdownMatchMakingManager.filterNotNullPokemonParty(playerOnePokemonParty);
        ArrayList<Pokemon> playerTwoPokemonsList = RachamonPixelmonShowdownMatchMakingManager.filterNotNullPokemonParty(playerTwoPokemonParty);

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

            return;

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
            RachamonPixelmonShowdown.getInstance().getLogger().debug("not same party size");
            return false;
        }

        for (Pokemon pokemon : party1) {
            if (!party2.contains(pokemon)) {
                RachamonPixelmonShowdown.getInstance().getLogger().debug("party pokemon not exist in party 2");
                return false;
            }
        }

        return true;
    }

    /**
     * Find match.
     *
     * @param queue the queue
     */
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

    /**
     * Stop task.
     */
    public static void stopTask() {
        if (!RachamonPixelmonShowdownMatchMakingManager.isRunning) {
            return;
        }

        RachamonPixelmonShowdownMatchMakingManager.isRunning = false;
        matchMaker.cancel();

    }

    /**
     * Leave queue.
     *
     * @param leagueName the league name
     * @param player     the player
     * @throws Exception the exception
     */
    public void leaveQueue(String leagueName, Player player) throws Exception {

        QueueService queueService = RachamonPixelmonShowdown
                .getInstance()
                .getQueueManager()
                .getPlayerInQueue(player.getUniqueId());


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

    /**
     * League stats.
     *
     * @param leagueName the league name
     * @param player     the player
     * @throws Exception the exception
     */
    public void leagueStats(String leagueName, Player player) throws Exception {

        QueueService queueService = RachamonPixelmonShowdown.getInstance().getQueueManager().findQueue(leagueName);
        RachamonPixelmonShowdownEloManager eloManager = queueService.getEloManager();
        PlayerEloProfile profile = eloManager.getProfileData(player.getUniqueId());
        int wins = profile.getWin();
        int loses = profile.getLose();
        int elo = profile.getElo();
        double winRate = Math.round(wins * 100.0 / (wins + loses));

        PaginationList.Builder builder = PaginationList
                .builder()
                .title(TextUtil.toText("&6&lStats"))
                .padding(TextUtil.toText("&8="));


        List<Text> contents = new ArrayList<>();

        for (String text : this.plugin.getLanguage().getGeneralLanguageBattle().getLeagueStatsValue()) {
            text = text
                    .replaceAll("\\{league-name}", leagueName + "")
                    .replaceAll("\\{win-rate}", winRate + "")
                    .replaceAll("\\{wins}", wins + "")
                    .replaceAll("\\{loses}", loses + "")
                    .replaceAll("\\{elo}", elo + "");
            contents.add(TextUtil.toText(text));
        }

        builder.contents(contents).sendTo(player);
    }

    /**
     * League leaderboard.
     *
     * @param leagueName the league name
     * @param player     the player
     * @throws Exception the exception
     */
    public void leagueLeaderboard(String leagueName, Player player) throws Exception {

        QueueService queueService = RachamonPixelmonShowdown.getInstance().getQueueManager().findQueue(leagueName);

        RachamonPixelmonShowdownEloManager eloManager = queueService.getEloManager();


        PaginationList.Builder builder = PaginationList
                .builder()
                .title(TextUtil.toText("&6&lLeaderboard"))
                .padding(TextUtil.toText("&8="));


        List<Text> contents = new ArrayList<>();
        int i = 1;
        for (UUID uuid : eloManager.getRanks()) {

            PlayerEloProfile profile = eloManager.getProfileData(uuid);

            String text = plugin
                    .getLanguage()
                    .getGeneralLanguageBattle()
                    .getLeagueLeaderboardValue()
                    .replaceAll("\\{rank}", String.valueOf(i))
                    .replaceAll("\\{player}", Objects
                            .requireNonNull(Objects.requireNonNull(Sponge
                                    .getServiceManager()
                                    .provide(UserStorageService.class)
                                    .flatMap(storage -> storage.get(uuid))
                                    .orElse(null)))
                            .getName())
                    .replaceAll("\\{elo}", String.valueOf(profile.getElo()));
            contents.add(TextUtil.toText(text));
            i++;
        }

        builder.contents(contents).sendTo(player);

    }

    /**
     * League rules.
     *
     * @param leagueName the league name
     */
    public void leagueRules(String leagueName, Player player) throws Exception {

        QueueService queueService = RachamonPixelmonShowdown.getInstance().getQueueManager().findQueue(leagueName);

        BattleLeagueConfig.BattleRule rules = queueService.getRuleManager().getLeague().getBattleRule();

        PaginationList.Builder builder = PaginationList
                .builder()
                .title(TextUtil.toText("&6&lBattle Rules"))
                .padding(TextUtil.toText("&8="));


        List<Text> contents = new ArrayList<>();
        for (String text : this.plugin.getLanguage().getGeneralLanguageBattle().getLeagueBattleRules()) {
            text = text
                    .replaceAll("\\{type}", rules.isDoubleBattle() ? "Double" : "Single" + "")
                    .replaceAll("\\{level-capacity}", rules.getLevelCapacity() + "")
                    .replaceAll("\\{raise-cap}", rules.isRaiseMaxLevel() ? "Yes" : "No" + "")
                    .replaceAll("\\{team-preview}", rules.isEnableTeamPreview() ? "Yes" : "No" + "")
                    .replaceAll("\\{turn-time}", rules.getTurnTime() + " seconds.");

            contents.add(TextUtil.toText(text));
        }

        builder.contents(contents).sendTo(player);

    }

    /**
     * Enter queue.
     *
     * @param leagueName the league name
     * @param player     the player
     * @throws Exception the exception
     */
    public void enterQueue(String leagueName, Player player) throws Exception {

        if (RachamonPixelmonShowdown.getInstance().getQueueManager().isPlayerInAction(player.getUniqueId())) {
            throw new Exception(RachamonPixelmonShowdown
                    .getInstance()
                    .getLanguage()
                    .getGeneralLanguageBattle()
                    .getYouAlreadyInBattle());
        }


        EntityPlayerMP participant = (EntityPlayerMP) player;

        Pokemon[] party = Arrays
                .stream(Pixelmon.storageManager.getParty(participant).getAll())
                .filter(Objects::nonNull)
                .toArray(Pokemon[]::new);

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

        queueService.addPlayerInQueue(player.getUniqueId());

        for (UUID uuid : queueService.getInQueue()) {
            this.plugin.getLogger().debug("enter in queue: " + uuid);
        }

        RachamonPixelmonShowdownMatchMakingManager.runTask();

        ChatUtil.sendMessage(player, RachamonPixelmonShowdown
                .getInstance()
                .getLanguage()
                .getGeneralLanguageBattle()
                .getPlayerEnterQueue()
                .replaceAll("\\{league}", leagueName));
    }

}
