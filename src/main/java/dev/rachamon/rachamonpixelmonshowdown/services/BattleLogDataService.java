package dev.rachamon.rachamonpixelmonshowdown.services;

import dev.rachamon.api.common.database.SQLiteConnectorProvider;
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
                boolean isSQLite = RachamonPixelmonShowdown
                        .getInstance()
                        .getDatabaseConnector() instanceof SQLiteConnectorProvider;
                statement.execute(isSQLite ? "CREATE TABLE showdown_log (" + "id INTEGER PRIMARY KEY " + autoIncrement + ", " + "winner_uuid VARCHAR(36) NOT NULL, " + "loser_uuid VARCHAR(36) NOT NULL, " + "winner_point INTEGER NOT NULL," + "loser_point INTEGER NOT NULL," + "league_type VARCHAR NOT NULL, " + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)" : "CREATE TABLE `showdown_log` (\n" +
                        "  `id` tinyint(4) DEFAULT NULL,\n" +
                        "  `winner_uuid` varchar(36) DEFAULT NULL,\n" +
                        "  `loser_uuid` varchar(36) DEFAULT NULL,\n" +
                        "  `winner_point` tinyint(4) DEFAULT NULL,\n" +
                        "  `loser_point` tinyint(4) DEFAULT NULL,\n" +
                        "  `league_type` varchar(40) DEFAULT NULL,\n" +
                        "  `created_at` varchar(0) DEFAULT NULL\n" +
                        ") ");
            } catch (Exception e) {
                RachamonPixelmonShowdown.getInstance().getLogger().error("error on initializing log database.");
            }
        });
    }

}
