package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.services.PlayerDataService;
import dev.rachamon.rachamonpixelmonshowdown.structures.PlayerEloProfile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RachamonPixelmonShowdownEloManager {
    private Map<UUID, PlayerEloProfile> cache = new HashMap<UUID, PlayerEloProfile>();
    private ArrayList<UUID> ranks = new ArrayList<>();
    private final PlayerDataService playerDataService;
    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();

    public RachamonPixelmonShowdownEloManager(String leagueName) {
        this.playerDataService = new PlayerDataService(leagueName);
    }

    public RachamonPixelmonShowdownEloManager initialize() {
        this.playerDataService.getPlayers((cache) -> {
            this.cache = cache;
            this.ranks = new ArrayList<UUID>(cache.keySet());
            this.sort();
        });

        return this;
    }

    public void sort() {
        Stream<Map.Entry<UUID, PlayerEloProfile>> sorted = this.cache
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getValue().getElo()));
        this.ranks = sorted.map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addPlayer(UUID uuid) {
        if (this.hasPlayer(uuid)) {
            return;
        }

        this.playerDataService.addPlayer(uuid, profile -> {
            this.cache.put(uuid, profile);
        });
    }

    public int getPlayerRank(UUID uuid) {
        return this.ranks.indexOf(uuid);
    }

    public void updatePlayer(UUID uuid, PlayerEloProfile profile) {
        if (!this.hasPlayer(uuid)) {
            return;
        }

        this.cache.put(uuid, profile);
        this.playerDataService.updatePlayerElo(uuid, profile.getElo());
    }

    public PlayerEloProfile getProfileData(UUID uuid) {
        return this.cache.get(uuid);
    }

    public Map<UUID, PlayerEloProfile> getCache() {
        return this.cache;
    }

    public boolean hasPlayer(UUID uuid) {
        return this.cache.containsKey(uuid);
    }

}
