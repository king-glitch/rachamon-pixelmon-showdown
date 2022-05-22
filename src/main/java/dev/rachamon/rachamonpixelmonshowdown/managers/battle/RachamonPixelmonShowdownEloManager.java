package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.services.PlayerDataService;
import dev.rachamon.rachamonpixelmonshowdown.structures.PlayerEloProfile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Rachamon pixelmon showdown elo manager.
 */
public class RachamonPixelmonShowdownEloManager {
    private Map<UUID, PlayerEloProfile> cache = new HashMap<UUID, PlayerEloProfile>();
    private ArrayList<UUID> ranks = new ArrayList<>();
    private final PlayerDataService playerDataService;
    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();

    /**
     * Instantiates a new Rachamon pixelmon showdown elo manager.
     *
     * @param leagueName the league name
     */
    public RachamonPixelmonShowdownEloManager(String leagueName) {
        this.playerDataService = new PlayerDataService(leagueName);
    }

    /**
     * Initialize rachamon pixelmon showdown elo manager.
     *
     * @return the rachamon pixelmon showdown elo manager
     */
    public RachamonPixelmonShowdownEloManager initialize() {
        this.playerDataService.getPlayers((cache) -> {
            this.cache = cache;
            this.ranks = new ArrayList<UUID>(cache.keySet());
            this.sort();
        });

        return this;
    }

    /**
     * Sort.
     */
    public void sort() {
        Stream<Map.Entry<UUID, PlayerEloProfile>> sorted = this.cache
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getValue().getElo()));
        this.ranks = sorted.map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Add player.
     *
     * @param uuid the uuid
     */
    public void addPlayer(UUID uuid) {
        this.playerDataService.addPlayer(uuid, profile -> {
            this.cache.put(uuid, profile);
            this.sort();
        });
    }

    /**
     * Gets player rank.
     *
     * @param uuid the uuid
     * @return the player rank
     */
    public int getPlayerRank(UUID uuid) {
        return this.ranks.indexOf(uuid);
    }

    /**
     * Update player.
     *
     * @param uuid    the uuid
     * @param profile the profile
     */
    public void updatePlayer(UUID uuid, PlayerEloProfile profile) {
        if (!this.hasPlayer(uuid)) {
            return;
        }

        this.cache.put(uuid, profile);
        this.playerDataService.updatePlayerElo(uuid, profile.getElo());
        this.sort();
    }

    /**
     * Gets profile data.
     *
     * @param uuid the uuid
     * @return the profile data
     */
    public PlayerEloProfile getProfileData(UUID uuid) {
        return this.cache.get(uuid);
    }

    /**
     * Gets cache.
     *
     * @return the cache
     */
    public Map<UUID, PlayerEloProfile> getCache() {
        return this.cache;
    }

    /**
     * Has player boolean.
     *
     * @param uuid the uuid
     * @return the boolean
     */
    public boolean hasPlayer(UUID uuid) {
        return this.cache.containsKey(uuid);
    }

    /**
     * Gets ranks.
     *
     * @return the ranks
     */
    public ArrayList<UUID> getRanks() {
        return ranks;
    }
}
