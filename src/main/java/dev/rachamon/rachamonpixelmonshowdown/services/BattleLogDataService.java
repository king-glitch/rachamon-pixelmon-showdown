package dev.rachamon.rachamonpixelmonshowdown.services;

import dev.rachamon.api.common.database.MySQLConnectorProvider;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

public class BattleLogDataService {

    public static void addLog(UUID winner, UUID loser, int winnerPoint, int loserPoint, String leagueName) {
        RachamonPixelmonShowdown.getInstance().getDatabaseConnector().connect(connection -> {
            String SQL = "INSERT INTO showdown_log (winner_uuid, loser_uuid, winner_point, loser_point, league_type) VALUES (?,?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, winner.toString());
                statement.setString(2, loser.toString());
                statement.setInt(3, winnerPoint);
                statement.setInt(4, loserPoint);
                statement.setString(5, leagueName);
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                RachamonPixelmonShowdown.getInstance().getLogger().error("error on add player data;");
            }
        });
    }

    public static void initialize() {
        String autoIncrement = "";

        RachamonPixelmonShowdown.getInstance().getDatabaseConnector().connect(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE showdown_log (" + "id INTEGER PRIMARY KEY " + autoIncrement + ", " + "winner_uuid VARCHAR(36) NOT NULL, " + "loser_uuid VARCHAR(36) NOT NULL, " + "winner_point INTEGER NOT NULL," + "loser_point INTEGER NOT NULL," + "league_type VARCHAR NOT NULL, " + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
            } catch (Exception e) {
                e.printStackTrace();
                RachamonPixelmonShowdown.getInstance().getLogger().error("error on initializing log database.");
            }
        });
    }

}
