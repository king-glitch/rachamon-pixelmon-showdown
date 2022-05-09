package dev.rachamon.rachamonpixelmonshowdown.services;

import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownRuleManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class QueueService {

    private final RachamonPixelmonShowdownRuleManager ruleManager;
    private final ArrayList<UUID> inQueue = new ArrayList<>();
    private final ArrayList<UUID> inPreMatch = new ArrayList<>();
    private final ArrayList<UUID> inMatch = new ArrayList<>();

    public QueueService(RachamonPixelmonShowdownRuleManager ruleManager) {
        this.ruleManager = ruleManager;
    }

    public boolean isPlayerInQueue(UUID uuid) {
        return this.inQueue.contains(uuid);
    }

    public boolean isPlayerInPreMatch(UUID uuid) {
        return this.inPreMatch.contains(uuid);
    }

    public boolean isPlayerInMatch(UUID uuid) {
        return this.inMatch.contains(uuid);
    }

    public boolean isPlayerInAction(UUID uuid) {
        return this.isPlayerInMatch(uuid) || this.isPlayerInPreMatch(uuid) || this.isPlayerInMatch(uuid);
    }

    public void addPlayerInQueue(UUID uuid) throws Exception {
        Optional<Player> player = Sponge.getServer().getPlayer(uuid);

        if (!player.isPresent()) {
            throw new Exception("Player not found");
        }

        if (this.isPlayerInAction(uuid)) {
            return;
        }

        // TODO: add elo

        this.inQueue.add(uuid);
    }

    public void addPlayerInPreMatch(UUID uuid) {

        if (!this.isPlayerInQueue(uuid)) {
            return;
        }

        if (this.isPlayerInMatch(uuid) || this.isPlayerInPreMatch(uuid)) {
            return;
        }

        // TODO: add elo

        this.inPreMatch.add(uuid);
        this.inQueue.remove(uuid);

    }

    public void addPlayerInMatch(UUID uuid) {
        if (!this.isPlayerInPreMatch(uuid)) {
            return;
        }

        if (this.isPlayerInMatch(uuid) || this.isPlayerInQueue(uuid)) {
            return;
        }

        this.inMatch.add(uuid);
        this.inPreMatch.remove(uuid);
    }

    public void removePlayerInQueue(UUID uuid) {
        if (!this.isPlayerInQueue(uuid)) {
            return;
        }
        this.inQueue.remove(uuid);
    }

    public void removePlayerInPreMatch(UUID uuid) {
        if (!this.isPlayerInPreMatch(uuid)) {
            return;
        }
        this.inPreMatch.remove(uuid);
    }

    public void removePlayerInMatch(UUID uuid) {
        if (!this.isPlayerInMatch(uuid)) {
            return;
        }
        this.inMatch.remove(uuid);
    }

    public void resetPlayer(UUID uuid) {
        this.removePlayerInQueue(uuid);
        this.removePlayerInMatch(uuid);
        this.removePlayerInPreMatch(uuid);
    }

    public ArrayList<UUID> getInQueue() {
        return this.getInQueue();
    }

    public RachamonPixelmonShowdownRuleManager getRuleManager() {
        return this.ruleManager;
    }

}
