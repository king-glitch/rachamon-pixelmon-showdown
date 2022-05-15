package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.services.QueueService;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RachamonPixelmonShowdownQueueManager {

    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();
    private final Map<String, QueueService> queue = new HashMap<String, QueueService>();

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

    public void addQueue(String leagueName, QueueService queueService) {
        this.queue.put(leagueName, queueService);
    }

    public void removeQueue(String leagueName) {
        if (!this.queue.containsKey(leagueName)) {
            return;
        }

        this.queue.remove(leagueName);
    }

    public boolean isPlayerInQueue(UUID uuid) {
        return this.queue.values().stream().anyMatch(q -> q.isPlayerInQueue(uuid));
    }

    public boolean isPlayerInPreMatch(UUID uuid) {
        return this.queue.values().stream().anyMatch(q -> q.isPlayerInPreMatch(uuid));
    }

    public boolean isPlayerInMatch(UUID uuid) {
        return this.queue.values().stream().anyMatch(q -> q.isPlayerInMatch(uuid));
    }

    public boolean isPlayerInAction(UUID uuid) {
        return this.queue.values().stream().anyMatch(q -> q.isPlayerInAction(uuid));
    }

    @Nullable
    public QueueService getPlayerInMatch(UUID uuid) {
        return this.queue.values().stream().filter(v -> v.isPlayerInMatch(uuid)).findFirst().orElse(null);
    }

    @Nullable
    public QueueService getPlayerInPreMatch(UUID uuid) {
        return this.queue.values().stream().filter(v -> v.isPlayerInPreMatch(uuid)).findFirst().orElse(null);
    }

    @Nullable
    public QueueService getPlayerInQueue(UUID uuid) {
        return this.queue.values().stream().filter(v -> v.isPlayerInQueue(uuid)).findFirst().orElse(null);
    }

    @Nullable
    public QueueService getPlayerInAction(UUID uuid) {
        return this.queue.values().stream().filter(v -> v.isPlayerInAction(uuid)).findFirst().orElse(null);
    }

    public Map<String, QueueService> getQueue() {
        return queue;
    }
}
