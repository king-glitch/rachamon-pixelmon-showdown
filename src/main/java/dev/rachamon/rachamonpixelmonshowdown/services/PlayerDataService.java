package dev.rachamon.rachamonpixelmonshowdown.services;

import dev.rachamon.api.common.database.MySQLConnectorProvider;
import dev.rachamon.api.common.database.SQLiteConnectorProvider;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.structures.PlayerEloProfile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The type Player data service.
 */
public class PlayerDataService {
    private final String leagueName;
    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();

    /**
     * Instantiates a new Player data service.
     *
     * @param leagueName the league name
     */
    public PlayerDataService(String leagueName) {
        this.leagueName = leagueName;
    }

    /**
     * Add player.
     *
     * @param uuid     the uuid
     * @param callback the callback
     */
    public void addPlayer(UUID uuid, Consumer<PlayerEloProfile> callback) {
        this.hasPlayer(uuid, (bool) -> {

            if (bool) {
                this.plugin.getLogger().debug("getting - player data to database;");
                this.getPlayer(uuid, callback);
                return;
            }

            this.plugin.getDatabaseConnector().connect(connection -> {
                String SQL = "INSERT INTO showdown_playerdata (uuid, league_type, elo) VALUES (?,?,?)";
                try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                    statement.setString(1, uuid.toString());
                    statement.setString(2, this.leagueName);
                    statement.setInt(3, 1000);
                    statement.executeUpdate();
                    statement.close();
                    this.plugin.getLogger().debug("adding player data to database;");
                    this.getPlayer(uuid, callback);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.plugin.getLogger().error("error on add player data;");
                    this.getPlayer(uuid, callback);

                }
            });
        });
    }

    /**
     * Update player elo.
     *
     * @param uuid the uuid
     * @param elo  the elo
     */
    public void updatePlayerElo(UUID uuid, int elo) {
        this.hasPlayer(uuid, (bool) -> {
            if (!bool) {
                return;
            }

            this.plugin.getDatabaseConnector().connect(connection -> {
                String SQL = "UPDATE showdown_playerdata SET elo = ?";
                try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                    statement.setInt(1, elo);
                    statement.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                    this.plugin.getLogger().error("error on add player data;");
                }
            });
        });
    }

    /**
     * Has player.
     *
     * @param uuid     the uuid
     * @param callback the callback
     */
    public void hasPlayer(UUID uuid, Consumer<Boolean> callback) {
        this.plugin.getDatabaseConnector().connect(connection -> {
            String SQL = "SELECT COUNT(*) as amount FROM showdown_playerdata WHERE uuid = ? AND league_type = ?";
            try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, uuid.toString());
                statement.setString(2, this.leagueName);
                ResultSet result = statement.executeQuery();
                statement.close();
                if (result.next()) {
                    callback.accept(result.getInt("amount") > 0);
                } else {
                    callback.accept(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().error("error on has player data;");
            }
        });
    }

    /**
     * Gets player wins.
     *
     * @param uuid     the uuid
     * @param callback the callback
     */
    public void getPlayerWins(UUID uuid, Consumer<Integer> callback) {
        String SQL = "SELECT COUNT(*) as amount FROM showdown_log WHERE winner_uuid = ? AND league_type = ?";
        this.getWinLose(uuid, callback, SQL);
    }

    /**
     * Gets player loses.
     *
     * @param uuid     the uuid
     * @param callback the callback
     */
    public void getPlayerLoses(UUID uuid, Consumer<Integer> callback) {
        String SQL = "SELECT COUNT(*) as amount FROM showdown_log WHERE loser_uuid = ? AND league_type = ?";
        this.getWinLose(uuid, callback, SQL);
    }

    private void getWinLose(UUID uuid, Consumer<Integer> callback, String SQL) {
        this.plugin.getDatabaseConnector().connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                this.plugin.getLogger().debug("getting win lose data.");

                statement.setString(1, uuid.toString());
                statement.setString(2, leagueName);
                ResultSet result = statement.executeQuery();
                statement.close();

                if (!result.next()) {
                    this.plugin.getLogger().debug("getting win lose data not found");
                    callback.accept(0);
                    return;
                }

                int amount = result.getInt("amount");
                callback.accept(amount);
                this.plugin.getLogger().debug("getting win lose:  " + amount);
            } catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().error("error on get player wins loses;");
                callback.accept(0);
            }
        });
    }

    /**
     * Gets player.
     *
     * @param uuid     the uuid
     * @param callback the callback
     */
    public void getPlayer(UUID uuid, Consumer<PlayerEloProfile> callback) {
        String SQL = "SELECT * FROM showdown_playerdata WHERE uuid = ? AND league_type = ?";
        this.plugin.getDatabaseConnector().connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(SQL)) {

                this.plugin.getLogger().debug("getting player " + uuid + " data in database");

                statement.setString(1, uuid.toString());
                statement.setString(2, this.leagueName);
                ResultSet result = statement.executeQuery();
                statement.close();

                if (!result.next()) {
                    this.plugin.getLogger().debug("no player " + uuid + " data in database");

                    callback.accept(null);
                    return;
                }



                int elo = result.getInt("elo");

                this.plugin.getLogger().debug("done getting player " + uuid + " data in database");

                this.getPlayerWins(uuid, (w) -> {
                    this.plugin.getLogger().debug("getting wins player " + uuid + " data in database");

                    this.getPlayerLoses(uuid, l -> {
                        this.plugin.getLogger().debug("getting loses player " + uuid + " data in database");

                        this.plugin.getLogger().debug("profile: " + uuid);
                        try {
                            this.plugin
                                    .getQueueManager()
                                    .findQueue(leagueName)
                                    .getEloManager()
                                    .updatePlayer(uuid, new PlayerEloProfile(uuid, elo, w, l));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        callback.accept(new PlayerEloProfile(uuid, elo, w, l));
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().error("error on add player data;");
                callback.accept(null);
            }
        });
    }

    /**
     * Gets players.
     *
     * @param callback the callback
     */
    public void getPlayers(Consumer<Map<UUID, PlayerEloProfile>> callback) {

        this.plugin.getDatabaseConnector().connect(connection -> {
            String SQL = "SELECT * FROM showdown_playerdata WHERE league_type = ?";
            Map<UUID, PlayerEloProfile> players = new HashMap<>();
            try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, leagueName);
                ResultSet result = statement.executeQuery();
                statement.close();

                while (result.next()) {
                    String _uuid = result.getString("uuid");
                    int elo = result.getInt("elo");
                    UUID uuid = UUID.fromString(_uuid);
                    PlayerEloProfile profile = new PlayerEloProfile(uuid);
                    this.getPlayerWins(uuid, w -> {
                        this.getPlayerLoses(uuid, l -> {
                            profile.setWin(w);
                            profile.setLose(l);
                            profile.setElo(elo);
                            players.put(uuid, profile);
                        });
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().error("error on add player data;");
            }
            callback.accept(players);
        });
    }

    /**
     * Initialize database.
     */
    public static void initialize() {

        RachamonPixelmonShowdown.getInstance().getDatabaseConnector().connect(connection -> {
            try (Statement statement = connection.createStatement()) {

                boolean isSQLite = RachamonPixelmonShowdown
                        .getInstance()
                        .getDatabaseConnector() instanceof SQLiteConnectorProvider;

                statement.execute(isSQLite ? "CREATE TABLE showdown_playerdata (" + "id INTEGER PRIMARY KEY, " + "uuid VARCHAR(36) NOT NULL, " + "elo INTEGER NOT NULL," + "league_type VARCHAR NOT NULL, " + "UNIQUE (league_type, uuid) ON CONFLICT ABORT)" : "CREATE TABLE `showdown_playerdata` (" + "`id` INTEGER AUTO_INCREMENT PRIMARY KEY ," + "`uuid` varchar(36) DEFAULT NULL," + "`elo` smallint(6) DEFAULT NULL," + "`league_type` varchar(3) DEFAULT NULL," + "UNIQUE KEY(uuid, league_type)" + ");");
            } catch (Exception e) {
                e.printStackTrace();
                RachamonPixelmonShowdown.getInstance().getLogger().error("error on initializing playerdata database.");
            }
        });
    }

}
