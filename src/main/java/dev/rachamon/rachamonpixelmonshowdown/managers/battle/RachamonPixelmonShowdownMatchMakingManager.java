package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import dev.rachamon.api.sponge.util.TextUtil;
import dev.rachamon.api.sponge.util.chatquestion.ChatQuestion;
import dev.rachamon.api.sponge.util.chatquestion.ChatQuestionAnswer;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.configs.BattleLeagueConfig;
import dev.rachamon.rachamonpixelmonshowdown.configs.LanguageConfig;
import dev.rachamon.rachamonpixelmonshowdown.services.QueueService;
import dev.rachamon.rachamonpixelmonshowdown.structures.PlayerEloProfile;
import dev.rachamon.rachamonpixelmonshowdown.utils.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

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

        Pokemon[] playerOneParty = Pixelmon.storageManager.getParty(participant1).getAll();
        Pokemon[] playerTwoParty = Pixelmon.storageManager.getParty(participant2).getAll();

        ArrayList<Pokemon> playerPokemonList1 = new ArrayList<>(Arrays.asList(RachamonPixelmonShowdownMatchMakingManager.filterNotNullPokemonParty(playerOneParty)));
        ArrayList<Pokemon> playerPokemonList2 = new ArrayList<>(Arrays.asList(RachamonPixelmonShowdownMatchMakingManager.filterNotNullPokemonParty(playerTwoParty)));

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
                RachamonPixelmonShowdownMatchMakingManager.startBattle(playerUuid1, playerPokemonList1, playerUuid2, playerPokemonList2, ruleManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).delay(prepareTime, TimeUnit.SECONDS).submit(RachamonPixelmonShowdown.getInstance());
    }

    private static Pokemon[] filterNotNullPokemonParty(Pokemon[] pokemons) {
        return Arrays.stream(pokemons).filter(Objects::nonNull).toArray(Pokemon[]::new);
    }

    /**
     * Start battle.
     *
     * @param uuid1             the uuid 1
     * @param playerOnePokemons the player one pokemons
     * @param uuid2             the uuid 2
     * @param playerTwoPokemons the player two pokemons
     * @param ruleManager       the rule manager
     * @throws Exception the exception
     */
    public static void startBattle(UUID uuid1, ArrayList<Pokemon> playerOnePokemons, UUID uuid2, ArrayList<Pokemon> playerTwoPokemons, RachamonPixelmonShowdownRuleManager ruleManager) throws Exception {

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

        ArrayList<Pokemon> playerOnePokemonsList = new ArrayList<>(Arrays.asList(RachamonPixelmonShowdownMatchMakingManager.filterNotNullPokemonParty(playerOnePokemonParty)));
        ArrayList<Pokemon> playerTwoPokemonsList = new ArrayList<>(Arrays.asList(RachamonPixelmonShowdownMatchMakingManager.filterNotNullPokemonParty(playerTwoPokemonParty)));

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

        EntityPixelmon participantOneSecondStarter = null;
        EntityPixelmon participantTwoSecondStarter = null;

        participantOneStarter = Pixelmon.storageManager
                .getParty(participant1)
                .getAndSendOutFirstAblePokemon(participant1);

        participantTwoStarter = Pixelmon.storageManager
                .getParty(participant2)
                .getAndSendOutFirstAblePokemon(participant2);

        PlayerParticipant[] playerOneParticipant = {new PlayerParticipant(participant1, participantOneStarter)};
        PlayerParticipant[] playerTwoParticipant = {new PlayerParticipant(participant2, participantTwoStarter)};

        if (ruleManager.getBattleRules().battleType == EnumBattleType.Double) {

            participantOneSecondStarter = Objects
                    .requireNonNull(Pixelmon.storageManager.getParty(participant1).get(1))
                    .getOrSpawnPixelmon(participant1);

            participantTwoSecondStarter = Objects
                    .requireNonNull(Pixelmon.storageManager.getParty(participant2).get(1))
                    .getOrSpawnPixelmon(participant2);

            playerOneParticipant = new PlayerParticipant[]{new PlayerParticipant(participant1, participantOneStarter, participantOneSecondStarter)};
            playerTwoParticipant = new PlayerParticipant[]{new PlayerParticipant(participant2, participantTwoStarter, participantTwoSecondStarter)};
        }


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
        ArrayList<UUID> nextPhase = new ArrayList<>();
        ArrayList<UUID> inQueue = queue.getInQueue();
        RachamonPixelmonShowdownEloManager eloManager = queue.getEloManager();

        for (UUID uuid : inQueue) {

            if (nextPhase.contains(uuid)) {
                continue;
            }

            int lowestMatchValue = -1;
            PlayerEloProfile playerProfile = eloManager.getProfileData(uuid);

            if (playerProfile == null) {
                eloManager.addPlayer(uuid);
                continue;
            }

            UUID opponent = null;

            for (UUID uuid2 : inQueue) {
                if (nextPhase.contains(uuid2)) {
                    continue;
                }

                if (uuid.equals(uuid2)) {
                    continue;
                }

                PlayerEloProfile opponentProfile = eloManager.getProfileData(uuid2);

                if (opponentProfile == null) {
                    eloManager.addPlayer(uuid2);
                    continue;
                }

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
                nextPhase.add(opponent);
                nextPhase.add(uuid);
                RachamonPixelmonShowdownMatchMakingManager.startPreBattle(uuid, opponent, queue.getRuleManager());
            }
        }

        for (UUID uuid : nextPhase) {
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

        if (profile == null) {
            eloManager.addPlayer(player.getUniqueId());
            throw new Exception("Adding your data to database please try again later.");
        }

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
                    .replaceAll("\\{type}", rules.isDoubleBattle() ? this.plugin
                            .getLanguage()
                            .getGeneralLanguageBattle()
                            .getTeamDoubleText() : this.plugin.getLanguage().getGeneralLanguageBattle().getTeamSingle())
                    .replaceAll("\\{level-capacity}", rules.getLevelCapacity() + "")
                    .replaceAll("\\{raise-cap}", rules.isRaiseMaxLevel() ? this.plugin
                            .getLanguage()
                            .getGeneralLanguageBattle()
                            .getYesText() : this.plugin.getLanguage().getGeneralLanguageBattle().getNoText())
                    .replaceAll("\\{team-preview}", rules.isEnableTeamPreview() ? this.plugin
                            .getLanguage()
                            .getGeneralLanguageBattle()
                            .getYesText() : this.plugin.getLanguage().getGeneralLanguageBattle().getNoText())
                    .replaceAll("\\{turn-time}", rules.getTurnTime() + " seconds.");


            Text.Builder display = Text.builder();

            if (text.contains("{hover-item-banned}")) {
                this.addHoverText(text, "\\{hover-item-banned}", display, queueService
                        .getRuleManager()
                        .getLeague()
                        .getHeldItemClause());
            }

            if (text.contains("{hover-ability-banned}")) {
                this.addHoverText(text, "\\{hover-ability-banned}", display, queueService
                        .getRuleManager()
                        .getLeague()
                        .getAbilities());
            }

            if (text.contains("{hover-move-banned}")) {
                this.addHoverText(text, "\\{hover-move-banned}", display, queueService
                        .getRuleManager()
                        .getLeague()
                        .getMoveClaus());
            }

            if (text.contains("{hover-pokemon-banned}")) {
                this.addHoverText(text, "\\{hover-pokemon-banned}", display, queueService
                        .getRuleManager()
                        .getLeague()
                        .getHeldItemClause());
            }

            if (text.contains("{hover-item-banned}") || text.contains("{hover-ability-banned}") || text.contains("{hover-move-banned}") || text.contains("{hover-pokemon-banned}")) {
                contents.add(display.build());
                continue;
            }

            contents.add(TextUtil.toText(text));
        }

        builder.contents(contents).sendTo(player);

    }

    private void addHoverText(String text, String containText, Text.Builder display, List<String> banned) {

        String[] splitText = text.split(containText);

        for (int i = 0; i < splitText.length; i++) {
            Text hover = TextUtil.toText(splitText[i]);
            display = display.append(hover);

            if (i != splitText.length - 1) {
                display = display
                        .append(TextUtil.toText(this.plugin.getLanguage().getGeneralLanguageBattle().getHoverText()))
                        .onHover(TextActions.showText(TextUtil.toText(String.join("\n", banned))));
            }
        }
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

        if (queueService.getRuleManager().getLeague().getBattleRule().isDoubleBattle() && pokemonArrayList.size() < 2) {
            throw new Exception(RachamonPixelmonShowdown
                    .getInstance()
                    .getLanguage()
                    .getGeneralLanguageBattle()
                    .getTeamDoubleError());
        }

        BattleRules rules = queueService.getRuleManager().getBattleRules();

        boolean isTeamValidate = rules.validateTeam(pokemonArrayList) == null;

        if (!isTeamValidate) {
            return;
        }

        queueService.addPlayerInQueue(player.getUniqueId());

        RachamonPixelmonShowdownMatchMakingManager.runTask();

        ChatUtil.sendMessage(player, RachamonPixelmonShowdown
                .getInstance()
                .getLanguage()
                .getGeneralLanguageBattle()
                .getPlayerEnterQueue()
                .replaceAll("\\{league}", leagueName));
    }

    public void askForDraw(Player player) throws Exception {

        EntityPlayer ePlayer = (EntityPlayer) player;
        RachamonPixelmonShowdownQueueManager queueManager = RachamonPixelmonShowdown.getInstance().getQueueManager();

        BattleControllerBase battleControllerBase = BattleRegistry.getBattle(ePlayer);
        List<PlayerParticipant> playerParticipants = battleControllerBase.getPlayers();

        UUID participant1 = playerParticipants.get(0).getEntity().getUniqueID();
        UUID participant2 = playerParticipants.get(1).getEntity().getUniqueID();

        QueueService queueService = queueManager.getPlayerInMatch(player.getUniqueId());

        if (queueService == null) {
            return;
        }

        LanguageConfig.GeneralLanguageBattle language = RachamonPixelmonShowdown
                .getInstance()
                .getLanguage()
                .getGeneralLanguageBattle();

        if (!queueService.isPlayerInMatch(participant1) || !queueService.isPlayerInMatch(participant2)) {
            throw new Exception(language.getNotInBattle());
        }

        Optional<Player> opponent = Sponge.getServer().getPlayer(participant2);

        if (!opponent.isPresent()) {
            return;
        }

        ChatUtil.sendMessage(player, language.getPlayerAskForDraw());

        ChatQuestion question = ChatQuestion
                .of(TextUtil.toText(language.getOpponentAskForDraw()))
                .addAnswer(ChatQuestionAnswer.of(TextUtil.toText(language.getAcceptText()), target -> {
                    queueService.resetPlayer(participant1);
                    queueService.resetPlayer(participant2);

                    if (BattleRegistry.getBattle(ePlayer) != null) {
                        BattleRegistry.getBattle(ePlayer).endBattle();
                    }

                    if (BattleRegistry.getBattle((EntityPlayerMP) opponent.get()) != null) {
                        BattleRegistry.getBattle((EntityPlayerMP) opponent.get()).endBattle();
                    }

                    ChatUtil.sendMessage(player, language.getSuccessfullyDraw());
                    ChatUtil.sendMessage(opponent.get(), language.getSuccessfullyDraw());
                }))
                .addAnswer(ChatQuestionAnswer.of(TextUtil.toText(language.getDeclineText()), target -> {
                    ChatUtil.sendMessage(player, language.getOpponentDecline());
                }))
                .build();

        question.setAlreadyResponse(TextUtil.toText(language.getAlreadyResponded()));
        question.setClickToAnswer(TextUtil.toText(language.getClickToAnswer()));
        question.setClickToView(TextUtil.toText(language.getClickToView()));
        question.setMustBePlayer(TextUtil.toText(language.getMustBePlayer()));

        question.pollChat(opponent.get());

    }

}
