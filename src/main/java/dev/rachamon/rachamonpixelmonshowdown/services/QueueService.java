package dev.rachamon.rachamonpixelmonshowdown.services;

import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownEloManager;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownRuleManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * The type Queue service.
 */
public class QueueService {

    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();
    private final RachamonPixelmonShowdownRuleManager ruleManager;
    private final RachamonPixelmonShowdownEloManager eloManager;
    private final ArrayList<UUID> inQueue = new ArrayList<>();
    private final ArrayList<UUID> inPreMatch = new ArrayList<>();
    private final ArrayList<UUID> inMatch = new ArrayList<>();

    /**
     * Instantiates a new Queue service.
     *
     * @param ruleManager the rule manager
     * @param eloManager  the elo manager
     */
    public QueueService(RachamonPixelmonShowdownRuleManager ruleManager, RachamonPixelmonShowdownEloManager eloManager) {
        this.ruleManager = ruleManager;
        this.eloManager = eloManager;
    }

    /**
     * Is player in queue boolean.
     *
     * @param uuid the uuid
     * @return the boolean
     */
    public boolean isPlayerInQueue(UUID uuid) {
        return this.inQueue.contains(uuid);
    }

    /**
     * Is player in pre match boolean.
     *
     * @param uuid the uuid
     * @return the boolean
     */
    public boolean isPlayerInPreMatch(UUID uuid) {
        return this.inPreMatch.contains(uuid);
    }

    /**
     * Is player in match boolean.
     *
     * @param uuid the uuid
     * @return the boolean
     */
    public boolean isPlayerInMatch(UUID uuid) {
        return this.inMatch.contains(uuid);
    }

    /**
     * Is player in action boolean.
     *
     * @param uuid the uuid
     * @return the boolean
     */
    public boolean isPlayerInAction(UUID uuid) {
        return this.isPlayerInQueue(uuid) || this.isPlayerInPreMatch(uuid) || this.isPlayerInMatch(uuid);
    }

    /**
     * Add player in queue.
     *
     * @param uuid the uuid
     * @throws Exception the exception
     */
    public void addPlayerInQueue(UUID uuid) throws Exception {
        Optional<Player> player = Sponge.getServer().getPlayer(uuid);

        if (!player.isPresent()) {
            throw new Exception("Player not found");
        }

        if (this.isPlayerInAction(uuid)) {
            this.plugin.getLogger().debug("adding " + uuid + " already in a queue");

            return;
        }

        if (!this.eloManager.hasPlayer(uuid)) {
            this.plugin.getLogger().debug("adding " + uuid + " to database");
            this.eloManager.addPlayer(uuid);
        }

        this.inQueue.add(uuid);
    }

    /**
     * Add player in pre match.
     *
     * @param uuid the uuid
     */
    public void addPlayerInPreMatch(UUID uuid) {

        if (!this.isPlayerInQueue(uuid)) {
            return;
        }

        if (this.isPlayerInMatch(uuid) || this.isPlayerInPreMatch(uuid)) {
            return;
        }

        this.inPreMatch.add(uuid);
        this.inQueue.remove(uuid);

    }

    /**
     * Add player in match.
     *
     * @param uuid the uuid
     */
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

    /**
     * Remove player in queue.
     *
     * @param uuid the uuid
     */
    public void removePlayerInQueue(UUID uuid) {
        if (!this.isPlayerInQueue(uuid)) {
            return;
        }

        this.inQueue.remove(uuid);
    }

    /**
     * Remove player in pre match.
     *
     * @param uuid the uuid
     */
    public void removePlayerInPreMatch(UUID uuid) {
        if (!this.isPlayerInPreMatch(uuid)) {
            return;
        }

        this.inPreMatch.remove(uuid);
    }

    /**
     * Remove player in match.
     *
     * @param uuid the uuid
     */
    public void removePlayerInMatch(UUID uuid) {
        if (!this.isPlayerInMatch(uuid)) {
            return;
        }

        this.inMatch.remove(uuid);
    }

    /**
     * Reset player.
     *
     * @param uuid the uuid
     */
    public void resetPlayer(UUID uuid) {
        this.removePlayerInQueue(uuid);
        this.removePlayerInMatch(uuid);
        this.removePlayerInPreMatch(uuid);
    }

    /**
     * Gets in queue.
     *
     * @return the in queue
     */
    public ArrayList<UUID> getInQueue() {
        return this.inQueue;
    }

    /**
     * Gets rule manager.
     *
     * @return the rule manager
     */
    public RachamonPixelmonShowdownRuleManager getRuleManager() {
        return this.ruleManager;
    }

    /**
     * Gets elo manager.
     *
     * @return the elo manager
     */
    public RachamonPixelmonShowdownEloManager getEloManager() {
        return eloManager;
    }
}
