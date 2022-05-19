package dev.rachamon.rachamonpixelmonshowdown.listeners;

import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownEloManager;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownQueueManager;
import dev.rachamon.rachamonpixelmonshowdown.services.BattleLogDataService;
import dev.rachamon.rachamonpixelmonshowdown.services.QueueService;
import dev.rachamon.rachamonpixelmonshowdown.structures.PlayerEloProfile;
import dev.rachamon.rachamonpixelmonshowdown.utils.BattleUtil;
import dev.rachamon.rachamonpixelmonshowdown.utils.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The type Battle listener.
 */
public class BattleListener {

    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();

    /**
     * On battle end.
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event) {

        BattleParticipant playerParticipant1 = event.results.keySet().asList().get(0);
        BattleParticipant playerParticipant2 = event.results.keySet().asList().get(1);

        if (!(playerParticipant1 instanceof PlayerParticipant) || !(playerParticipant2 instanceof PlayerParticipant)) {
            return;
        }

        PlayerParticipant participant1 = (PlayerParticipant) playerParticipant1;
        PlayerParticipant participant2 = (PlayerParticipant) playerParticipant2;

        Player _entityPlayerOne = (Player) participant1.getEntity();
        Player _entityPlayerTwo = (Player) participant2.getEntity();

        UUID playerOneUuid = _entityPlayerOne.getUniqueId();
        UUID playerTwoUuid = _entityPlayerTwo.getUniqueId();

        EntityPlayerMP entityPlayerOne = (EntityPlayerMP) participant1.getEntity();
        EntityPlayerMP entityPlayerTwo = (EntityPlayerMP) participant2.getEntity();

        if (BattleRegistry.getBattle(entityPlayerOne) != null) {
            BattleControllerBase base = BattleRegistry.getBattle(entityPlayerOne);
            BattleRegistry.deRegisterBattle(base);
        }

        RachamonPixelmonShowdownQueueManager queueManager = this.plugin.getQueueManager();

        if (!queueManager.isPlayerInMatch(playerOneUuid) || !queueManager.isPlayerInMatch(playerTwoUuid)) {
            this.plugin.getLogger().debug("player not in match");
            return;
        }

        try {
            QueueService queue = queueManager.getPlayerInMatch(playerOneUuid);

            RachamonPixelmonShowdownEloManager eloManager = queue.getEloManager();

            if (!queue.isPlayerInMatch(playerTwoUuid)) {
                this.plugin.getLogger().debug("player 2 not in match");
                return;
            }

            if (event.abnormal || event.cause == EnumBattleEndCause.FORCE) {

                Task.builder().execute(() -> {
                    this.runPostBattle(true, event, participant1, playerOneUuid, playerTwoUuid, _entityPlayerOne, _entityPlayerTwo, queue, eloManager);

                    if (BattleRegistry.getBattle(entityPlayerOne) != null) {
                        BattleRegistry.getBattle(entityPlayerOne).endBattle();
                    }

                    if (BattleRegistry.getBattle(entityPlayerTwo) != null) {
                        BattleRegistry.getBattle(entityPlayerTwo).endBattle();
                    }

                }).delay(500, TimeUnit.MICROSECONDS).submit(RachamonPixelmonShowdown.getInstance());

                return;
            }

            this.runPostBattle(false, event, participant1, playerOneUuid, playerTwoUuid, _entityPlayerOne, _entityPlayerTwo, queue, eloManager);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * On player quit.
     *
     * @param event the event
     */
    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
        RachamonPixelmonShowdownQueueManager queueManager = RachamonPixelmonShowdown.getInstance().getQueueManager();
        UUID uuid = event.getTargetEntity().getUniqueId();

        if (!queueManager.isPlayerInAction(uuid)) {
            return;
        }

        QueueService queueService = queueManager.getPlayerInAction(uuid);
        if (queueService == null) {
            return;
        }

