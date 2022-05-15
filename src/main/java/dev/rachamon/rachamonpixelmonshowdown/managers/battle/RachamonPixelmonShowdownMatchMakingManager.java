package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import dev.rachamon.api.sponge.util.TextUtil;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.services.QueueService;
import dev.rachamon.rachamonpixelmonshowdown.structures.PlayerEloProfile;
import dev.rachamon.rachamonpixelmonshowdown.utils.ChatUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

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

    public static void startBattle(UUID uuid1, ArrayList<Pokemon> PlayerOnePokemons, Pokemon PlayerOneStarterPokemon, UUID uuid2, ArrayList<Pokemon> PlayerTwoPokemons, Pokemon PlayerTwoStarterPokemon, RachamonPixelmonShowdownRuleManager ruleManager) {

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
    }

    public void enterQueue(String leagueName, EntityPlayerMP participant) throws Exception {

        Pokemon[] party = (Pokemon[]) Arrays
                .stream(Pixelmon.storageManager.getParty(participant).getAll())
                .filter(Objects::nonNull)
                .toArray();
        ArrayList<Pokemon> pokemonArrayList = new ArrayList<>(Arrays.asList(party));

        QueueService queueService = this.plugin.getQueueManager().findQueue(leagueName);

        if (queueService == null) {
            return;
        }

        BattleRules rules = queueService.getRuleManager().getBattleRules();

        boolean isTeamValidate = rules.validateTeam(pokemonArrayList) == null;

        if (!isTeamValidate) {
            return;
        }

        queueService.addPlayerInQueue(participant.getUniqueID());

    }

}
