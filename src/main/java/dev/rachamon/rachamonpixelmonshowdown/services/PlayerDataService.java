package dev.rachamon.rachamonpixelmonshowdown.services;

import dev.rachamon.api.common.database.MySQLConnectorProvider;
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
     * @param uuid the uuid
     */
    public void addPlayer(UUID uuid) {
        this.hasPlayer(uuid, (bool) -> {
            if (bool) {
                return;
            }

            this.plugin.getDatabaseConnector().connect(connection -> {
                String SQL = "INSERT INTO showdown_playerdata (uuid, league_type, elo) VALUES (?,?,?)";
                try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                    statement.setString(1, uuid.toString());
                    statement.setString(2, this.leagueName);
                    statement.setInt(3, 1000);
                    statement.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                    this.plugin.getLogger().error("error on add player data;");
                }
            });
        });
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
                    this.getPlayer(uuid, callback);
                    this.plugin.getLogger().debug("adding player data to database;");
                } catch (Exception e) {
                    this.getPlayer(uuid, callback);
                    this.plugin.getLogger().error("error on add player data;");
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
                if (result.next()) {
                    callback.accept(result.getInt("amount") > 0);
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
                statement.setString(1, uuid.toString());
                statement.setString(2, leagueName);
                ResultSet result = statement.executeQuery();
                if (!result.next()) {
                    this.plugin.getLogger().debug("getting win lose data not found");
                    callback.accept(0);
                    return;
                }
                int amount = result.getInt("amount");
                callback.accept(amount);
                this.plugin.getLogger().debug("getting win lose:  " + amount);
            } catch (Exception e) {
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
        this.plugin.getLogger().debug("getting player " + uuid + " data in database");
        String SQL = "SELECT * FROM showdown_playerdata WHERE uuid = ? AND league_type = ?";
        this.plugin.getDatabaseConnector().connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, uuid.toString());
                statement.setString(2, this.leagueName);
                ResultSet result = statement.executeQuery();
                if (!result.next()) {
                    callback.accept(null);
                    return;
                }

                int elo = result.getInt("elo");

                this.getPlayerWins(uuid, (w) -> {
                    this.getPlayerLoses(uuid, l -> {
                        this.plugin.getLogger().debug("profile: " + uuid);
                        callback.accept(new PlayerEloProfile(uuid, this.leagueName, elo, w, l));
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().error("error on add player data;");
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
            String SQL = "SELECT * FROM showdown_playerdata WHERE league_type = ? AND uuid = ?";
            Map<UUID, PlayerEloProfile> players = new HashMap<>();

            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                    statement.setString(1, leagueName);
                    statement.setString(2, player.getUniqueId().toString());

                    ResultSet result = statement.executeQuery();
                    if (result.next()) {
                        String _uuid = result.getString("uuid");
                        int elo = result.getInt("elo");

                        UUID uuid = UUID.fromString(_uuid);

                        PlayerEloProfile profile = new PlayerEloProfile(uuid, leagueName, elo);
                        this.getPlayerWins(uuid, w -> {
                            profile.setWin(w);
                            this.getPlayerLoses(uuid, l -> {
                                profile.setLose(l);
                                players.put(uuid, profile);
                            });
                        });
                    } else {
                        this.addPlayer(player.getUniqueId(), (p) -> players.put(player.getUniqueId(), p));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    this.plugin.getLogger().error("error on add player data;");
                }
            }
            callback.accept(players);


        });
    }

    /**
     * Initialize database.
     */
    public static void initialize() {
        String autoIncrement = RachamonPixelmonShowdown
                .getInstance()
                .getDatabaseConnector() instanceof MySQLConnectorProvider ? "AUTO_INCREMENT" : "";

        RachamonPixelmonShowdown.getInstance().getDatabaseConnector().connect(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE showdown_playerdata (" + "id INTEGER PRIMARY KEY " + autoIncrement + ", " + "uuid VARCHAR(36) NOT NULL, " + "elo INTEGER NOT NULL," + "league_type VARCHAR NOT NULL, " + "UNIQUE (league_type, uuid) ON CONFLICT ABORT)");
            } catch (Exception e) {
                RachamonPixelmonShowdown.getInstance().getLogger().error("error on initializing playerdata database.");
            }
        });
    }

}
