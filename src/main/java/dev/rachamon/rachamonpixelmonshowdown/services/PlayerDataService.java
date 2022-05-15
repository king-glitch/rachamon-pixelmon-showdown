package dev.rachamon.rachamonpixelmonshowdown.services;

import dev.rachamon.api.common.database.MySQLConnectorProvider;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.structures.PlayerEloProfile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerDataService {
    private final String leagueName;
    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();

    public PlayerDataService(String leagueName) {
        this.leagueName = leagueName;
    }

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
                } catch (Exception e) {
                    e.printStackTrace();
                    this.plugin.getLogger().error("error on add player data;");
                }
            });
        });
    }

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

    public void hasPlayer(UUID uuid, Consumer<Boolean> callback) {
        this.plugin.getDatabaseConnector().connect(connection -> {
            String SQL = "SELECT COUNT(*) as amount FROM showdown_playerdata WHERE uuid = ? AND league_type = ?";
            try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, uuid.toString());
                statement.setString(2, this.leagueName);
                ResultSet result = statement.executeQuery();
                callback.accept(result.next());
            } catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().error("error on has player data;");
            }
        });
    }

    public void getPlayerWins(UUID uuid, Consumer<Integer> callback) {
        String SQL = "SELECT COUNT(*) as amount FROM showdown_log WHERE winner_uuid = ? AND league_type = ?";
        this.getWinLose(uuid, callback, SQL);
    }

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
                    callback.accept(0);
                    return;
                }
                int elo = result.getInt("elo");
                callback.accept(elo);
            } catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().error("error on get player wins;");
            }
        });
    }

    public void getPlayer(UUID uuid, Consumer<PlayerEloProfile> callback) {
        String SQL = "SELECT * FROM showdown_playerdata WHERE uuid = ? AND league_type = ?";
        this.plugin.getDatabaseConnector().connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, uuid.toString());
                statement.setString(2, leagueName);
                ResultSet result = statement.executeQuery(SQL);
                if (!result.next()) {
                    callback.accept(null);
                    return;
                }

                int elo = result.getInt("elo");

                PlayerEloProfile profile = new PlayerEloProfile(uuid, leagueName, elo);
                this.getPlayerWins(uuid, w -> {
                    profile.setWin(w);
                    this.getPlayerLoses(uuid, l -> {
                        profile.setLose(l);
                        callback.accept(profile);
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().error("error on add player data;");
            }
        });
    }

    public void getPlayers(Consumer<Map<UUID, PlayerEloProfile>> callback) {
        String SQL = "SELECT * FROM showdown_playerdata WHERE league_type = ?";
        this.plugin.getDatabaseConnector().connect(connection -> {
            Map<UUID, PlayerEloProfile> players = new HashMap<>();
            try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, leagueName);
                ResultSet result = statement.executeQuery(SQL);
                while (result.next()) {
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
                }

                callback.accept(players);


            } catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().error("error on add player data;");
            }
        });
    }

    public static void initializeDatabase() {
        String autoIncrement = RachamonPixelmonShowdown
                .getInstance()
                .getDatabaseConnector() instanceof MySQLConnectorProvider ? "AUTO_INCREMENT" : "";

        RachamonPixelmonShowdown.getInstance().getDatabaseConnector().connect(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE showdown_playerdata (" + "id INTEGER PRIMARY KEY " + autoIncrement + ", " + "uuid VARCHAR(36) NOT NULL, " + "elo INTEGER NOT NULL," + "league_type VARCHAR NOT NULL)");
            } catch (Exception e) {
                e.printStackTrace();
                RachamonPixelmonShowdown.getInstance().getLogger().error("error on initializing playerdata database.");
            }

            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE showdown_log (" + "id INTEGER PRIMARY KEY " + autoIncrement + ", " + "winner_uuid VARCHAR(36) NOT NULL, " + "loser_uuid VARCHAR(36) NOT NULL, " + "winner_point INTEGER NOT NULL," + "loser_point INTEGER NOT NULL," + "league_type VARCHAR NOT NULL, " + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
            } catch (Exception e) {
                e.printStackTrace();
                RachamonPixelmonShowdown.getInstance().getLogger().error("error on initializing log database.");
            }
        });
    }

}
