package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.services.QueueService;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The type Rachamon pixelmon showdown queue manager.
 */
public class RachamonPixelmonShowdownQueueManager {

    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();
    private final Map<String, QueueService> queue = new HashMap<String, QueueService>();

    /**
     * Initialize queue.
     *
     * @throws Exception the exception
     */
    public void initializeQueue() throws Exception {
        this.queue.clear();
        for (String leagueName : this.plugin.getLeague().getLeagues().keySet()) {
            RachamonPixelmonShowdownRuleManager ruleManager = new RachamonPixelmonShowdownRuleManager(leagueName)
                    .load()
                    .build();

            RachamonPixelmonShowdownEloManager eloManager = new RachamonPixelmonShowdownEloManager(leagueName).initialize();
            QueueService queueService = new QueueService(ruleManager, eloManager);
            this.addQueue(leagueName, queueService);
        }
    }

    /**
     * Find queue queue service.
     *
     * @param leagueName the league name
     * @return the queue service
     * @throws Exception the exception
     */
    public QueueService findQueue(String leagueName) throws Exception {
        QueueService queueService = this.queue.get(leagueName);
        if (queueService == null) {
            throw new Exception(RachamonPixelmonShowdown
                    .getInstance()
                    .getLanguage()
                    .getGeneralLanguageBattle()
                    .getLeagueNotFound());
        }

        return queueService;
    }

    /**
     * Add queue.
     *
     * @param leagueName   the league name
     * @param queueService the queue service
     */
    public void addQueue(String leagueName, QueueService queueService) {
        this.queue.put(leagueName, queueService);
    }

    /**
     * Remove queue.
     *
     * @param leagueName the league name
     */
    public void removeQueue(String leagueName) {
        if (!this.queue.containsKey(leagueName)) {
            return;
        }

        this.queue.remove(leagueName);
    }

    /**
     * Is player in queue boolean.
     *
     * @param uuid the uuid
     * @return the boolean
     */
    public boolean isPlayerInQueue(UUID uuid) {
        return this.queue.values().stream().anyMatch(q -> q.isPlayerInQueue(uuid));
    }

    /**
     * Is player in pre match boolean.
     *
     * @param uuid the uuid
     * @return the boolean
     */
    public boolean isPlayerInPreMatch(UUID uuid) {
        return this.queue.values().stream().anyMatch(q -> q.isPlayerInPreMatch(uuid));
    }

    /**
     * Is player in match boolean.
     *
     * @param uuid the uuid
     * @return the boolean
     */
    public boolean isPlayerInMatch(UUID uuid) {
        return this.queue.values().stream().anyMatch(q -> q.isPlayerInMatch(uuid));
    }

    /**
     * Is player in action boolean.
     *
     * @param uuid the uuid
     * @return the boolean
     */
    public boolean isPlayerInAction(UUID uuid) {
        return this.queue.values().stream().anyMatch(q -> q.isPlayerInAction(uuid));
    }

    /**
     * Gets player in match.
     *
     * @param uuid the uuid
     * @return the player in match
     */
    @Nullable
    public QueueService getPlayerInMatch(UUID uuid) {
        return this.queue.values().stream().filter(v -> v.isPlayerInMatch(uuid)).findFirst().orElse(null);
    }

    /**
     * Gets player in pre match.
     *
     * @param uuid the uuid
     * @return the player in pre match
     */
    @Nullable
    public QueueService getPlayerInPreMatch(UUID uuid) {
        return this.queue.values().stream().filter(v -> v.isPlayerInPreMatch(uuid)).findFirst().orElse(null);
    }

    /**
     * Gets player in queue.
     *
     * @param uuid the uuid
     * @return the player in queue
     */
    @Nullable
    public QueueService getPlayerInQueue(UUID uuid) {
        return this.queue.values().stream().filter(v -> v.isPlayerInQueue(uuid)).findFirst().orElse(null);
    }

    /**
     * Gets player in action.
     *
     * @param uuid the uuid
     * @return the player in action
     */
    @Nullable
    public QueueService getPlayerInAction(UUID uuid) {
        return this.queue.values().stream().filter(v -> v.isPlayerInAction(uuid)).findFirst().orElse(null);
    }

    /**
     * Gets queue.
     *
     * @return the queue
     */
    public Map<String, QueueService> getQueue() {
        return queue;
    }
}