        queueService.resetPlayer(uuid);
    }

    /**
     * On command send.
     *
     * @param event the event
     */
    @Listener
    public void onCommandSend(SendCommandEvent event) {
        if (!event.getCommand().equalsIgnoreCase("endbattle")) {
            return;
        }

        if (!(event.getSource() instanceof Player)) {
            return;
        }

        RachamonPixelmonShowdownQueueManager queueManager = RachamonPixelmonShowdown.getInstance().getQueueManager();

        Player loser = (Player) event.getSource();
        UUID loserUuid = loser.getUniqueId();

        Player winner;
        UUID winnerUuid;

        if (!queueManager.isPlayerInMatch(loserUuid)) {
            return;
        }

        try {
            QueueService queueService = queueManager.getPlayerInMatch(loserUuid);

            EntityPlayer ePlayer = (EntityPlayer) loser;
            BattleControllerBase battleControllerBase = BattleRegistry.getBattle(ePlayer);
            List<PlayerParticipant> playerParticipants = battleControllerBase.getPlayers();

            UUID participant1 = ((Player) playerParticipants.get(0).getEntity()).getUniqueId();
            UUID participant2 = ((Player) playerParticipants.get(1).getEntity()).getUniqueId();

            if (loser.getUniqueId().equals(participant1)) {
                winner = Sponge.getServer().getPlayer(participant2).orElse(null);
            } else {
                winner = Sponge.getServer().getPlayer(participant1).orElse(null);
            }

            if (winner == null) {
                return;
            }

            winnerUuid = winner.getUniqueId();

            RachamonPixelmonShowdownEloManager eloManager = queueService.getEloManager();

            PlayerEloProfile eloLoserProfile = eloManager.getProfileData(loserUuid);
            PlayerEloProfile eloWinnerProfile = eloManager.getProfileData(winnerUuid);

            this.processElo(winner, winnerUuid, loser, loserUuid, queueService, eloManager, eloWinnerProfile, eloLoserProfile);
        } catch (Exception ignored) {

        }
    }

    private void processElo(Player winner, UUID winnerUuid, Player loser, UUID loserUuid, QueueService queueService, RachamonPixelmonShowdownEloManager eloManager, PlayerEloProfile eloWinnerProfile, PlayerEloProfile eloLoserProfile) {
        int winnerElo = eloWinnerProfile.getElo();
        int loserElo = eloLoserProfile.getElo();

        int newWinnerElo = BattleUtil.calculateWinElo(winnerElo, loserElo);
        int newLoserElo = BattleUtil.calculateLoseElo(loserElo, winnerElo);

        BattleLogDataService.addLog(winner.getUniqueId(), loser.getUniqueId(), Math.abs(newWinnerElo - winnerElo), Math.abs(loserElo - newLoserElo), queueService
                .getRuleManager()
                .getLeagueName());

        ChatUtil.sendMessage(winner, RachamonPixelmonShowdown
                .getInstance()
                .getLanguage()
                .getGeneralLanguageBattle()
                .getWinMessage()
                .replaceAll("\\{current}", String.valueOf(winnerElo))
                .replaceAll("\\{new-elo}", String.valueOf(newWinnerElo))
                .replaceAll("\\{player}", loser.getName()));

        ChatUtil.sendMessage(loser, RachamonPixelmonShowdown
                .getInstance()
                .getLanguage()
                .getGeneralLanguageBattle()
                .getLoseMessage()
                .replaceAll("\\{current}", String.valueOf(loserElo))
                .replaceAll("\\{new-elo}", String.valueOf(newLoserElo))
                .replaceAll("\\{player}", winner.getName()));

        queueService.removePlayerInMatch(winnerUuid);
        queueService.removePlayerInMatch(loserUuid);

        eloWinnerProfile.setElo(newWinnerElo);
        eloWinnerProfile.setWin(eloWinnerProfile.getWin() + 1);

        eloLoserProfile.setElo(newLoserElo);
        eloLoserProfile.setLose(eloLoserProfile.getLose() + 1);


        eloManager.updatePlayer(winnerUuid, eloWinnerProfile);
        eloManager.updatePlayer(loserUuid, eloLoserProfile);

        eloManager.sort();
    }

    private void runPostBattle(boolean force, BattleEndEvent event, PlayerParticipant participant1, UUID playerUuid1, UUID playerUuid2, Player player1, Player player2, QueueService queue, RachamonPixelmonShowdownEloManager eloManager) {
        boolean isPlayerOneWin = event.results.get(participant1) == BattleResults.VICTORY;

        if (force) {
            isPlayerOneWin = !Sponge.getServer().getPlayer(playerUuid2).isPresent();
        }

        PlayerEloProfile eloWinner = isPlayerOneWin ? eloManager.getProfileData(playerUuid1) : eloManager.getProfileData(playerUuid2);
        PlayerEloProfile eloLoser = isPlayerOneWin ? eloManager.getProfileData(playerUuid2) : eloManager.getProfileData(playerUuid1);
        Player winner = isPlayerOneWin ? player1 : player2;
        Player loser = isPlayerOneWin ? player2 : player1;


        this.processElo(winner, winner.getUniqueId(), loser, loser.getUniqueId(), queue, eloManager, eloWinner, eloLoser);
    }

}
